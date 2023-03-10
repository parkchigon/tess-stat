package com.vup.tess.proc.stathour.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vup.tess.proc.stathour.service.StatHourService1;

@RestController
public class StatHoruController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private StatHourService1 statHourService;
	
	@GetMapping({"/test11"})
	public void factroyFind() {

		logger.debug("Request factory data Search!" );
		
		statHourService.doService(); 

		
	}
}
