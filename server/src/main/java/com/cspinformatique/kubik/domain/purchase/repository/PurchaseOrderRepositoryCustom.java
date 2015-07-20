package com.cspinformatique.kubik.domain.purchase.repository;

import java.util.List;

public interface PurchaseOrderRepositoryCustom {
	List<Long> findIdsWithDilicomOrders();
}
