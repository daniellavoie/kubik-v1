package com.cspinformatique.kubik.server.domain.system.service;

import java.io.InputStream;

public interface LogoService {
	InputStream findLogo();
	
	void saveLogo(byte[] logo);
}
