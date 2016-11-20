package com.cspinformatique.kubik.server.domain.sales.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cspinformatique.kubik.server.domain.sales.service.InvoiceConfirmationService;

@Component
public class InvoiceConfirmationTask {
	private InvoiceConfirmationService invoiceConfirmationService;

	public InvoiceConfirmationTask(InvoiceConfirmationService invoiceConfirmationService) {
		this.invoiceConfirmationService = invoiceConfirmationService;
	}

	@Scheduled(fixedDelay = 15 * 1000)
	public void processConfirmations() {
		invoiceConfirmationService.processConfirmations();
	}

	@Scheduled(fixedDelay = 15 * 1000 * 60)
	public void recover() {
		invoiceConfirmationService.recoverConfirmations();
	}
}
