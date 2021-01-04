package com.lunacia.filemanager.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class RandomUtil {

	public static String suffix() {
		Random rand = new Random();

		int num = rand.nextInt(100000);

		String suffix = String.valueOf(num);
		int length = suffix.length();
		for (int i = 0; i < 5 - length; i++) {
			suffix = "0" + suffix;
		}
		return suffix;
	}

	public static String confirmCode() {
		String codeSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
		StringBuilder confirm = new StringBuilder();
		Random rand = new Random();
		for (int i = 0; i < 6; i++) {
			confirm.append(codeSet.charAt(rand.nextInt(62)));
		}
		return  confirm.toString();
	}

	public static void main(String[] args) throws IOException {
//		String context = Encode.Base64Decode("L2x1bmFjaWEvYWFhLmpwZ18vbHVuYWNpYV8vMjAyMS0wMS0wMiAyMToyMTozMg==");
//		String[] slice = context.split("_/");
//		System.out.println(slice[0]);

//		File file = new File("/usr/local/var/file_manage/lunacia/web.xml");
//		System.out.println(file.isFile());
		System.out.println("/users/tiefblau/var/file_manage".length());

	}
}
