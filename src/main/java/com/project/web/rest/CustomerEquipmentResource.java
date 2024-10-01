package com.project.web.rest;

import com.project.repository.CustomerEquipmentRepository;
import com.project.service.CustomerEquipmentService;
import com.project.service.dto.CustomerEquipmentDTO;
import com.project.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.project.domain.CustomerEquipment}.
 */
@RestController
@RequestMapping("/api/customer-equipments")
public class CustomerEquipmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerEquipmentResource.class);

    private static final String ENTITY_NAME = "customerEquipment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerEquipmentService customerEquipmentService;

    private final CustomerEquipmentRepository customerEquipmentRepository;

    public CustomerEquipmentResource(
        CustomerEquipmentService customerEquipmentService,
        CustomerEquipmentRepository customerEquipmentRepository
    ) {
        this.customerEquipmentService = customerEquipmentService;
        this.customerEquipmentRepository = customerEquipmentRepository;
    }

    /**
     * {@code POST  /customer-equipments} : Create a new customerEquipment.
     *
     * @param customerEquipmentDTO the customerEquipmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerEquipmentDTO, or with status {@code 400 (Bad Request)} if the customerEquipment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CustomerEquipmentDTO> createCustomerEquipment(@RequestBody CustomerEquipmentDTO customerEquipmentDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CustomerEquipment : {}", customerEquipmentDTO);
        if (customerEquipmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerEquipment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        customerEquipmentDTO = customerEquipmentService.save(customerEquipmentDTO);
        return ResponseEntity.created(new URI("/api/customer-equipments/" + customerEquipmentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, customerEquipmentDTO.getId().toString()))
            .body(customerEquipmentDTO);
    }

    /**
     * {@code PUT  /customer-equipments/:id} : Updates an existing customerEquipment.
     *
     * @param id the id of the customerEquipmentDTO to save.
     * @param customerEquipmentDTO the customerEquipmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerEquipmentDTO,
     * or with status {@code 400 (Bad Request)} if the customerEquipmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerEquipmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerEquipmentDTO> updateCustomerEquipment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CustomerEquipmentDTO customerEquipmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CustomerEquipment : {}, {}", id, customerEquipmentDTO);
        if (customerEquipmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerEquipmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerEquipmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        customerEquipmentDTO = customerEquipmentService.update(customerEquipmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerEquipmentDTO.getId().toString()))
            .body(customerEquipmentDTO);
    }

    /**
     * {@code PATCH  /customer-equipments/:id} : Partial updates given fields of an existing customerEquipment, field will ignore if it is null
     *
     * @param id the id of the customerEquipmentDTO to save.
     * @param customerEquipmentDTO the customerEquipmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerEquipmentDTO,
     * or with status {@code 400 (Bad Request)} if the customerEquipmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customerEquipmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerEquipmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerEquipmentDTO> partialUpdateCustomerEquipment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CustomerEquipmentDTO customerEquipmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CustomerEquipment partially : {}, {}", id, customerEquipmentDTO);
        if (customerEquipmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerEquipmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerEquipmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerEquipmentDTO> result = customerEquipmentService.partialUpdate(customerEquipmentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerEquipmentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /customer-equipments} : get all the customerEquipments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerEquipments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CustomerEquipmentDTO>> getAllCustomerEquipments(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of CustomerEquipments");
        Page<CustomerEquipmentDTO> page = customerEquipmentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customer-equipments/:id} : get the "id" customerEquipment.
     *
     * @param id the id of the customerEquipmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerEquipmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerEquipmentDTO> getCustomerEquipment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CustomerEquipment : {}", id);
        Optional<CustomerEquipmentDTO> customerEquipmentDTO = customerEquipmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerEquipmentDTO);
    }

    /**
     * {@code DELETE  /customer-equipments/:id} : delete the "id" customerEquipment.
     *
     * @param id the id of the customerEquipmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerEquipment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CustomerEquipment : {}", id);
        customerEquipmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
