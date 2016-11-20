package com.cspinformatique.kubik.kos.domain.order.validator;

import java.security.Principal;

import com.cspinformatique.kubik.kos.model.order.CustomerOrder;

public interface CustomerOrderValidator {
	void checkAccessRights(Principal principal, CustomerOrder customerOrder, String customerOrderUuid);
}
