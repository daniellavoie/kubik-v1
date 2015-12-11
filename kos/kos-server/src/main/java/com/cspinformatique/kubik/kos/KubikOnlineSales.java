package com.cspinformatique.kubik.kos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@SpringBootApplication
public class KubikOnlineSales {
	@Value("${kos.contact.email}")
	private String contactUsEmail;

	public static void main(String[] args) {
		SpringApplication.run(KubikOnlineSales.class, args);
	}

	@RequestMapping({ "/", "/la-librairie" })
	public String getMainPage() {
		return "store";
	}

	@ResponseBody
	@RequestMapping(value = "/contact-us", produces = MediaType.TEXT_PLAIN_VALUE)
	public String getContactUsEmail() {
		return contactUsEmail;
	}
}
