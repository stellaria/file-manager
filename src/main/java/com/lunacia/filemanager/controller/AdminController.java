package com.lunacia.filemanager.controller;


import com.lunacia.filemanager.domain.User;
import com.lunacia.filemanager.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AdminController {

	@Autowired
	private UserServiceImpl userService;

	@GetMapping("/userlist")
	public HashMap<String, Object> getUserlist() {
		HashMap<String, Object> res = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();
		res.put("code", 200);
		res.put("msg", "");
		data.put("userlist", userService.getUserlist());
		res.put("data", data);
		return res;
	}

	@PostMapping("/change")
	public String changeAuth(@RequestBody Map<String, Object> res) {
		List list = (List) res.get("userlist");
		Iterator iterator = list.iterator();
		HashMap<String, Object> userInfo = null;
		while (iterator.hasNext()) {
			userInfo = (HashMap<String, Object>) iterator.next();
			userService.changeAuth(String.valueOf(userInfo.get("id")), (Boolean)userInfo.get("upload"), (Boolean)userInfo.get("download"),
					(Boolean)userInfo.get("delete"));
		}
		return "success";
	}
}
