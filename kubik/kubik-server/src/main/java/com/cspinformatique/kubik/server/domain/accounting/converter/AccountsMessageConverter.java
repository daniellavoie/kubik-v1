package com.cspinformatique.kubik.server.domain.accounting.converter;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.cspinformatique.kubik.server.domain.accounting.model.Account;
import com.cspinformatique.kubik.server.domain.accounting.model.AccountsExports;

public class AccountsMessageConverter extends AbstractHttpMessageConverter<AccountsExports> {
	public static final MediaType MEDIA_TYPE = new MediaType("text", "csv", Charset.forName("utf-8"));
	private SimpleDateFormat fileDateFormat;

	public AccountsMessageConverter() {
		super(MEDIA_TYPE);

		fileDateFormat = new SimpleDateFormat("yyyy/MM/dd");
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		return AccountsExports.class.equals(clazz);
	}

	@Override
	protected AccountsExports readInternal(Class<? extends AccountsExports> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		throw new UnsupportedOperationException("Not implemented yet !");
	}

	@Override
	protected void writeInternal(AccountsExports accountsExports, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		String filename = accountsExports.getLabel() + "-" + fileDateFormat.format(new Date()) + ".csv";

		outputMessage.getHeaders().setContentType(MEDIA_TYPE);
		outputMessage.getHeaders().set("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		outputMessage.getHeaders().setAcceptCharset(Arrays.asList(new Charset[] { Charset.forName("UTF-8") }));

		PrintStream printStream = new PrintStream(outputMessage.getBody());
		String separator = accountsExports.getSeparator();

		String seperator = accountsExports.getSeparator();
		for (Account account : accountsExports.getAccounts()) {
			printStream.append(account.getAccountNumber() + separator + seperator + account.getCustomerName()
					+ seperator + seperator + seperator + "X" + "\n");
		}

		printStream.close();
	}

}
