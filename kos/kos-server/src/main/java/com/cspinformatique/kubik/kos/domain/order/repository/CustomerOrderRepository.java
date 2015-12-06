package com.cspinformatique.kubik.kos.domain.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cspinformatique.kubik.kos.model.account.Account;
import com.cspinformatique.kubik.kos.model.order.CustomerOrder;
import com.cspinformatique.kubik.kos.model.order.CustomerOrder.Status;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
	@Query("SELECT customerOrder FROM CustomerOrder customerOrder WHERE status = ?1 AND (account = ?2 OR uuid = ?3)")
	List<CustomerOrder> findByStatusAndAccountOrUuid(Status status, Account account, String uuid);
	
	CustomerOrder findByUuid(String uuid);
}
