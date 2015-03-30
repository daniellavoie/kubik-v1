package com.cspinformatique.kubik.print.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cspinformatique.kubik.model.print.ReceiptPrintJob;
import com.cspinformatique.kubik.print.service.PrintService;

@Controller
@RequestMapping("/print")
public class PrintController {
	@Autowired private PrintService printService;
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value= "/{receiptPrintJobId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int receiptPrintJobId){
		this.printService.deleteReceiptPrintJob(receiptPrintJobId);
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public @ResponseBody List<ReceiptPrintJob> findPendingJobs(){
		return this.printService.findPendingReceiptPrintJobs();
	}
}
