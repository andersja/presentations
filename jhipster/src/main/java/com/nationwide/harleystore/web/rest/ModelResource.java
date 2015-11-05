package com.nationwide.harleystore.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nationwide.harleystore.domain.Model;
import com.nationwide.harleystore.repository.ModelRepository;
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
 * REST controller for managing Model.
 */
@RestController
@RequestMapping("/api")
public class ModelResource {

    private final Logger log = LoggerFactory.getLogger(ModelResource.class);

    @Inject
    private ModelRepository modelRepository;

    /**
     * POST  /models -> Create a new model.
     */
    @RequestMapping(value = "/models",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Model> createModel(@Valid @RequestBody Model model) throws URISyntaxException {
        log.debug("REST request to save Model : {}", model);
        if (model.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new model cannot already have an ID").body(null);
        }
        Model result = modelRepository.save(model);
        return ResponseEntity.created(new URI("/api/models/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("model", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /models -> Updates an existing model.
     */
    @RequestMapping(value = "/models",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Model> updateModel(@Valid @RequestBody Model model) throws URISyntaxException {
        log.debug("REST request to update Model : {}", model);
        if (model.getId() == null) {
            return createModel(model);
        }
        Model result = modelRepository.save(model);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("model", model.getId().toString()))
            .body(result);
    }

    /**
     * GET  /models -> get all the models.
     */
    @RequestMapping(value = "/models",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Model>> getAllModels(Pageable pageable)
        throws URISyntaxException {
        Page<Model> page = modelRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/models");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /models/:id -> get the "id" model.
     */
    @RequestMapping(value = "/models/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Model> getModel(@PathVariable Long id) {
        log.debug("REST request to get Model : {}", id);
        return Optional.ofNullable(modelRepository.findOne(id))
            .map(model -> new ResponseEntity<>(
                model,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /models/:id -> delete the "id" model.
     */
    @RequestMapping(value = "/models/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteModel(@PathVariable Long id) {
        log.debug("REST request to delete Model : {}", id);
        modelRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("model", id.toString())).build();
    }
}
