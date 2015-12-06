package com.cspinformatique.kubik.server.domain.purchase.repository;

import java.util.List;

public interface PurchaseOrderRepositoryCustom {
	List<Long> findIdsWithDilicomOrders();
}
