package com.cspinformatique.kubik.server.jasper.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.springframework.web.servlet.view.AbstractView;

public class PdfView extends AbstractView {

	public PdfView() {
		setContentType("application/pdf");
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JasperExportManager.exportReportToPdfStream(
				(JasperPrint) model.get("report"), response.getOutputStream());
	}

}
