package com.cspinformatique.kubik.server.domain.warehouse.converter;

import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.apache.commons.math3.util.Precision;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.cspinformatique.kubik.server.domain.warehouse.model.InventoryExtract;
import com.cspinformatique.kubik.server.domain.warehouse.model.InventoryExtractLine;

public class InventoryExtractConverter extends AbstractHttpMessageConverter<InventoryExtract> {
	public static final MediaType MEDIA_TYPE = new MediaType("text", "csv", Charset.forName("utf-8"));

	private SimpleDateFormat fileDateFormat;

	public InventoryExtractConverter() {
		super(MEDIA_TYPE);

		fileDateFormat = new SimpleDateFormat("yyyy/MM/dd");
	}

	@Override
	protected InventoryExtract readInternal(Class<? extends InventoryExtract> arg0, HttpInputMessage arg1)
			throws IOException, HttpMessageNotReadableException {
		throw new UnsupportedOperationException("Not implemented yet !");
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		return InventoryExtract.class.equals(clazz);
	}

	@Override
	protected void writeInternal(InventoryExtract inventoryExtract, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		String filename = "EXTRACT-INVENTAIRE-" + fileDateFormat.format(inventoryExtract.getDate()) + ".csv";

		outputMessage.getHeaders().setContentType(MEDIA_TYPE);
		outputMessage.getHeaders().set("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		outputMessage.getHeaders().setAcceptCharset(Arrays.asList(new Charset[] { Charset.forName("UTF-8") }));

		PrintStream printStream = new PrintStream(outputMessage.getBody());
		String separator = inventoryExtract.getSeparator();

		printStream.append("EAN13" + separator + "NOM ARTICLE" + separator + "QUANTITE" + separator + "PRIX ACHAT"
				+ separator + "VALEUR ACHAT" + separator + "PRIX VENTE HT" + separator + "VALEUR VENTE HT\n");

		DecimalFormat decimalFormat = inventoryExtract.getDecimalFormat();
		DecimalFormat quantityDecimalFormat = new DecimalFormat("0");

		for (InventoryExtractLine line : inventoryExtract.getInventoryExtractLines()) {
			printStream.append(line.getEan13() + separator + "\"" + line.getLabel() + "\"" + separator
					+ quantityDecimalFormat.format(line.getQuantity()) + separator
					+ printBigDecimal(line.getPurchasePrice(), decimalFormat) + separator
					+ printBigDecimal(line.getPurchaseValue(), decimalFormat) + separator
					+ printBigDecimal(line.getTaxLessPrice(), decimalFormat) + separator
					+ printBigDecimal(line.getTaxLessValue(), decimalFormat) + "\n");
		}

		printStream.close();
	}

	private String printBigDecimal(BigDecimal bigDecimal, DecimalFormat decimalFormat) {
		return decimalFormat.format(Precision.round(bigDecimal.doubleValue(), 2));
	}

}
