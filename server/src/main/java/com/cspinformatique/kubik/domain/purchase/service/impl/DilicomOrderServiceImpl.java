package com.cspinformatique.kubik.domain.purchase.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.domain.purchase.model.PurchaseOrder;
import com.cspinformatique.kubik.domain.purchase.model.PurchaseOrderDetail;
import com.cspinformatique.kubik.domain.purchase.service.DilicomOrderService;

@Service
public class DilicomOrderServiceImpl implements DilicomOrderService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DilicomOrderServiceImpl.class);
	
	@Resource
	private Environment env;
	
	@Autowired 
	private SessionFactory<FTPFile> sessionFactory;

	@Value("${kubik.dilicom.ftp.username}")
	private String ftpUsername;

	@Value("${kubik.dilicom.ftp.url}")
	private String ftpUrl;

	@Value("${kubik.dilicom.ftp.password}")
	private String ftpPassword;

	@Value("${kubik.dilicom.ftp.in.path}")
	private String remoteDirectory;

	private DateFormat dateFormat;
	private DateFormat fileDateFormat;
	private DecimalFormat lineNumberFormat;
	private DecimalFormat quantityNumberFormat;
	private String ean13;

	public DilicomOrderServiceImpl() {
		this.dateFormat = new SimpleDateFormat("yyyyMMdd");
		this.fileDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		this.lineNumberFormat = new DecimalFormat("0000");
		this.quantityNumberFormat = new DecimalFormat("000000");
	}
	
	@Override
	public void confirmDilicomOrder(String orderFileName){
		int dotIndex = orderFileName.lastIndexOf(".");
		
		FTPClient client = new FTPClient();
		try{
			client.connect(ftpUrl);
			
			client.login(ftpUsername, ftpPassword);
			
			client.rename(orderFileName, orderFileName.substring(0, dotIndex) + orderFileName.substring(dotIndex + 1));
			client.logout();
		} catch (IOException ioEx) {
			LOGGER.error("Error while renaming order.", ioEx);
		} finally {
			try {
				client.disconnect();
			} catch (IOException ioEx) {
				LOGGER.error("Error while closing FTP connection.", ioEx);
			}
		}
	}

	@Override
	public String sendOrderToDilicom(PurchaseOrder purchaseOrder) {
		LOGGER.info("Sending purcharse order " + purchaseOrder.getId()
				+ " to Dilicom.");

		if (ean13 == null) {
			this.ean13 = this.env.getRequiredProperty("kubik.ean13");
		}

		String orderFileName = "." + this.fileDateFormat.format(new Date());

		try (FileWriter writer = new FileWriter(orderFileName)) {
			int lineNumber = 0;

			// First line.
			++lineNumber;
			writer.write("A" + lineNumberFormat.format(lineNumber)
					+ (purchaseOrder.getSupplier().getPurchaseOrderEan13() == null ? purchaseOrder
					.getSupplier().getEan13() : purchaseOrder.getSupplier()
					.getPurchaseOrderEan13())
					+ (purchaseOrder.getOperationCode() != null ? purchaseOrder
							.getOperationCode() : "") + "\n");

			// Second line.
			++lineNumber;
			writer.write("B" + lineNumberFormat.format(lineNumber) + this.ean13
					+ this.ean13 + "\n");

			// Third line.
			++lineNumber;
			writer.write("C" + lineNumberFormat.format(lineNumber)
					+ purchaseOrder.getId() + "\n");

			// Fourth line.
			++lineNumber;
			writer.write("D" + lineNumberFormat.format(lineNumber)
					+ dateFormat.format(new Date()) + "\n");

			// Fifth line.
			++lineNumber;
			writer.write("E" + lineNumberFormat.format(lineNumber)
					+ purchaseOrder.getShippingMode().getCode() + "0   "
					+ dateFormat.format(purchaseOrder.getMinDeliveryDate())
					+ dateFormat.format(purchaseOrder.getMaxDeliveryDate())
					+ "\n");

			for (PurchaseOrderDetail detail : purchaseOrder.getDetails()) {
				++lineNumber;
				writer.write("L" + lineNumberFormat.format(lineNumber)
						+ detail.getProduct().getEan13()
						+ quantityNumberFormat.format(detail.getQuantity())
						+ "\n");
			}

			// Last line.
			++lineNumber;
			writer.write("Q" + lineNumberFormat.format(lineNumber));
			
			writer.flush();
			
			String remoteFileName = remoteDirectory + "/" + orderFileName;
			
			FTPClient client = new FTPClient();
			FileInputStream fileInputStream = null;
			try{
				client.connect(ftpUrl);
				
				client.login(ftpUsername, ftpPassword);
				fileInputStream = new FileInputStream(orderFileName);
				client.storeFile(remoteFileName, fileInputStream);
				
				client.logout();
			} catch (IOException ioEx) {
				LOGGER.error("Error while renaming order.", ioEx);
			} finally {
				IOUtils.closeQuietly(fileInputStream);
				try {
					client.disconnect();
				} catch (IOException ioEx) {
					LOGGER.error("Error while closing FTP connection.", ioEx);
				}
			}
			
			new File(orderFileName).delete();
			
			return remoteFileName;
		} catch (IOException ioEx) {
			throw new RuntimeException(ioEx);
		}
	}
}
