package com.cspinformatique.kubik.domain.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.model.product.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
