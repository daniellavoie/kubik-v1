package com.cspinformatique.kubik.kos.domain.order.validator.impl;

import java.security.Principal;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.kos.domain.account.service.AccountService;
import com.cspinformatique.kubik.kos.domain.order.validator.CustomerOrderValidator;
import com.cspinformatique.kubik.kos.exception.ForbiddenAccessException;
import com.cspinformatique.kubik.kos.model.account.Account;
import com.cspinformatique.kubik.kos.model.order.CustomerOrder;

@Service
public class CustomerOrderValidatorImpl implements CustomerOrderValidator {
	@Resource
	AccountService accountService;
	
	public void checkAccessRights(Principal principal, CustomerOrder customerOrder,
			String customerOrderUuid) {
		Account account = accountService.findByPrincipal(principal);

		boolean systemUser = account.getAuthorities().stream().filter(role -> role.getName().equals("SYSTEM")).findAny()
				.isPresent();

		boolean accountDoesNotMatch = customerOrder.getAccount() != null && account != null && !systemUser
				&& customerOrder.getAccount().getId() != account.getId();

		boolean uuidDoesNotMatchForAnonymusOrder = principal == null && customerOrder.getUuid() != null && customerOrderUuid != null
				&& !customerOrderUuid.equals(customerOrder.getUuid());

		if (accountDoesNotMatch || uuidDoesNotMatchForAnonymusOrder) {
			String message;
			if (accountDoesNotMatch)
				message = "Customer order " + customerOrder.getId() + " does not belong to user "
						+ customerOrder.getId() + ".";
			else
				message = "Anonymus order " + customerOrder.getId() + " with UUID " + customerOrder.getUuid()
						+ " does not match user current UUID " + customerOrderUuid + ".";

			throw new ForbiddenAccessException(account, message);
		}
	}
}
