package com.cspinformatique.kubik.domain.purchase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.domain.purchase.model.Reception;
import com.cspinformatique.kubik.domain.purchase.service.ReceptionService;

@Controller
@RequestMapping("/reception")
public class ReceptionController {
	@Autowired private ReceptionService receptionService;
	
	@RequestMapping(method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Iterable<Reception> findAll(
			@RequestParam(required = false) String status,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "50") Integer resultPerPage,
			@RequestParam(required = false) Direction direction,
			@RequestParam(defaultValue = "deliveryDate") String sortBy){
		return this.receptionService.findAll(new PageRequest(page, resultPerPage,
				direction != null ? direction : Direction.DESC, sortBy));
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
	
	@RequestMapping(method = RequestMethod.GET, params="initialize")
	public String initialize(){
		this.receptionService.initialize();
		
		return "redirect:/reception";
	}
	
	@RequestMapping(method={RequestMethod.POST, RequestMethod.POST}, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Reception save(@RequestBody Reception reception){
		return this.receptionService.save(reception);
	}
	
	@RequestMapping(method = RequestMethod.GET, params="validate")
	public String validate(){
		this.receptionService.validate();
		
		return "redirect:/reception";
	}
}
