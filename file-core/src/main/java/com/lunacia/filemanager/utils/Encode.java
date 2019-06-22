package com.lunacia.filemanager.utils;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

	public static void main(String[] args) throws ParseException, IOException {
		System.out.println(Encode.MD5("jack990729"));
	}
}
