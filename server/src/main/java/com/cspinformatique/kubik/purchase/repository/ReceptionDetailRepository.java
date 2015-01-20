package com.cspinformatique.kubik.purchase.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.purchase.model.ReceptionDetail;

public interface ReceptionDetailRepository extends
		PagingAndSortingRepository<ReceptionDetail, Integer> {

}
