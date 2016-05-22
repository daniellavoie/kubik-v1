package com.cspinformatique.kubik.server.domain.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cspinformatique.kubik.server.domain.purchase.service.DiscountTypeService;
import com.cspinformatique.kubik.server.domain.sales.service.InvoiceStatusService;
import com.cspinformatique.kubik.server.domain.sales.service.PaymentMethodService;
import com.cspinformatique.kubik.server.model.purchase.DiscountType;
import com.cspinformatique.kubik.server.model.sales.InvoiceStatus;
import com.cspinformatique.kubik.server.model.sales.PaymentMethod;

@Controller
@RequestMapping("/initialization")
public class InitializationController {
	private DiscountTypeService discountTypeService;
	private InvoiceStatusService invoiceStatusService;
	private PaymentMethodService paymentMethodService;

	@Autowired
	public InitializationController(DiscountTypeService discountTypeService, InvoiceStatusService invoiceStatusService,
			PaymentMethodService paymentMethodService) {
		this.discountTypeService = discountTypeService;
		this.invoiceStatusService = invoiceStatusService;
		this.paymentMethodService = paymentMethodService;
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getInitializationPage() {
		return "initialization";
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void initialize() {		
		// creates the new invoice status.
		invoiceStatusService.save(new InvoiceStatus(InvoiceStatus.Types.CANCELED.name(), "Annulé"));
		invoiceStatusService.save(new InvoiceStatus(InvoiceStatus.Types.DRAFT.name(), "Brouillon"));
		invoiceStatusService.save(new InvoiceStatus(InvoiceStatus.Types.ORDER.name(), "Commande"));
		invoiceStatusService.save(new InvoiceStatus(InvoiceStatus.Types.ORDER_CONFIRMED.name(), "Commande confirmée"));
		invoiceStatusService.save(new InvoiceStatus(InvoiceStatus.Types.PAID.name(), "Payé"));
		invoiceStatusService.save(new InvoiceStatus(InvoiceStatus.Types.REFUND.name(), "Remboursé"));
		
		// Creates the paiement methods.
		paymentMethodService.save(new PaymentMethod("CARD", "Carte", "58020000", true));
		paymentMethodService.save(new PaymentMethod("CASH", "Espèce", "53000000", true));
		paymentMethodService.save(new PaymentMethod("CHECK", "Chèque", "58010000", true));
		paymentMethodService.save(new PaymentMethod("CREDIT", "Avoir", null, false));
		paymentMethodService.save(new PaymentMethod("KADEOS", "Kadéos", "58030000", true));
		paymentMethodService.save(new PaymentMethod("LIRE", "Chèque Lire", "58030000", true));
		paymentMethodService.save(new PaymentMethod("WIRE", "Virement", "58040000", true));
		
		// Creates the discount types.
		discountTypeService.save(new DiscountType(DiscountType.Types.ORDER.name(), "Commande"));
		discountTypeService.save(new DiscountType(DiscountType.Types.ORDER_DETAIL.name(), "Produit commande"));
		discountTypeService.save(new DiscountType(DiscountType.Types.PRODUCT.name(), "Produit"));
		discountTypeService.save(new DiscountType(DiscountType.Types.SUPPLIER.name(), "Fournisseur"));
	}
}