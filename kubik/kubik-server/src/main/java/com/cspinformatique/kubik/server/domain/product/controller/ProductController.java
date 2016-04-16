package com.cspinformatique.kubik.server.domain.product.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.cspinformatique.kubik.server.domain.product.exception.ImageNotFoundException;
import com.cspinformatique.kubik.server.domain.product.service.CategoryService;
import com.cspinformatique.kubik.server.domain.product.service.ProductImageService;
import com.cspinformatique.kubik.server.domain.product.service.ProductService;
import com.cspinformatique.kubik.server.domain.product.service.ProductStatsService;
import com.cspinformatique.kubik.server.domain.product.service.SupplierService;
import com.cspinformatique.kubik.server.domain.purchase.service.ReceptionDetailService;
import com.cspinformatique.kubik.server.domain.purchase.service.RmaDetailService;
import com.cspinformatique.kubik.server.domain.sales.service.CustomerCreditDetailService;
import com.cspinformatique.kubik.server.domain.sales.service.InvoiceDetailService;
import com.cspinformatique.kubik.server.domain.warehouse.service.InventoryCountService;
import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.product.ProductImageSize;
import com.cspinformatique.kubik.server.model.product.ProductStats;
import com.cspinformatique.kubik.server.model.purchase.Reception;
import com.cspinformatique.kubik.server.model.purchase.ReceptionDetail;
import com.cspinformatique.kubik.server.model.purchase.Rma;
import com.cspinformatique.kubik.server.model.purchase.RmaDetail;
import com.cspinformatique.kubik.server.model.sales.CustomerCredit;
import com.cspinformatique.kubik.server.model.sales.CustomerCreditDetail;
import com.cspinformatique.kubik.server.model.sales.InvoiceDetail;
import com.cspinformatique.kubik.server.model.sales.InvoiceStatus;
import com.cspinformatique.kubik.server.model.sales.ProductInvoice;
import com.cspinformatique.kubik.server.model.warehouse.InventoryCount;

@Controller
@RequestMapping("/product")
public class ProductController {
	@Resource
	private CategoryService categoryService;

	@Resource
	private ProductService productService;

	@Resource
	private SupplierService supplierService;

	@Resource
	private CustomerCreditDetailService customerCreditDetailService;

	@Resource
	private InventoryCountService inventoryCountService;

	@Resource
	private InvoiceDetailService invoiceDetailService;

	@Resource
	private ProductImageService productImageService;

	@Resource
	private ProductStatsService productsStatsService;

	@Resource
	private ReceptionDetailService receptionDetailService;

	@Resource
	private RmaDetailService rmaDetailService;

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, params = "category", produces = MediaType.APPLICATION_JSON_VALUE)
	public Integer countByCategory(@RequestParam Integer category) {
		return productService.countByCategory(category != null ? categoryService.findOne(category) : null);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, params = { "count",
			"nonValidatedProductImages" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public Integer countWithoutValidatedImages() {
		return productService.countByImagesValidated(false);
	}

	@ResponseBody
	@RequestMapping(params = "ean13", produces = MediaType.APPLICATION_JSON_VALUE)
	public Product findByEan13(@RequestParam String ean13) {
		return productService.findByEan13(ean13);
	}

	@ResponseBody
	@RequestMapping(params = { "ean13", "supplierEan13" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public Product findByEan13AndSupplierEan13(@RequestParam String ean13, @RequestParam String supplierEan13) {
		return this.productService.findByEan13AndSupplier(ean13, this.supplierService.findByEan13(supplierEan13));
	}

	@ResponseBody
	@RequestMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Product findOne(@PathVariable int id) {
		return productService.findOne(id);
	}

	@ResponseBody
	@RequestMapping(value = "/{id}/productStats", produces = MediaType.APPLICATION_JSON_VALUE)
	public ProductStats findProductStats(@PathVariable int id, @RequestParam(required = false) Date startDate,
			@RequestParam(required = false) Date endDate) {
		if (startDate == null) {
			startDate = new Date(0);
		}
		if (endDate == null) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, 1000);

			endDate = cal.getTime();
		}

		return productsStatsService.findByProductId(id, startDate, endDate);
	}

	@ResponseBody
	@RequestMapping(value = "/{productId}/confirmedInvoice", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<ProductInvoice> findProductConfirmedInvoices(@PathVariable("productId") int productId,
			@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer resultPerPage,
			@RequestParam(defaultValue = "DESC") Direction direction,
			@RequestParam(defaultValue = "invoice.confirmedDate") String sortBy) {
		Pageable pageable = new PageRequest(page, resultPerPage, direction, sortBy);

		Page<InvoiceDetail> invoiceDetailPage = invoiceDetailService.findByProductAndInvoiceStatus(
				productService.findOne(productId), new InvoiceStatus(InvoiceStatus.Types.ORDER_CONFIRMED.name(), null),
				pageable);

		return new PageImpl<>(invoiceDetailPage.getContent().stream()
				.map(detail -> new ProductInvoice(detail, detail.getInvoice())).collect(Collectors.toList()), pageable,
				invoiceDetailPage.getTotalElements());
	}

	@ResponseBody
	@RequestMapping(value = "/{productId}/customerCredit", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<CustomerCreditDetail> findProductCustomerCredits(@PathVariable("productId") int productId,
			@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer resultPerPage,
			@RequestParam(required = false) Direction direction,
			@RequestParam(defaultValue = "extendedLabel") String sortBy) {
		return this.customerCreditDetailService.findByProductAndCustomerCreditStatus(productService.findOne(productId),
				CustomerCredit.Status.COMPLETED,
				new PageRequest(page, resultPerPage, direction != null ? direction : Direction.ASC, sortBy));
	}

	@ResponseBody
	@RequestMapping(value = "/{productId}/inventoryCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<InventoryCount> findProductInventoryCounts(@PathVariable("productId") int productId,
			@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer resultPerPage,
			@RequestParam(defaultValue = "DESC") Direction direction,
			@RequestParam(defaultValue = "dateCounted") String sortBy) {
		return inventoryCountService.findByProduct(productService.findOne(productId),
				new PageRequest(page, resultPerPage, direction != null ? direction : Direction.ASC, sortBy));
	}

	@ResponseBody
	@RequestMapping(value = "/{productId}/paidInvoice", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<ProductInvoice> findProductPaidInvoices(@PathVariable("productId") int productId,
			@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer resultPerPage,
			@RequestParam(defaultValue = "DESC") Direction direction,
			@RequestParam(defaultValue = "invoice.paidDate") String sortBy) {
		Pageable pageable = new PageRequest(page, resultPerPage, direction, sortBy);
		Page<InvoiceDetail> detailsPage = invoiceDetailService.findByProductAndInvoiceStatus(
				productService.findOne(productId), new InvoiceStatus(InvoiceStatus.Types.PAID.name(), null), pageable);

		return new PageImpl<>(detailsPage.getContent().stream()
				.map(detail -> new ProductInvoice(detail, detail.getInvoice())).collect(Collectors.toList()), pageable,
				detailsPage.getTotalElements());
	}

	@ResponseBody
	@RequestMapping(value = "/{productId}/reception", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<ReceptionDetail> findProductPurchaseOrders(@PathVariable("productId") int productId,
			@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer resultPerPage,
			@RequestParam(required = false) Direction direction,
			@RequestParam(defaultValue = "extendedLabel") String sortBy) {
		return receptionDetailService.findByProductAndReceptionStatus(productService.findOne(productId),
				Reception.Status.CLOSED,
				new PageRequest(page, resultPerPage, direction != null ? direction : Direction.ASC, sortBy));
	}

	@ResponseBody
	@RequestMapping(value = "/{productId}/rma", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<RmaDetail> findProductRmas(@PathVariable("productId") int productId,
			@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer resultPerPage,
			@RequestParam(required = false) Direction direction,
			@RequestParam(defaultValue = "extendedLabel") String sortBy) {
		return rmaDetailService.findByProductAndRmaStatus(this.productService.findOne(productId), Rma.Status.SHIPPED,
				new PageRequest(page, resultPerPage, direction != null ? direction : Direction.ASC, sortBy));
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, params = { "random",
			"category" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public Product findRandomByCategory(@RequestParam Integer category) {
		return productService.findRandomByCategory(category != null ? categoryService.findOne(category) : null);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, params = { "random",
			"nonValidatedProductImages" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public Product findRandomWithoutValidatedImages() {
		return productService.findRandomByImagesValidated(false);
	}

	@RequestMapping(method = RequestMethod.GET, params = { "random", "category" }, produces = MediaType.TEXT_HTML_VALUE)
	public String getNonCategorizedProductsPage() {
		return "product/non-categorized-product";
	}

	@RequestMapping(params = "card", produces = MediaType.TEXT_HTML_VALUE)
	public String getProductCardPage() {
		return "product/product-card :: product-card";
	}

	@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
	public String getProductsPage() {
		return "product/products-page";
	}

	@RequestMapping(method = RequestMethod.GET, params = { "random",
			"nonValidatedProductImages" }, produces = MediaType.TEXT_HTML_VALUE)
	public String getNonValidatedProductImagesPage() {
		return "product/non-validated-product-images";
	}

	@ResponseBody
	@RequestMapping(value = "/{id}/image/{size}", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> loadProductImage(@PathVariable int id,
			@PathVariable ProductImageSize size) {
		try {
			return new ResponseEntity<InputStreamResource>(
					new InputStreamResource(productImageService.loadInputStream(productService.findOne(id), size)),
					HttpStatus.OK);
		} catch (ImageNotFoundException imageNotFoundEx) {
			return new ResponseEntity<InputStreamResource>(HttpStatus.NOT_FOUND);
		}
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{productId}", params = { "targetProductId", "mergeProduct" })
	public void mergeProducts(@PathVariable int productId, @RequestParam int targetProductId) {
		productService.mergeProduct(findOne(productId), this.findOne(targetProductId));
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{productId}/image/amazon", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void persistAmazonImages(@PathVariable int productId) {
		productImageService.persistAmazonImages(productService.findOne(productId));
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{productId}/image/dilicom", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void persistDilicomImages(@PathVariable int productId) {
		productImageService.persistDilicomImages(productService.findOne(productId));
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{productId}/image/url", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void persistIamgesFromUrl(@PathVariable int productId, @RequestParam String url) {
		productImageService.persistImageFromUrlToAws(url, productService.findOne(productId));
	}

	@ResponseBody
	@RequestMapping(method = { RequestMethod.POST, RequestMethod.PUT }, produces = MediaType.APPLICATION_JSON_VALUE)
	public Product save(@RequestBody Product product) {
		return productService.save(product);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{productId}/image/custom", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void uploadCustomProductImage(@PathVariable int productId, @RequestParam("file") MultipartFile file) {
		try {
			productImageService.uploadImageToAws(file.getBytes(), productService.findOne(productId));
		} catch (IOException ioEx) {
			throw new RuntimeException(ioEx);
		}
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, params = "search", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<Product> search(@RequestParam(defaultValue = "") String query,
			@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "50") Integer resultPerPage,
			@RequestParam(required = false) Direction direction,
			@RequestParam(defaultValue = "extendedLabel") String sortBy) {
		return productService.search(query,
				new PageRequest(page, resultPerPage, direction != null ? direction : Direction.ASC, sortBy));
	}
}
