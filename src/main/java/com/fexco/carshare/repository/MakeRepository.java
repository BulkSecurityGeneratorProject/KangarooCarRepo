package com.fexco.carshare.repository;

import org.springframework.data.repository.CrudRepository;

import com.fexco.carshare.domain.Make;

public interface MakeRepository extends CrudRepository<Make, Long>{
	Make findByMake(String make);
}
