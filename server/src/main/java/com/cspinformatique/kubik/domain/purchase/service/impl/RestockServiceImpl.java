package com.cspinformatique.kubik.domain.purchase.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.domain.purchase.repository.RestockRepository;
import com.cspinformatique.kubik.domain.purchase.service.PurchaseSessionService;
import com.cspinformatique.kubik.domain.purchase.service.RestockService;
import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.purchase.Restock;
import com.cspinformatique.kubik.model.purchase.Restock.Status;

@Service
public class RestockServiceImpl implements RestockService {
	@Autowired
	private PurchaseSessionService purchaseSessionService;

	@Autowired
	private RestockRepository restockRepository;

	@Override
	public Long countByStatus(Status status) {
		return this.restockRepository.countByStatus(status);
	}

	@Override
	public Page<Restock> findByStatus(Status status, Pageable pageable) {
		return this.restockRepository.findByStatus(status, pageable);
	}

	@Override
	public Restock findOne(int id) {
		return this.restockRepository.findOne(id);
	}

	@Override
	public Restock restockProduct(Product product, double quantity) {
		Restock restock = this.restockRepository.findByProductAndStatus(
				product, Status.OPEN);

		if (restock == null) {
			restock = new Restock(0, product, 0, Status.OPEN, new Date(), null,
					null, null);
		}

		restock.setQuantity(restock.getQuantity() + quantity);

		return this.save(restock);
	}

	@Override
	public Restock save(Restock restock) {
		if (restock.getStatus().equals(Status.VALIDATED)
				&& restock.getValidatedDate() == null) {
			restock.setValidatedDate(new Date());

			// Add the product to a purchase session.
			restock.setPurchaseSession(this.purchaseSessionService
					.addProductToNewestPurchaseSession(restock.getProduct(),
							restock.getQuantity()));
		}

		return this.restockRepository.save(restock);
	}

}
