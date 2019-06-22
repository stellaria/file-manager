package com.lunacia.filemanager.utils;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class MailUtil {

	private HtmlEmail email;

	public MailUtil() throws EmailException {
		email = new HtmlEmail();
		email.setHostName("smtp.163.com");
		email.setCharset("utf-8");
		email.setFrom("13950553737@163.com", "lunacia");
		email.setAuthentication("13950553737@163.com", "wang111111");
	}

	public void send(String to, String code) throws EmailException {
		email.addTo(to);
		email.setSubject("验证码");
		email.setMsg(code);
		email.send();
	}

	public static void main(String[] args) throws EmailException {
		MailUtil mailUtil = new MailUtil();
		mailUtil.send("99730729@qq.com", RandomUtil.confirmCode());
	}

}
