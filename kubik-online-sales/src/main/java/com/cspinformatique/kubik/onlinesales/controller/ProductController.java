package com.cspinformatique.kubik.onlinesales.controller;

import javax.annotation.Resource;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.onlinesales.model.Product;
import com.cspinformatique.kubik.onlinesales.model.ProductImage;
import com.cspinformatique.kubik.onlinesales.model.ProductImageSize;
import com.cspinformatique.kubik.onlinesales.service.ProductImageService;
import com.cspinformatique.kubik.onlinesales.service.ProductService;

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
		return "product";
	}

	@RequestMapping(value = "/{id}/image/{size}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public @ResponseBody ResponseEntity<InputStreamResource> loadProductImage(@PathVariable int id,
			@PathVariable ProductImageSize size) {
		ProductImage productImage = productImageService.findByProductAndSize(productService.findOne(id), size);

		if (productImage != null) {

			return new ResponseEntity<InputStreamResource>(
					new InputStreamResource(productImageService.loadImageInputStream(productService.findOne(id), size)),
					new HttpHeaders(), HttpStatus.OK);
		} else {
			return new ResponseEntity<InputStreamResource>(null, new HttpHeaders(), HttpStatus.NOT_FOUND);
		}
	}
}
