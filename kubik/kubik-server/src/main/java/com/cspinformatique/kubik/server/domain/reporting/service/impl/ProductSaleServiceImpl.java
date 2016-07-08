package com.cspinformatique.kubik.server.domain.reporting.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.domain.reporting.model.ProductSale;
import com.cspinformatique.kubik.server.domain.reporting.repository.es.ProductSaleRepository;
import com.cspinformatique.kubik.server.domain.reporting.service.ProductSaleService;
import com.cspinformatique.kubik.server.model.product.Category;
import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.sales.Invoice;
import com.cspinformatique.kubik.server.model.sales.InvoiceDetail;

@Service
public class ProductSaleServiceImpl implements ProductSaleService {
	@Resource
	ProductSaleRepository productSaleRepository;

	@Override
	public void update(Invoice invoice) {
		productSaleRepository.save(invoice.getDetails().stream().map(this::map).collect(Collectors.toList()));
	}

	private ProductSale map(InvoiceDetail invoiceDetail) {
		return new ProductSale(invoiceDetail.getId(), invoiceDetail.getProduct().getEan13(),
				invoiceDetail.getInvoice().getPaidDate(), invoiceDetail.getProduct().getExtendedLabel(),
				mapProductCategories(invoiceDetail.getProduct()), invoiceDetail.getTotalAmount(),
				invoiceDetail.getQuantity(), invoiceDetail.getProduct().getTvaRate1());
	}

	private List<String> mapProductCategories(Product product) {
		List<String> categories = new ArrayList<>();
		Category category = product.getCategory();
		while (category != null) {
			categories.add(category.getName());
			category = category.getParentCategory();
		}

		return categories;
	}

}
