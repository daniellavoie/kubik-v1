package com.cspinformatique.kubik.reference.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.reference.model.Reference;
import com.cspinformatique.kubik.reference.service.ReferenceService;

@Controller
@RequestMapping("/reference")
public class ReferenceController {
	@Autowired
	private ReferenceService referenceService;

	@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
	public String getSearchPage(){
		return "reference/search :: search";
	}
	
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Page<Reference> search(
			@RequestParam(required = false) String query,
			@RequestParam(required = false) String[] fields,
			@RequestParam(required = false) Boolean importedInKubik,
			@RequestParam(required = false, defaultValue = "0") Integer page,
			@RequestParam(required = false, defaultValue = "50") Integer resultPerPage,
			@RequestParam(required = false, defaultValue = "extendedLabel") String sortBy,
			@RequestParam(required = false, defaultValue = "asc") String direction) {

		if (fields == null || fields.length == 0) {
			fields = new String[] { "ean13", "author", "extendedLabel",
					"publisher" };
		}

		return referenceService.search(
				query,
				fields,
				importedInKubik,
				new PageRequest(page, resultPerPage, Direction
						.valueOf(direction), sortBy));
	}
}
