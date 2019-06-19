package com.lunacia.filemanager.service;

import com.lunacia.filemanager.dao.UserMapper;
import com.lunacia.filemanager.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public User login(String username, String password) {
		return userMapper.login(username, password);
	}

	@Override
	public User findDuplicate(String email, String username) {
		return userMapper.findDuplicate(email, username);
	}

	@Override
	public List<User> getUserlist() {
		return userMapper.getUserlist();
	}

	@Override
	public void signUp(User user) {
		userMapper.signUp(user);
	}

	@Override
	public void changeAuth(String id, boolean upload, boolean download, boolean delete) {
		userMapper.changeAuth(id, upload, download, delete);
	}
}
