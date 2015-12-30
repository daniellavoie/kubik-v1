package com.cspinformatique.kubik.kos.domain.account.controller;

import java.security.Principal;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.kos.domain.account.service.AccountService;
import com.cspinformatique.kubik.kos.model.account.Account;
import com.cspinformatique.kubik.kos.model.account.AccountWrapper;
import com.cspinformatique.kubik.kos.model.account.Address;

@Controller
@RequestMapping({ "/account", "/compte" })
public class AccountController {
	@Resource
	private AccountService accountService;

	@RequestMapping(method = RequestMethod.POST, params = { "username", "password" })
	public @ResponseBody Account createAccount(@RequestParam String username, @RequestParam CharSequence password) {
		return accountService.createAccount(username, password);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody AccountWrapper getAccount(Principal principal) {
		return new AccountWrapper(principal != null ? accountService.findByUsername(principal.getName()) : null, null);
	}

	@RequestMapping(value = "{/id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Account getAccount(@PathVariable long id) {
		return accountService.findOne(id);
	}

	@RequestMapping({ "/create-account", "/creer-un-compte" })
	public String getCreateAccountPage() {
		return "account/create-account";
	}

	@RequestMapping({ "/sign-in", "/connexion" })
	public String getSignInPage() {
		return "account/sign-in";
	}

	@RequestMapping(value = "/address", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Address saveAddress(@RequestBody Address address,
			@RequestParam(defaultValue = "false") boolean shippingAddressPreferedForBilling, Principal principal) {
		return accountService.saveAddress(accountService.findByUsername(principal.getName()), address,
				shippingAddressPreferedForBilling);
	}
}
