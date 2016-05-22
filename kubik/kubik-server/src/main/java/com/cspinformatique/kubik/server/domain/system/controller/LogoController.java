package com.cspinformatique.kubik.server.domain.system.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cspinformatique.kubik.server.domain.product.exception.ImageNotFoundException;
import com.cspinformatique.kubik.server.domain.system.service.LogoService;

@RestController
@RequestMapping("/logo")
public class LogoController {
	private LogoService logoService;

	@Autowired
	public LogoController(LogoService logoService) {
		this.logoService = logoService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> findLogo() {
		try {
			return new ResponseEntity<InputStreamResource>(new InputStreamResource(logoService.findLogo()),
					HttpStatus.OK);
		} catch (ImageNotFoundException imageNotFoundEx) {
			return new ResponseEntity<InputStreamResource>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public void saveLogo(@RequestParam("file") MultipartFile file) {
		try {
			logoService.saveLogo(file.getBytes());
		} catch (IOException ioEx) {
			throw new RuntimeException(ioEx);
		}
	}
}
