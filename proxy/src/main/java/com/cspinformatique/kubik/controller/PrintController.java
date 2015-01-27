package com.cspinformatique.kubik.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cspinformatique.kubik.service.PrintService;

@Controller
@RequestMapping("/print")
public class PrintController {
	private static final Logger LOGGER = LoggerFactory.getLogger(PrintController.class);
	
	@Autowired private PrintService printService;
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(method=RequestMethod.POST)
	public void print(@RequestBody byte[] content, HttpServletRequest request){
		LOGGER.info("Received print request from " + request.getRemoteHost());
		
		this.printService.print(content);
		
		LOGGER.info("Print request from " + request.getRemoteHost() + " completed.");
	}
}
