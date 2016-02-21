package com.cspinformatique.kubik.server.domain.misc.service;

import com.cspinformatique.kubik.server.model.misc.Address;

public interface AddressService {
	public Address transcodeFromKos(com.cspinformatique.kubik.kos.model.account.Address kosAddress);
}
