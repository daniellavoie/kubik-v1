package com.cspinformatique.kubik.company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.company.model.Company;
import com.cspinformatique.kubik.company.service.CompanyService;

@Controller
@RequestMapping("/company")
public class CompanyController {
	@Autowired private CompanyService companyService;
	
	@RequestMapping
	public @ResponseBody Company findCompany(){
		return this.companyService.find();
	}
}
