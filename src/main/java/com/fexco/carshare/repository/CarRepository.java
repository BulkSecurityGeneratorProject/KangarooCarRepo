package com.fexco.carshare.repository;

import org.springframework.data.repository.CrudRepository;

import com.fexco.carshare.domain.Car;
import com.fexco.carshare.domain.User;

public interface CarRepository extends CrudRepository<Car, Long>{
	public Car findByUser(User user);
}