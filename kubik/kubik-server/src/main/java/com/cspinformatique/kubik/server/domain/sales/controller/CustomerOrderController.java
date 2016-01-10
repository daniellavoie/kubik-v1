package com.cspinformatique.kubik.server.domain.sales.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.kos.model.order.CustomerOrder;
import com.cspinformatique.kubik.server.domain.sales.service.CustomerOrderService;

@Controller
@RequestMapping("/customer-order")
public class CustomerOrderController {
	@Resource
	private CustomerOrderService customerOrderService;

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<CustomerOrder> findAll(HttpServletRequest request) {
		Map<String, List<String>> parameters = new HashMap<>();
		for (Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
			parameters.put(entry.getKey(), Arrays.asList(entry.getValue()));
		}

		return customerOrderService.findAll(CollectionUtils.toMultiValueMap(parameters));
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getCustomerOrdersPage() {
		return "sales/customer-order/customer-orders";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getCustomerOrderDetailsPage() {
		return "sales/customer-order/customer-order-details";
	}

	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public CustomerOrder findOne(@PathVariable long id) {
		return customerOrderService.findOne(id);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public CustomerOrder save(@RequestBody CustomerOrder customerOrder) {
		return customerOrderService.save(customerOrder);
	}
}
