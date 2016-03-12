package com.fexco.carshare.web.rest;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fexco.carshare.domain.Make;
import com.fexco.carshare.domain.MakeAndModel;
import com.fexco.carshare.repository.MakeAndModelRepository;
import com.fexco.carshare.repository.MakeRepository;

@Controller
@RequestMapping("/api/dropdown")
public class DropdownController {
	
	@Autowired
    MakeAndModelRepository makeAndModelRepository;
    
    @Autowired
    MakeRepository makeRepository;

    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/makes", method=RequestMethod.GET)
    public @ResponseBody JSONObject sendMakes(HttpServletResponse response) {
    	JSONObject makesAsJson = new JSONObject();
    	Iterable<Make> list =  makeRepository.findAll();
    	makesAsJson.put("makes", list);
    	return makesAsJson;
    }
    
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/models/{make}", method=RequestMethod.GET)
    @PreAuthorize("permitAll")
    public @ResponseBody JSONObject sendModels(@PathVariable("make") Long make, HttpServletResponse response) {
    	JSONObject makeAndModelAsJson = new JSONObject();
    	List<MakeAndModel> makeAndModelsList = makeAndModelRepository.findByMakeId(make);
    	makeAndModelAsJson.put("makeAndModel", makeAndModelsList);
    	return makeAndModelAsJson;
    }
}
