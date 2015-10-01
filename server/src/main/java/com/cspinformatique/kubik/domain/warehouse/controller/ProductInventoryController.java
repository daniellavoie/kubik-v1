package com.cspinformatique.kubik.domain.warehouse.controller;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.domain.warehouse.model.InventoryExtract;
import com.cspinformatique.kubik.domain.warehouse.service.ProductInventoryService;

@Controller
@RequestMapping("/productInventory")
public class ProductInventoryController {
	@Resource
	private ProductInventoryService productInventoryService;
	
	@RequestMapping(params = {"separator", "decimalSeparator"}, produces = "text/csv; charset=utf-8")
	public @ResponseBody InventoryExtract generateInventoryExtract(@RequestParam String separator,
			@RequestParam String decimalSeparator) {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(decimalSeparator.toCharArray()[0]);

		DecimalFormat decimalFormat = new DecimalFormat("0.00", symbols);
		decimalFormat.setRoundingMode(RoundingMode.HALF_UP);

		return productInventoryService.generateInventoryExtraction(separator, decimalFormat);
	}
	
	@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
	public String getExportProductInventoryPage(){
		return "warehouse/export-product-inventory";
	}
}
