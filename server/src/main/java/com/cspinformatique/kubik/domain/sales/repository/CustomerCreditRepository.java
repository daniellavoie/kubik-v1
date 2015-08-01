package com.cspinformatique.kubik.domain.sales.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cspinformatique.kubik.model.sales.Customer;
import com.cspinformatique.kubik.model.sales.CustomerCredit;
import com.cspinformatique.kubik.model.sales.CustomerCredit.Status;
import com.cspinformatique.kubik.model.sales.Invoice;

public interface CustomerCreditRepository extends
		JpaRepository<CustomerCredit, Integer> {

	@Query("SELECT customerCredit FROM CustomerCredit customerCredit ORDER BY completeDate ASC")
	Iterable<CustomerCredit> findAllOrderByCompleteDateDesc();

	List<CustomerCredit> findByCompleteDateBetweenAndStatus(Date startDate,
			Date endDate, Status status);

	Page<CustomerCredit> findByCompleteDateBetweenAndStatus(Date startDate,
			Date endDate, Status status, Pageable pageable);

	List<CustomerCredit> findByInvoice(Invoice invoice);

	Page<CustomerCredit> findByStatusAndNumberIsNotNull(Status status,
			Pageable pageable);

	@Query("SELECT id FROM CustomerCredit customerCredit WHERE id > ?1")
	Page<Integer> findIdByIdGreaterThan(int id, Pageable pageable);

	@Query("SELECT id FROM CustomerCredit customerCredit WHERE id < ?1")
	Page<Integer> findIdByIdLessThan(int id, Pageable pageable);

	@Query("SELECT sum(customerCredit.totalAmount) FROM CustomerCredit customerCredit WHERE customerCredit.status = 'COMPLETED' AND customerCredit.customer = ?1")
	Double findCustomerCredit(Customer customer);

	@Query("SELECT sum(payment.amount) FROM Payment payment WHERE payment.invoice.status = 'PAID' AND payment.invoice.customer = ?1 AND payment.paymentMethod.type = 'CREDIT'")
	Double findCustomerCreditUsed(Customer customer);

	@Query("SELECT sum(detail.quantity) FROM CustomerCreditDetail detail WHERE detail.product.id = ?1 AND detail.customerCredit.status = 'COMPLETED'")
	Double findProductQuantityReturnedByCustomer(int productId);
}