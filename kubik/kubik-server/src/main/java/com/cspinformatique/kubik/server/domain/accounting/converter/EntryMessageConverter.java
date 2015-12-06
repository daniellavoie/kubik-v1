package com.cspinformatique.kubik.server.domain.accounting.converter;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.cspinformatique.kubik.server.domain.accounting.model.EntriesExport;
import com.cspinformatique.kubik.server.domain.accounting.model.Entry;

public class EntryMessageConverter extends
		AbstractHttpMessageConverter<EntriesExport> {
	public static final MediaType MEDIA_TYPE = new MediaType("text", "csv",
			Charset.forName("utf-8"));

	private SimpleDateFormat exportDateFormat;
	private SimpleDateFormat fileDateFormat;

	public EntryMessageConverter() {
		super(MEDIA_TYPE);

		exportDateFormat = new SimpleDateFormat("d/M/yyyy");
		fileDateFormat = new SimpleDateFormat("yyyy/MM/dd");
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		return EntriesExport.class.equals(clazz);
	}

	@Override
	protected EntriesExport readInternal(Class<? extends EntriesExport> clazz,
			HttpInputMessage inputMessage) throws IOException,
			HttpMessageNotReadableException {
		throw new UnsupportedOperationException("Not implemented yet !");
	}

	@Override
	protected void writeInternal(EntriesExport entriesExport,
			HttpOutputMessage outputMessage) throws IOException,
			HttpMessageNotWritableException {
		String filename = entriesExport.getLabel() + "-"
				+ fileDateFormat.format(entriesExport.getStartDate()) + "-"
				+ fileDateFormat.format(entriesExport.getEndDate()) + ".csv";

		outputMessage.getHeaders().setContentType(MEDIA_TYPE);
		outputMessage.getHeaders().set("Content-Disposition",
				"attachment; filename=\"" + filename + "\"");
		outputMessage.getHeaders().setAcceptCharset(
				Arrays.asList(new Charset[] { Charset.forName("UTF-8") }));

		PrintStream printStream = new PrintStream(outputMessage.getBody());
		String separator = entriesExport.getSeparator();

		for (Entry entry : entriesExport.getEntries()) {
			printStream.append(exportDateFormat.format(entry.getDate())
					+ separator
					+ entry.getJournalCode()
					+ separator
					+ entry.getAccount()
					+ separator
					+ entry.getInvoiceNumber()
					+ separator
					+ entry.getDescription()
					+ separator
					+ entriesExport.getDecimalFormat().format(entry.getDebit())
					+ separator
					+ entriesExport.getDecimalFormat().format(entry.getCredit())
					+ separator
					+ entry.getCurrency() + "\n");
		}

		printStream.close();
	}
}
