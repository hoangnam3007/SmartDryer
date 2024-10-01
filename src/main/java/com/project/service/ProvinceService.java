package com.project.service;

import com.project.domain.Province;
import com.project.repository.ProvinceRepository;
import com.project.service.dto.ProvinceDTO;
import com.project.service.mapper.ProvinceMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.project.domain.Province}.
 */
@Service
@Transactional
public class ProvinceService {

    private static final Logger LOG = LoggerFactory.getLogger(ProvinceService.class);

    private final ProvinceRepository provinceRepository;

    private final ProvinceMapper provinceMapper;

    public ProvinceService(ProvinceRepository provinceRepository, ProvinceMapper provinceMapper) {
        this.provinceRepository = provinceRepository;
        this.provinceMapper = provinceMapper;
    }

    /**
     * Save a province.
     *
     * @param provinceDTO the entity to save.
     * @return the persisted entity.
     */
    public ProvinceDTO save(ProvinceDTO provinceDTO) {
        LOG.debug("Request to save Province : {}", provinceDTO);
        Province province = provinceMapper.toEntity(provinceDTO);
        province = provinceRepository.save(province);
        return provinceMapper.toDto(province);
    }

    /**
     * Update a province.
     *
     * @param provinceDTO the entity to save.
     * @return the persisted entity.
     */
    public ProvinceDTO update(ProvinceDTO provinceDTO) {
        LOG.debug("Request to update Province : {}", provinceDTO);
        Province province = provinceMapper.toEntity(provinceDTO);
        province = provinceRepository.save(province);
        return provinceMapper.toDto(province);
    }

    /**
     * Partially update a province.
     *
     * @param provinceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProvinceDTO> partialUpdate(ProvinceDTO provinceDTO) {
        LOG.debug("Request to partially update Province : {}", provinceDTO);

        return provinceRepository
            .findById(provinceDTO.getId())
            .map(existingProvince -> {
                provinceMapper.partialUpdate(existingProvince, provinceDTO);

                return existingProvince;
            })
            .map(provinceRepository::save)
            .map(provinceMapper::toDto);
    }

    /**
     * Get all the provinces.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProvinceDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Provinces");
        return provinceRepository.findAll(pageable).map(provinceMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<ProvinceDTO> findAllAsDto() {
        LOG.debug("Request to get all Provinces as DTOs");
        return provinceRepository.findAll().stream().map(provinceMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Get one province by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProvinceDTO> findOne(Long id) {
        LOG.debug("Request to get Province : {}", id);
        return provinceRepository.findById(id).map(provinceMapper::toDto);
    }

    /**
     * Delete the province by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Province : {}", id);
        provinceRepository.deleteById(id);
    }
}
