package com.project.service;

import com.project.domain.HistoryOrder;
import com.project.repository.HistoryOrderRepository;
import com.project.service.dto.HistoryOrderDTO;
import com.project.service.mapper.HistoryOrderMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.project.domain.HistoryOrder}.
 */
@Service
@Transactional
public class HistoryOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(HistoryOrderService.class);

    private final HistoryOrderRepository historyOrderRepository;

    private final HistoryOrderMapper historyOrderMapper;

    public HistoryOrderService(HistoryOrderRepository historyOrderRepository, HistoryOrderMapper historyOrderMapper) {
        this.historyOrderRepository = historyOrderRepository;
        this.historyOrderMapper = historyOrderMapper;
    }

    /**
     * Save a historyOrder.
     *
     * @param historyOrderDTO the entity to save.
     * @return the persisted entity.
     */
    public HistoryOrderDTO save(HistoryOrderDTO historyOrderDTO) {
        LOG.debug("Request to save HistoryOrder : {}", historyOrderDTO);
        HistoryOrder historyOrder = historyOrderMapper.toEntity(historyOrderDTO);
        historyOrder = historyOrderRepository.save(historyOrder);
        return historyOrderMapper.toDto(historyOrder);
    }

    /**
     * Update a historyOrder.
     *
     * @param historyOrderDTO the entity to save.
     * @return the persisted entity.
     */
    public HistoryOrderDTO update(HistoryOrderDTO historyOrderDTO) {
        LOG.debug("Request to update HistoryOrder : {}", historyOrderDTO);
        HistoryOrder historyOrder = historyOrderMapper.toEntity(historyOrderDTO);
        historyOrder = historyOrderRepository.save(historyOrder);
        return historyOrderMapper.toDto(historyOrder);
    }

    /**
     * Partially update a historyOrder.
     *
     * @param historyOrderDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HistoryOrderDTO> partialUpdate(HistoryOrderDTO historyOrderDTO) {
        LOG.debug("Request to partially update HistoryOrder : {}", historyOrderDTO);

        return historyOrderRepository
            .findById(historyOrderDTO.getId())
            .map(existingHistoryOrder -> {
                historyOrderMapper.partialUpdate(existingHistoryOrder, historyOrderDTO);

                return existingHistoryOrder;
            })
            .map(historyOrderRepository::save)
            .map(historyOrderMapper::toDto);
    }

    /**
     * Get all the historyOrders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<HistoryOrderDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all HistoryOrders");
        return historyOrderRepository.findAll(pageable).map(historyOrderMapper::toDto);
    }

    /**
     * Get one historyOrder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HistoryOrderDTO> findOne(Long id) {
        LOG.debug("Request to get HistoryOrder : {}", id);
        return historyOrderRepository.findById(id).map(historyOrderMapper::toDto);
    }

    /**
     * Delete the historyOrder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete HistoryOrder : {}", id);
        historyOrderRepository.deleteById(id);
    }
}
