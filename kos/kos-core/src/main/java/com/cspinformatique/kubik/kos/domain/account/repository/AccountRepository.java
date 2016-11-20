package com.cspinformatique.kubik.kos.domain.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.kos.model.account.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{
	Account findByUsername(String username);
}
