package com.project.service;

import com.project.domain.Staff;
import com.project.repository.StaffRepository;
import com.project.service.dto.StaffDTO;
import com.project.service.mapper.StaffMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.project.domain.Staff}.
 */
@Service
@Transactional
public class StaffService {

    private static final Logger LOG = LoggerFactory.getLogger(StaffService.class);

    private final StaffRepository staffRepository;

    private final StaffMapper staffMapper;

    public StaffService(StaffRepository staffRepository, StaffMapper staffMapper) {
        this.staffRepository = staffRepository;
        this.staffMapper = staffMapper;
    }

    /**
     * Save a staff.
     *
     * @param staffDTO the entity to save.
     * @return the persisted entity.
     */
    public StaffDTO save(StaffDTO staffDTO) {
        LOG.debug("Request to save Staff : {}", staffDTO);
        Staff staff = staffMapper.toEntity(staffDTO);
        staff = staffRepository.save(staff);
        return staffMapper.toDto(staff);
    }

    /**
     * Update a staff.
     *
     * @param staffDTO the entity to save.
     * @return the persisted entity.
     */
    public StaffDTO update(StaffDTO staffDTO) {
        LOG.debug("Request to update Staff : {}", staffDTO);
        Staff staff = staffMapper.toEntity(staffDTO);
        staff = staffRepository.save(staff);
        return staffMapper.toDto(staff);
    }

    /**
     * Partially update a staff.
     *
     * @param staffDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StaffDTO> partialUpdate(StaffDTO staffDTO) {
        LOG.debug("Request to partially update Staff : {}", staffDTO);

        return staffRepository
            .findById(staffDTO.getId())
            .map(existingStaff -> {
                staffMapper.partialUpdate(existingStaff, staffDTO);

                return existingStaff;
            })
            .map(staffRepository::save)
            .map(staffMapper::toDto);
    }

    /**
     * Get all the staff.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StaffDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Staff");
        return staffRepository.findAll(pageable).map(staffMapper::toDto);
    }

    /**
     * Get one staff by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StaffDTO> findOne(Long id) {
        LOG.debug("Request to get Staff : {}", id);
        return staffRepository.findById(id).map(staffMapper::toDto);
    }

    /**
     * Delete the staff by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Staff : {}", id);
        staffRepository.deleteById(id);
    }
}
