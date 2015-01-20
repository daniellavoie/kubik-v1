package com.cspinformatique.kubik.jasper.service.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.jasper.service.ReportService;
import com.cspinformatique.kubik.sales.model.Invoice;

@Service
public class ReportServiceImpl implements ReportService {
	@Autowired
	private DataSource dataSource;

	@Override
	public JasperPrint generateReceiptReport(Invoice invoice) {
		try {
			HashMap<String, Object> reportParameters = new HashMap<String, Object>();

			reportParameters.put("INVOICE_ID", invoice.getId());

			return JasperFillManager.fillReport(new ClassPathResource(
					"reports/sale-receipt.jasper").getInputStream(),
					reportParameters, dataSource.getConnection());
		} catch (JRException | SQLException | IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public JasperPrint generateInvoiceReport(Invoice invoice) {
		return null;
	}

}
