package com.project.web.rest;

import com.project.repository.HistoryOrderRepository;
import com.project.service.HistoryOrderService;
import com.project.service.dto.HistoryOrderDTO;
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
 * REST controller for managing {@link com.project.domain.HistoryOrder}.
 */
@RestController
@RequestMapping("/api/history-orders")
public class HistoryOrderResource {

    private static final Logger LOG = LoggerFactory.getLogger(HistoryOrderResource.class);

    private static final String ENTITY_NAME = "historyOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HistoryOrderService historyOrderService;

    private final HistoryOrderRepository historyOrderRepository;

    public HistoryOrderResource(HistoryOrderService historyOrderService, HistoryOrderRepository historyOrderRepository) {
        this.historyOrderService = historyOrderService;
        this.historyOrderRepository = historyOrderRepository;
    }

    /**
     * {@code POST  /history-orders} : Create a new historyOrder.
     *
     * @param historyOrderDTO the historyOrderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new historyOrderDTO, or with status {@code 400 (Bad Request)} if the historyOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HistoryOrderDTO> createHistoryOrder(@Valid @RequestBody HistoryOrderDTO historyOrderDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save HistoryOrder : {}", historyOrderDTO);
        if (historyOrderDTO.getId() != null) {
            throw new BadRequestAlertException("A new historyOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        historyOrderDTO = historyOrderService.save(historyOrderDTO);
        return ResponseEntity.created(new URI("/api/history-orders/" + historyOrderDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, historyOrderDTO.getId().toString()))
            .body(historyOrderDTO);
    }

    /**
     * {@code PUT  /history-orders/:id} : Updates an existing historyOrder.
     *
     * @param id the id of the historyOrderDTO to save.
     * @param historyOrderDTO the historyOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historyOrderDTO,
     * or with status {@code 400 (Bad Request)} if the historyOrderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the historyOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HistoryOrderDTO> updateHistoryOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HistoryOrderDTO historyOrderDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update HistoryOrder : {}, {}", id, historyOrderDTO);
        if (historyOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historyOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historyOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        historyOrderDTO = historyOrderService.update(historyOrderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historyOrderDTO.getId().toString()))
            .body(historyOrderDTO);
    }

    /**
     * {@code PATCH  /history-orders/:id} : Partial updates given fields of an existing historyOrder, field will ignore if it is null
     *
     * @param id the id of the historyOrderDTO to save.
     * @param historyOrderDTO the historyOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historyOrderDTO,
     * or with status {@code 400 (Bad Request)} if the historyOrderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the historyOrderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the historyOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HistoryOrderDTO> partialUpdateHistoryOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HistoryOrderDTO historyOrderDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HistoryOrder partially : {}, {}", id, historyOrderDTO);
        if (historyOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historyOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historyOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HistoryOrderDTO> result = historyOrderService.partialUpdate(historyOrderDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historyOrderDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /history-orders} : get all the historyOrders.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historyOrders in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HistoryOrderDTO>> getAllHistoryOrders(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of HistoryOrders");
        Page<HistoryOrderDTO> page = historyOrderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /history-orders/:id} : get the "id" historyOrder.
     *
     * @param id the id of the historyOrderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the historyOrderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HistoryOrderDTO> getHistoryOrder(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HistoryOrder : {}", id);
        Optional<HistoryOrderDTO> historyOrderDTO = historyOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(historyOrderDTO);
    }

    /**
     * {@code DELETE  /history-orders/:id} : delete the "id" historyOrder.
     *
     * @param id the id of the historyOrderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistoryOrder(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HistoryOrder : {}", id);
        historyOrderService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
