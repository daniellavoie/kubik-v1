package com.cspinformatique.kubik.purchase.service.impl;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.purchase.model.PurchaseOrder;
import com.cspinformatique.kubik.purchase.model.PurchaseOrderDetail;
import com.cspinformatique.kubik.purchase.service.DilicomOrderService;

@Service
public class DilicomOrderServiceImpl implements DilicomOrderService{
	private static final Logger logger = LoggerFactory.getLogger(DilicomOrderServiceImpl.class);
	
	@Resource private Environment env;
	
	private DateFormat dateFormat;
	private DateFormat fileDateFormat;
	private DecimalFormat lineNumberFormat;
	private DecimalFormat quantityNumberFormat;
	private String ean13;
	
	public DilicomOrderServiceImpl() {
		this.dateFormat = new SimpleDateFormat("yyyyMMdd");
		this.fileDateFormat = new SimpleDateFormat("yyyyMMddHH_mmss");
		this.lineNumberFormat = new DecimalFormat("0000");
		this.quantityNumberFormat = new DecimalFormat("000000");
	}
	
	@Override
	public String sendOrderToDilicom(PurchaseOrder purchaseOrder){
		logger.info("Sending purcharse order " + purchaseOrder.getId() + " to Dilicom.");
		
		if(ean13 == null){
			this.ean13 = this.env.getRequiredProperty("kubik.ean13");
		}

		String orderFileName = "." + this.fileDateFormat.format(new Date()) + ".edi";
		
		// TODO - ChangeToAFTPStream.
		try(FileWriter writer = new FileWriter(orderFileName)){
			int lineNumber = 0;
			
			// First line.
			++lineNumber;
			writer.write("A" + lineNumberFormat.format(lineNumber) + purchaseOrder.getSupplier().getEan13() + (purchaseOrder.getOperationCode() != null ? purchaseOrder.getOperationCode() : "") + "\n");
			
			// Second line.
			++lineNumber;
			writer.write("B" + lineNumberFormat.format(lineNumber) + this.ean13 + this.ean13 + "\n");

			// Third line.
			++lineNumber;
			writer.write("C" + lineNumberFormat.format(lineNumber) + purchaseOrder.getId() + "\n");
			
			// Fourth line.
			++lineNumber;
			writer.write("D" + lineNumberFormat.format(lineNumber) + dateFormat.format(new Date()) + "\n");
			
			// Fifth line.
			++lineNumber;
			writer.write("E" + lineNumberFormat.format(lineNumber) + purchaseOrder.getShippingMode().getCode() + "0   " + dateFormat.format(purchaseOrder.getMinDeliveryDate()) + dateFormat.format(purchaseOrder.getMaxDeliveryDate()) + "\n");

			for(PurchaseOrderDetail detail : purchaseOrder.getDetails()){
				++lineNumber;
				writer.write("L" + lineNumberFormat.format(lineNumber) + detail.getProduct().getEan13() + quantityNumberFormat.format(detail.getQuantity()) + "\n");
			}
			
			// Last line.
			++lineNumber;
			writer.write("Q" + lineNumberFormat.format(lineNumber));
			
			return orderFileName;
		}catch(IOException ioEx){
			throw new RuntimeException(ioEx);
		}
	}
}
