package com.nationwide.harleystore.web.rest;

import com.nationwide.harleystore.Application;
import com.nationwide.harleystore.domain.Model;
import com.nationwide.harleystore.repository.ModelRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ModelResource REST controller.
 *
 * @see ModelResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ModelResourceTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(1);

    @Inject
    private ModelRepository modelRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restModelMockMvc;

    private Model model;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ModelResource modelResource = new ModelResource();
        ReflectionTestUtils.setField(modelResource, "modelRepository", modelRepository);
        this.restModelMockMvc = MockMvcBuilders.standaloneSetup(modelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        model = new Model();
        model.setName(DEFAULT_NAME);
        model.setPrice(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    public void createModel() throws Exception {
        int databaseSizeBeforeCreate = modelRepository.findAll().size();

        // Create the Model

        restModelMockMvc.perform(post("/api/models")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(model)))
                .andExpect(status().isCreated());

        // Validate the Model in the database
        List<Model> models = modelRepository.findAll();
        assertThat(models).hasSize(databaseSizeBeforeCreate + 1);
        Model testModel = models.get(models.size() - 1);
        assertThat(testModel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testModel.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelRepository.findAll().size();
        // set the field null
        model.setName(null);

        // Create the Model, which fails.

        restModelMockMvc.perform(post("/api/models")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(model)))
                .andExpect(status().isBadRequest());

        List<Model> models = modelRepository.findAll();
        assertThat(models).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelRepository.findAll().size();
        // set the field null
        model.setPrice(null);

        // Create the Model, which fails.

        restModelMockMvc.perform(post("/api/models")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(model)))
                .andExpect(status().isBadRequest());

        List<Model> models = modelRepository.findAll();
        assertThat(models).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllModels() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the models
        restModelMockMvc.perform(get("/api/models"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(model.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())));
    }

    @Test
    @Transactional
    public void getModel() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get the model
        restModelMockMvc.perform(get("/api/models/{id}", model.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(model.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingModel() throws Exception {
        // Get the model
        restModelMockMvc.perform(get("/api/models/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateModel() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

		int databaseSizeBeforeUpdate = modelRepository.findAll().size();

        // Update the model
        model.setName(UPDATED_NAME);
        model.setPrice(UPDATED_PRICE);

        restModelMockMvc.perform(put("/api/models")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(model)))
                .andExpect(status().isOk());

        // Validate the Model in the database
        List<Model> models = modelRepository.findAll();
        assertThat(models).hasSize(databaseSizeBeforeUpdate);
        Model testModel = models.get(models.size() - 1);
        assertThat(testModel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testModel.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void deleteModel() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

		int databaseSizeBeforeDelete = modelRepository.findAll().size();

        // Get the model
        restModelMockMvc.perform(delete("/api/models/{id}", model.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Model> models = modelRepository.findAll();
        assertThat(models).hasSize(databaseSizeBeforeDelete - 1);
    }
}
