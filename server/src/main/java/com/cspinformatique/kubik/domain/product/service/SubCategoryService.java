package com.cspinformatique.kubik.domain.product.service;

import com.cspinformatique.kubik.model.product.SubCategory;

public interface SubCategoryService {
	SubCategory findOne(int id);
	
	SubCategory save(SubCategory subCategory);
}
