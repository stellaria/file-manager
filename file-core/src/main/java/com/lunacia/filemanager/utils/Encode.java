package com.lunacia.filemanager.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class Encode {
	public static String MD5(String param) {
		byte[] secrets = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(param.getBytes());
			secrets = md.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		String md5code = new BigInteger(1, secrets).toString(16);// 16进制数字
		// 如果生成数字未满32位，需要前面补0
		int length = md5code.length();
		for (int i = 0; i < 32 - length; i++) {
			md5code = "0" + md5code;
		}
		return md5code;
	}

	public static String Base64Encode(String param) {
		Base64.Encoder encoder = Base64.getEncoder();
		byte[] text = param.getBytes();

		return encoder.encodeToString(text);
	}

	public static String Base64Decode(String param) throws IOException {
		Base64.Decoder decoder = Base64.getDecoder();

		return new String(decoder.decode(param), "UTF-8");

	}

}
