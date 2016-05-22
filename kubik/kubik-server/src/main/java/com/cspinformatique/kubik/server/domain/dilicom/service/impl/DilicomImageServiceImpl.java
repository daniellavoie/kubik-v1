package com.cspinformatique.kubik.server.domain.dilicom.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.domain.company.service.CompanyService;
import com.cspinformatique.kubik.server.domain.dilicom.service.DilicomImageService;
import com.cspinformatique.kubik.server.model.company.Company;

@Service
public class DilicomImageServiceImpl implements DilicomImageService {
	@Autowired
	private CompanyService companyService;

	private Base64 base64;
	private Cipher cipher;

	@Override
	public String getEncryptedUrl(String ean13, String supplierEan13) {
		return this.encryptBlowfish("DILICOM:" + ean13 + "-" + supplierEan13).trim();
	}

	private String encryptBlowfish(String toEncrypt) {
		try {
			if (base64 == null) {
				Company company = this.companyService.findComapny().get();

				cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");

				cipher.init(Cipher.ENCRYPT_MODE,
						new SecretKeySpec(company.getDilicomImageEncryptionKey().getBytes("UTF8"), "Blowfish"),
						new IvParameterSpec("00000000".getBytes()));

				base64 = new Base64(true);
			}

			return base64.encodeToString(cipher.doFinal(toEncrypt.getBytes("UTF8")));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
				| InvalidAlgorithmParameterException | UnsupportedEncodingException | IllegalBlockSizeException
				| BadPaddingException ex) {
			throw new RuntimeException(ex);
		}
	}
}
