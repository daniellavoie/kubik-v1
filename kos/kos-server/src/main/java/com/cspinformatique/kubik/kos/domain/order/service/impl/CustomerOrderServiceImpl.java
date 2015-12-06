package com.cspinformatique.kubik.kos.domain.order.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.math3.util.Precision;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.kos.domain.order.repository.CustomerOrderRepository;
import com.cspinformatique.kubik.kos.domain.order.service.CustomerOrderService;
import com.cspinformatique.kubik.kos.domain.order.service.ShippingCostLevelService;
import com.cspinformatique.kubik.kos.domain.product.service.ProductService;
import com.cspinformatique.kubik.kos.model.account.Account;
import com.cspinformatique.kubik.kos.model.account.Address;
import com.cspinformatique.kubik.kos.model.order.CustomerOrder;
import com.cspinformatique.kubik.kos.model.order.CustomerOrderDetail;
import com.cspinformatique.kubik.kos.model.order.CustomerOrder.ShippingMethod;
import com.cspinformatique.kubik.kos.model.order.CustomerOrder.Status;
import com.cspinformatique.kubik.kos.model.product.Product;

@Service
public class CustomerOrderServiceImpl implements CustomerOrderService {
	@Resource
	private CustomerOrderRepository customerOrderRepository;

	@Resource
	private ProductService productService;

	@Resource
	private ShippingCostLevelService shippingCostLevelService;

	@Override
	public CustomerOrder addDetail(CustomerOrder customerOrder, CustomerOrderDetail customerOrderDetail) {
		Product product = productService.findOne(customerOrderDetail.getProduct().getId());

		customerOrderDetail.setProduct(product);

		// Retreives the id and the quantity of an existing detail.
		Optional<CustomerOrderDetail> detailOptional = customerOrder.getCustomerOrderDetails().stream()
				.filter(existingDetail -> existingDetail.getProduct().getId() == product.getId()).findAny();

		if (detailOptional.isPresent()) {
			CustomerOrderDetail existingDetail = detailOptional.get();
			existingDetail.setQuantity(existingDetail.getQuantity() + customerOrderDetail.getQuantity());

			customerOrderDetail = existingDetail;
		} else {
			customerOrderDetail.setCustomerOrder(customerOrder);
			customerOrder.getCustomerOrderDetails().add(customerOrderDetail);
		}

		customerOrderDetail.setUnitPrice(customerOrderDetail.getProduct().getPrice());
		customerOrderDetail.setTotalAmount(customerOrderDetail.getUnitPrice() * customerOrderDetail.getQuantity());

		return save(customerOrder);
	}

	private void calculateAmounts(CustomerOrder customerOrder) {
		int totalQuantity = 0;
		Integer totalWeight = 0;
		Double subTotal = 0d;

		for (CustomerOrderDetail detail : customerOrder.getCustomerOrderDetails()) {
			totalQuantity += detail.getQuantity();
			totalWeight += detail.getProduct().getWeight() * detail.getQuantity();
			subTotal += detail.getProduct().getPrice() * detail.getQuantity();
		}

		customerOrder.setTotalQuantity(totalQuantity);
		customerOrder.setSubTotal(Precision.round(subTotal, 2));
		customerOrder.setTotalAmount(Precision.round(customerOrder.getSubTotal() + customerOrder.getShippingCost(), 2));
		customerOrder.setTotalWeight(totalWeight);
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
				null, 0d, 0d, 0, 0, 0d, null, ShippingMethod.COLISSIMO, shippingAddress, billingAddress, null, null));
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

	@Override
	public CustomerOrder loadOpenCustomerOrder(Account account, String uuid) {
		List<CustomerOrder> customerOrders = customerOrderRepository.findByStatusAndAccountOrUuid(Status.OPEN, account,
				uuid);

		CustomerOrder customerOrder = null;

		if (customerOrders.size() == 1)
			customerOrder = customerOrders.get(0);
		else if (customerOrders.size() > 1) {
			customerOrders = customerOrders.stream()
					.filter(customerOrderToFilter -> customerOrderToFilter.getCustomerOrderDetails().size() != 0)
					.collect(Collectors.toList());

			if (customerOrders.size() == 1)
				customerOrder = customerOrders.get(0);
			else if (customerOrders.size() > 1)
				customerOrder = customerOrders.stream()
						.filter(customerOrderToFilter -> customerOrderToFilter.getAccount() == null).findAny().get();
		}

		if (customerOrder != null) {
			if (customerOrder.getAccount() == null && account != null) {
				customerOrder.setAccount(account);
				customerOrder = save(customerOrder);
			}
		} else
			customerOrder = generateNewCustomerOrder(account);

		return customerOrder;
	}

	@Override
	public CustomerOrder save(CustomerOrder customerOrder) {
		customerOrder.getCustomerOrderDetails().forEach(detail -> detail.setCustomerOrder(customerOrder));

		if (customerOrder.getStatus().equals(Status.OPEN) && customerOrder.getOpenDate() == null) {
			customerOrder.setOpenDate(new Date());
		}

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
