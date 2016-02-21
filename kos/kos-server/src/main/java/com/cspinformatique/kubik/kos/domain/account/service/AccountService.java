package com.cspinformatique.kubik.kos.domain.account.service;

import java.security.Principal;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.cspinformatique.kubik.kos.model.account.Account;
import com.cspinformatique.kubik.kos.model.account.Address;

public interface AccountService extends UserDetailsService {

	Account createAccount(String username, CharSequence password);

	Account findByUsername(String username);

	Account findByPrincipal(Principal principal);

	Account findOne(long id);

	Account save(Account account);

	Address saveAddress(Account account, Address address);
}
