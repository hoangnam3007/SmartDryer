package com.project.web.rest;

import static com.project.domain.SendSMSAsserts.*;
import static com.project.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.IntegrationTest;
import com.project.domain.SendSMS;
import com.project.repository.SendSMSRepository;
import com.project.service.dto.SendSMSDTO;
import com.project.service.mapper.SendSMSMapper;
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
 * Integration tests for the {@link SendSMSResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SendSMSResourceIT {

    private static final String DEFAULT_MOBILE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_SENDED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SENDED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_TYPE = 1;
    private static final Integer UPDATED_TYPE = 2;

    private static final String ENTITY_API_URL = "/api/send-sms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SendSMSRepository sendSMSRepository;

    @Autowired
    private SendSMSMapper sendSMSMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSendSMSMockMvc;

    private SendSMS sendSMS;

    private SendSMS insertedSendSMS;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SendSMS createEntity() {
        return new SendSMS()
            .mobile(DEFAULT_MOBILE)
            .content(DEFAULT_CONTENT)
            .status(DEFAULT_STATUS)
            .createDate(DEFAULT_CREATE_DATE)
            .sendedDate(DEFAULT_SENDED_DATE)
            .type(DEFAULT_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SendSMS createUpdatedEntity() {
        return new SendSMS()
            .mobile(UPDATED_MOBILE)
            .content(UPDATED_CONTENT)
            .status(UPDATED_STATUS)
            .createDate(UPDATED_CREATE_DATE)
            .sendedDate(UPDATED_SENDED_DATE)
            .type(UPDATED_TYPE);
    }

    @BeforeEach
    public void initTest() {
        sendSMS = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSendSMS != null) {
            sendSMSRepository.delete(insertedSendSMS);
            insertedSendSMS = null;
        }
    }

    @Test
    @Transactional
    void createSendSMS() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SendSMS
        SendSMSDTO sendSMSDTO = sendSMSMapper.toDto(sendSMS);
        var returnedSendSMSDTO = om.readValue(
            restSendSMSMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sendSMSDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SendSMSDTO.class
        );

        // Validate the SendSMS in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSendSMS = sendSMSMapper.toEntity(returnedSendSMSDTO);
        assertSendSMSUpdatableFieldsEquals(returnedSendSMS, getPersistedSendSMS(returnedSendSMS));

        insertedSendSMS = returnedSendSMS;
    }

    @Test
    @Transactional
    void createSendSMSWithExistingId() throws Exception {
        // Create the SendSMS with an existing ID
        sendSMS.setId(1L);
        SendSMSDTO sendSMSDTO = sendSMSMapper.toDto(sendSMS);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSendSMSMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sendSMSDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SendSMS in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMobileIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sendSMS.setMobile(null);

        // Create the SendSMS, which fails.
        SendSMSDTO sendSMSDTO = sendSMSMapper.toDto(sendSMS);

        restSendSMSMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sendSMSDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSendSMS() throws Exception {
        // Initialize the database
        insertedSendSMS = sendSMSRepository.saveAndFlush(sendSMS);

        // Get all the sendSMSList
        restSendSMSMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sendSMS.getId().intValue())))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].sendedDate").value(hasItem(DEFAULT_SENDED_DATE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @Test
    @Transactional
    void getSendSMS() throws Exception {
        // Initialize the database
        insertedSendSMS = sendSMSRepository.saveAndFlush(sendSMS);

        // Get the sendSMS
        restSendSMSMockMvc
            .perform(get(ENTITY_API_URL_ID, sendSMS.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sendSMS.getId().intValue()))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.sendedDate").value(DEFAULT_SENDED_DATE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingSendSMS() throws Exception {
        // Get the sendSMS
        restSendSMSMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSendSMS() throws Exception {
        // Initialize the database
        insertedSendSMS = sendSMSRepository.saveAndFlush(sendSMS);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sendSMS
        SendSMS updatedSendSMS = sendSMSRepository.findById(sendSMS.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSendSMS are not directly saved in db
        em.detach(updatedSendSMS);
        updatedSendSMS
            .mobile(UPDATED_MOBILE)
            .content(UPDATED_CONTENT)
            .status(UPDATED_STATUS)
            .createDate(UPDATED_CREATE_DATE)
            .sendedDate(UPDATED_SENDED_DATE)
            .type(UPDATED_TYPE);
        SendSMSDTO sendSMSDTO = sendSMSMapper.toDto(updatedSendSMS);

        restSendSMSMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sendSMSDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sendSMSDTO))
            )
            .andExpect(status().isOk());

        // Validate the SendSMS in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSendSMSToMatchAllProperties(updatedSendSMS);
    }

    @Test
    @Transactional
    void putNonExistingSendSMS() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sendSMS.setId(longCount.incrementAndGet());

        // Create the SendSMS
        SendSMSDTO sendSMSDTO = sendSMSMapper.toDto(sendSMS);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSendSMSMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sendSMSDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sendSMSDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SendSMS in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSendSMS() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sendSMS.setId(longCount.incrementAndGet());

        // Create the SendSMS
        SendSMSDTO sendSMSDTO = sendSMSMapper.toDto(sendSMS);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSendSMSMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sendSMSDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SendSMS in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSendSMS() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sendSMS.setId(longCount.incrementAndGet());

        // Create the SendSMS
        SendSMSDTO sendSMSDTO = sendSMSMapper.toDto(sendSMS);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSendSMSMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sendSMSDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SendSMS in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSendSMSWithPatch() throws Exception {
        // Initialize the database
        insertedSendSMS = sendSMSRepository.saveAndFlush(sendSMS);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sendSMS using partial update
        SendSMS partialUpdatedSendSMS = new SendSMS();
        partialUpdatedSendSMS.setId(sendSMS.getId());

        partialUpdatedSendSMS.content(UPDATED_CONTENT).sendedDate(UPDATED_SENDED_DATE).type(UPDATED_TYPE);

        restSendSMSMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSendSMS.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSendSMS))
            )
            .andExpect(status().isOk());

        // Validate the SendSMS in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSendSMSUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSendSMS, sendSMS), getPersistedSendSMS(sendSMS));
    }

    @Test
    @Transactional
    void fullUpdateSendSMSWithPatch() throws Exception {
        // Initialize the database
        insertedSendSMS = sendSMSRepository.saveAndFlush(sendSMS);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sendSMS using partial update
        SendSMS partialUpdatedSendSMS = new SendSMS();
        partialUpdatedSendSMS.setId(sendSMS.getId());

        partialUpdatedSendSMS
            .mobile(UPDATED_MOBILE)
            .content(UPDATED_CONTENT)
            .status(UPDATED_STATUS)
            .createDate(UPDATED_CREATE_DATE)
            .sendedDate(UPDATED_SENDED_DATE)
            .type(UPDATED_TYPE);

        restSendSMSMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSendSMS.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSendSMS))
            )
            .andExpect(status().isOk());

        // Validate the SendSMS in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSendSMSUpdatableFieldsEquals(partialUpdatedSendSMS, getPersistedSendSMS(partialUpdatedSendSMS));
    }

    @Test
    @Transactional
    void patchNonExistingSendSMS() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sendSMS.setId(longCount.incrementAndGet());

        // Create the SendSMS
        SendSMSDTO sendSMSDTO = sendSMSMapper.toDto(sendSMS);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSendSMSMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sendSMSDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sendSMSDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SendSMS in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSendSMS() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sendSMS.setId(longCount.incrementAndGet());

        // Create the SendSMS
        SendSMSDTO sendSMSDTO = sendSMSMapper.toDto(sendSMS);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSendSMSMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sendSMSDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SendSMS in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSendSMS() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sendSMS.setId(longCount.incrementAndGet());

        // Create the SendSMS
        SendSMSDTO sendSMSDTO = sendSMSMapper.toDto(sendSMS);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSendSMSMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(sendSMSDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SendSMS in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSendSMS() throws Exception {
        // Initialize the database
        insertedSendSMS = sendSMSRepository.saveAndFlush(sendSMS);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sendSMS
        restSendSMSMockMvc
            .perform(delete(ENTITY_API_URL_ID, sendSMS.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sendSMSRepository.count();
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

    protected SendSMS getPersistedSendSMS(SendSMS sendSMS) {
        return sendSMSRepository.findById(sendSMS.getId()).orElseThrow();
    }

    protected void assertPersistedSendSMSToMatchAllProperties(SendSMS expectedSendSMS) {
        assertSendSMSAllPropertiesEquals(expectedSendSMS, getPersistedSendSMS(expectedSendSMS));
    }

    protected void assertPersistedSendSMSToMatchUpdatableProperties(SendSMS expectedSendSMS) {
        assertSendSMSAllUpdatablePropertiesEquals(expectedSendSMS, getPersistedSendSMS(expectedSendSMS));
    }
}
