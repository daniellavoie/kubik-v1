package com.cspinformatique.kubik.server.domain.dilicom.converter;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cspinformatique.kubik.server.model.purchase.PurchaseOrder;
import com.cspinformatique.kubik.server.model.purchase.PurchaseOrderDetail;

@Component
public class DilicomOrderStringConverter {
	
	@Value("${kubik.ean13}")
	private String ean13;

	private DateFormat dateFormat;
	private DecimalFormat lineNumberFormat;
	private DecimalFormat quantityNumberFormat;
	
	public DilicomOrderStringConverter(){
		this.dateFormat = new SimpleDateFormat("yyyyMMdd");
		this.lineNumberFormat = new DecimalFormat("0000");
		this.quantityNumberFormat = new DecimalFormat("000000");
	}
	
	public String convertToString(PurchaseOrder purchaseOrder){
		StringBuffer stringBuffer = new StringBuffer();
		
		int lineNumber = 0;

		// First line.
		++lineNumber;
		stringBuffer.append("A" + lineNumberFormat.format(lineNumber)
				+ (purchaseOrder.getSupplier().getPurchaseOrderEan13() == null ? purchaseOrder
				.getSupplier().getEan13() : purchaseOrder.getSupplier()
				.getPurchaseOrderEan13())
				+ (purchaseOrder.getOperationCode() != null ? purchaseOrder
						.getOperationCode() : "") + "\n");

		// Second line.
		++lineNumber;
		stringBuffer.append("B" + lineNumberFormat.format(lineNumber) + this.ean13
				+ this.ean13 + "\n");

		// Third line.
		++lineNumber;
		stringBuffer.append("C" + lineNumberFormat.format(lineNumber)
				+ purchaseOrder.getId() + "\n");

		// Fourth line.
		++lineNumber;
		stringBuffer.append("D" + lineNumberFormat.format(lineNumber)
				+ dateFormat.format(new Date()) + "\n");

		// Fifth line.
		++lineNumber;
		stringBuffer.append("E" + lineNumberFormat.format(lineNumber)
				+ purchaseOrder.getShippingMode().getCode() + "0    "
				+ dateFormat.format(purchaseOrder.getMinDeliveryDate())
				+ dateFormat.format(purchaseOrder.getMaxDeliveryDate())
				+ "\n");

		for (PurchaseOrderDetail detail : purchaseOrder.getDetails()) {
			++lineNumber;
			stringBuffer.append("L" + lineNumberFormat.format(lineNumber)
					+ detail.getProduct().getEan13()
					+ quantityNumberFormat.format(detail.getQuantity())
					+ "\n");
		}

		// Last line.
		++lineNumber;
		stringBuffer.append("Q" + lineNumberFormat.format(lineNumber));
		
		return stringBuffer.toString();
	}
}
