package com.project.service;

import com.project.domain.SourceOrder;
import com.project.repository.SourceOrderRepository;
import com.project.service.dto.SourceOrderDTO;
import com.project.service.mapper.SourceOrderMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.project.domain.SourceOrder}.
 */
@Service
@Transactional
public class SourceOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(SourceOrderService.class);

    private final SourceOrderRepository sourceOrderRepository;

    private final SourceOrderMapper sourceOrderMapper;

    public SourceOrderService(SourceOrderRepository sourceOrderRepository, SourceOrderMapper sourceOrderMapper) {
        this.sourceOrderRepository = sourceOrderRepository;
        this.sourceOrderMapper = sourceOrderMapper;
    }

    /**
     * Save a sourceOrder.
     *
     * @param sourceOrderDTO the entity to save.
     * @return the persisted entity.
     */
    public SourceOrderDTO save(SourceOrderDTO sourceOrderDTO) {
        LOG.debug("Request to save SourceOrder : {}", sourceOrderDTO);
        SourceOrder sourceOrder = sourceOrderMapper.toEntity(sourceOrderDTO);
        sourceOrder = sourceOrderRepository.save(sourceOrder);
        return sourceOrderMapper.toDto(sourceOrder);
    }

    /**
     * Update a sourceOrder.
     *
     * @param sourceOrderDTO the entity to save.
     * @return the persisted entity.
     */
    public SourceOrderDTO update(SourceOrderDTO sourceOrderDTO) {
        LOG.debug("Request to update SourceOrder : {}", sourceOrderDTO);
        SourceOrder sourceOrder = sourceOrderMapper.toEntity(sourceOrderDTO);
        sourceOrder = sourceOrderRepository.save(sourceOrder);
        return sourceOrderMapper.toDto(sourceOrder);
    }

    /**
     * Partially update a sourceOrder.
     *
     * @param sourceOrderDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SourceOrderDTO> partialUpdate(SourceOrderDTO sourceOrderDTO) {
        LOG.debug("Request to partially update SourceOrder : {}", sourceOrderDTO);

        return sourceOrderRepository
            .findById(sourceOrderDTO.getId())
            .map(existingSourceOrder -> {
                sourceOrderMapper.partialUpdate(existingSourceOrder, sourceOrderDTO);

                return existingSourceOrder;
            })
            .map(sourceOrderRepository::save)
            .map(sourceOrderMapper::toDto);
    }

    /**
     * Get all the sourceOrders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SourceOrderDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SourceOrders");
        return sourceOrderRepository.findAll(pageable).map(sourceOrderMapper::toDto);
    }

    /**
     * Get one sourceOrder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SourceOrderDTO> findOne(Long id) {
        LOG.debug("Request to get SourceOrder : {}", id);
        return sourceOrderRepository.findById(id).map(sourceOrderMapper::toDto);
    }

    /**
     * Delete the sourceOrder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SourceOrder : {}", id);
        sourceOrderRepository.deleteById(id);
    }
}
