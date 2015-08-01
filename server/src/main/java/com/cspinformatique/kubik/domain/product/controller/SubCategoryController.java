package com.cspinformatique.kubik.domain.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.domain.product.service.SubCategoryService;
import com.cspinformatique.kubik.model.product.SubCategory;

@Controller
@RequestMapping("/subCategory")
public class SubCategoryController {
	@Autowired
	private SubCategoryService subCategoryService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody SubCategory findOne(@PathVariable int id) {
		return subCategoryService.findOne(id);
	}
}
