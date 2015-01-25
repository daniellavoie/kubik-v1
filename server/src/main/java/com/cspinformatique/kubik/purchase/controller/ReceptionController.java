package com.cspinformatique.kubik.purchase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.purchase.model.Reception;
import com.cspinformatique.kubik.purchase.service.ReceptionService;

@Controller
@RequestMapping("/reception")
public class ReceptionController {
	@Autowired private ReceptionService receptionService;
	
	@RequestMapping(method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Iterable<Reception> findAll(){
		return this.receptionService.findAll();
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Reception findOne(@PathVariable int id){
		return this.receptionService.findOne(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getOrderDetailsPage(@PathVariable int id, Model model) {
		model.addAttribute("id", id);

		return "purchase/reception/reception-details";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String getOrdersPage() {
		return "purchase/reception/receptions";
	}
	
	@RequestMapping(method={RequestMethod.POST, RequestMethod.POST}, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Reception save(@RequestBody Reception reception){
		return this.receptionService.save(reception);
	}
}