package com.cspinformatique.kubik.kos.domain.order.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.common.rest.HtmlException;
import com.cspinformatique.kubik.common.rest.RestException;
import com.cspinformatique.kubik.kos.domain.account.service.AccountService;
import com.cspinformatique.kubik.kos.domain.order.service.CustomerOrderService;
import com.cspinformatique.kubik.kos.domain.order.validator.CustomerOrderValidator;
import com.cspinformatique.kubik.kos.exception.ResourceNotFoundException;
import com.cspinformatique.kubik.kos.model.order.CustomerOrder;
import com.cspinformatique.kubik.kos.model.order.CustomerOrder.Status;
import com.mysema.query.types.Predicate;

@Controller
@Transactional
@RequestMapping({ "/customer-order", "/commande-client" })
public class CustomerOrderController {
	private final List<Status> HISTORY_STATUS = Arrays.asList(new Status[] { Status.CONFIRMED, Status.PROCESSED });

	@Resource
	private AccountService accountService;

	@Resource
	CustomerOrderService customerOrderService;

	@Resource
	CustomerOrderValidator customerOrderValidator;

	@PreAuthorize("hasRole('SYSTEM')")
	@RequestMapping(method = RequestMethod.GET, params = "search", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Page<CustomerOrder> findAll(@QuerydslPredicate(root = CustomerOrder.class) Predicate predicate,
			Pageable pageable) {
		return customerOrderService.findAll(predicate, pageable);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<CustomerOrder> findByAccount(Pageable pageable, Principal principal) {
		return customerOrderService.findByAccountAndStatusIn(accountService.findByPrincipal(principal),
				HISTORY_STATUS, pageable);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CustomerOrder findOne(@PathVariable long id, Principal principal,
			@CookieValue(required = false) String customerOrderUuid) {
		CustomerOrder customerOrder = customerOrderService.findOne(id);

		if (customerOrder == null) {
			throw new ResourceNotFoundException("Customer order " + id + " could not be found.");
		}

		customerOrderValidator.checkAccessRights(principal, customerOrder, customerOrderUuid);

		return customerOrderService.findOne(id);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getCustomerOrdersHistoryPage(Principal principal) {
		Assert.notNull(principal);

		return "order/customer-orders-history";
	}

	@RequestMapping(value = "/{id}", produces = MediaType.TEXT_HTML_VALUE)
	public String getCustomerOrderPage(@PathVariable long id, @CookieValue(required = false) String customerOrderUuid,
			Principal principal) {
		try {
			findOne(id, principal, customerOrderUuid);
		} catch (RestException restEx) {
			throw new HtmlException(restEx);
		}

		return "order/customer-order";
	}

	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CustomerOrder save(@RequestBody CustomerOrder customerOrder) {
		return customerOrderService.save(customerOrder);
	}
}
