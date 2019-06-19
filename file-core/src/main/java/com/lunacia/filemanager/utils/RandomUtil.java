package com.lunacia.filemanager.utils;

import java.io.File;
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

	public static void main(String[] args) {
		File file = new File("/users/tiefblau/ideaprojects/sms");
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			System.out.println(files[i].isFile() + " " + files[i].getName());
		}
	}
}
