package com.y3r9.c47.dog.util;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AESUtil {
	
	private static final int KEY_SIZE = 128;
	

	/**
	 * 加密
	 * @param content
	 * @param password
	 * @return
	 */
	public static byte[] encrypt(String content, String password) throws Exception{
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(KEY_SIZE, new SecureRandom(password.getBytes()));
		SecretKey secretKey = kgen.generateKey();
		byte[] enCodeFormat = secretKey.getEncoded();
		SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		byte[] byteContent = content.getBytes("utf-8");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] result = cipher.doFinal(byteContent);
		return result;
	}
	
	/**
	 * 加密
	 * @param content
	 * @param password
	 * @return
	 */
	public static String encryptToBase64(String content, String password, String charsetName) throws Exception{
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(KEY_SIZE, new SecureRandom(password.getBytes()));
		SecretKey secretKey = kgen.generateKey();
		byte[] enCodeFormat = secretKey.getEncoded();
		SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		byte[] byteContent = content.getBytes(charsetName);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] result = cipher.doFinal(byteContent);
		return new BASE64Encoder().encode(result); 
	}
	
	/**
	 * 解密
	 * @param content
	 * @param password
	 * @return
	 */
	public static String decrypt(byte[] content, String password)
			throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(KEY_SIZE, new SecureRandom(password.getBytes()));
		SecretKey secretKey = kgen.generateKey();
		byte[] enCodeFormat = secretKey.getEncoded();
		SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] result = cipher.doFinal(content);
		return new String(result, "utf-8");
	}
	
	/**
	 * 解密
	 * @param codes
	 * @param password
	 * @return
	 */
	public static String decryptFromBase64(String codes, String password, String charsetName)
			throws Exception {
		byte[] content = new BASE64Decoder().decodeBuffer(codes);
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(KEY_SIZE, new SecureRandom(password.getBytes()));
		SecretKey secretKey = kgen.generateKey();
		byte[] enCodeFormat = secretKey.getEncoded();
		SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] result = cipher.doFinal(content);
		return new String(result, charsetName);
	}
}
