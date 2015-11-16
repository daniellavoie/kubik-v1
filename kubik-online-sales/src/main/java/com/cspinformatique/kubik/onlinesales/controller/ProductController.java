package com.cspinformatique.kubik.onlinesales.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
		return "product/product";
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getProductSearchPage() {
		return "product/product-search";
	}

	@RequestMapping(value = "/{id}/image/{size}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public @ResponseBody ResponseEntity<InputStreamResource> loadProductImage(@PathVariable int id,
			@PathVariable ProductImageSize size) {
		ProductImage productImage = productImageService.findByProductAndSize(productService.findOne(id), size);

		if (productImage != null) {
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentLength(productImage.getContentLength());

			return new ResponseEntity<InputStreamResource>(
					new InputStreamResource(productImageService.loadImageInputStream(productService.findOne(id), size)),
					httpHeaders, HttpStatus.OK);
		} else {
			return new ResponseEntity<InputStreamResource>(null, new HttpHeaders(), HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Page<Product> search(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size, @RequestParam(defaultValue = "ASC") Direction direction,
			@RequestParam(defaultValue = "title") String orderBy, @RequestParam(required = false) String title,
			@RequestParam(required = false) String author,
			@RequestParam(value = "categories[]", required = false) Integer[] categories,
			@RequestParam(required = false) Date publishFrom, @RequestParam(required = false) Date publishUntil,
			@RequestParam(required = false) String manufacturer, @RequestParam(required = false) Double priceFrom,
			@RequestParam(required = false) Double priceTo, @RequestParam(required = false) String query) {
		return productService
				.search(title, author,
						categories != null ? Arrays.asList(categories).stream().map(id -> Integer.valueOf(id))
								.collect(Collectors.toList()) : null,
						publishFrom, publishUntil, manufacturer, priceFrom, priceTo, query,
						new PageRequest(page, size, direction, orderBy));
	}
}
