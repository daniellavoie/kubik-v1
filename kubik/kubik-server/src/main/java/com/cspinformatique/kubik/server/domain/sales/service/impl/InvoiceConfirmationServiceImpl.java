package com.cspinformatique.kubik.server.domain.sales.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.mail.internet.InternetAddress;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.cspinformatique.kubik.kos.model.order.CustomerOrder;
import com.cspinformatique.kubik.server.domain.sales.repository.InvoiceConfirmationRepository;
import com.cspinformatique.kubik.server.domain.sales.service.CustomerOrderService;
import com.cspinformatique.kubik.server.domain.sales.service.InvoiceConfirmationService;
import com.cspinformatique.kubik.server.domain.sales.service.InvoiceService;
import com.cspinformatique.kubik.server.jasper.service.ReportService;
import com.cspinformatique.kubik.server.model.sales.Invoice;
import com.cspinformatique.kubik.server.model.sales.InvoiceConfirmation;
import com.cspinformatique.kubik.server.model.sales.InvoiceConfirmation.Status;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;

@Service
public class InvoiceConfirmationServiceImpl implements InvoiceConfirmationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceConfirmationServiceImpl.class);

	private InvoiceConfirmationRepository repository;
	private CustomerOrderService customerOrderService;
	private InvoiceService invoiceService;
	private ReportService reportService;
	private AmazonS3 amazonS3;
	private JavaMailSender sender;
	private String contactEmail;
	private String bucketName;

	@Autowired
	public InvoiceConfirmationServiceImpl(InvoiceConfirmationRepository repository,
			CustomerOrderService customerOrderService, InvoiceService invoiceService, ReportService reportService,
			AmazonS3 amazonS3, JavaMailSender sender, @Value("${kubik.contact.email}") String contactEmail,
			@Value("${aws.s3.bucket.name}") String bucketName) {
		this.repository = repository;
		this.customerOrderService = customerOrderService;
		this.invoiceService = invoiceService;
		this.reportService = reportService;
		this.amazonS3 = amazonS3;
		this.sender = sender;
		this.contactEmail = contactEmail;
		this.bucketName = bucketName;
	}

	@Override
	public void create(Invoice invoice) {
		// Creates an email notification task.
		repository.save(new InvoiceConfirmation(null, invoice, Status.TO_PROCESS, new Date(), null, null));
	}

	@Override
	public void processConfirmations() {
		processConfirmations(Status.TO_PROCESS);
	}

	@Override
	public void recoverConfirmations() {
		processConfirmations(Status.ERROR);
	}

	private void processConfirmations(Status status) {
		for (InvoiceConfirmation invoiceConfirmation : repository.findByStatus(status))
			process(invoiceConfirmation);
	}

	private void process(InvoiceConfirmation invoiceConfirmation) {
		LOGGER.info("Processing confirmation for invoice " + invoiceConfirmation.getInvoice().getId() + ".");

		try {
			// Persists the invoice to amazon s3.
			uploadInvoiceToAmazonS3(invoiceConfirmation.getInvoice());

			invoiceConfirmation.setError(null);

			// Send the email.
			sendEmail(customerOrderService.findOne(invoiceConfirmation.getInvoice().getCustomerOrderId()),
					invoiceConfirmation.getInvoice());

			invoiceConfirmation.setStatus(Status.PROCESSED);
		} catch (Exception ex) {
			if (LOGGER.isDebugEnabled())
				LOGGER.error("Error while processing confirmation for invoice "
						+ invoiceConfirmation.getInvoice().getId() + ".", ex);
			else
				LOGGER.error("Error while processing confirmation for invoice "
						+ invoiceConfirmation.getInvoice().getId() + ". Message : " + ex.getMessage());

			invoiceConfirmation.setError(ExceptionUtils.getStackTrace(ex));
		} finally {
			invoiceConfirmation.setProcessedDate(new Date());
			repository.save(invoiceConfirmation);
		}
	}

	private void sendEmail(CustomerOrder customerOrder, Invoice invoice) {
		sender.send(mimeMessage -> {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.addTo(new InternetAddress(customerOrder.getAccount().getUsername()));
			helper.setFrom(new InternetAddress(contactEmail));
			helper.setSubject("Votre facture pour votre commande " + customerOrder.getId() + ".");
			helper.setText("Cher " + customerOrder.getBillingAddress().getFirstName() + " "
					+ customerOrder.getBillingAddress().getLastName() + ",\n\n"
					+ "Nous vous remercions de votre commande " + customerOrder.getId() + ".\n\n"
					+ "Veuillez trouver ci-joint votre facture au format PDF. Vous pouvez la sauvegarder ou l'imprimer en cas de besoin.\n\n"
					+ "Pour toute autre question concernant votre commande, vous pouvez contacter notre service client grâce à notre adresse mail "
					+ contactEmail + ".\n\n" + "L'équipe La Dimension Fantastique");

			helper.addAttachment("facture-" + invoice.getId() + ".pdf",
					new ByteArrayResource(JasperExportManager.exportReportToPdf(
							reportService.generateInvoiceReport(invoiceService.findOne(invoice.getId().intValue())))));
		});

		LOGGER.info("Confirmation for invoice " + invoice.getId() + " has been sent.");
	}

	private void uploadInvoiceToAmazonS3(Invoice invoice) {
		try {
			byte[] invoicePdf = JasperExportManager.exportReportToPdf(
					reportService.generateInvoiceReport(invoiceService.findOne(invoice.getId().intValue())));

			FileUtils.writeByteArrayToFile(new File("target/test.pdf"), invoicePdf);

			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(invoicePdf.length);

			amazonS3.putObject(bucketName, "invoice-" + invoice.getId(), new ByteArrayInputStream(invoicePdf),
					metadata);
		} catch (JRException | IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}
