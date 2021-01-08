package com.lunacia.filemanager.service;

import com.lunacia.filemanager.domain.User;

import java.util.List;

public interface UserService {


	//登录 返回空为登录失败,返回用户为登录成功
	User login(String username, String password);

	void init(String username, String password);

//	User findDuplicate(String email, String username);
//
//	void signUp(User user);
//
//	List<User> getUserlist();
//
//	void changeAuth(String id, boolean upload, boolean download, boolean delete);
}
