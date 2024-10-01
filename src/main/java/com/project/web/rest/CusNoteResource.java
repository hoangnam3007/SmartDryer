package com.project.web.rest;

import com.project.repository.CusNoteRepository;
import com.project.service.CusNoteService;
import com.project.service.dto.CusNoteDTO;
import com.project.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.project.domain.CusNote}.
 */
@RestController
@RequestMapping("/api/cus-notes")
public class CusNoteResource {

    private static final Logger LOG = LoggerFactory.getLogger(CusNoteResource.class);

    private static final String ENTITY_NAME = "cusNote";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CusNoteService cusNoteService;

    private final CusNoteRepository cusNoteRepository;

    public CusNoteResource(CusNoteService cusNoteService, CusNoteRepository cusNoteRepository) {
        this.cusNoteService = cusNoteService;
        this.cusNoteRepository = cusNoteRepository;
    }

    /**
     * {@code POST  /cus-notes} : Create a new cusNote.
     *
     * @param cusNoteDTO the cusNoteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cusNoteDTO, or with status {@code 400 (Bad Request)} if the cusNote has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CusNoteDTO> createCusNote(@Valid @RequestBody CusNoteDTO cusNoteDTO) throws URISyntaxException {
        LOG.debug("REST request to save CusNote : {}", cusNoteDTO);
        if (cusNoteDTO.getId() != null) {
            throw new BadRequestAlertException("A new cusNote cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cusNoteDTO = cusNoteService.save(cusNoteDTO);
        return ResponseEntity.created(new URI("/api/cus-notes/" + cusNoteDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cusNoteDTO.getId().toString()))
            .body(cusNoteDTO);
    }

    /**
     * {@code PUT  /cus-notes/:id} : Updates an existing cusNote.
     *
     * @param id the id of the cusNoteDTO to save.
     * @param cusNoteDTO the cusNoteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cusNoteDTO,
     * or with status {@code 400 (Bad Request)} if the cusNoteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cusNoteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CusNoteDTO> updateCusNote(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CusNoteDTO cusNoteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CusNote : {}, {}", id, cusNoteDTO);
        if (cusNoteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cusNoteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cusNoteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cusNoteDTO = cusNoteService.update(cusNoteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cusNoteDTO.getId().toString()))
            .body(cusNoteDTO);
    }

    /**
     * {@code PATCH  /cus-notes/:id} : Partial updates given fields of an existing cusNote, field will ignore if it is null
     *
     * @param id the id of the cusNoteDTO to save.
     * @param cusNoteDTO the cusNoteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cusNoteDTO,
     * or with status {@code 400 (Bad Request)} if the cusNoteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cusNoteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cusNoteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CusNoteDTO> partialUpdateCusNote(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CusNoteDTO cusNoteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CusNote partially : {}, {}", id, cusNoteDTO);
        if (cusNoteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cusNoteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cusNoteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CusNoteDTO> result = cusNoteService.partialUpdate(cusNoteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cusNoteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cus-notes} : get all the cusNotes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cusNotes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CusNoteDTO>> getAllCusNotes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of CusNotes");
        Page<CusNoteDTO> page = cusNoteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cus-notes/:id} : get the "id" cusNote.
     *
     * @param id the id of the cusNoteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cusNoteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CusNoteDTO> getCusNote(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CusNote : {}", id);
        Optional<CusNoteDTO> cusNoteDTO = cusNoteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cusNoteDTO);
    }

    /**
     * {@code DELETE  /cus-notes/:id} : delete the "id" cusNote.
     *
     * @param id the id of the cusNoteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCusNote(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CusNote : {}", id);
        cusNoteService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
