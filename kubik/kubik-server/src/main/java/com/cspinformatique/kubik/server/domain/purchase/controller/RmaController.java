package com.cspinformatique.kubik.server.domain.purchase.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cspinformatique.kubik.server.domain.purchase.service.RmaService;
import com.cspinformatique.kubik.server.domain.warehouse.service.ProductInventoryService;
import com.cspinformatique.kubik.server.jasper.service.ReportService;
import com.cspinformatique.kubik.server.model.purchase.Rma;
import com.cspinformatique.kubik.server.model.purchase.RmaDetail;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;

@Controller
@RequestMapping("/rma")
public class RmaController {
	@Autowired
	private ReportService reportService;

	@Autowired
	private RmaService rmaService;

	@Autowired
	private ProductInventoryService productInventoryService;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Page<Rma> findAll(@RequestParam(required = false) String status,
			@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "50") Integer resultPerPage,
			@RequestParam(required = false) Direction direction,
			@RequestParam(defaultValue = "openDate") String sortBy) {
		return this.rmaService
				.findAll(new PageRequest(page, resultPerPage, direction != null ? direction : Direction.DESC, sortBy));
	}

	@RequestMapping(value = "/{id}/next", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Integer findNextRma(@PathVariable int id) {
		return this.rmaService.findNext(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Rma findOne(@PathVariable int id) {
		return this.rmaService.findOne(id);
	}

	@RequestMapping(value = "/{id}/previous", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Integer findPreviousRma(@PathVariable int id) {
		return this.rmaService.findPrevious(id);
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/{rmaId}/report", method = RequestMethod.GET, produces = "application/pdf")
	public byte[] generateRmaPdf(@PathVariable int rmaId, HttpServletResponse response) {
		try {
			return JasperExportManager
					.exportReportToPdf(this.reportService.generateRmaReport(this.rmaService.findOne(rmaId)));
		} catch (JRException ex) {
			throw new RuntimeException(ex);
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getRmaPage(@PathVariable int id, Model model) {
		model.addAttribute("id", id);

		return "purchase/rma/rma-details";
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getRmasPage() {
		return "purchase/rma/rmas";
	}

	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody Rma save(@RequestBody Rma rma) {
		return this.rmaService.save(rma);
	}

	@RequestMapping(params = "fixDetails", method = RequestMethod.GET)
	public String fixDetails() {
		for (Rma rma : this.rmaService.findAll(new PageRequest(0, 100000))) {
			for (RmaDetail detail : rma.getDetails()) {
				detail.setRma(rma);
			}

			this.save(rma);

			for (RmaDetail detail : rma.getDetails()) {
				this.productInventoryService.updateInventory(detail.getProduct());
			}

		}

		return this.getRmasPage();
	}
}
