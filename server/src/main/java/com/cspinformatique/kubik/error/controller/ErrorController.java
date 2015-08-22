package com.cspinformatique.kubik.error.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.error.model.Error;
import com.cspinformatique.kubik.error.service.ErrorService;

@Controller
@RequestMapping("/error")
public class ErrorController {
	@Autowired
	private ErrorService errorService;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Page<Error> findAll(@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "50") Integer resultPerPage,
			@RequestParam(defaultValue = "DESC") Direction direction,
			@RequestParam(defaultValue = "timestamp") String sortBy) {
		return this.errorService.findAll(new PageRequest(page, resultPerPage, direction, sortBy));
	}

	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Error save(@RequestBody Error error, HttpServletRequest request) {
		error.setIp(request.getRemoteAddr());
		error.setHostname(request.getRemoteHost());
		error.setAgent(request.getHeader("User-Agent"));
		error.setTimestamp(new Date());
		
		return this.errorService.save(error);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getErrorsPage() {
		return "error/errors";
	}
}
