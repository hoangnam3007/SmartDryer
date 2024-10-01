package com.project.service;

import com.project.domain.District;
import com.project.repository.DistrictRepository;
import com.project.service.dto.DistrictDTO;
import com.project.service.mapper.DistrictMapper;
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
 * Service Implementation for managing {@link com.project.domain.District}.
 */
@Service
@Transactional
public class DistrictService {

    private static final Logger LOG = LoggerFactory.getLogger(DistrictService.class);

    private final DistrictRepository districtRepository;

    private final DistrictMapper districtMapper;

    public DistrictService(DistrictRepository districtRepository, DistrictMapper districtMapper) {
        this.districtRepository = districtRepository;
        this.districtMapper = districtMapper;
    }

    /**
     * Save a district.
     *
     * @param districtDTO the entity to save.
     * @return the persisted entity.
     */
    public DistrictDTO save(DistrictDTO districtDTO) {
        LOG.debug("Request to save District : {}", districtDTO);
        District district = districtMapper.toEntity(districtDTO);
        district = districtRepository.save(district);
        return districtMapper.toDto(district);
    }

    /**
     * Update a district.
     *
     * @param districtDTO the entity to save.
     * @return the persisted entity.
     */
    public DistrictDTO update(DistrictDTO districtDTO) {
        LOG.debug("Request to update District : {}", districtDTO);
        District district = districtMapper.toEntity(districtDTO);
        district = districtRepository.save(district);
        return districtMapper.toDto(district);
    }

    /**
     * Partially update a district.
     *
     * @param districtDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DistrictDTO> partialUpdate(DistrictDTO districtDTO) {
        LOG.debug("Request to partially update District : {}", districtDTO);

        return districtRepository
            .findById(districtDTO.getId())
            .map(existingDistrict -> {
                districtMapper.partialUpdate(existingDistrict, districtDTO);

                return existingDistrict;
            })
            .map(districtRepository::save)
            .map(districtMapper::toDto);
    }

    /**
     * Get all the districts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DistrictDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Districts");
        return districtRepository.findAll(pageable).map(districtMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<DistrictDTO> findAllDTO() {
        return districtRepository.findAll().stream().map(districtMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Get one district by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DistrictDTO> findOne(Long id) {
        LOG.debug("Request to get District : {}", id);
        return districtRepository.findById(id).map(districtMapper::toDto);
    }

    /**
     * Delete the district by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete District : {}", id);
        districtRepository.deleteById(id);
    }
}
