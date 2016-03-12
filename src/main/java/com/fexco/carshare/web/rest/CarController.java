package com.fexco.carshare.web.rest;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fexco.carshare.service.CarService;
import com.fexco.carshare.web.rest.dto.CarDTO;
import com.fexco.carshare.web.rest.util.CarshareUtils;

@Controller
@RequestMapping("/api/car")
public class CarController {
	
	@Autowired
	CarService carService;
	
	@RequestMapping(value="/registration",method=RequestMethod.POST)
    public @ResponseBody JSONObject processRegistrationCar(@RequestBody CarDTO carDto, BindingResult result) {
		List<String> errors = carService.validateCar(result);
 		if(!result.hasErrors()){
			carService.createCar(carDto);
			return CarshareUtils.returnSuccess();
		}
		else{
			return CarshareUtils.returnErrors(errors);
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getUserCar/{username}", method = RequestMethod.GET)
	@ResponseBody JSONObject findAllJourneys(@PathVariable("username") String username, HttpServletResponse response) {
		JSONObject car = new JSONObject();
		List<Object> carDetails = carService.getCar(username);
		car.put("make", carDetails.get(0));
		car.put("model", carDetails.get(1));
		car.put("year", carDetails.get(2));
		return car;
    }
	
	@RequestMapping(value = "/updateCar/{username}", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void updateCar(@RequestBody CarDTO carDto, BindingResult result, @PathVariable("username") String username) {
		carService.updateCar(carDto, username);
    }
}
