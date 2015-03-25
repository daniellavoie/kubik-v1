package com.cspinformatique.kubik.domain.purchase.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.domain.product.service.ProductService;
import com.cspinformatique.kubik.domain.purchase.model.NotationCode;
import com.cspinformatique.kubik.domain.purchase.model.PurchaseOrder;
import com.cspinformatique.kubik.domain.purchase.model.PurchaseOrderDetail;
import com.cspinformatique.kubik.domain.purchase.model.PurchaseSession;
import com.cspinformatique.kubik.domain.purchase.model.PurchaseSessionDetail;
import com.cspinformatique.kubik.domain.purchase.model.ShippingMode;
import com.cspinformatique.kubik.domain.purchase.model.PurchaseSession.Status;
import com.cspinformatique.kubik.domain.purchase.repository.PurchaseSessionRepository;
import com.cspinformatique.kubik.domain.purchase.service.PurchaseOrderService;
import com.cspinformatique.kubik.domain.purchase.service.PurchaseSessionDetailService;
import com.cspinformatique.kubik.domain.purchase.service.PurchaseSessionService;
import com.cspinformatique.kubik.product.model.Product;
import com.cspinformatique.kubik.product.model.Supplier;

@Service
public class PurchaseSessionServiceImpl implements PurchaseSessionService {
	@Autowired
	private ProductService productService;

	@Autowired
	private PurchaseOrderService purchaseOrderService;

	@Autowired
	private PurchaseSessionDetailService purchaseSessionDetailService;

	@Autowired
	private PurchaseSessionRepository purchaseSessionRepository;

	@Override
	public Iterable<PurchaseSession> findAll() {
		return this.purchaseSessionRepository.findAll();
	}

	@Override
	public Page<PurchaseSession> findAll(Pageable pageable) {
		return this.purchaseSessionRepository.findAll(pageable);
	}

	@Override
	public Page<PurchaseSession> findByStatus(Status status, Pageable pageable) {
		return this.purchaseSessionRepository.findByStatus(status, pageable);
	}

	@Override
	public Iterable<PurchaseSession> findByProduct(Product product) {
		return this.purchaseSessionDetailService
				.findPurchaseOrdersByProduct(product);
	}

	@Override
	public Iterable<PurchaseSession> findByProductAndStatus(Product product,
			Status status) {
		return this.purchaseSessionDetailService
				.findPurchaseOrdersByProductAndStatus(product, status);
	}

	@Override
	public PurchaseSession findOne(int id) {
		return this.purchaseSessionRepository.findOne(id);
	}

	private void generatePurchaseOrder(PurchaseSession purchaseSession) {
		// Generates a purchase order for every distinct supplier concerned by
		// the delivery.
		Map<String, PurchaseOrder> purchaseOrders = new HashMap<String, PurchaseOrder>();

		Calendar maxDeliveryCalendar = Calendar.getInstance();
		maxDeliveryCalendar.add(Calendar.DAY_OF_MONTH, 7);
		Date maxDeliveryDate = maxDeliveryCalendar.getTime();

		for (PurchaseSessionDetail detail : purchaseSession.getDetails()) {
			detail.setProduct(this.productService.findOne(detail.getProduct()
					.getId()));

			Supplier supplier = detail.getProduct().getSupplier();

			PurchaseOrder order = purchaseOrders.get(supplier.getEan13());

			if (order == null) {
				order = new PurchaseOrder(0, supplier, new Date(), null, null,
						ShippingMode.USUAL_METHOD, NotationCode.USUAL_RULE,
						new Date(), maxDeliveryDate,
						new ArrayList<PurchaseOrderDetail>(),
						PurchaseOrder.Status.DRAFT, false, false, 0f, 0d, purchaseSession, null);

				purchaseOrders.put(supplier.getEan13(), order);
			}
			
			order.getDetails().add(
					new PurchaseOrderDetail(null, order, detail.getProduct(),
							detail.getQuantity(), 0f, 0f, null,
							0d, 0d));
		}

		this.purchaseOrderService.save(purchaseOrders.values());
	}

	@Override
	@Transactional
	public PurchaseSession save(PurchaseSession purchaseSession) {
		if (purchaseSession.getOpenDate() == null) {
			purchaseSession.setOpenDate(new Date());
		}

		if (purchaseSession.getMinDeliveryDate() == null) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			purchaseSession.setMinDeliveryDate(calendar.getTime());
		}

		if (purchaseSession.getMaxDeliveryDate() == null) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, 7);
			purchaseSession.setMaxDeliveryDate(calendar.getTime());
		}

		if (purchaseSession.getStatus() == null) {
			purchaseSession.setStatus(Status.DRAFT);
		}

		// Checks if the product presents exists in the database.
		if (purchaseSession.getDetails() != null) {
			for (PurchaseSessionDetail detail : purchaseSession.getDetails()) {
				// Generates the missing ones.
				detail.setProduct(this.productService
						.generateProductIfNotFound(detail.getProduct()
								.getEan13(), detail.getProduct().getSupplier()
								.getEan13()));
			}
		}

		Status status = purchaseSession.getStatus();
		if (status.equals(Status.SUBMITED) || status.equals(Status.CANCELED)) {
			PurchaseSession oldSession = this.findOne(purchaseSession.getId());

			if (oldSession == null
					|| oldSession.getStatus().equals(Status.DRAFT)) {
				if (status.equals(Status.SUBMITED)) {
					// Generate the purchase order
					this.generatePurchaseOrder(purchaseSession);
				}

				purchaseSession.setCloseDate(new Date());
			}
		}

		return this.purchaseSessionRepository.save(purchaseSession);
	}
}
