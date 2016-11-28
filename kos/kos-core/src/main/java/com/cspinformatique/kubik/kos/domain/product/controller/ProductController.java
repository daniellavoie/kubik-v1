package com.cspinformatique.kubik.kos.domain.product.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.kos.domain.product.exception.ImageNotFoundException;
import com.cspinformatique.kubik.kos.domain.product.service.ProductImageService;
import com.cspinformatique.kubik.kos.domain.product.service.ProductService;
import com.cspinformatique.kubik.kos.model.product.Product;
import com.cspinformatique.kubik.kos.model.product.ProductImageSize;

@Controller
@RequestMapping({ "/product", "/produit" })
public class ProductController {
	@Resource
	private ProductImageService productImageService;

	@Resource
	private ProductService productService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Product findOne(@PathVariable int id) {
		return productService.findOne(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getProductPage() {
		return "product/product";
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getProductSearchPage() {
		return "product/product-search";
	}

	@RequestMapping(value = "/{id}/image/{size}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public @ResponseBody ResponseEntity<InputStreamResource> loadProductImage(@PathVariable int id,
			@PathVariable ProductImageSize size) {
		try {
			return new ResponseEntity<InputStreamResource>(
					new InputStreamResource(productImageService.loadImageInputStream(productService.findOne(id), size)),
					HttpStatus.OK);
		} catch (ImageNotFoundException imageNotFoundEx) {
			try {
				InputStream inputStream = new ClassPathResource("resources/img/logos/dimension-fantastique-noir.png")
						.getInputStream();

				return new ResponseEntity<InputStreamResource>(new InputStreamResource(inputStream), HttpStatus.OK);
			} catch (IOException ioEx) {
				throw new RuntimeException(ioEx);
			}
		}
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Page<Product> search(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size, @RequestParam(defaultValue = "ASC") Direction direction,
			@RequestParam(defaultValue = "title") String orderBy, @RequestParam(required = false) String title,
			@RequestParam(required = false) String brand,
			@RequestParam(value = "categories[]", required = false) String[] categories,
			@RequestParam(required = false) Date publishFrom, @RequestParam(required = false) Date publishUntil,
			@RequestParam(required = false) String manufacturer, @RequestParam(required = false) Double priceFrom,
			@RequestParam(required = false) Double priceTo, @RequestParam(defaultValue = "true") boolean hideUnavailable,
			@RequestParam(required = false) String query) {
		return productService.search(title, brand, categories != null ? Arrays.asList(categories) : null, publishFrom,
				publishUntil, manufacturer, priceFrom, priceTo, hideUnavailable, query, new PageRequest(page, size, direction, orderBy));
	}
}