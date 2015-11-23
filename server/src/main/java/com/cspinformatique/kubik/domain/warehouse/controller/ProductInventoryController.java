package com.cspinformatique.kubik.domain.warehouse.controller;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cspinformatique.kubik.domain.product.service.ProductService;
import com.cspinformatique.kubik.domain.warehouse.model.InventoryExtract;
import com.cspinformatique.kubik.domain.warehouse.service.ProductInventoryService;

@Controller
@RequestMapping("/productInventory")
public class ProductInventoryController {
	@Resource
	private ProductInventoryService productInventoryService;

	@Resource
	private ProductService productService;

	@RequestMapping(params = { "separator", "decimalSeparator" }, produces = "text/csv; charset=utf-8")
	public @ResponseBody InventoryExtract generateInventoryExtract(@RequestParam String separator,
			@RequestParam String decimalSeparator) {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(decimalSeparator.toCharArray()[0]);

		DecimalFormat decimalFormat = new DecimalFormat("0.00", symbols);
		decimalFormat.setRoundingMode(RoundingMode.HALF_UP);

		return productInventoryService.generateInventoryExtraction(separator, decimalFormat);
	}

	@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
	public String getExportProductInventoryPage() {
		return "warehouse/export-product-inventory";
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/product/{ean13}", method = RequestMethod.GET, params = "updateInventory")
	public void updateProductInventory(@PathVariable String ean13) {
		productInventoryService.updateInventory(productService.findByEan13(ean13).iterator().next());
	}

	@RequestMapping(value = "/product", method = RequestMethod.GET, params = "updateInventory", produces = MediaType.TEXT_HTML_VALUE)
	public @ResponseBody String updateProductInventorird() {
		productInventoryService.updateInventories();

		return "DONE";
	}
}
