package com.cspinformatique.kubik.server.proxy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.model.proxy.Proxy;
import com.cspinformatique.kubik.server.proxy.repository.ProxyRepository;
import com.cspinformatique.kubik.server.proxy.service.ProxyService;

@Service
public class ProxyServiceImpl implements ProxyService {
	@Autowired
	private ProxyRepository proxyRepository;

	@Override
	public void delete(int id) {
		this.proxyRepository.delete(id);
	}

	@Override
	public Proxy find() {
		List<Proxy> proxyList = this.proxyRepository.findAll();

		if (proxyList.isEmpty()) {
			return null;
		}

		return proxyList.get(0);
	}

	@Override
	public Proxy save(Proxy proxy) {
		Proxy oldProxy = this.find();

		if (oldProxy != null) {
			this.delete(oldProxy.getId());
		}

		return this.proxyRepository.save(proxy);
	}

}
