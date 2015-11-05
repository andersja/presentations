package com.nationwide.harleystore.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nationwide.harleystore.domain.Family;
import com.nationwide.harleystore.repository.FamilyRepository;
import com.nationwide.harleystore.web.rest.util.HeaderUtil;
import com.nationwide.harleystore.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Family.
 */
@RestController
@RequestMapping("/api")
public class FamilyResource {

    private final Logger log = LoggerFactory.getLogger(FamilyResource.class);

    @Inject
    private FamilyRepository familyRepository;

    /**
     * POST  /familys -> Create a new family.
     */
    @RequestMapping(value = "/familys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Family> createFamily(@Valid @RequestBody Family family) throws URISyntaxException {
        log.debug("REST request to save Family : {}", family);
        if (family.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new family cannot already have an ID").body(null);
        }
        Family result = familyRepository.save(family);
        return ResponseEntity.created(new URI("/api/familys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("family", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /familys -> Updates an existing family.
     */
    @RequestMapping(value = "/familys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Family> updateFamily(@Valid @RequestBody Family family) throws URISyntaxException {
        log.debug("REST request to update Family : {}", family);
        if (family.getId() == null) {
            return createFamily(family);
        }
        Family result = familyRepository.save(family);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("family", family.getId().toString()))
            .body(result);
    }

    /**
     * GET  /familys -> get all the familys.
     */
    @RequestMapping(value = "/familys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Family>> getAllFamilys(Pageable pageable)
        throws URISyntaxException {
        Page<Family> page = familyRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/familys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /familys/:id -> get the "id" family.
     */
    @RequestMapping(value = "/familys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Family> getFamily(@PathVariable Long id) {
        log.debug("REST request to get Family : {}", id);
        return Optional.ofNullable(familyRepository.findOne(id))
            .map(family -> new ResponseEntity<>(
                family,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /familys/:id -> delete the "id" family.
     */
    @RequestMapping(value = "/familys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFamily(@PathVariable Long id) {
        log.debug("REST request to delete Family : {}", id);
        familyRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("family", id.toString())).build();
    }
}
