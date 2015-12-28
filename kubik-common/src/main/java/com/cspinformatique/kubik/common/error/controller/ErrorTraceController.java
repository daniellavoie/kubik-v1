package com.cspinformatique.kubik.common.error.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.common.error.model.ErrorTrace;
import com.cspinformatique.kubik.common.error.service.ErrorTraceService;

@Controller
@RequestMapping("/error")
public class ErrorTraceController {
	@Resource private ErrorTraceService errorTraceService;
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public ErrorTrace save(@RequestBody ErrorTrace errorTrace){
		return errorTraceService.save(errorTrace);
	}
}
