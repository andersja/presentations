package com.nationwide.harleystore.repository;

import com.nationwide.harleystore.domain.Model;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Model entity.
 */
public interface ModelRepository extends JpaRepository<Model,Long> {

}
