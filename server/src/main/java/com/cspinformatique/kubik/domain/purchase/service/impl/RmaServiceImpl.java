package com.cspinformatique.kubik.domain.purchase.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.domain.purchase.repository.RmaRepository;
import com.cspinformatique.kubik.domain.purchase.service.RmaService;
import com.cspinformatique.kubik.domain.warehouse.service.ProductInventoryService;
import com.cspinformatique.kubik.model.purchase.Rma;
import com.cspinformatique.kubik.model.purchase.RmaDetail;
import com.cspinformatique.kubik.model.purchase.Rma.Status;

@Service
public class RmaServiceImpl implements RmaService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RmaServiceImpl.class);
	
	@Autowired private ProductInventoryService productInventoryService;
	
	@Autowired
	private RmaRepository rmaRepository;

	@Override
	public Page<Rma> findAll(Pageable pageable) {
		return this.rmaRepository.findAll(pageable);
	}
	
	@Override
	public Rma findOne(int id){
		return this.rmaRepository.findOne(id);
	}
	
	@Override
	public double findProductQuantityReturnedToSupplier(int productId){
		Double result = this.rmaRepository.findProductQuantityReturnedToSupplier(productId);
		
		if(result == null){
			return 0d;
		}
		
		return result;
	}
	
	@Override
	public Integer findNext(int rmaId) {
		Page<Integer> result = this.rmaRepository
				.findIdByIdGreaterThan(rmaId, new PageRequest(0, 1,
						Direction.ASC, "id"));

		if (result.getContent().size() == 0) {
			return null;
		}

		return result.getContent().get(0);
	}
	
	@Override
	public Integer findPrevious(int rmaId) {
		Page<Integer> result = this.rmaRepository
				.findIdByIdLessThan(rmaId, new PageRequest(0, 1,
						Direction.DESC, "id"));

		if (result.getContent().size() == 0) {
			return null;
		}

		return result.getContent().get(0);
	}

	@Override
	public Rma save(Rma rma) {
		boolean updateInventory = false;
		
		if(Status.CANCELED.equals(rma.getStatus()) && rma.getCanceledDate() == null){
			rma.setCanceledDate(new Date());
		}else if(Status.SHIPPED.equals(rma.getStatus()) && rma.getShippedDate() == null){
			rma.setShippedDate(new Date());
			updateInventory = true;
		}else if(Status.OPEN.equals(rma.getStatus()) && rma.getOpenDate() == null){
			rma.setOpenDate(new Date());
		}
		
		rma = this.rmaRepository.save(rma);
		
		if(updateInventory){
			LOGGER.info("Updating inventory for products in rma " + rma.getId());
			for(RmaDetail rmaDetail : rma.getDetails()){
				this.productInventoryService.updateInventory(rmaDetail.getProduct());
			}
		}
		
		return rma;
	}

}
