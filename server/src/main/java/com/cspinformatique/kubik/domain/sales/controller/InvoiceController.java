package com.cspinformatique.kubik.domain.sales.controller;

import java.io.IOException;

import javax.servlet.ServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;

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

import com.cspinformatique.kubik.domain.sales.service.InvoiceService;
import com.cspinformatique.kubik.domain.sales.service.InvoiceStatusService;
import com.cspinformatique.kubik.domain.sales.service.PaymentService;
import com.cspinformatique.kubik.jasper.service.ReportService;
import com.cspinformatique.kubik.model.sales.Invoice;
import com.cspinformatique.kubik.model.sales.InvoiceDetail;
import com.cspinformatique.kubik.model.sales.Payment;
import com.cspinformatique.kubik.print.service.PrintService;

@Controller
@RequestMapping("/invoice")
public class InvoiceController {
	@Autowired
	private InvoiceService invoiceService;

	@Autowired
	private InvoiceStatusService invoiceStatusService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private PrintService printService;

	@Autowired
	private ReportService reportService;

	@RequestMapping(value = "/init", method = RequestMethod.GET)
	public void init() {
		this.invoiceService.initializeInvoiceNumbers();
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{invoiceId}/receipt", method = RequestMethod.GET, produces = "application/pdf")
	public void generateReceiptPdf(@PathVariable int invoiceId,
			ServletResponse response) {
		try {
			JasperExportManager.exportReportToPdfStream(this.reportService
					.generateReceiptReport(invoiceService.findOne(invoiceId)),
					response.getOutputStream());
		} catch (JRException | IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{invoiceId}/receipt", method = RequestMethod.POST, params = "print")
	public void generateReceiptPrintJob(@PathVariable int invoiceId) {
		this.printService.createReceiptPrintJob(this.invoiceService
				.findOne(invoiceId));
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Page<Invoice> find(
			@RequestParam(required = false) String status,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "50") Integer resultPerPage,
			@RequestParam(required = false) Direction direction,
			@RequestParam(defaultValue = "paidDate") String sortBy) {
		PageRequest pageRequest = new PageRequest(page, resultPerPage,
				direction != null ? direction : Direction.DESC, sortBy);

		if (status != null) {
			return this.invoiceService.findByStatus(
					invoiceStatusService.findOne(status), pageRequest);
		} else {
			return this.invoiceService.findAll(pageRequest);
		}
	}

	@RequestMapping(value = "/{id}/detail/product/ean13/{ean13}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody InvoiceDetail findDetailByInvoiceIdAndProductEan13(
			@PathVariable int id, @PathVariable String ean13) {
		return this.invoiceService.findDetailByInvoiceIdAndProductEan13(id,
				ean13);
	}

	@RequestMapping(method = RequestMethod.GET, params = "number", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Invoice findByNumber(@RequestParam String number) {
		return this.invoiceService.findByNumber(number);
	}

	@RequestMapping(value = "/{invoiceId}/next", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Integer findNextInvoice(@PathVariable int invoiceId) {
		return this.invoiceService.findNext(invoiceId);
	}

	@RequestMapping(value = "/{invoiceId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Invoice findOne(@PathVariable int invoiceId) {
		return this.invoiceService.findOne(invoiceId);
	}

	@RequestMapping(value = "/{invoiceId}/previous", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Integer findPreviousInvoice(@PathVariable int invoiceId) {
		return this.invoiceService.findPrevious(invoiceId);
	}

	@RequestMapping(value = "/{invoiceId}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getInvoiceDetailsPage() {
		return "sales/invoice/invoice-details";
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getInvoicesPage() {
		return "sales/invoice/invoices-page";
	}

	@RequestMapping(method = { RequestMethod.POST, RequestMethod.PUT }, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Invoice save(@RequestBody Invoice invoice) {
		return this.invoiceService.save(invoice);
	}

	@RequestMapping(value = "/{invoiceId}/payment", method = {
			RequestMethod.POST, RequestMethod.PUT }, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Iterable<Payment> savePayments(
			Iterable<Payment> payments) {
		return this.paymentService.save(payments);
	}
}
