package com.cspinformatique.kubik.kos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableScheduling
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
	
	@RequestMapping("/400")
	public String getBadRequestPage(){
		return "400";
	}

	@RequestMapping("/403")
	public String getForbiddenPage(){
		return "403";
	}

	@RequestMapping("/404")
	public String getResourceNotFoundPage(){
		return "404";
	}

	@RequestMapping("/500")
	public String getInternalErrorPage(){
		return "500";
	}
}
