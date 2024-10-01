package com.project.web.rest;

import static com.project.domain.HistoryOrderAsserts.*;
import static com.project.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.IntegrationTest;
import com.project.domain.HistoryOrder;
import com.project.domain.enumeration.OrderStatus;
import com.project.domain.enumeration.OrderStatus;
import com.project.repository.HistoryOrderRepository;
import com.project.service.dto.HistoryOrderDTO;
import com.project.service.mapper.HistoryOrderMapper;
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
 * Integration tests for the {@link HistoryOrderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HistoryOrderResourceIT {

    private static final String DEFAULT_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_MODIFIED_BY = "BBBBBBBBBB";

    private static final OrderStatus DEFAULT_STATUS_NEW = OrderStatus.NEW;
    private static final OrderStatus UPDATED_STATUS_NEW = OrderStatus.ASSIGNED;

    private static final OrderStatus DEFAULT_STATUS_OLD = OrderStatus.NEW;
    private static final OrderStatus UPDATED_STATUS_OLD = OrderStatus.ASSIGNED;

    private static final LocalDate DEFAULT_MODIFIED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/history-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HistoryOrderRepository historyOrderRepository;

    @Autowired
    private HistoryOrderMapper historyOrderMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHistoryOrderMockMvc;

    private HistoryOrder historyOrder;

    private HistoryOrder insertedHistoryOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoryOrder createEntity() {
        return new HistoryOrder()
            .modifiedBy(DEFAULT_MODIFIED_BY)
            .statusNew(DEFAULT_STATUS_NEW)
            .statusOld(DEFAULT_STATUS_OLD)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoryOrder createUpdatedEntity() {
        return new HistoryOrder()
            .modifiedBy(UPDATED_MODIFIED_BY)
            .statusNew(UPDATED_STATUS_NEW)
            .statusOld(UPDATED_STATUS_OLD)
            .modifiedDate(UPDATED_MODIFIED_DATE);
    }

    @BeforeEach
    public void initTest() {
        historyOrder = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedHistoryOrder != null) {
            historyOrderRepository.delete(insertedHistoryOrder);
            insertedHistoryOrder = null;
        }
    }

    @Test
    @Transactional
    void createHistoryOrder() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HistoryOrder
        HistoryOrderDTO historyOrderDTO = historyOrderMapper.toDto(historyOrder);
        var returnedHistoryOrderDTO = om.readValue(
            restHistoryOrderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historyOrderDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HistoryOrderDTO.class
        );

        // Validate the HistoryOrder in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHistoryOrder = historyOrderMapper.toEntity(returnedHistoryOrderDTO);
        assertHistoryOrderUpdatableFieldsEquals(returnedHistoryOrder, getPersistedHistoryOrder(returnedHistoryOrder));

        insertedHistoryOrder = returnedHistoryOrder;
    }

    @Test
    @Transactional
    void createHistoryOrderWithExistingId() throws Exception {
        // Create the HistoryOrder with an existing ID
        historyOrder.setId(1L);
        HistoryOrderDTO historyOrderDTO = historyOrderMapper.toDto(historyOrder);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoryOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historyOrderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HistoryOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkModifiedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historyOrder.setModifiedBy(null);

        // Create the HistoryOrder, which fails.
        HistoryOrderDTO historyOrderDTO = historyOrderMapper.toDto(historyOrder);

        restHistoryOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historyOrderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHistoryOrders() throws Exception {
        // Initialize the database
        insertedHistoryOrder = historyOrderRepository.saveAndFlush(historyOrder);

        // Get all the historyOrderList
        restHistoryOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historyOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].statusNew").value(hasItem(DEFAULT_STATUS_NEW.toString())))
            .andExpect(jsonPath("$.[*].statusOld").value(hasItem(DEFAULT_STATUS_OLD.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getHistoryOrder() throws Exception {
        // Initialize the database
        insertedHistoryOrder = historyOrderRepository.saveAndFlush(historyOrder);

        // Get the historyOrder
        restHistoryOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, historyOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historyOrder.getId().intValue()))
            .andExpect(jsonPath("$.modifiedBy").value(DEFAULT_MODIFIED_BY))
            .andExpect(jsonPath("$.statusNew").value(DEFAULT_STATUS_NEW.toString()))
            .andExpect(jsonPath("$.statusOld").value(DEFAULT_STATUS_OLD.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingHistoryOrder() throws Exception {
        // Get the historyOrder
        restHistoryOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHistoryOrder() throws Exception {
        // Initialize the database
        insertedHistoryOrder = historyOrderRepository.saveAndFlush(historyOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historyOrder
        HistoryOrder updatedHistoryOrder = historyOrderRepository.findById(historyOrder.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHistoryOrder are not directly saved in db
        em.detach(updatedHistoryOrder);
        updatedHistoryOrder
            .modifiedBy(UPDATED_MODIFIED_BY)
            .statusNew(UPDATED_STATUS_NEW)
            .statusOld(UPDATED_STATUS_OLD)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        HistoryOrderDTO historyOrderDTO = historyOrderMapper.toDto(updatedHistoryOrder);

        restHistoryOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historyOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historyOrderDTO))
            )
            .andExpect(status().isOk());

        // Validate the HistoryOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHistoryOrderToMatchAllProperties(updatedHistoryOrder);
    }

    @Test
    @Transactional
    void putNonExistingHistoryOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historyOrder.setId(longCount.incrementAndGet());

        // Create the HistoryOrder
        HistoryOrderDTO historyOrderDTO = historyOrderMapper.toDto(historyOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoryOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historyOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historyOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoryOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHistoryOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historyOrder.setId(longCount.incrementAndGet());

        // Create the HistoryOrder
        HistoryOrderDTO historyOrderDTO = historyOrderMapper.toDto(historyOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoryOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historyOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoryOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHistoryOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historyOrder.setId(longCount.incrementAndGet());

        // Create the HistoryOrder
        HistoryOrderDTO historyOrderDTO = historyOrderMapper.toDto(historyOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoryOrderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historyOrderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoryOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHistoryOrderWithPatch() throws Exception {
        // Initialize the database
        insertedHistoryOrder = historyOrderRepository.saveAndFlush(historyOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historyOrder using partial update
        HistoryOrder partialUpdatedHistoryOrder = new HistoryOrder();
        partialUpdatedHistoryOrder.setId(historyOrder.getId());

        partialUpdatedHistoryOrder.modifiedDate(UPDATED_MODIFIED_DATE);

        restHistoryOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoryOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHistoryOrder))
            )
            .andExpect(status().isOk());

        // Validate the HistoryOrder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHistoryOrderUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHistoryOrder, historyOrder),
            getPersistedHistoryOrder(historyOrder)
        );
    }

    @Test
    @Transactional
    void fullUpdateHistoryOrderWithPatch() throws Exception {
        // Initialize the database
        insertedHistoryOrder = historyOrderRepository.saveAndFlush(historyOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historyOrder using partial update
        HistoryOrder partialUpdatedHistoryOrder = new HistoryOrder();
        partialUpdatedHistoryOrder.setId(historyOrder.getId());

        partialUpdatedHistoryOrder
            .modifiedBy(UPDATED_MODIFIED_BY)
            .statusNew(UPDATED_STATUS_NEW)
            .statusOld(UPDATED_STATUS_OLD)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restHistoryOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoryOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHistoryOrder))
            )
            .andExpect(status().isOk());

        // Validate the HistoryOrder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHistoryOrderUpdatableFieldsEquals(partialUpdatedHistoryOrder, getPersistedHistoryOrder(partialUpdatedHistoryOrder));
    }

    @Test
    @Transactional
    void patchNonExistingHistoryOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historyOrder.setId(longCount.incrementAndGet());

        // Create the HistoryOrder
        HistoryOrderDTO historyOrderDTO = historyOrderMapper.toDto(historyOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoryOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, historyOrderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(historyOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoryOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHistoryOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historyOrder.setId(longCount.incrementAndGet());

        // Create the HistoryOrder
        HistoryOrderDTO historyOrderDTO = historyOrderMapper.toDto(historyOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoryOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(historyOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoryOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHistoryOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historyOrder.setId(longCount.incrementAndGet());

        // Create the HistoryOrder
        HistoryOrderDTO historyOrderDTO = historyOrderMapper.toDto(historyOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoryOrderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(historyOrderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoryOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHistoryOrder() throws Exception {
        // Initialize the database
        insertedHistoryOrder = historyOrderRepository.saveAndFlush(historyOrder);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the historyOrder
        restHistoryOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, historyOrder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return historyOrderRepository.count();
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

    protected HistoryOrder getPersistedHistoryOrder(HistoryOrder historyOrder) {
        return historyOrderRepository.findById(historyOrder.getId()).orElseThrow();
    }

    protected void assertPersistedHistoryOrderToMatchAllProperties(HistoryOrder expectedHistoryOrder) {
        assertHistoryOrderAllPropertiesEquals(expectedHistoryOrder, getPersistedHistoryOrder(expectedHistoryOrder));
    }

    protected void assertPersistedHistoryOrderToMatchUpdatableProperties(HistoryOrder expectedHistoryOrder) {
        assertHistoryOrderAllUpdatablePropertiesEquals(expectedHistoryOrder, getPersistedHistoryOrder(expectedHistoryOrder));
    }
}
