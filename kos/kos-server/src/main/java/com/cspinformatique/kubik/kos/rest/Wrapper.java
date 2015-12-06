package com.cspinformatique.kubik.kos.rest;

import java.util.List;

public abstract class Wrapper {
	private List<String> errors;
	
	public Wrapper(){
		
	}
	
	public Wrapper(List<String> errors){
		this.errors = errors;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
}
