package com.project.service;

import com.project.domain.CustomerEquipment;
import com.project.repository.CustomerEquipmentRepository;
import com.project.service.dto.CustomerEquipmentDTO;
import com.project.service.mapper.CustomerEquipmentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.project.domain.CustomerEquipment}.
 */
@Service
@Transactional
public class CustomerEquipmentService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerEquipmentService.class);

    private final CustomerEquipmentRepository customerEquipmentRepository;

    private final CustomerEquipmentMapper customerEquipmentMapper;

    public CustomerEquipmentService(
        CustomerEquipmentRepository customerEquipmentRepository,
        CustomerEquipmentMapper customerEquipmentMapper
    ) {
        this.customerEquipmentRepository = customerEquipmentRepository;
        this.customerEquipmentMapper = customerEquipmentMapper;
    }

    /**
     * Save a customerEquipment.
     *
     * @param customerEquipmentDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerEquipmentDTO save(CustomerEquipmentDTO customerEquipmentDTO) {
        LOG.debug("Request to save CustomerEquipment : {}", customerEquipmentDTO);
        CustomerEquipment customerEquipment = customerEquipmentMapper.toEntity(customerEquipmentDTO);
        customerEquipment = customerEquipmentRepository.save(customerEquipment);
        return customerEquipmentMapper.toDto(customerEquipment);
    }

    /**
     * Update a customerEquipment.
     *
     * @param customerEquipmentDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerEquipmentDTO update(CustomerEquipmentDTO customerEquipmentDTO) {
        LOG.debug("Request to update CustomerEquipment : {}", customerEquipmentDTO);
        CustomerEquipment customerEquipment = customerEquipmentMapper.toEntity(customerEquipmentDTO);
        customerEquipment = customerEquipmentRepository.save(customerEquipment);
        return customerEquipmentMapper.toDto(customerEquipment);
    }

    /**
     * Partially update a customerEquipment.
     *
     * @param customerEquipmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomerEquipmentDTO> partialUpdate(CustomerEquipmentDTO customerEquipmentDTO) {
        LOG.debug("Request to partially update CustomerEquipment : {}", customerEquipmentDTO);

        return customerEquipmentRepository
            .findById(customerEquipmentDTO.getId())
            .map(existingCustomerEquipment -> {
                customerEquipmentMapper.partialUpdate(existingCustomerEquipment, customerEquipmentDTO);

                return existingCustomerEquipment;
            })
            .map(customerEquipmentRepository::save)
            .map(customerEquipmentMapper::toDto);
    }

    /**
     * Get all the customerEquipments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerEquipmentDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CustomerEquipments");
        return customerEquipmentRepository.findAll(pageable).map(customerEquipmentMapper::toDto);
    }

    /**
     * Get one customerEquipment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerEquipmentDTO> findOne(Long id) {
        LOG.debug("Request to get CustomerEquipment : {}", id);
        return customerEquipmentRepository.findById(id).map(customerEquipmentMapper::toDto);
    }

    /**
     * Delete the customerEquipment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CustomerEquipment : {}", id);
        customerEquipmentRepository.deleteById(id);
    }
}
