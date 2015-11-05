package com.nationwide.harleystore.web.rest;

import com.nationwide.harleystore.Application;
import com.nationwide.harleystore.domain.Family;
import com.nationwide.harleystore.repository.FamilyRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the FamilyResource REST controller.
 *
 * @see FamilyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FamilyResourceTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private FamilyRepository familyRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFamilyMockMvc;

    private Family family;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FamilyResource familyResource = new FamilyResource();
        ReflectionTestUtils.setField(familyResource, "familyRepository", familyRepository);
        this.restFamilyMockMvc = MockMvcBuilders.standaloneSetup(familyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        family = new Family();
        family.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createFamily() throws Exception {
        int databaseSizeBeforeCreate = familyRepository.findAll().size();

        // Create the Family

        restFamilyMockMvc.perform(post("/api/familys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(family)))
                .andExpect(status().isCreated());

        // Validate the Family in the database
        List<Family> familys = familyRepository.findAll();
        assertThat(familys).hasSize(databaseSizeBeforeCreate + 1);
        Family testFamily = familys.get(familys.size() - 1);
        assertThat(testFamily.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = familyRepository.findAll().size();
        // set the field null
        family.setName(null);

        // Create the Family, which fails.

        restFamilyMockMvc.perform(post("/api/familys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(family)))
                .andExpect(status().isBadRequest());

        List<Family> familys = familyRepository.findAll();
        assertThat(familys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFamilys() throws Exception {
        // Initialize the database
        familyRepository.saveAndFlush(family);

        // Get all the familys
        restFamilyMockMvc.perform(get("/api/familys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(family.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getFamily() throws Exception {
        // Initialize the database
        familyRepository.saveAndFlush(family);

        // Get the family
        restFamilyMockMvc.perform(get("/api/familys/{id}", family.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(family.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFamily() throws Exception {
        // Get the family
        restFamilyMockMvc.perform(get("/api/familys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFamily() throws Exception {
        // Initialize the database
        familyRepository.saveAndFlush(family);

		int databaseSizeBeforeUpdate = familyRepository.findAll().size();

        // Update the family
        family.setName(UPDATED_NAME);

        restFamilyMockMvc.perform(put("/api/familys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(family)))
                .andExpect(status().isOk());

        // Validate the Family in the database
        List<Family> familys = familyRepository.findAll();
        assertThat(familys).hasSize(databaseSizeBeforeUpdate);
        Family testFamily = familys.get(familys.size() - 1);
        assertThat(testFamily.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteFamily() throws Exception {
        // Initialize the database
        familyRepository.saveAndFlush(family);

		int databaseSizeBeforeDelete = familyRepository.findAll().size();

        // Get the family
        restFamilyMockMvc.perform(delete("/api/familys/{id}", family.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Family> familys = familyRepository.findAll();
        assertThat(familys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
