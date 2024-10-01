package com.project.web.rest;

import com.project.repository.SendSMSRepository;
import com.project.service.SendSMSService;
import com.project.service.dto.SendSMSDTO;
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
 * REST controller for managing {@link com.project.domain.SendSMS}.
 */
@RestController
@RequestMapping("/api/send-sms")
public class SendSMSResource {

    private static final Logger LOG = LoggerFactory.getLogger(SendSMSResource.class);

    private static final String ENTITY_NAME = "sendSMS";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SendSMSService sendSMSService;

    private final SendSMSRepository sendSMSRepository;

    public SendSMSResource(SendSMSService sendSMSService, SendSMSRepository sendSMSRepository) {
        this.sendSMSService = sendSMSService;
        this.sendSMSRepository = sendSMSRepository;
    }

    /**
     * {@code POST  /send-sms} : Create a new sendSMS.
     *
     * @param sendSMSDTO the sendSMSDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sendSMSDTO, or with status {@code 400 (Bad Request)} if the sendSMS has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SendSMSDTO> createSendSMS(@Valid @RequestBody SendSMSDTO sendSMSDTO) throws URISyntaxException {
        LOG.debug("REST request to save SendSMS : {}", sendSMSDTO);
        if (sendSMSDTO.getId() != null) {
            throw new BadRequestAlertException("A new sendSMS cannot already have an ID", ENTITY_NAME, "idexists");
        }
        sendSMSDTO = sendSMSService.save(sendSMSDTO);
        return ResponseEntity.created(new URI("/api/send-sms/" + sendSMSDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, sendSMSDTO.getId().toString()))
            .body(sendSMSDTO);
    }

    /**
     * {@code PUT  /send-sms/:id} : Updates an existing sendSMS.
     *
     * @param id the id of the sendSMSDTO to save.
     * @param sendSMSDTO the sendSMSDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sendSMSDTO,
     * or with status {@code 400 (Bad Request)} if the sendSMSDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sendSMSDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SendSMSDTO> updateSendSMS(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SendSMSDTO sendSMSDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SendSMS : {}, {}", id, sendSMSDTO);
        if (sendSMSDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sendSMSDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sendSMSRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        sendSMSDTO = sendSMSService.update(sendSMSDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sendSMSDTO.getId().toString()))
            .body(sendSMSDTO);
    }

    /**
     * {@code PATCH  /send-sms/:id} : Partial updates given fields of an existing sendSMS, field will ignore if it is null
     *
     * @param id the id of the sendSMSDTO to save.
     * @param sendSMSDTO the sendSMSDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sendSMSDTO,
     * or with status {@code 400 (Bad Request)} if the sendSMSDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sendSMSDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sendSMSDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SendSMSDTO> partialUpdateSendSMS(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SendSMSDTO sendSMSDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SendSMS partially : {}, {}", id, sendSMSDTO);
        if (sendSMSDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sendSMSDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sendSMSRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SendSMSDTO> result = sendSMSService.partialUpdate(sendSMSDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sendSMSDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /send-sms} : get all the sendSMS.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sendSMS in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SendSMSDTO>> getAllSendSMS(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of SendSMS");
        Page<SendSMSDTO> page = sendSMSService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /send-sms/:id} : get the "id" sendSMS.
     *
     * @param id the id of the sendSMSDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sendSMSDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SendSMSDTO> getSendSMS(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SendSMS : {}", id);
        Optional<SendSMSDTO> sendSMSDTO = sendSMSService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sendSMSDTO);
    }

    /**
     * {@code DELETE  /send-sms/:id} : delete the "id" sendSMS.
     *
     * @param id the id of the sendSMSDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSendSMS(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SendSMS : {}", id);
        sendSMSService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
