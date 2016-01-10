package com.cspinformatique.kubik.kos.domain.order.controller;

import java.security.Principal;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cspinformatique.kubik.kos.domain.account.service.AccountService;
import com.cspinformatique.kubik.kos.domain.order.service.CustomerOrderService;
import com.cspinformatique.kubik.kos.domain.order.validator.CustomerOrderValidator;
import com.cspinformatique.kubik.kos.model.order.CustomerOrder;
import com.cspinformatique.kubik.kos.model.order.CustomerOrderDetail;

@Controller
@RequestMapping({"/cart", "/panier"})
public class CartController {
	@Resource
	private AccountService accountService;

	@Resource
	CustomerOrderService customerOrderService;

	@Resource
	CustomerOrderValidator customerOrderValidator;

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/detail", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public void addDetail(Principal principal, @CookieValue(required = false) String customerOrderUuid,
			HttpServletResponse response, @RequestBody CustomerOrderDetail customerOrderDetail) {
		customerOrderService.addDetail(loadCart(principal, customerOrderUuid, response),
				customerOrderDetail);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CustomerOrder loadCart(Principal principal,
			@CookieValue(required = false) String customerOrderUuid, HttpServletResponse response) {
		CustomerOrder customerOrder = customerOrderService
				.loadOpenCustomerOrder(accountService.findByPrincipal(principal), customerOrderUuid);

		if (principal == null)
			response.addCookie(new Cookie("customerOrderUuid", customerOrder.getUuid()));

		return customerOrder;
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getCartPage(Principal principal, @CookieValue(required = false) String customerOrderUuid,
			HttpServletResponse response) {
		CustomerOrder customerOrder = loadCart(principal, customerOrderUuid, response);

		return "redirect:/commande-client/" + customerOrder.getId();
	}
}
