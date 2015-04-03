package com.cspinformatique.kubik.domain.sales.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cspinformatique.kubik.model.sales.Customer;
import com.cspinformatique.kubik.model.sales.CustomerCredit;
import com.cspinformatique.kubik.model.sales.Invoice;
import com.cspinformatique.kubik.model.sales.CustomerCredit.Status;

public interface CustomerCreditRepository extends
		JpaRepository<CustomerCredit, Integer> {

	List<CustomerCredit> findByCompleteDateBetweenAndStatus(Date startDate, Date endDate, Status status);
	
	Page<CustomerCredit> findByCompleteDateBetweenAndStatus(Date startDate, Date endDate, Status status, Pageable pageable);
	
	List<CustomerCredit> findByInvoice(Invoice invoice);
	
	Page<CustomerCredit> findByStatusAndNumberIsNotNull(Status status, Pageable pageable);
	
	@Query("SELECT id FROM CustomerCredit customerCredit WHERE id > :id")
	Page<Integer> findIdByIdGreaterThan(@Param("id") int id, Pageable pageable);

	@Query("SELECT id FROM CustomerCredit customerCredit WHERE id < :id")
	Page<Integer> findIdByIdLessThan(@Param("id") int id, Pageable pageable);

	@Query("SELECT sum(customerCredit.totalAmount) FROM CustomerCredit customerCredit WHERE customerCredit.status = 'COMPLETED' AND customerCredit.customer = :customer")
	Double findCustomerCredit(@Param("customer") Customer customer);

	@Query("SELECT sum(payment.amount) FROM Payment payment WHERE payment.invoice.status = 'PAID' AND payment.invoice.customer = :customer AND payment.paymentMethod.type = 'CREDIT'")
	Double findCustomerCreditUsed(@Param("customer") Customer customer);
	
	@Query("SELECT sum(detail.quantity) FROM CustomerCreditDetail detail WHERE detail.product.id = :productId AND detail.customerCredit.status = 'COMPLETED'")
	Double findProductQuantityReturned(@Param("productId") int productId);
}