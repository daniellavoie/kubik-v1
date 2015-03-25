package com.cspinformatique.kubik.domain.purchase.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.cspinformatique.kubik.domain.purchase.model.ReceptionDetail;

public interface ReceptionDetailRepository extends
		PagingAndSortingRepository<ReceptionDetail, Integer> {

}
