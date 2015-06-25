package com.nationwide.andersja.da;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {
	
	public List<Customer> findByEmail(@Param("email") String email);

}
