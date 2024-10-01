package com.project.service;

import com.project.domain.CusNote;
import com.project.repository.CusNoteRepository;
import com.project.service.dto.CusNoteDTO;
import com.project.service.mapper.CusNoteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.project.domain.CusNote}.
 */
@Service
@Transactional
public class CusNoteService {

    private static final Logger LOG = LoggerFactory.getLogger(CusNoteService.class);

    private final CusNoteRepository cusNoteRepository;

    private final CusNoteMapper cusNoteMapper;

    public CusNoteService(CusNoteRepository cusNoteRepository, CusNoteMapper cusNoteMapper) {
        this.cusNoteRepository = cusNoteRepository;
        this.cusNoteMapper = cusNoteMapper;
    }

    /**
     * Save a cusNote.
     *
     * @param cusNoteDTO the entity to save.
     * @return the persisted entity.
     */
    public CusNoteDTO save(CusNoteDTO cusNoteDTO) {
        LOG.debug("Request to save CusNote : {}", cusNoteDTO);
        CusNote cusNote = cusNoteMapper.toEntity(cusNoteDTO);
        cusNote = cusNoteRepository.save(cusNote);
        return cusNoteMapper.toDto(cusNote);
    }

    /**
     * Update a cusNote.
     *
     * @param cusNoteDTO the entity to save.
     * @return the persisted entity.
     */
    public CusNoteDTO update(CusNoteDTO cusNoteDTO) {
        LOG.debug("Request to update CusNote : {}", cusNoteDTO);
        CusNote cusNote = cusNoteMapper.toEntity(cusNoteDTO);
        cusNote = cusNoteRepository.save(cusNote);
        return cusNoteMapper.toDto(cusNote);
    }

    /**
     * Partially update a cusNote.
     *
     * @param cusNoteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CusNoteDTO> partialUpdate(CusNoteDTO cusNoteDTO) {
        LOG.debug("Request to partially update CusNote : {}", cusNoteDTO);

        return cusNoteRepository
            .findById(cusNoteDTO.getId())
            .map(existingCusNote -> {
                cusNoteMapper.partialUpdate(existingCusNote, cusNoteDTO);

                return existingCusNote;
            })
            .map(cusNoteRepository::save)
            .map(cusNoteMapper::toDto);
    }

    /**
     * Get all the cusNotes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CusNoteDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CusNotes");
        return cusNoteRepository.findAll(pageable).map(cusNoteMapper::toDto);
    }

    /**
     * Get one cusNote by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CusNoteDTO> findOne(Long id) {
        LOG.debug("Request to get CusNote : {}", id);
        return cusNoteRepository.findById(id).map(cusNoteMapper::toDto);
    }

    /**
     * Delete the cusNote by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CusNote : {}", id);
        cusNoteRepository.deleteById(id);
    }
}
