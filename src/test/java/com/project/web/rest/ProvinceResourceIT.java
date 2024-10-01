package com.project.web.rest;

import static com.project.domain.ProvinceAsserts.*;
import static com.project.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.IntegrationTest;
import com.project.domain.Province;
import com.project.repository.ProvinceRepository;
import com.project.service.dto.ProvinceDTO;
import com.project.service.mapper.ProvinceMapper;
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
 * Integration tests for the {@link ProvinceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProvinceResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/provinces";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private ProvinceMapper provinceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProvinceMockMvc;

    private Province province;

    private Province insertedProvince;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Province createEntity() {
        return new Province().code(DEFAULT_CODE).name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Province createUpdatedEntity() {
        return new Province().code(UPDATED_CODE).name(UPDATED_NAME);
    }

    @BeforeEach
    public void initTest() {
        province = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProvince != null) {
            provinceRepository.delete(insertedProvince);
            insertedProvince = null;
        }
    }

    @Test
    @Transactional
    void createProvince() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);
        var returnedProvinceDTO = om.readValue(
            restProvinceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(provinceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProvinceDTO.class
        );

        // Validate the Province in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProvince = provinceMapper.toEntity(returnedProvinceDTO);
        assertProvinceUpdatableFieldsEquals(returnedProvince, getPersistedProvince(returnedProvince));

        insertedProvince = returnedProvince;
    }

    @Test
    @Transactional
    void createProvinceWithExistingId() throws Exception {
        // Create the Province with an existing ID
        province.setId(1L);
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProvinceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(provinceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Province in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        province.setName(null);

        // Create the Province, which fails.
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        restProvinceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(provinceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProvinces() throws Exception {
        // Initialize the database
        insertedProvince = provinceRepository.saveAndFlush(province);

        // Get all the provinceList
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(province.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getProvince() throws Exception {
        // Initialize the database
        insertedProvince = provinceRepository.saveAndFlush(province);

        // Get the province
        restProvinceMockMvc
            .perform(get(ENTITY_API_URL_ID, province.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(province.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingProvince() throws Exception {
        // Get the province
        restProvinceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProvince() throws Exception {
        // Initialize the database
        insertedProvince = provinceRepository.saveAndFlush(province);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the province
        Province updatedProvince = provinceRepository.findById(province.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProvince are not directly saved in db
        em.detach(updatedProvince);
        updatedProvince.code(UPDATED_CODE).name(UPDATED_NAME);
        ProvinceDTO provinceDTO = provinceMapper.toDto(updatedProvince);

        restProvinceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, provinceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(provinceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Province in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProvinceToMatchAllProperties(updatedProvince);
    }

    @Test
    @Transactional
    void putNonExistingProvince() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        province.setId(longCount.incrementAndGet());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, provinceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(provinceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Province in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProvince() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        province.setId(longCount.incrementAndGet());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(provinceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Province in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProvince() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        province.setId(longCount.incrementAndGet());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(provinceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Province in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProvinceWithPatch() throws Exception {
        // Initialize the database
        insertedProvince = provinceRepository.saveAndFlush(province);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the province using partial update
        Province partialUpdatedProvince = new Province();
        partialUpdatedProvince.setId(province.getId());

        partialUpdatedProvince.code(UPDATED_CODE);

        restProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProvince.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProvince))
            )
            .andExpect(status().isOk());

        // Validate the Province in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProvinceUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProvince, province), getPersistedProvince(province));
    }

    @Test
    @Transactional
    void fullUpdateProvinceWithPatch() throws Exception {
        // Initialize the database
        insertedProvince = provinceRepository.saveAndFlush(province);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the province using partial update
        Province partialUpdatedProvince = new Province();
        partialUpdatedProvince.setId(province.getId());

        partialUpdatedProvince.code(UPDATED_CODE).name(UPDATED_NAME);

        restProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProvince.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProvince))
            )
            .andExpect(status().isOk());

        // Validate the Province in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProvinceUpdatableFieldsEquals(partialUpdatedProvince, getPersistedProvince(partialUpdatedProvince));
    }

    @Test
    @Transactional
    void patchNonExistingProvince() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        province.setId(longCount.incrementAndGet());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, provinceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(provinceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Province in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProvince() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        province.setId(longCount.incrementAndGet());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(provinceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Province in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProvince() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        province.setId(longCount.incrementAndGet());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProvinceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(provinceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Province in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProvince() throws Exception {
        // Initialize the database
        insertedProvince = provinceRepository.saveAndFlush(province);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the province
        restProvinceMockMvc
            .perform(delete(ENTITY_API_URL_ID, province.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return provinceRepository.count();
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

    protected Province getPersistedProvince(Province province) {
        return provinceRepository.findById(province.getId()).orElseThrow();
    }

    protected void assertPersistedProvinceToMatchAllProperties(Province expectedProvince) {
        assertProvinceAllPropertiesEquals(expectedProvince, getPersistedProvince(expectedProvince));
    }

    protected void assertPersistedProvinceToMatchUpdatableProperties(Province expectedProvince) {
        assertProvinceAllUpdatablePropertiesEquals(expectedProvince, getPersistedProvince(expectedProvince));
    }
}
