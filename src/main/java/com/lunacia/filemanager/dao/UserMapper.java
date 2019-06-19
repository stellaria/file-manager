package com.lunacia.filemanager.dao;

import com.lunacia.filemanager.domain.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapper {


	@Select("SELECT * FROM t_users WHERE mail=#{email} AND password=#{password}")
	@Results(id = "loginMap" , value = {
			@Result(property = "email", column = "mail")
	})
	User login(@Param("email") String email, @Param("password") String password);


	@Select("SELECT * FROM t_users WHERE mail=#{email} OR username=#{username}")
	User findDuplicate(@Param("email") String mail, @Param("username") String username);

	@Insert("INSERT INTO t_users(username,password,mail)" +
			"values(#{username}, #{password}, #{email})")
	void signUp(User user);

	@Select("SELECT * FROM t_users WHERE admin<>1")
	List<User> getUserlist();

	@Update("UPDATE t_users SET upload=#{upload}, download=#{download}, `delete`=#{delete} WHERE id=#{id}")
	void changeAuth(@Param("id") String id, @Param("upload") boolean upload, @Param("download") boolean download, @Param("delete") boolean delete);
}
