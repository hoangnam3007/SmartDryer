package com.project.web.rest;

import com.project.repository.SaleRepository;
import com.project.service.SaleService;
import com.project.service.dto.SaleDTO;
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
 * REST controller for managing {@link com.project.domain.Sale}.
 */
@RestController
@RequestMapping("/api/sales")
public class SaleResource {

    private static final Logger LOG = LoggerFactory.getLogger(SaleResource.class);

    private static final String ENTITY_NAME = "sale";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SaleService saleService;

    private final SaleRepository saleRepository;

    public SaleResource(SaleService saleService, SaleRepository saleRepository) {
        this.saleService = saleService;
        this.saleRepository = saleRepository;
    }

    /**
     * {@code POST  /sales} : Create a new sale.
     *
     * @param saleDTO the saleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new saleDTO, or with status {@code 400 (Bad Request)} if the sale has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SaleDTO> createSale(@Valid @RequestBody SaleDTO saleDTO) throws URISyntaxException {
        LOG.debug("REST request to save Sale : {}", saleDTO);
        if (saleDTO.getId() != null) {
            throw new BadRequestAlertException("A new sale cannot already have an ID", ENTITY_NAME, "idexists");
        }
        saleDTO = saleService.save(saleDTO);
        return ResponseEntity.created(new URI("/api/sales/" + saleDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, saleDTO.getId().toString()))
            .body(saleDTO);
    }

    /**
     * {@code PUT  /sales/:id} : Updates an existing sale.
     *
     * @param id the id of the saleDTO to save.
     * @param saleDTO the saleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated saleDTO,
     * or with status {@code 400 (Bad Request)} if the saleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the saleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SaleDTO> updateSale(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SaleDTO saleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Sale : {}, {}", id, saleDTO);
        if (saleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, saleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!saleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        saleDTO = saleService.update(saleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, saleDTO.getId().toString()))
            .body(saleDTO);
    }

    /**
     * {@code PATCH  /sales/:id} : Partial updates given fields of an existing sale, field will ignore if it is null
     *
     * @param id the id of the saleDTO to save.
     * @param saleDTO the saleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated saleDTO,
     * or with status {@code 400 (Bad Request)} if the saleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the saleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the saleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SaleDTO> partialUpdateSale(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SaleDTO saleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Sale partially : {}, {}", id, saleDTO);
        if (saleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, saleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!saleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SaleDTO> result = saleService.partialUpdate(saleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, saleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sales} : get all the sales.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sales in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SaleDTO>> getAllSales(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Sales");
        Page<SaleDTO> page = saleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sales/:id} : get the "id" sale.
     *
     * @param id the id of the saleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the saleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SaleDTO> getSale(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Sale : {}", id);
        Optional<SaleDTO> saleDTO = saleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(saleDTO);
    }

    /**
     * {@code DELETE  /sales/:id} : delete the "id" sale.
     *
     * @param id the id of the saleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Sale : {}", id);
        saleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
