package com.cspinformatique.kubik.server.domain.sales.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
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

import com.cspinformatique.kubik.server.domain.sales.service.CustomerCreditService;
import com.cspinformatique.kubik.server.domain.sales.service.InvoiceService;
import com.cspinformatique.kubik.server.domain.sales.service.PaymentService;
import com.cspinformatique.kubik.server.jasper.service.ReportService;
import com.cspinformatique.kubik.server.model.sales.Invoice;
import com.cspinformatique.kubik.server.model.sales.InvoiceDetail;
import com.cspinformatique.kubik.server.model.sales.Payment;
import com.cspinformatique.kubik.server.print.service.PrintService;
import com.mysema.query.types.Predicate;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;

@Controller
@RequestMapping("/invoice")
public class InvoiceController {

	@Autowired
	private CustomerCreditService customerCreditService;

	@Autowired
	private InvoiceService invoiceService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private PrintService printService;

	@Autowired
	private ReportService reportService;

	@RequestMapping(value = "/init", method = RequestMethod.GET)
	public void init() {
		invoiceService.recalculateInvoiceTaxes();
		customerCreditService.recalculateCustomerCreditsTaxes();
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/{id}", params = "pdf", method = RequestMethod.GET, produces = "application/pdf")
	public byte[] generateInvoicePdf(@PathVariable int id, HttpServletResponse response) {
		try {
			return JasperExportManager
					.exportReportToPdf(reportService.generateInvoiceReport(invoiceService.findOne(id)));
		} catch (JRException jrEx) {
			throw new RuntimeException(jrEx);
		}
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/{id}/receipt", method = RequestMethod.GET, produces = "application/pdf")
	public byte[] generateReceiptPdf(@PathVariable int id, HttpServletResponse response) {
		try {
			return JasperExportManager
					.exportReportToPdf(reportService.generateReceiptReport(invoiceService.findOne(id)));
		} catch (JRException jrEx) {
			throw new RuntimeException(jrEx);
		}
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}/receipt", method = RequestMethod.POST, params = "print")
	public void generateReceiptPrintJob(@PathVariable int id) {
		printService.createReceiptPrintJob(this.invoiceService.findOne(id));
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<Invoice> findAll(@QuerydslPredicate(root = Invoice.class) Predicate predicate, Pageable pageable) {
		return invoiceService.findAll(predicate, pageable);
	}

	@ResponseBody
	@RequestMapping(value = "/{id}/detail/product/ean13/{ean13}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public InvoiceDetail findDetailByInvoiceIdAndProductEan13(@PathVariable int id, @PathVariable String ean13) {
		return invoiceService.findDetailByInvoiceIdAndProductEan13(id, ean13);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, params = "number", produces = MediaType.APPLICATION_JSON_VALUE)
	public Invoice findByNumber(@RequestParam String number) {
		return invoiceService.findByNumber(number);
	}

	@ResponseBody
	@RequestMapping(value = "/{id}/next", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Integer findNextInvoice(@PathVariable int id) {
		return invoiceService.findNext(id);
	}

	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Invoice findOne(@PathVariable int id) {
		return invoiceService.findOne(id);
	}

	@ResponseBody
	@RequestMapping(value = "/{id}/previous", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Integer findPreviousInvoice(@PathVariable int id) {
		return invoiceService.findPrevious(id);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, params = "new", produces = MediaType.APPLICATION_JSON_VALUE)
	public Invoice generateNewOrder(@RequestParam int customerId) {
		return invoiceService.generateNewOrder(customerId);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getInvoicePage() {
		return "sales/invoice/invoice";
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getInvoicesPage() {
		return "sales/invoice/invoices";
	}

	@ResponseBody
	@RequestMapping(method = { RequestMethod.POST, RequestMethod.PUT }, produces = MediaType.APPLICATION_JSON_VALUE)
	public Invoice save(@RequestBody Invoice invoice) {
		return invoiceService.save(invoice);
	}

	@ResponseBody
	@RequestMapping(value = "/{id}/payment", method = { RequestMethod.POST,
			RequestMethod.PUT }, produces = MediaType.APPLICATION_JSON_VALUE)
	public Iterable<Payment> savePayments(Iterable<Payment> payments) {
		return paymentService.save(payments);
	}
}
