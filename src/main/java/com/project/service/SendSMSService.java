package com.project.service;

import com.project.domain.SendSMS;
import com.project.repository.SendSMSRepository;
import com.project.service.dto.SendSMSDTO;
import com.project.service.mapper.SendSMSMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.project.domain.SendSMS}.
 */
@Service
@Transactional
public class SendSMSService {

    private static final Logger LOG = LoggerFactory.getLogger(SendSMSService.class);

    private final SendSMSRepository sendSMSRepository;

    private final SendSMSMapper sendSMSMapper;

    public SendSMSService(SendSMSRepository sendSMSRepository, SendSMSMapper sendSMSMapper) {
        this.sendSMSRepository = sendSMSRepository;
        this.sendSMSMapper = sendSMSMapper;
    }

    /**
     * Save a sendSMS.
     *
     * @param sendSMSDTO the entity to save.
     * @return the persisted entity.
     */
    public SendSMSDTO save(SendSMSDTO sendSMSDTO) {
        LOG.debug("Request to save SendSMS : {}", sendSMSDTO);
        SendSMS sendSMS = sendSMSMapper.toEntity(sendSMSDTO);
        sendSMS = sendSMSRepository.save(sendSMS);
        return sendSMSMapper.toDto(sendSMS);
    }

    /**
     * Update a sendSMS.
     *
     * @param sendSMSDTO the entity to save.
     * @return the persisted entity.
     */
    public SendSMSDTO update(SendSMSDTO sendSMSDTO) {
        LOG.debug("Request to update SendSMS : {}", sendSMSDTO);
        SendSMS sendSMS = sendSMSMapper.toEntity(sendSMSDTO);
        sendSMS = sendSMSRepository.save(sendSMS);
        return sendSMSMapper.toDto(sendSMS);
    }

    /**
     * Partially update a sendSMS.
     *
     * @param sendSMSDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SendSMSDTO> partialUpdate(SendSMSDTO sendSMSDTO) {
        LOG.debug("Request to partially update SendSMS : {}", sendSMSDTO);

        return sendSMSRepository
            .findById(sendSMSDTO.getId())
            .map(existingSendSMS -> {
                sendSMSMapper.partialUpdate(existingSendSMS, sendSMSDTO);

                return existingSendSMS;
            })
            .map(sendSMSRepository::save)
            .map(sendSMSMapper::toDto);
    }

    /**
     * Get all the sendSMS.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SendSMSDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SendSMS");
        return sendSMSRepository.findAll(pageable).map(sendSMSMapper::toDto);
    }

    /**
     * Get one sendSMS by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SendSMSDTO> findOne(Long id) {
        LOG.debug("Request to get SendSMS : {}", id);
        return sendSMSRepository.findById(id).map(sendSMSMapper::toDto);
    }

    /**
     * Delete the sendSMS by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SendSMS : {}", id);
        sendSMSRepository.deleteById(id);
    }
}
