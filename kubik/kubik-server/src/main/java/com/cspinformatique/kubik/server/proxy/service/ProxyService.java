package com.cspinformatique.kubik.server.proxy.service;

import com.cspinformatique.kubik.server.model.proxy.Proxy;

public interface ProxyService {
	void delete(int id);
	
	Proxy find();
	
	Proxy save(Proxy proxy);
}
