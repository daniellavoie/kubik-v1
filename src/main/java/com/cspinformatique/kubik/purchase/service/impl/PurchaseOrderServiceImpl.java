package com.cspinformatique.kubik.purchase.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.product.service.ProductService;
import com.cspinformatique.kubik.purchase.model.DeliveryDateType;
import com.cspinformatique.kubik.purchase.model.PurchaseOrder;
import com.cspinformatique.kubik.purchase.model.PurchaseOrder.Status;
import com.cspinformatique.kubik.purchase.model.PurchaseOrderDetail;
import com.cspinformatique.kubik.purchase.model.Reception;
import com.cspinformatique.kubik.purchase.model.ReceptionDetail;
import com.cspinformatique.kubik.purchase.model.ShippingPackage;
import com.cspinformatique.kubik.purchase.repository.PurchaseOrderRepository;
import com.cspinformatique.kubik.purchase.service.DilicomOrderService;
import com.cspinformatique.kubik.purchase.service.PurchaseOrderService;
import com.cspinformatique.kubik.purchase.service.ReceptionService;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
	@Autowired
	private DilicomOrderService dilicomOrderService;

	@Autowired
	private ProductService productService;

	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;

	@Autowired
	private ReceptionService receptionService;

	private DateFormat dateFormat;

	public PurchaseOrderServiceImpl() {
		this.dateFormat = new SimpleDateFormat("yy");
	}

	@Override
	public Iterable<PurchaseOrder> findAll() {
		return this.purchaseOrderRepository.findAll();
	}

	@Override
	public Iterable<PurchaseOrder> findAll(Pageable pageable) {
		return this.purchaseOrderRepository.findAll(pageable);
	}

	@Override
	public PurchaseOrder findOne(long id) {
		return this.purchaseOrderRepository.findOne(id);
	}

	private long generateId() {
		String dateString = dateFormat.format(new Date());

		Page<PurchaseOrder> page = this.purchaseOrderRepository
				.findAll(new PageRequest(0, 1, Direction.DESC, "id"));

		long number = Long.valueOf(dateString + "000001");
		if (page.getContent().size() > 0) {
			long lastNumber = page.getContent().get(0).getId();
			String lastDateString = String.valueOf(lastNumber).substring(0, 2);

			if (lastDateString.equals(dateString)) {
				number = lastNumber + 1;
			}
		}

		return number;
	}

	@Override
	public Reception generateReception(PurchaseOrder purchaseOrder) {
		Reception reception = new Reception(null, purchaseOrder.getSupplier(), purchaseOrder.getShippingMode(), new Date(), null,
				DeliveryDateType.SOONEST, purchaseOrder.getMinDeliveryDate(),
				purchaseOrder, new ArrayList<ReceptionDetail>(),
				Reception.Status.OPEN, new ArrayList<ShippingPackage>());

		for (PurchaseOrderDetail detail : purchaseOrder.getDetails()) {
			reception.getDetails().add(
					new ReceptionDetail(null, reception, detail.getProduct(),
							detail.getQuantity(), 0));
		}

		return this.receptionService.save(reception);
	}

	public PurchaseOrder save(PurchaseOrder purchaseOrder) {
		return this.save(Arrays.asList(new PurchaseOrder[] { purchaseOrder }))
				.iterator().next();
	}

	@Override
	@Transactional
	public Iterable<PurchaseOrder> save(Iterable<PurchaseOrder> entities) {
		try {
			List<String> orderReferences = new ArrayList<String>();

			for (PurchaseOrder purchaseOrder : entities) {
				if (purchaseOrder.getId() == 0) {
					purchaseOrder.setId(this.generateId());
				}

				// Checks if the product presents exists in the database.
				if (purchaseOrder.getDetails() != null) {
					for (PurchaseOrderDetail detail : purchaseOrder
							.getDetails()) {
						// Generates the missing ones.
						this.productService.generateProductIfNotFound(detail
								.getProduct().getEan13(), detail.getProduct()
								.getSupplier().getEan13());
					}
				}

				if (purchaseOrder.getStatus().equals(Status.SUBMITED)) {
					PurchaseOrder oldOrder = this
							.findOne(purchaseOrder.getId());

					if ((oldOrder == null
							|| !purchaseOrder.getStatus().equals(
									oldOrder.getStatus()) && !purchaseOrder.isSentToDilicom())) {
						this.generateReception(purchaseOrder);
					}

					if ((oldOrder == null || !oldOrder.isSentToDilicom())
							&& purchaseOrder.isSentToDilicom()) {
						orderReferences.add(this.dilicomOrderService
								.sendOrderToDilicom(purchaseOrder));
					}
				}
			}

			Iterable<PurchaseOrder> results = this.purchaseOrderRepository
					.save(entities);

			// Confirm the orders (rename the FTP files).

			return results;
		} catch (Exception ex) {
			// Cancels the orders (delete the files from FTP).

			throw new RuntimeException(ex);
		}
	}
}
