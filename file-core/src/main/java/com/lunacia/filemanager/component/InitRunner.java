package com.lunacia.filemanager.component;

import com.lunacia.filemanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Order(1)
public class InitRunner implements ApplicationRunner {

	@Value("${login.username}")
	private String username;
	@Value("${login.password}")
	private String password;

	@Autowired
	private UserService userService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		userService.init(username, password);
	}
}
