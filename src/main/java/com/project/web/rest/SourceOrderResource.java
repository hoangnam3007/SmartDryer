package com.project.web.rest;

import com.project.repository.SourceOrderRepository;
import com.project.service.SourceOrderService;
import com.project.service.dto.SourceOrderDTO;
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
 * REST controller for managing {@link com.project.domain.SourceOrder}.
 */
@RestController
@RequestMapping("/api/source-orders")
public class SourceOrderResource {

    private static final Logger LOG = LoggerFactory.getLogger(SourceOrderResource.class);

    private static final String ENTITY_NAME = "sourceOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SourceOrderService sourceOrderService;

    private final SourceOrderRepository sourceOrderRepository;

    public SourceOrderResource(SourceOrderService sourceOrderService, SourceOrderRepository sourceOrderRepository) {
        this.sourceOrderService = sourceOrderService;
        this.sourceOrderRepository = sourceOrderRepository;
    }

    /**
     * {@code POST  /source-orders} : Create a new sourceOrder.
     *
     * @param sourceOrderDTO the sourceOrderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sourceOrderDTO, or with status {@code 400 (Bad Request)} if the sourceOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SourceOrderDTO> createSourceOrder(@Valid @RequestBody SourceOrderDTO sourceOrderDTO) throws URISyntaxException {
        LOG.debug("REST request to save SourceOrder : {}", sourceOrderDTO);
        if (sourceOrderDTO.getId() != null) {
            throw new BadRequestAlertException("A new sourceOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        sourceOrderDTO = sourceOrderService.save(sourceOrderDTO);
        return ResponseEntity.created(new URI("/api/source-orders/" + sourceOrderDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, sourceOrderDTO.getId().toString()))
            .body(sourceOrderDTO);
    }

    /**
     * {@code PUT  /source-orders/:id} : Updates an existing sourceOrder.
     *
     * @param id the id of the sourceOrderDTO to save.
     * @param sourceOrderDTO the sourceOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sourceOrderDTO,
     * or with status {@code 400 (Bad Request)} if the sourceOrderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sourceOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SourceOrderDTO> updateSourceOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SourceOrderDTO sourceOrderDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SourceOrder : {}, {}", id, sourceOrderDTO);
        if (sourceOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sourceOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sourceOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        sourceOrderDTO = sourceOrderService.update(sourceOrderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sourceOrderDTO.getId().toString()))
            .body(sourceOrderDTO);
    }

    /**
     * {@code PATCH  /source-orders/:id} : Partial updates given fields of an existing sourceOrder, field will ignore if it is null
     *
     * @param id the id of the sourceOrderDTO to save.
     * @param sourceOrderDTO the sourceOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sourceOrderDTO,
     * or with status {@code 400 (Bad Request)} if the sourceOrderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sourceOrderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sourceOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SourceOrderDTO> partialUpdateSourceOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SourceOrderDTO sourceOrderDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SourceOrder partially : {}, {}", id, sourceOrderDTO);
        if (sourceOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sourceOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sourceOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SourceOrderDTO> result = sourceOrderService.partialUpdate(sourceOrderDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sourceOrderDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /source-orders} : get all the sourceOrders.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sourceOrders in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SourceOrderDTO>> getAllSourceOrders(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of SourceOrders");
        Page<SourceOrderDTO> page = sourceOrderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /source-orders/:id} : get the "id" sourceOrder.
     *
     * @param id the id of the sourceOrderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sourceOrderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SourceOrderDTO> getSourceOrder(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SourceOrder : {}", id);
        Optional<SourceOrderDTO> sourceOrderDTO = sourceOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sourceOrderDTO);
    }

    /**
     * {@code DELETE  /source-orders/:id} : delete the "id" sourceOrder.
     *
     * @param id the id of the sourceOrderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSourceOrder(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SourceOrder : {}", id);
        sourceOrderService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
