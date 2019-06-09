package com.lunacia.filemanager.service;

import com.lunacia.filemanager.domain.User;

public interface UserService {

	User login(String username, String password);

	User findDuplicate(String email, String username);

	void signUp(User user);
}
