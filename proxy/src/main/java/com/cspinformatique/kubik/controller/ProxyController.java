package com.cspinformatique.kubik.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping
public class ProxyController {
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method=RequestMethod.GET)
	public void ping(){
		
	}
}
