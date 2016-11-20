package com.cspinformatique.kubik.kos.domain.order.controller;

import java.security.Principal;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.kos.domain.account.service.AccountService;
import com.cspinformatique.kubik.kos.domain.order.service.CheckoutService;
import com.cspinformatique.kubik.kos.domain.order.service.CustomerOrderService;
import com.cspinformatique.kubik.kos.model.order.CustomerOrder;

@Controller
@Transactional
@RequestMapping("/checkout")
public class CheckoutController {
	@Resource
	private AccountService accountService;

	@Resource
	private CheckoutService checkoutService;

	@Resource
	private CustomerOrderService customerOrderService;

	@RequestMapping(method = RequestMethod.POST, params = "payment_method_nonce")
	public String checkout(@RequestParam(value = "payment_method_nonce") String nonce, Principal principal) {
		CustomerOrder customerOrder = customerOrderService
				.loadOpenCustomerOrder(accountService.findByPrincipal(principal), null);

		checkoutService.checkout(customerOrder, nonce);

		return "redirect:/commande-client/" + customerOrder.getId();
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getCheckoutPage() {
		return "order/checkout";
	}

	@RequestMapping(value = "/token", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String getClientToken() {
		return checkoutService.generateClientToken();
	}
}
