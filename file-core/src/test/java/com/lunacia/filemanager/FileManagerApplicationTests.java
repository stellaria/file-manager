package com.lunacia.filemanager;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.lunacia.filemanager.domain.SimpleFile;
import com.lunacia.filemanager.domain.User;
import com.lunacia.filemanager.service.FileService;
import com.lunacia.filemanager.service.UserService;
import com.lunacia.filemanager.utils.Encode;
import com.mongodb.Mongo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileManagerApplicationTests {

	@Autowired
	private FileService fileService;

	@Autowired
	private UserService userService;

	@Autowired
	private MongoTemplate mongoTemplate;


	@Value("${location.prefix}")
	private String LOCATION_PREFIX;

//	@Test
//	public void signupTest() {
//		User user = new User();
//		user.setUsername("lunacia");
//		user.setPassword(Encode.MD5("jack990729"));
//
//		mongoTemplate.insert(user, "user");
//	}
//
//	@Test
//	public void loginTest() {
//
//		User user;
//		user = userService.login("lunacia", "jack990729");
//
//		if (user.getId() != null)
//			System.out.println(user.getId());
//
//	}

	@Test
	public void getShareCodeTest() {
		String location = "/lunacia/src.jpg";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String expire = sdf.format(new Date(System.currentTimeMillis()+30*60*1000L));
		String suffix = Encode.Base64Encode(location + "_/" + "lunacia" + "_/" + expire);

		System.out.println(suffix);
		//L2x1bmFjaWEvYWFhLmpwZ18vbHVuYWNpYV8vMjAyMS0wMS0wMiAyMToyMTozMg==
	}
//
//	@Test
//	public void mongoTest() {
//
//		System.out.println(mongoTemplate.collectionExists("user"));
//	}
	@Test
	public void fileInsertTest() {
		File file = new File("/lunacia/src.zip");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		com.lunacia.filemanager.domain.File file1 = new com.lunacia.filemanager.domain.File();
		file1.setTimestamp(String.valueOf(System.currentTimeMillis()));
		file1.setOrigin(file.getName());
		file1.setLocation(file.getAbsolutePath());
		file1.setSize(checkSize(file.length()));
		file1.setDate(sdf.format(new Date()));
		file1.setOwner("lunacia");
		fileService.uploadFile(file1);

	}

	@Test
	public void shareCodeTest() throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String context = Encode.Base64Decode("L2x1bmFjaWEvYWFhLmpwZ18vbHVuYWNpYV8vMjAyMS0wMS0wMiAyMToyMTozMg==");
		String[] slice = context.split("_/");

		com.lunacia.filemanager.domain.File file = fileService.getFile(slice[0]);
		System.out.println(file.getLocation());
	}

	@Test
	public void currentFileListTest(){

		String currentLocation = "/lunacia";
		List<SimpleFile> list = new LinkedList<>();
		List<com.lunacia.filemanager.domain.File> dbFiles;

		dbFiles = fileService.getAllFiles(currentLocation);
		for (com.lunacia.filemanager.domain.File dbFile : dbFiles) {
			SimpleFile sf = new SimpleFile();
			sf.setName(dbFile.getOrigin());
			java.io.File file1 = new java.io.File(LOCATION_PREFIX + dbFile.getLocation());
			if (file1.isFile())
				sf.setType("file");
			else
				sf.setType("folder");
			list.add(sf);
		}

		for (SimpleFile sf : list) {
			System.out.println(sf.getName() + " " + sf.getType());
		}
	}

	private String checkSize (Long size) {
		double fileSize = 0;
		char m;
		if (size / 1024 < 1024) {
			fileSize = size / 1024.0;
			m = 'B';
		}
		else if (size / 1048576 < 1024) {
			fileSize = size / 1048576.0;
			m = 'M';
		}
		else {
			fileSize = size / 1073741824.0;
			m = 'G';
		}
		String str = String.format("%.2f", fileSize);
		str += Character.toString(m);
		return str;
	}
}
