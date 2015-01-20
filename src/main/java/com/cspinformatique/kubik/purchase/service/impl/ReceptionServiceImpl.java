package com.cspinformatique.kubik.purchase.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.purchase.model.Reception;
import com.cspinformatique.kubik.purchase.model.ReceptionDetail;
import com.cspinformatique.kubik.purchase.model.Reception.Status;
import com.cspinformatique.kubik.purchase.repository.ReceptionRepository;
import com.cspinformatique.kubik.purchase.service.ReceptionService;
import com.cspinformatique.kubik.warehouse.service.ProductInventoryService;

@Service
public class ReceptionServiceImpl implements ReceptionService {
	@Autowired private ProductInventoryService productInventoryService;
	@Autowired private ReceptionRepository receptionRepository;
	
	@Override
	public Iterable<Reception> findAll() {
		return this.receptionRepository.findAll();
	}

	@Override
	public Reception findOne(int id) {
		return this.receptionRepository.findOne(id);
	}

	@Override
	public Reception save(Reception reception) {
		if(reception.getStatus().equals(Status.CLOSED)){
			Reception oldReception = this.findOne(reception.getId());
			
			if(oldReception == null || !oldReception.getStatus().equals(reception.getStatus())){
				reception.setDateReceived(new Date());
				this.updateInventory(reception);
			}
		}
		
		return this.receptionRepository.save(reception);
	}
	
	private void updateInventory(Reception reception){
		for(ReceptionDetail detail : reception.getDetails()){
			productInventoryService.addInventory(detail.getProduct(), detail.getQuantityReceived());
		}
	}
}
