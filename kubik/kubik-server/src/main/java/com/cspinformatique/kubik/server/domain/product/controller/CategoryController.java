package com.cspinformatique.kubik.server.domain.product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cspinformatique.kubik.server.domain.product.service.CategoryService;
import com.cspinformatique.kubik.server.model.product.Category;

@Controller
@RequestMapping("/category")
public class CategoryController {
	@Autowired
	private CategoryService categoryService;

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void delete(@PathVariable int id) {
		categoryService.delete(id);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}/product", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void deleteProductCategories(@PathVariable int id) {
		categoryService.deleteProductCategories(categoryService.findOne(id));
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Category> findRootCategories() {
		return categoryService.findByRootCategory(true);
	}

	@RequestMapping(method = RequestMethod.GET, params = "page", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Page<Category> findAll(@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "50") Integer resultPerPage,
			@RequestParam(defaultValue = "ASC") Direction direction,
			@RequestParam(defaultValue = "name") String sortBy) {
		return categoryService.findAll(new PageRequest(page, resultPerPage, direction, sortBy));
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Category findOne(@PathVariable int id) {
		return categoryService.findOne(id);
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.GET, params = "newName", produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String generateNewName() {
		return categoryService.generateNewName();
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getCategoriesPage() {
		return "product/categories-page";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getCategoryPage(@PathVariable int id) {
		return "product/category";
	}

	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Category save(@RequestBody Category category) {
		return categoryService.save(category);
	}
}
