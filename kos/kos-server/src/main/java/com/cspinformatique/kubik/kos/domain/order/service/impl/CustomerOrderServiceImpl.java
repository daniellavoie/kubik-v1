package com.cspinformatique.kubik.kos.domain.order.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.math3.util.Precision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.kos.domain.order.repository.CustomerOrderRepository;
import com.cspinformatique.kubik.kos.domain.order.service.CustomerOrderService;
import com.cspinformatique.kubik.kos.domain.order.service.ShippingCostLevelService;
import com.cspinformatique.kubik.kos.domain.product.exception.ProductWithoutWeightException;
import com.cspinformatique.kubik.kos.domain.product.service.ProductService;
import com.cspinformatique.kubik.kos.model.account.Account;
import com.cspinformatique.kubik.kos.model.account.Address;
import com.cspinformatique.kubik.kos.model.order.CustomerOrder;
import com.cspinformatique.kubik.kos.model.order.CustomerOrder.ShippingMethod;
import com.cspinformatique.kubik.kos.model.order.CustomerOrder.Status;
import com.cspinformatique.kubik.kos.model.order.CustomerOrderDetail;
import com.cspinformatique.kubik.kos.model.product.Product;
import com.mysema.query.types.Predicate;

@Service
public class CustomerOrderServiceImpl implements CustomerOrderService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerOrderServiceImpl.class);

	@Resource
	private CustomerOrderRepository customerOrderRepository;

	@Resource
	private ProductService productService;

	@Resource
	private ShippingCostLevelService shippingCostLevelService;

	@Override
	public CustomerOrder addDetail(CustomerOrder customerOrder, CustomerOrderDetail customerOrderDetail) {
		LOGGER.info("Adding " + customerOrderDetail.getQuantityOrdered() + " product "
				+ customerOrderDetail.getProduct().getId() + " to order " + customerOrder.getId() + ".");

		Product product = productService.findOne(customerOrderDetail.getProduct().getId());

		customerOrderDetail.setProduct(product);
		customerOrderDetail.setCustomerOrder(customerOrder);

		// Retreives the id and the quantity of an existing detail.
		Optional<CustomerOrderDetail> detailOptional = customerOrder.getCustomerOrderDetails().stream()
				.filter(existingDetail -> existingDetail.getProduct().getId() == product.getId()).findAny();

		int quantity = getDetailQuantity(customerOrderDetail);
		if (detailOptional.isPresent()) {
			CustomerOrderDetail existingDetail = detailOptional.get();

			int newQuantity = getDetailQuantity(existingDetail) + quantity;

			existingDetail.setQuantityOrdered(newQuantity);

			if (Status.OPEN.equals(customerOrderDetail.getCustomerOrder().getStatus()))
				existingDetail.setQuantityShipped(newQuantity);

			customerOrderDetail = existingDetail;
		} else {
			customerOrderDetail.setCustomerOrder(customerOrder);
			customerOrder.getCustomerOrderDetails().add(customerOrderDetail);
		}

		customerOrderDetail.setUnitPrice(customerOrderDetail.getProduct().getPrice());
		customerOrderDetail.setTotalAmount(customerOrderDetail.getUnitPrice() * getDetailQuantity(customerOrderDetail));

		return save(customerOrder);
	}

	private void calculateAmounts(CustomerOrder customerOrder) {
		int totalQuantity = 0;
		Integer totalWeight = 0;
		Double subTotal = 0d;

		for (CustomerOrderDetail detail : customerOrder.getCustomerOrderDetails()) {
			int quantity = getDetailQuantity(detail);

			totalQuantity += quantity;

			Integer productWeight = detail.getProduct().getWeight();
			if (productWeight == null || productWeight == 0) {
				ProductWithoutWeightException ex = new ProductWithoutWeightException(detail.getProduct());

				LOGGER.error(ex.getMessage());

				throw ex;
			}
			totalWeight += productWeight * quantity;
			subTotal += detail.getProduct().getPrice() * quantity;
		}

		customerOrder.setTotalQuantity(totalQuantity);
		customerOrder.setSubTotal(Precision.round(subTotal, 2));
		customerOrder.setTotalAmount(Precision.round(customerOrder.getSubTotal() + customerOrder.getShippingCost(), 2));
		customerOrder.setTotalWeight(totalWeight);
	}

	@Override
	public Page<CustomerOrder> findAll(Predicate predicate, Pageable pageable) {
		return customerOrderRepository.findAll(predicate, pageable);
	}

	@Override
	public Page<CustomerOrder> findByAccountAndStatusIn(Account account, List<Status> status, Pageable pageable) {
		return customerOrderRepository.findByAccountAndStatusIn(account, status, pageable);
	}

	@Override
	public CustomerOrder findOne(long id) {
		return customerOrderRepository.findOne(id);
	}

	private CustomerOrder generateNewCustomerOrder(Account account) {
		String uuid = null;
		if (account == null)
			uuid = generateNewUUid();

		Address shippingAddress = account != null ? account.getShippingAddress() : null;

		Address billingAddress = null;
		if (account != null)
			billingAddress = account.isShippingAddressPreferedForBilling() ? account.getShippingAddress()
					: account.getBillingAddress();

		return save(new CustomerOrder(0, uuid, new ArrayList<>(), account, Status.OPEN, null, null, null, null, null,
				null, 0d, 0d, 0, 0, 0d, null, ShippingMethod.COLISSIMO, shippingAddress, billingAddress, null, null,
				null));
	}

	private String generateNewUUid() {
		String uuid;

		boolean duplicate = true;
		do {
			uuid = UUID.randomUUID().toString();
			if (customerOrderRepository.findByUuid(uuid) == null) {
				duplicate = false;
			}
		} while (duplicate);

		return uuid;
	}

	private int getDetailQuantity(CustomerOrderDetail detail) {
		if (Status.OPEN.equals(detail.getCustomerOrder().getStatus()))
			return detail.getQuantityOrdered();
		else
			return detail.getQuantityShipped();
	}
	
	@Override
	public boolean isActivated(){
		return false;
	}

	@Override
	public CustomerOrder loadOpenCustomerOrder(Account account, String uuid) {
		List<CustomerOrder> customerOrders = customerOrderRepository.findByStatusAndAccountOrUuid(Status.OPEN, account,
				uuid);

		CustomerOrder customerOrder = null;

		if (customerOrders.size() > 0)
			customerOrder = customerOrders.get(0);

		if (customerOrder != null) {
			if (customerOrder.getAccount() == null && account != null) {
				customerOrder.setAccount(account);
				customerOrder = save(customerOrder);
			}
		} else
			customerOrder = generateNewCustomerOrder(account);

		LOGGER.info("Found open order " + customerOrder.getId() + " for "
				+ (account != null ? account.getUsername() : "anonymus account") + ".");

		return customerOrder;
	}

	@Override
	public CustomerOrder save(CustomerOrder customerOrder) {
		customerOrder.getCustomerOrderDetails().forEach(detail -> detail.setCustomerOrder(customerOrder));

		if (customerOrder.getStatus().equals(Status.OPEN) && customerOrder.getOpenDate() == null)
			customerOrder.setOpenDate(new Date());

		if (customerOrder.getStatus().equals(Status.CONFIRMED) && customerOrder.getConfirmedDate() == null)
			customerOrder.setConfirmedDate(new Date());

		if (ShippingMethod.COLISSIMO.equals(customerOrder.getShippingMethod())) {
			customerOrder.setApplicableShippingCostLevel(
					shippingCostLevelService.calculateShippingCostLevel(customerOrder.getTotalWeight()));
			customerOrder.setShippingCost(customerOrder.getApplicableShippingCostLevel().getCost());
		} else {
			customerOrder.setShippingCost(0d);
		}

		calculateAmounts(customerOrder);

		return customerOrderRepository.save(customerOrder);
	}
}
