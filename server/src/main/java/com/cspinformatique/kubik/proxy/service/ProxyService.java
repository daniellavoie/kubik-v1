package com.cspinformatique.kubik.proxy.service;

import com.cspinformatique.kubik.proxy.model.Proxy;

public interface ProxyService {
	void delete(int id);
	
	Proxy find();
	
	Proxy save(Proxy proxy);
}
