package com.cspinformatique.kubik.proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cspinformatique.kubik.proxy.service.ProxyService;

import org.springframework.web.bind.annotation.RequestMapping;

import com.cspinformatique.kubik.proxy.model.Proxy;

@Controller
@RequestMapping("/proxy")
public class ProxyController {
	@Autowired
	private ProxyService proxyService;

	
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody Proxy save(@RequestBody Proxy proxy) {
		return this.proxyService.save(proxy);
	}
}
