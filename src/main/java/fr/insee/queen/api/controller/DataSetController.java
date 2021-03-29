package fr.insee.queen.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.insee.queen.api.service.DataSetInjectorService;
import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping(path = "/api")
public class DataSetController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DataSetController.class);
	@Autowired
	private DataSetInjectorService injector;
	
	@ApiOperation(value = "Create dataset")
	@PostMapping(path = "/createDataSet")
	public ResponseEntity<Object> createDataSet() {
		
		injector.createDataSet();
		
		LOGGER.info("Dataset creation end");	
		return new ResponseEntity<>("dataSet created", HttpStatus.OK);
	}

	

}
