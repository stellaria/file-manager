package com.lunacia.filemanager.service;

import com.lunacia.filemanager.domain.User;
import com.lunacia.filemanager.utils.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {


	private static final String USER_COLLECTION = "user";

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public User login(String username, String password) {

		Query query = new Query(Criteria.where("username").is(username).and("password").is(Encode.MD5(password)));

		User user = mongoTemplate.findOne(query, User.class, USER_COLLECTION);
		if (user.getId() == null)
			return null;
		else
			return user;
	}
//
//	@Override
//	public User findDuplicate(String email, String username) {
//		return userMapper.findDuplicate(email, username);
//	}



}
