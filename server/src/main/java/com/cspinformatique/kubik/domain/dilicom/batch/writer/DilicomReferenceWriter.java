package com.cspinformatique.kubik.domain.dilicom.batch.writer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cspinformatique.kubik.domain.dilicom.model.Reference;
import com.cspinformatique.kubik.domain.dilicom.model.ReferenceNotification;
import com.cspinformatique.kubik.domain.dilicom.service.ReferenceNotificationService;
import com.cspinformatique.kubik.domain.dilicom.service.ReferenceService;
import com.cspinformatique.kubik.domain.product.service.ProductService;
import com.cspinformatique.kubik.domain.product.service.SupplierService;
import com.cspinformatique.kubik.model.product.Product;

@Component
public class DilicomReferenceWriter implements ItemWriter<Reference> {
	@Autowired
	private ReferenceNotificationService referenceNotificationService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ReferenceService referenceService;

	@Autowired
	private SupplierService supplierService;

	@Override
	public void write(List<? extends Reference> items) throws Exception {
		// Using a map based on product ids since only a single notification may
		// exists for a single product.
		// Persistence layer will encounter a constraint error if two
		// notifications for a single product are
		// sent for persistence.
		Map<Integer, ReferenceNotification> notifications = new HashMap<Integer, ReferenceNotification>();
		Map<String, Reference> references = new HashMap<String, Reference>();
		for (Reference reference : items) {
			if (this.productService.getProductIdsCache().contains(
					reference.getEan13() + "-" + reference.getSupplierEan13())) {
				Product product = this.productService.findByEan13AndSupplier(
						reference.getEan13(), supplierService
								.findByEan13(reference.getSupplierEan13()));
				notifications.put(product.getId(), new ReferenceNotification(
						null, product, ReferenceNotification.Status.NEW, null,
						null));
			}
			
			references.put(reference.getEan13() + "-" + reference.getSupplierEan13(), reference);
		}

		if (!notifications.isEmpty()) {
			this.referenceNotificationService.save(notifications.values());
		}

		this.referenceService.save(references.values());
	}
}