package com.cspinformatique.kubik.domain.product.service.impl;

import java.io.UnsupportedEncodingException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.company.model.Company;
import com.cspinformatique.kubik.domain.company.service.CompanyService;
import com.cspinformatique.kubik.domain.product.service.ImageService;

@Service
public class ImageServiceImpl implements ImageService, InitializingBean {
	@Autowired private CompanyService companyService;
	
	private Base64 base64;
	private Cipher cipher;
	private Company company;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		company = this.companyService.find();

		cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");
		
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(company
				.getDilicomImageEncryptionKey().getBytes("UTF8"), "Blowfish"),
				new IvParameterSpec("00000000".getBytes()));
		
		base64 = new Base64(true);
	}

	@Override
	public String getEncryptedUrl(String ean13, String supplierEan13){
		return this.encryptBlowfish("DILICOM:"+ ean13 + "-" + supplierEan13);
	}

	private String encryptBlowfish(String toEncrypt) {
		try {
			return base64.encodeToString(cipher.doFinal(toEncrypt
					.getBytes("UTF8")));
		} catch (IllegalBlockSizeException | BadPaddingException
				| UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}

}
