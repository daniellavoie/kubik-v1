package com.cspinformatique.kubik.onlinesales.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/company")
public class CompanyController {
	@Value("kos.ean13")
	private String ean13;

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/ean13", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String getEan13() {
		return ean13;
	}
}
