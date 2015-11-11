package com.cspinformatique.kubik.onlinesales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@SpringBootApplication
public class KubikOnlineSales {

	public static void main(String[] args) {
		SpringApplication.run(KubikOnlineSales.class, args);
	}

	@RequestMapping
	public String getMainPage() {
		return "index";
	}
}
