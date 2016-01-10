package com.cspinformatique.kubik.common.rest;

import java.util.Iterator;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class RestPage<T> implements Page<T> {
	private List<T> content;
	private int totalPages;
	private long totalElements;
	private int size;
	private int number;
	private boolean first;
	private boolean last;
	private int numberOfElements;

	public RestPage() {

	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public int getNumberOfElements() {
		return numberOfElements;
	}

	public void setNumberOfElements(int numberOfElements) {
		this.numberOfElements = numberOfElements;
	}
	
	@Override
	public Sort getSort() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean hasContent() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean hasPrevious() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public Iterator<T> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public <S> Page<S> map(Converter<? super T, ? extends S> converter) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Pageable nextPageable() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Pageable previousPageable() {
		// TODO Auto-generated method stub
		return null;
	}
}
