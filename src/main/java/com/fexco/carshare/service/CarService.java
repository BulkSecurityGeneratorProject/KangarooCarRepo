package com.fexco.carshare.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.fexco.carshare.domain.Car;
import com.fexco.carshare.domain.Make;
import com.fexco.carshare.domain.User;
import com.fexco.carshare.repository.CarRepository;
import com.fexco.carshare.repository.MakeAndModelRepository;
import com.fexco.carshare.repository.MakeRepository;
import com.fexco.carshare.repository.UserRepository;
import com.fexco.carshare.web.rest.dto.CarDTO;

@Service
@Transactional
public class CarService {
	@Autowired
	private CarRepository carRepository;
	
	@Autowired
	private MakeAndModelRepository makeAndModelRepository;
	
	@Autowired
	MakeRepository makeRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public void createCar(CarDTO carDto){
		Car car = carRepository.findByUser(userRepository.findByLogin(carDto.getUserName()));
		if(car == null){
			car = new Car();
		}
		dtoToEntity(car, carDto);
		carRepository.save(car);
	}

	private void dtoToEntity(Car car, CarDTO carDto) {
		car.setMakeAndModel(makeAndModelRepository.findOne(carDto.getMakeAndModel()));
		car.setUser(userRepository.findByLogin(carDto.getUserName()));
		car.setYear(carDto.getYear());
	}
	
	public List<String> validateCar(BindingResult result){
		List<String> errorMessages = new LinkedList<String>();
    	if (result.hasErrors()) {
			
			List<FieldError> errors = result.getFieldErrors();
			for (FieldError error : errors ) {
			     errorMessages.add(error.getDefaultMessage());
			}
		} 
    	return errorMessages;
	}

	public List<Object> getCar(String username) {
		List<Object> carDetails = new ArrayList<Object>();
		String model = null;
		String makeName = null;
		User user = userRepository.findByLogin(username);
		Car car = carRepository.findByUser(user);
		if(car == null){
			carDetails.add("");
			carDetails.add("");
			carDetails.add("");
			return carDetails;
		}
		model = car.getMakeAndModel().getModel();
		Make make = makeRepository.findOne(car.getMakeAndModel().getMakeId());
		makeName = make.getMake();
		int year = car.getYear();
		carDetails.add(makeName);
		carDetails.add(model);
		carDetails.add(year);
		return carDetails;
	}

	public void updateCar(CarDTO carDto, String username) {
		Car car = carRepository.findByUser(userRepository.findByLogin(username));
		dtoToEntity(car, carDto);
		carRepository.save(car);
	}

}