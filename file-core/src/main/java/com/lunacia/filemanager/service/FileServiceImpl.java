package com.lunacia.filemanager.service;


import com.lunacia.filemanager.domain.File;
import com.lunacia.filemanager.domain.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileServiceImpl implements FileService{


	private static final String FILE_COLLECTION="file";

	private static final String RECORD_COLLECTION="record";
	@Autowired
	private MongoTemplate mongoTemplate;


	@Override
	public void uploadFile(File file) {
		Query query1 = new Query(Criteria.where("location").is(file.getLocation()));
		long count = mongoTemplate.count(query1, FILE_COLLECTION);
		if (count>0L) { //同名
			String cur = file.getLocation();
			if (cur.lastIndexOf('.') == -1) {
				cur=cur+count;
			}else {
				String front = cur.substring(0, cur.lastIndexOf('.'));
				String last = cur.substring(cur.lastIndexOf('.'));

				file.setLocation(front+count+last);
			}
		}
		mongoTemplate.insert(file, FILE_COLLECTION);
	}

	@Override
	public void record(Record record) {
		mongoTemplate.insert(record, RECORD_COLLECTION);
	}

	@Override
	public void delete(File file) {
		Query query = new Query(Criteria.where("_id").is(file.getId()));
		mongoTemplate.findAndRemove(query, File.class, FILE_COLLECTION);
	}

	@Override
	public File getFile(String absolutePath) {
//		File file = new File();
//		if (absolutePath.substring(absolutePath.lastIndexOf('/') + 1).matches("\\d+"))
//			file.setTimestamp(absolutePath.substring(absolutePath.lastIndexOf('/') + 1));
//		else
//			file.setOrigin(absolutePath.substring(absolutePath.lastIndexOf('/') + 1));
//		file.setLocation(absolutePath.substring(0, absolutePath.lastIndexOf('/')));

		Query query = new Query(Criteria.where("location").is(absolutePath));
		return mongoTemplate.findOne(query, File.class, FILE_COLLECTION);
	}

	@Override
	public File getFileByNameAndLocation(String name, String location) {
		Query query = new Query(Criteria.where("location").regex(location).and("origin").is(name));

		return mongoTemplate.findOne(query, File.class, FILE_COLLECTION);
	}

	@Override
	public File getFileByTimestamp(String timestamp) {

		Query query = new Query(Criteria.where("timestamp").is(timestamp));
		return mongoTemplate.findOne(query, File.class, FILE_COLLECTION);
	}

	@Override
	public List<File> getAllFiles(String location) {

		Query query = new Query();
		query.addCriteria(Criteria.where("location").regex(location));
		return mongoTemplate.findAll(File.class, FILE_COLLECTION);
	}

	public static void main(String[] args) {
		java.io.File file = new java.io.File("/users/tiefblau/sublimeproject/myHead.h");
		System.out.println(file.exists());
		System.out.println(file.getAbsolutePath());
	}
}
