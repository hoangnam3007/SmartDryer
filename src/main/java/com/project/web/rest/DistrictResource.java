package com.project.web.rest;

import com.project.repository.DistrictRepository;
import com.project.service.DistrictService;
import com.project.service.dto.DistrictDTO;
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
 * REST controller for managing {@link com.project.domain.District}.
 */
@RestController
@RequestMapping("/api/districts")
public class DistrictResource {

    private static final Logger LOG = LoggerFactory.getLogger(DistrictResource.class);

    private static final String ENTITY_NAME = "district";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DistrictService districtService;

    private final DistrictRepository districtRepository;

    public DistrictResource(DistrictService districtService, DistrictRepository districtRepository) {
        this.districtService = districtService;
        this.districtRepository = districtRepository;
    }

    /**
     * {@code POST  /districts} : Create a new district.
     *
     * @param districtDTO the districtDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new districtDTO, or with status {@code 400 (Bad Request)} if the district has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DistrictDTO> createDistrict(@Valid @RequestBody DistrictDTO districtDTO) throws URISyntaxException {
        LOG.debug("REST request to save District : {}", districtDTO);
        if (districtDTO.getId() != null) {
            throw new BadRequestAlertException("A new district cannot already have an ID", ENTITY_NAME, "idexists");
        }
        districtDTO = districtService.save(districtDTO);
        return ResponseEntity.created(new URI("/api/districts/" + districtDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, districtDTO.getId().toString()))
            .body(districtDTO);
    }

    /**
     * {@code PUT  /districts/:id} : Updates an existing district.
     *
     * @param id the id of the districtDTO to save.
     * @param districtDTO the districtDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated districtDTO,
     * or with status {@code 400 (Bad Request)} if the districtDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the districtDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DistrictDTO> updateDistrict(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DistrictDTO districtDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update District : {}, {}", id, districtDTO);
        if (districtDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, districtDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!districtRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        districtDTO = districtService.update(districtDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, districtDTO.getId().toString()))
            .body(districtDTO);
    }

    /**
     * {@code PATCH  /districts/:id} : Partial updates given fields of an existing district, field will ignore if it is null
     *
     * @param id the id of the districtDTO to save.
     * @param districtDTO the districtDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated districtDTO,
     * or with status {@code 400 (Bad Request)} if the districtDTO is not valid,
     * or with status {@code 404 (Not Found)} if the districtDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the districtDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DistrictDTO> partialUpdateDistrict(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DistrictDTO districtDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update District partially : {}, {}", id, districtDTO);
        if (districtDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, districtDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!districtRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DistrictDTO> result = districtService.partialUpdate(districtDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, districtDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /districts} : get all the districts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of districts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DistrictDTO>> getAllDistricts(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Districts");
        Page<DistrictDTO> page = districtService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /districts/:id} : get the "id" district.
     *
     * @param id the id of the districtDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the districtDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DistrictDTO> getDistrict(@PathVariable("id") Long id) {
        LOG.debug("REST request to get District : {}", id);
        Optional<DistrictDTO> districtDTO = districtService.findOne(id);
        return ResponseUtil.wrapOrNotFound(districtDTO);
    }

    /**
     * {@code DELETE  /districts/:id} : delete the "id" district.
     *
     * @param id the id of the districtDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDistrict(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete District : {}", id);
        districtService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
