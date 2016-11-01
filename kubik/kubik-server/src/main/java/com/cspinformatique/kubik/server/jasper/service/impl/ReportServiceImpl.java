package com.cspinformatique.kubik.server.jasper.service.impl;

import java.awt.Image;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.domain.company.service.CompanyService;
import com.cspinformatique.kubik.server.domain.system.service.LogoService;
import com.cspinformatique.kubik.server.jasper.service.ReportService;
import com.cspinformatique.kubik.server.model.company.Company;
import com.cspinformatique.kubik.server.model.purchase.PurchaseOrder;
import com.cspinformatique.kubik.server.model.purchase.Rma;
import com.cspinformatique.kubik.server.model.sales.Invoice;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class ReportServiceImpl implements ReportService {
	private CompanyService companyService;
	private DataSource dataSource;
	private LogoService logoService;

	@Autowired
	public ReportServiceImpl(CompanyService companyService, DataSource dataSource, LogoService logoService) {
		this.companyService = companyService;
		this.dataSource = dataSource;
		this.logoService = logoService;
	}

	private Image loadLogo() {
		try {
			return ImageIO.read(logoService.findLogo());
		} catch (IOException ioEx) {
			throw new RuntimeException(ioEx);
		}
	}

	@Override
	public JasperPrint generateInvoiceReport(Invoice invoice) {
		try(Connection connection = dataSource.getConnection()) {
			HashMap<String, Object> reportParameters = new HashMap<String, Object>();

			Company company = companyService.findComapny().get();

			reportParameters.put("INVOICE_ID", invoice.getId());
			reportParameters.put("LOGO", loadLogo());
			reportParameters.put("COMPANY_NAME", company.getName());
			reportParameters.put("SIRET", company.getSiret());
			reportParameters.put("PHONE", company.getPhone());
			reportParameters.put("ADDRESS_NUMBER", company.getAddressNumber());
			reportParameters.put("ADDRESS_STREET", company.getAddressStreet());
			reportParameters.put("ADDRESS_CITY", company.getAddressCity());
			reportParameters.put("ADDRESS_ZIP_CODE", company.getAddressZipCode());
			reportParameters.put("LEGAL_MENTION_1", company.getLegalMention1());
			reportParameters.put("LEGAL_MENTION_2", company.getLegalMention2());
			reportParameters.put("WEBSITE", company.getWebsite());
			reportParameters.put("EMAIL", company.getEmail());
			reportParameters.put("NOTE", invoice.getNote());

			return JasperFillManager.fillReport(
					new ClassPathResource("reports/invoice/invoice.jasper").getInputStream(), reportParameters,
					connection);
		} catch (JRException | SQLException | IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public JasperPrint generatePurchaseOrderReport(PurchaseOrder purchaseOrder) {
		try(Connection connection = dataSource.getConnection()) {
			Company company = companyService.findComapny().get();

			HashMap<String, Object> reportParameters = new HashMap<String, Object>();

			reportParameters.put("PURCHASE_ORDER_ID", purchaseOrder.getId());
			reportParameters.put("SUPPLIER_NAME", purchaseOrder.getSupplier().getName());
			reportParameters.put("SUPPLIER_ADDRESS", purchaseOrder.getSupplier().getAddress());
			reportParameters.put("SUPPLIER_ACCOUNT_NUMBER", purchaseOrder.getSupplier().getAccountNumber());

			reportParameters.put("LOGO", loadLogo());
			reportParameters.put("COMPANY_NAME", company.getName());
			reportParameters.put("SIRET", company.getSiret());
			reportParameters.put("ADDRESS_NUMBER", company.getAddressNumber());
			reportParameters.put("ADDRESS_STREET", company.getAddressStreet());
			reportParameters.put("ADDRESS_CITY", company.getAddressCity());
			reportParameters.put("ADDRESS_ZIP_CODE", company.getAddressZipCode());
			reportParameters.put("LEGAL_MENTION_1", company.getLegalMention1());
			reportParameters.put("LEGAL_MENTION_2", company.getLegalMention2());
			reportParameters.put("WEBSITE", company.getWebsite());
			reportParameters.put("EMAIL", company.getEmail());

			return JasperFillManager.fillReport(
					new ClassPathResource("reports/order/order-form.jasper").getInputStream(), reportParameters,
					connection);
		} catch (JRException | SQLException | IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public JasperPrint generateReceiptReport(Invoice invoice) {
		try(Connection connection = dataSource.getConnection()) {
			Company company = companyService.findComapny().get();

			HashMap<String, Object> reportParameters = new HashMap<String, Object>();

			reportParameters.put("INVOICE_ID", invoice.getId());
			reportParameters.put("LOGO", loadLogo());

			reportParameters.put("COMPANY_NAME", company.getName());
			reportParameters.put("EMAIL", company.getEmail());
			reportParameters.put("PHONE", company.getPhone());

			return JasperFillManager.fillReport(
					new ClassPathResource("reports/sale-receipt/sale-receipt.jasper").getInputStream(),
					reportParameters, connection);
		} catch (JRException | SQLException | IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public JasperPrint generateRmaReport(Rma rma) {
		try(Connection connection = dataSource.getConnection()) {
			HashMap<String, Object> reportParameters = new HashMap<String, Object>();

			Company company = companyService.findComapny().get();

			reportParameters.put("RMA_ID", rma.getId());
			reportParameters.put("SUPPLIER_NAME", rma.getSupplier().getName());
			reportParameters.put("SUPPLIER_ADDRESS", rma.getSupplier().getAddress());
			reportParameters.put("SUPPLIER_ACCOUNT_NUMBER", rma.getSupplier().getAccountNumber());
			reportParameters.put("LOGO", loadLogo());

			reportParameters.put("COMPANY_NAME", company.getName());
			reportParameters.put("ADDRESS_NUMBER", company.getAddressNumber());
			reportParameters.put("ADDRESS_STREET", company.getAddressStreet());
			reportParameters.put("ADDRESS_ZIP_CODE", company.getAddressZipCode());
			reportParameters.put("ADDRESS_CITY", company.getAddressCity());

			return JasperFillManager.fillReport(new ClassPathResource("reports/rma/rma.jasper").getInputStream(),
					reportParameters, connection);
		} catch (JRException | SQLException | IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}
