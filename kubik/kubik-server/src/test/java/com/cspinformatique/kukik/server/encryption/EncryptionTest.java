package com.cspinformatique.kukik.server.encryption;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

public class EncryptionTest {
	private static Base64 base64 = new Base64(true);

	private String dilicomKey = "motdpasseDilicom";

	@Test
	public void encryptionTest() throws Exception{
		SecretKeySpec key = new SecretKeySpec(dilicomKey.getBytes("UTF8"), "Blowfish");
        
		Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");
        
		cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec("00000000".getBytes()));

        Assert.assertEquals(base64.encodeToString(cipher.doFinal("DILICOM:9782000000006-3010000000006".getBytes("UTF8"))), "YVV2myd52pjPexG2wGveNSgRgdsT-PfaxI4MxC9WYQEz5alOtdHD4Q\r\n");
	}
}
