package com.cspinformatique.kubik.kos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@SpringBootApplication
public class KubikOnlineSales {

	public static void main(String[] args) {
		SpringApplication.run(KubikOnlineSales.class, args);
	}

	@RequestMapping({"/", "/la-librairie"})
	public String getMainPage() {
		return "store";
	}
}
