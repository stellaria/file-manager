package com.lunacia.filemanager.controller;


import com.lunacia.filemanager.domain.User;
import com.lunacia.filemanager.service.UserServiceImpl;
import com.lunacia.filemanager.utils.Encode;
import com.lunacia.filemanager.utils.MailUtil;
import com.lunacia.filemanager.utils.RandomUtil;
import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

	@Autowired
	private UserServiceImpl userService;
	private String confirmCode;

	@PostMapping("/login")
	public HashMap login(@RequestBody Map<String, Object> resMap) {

		HashMap<String, Object> res = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();
		String username = resMap.get("username").toString();
		String password = Encode.MD5(resMap.get("password").toString());

		User user = login(username, password);
		if (user == null) {
			res.put("code", 400);
			res.put("msg", "login failed");
		} else {
			res.put("code", 200);
			res.put("msg", "");
			data.put("user", user);
			res.put("data", data);
		}
		return res;
	}

	@PostMapping("/signUp")
	public HashMap<String, Object> signUp(@RequestBody Map<String, Object> resMap){
		HashMap<String, Object> res = new HashMap<>();
		User user = new User();
		user.setUsername((String) resMap.get("username"));
		user.setPassword(Encode.MD5((String) resMap.get("password")));
		user.setGroup((String) resMap.get("group"));
		user.setEmail((String) resMap.get("email"));
		if (userService.findDuplicate(user.getEmail(), user.getUsername()) == null) {
			userService.signUp(user);
			res.put("code", 200);
			res.put("msg", "");
			File file = new File("/file/"+user.getUsername());
			if (!file.exists()) {
				file.mkdirs();
			}
		} else if (!confirmCode.equals(resMap.get("confirmCode"))) {
			res.put("code", 400);
			res.put("msg", "confirm code doesn't match!");
		} else {
			res.put("code", 400);
			res.put("msg", "sign up failed, the email address or username has been used");
		}
		return res;
	}

	@GetMapping("/code")
	public HashMap<String, Object> getConfirmCode(@RequestParam("email") String email) {
		confirmCode = RandomUtil.confirmCode();
		HashMap<String, Object> res = new HashMap<>();
		try {
			MailUtil mail = new MailUtil();
			mail.send(email, confirmCode);
			res.put("code", 200);
			res.put("msg", "");
		} catch (EmailException e) {
			e.printStackTrace();
			res.put("code", 500);
			res.put("msg", "未知错误");
		}
		return res;
	}

	private User login(String username, String password) {
		return userService.login(username, password);
	}
}
