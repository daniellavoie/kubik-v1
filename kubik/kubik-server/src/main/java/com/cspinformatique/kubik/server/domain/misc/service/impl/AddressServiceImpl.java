package com.cspinformatique.kubik.server.domain.misc.service.impl;

import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.domain.misc.service.AddressService;
import com.cspinformatique.kubik.server.model.misc.Address;

@Service
public class AddressServiceImpl implements AddressService {

	@Override
	public Address transcodeFromKos(com.cspinformatique.kubik.kos.model.account.Address kosAddress) {
		Address address = new Address();
		address.setCity(kosAddress.getCity());
		address.setFirstName(kosAddress.getFirstName());
		address.setKosId(kosAddress.getId());
		address.setLastName(kosAddress.getLastName());
		address.setPhone(kosAddress.getPhone());
		address.setState(kosAddress.getState());
		address.setStreetLine1(kosAddress.getAddress1());
		address.setStreetLine2(kosAddress.getAddress2());
		address.setZipCode(kosAddress.getPostalCode());
		
		return address;
	}

}
