package com.lunacia.filemanager.service;

import com.lunacia.filemanager.domain.User;
import com.lunacia.filemanager.utils.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;


@Service
@CrossOrigin
public class UserServiceImpl implements UserService {


	private static final String USER_COLLECTION = "user";

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public User login(String username, String password) {

		Query query = new Query(Criteria.where("username").is(username).and("password").is(password));

		User user = mongoTemplate.findOne(query, User.class, USER_COLLECTION);
		return user;
	}

	@Override
	public void init(String username, String password) {
		Query query = new Query();
		long count = mongoTemplate.count(query, USER_COLLECTION);
		if (count == 0L) {
			User user = new User();
			user.setUsername(username);
			user.setPassword(Encode.MD5(password));
			mongoTemplate.insert(user, USER_COLLECTION);
		}
	}

	//
//	@Override
//	public User findDuplicate(String email, String username) {
//		return userMapper.findDuplicate(email, username);
//	}



}
