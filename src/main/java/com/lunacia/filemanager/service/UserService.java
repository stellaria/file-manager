package com.lunacia.filemanager.service;

import com.lunacia.filemanager.domain.User;

import java.util.List;

public interface UserService {

	User login(String username, String password);

	User findDuplicate(String email, String username);

	void signUp(User user);

	List<User> getUserlist();

	void changeAuth(String id, boolean upload, boolean download, boolean delete);
}
