package com.project.web.rest;

import static com.project.domain.CusNoteAsserts.*;
import static com.project.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.IntegrationTest;
import com.project.domain.CusNote;
import com.project.repository.CusNoteRepository;
import com.project.service.dto.CusNoteDTO;
import com.project.service.mapper.CusNoteMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CusNoteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CusNoteResourceIT {

    private static final String DEFAULT_CREATE_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATE_BY = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/cus-notes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CusNoteRepository cusNoteRepository;

    @Autowired
    private CusNoteMapper cusNoteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCusNoteMockMvc;

    private CusNote cusNote;

    private CusNote insertedCusNote;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CusNote createEntity() {
        return new CusNote().createBy(DEFAULT_CREATE_BY).content(DEFAULT_CONTENT).createDate(DEFAULT_CREATE_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CusNote createUpdatedEntity() {
        return new CusNote().createBy(UPDATED_CREATE_BY).content(UPDATED_CONTENT).createDate(UPDATED_CREATE_DATE);
    }

    @BeforeEach
    public void initTest() {
        cusNote = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCusNote != null) {
            cusNoteRepository.delete(insertedCusNote);
            insertedCusNote = null;
        }
    }

    @Test
    @Transactional
    void createCusNote() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CusNote
        CusNoteDTO cusNoteDTO = cusNoteMapper.toDto(cusNote);
        var returnedCusNoteDTO = om.readValue(
            restCusNoteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cusNoteDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CusNoteDTO.class
        );

        // Validate the CusNote in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCusNote = cusNoteMapper.toEntity(returnedCusNoteDTO);
        assertCusNoteUpdatableFieldsEquals(returnedCusNote, getPersistedCusNote(returnedCusNote));

        insertedCusNote = returnedCusNote;
    }

    @Test
    @Transactional
    void createCusNoteWithExistingId() throws Exception {
        // Create the CusNote with an existing ID
        cusNote.setId(1L);
        CusNoteDTO cusNoteDTO = cusNoteMapper.toDto(cusNote);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCusNoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cusNoteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CusNote in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreateByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cusNote.setCreateBy(null);

        // Create the CusNote, which fails.
        CusNoteDTO cusNoteDTO = cusNoteMapper.toDto(cusNote);

        restCusNoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cusNoteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContentIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cusNote.setContent(null);

        // Create the CusNote, which fails.
        CusNoteDTO cusNoteDTO = cusNoteMapper.toDto(cusNote);

        restCusNoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cusNoteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCusNotes() throws Exception {
        // Initialize the database
        insertedCusNote = cusNoteRepository.saveAndFlush(cusNote);

        // Get all the cusNoteList
        restCusNoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cusNote.getId().intValue())))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())));
    }

    @Test
    @Transactional
    void getCusNote() throws Exception {
        // Initialize the database
        insertedCusNote = cusNoteRepository.saveAndFlush(cusNote);

        // Get the cusNote
        restCusNoteMockMvc
            .perform(get(ENTITY_API_URL_ID, cusNote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cusNote.getId().intValue()))
            .andExpect(jsonPath("$.createBy").value(DEFAULT_CREATE_BY))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCusNote() throws Exception {
        // Get the cusNote
        restCusNoteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCusNote() throws Exception {
        // Initialize the database
        insertedCusNote = cusNoteRepository.saveAndFlush(cusNote);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cusNote
        CusNote updatedCusNote = cusNoteRepository.findById(cusNote.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCusNote are not directly saved in db
        em.detach(updatedCusNote);
        updatedCusNote.createBy(UPDATED_CREATE_BY).content(UPDATED_CONTENT).createDate(UPDATED_CREATE_DATE);
        CusNoteDTO cusNoteDTO = cusNoteMapper.toDto(updatedCusNote);

        restCusNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cusNoteDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cusNoteDTO))
            )
            .andExpect(status().isOk());

        // Validate the CusNote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCusNoteToMatchAllProperties(updatedCusNote);
    }

    @Test
    @Transactional
    void putNonExistingCusNote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cusNote.setId(longCount.incrementAndGet());

        // Create the CusNote
        CusNoteDTO cusNoteDTO = cusNoteMapper.toDto(cusNote);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCusNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cusNoteDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cusNoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CusNote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCusNote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cusNote.setId(longCount.incrementAndGet());

        // Create the CusNote
        CusNoteDTO cusNoteDTO = cusNoteMapper.toDto(cusNote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCusNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cusNoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CusNote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCusNote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cusNote.setId(longCount.incrementAndGet());

        // Create the CusNote
        CusNoteDTO cusNoteDTO = cusNoteMapper.toDto(cusNote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCusNoteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cusNoteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CusNote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCusNoteWithPatch() throws Exception {
        // Initialize the database
        insertedCusNote = cusNoteRepository.saveAndFlush(cusNote);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cusNote using partial update
        CusNote partialUpdatedCusNote = new CusNote();
        partialUpdatedCusNote.setId(cusNote.getId());

        partialUpdatedCusNote.createBy(UPDATED_CREATE_BY).content(UPDATED_CONTENT);

        restCusNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCusNote.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCusNote))
            )
            .andExpect(status().isOk());

        // Validate the CusNote in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCusNoteUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCusNote, cusNote), getPersistedCusNote(cusNote));
    }

    @Test
    @Transactional
    void fullUpdateCusNoteWithPatch() throws Exception {
        // Initialize the database
        insertedCusNote = cusNoteRepository.saveAndFlush(cusNote);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cusNote using partial update
        CusNote partialUpdatedCusNote = new CusNote();
        partialUpdatedCusNote.setId(cusNote.getId());

        partialUpdatedCusNote.createBy(UPDATED_CREATE_BY).content(UPDATED_CONTENT).createDate(UPDATED_CREATE_DATE);

        restCusNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCusNote.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCusNote))
            )
            .andExpect(status().isOk());

        // Validate the CusNote in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCusNoteUpdatableFieldsEquals(partialUpdatedCusNote, getPersistedCusNote(partialUpdatedCusNote));
    }

    @Test
    @Transactional
    void patchNonExistingCusNote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cusNote.setId(longCount.incrementAndGet());

        // Create the CusNote
        CusNoteDTO cusNoteDTO = cusNoteMapper.toDto(cusNote);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCusNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cusNoteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cusNoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CusNote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCusNote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cusNote.setId(longCount.incrementAndGet());

        // Create the CusNote
        CusNoteDTO cusNoteDTO = cusNoteMapper.toDto(cusNote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCusNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cusNoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CusNote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCusNote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cusNote.setId(longCount.incrementAndGet());

        // Create the CusNote
        CusNoteDTO cusNoteDTO = cusNoteMapper.toDto(cusNote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCusNoteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cusNoteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CusNote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCusNote() throws Exception {
        // Initialize the database
        insertedCusNote = cusNoteRepository.saveAndFlush(cusNote);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cusNote
        restCusNoteMockMvc
            .perform(delete(ENTITY_API_URL_ID, cusNote.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cusNoteRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected CusNote getPersistedCusNote(CusNote cusNote) {
        return cusNoteRepository.findById(cusNote.getId()).orElseThrow();
    }

    protected void assertPersistedCusNoteToMatchAllProperties(CusNote expectedCusNote) {
        assertCusNoteAllPropertiesEquals(expectedCusNote, getPersistedCusNote(expectedCusNote));
    }

    protected void assertPersistedCusNoteToMatchUpdatableProperties(CusNote expectedCusNote) {
        assertCusNoteAllUpdatablePropertiesEquals(expectedCusNote, getPersistedCusNote(expectedCusNote));
    }
}
