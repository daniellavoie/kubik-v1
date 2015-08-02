package com.cspinformatique.kubik.domain.product.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.domain.product.repository.SubCategoryRepository;
import com.cspinformatique.kubik.domain.product.service.ProductService;
import com.cspinformatique.kubik.domain.product.service.SubCategoryService;
import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.product.SubCategory;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {
	@Autowired private ProductService productService;
	
	@Autowired private SubCategoryRepository subCategoryRepository;
	
	@Override
	public void delete(int id){
		for(Product product : productService.findBySubCategory(this.findOne(id))){
			product.setSubCategory(null);
			
			this.productService.save(product);
		}
		
		subCategoryRepository.delete(id);
	}
	
	@Override
	public SubCategory findOne(int id) {
		return subCategoryRepository.findOne(id);
	}

	@Override
	public SubCategory save(SubCategory subCategory) {
		return subCategoryRepository.save(subCategory);
	}
	
	public SubCategory updateProductCount(SubCategory subCategory){
		subCategory.setProductCount(productService.countBySubCategory(subCategory));
		
		return this.save(subCategory);
	}

}
