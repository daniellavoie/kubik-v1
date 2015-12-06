package com.cspinformatique.kubik.kos.domain.order.controller;

import java.security.Principal;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cspinformatique.kubik.kos.domain.account.service.AccountService;
import com.cspinformatique.kubik.kos.domain.order.service.CustomerOrderService;
import com.cspinformatique.kubik.kos.exception.BadRequestException;
import com.cspinformatique.kubik.kos.model.order.CustomerOrder;
import com.cspinformatique.kubik.kos.model.order.CustomerOrderDetail;

@Controller
@Transactional
@RequestMapping({ "/customer-order", "/commande-client", "/cart", "/panier" })
public class CustomerOrderController {
	@Resource
	private AccountService accountService;

	@Resource
	private CustomerOrderService customerOrderService;

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/detail", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public void addDetail(Principal principal, @CookieValue(required = false) String customerOrderUuid,
			HttpServletResponse response, @RequestBody CustomerOrderDetail customerOrderDetail) {
		customerOrderService.addDetail(findActiveCustomerOrder(principal, customerOrderUuid, response),
				customerOrderDetail);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CustomerOrder findActiveCustomerOrder(Principal principal,
			@CookieValue(required = false) String customerOrderUuid, HttpServletResponse response) {
		CustomerOrder customerOrder = customerOrderService.loadOpenCustomerOrder(
				principal != null ? accountService.findByUsername(principal.getName()) : null, customerOrderUuid);

		if (principal == null)
			response.addCookie(new Cookie("customerOrderUuid", customerOrder.getUuid()));

		return customerOrder;
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getCustomerOrderPage() {
		return "order/customer-order";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public CustomerOrder getCustomerOrderConfirmation(@PathVariable long id, Principal principal) {
		return getCustomerOrderForConfirmation(id, principal);
	}

	private CustomerOrder getCustomerOrderForConfirmation(long id, Principal principal) {
		CustomerOrder customerOrder = customerOrderService.findOne(id);

		if (customerOrder == null
				|| customerOrder.getAccount().getId() != accountService.findByUsername(principal.getName()).getId()
				|| customerOrder.getStatus().equals(CustomerOrder.Status.OPEN))
			throw new BadRequestException();

		return customerOrder;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getCustomerOrderConfirmationPage(@PathVariable long id, Principal principal) {
		getCustomerOrderForConfirmation(id, principal);

		return "order/customer-order-confirmation";
	}

	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CustomerOrder save(@RequestBody CustomerOrder customerOrder) {
		return customerOrderService.save(customerOrder);
	}
}
