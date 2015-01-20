package com.cspinformatique.kubik.navigation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class NavigationController {
	@RequestMapping
	public String getMainPage(){
		return "index";
	}
}
