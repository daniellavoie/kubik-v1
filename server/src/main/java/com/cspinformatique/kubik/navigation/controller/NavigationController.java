package com.cspinformatique.kubik.navigation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class NavigationController {
	@RequestMapping
	public String getMainPage(){
		return "index";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String getLoginPage(){
		return "login";
	}
}
