package com.project.service;

import com.project.domain.Ward;
import com.project.repository.WardRepository;
import com.project.service.dto.WardDTO;
import com.project.service.mapper.WardMapper;
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
 * Service Implementation for managing {@link com.project.domain.Ward}.
 */
@Service
@Transactional
public class WardService {

    private static final Logger LOG = LoggerFactory.getLogger(WardService.class);

    private final WardRepository wardRepository;

    private final WardMapper wardMapper;

    public WardService(WardRepository wardRepository, WardMapper wardMapper) {
        this.wardRepository = wardRepository;
        this.wardMapper = wardMapper;
    }

    /**
     * Save a ward.
     *
     * @param wardDTO the entity to save.
     * @return the persisted entity.
     */
    public WardDTO save(WardDTO wardDTO) {
        LOG.debug("Request to save Ward : {}", wardDTO);
        Ward ward = wardMapper.toEntity(wardDTO);
        ward = wardRepository.save(ward);
        return wardMapper.toDto(ward);
    }

    /**
     * Update a ward.
     *
     * @param wardDTO the entity to save.
     * @return the persisted entity.
     */
    public WardDTO update(WardDTO wardDTO) {
        LOG.debug("Request to update Ward : {}", wardDTO);
        Ward ward = wardMapper.toEntity(wardDTO);
        ward = wardRepository.save(ward);
        return wardMapper.toDto(ward);
    }

    /**
     * Partially update a ward.
     *
     * @param wardDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WardDTO> partialUpdate(WardDTO wardDTO) {
        LOG.debug("Request to partially update Ward : {}", wardDTO);

        return wardRepository
            .findById(wardDTO.getId())
            .map(existingWard -> {
                wardMapper.partialUpdate(existingWard, wardDTO);

                return existingWard;
            })
            .map(wardRepository::save)
            .map(wardMapper::toDto);
    }

    /**
     * Get all the wards.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WardDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Wards");
        return wardRepository.findAll(pageable).map(wardMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<WardDTO> findAllDTO() {
        return wardRepository.findAll().stream().map(wardMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Get one ward by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WardDTO> findOne(Long id) {
        LOG.debug("Request to get Ward : {}", id);
        return wardRepository.findById(id).map(wardMapper::toDto);
    }

    /**
     * Delete the ward by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Ward : {}", id);
        wardRepository.deleteById(id);
    }
}
