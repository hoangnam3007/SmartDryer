package com.project.web.rest;

import static com.project.domain.CustomerEquipmentAsserts.*;
import static com.project.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.IntegrationTest;
import com.project.domain.CustomerEquipment;
import com.project.repository.CustomerEquipmentRepository;
import com.project.service.dto.CustomerEquipmentDTO;
import com.project.service.mapper.CustomerEquipmentMapper;
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
 * Integration tests for the {@link CustomerEquipmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomerEquipmentResourceIT {

    private static final Integer DEFAULT_QUANTILY = 1;
    private static final Integer UPDATED_QUANTILY = 2;

    private static final String ENTITY_API_URL = "/api/customer-equipments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CustomerEquipmentRepository customerEquipmentRepository;

    @Autowired
    private CustomerEquipmentMapper customerEquipmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerEquipmentMockMvc;

    private CustomerEquipment customerEquipment;

    private CustomerEquipment insertedCustomerEquipment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerEquipment createEntity() {
        return new CustomerEquipment().quantily(DEFAULT_QUANTILY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerEquipment createUpdatedEntity() {
        return new CustomerEquipment().quantily(UPDATED_QUANTILY);
    }

    @BeforeEach
    public void initTest() {
        customerEquipment = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCustomerEquipment != null) {
            customerEquipmentRepository.delete(insertedCustomerEquipment);
            insertedCustomerEquipment = null;
        }
    }

    @Test
    @Transactional
    void createCustomerEquipment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CustomerEquipment
        CustomerEquipmentDTO customerEquipmentDTO = customerEquipmentMapper.toDto(customerEquipment);
        var returnedCustomerEquipmentDTO = om.readValue(
            restCustomerEquipmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerEquipmentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CustomerEquipmentDTO.class
        );

        // Validate the CustomerEquipment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCustomerEquipment = customerEquipmentMapper.toEntity(returnedCustomerEquipmentDTO);
        assertCustomerEquipmentUpdatableFieldsEquals(returnedCustomerEquipment, getPersistedCustomerEquipment(returnedCustomerEquipment));

        insertedCustomerEquipment = returnedCustomerEquipment;
    }

    @Test
    @Transactional
    void createCustomerEquipmentWithExistingId() throws Exception {
        // Create the CustomerEquipment with an existing ID
        customerEquipment.setId(1L);
        CustomerEquipmentDTO customerEquipmentDTO = customerEquipmentMapper.toDto(customerEquipment);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerEquipmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerEquipmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerEquipment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCustomerEquipments() throws Exception {
        // Initialize the database
        insertedCustomerEquipment = customerEquipmentRepository.saveAndFlush(customerEquipment);

        // Get all the customerEquipmentList
        restCustomerEquipmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerEquipment.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantily").value(hasItem(DEFAULT_QUANTILY)));
    }

    @Test
    @Transactional
    void getCustomerEquipment() throws Exception {
        // Initialize the database
        insertedCustomerEquipment = customerEquipmentRepository.saveAndFlush(customerEquipment);

        // Get the customerEquipment
        restCustomerEquipmentMockMvc
            .perform(get(ENTITY_API_URL_ID, customerEquipment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerEquipment.getId().intValue()))
            .andExpect(jsonPath("$.quantily").value(DEFAULT_QUANTILY));
    }

    @Test
    @Transactional
    void getNonExistingCustomerEquipment() throws Exception {
        // Get the customerEquipment
        restCustomerEquipmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomerEquipment() throws Exception {
        // Initialize the database
        insertedCustomerEquipment = customerEquipmentRepository.saveAndFlush(customerEquipment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerEquipment
        CustomerEquipment updatedCustomerEquipment = customerEquipmentRepository.findById(customerEquipment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustomerEquipment are not directly saved in db
        em.detach(updatedCustomerEquipment);
        updatedCustomerEquipment.quantily(UPDATED_QUANTILY);
        CustomerEquipmentDTO customerEquipmentDTO = customerEquipmentMapper.toDto(updatedCustomerEquipment);

        restCustomerEquipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerEquipmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerEquipmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the CustomerEquipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCustomerEquipmentToMatchAllProperties(updatedCustomerEquipment);
    }

    @Test
    @Transactional
    void putNonExistingCustomerEquipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerEquipment.setId(longCount.incrementAndGet());

        // Create the CustomerEquipment
        CustomerEquipmentDTO customerEquipmentDTO = customerEquipmentMapper.toDto(customerEquipment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerEquipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerEquipmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerEquipmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerEquipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomerEquipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerEquipment.setId(longCount.incrementAndGet());

        // Create the CustomerEquipment
        CustomerEquipmentDTO customerEquipmentDTO = customerEquipmentMapper.toDto(customerEquipment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerEquipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerEquipmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerEquipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomerEquipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerEquipment.setId(longCount.incrementAndGet());

        // Create the CustomerEquipment
        CustomerEquipmentDTO customerEquipmentDTO = customerEquipmentMapper.toDto(customerEquipment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerEquipmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerEquipmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerEquipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerEquipmentWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerEquipment = customerEquipmentRepository.saveAndFlush(customerEquipment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerEquipment using partial update
        CustomerEquipment partialUpdatedCustomerEquipment = new CustomerEquipment();
        partialUpdatedCustomerEquipment.setId(customerEquipment.getId());

        partialUpdatedCustomerEquipment.quantily(UPDATED_QUANTILY);

        restCustomerEquipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerEquipment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerEquipment))
            )
            .andExpect(status().isOk());

        // Validate the CustomerEquipment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerEquipmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCustomerEquipment, customerEquipment),
            getPersistedCustomerEquipment(customerEquipment)
        );
    }

    @Test
    @Transactional
    void fullUpdateCustomerEquipmentWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerEquipment = customerEquipmentRepository.saveAndFlush(customerEquipment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerEquipment using partial update
        CustomerEquipment partialUpdatedCustomerEquipment = new CustomerEquipment();
        partialUpdatedCustomerEquipment.setId(customerEquipment.getId());

        partialUpdatedCustomerEquipment.quantily(UPDATED_QUANTILY);

        restCustomerEquipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerEquipment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerEquipment))
            )
            .andExpect(status().isOk());

        // Validate the CustomerEquipment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerEquipmentUpdatableFieldsEquals(
            partialUpdatedCustomerEquipment,
            getPersistedCustomerEquipment(partialUpdatedCustomerEquipment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCustomerEquipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerEquipment.setId(longCount.incrementAndGet());

        // Create the CustomerEquipment
        CustomerEquipmentDTO customerEquipmentDTO = customerEquipmentMapper.toDto(customerEquipment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerEquipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerEquipmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerEquipmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerEquipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomerEquipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerEquipment.setId(longCount.incrementAndGet());

        // Create the CustomerEquipment
        CustomerEquipmentDTO customerEquipmentDTO = customerEquipmentMapper.toDto(customerEquipment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerEquipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerEquipmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerEquipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomerEquipment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerEquipment.setId(longCount.incrementAndGet());

        // Create the CustomerEquipment
        CustomerEquipmentDTO customerEquipmentDTO = customerEquipmentMapper.toDto(customerEquipment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerEquipmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(customerEquipmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerEquipment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomerEquipment() throws Exception {
        // Initialize the database
        insertedCustomerEquipment = customerEquipmentRepository.saveAndFlush(customerEquipment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the customerEquipment
        restCustomerEquipmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, customerEquipment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return customerEquipmentRepository.count();
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

    protected CustomerEquipment getPersistedCustomerEquipment(CustomerEquipment customerEquipment) {
        return customerEquipmentRepository.findById(customerEquipment.getId()).orElseThrow();
    }

    protected void assertPersistedCustomerEquipmentToMatchAllProperties(CustomerEquipment expectedCustomerEquipment) {
        assertCustomerEquipmentAllPropertiesEquals(expectedCustomerEquipment, getPersistedCustomerEquipment(expectedCustomerEquipment));
    }

    protected void assertPersistedCustomerEquipmentToMatchUpdatableProperties(CustomerEquipment expectedCustomerEquipment) {
        assertCustomerEquipmentAllUpdatablePropertiesEquals(
            expectedCustomerEquipment,
            getPersistedCustomerEquipment(expectedCustomerEquipment)
        );
    }
}
