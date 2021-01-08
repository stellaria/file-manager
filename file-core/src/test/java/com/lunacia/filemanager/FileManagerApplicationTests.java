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
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
	public void countTest(){
		Query query = new Query();
		System.out.println(mongoTemplate.count(query, "user"));
	}

	@Test
	public void getShareCodeTest() {
		String location = "/lunacia/160977327748418770.pdf";
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
		java.io.File file = new java.io.File(LOCATION_PREFIX + currentLocation);
		List<SimpleFile> list = new LinkedList<>();
		HashMap<String, Object> res = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();
		List<com.lunacia.filemanager.domain.File> dbFiles;
		List<java.io.File> fileList;

		dbFiles = fileService.getAllFiles(currentLocation);
		if (file.exists()) {
			fileList = Arrays.asList(file.listFiles());
			for (java.io.File f : fileList) {
				if (f.isDirectory()) {
					SimpleFile sf = new SimpleFile();
					sf.setType("folder");
					sf.setName(f.getName());
					list.add(sf);
				} else {
					com.lunacia.filemanager.domain.File mf = fileService.getFile(f.getPath().substring(f.getPath().indexOf(currentLocation)));
					SimpleFile sf = new SimpleFile();
					sf.setName(mf.getOrigin());
					sf.setType("file");
					list.add(sf);
				}
//				System.out.println(f.getPath().substring(f.getPath().indexOf(currentLocation)));
//				com.lunacia.filemanager.domain.File mf = fileService.getFile(f.getPath()); /
			}
		}
		for (SimpleFile s : list){
			System.out.println(s.getName()+ " " + s.getType());
		}
	}


	@Test
	public void downloadTest() {
		String filePath = "/lunacia/6JIB20(]VWX6DEM(3SW7RWA.jpg";
		String username = "lunacia";

		String location = filePath.substring(0, filePath.lastIndexOf('/'));
		String fileName = filePath.substring(filePath.lastIndexOf('/'));

		System.out.println(location + " " + fileName);

		com.lunacia.filemanager.domain.File myFile = fileService.getFileByNameAndLocation(fileName, location);
		System.out.println(myFile == null);
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
