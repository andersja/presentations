package com.nationwide.harleystore.repository;

import com.nationwide.harleystore.domain.Family;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Family entity.
 */
public interface FamilyRepository extends JpaRepository<Family,Long> {

}
