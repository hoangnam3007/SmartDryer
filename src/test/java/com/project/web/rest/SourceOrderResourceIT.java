package com.project.web.rest;

import static com.project.domain.SourceOrderAsserts.*;
import static com.project.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.IntegrationTest;
import com.project.domain.SourceOrder;
import com.project.repository.SourceOrderRepository;
import com.project.service.dto.SourceOrderDTO;
import com.project.service.mapper.SourceOrderMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link SourceOrderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SourceOrderResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/source-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SourceOrderRepository sourceOrderRepository;

    @Autowired
    private SourceOrderMapper sourceOrderMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSourceOrderMockMvc;

    private SourceOrder sourceOrder;

    private SourceOrder insertedSourceOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SourceOrder createEntity() {
        return new SourceOrder().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SourceOrder createUpdatedEntity() {
        return new SourceOrder().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        sourceOrder = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSourceOrder != null) {
            sourceOrderRepository.delete(insertedSourceOrder);
            insertedSourceOrder = null;
        }
    }

    @Test
    @Transactional
    void createSourceOrder() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SourceOrder
        SourceOrderDTO sourceOrderDTO = sourceOrderMapper.toDto(sourceOrder);
        var returnedSourceOrderDTO = om.readValue(
            restSourceOrderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sourceOrderDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SourceOrderDTO.class
        );

        // Validate the SourceOrder in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSourceOrder = sourceOrderMapper.toEntity(returnedSourceOrderDTO);
        assertSourceOrderUpdatableFieldsEquals(returnedSourceOrder, getPersistedSourceOrder(returnedSourceOrder));

        insertedSourceOrder = returnedSourceOrder;
    }

    @Test
    @Transactional
    void createSourceOrderWithExistingId() throws Exception {
        // Create the SourceOrder with an existing ID
        sourceOrder.setId(1L);
        SourceOrderDTO sourceOrderDTO = sourceOrderMapper.toDto(sourceOrder);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSourceOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sourceOrderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SourceOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sourceOrder.setName(null);

        // Create the SourceOrder, which fails.
        SourceOrderDTO sourceOrderDTO = sourceOrderMapper.toDto(sourceOrder);

        restSourceOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sourceOrderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSourceOrders() throws Exception {
        // Initialize the database
        insertedSourceOrder = sourceOrderRepository.saveAndFlush(sourceOrder);

        // Get all the sourceOrderList
        restSourceOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sourceOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getSourceOrder() throws Exception {
        // Initialize the database
        insertedSourceOrder = sourceOrderRepository.saveAndFlush(sourceOrder);

        // Get the sourceOrder
        restSourceOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, sourceOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sourceOrder.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingSourceOrder() throws Exception {
        // Get the sourceOrder
        restSourceOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSourceOrder() throws Exception {
        // Initialize the database
        insertedSourceOrder = sourceOrderRepository.saveAndFlush(sourceOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sourceOrder
        SourceOrder updatedSourceOrder = sourceOrderRepository.findById(sourceOrder.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSourceOrder are not directly saved in db
        em.detach(updatedSourceOrder);
        updatedSourceOrder.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        SourceOrderDTO sourceOrderDTO = sourceOrderMapper.toDto(updatedSourceOrder);

        restSourceOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sourceOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sourceOrderDTO))
            )
            .andExpect(status().isOk());

        // Validate the SourceOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSourceOrderToMatchAllProperties(updatedSourceOrder);
    }

    @Test
    @Transactional
    void putNonExistingSourceOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sourceOrder.setId(longCount.incrementAndGet());

        // Create the SourceOrder
        SourceOrderDTO sourceOrderDTO = sourceOrderMapper.toDto(sourceOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSourceOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sourceOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sourceOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourceOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSourceOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sourceOrder.setId(longCount.incrementAndGet());

        // Create the SourceOrder
        SourceOrderDTO sourceOrderDTO = sourceOrderMapper.toDto(sourceOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sourceOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourceOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSourceOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sourceOrder.setId(longCount.incrementAndGet());

        // Create the SourceOrder
        SourceOrderDTO sourceOrderDTO = sourceOrderMapper.toDto(sourceOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceOrderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sourceOrderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SourceOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSourceOrderWithPatch() throws Exception {
        // Initialize the database
        insertedSourceOrder = sourceOrderRepository.saveAndFlush(sourceOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sourceOrder using partial update
        SourceOrder partialUpdatedSourceOrder = new SourceOrder();
        partialUpdatedSourceOrder.setId(sourceOrder.getId());

        partialUpdatedSourceOrder.description(UPDATED_DESCRIPTION);

        restSourceOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSourceOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSourceOrder))
            )
            .andExpect(status().isOk());

        // Validate the SourceOrder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSourceOrderUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSourceOrder, sourceOrder),
            getPersistedSourceOrder(sourceOrder)
        );
    }

    @Test
    @Transactional
    void fullUpdateSourceOrderWithPatch() throws Exception {
        // Initialize the database
        insertedSourceOrder = sourceOrderRepository.saveAndFlush(sourceOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sourceOrder using partial update
        SourceOrder partialUpdatedSourceOrder = new SourceOrder();
        partialUpdatedSourceOrder.setId(sourceOrder.getId());

        partialUpdatedSourceOrder.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restSourceOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSourceOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSourceOrder))
            )
            .andExpect(status().isOk());

        // Validate the SourceOrder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSourceOrderUpdatableFieldsEquals(partialUpdatedSourceOrder, getPersistedSourceOrder(partialUpdatedSourceOrder));
    }

    @Test
    @Transactional
    void patchNonExistingSourceOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sourceOrder.setId(longCount.incrementAndGet());

        // Create the SourceOrder
        SourceOrderDTO sourceOrderDTO = sourceOrderMapper.toDto(sourceOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSourceOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sourceOrderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sourceOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourceOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSourceOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sourceOrder.setId(longCount.incrementAndGet());

        // Create the SourceOrder
        SourceOrderDTO sourceOrderDTO = sourceOrderMapper.toDto(sourceOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sourceOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourceOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSourceOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sourceOrder.setId(longCount.incrementAndGet());

        // Create the SourceOrder
        SourceOrderDTO sourceOrderDTO = sourceOrderMapper.toDto(sourceOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceOrderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(sourceOrderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SourceOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSourceOrder() throws Exception {
        // Initialize the database
        insertedSourceOrder = sourceOrderRepository.saveAndFlush(sourceOrder);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sourceOrder
        restSourceOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, sourceOrder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sourceOrderRepository.count();
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

    protected SourceOrder getPersistedSourceOrder(SourceOrder sourceOrder) {
        return sourceOrderRepository.findById(sourceOrder.getId()).orElseThrow();
    }

    protected void assertPersistedSourceOrderToMatchAllProperties(SourceOrder expectedSourceOrder) {
        assertSourceOrderAllPropertiesEquals(expectedSourceOrder, getPersistedSourceOrder(expectedSourceOrder));
    }

    protected void assertPersistedSourceOrderToMatchUpdatableProperties(SourceOrder expectedSourceOrder) {
        assertSourceOrderAllUpdatablePropertiesEquals(expectedSourceOrder, getPersistedSourceOrder(expectedSourceOrder));
    }
}
