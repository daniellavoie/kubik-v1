package com.cspinformatique.kubik.sales.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.sales.model.Customer;
import com.cspinformatique.kubik.sales.service.CustomerService;

@Controller
@RequestMapping("/customer")
public class CustomerController {
	@Autowired
	private CustomerService customerService;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Iterable<Customer> findAll() {
		return this.customerService.findAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Customer findOne(@PathVariable int id) {
		return this.customerService.findOne(id);
	}

	@RequestMapping(params = "card", produces = MediaType.TEXT_HTML_VALUE)
	public String getCustomerCardPage() {
		return "sales/customer/customer-card :: customer-card";
	}

	@RequestMapping(params = "modal", produces = MediaType.TEXT_HTML_VALUE)
	public String getCustomersModal() {
		return "sales/customer/customers-modal :: customers-modal";
	}

	@RequestMapping(params = "search", produces = MediaType.TEXT_HTML_VALUE)
	public String getSeatchCustomerPage() {
		return "sales/customer/customers :: customers";
	}

	@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
	public String getCustomersPage() {
		return "sales/customer/customers-page";
	}

	@RequestMapping(method = { RequestMethod.POST, RequestMethod.PUT }, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Customer save(@RequestBody Customer customer) {
		return this.customerService.save(customer);
	}

	@RequestMapping(method = RequestMethod.GET, params = "search", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Page<Customer> search(@RequestParam String query,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "50") Integer resultPerPage,
			@RequestParam(required = false) Direction direction,
			@RequestParam(defaultValue = "firstName") String sortBy) {
		return this.customerService.search(query, new PageRequest(page,
				resultPerPage, direction != null ? direction : Direction.ASC,
				sortBy));
	}
}
