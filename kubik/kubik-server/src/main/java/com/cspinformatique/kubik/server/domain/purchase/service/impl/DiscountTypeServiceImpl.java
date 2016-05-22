package com.cspinformatique.kubik.server.domain.purchase.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.domain.purchase.repository.DiscountTypeRepository;
import com.cspinformatique.kubik.server.domain.purchase.service.DiscountTypeService;
import com.cspinformatique.kubik.server.model.purchase.DiscountType;

@Service
public class DiscountTypeServiceImpl implements DiscountTypeService {
	private DiscountTypeRepository discountTypeRepository;

	@Autowired
	public DiscountTypeServiceImpl(DiscountTypeRepository discountTypeRepository) {
		this.discountTypeRepository = discountTypeRepository;
	}

	@Override
	public DiscountType save(DiscountType discountType) {
		return discountTypeRepository.save(discountType);
	}

}
