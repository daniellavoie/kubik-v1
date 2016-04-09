package com.cspinformatique.kubik.server.domain.purchase.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.domain.purchase.repository.RmaRepository;
import com.cspinformatique.kubik.server.domain.purchase.service.RmaService;
import com.cspinformatique.kubik.server.domain.warehouse.service.ProductInventoryService;
import com.cspinformatique.kubik.server.domain.warehouse.service.StocktakingService;
import com.cspinformatique.kubik.server.model.purchase.Rma;
import com.cspinformatique.kubik.server.model.purchase.Rma.Status;
import com.cspinformatique.kubik.server.model.purchase.RmaDetail;

@Service
public class RmaServiceImpl implements RmaService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RmaServiceImpl.class);

	@Resource
	ProductInventoryService productInventoryService;

	@Resource
	RmaRepository rmaRepository;

	@Resource
	StocktakingService stocktakingService;

	@Override
	public Page<Rma> findAll(Pageable pageable) {
		return rmaRepository.findAll(pageable);
	}

	@Override
	public List<Rma> findByStatusAndShippedDateAfter(Status status, Date shippedDate) {
		return rmaRepository.findByStatusAndShippedDateAfter(status, shippedDate);
	}

	@Override
	public Rma findOne(int id) {
		return rmaRepository.findOne(id);
	}

	@Override
	public Integer findNext(int rmaId) {
		Page<Integer> result = rmaRepository.findIdByIdGreaterThan(rmaId, new PageRequest(0, 1, Direction.ASC, "id"));

		if (result.getContent().size() == 0) {
			return null;
		}

		return result.getContent().get(0);
	}

	@Override
	public Integer findPrevious(int rmaId) {
		Page<Integer> result = rmaRepository.findIdByIdLessThan(rmaId, new PageRequest(0, 1, Direction.DESC, "id"));

		if (result.getContent().size() == 0) {
			return null;
		}

		return result.getContent().get(0);
	}

	@Override
	public Rma save(Rma rma) {
		boolean updateInventory = false;

		if (Status.CANCELED.equals(rma.getStatus()) && rma.getCanceledDate() == null) {
			rma.setCanceledDate(new Date());
		} else if (Status.SHIPPED.equals(rma.getStatus()) && rma.getShippedDate() == null) {
			rma.setShippedDate(new Date());
			updateInventory = true;
		} else if (Status.OPEN.equals(rma.getStatus()) && rma.getOpenDate() == null) {
			rma.setOpenDate(new Date());
		}

		rma = rmaRepository.save(rma);

		if (updateInventory) {
			LOGGER.info("Updating inventory for products in rma " + rma.getId());

			for (RmaDetail rmaDetail : rma.getDetails()) {
				productInventoryService.updateInventory(rmaDetail.getProduct());

				stocktakingService.applyInventoryAdjustments(rmaDetail.getProduct(), rmaDetail.getQuantity() * -1);
			}
		}

		return rma;
	}

	@Override
	public double findProductQuantityReturnedToSupplier(int productId) {
		Double result = rmaRepository.findProductQuantityReturnedToSupplier(productId);

		if (result == null) {
			return 0d;
		}

		return result;
	}

	@Override
	public double findProductQuantityReturnedToSupplierUntil(int productId, Date until) {
		Double result = rmaRepository.findProductQuantityReturnedToSupplierUntil(productId, until);

		if (result == null) {
			return 0d;
		}

		return result;
	}

}
