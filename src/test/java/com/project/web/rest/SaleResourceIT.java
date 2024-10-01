package com.project.web.rest;

import static com.project.domain.SaleAsserts.*;
import static com.project.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.IntegrationTest;
import com.project.domain.Sale;
import com.project.repository.SaleRepository;
import com.project.service.dto.SaleDTO;
import com.project.service.mapper.SaleMapper;
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
 * Integration tests for the {@link SaleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SaleResourceIT {

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_MODIFIED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/sales";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SaleMapper saleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSaleMockMvc;

    private Sale sale;

    private Sale insertedSale;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sale createEntity() {
        return new Sale()
            .userName(DEFAULT_USER_NAME)
            .fullName(DEFAULT_FULL_NAME)
            .mobile(DEFAULT_MOBILE)
            .email(DEFAULT_EMAIL)
            .note(DEFAULT_NOTE)
            .createDate(DEFAULT_CREATE_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sale createUpdatedEntity() {
        return new Sale()
            .userName(UPDATED_USER_NAME)
            .fullName(UPDATED_FULL_NAME)
            .mobile(UPDATED_MOBILE)
            .email(UPDATED_EMAIL)
            .note(UPDATED_NOTE)
            .createDate(UPDATED_CREATE_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
    }

    @BeforeEach
    public void initTest() {
        sale = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSale != null) {
            saleRepository.delete(insertedSale);
            insertedSale = null;
        }
    }

    @Test
    @Transactional
    void createSale() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Sale
        SaleDTO saleDTO = saleMapper.toDto(sale);
        var returnedSaleDTO = om.readValue(
            restSaleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saleDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SaleDTO.class
        );

        // Validate the Sale in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSale = saleMapper.toEntity(returnedSaleDTO);
        assertSaleUpdatableFieldsEquals(returnedSale, getPersistedSale(returnedSale));

        insertedSale = returnedSale;
    }

    @Test
    @Transactional
    void createSaleWithExistingId() throws Exception {
        // Create the Sale with an existing ID
        sale.setId(1L);
        SaleDTO saleDTO = saleMapper.toDto(sale);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSaleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sale in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sale.setUserName(null);

        // Create the Sale, which fails.
        SaleDTO saleDTO = saleMapper.toDto(sale);

        restSaleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFullNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sale.setFullName(null);

        // Create the Sale, which fails.
        SaleDTO saleDTO = saleMapper.toDto(sale);

        restSaleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSales() throws Exception {
        // Initialize the database
        insertedSale = saleRepository.saveAndFlush(sale);

        // Get all the saleList
        restSaleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sale.getId().intValue())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getSale() throws Exception {
        // Initialize the database
        insertedSale = saleRepository.saveAndFlush(sale);

        // Get the sale
        restSaleMockMvc
            .perform(get(ENTITY_API_URL_ID, sale.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sale.getId().intValue()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSale() throws Exception {
        // Get the sale
        restSaleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSale() throws Exception {
        // Initialize the database
        insertedSale = saleRepository.saveAndFlush(sale);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sale
        Sale updatedSale = saleRepository.findById(sale.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSale are not directly saved in db
        em.detach(updatedSale);
        updatedSale
            .userName(UPDATED_USER_NAME)
            .fullName(UPDATED_FULL_NAME)
            .mobile(UPDATED_MOBILE)
            .email(UPDATED_EMAIL)
            .note(UPDATED_NOTE)
            .createDate(UPDATED_CREATE_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        SaleDTO saleDTO = saleMapper.toDto(updatedSale);

        restSaleMockMvc
            .perform(put(ENTITY_API_URL_ID, saleDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saleDTO)))
            .andExpect(status().isOk());

        // Validate the Sale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSaleToMatchAllProperties(updatedSale);
    }

    @Test
    @Transactional
    void putNonExistingSale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sale.setId(longCount.incrementAndGet());

        // Create the Sale
        SaleDTO saleDTO = saleMapper.toDto(sale);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSaleMockMvc
            .perform(put(ENTITY_API_URL_ID, saleDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sale.setId(longCount.incrementAndGet());

        // Create the Sale
        SaleDTO saleDTO = saleMapper.toDto(sale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(saleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sale.setId(longCount.incrementAndGet());

        // Create the Sale
        SaleDTO saleDTO = saleMapper.toDto(sale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSaleWithPatch() throws Exception {
        // Initialize the database
        insertedSale = saleRepository.saveAndFlush(sale);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sale using partial update
        Sale partialUpdatedSale = new Sale();
        partialUpdatedSale.setId(sale.getId());

        partialUpdatedSale
            .userName(UPDATED_USER_NAME)
            .fullName(UPDATED_FULL_NAME)
            .email(UPDATED_EMAIL)
            .createDate(UPDATED_CREATE_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restSaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSale.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSale))
            )
            .andExpect(status().isOk());

        // Validate the Sale in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSaleUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSale, sale), getPersistedSale(sale));
    }

    @Test
    @Transactional
    void fullUpdateSaleWithPatch() throws Exception {
        // Initialize the database
        insertedSale = saleRepository.saveAndFlush(sale);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sale using partial update
        Sale partialUpdatedSale = new Sale();
        partialUpdatedSale.setId(sale.getId());

        partialUpdatedSale
            .userName(UPDATED_USER_NAME)
            .fullName(UPDATED_FULL_NAME)
            .mobile(UPDATED_MOBILE)
            .email(UPDATED_EMAIL)
            .note(UPDATED_NOTE)
            .createDate(UPDATED_CREATE_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restSaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSale.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSale))
            )
            .andExpect(status().isOk());

        // Validate the Sale in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSaleUpdatableFieldsEquals(partialUpdatedSale, getPersistedSale(partialUpdatedSale));
    }

    @Test
    @Transactional
    void patchNonExistingSale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sale.setId(longCount.incrementAndGet());

        // Create the Sale
        SaleDTO saleDTO = saleMapper.toDto(sale);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, saleDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(saleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sale.setId(longCount.incrementAndGet());

        // Create the Sale
        SaleDTO saleDTO = saleMapper.toDto(sale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(saleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sale.setId(longCount.incrementAndGet());

        // Create the Sale
        SaleDTO saleDTO = saleMapper.toDto(sale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(saleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSale() throws Exception {
        // Initialize the database
        insertedSale = saleRepository.saveAndFlush(sale);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sale
        restSaleMockMvc
            .perform(delete(ENTITY_API_URL_ID, sale.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return saleRepository.count();
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

    protected Sale getPersistedSale(Sale sale) {
        return saleRepository.findById(sale.getId()).orElseThrow();
    }

    protected void assertPersistedSaleToMatchAllProperties(Sale expectedSale) {
        assertSaleAllPropertiesEquals(expectedSale, getPersistedSale(expectedSale));
    }

    protected void assertPersistedSaleToMatchUpdatableProperties(Sale expectedSale) {
        assertSaleAllUpdatablePropertiesEquals(expectedSale, getPersistedSale(expectedSale));
    }
}
