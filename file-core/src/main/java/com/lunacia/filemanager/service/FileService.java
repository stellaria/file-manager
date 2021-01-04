package com.lunacia.filemanager.service;

import com.lunacia.filemanager.domain.File;
import com.lunacia.filemanager.domain.Record;

import java.util.List;

public interface FileService {

	//上传文件
	void uploadFile(File file);

	//插入一条记录
	void record(Record record);

	//删除文件记录
	void delete(File file);

	//根据地址获取文件
	File getFile(String absolutePath);


	File findFileByTimestamp(String timestamp);

	//获取所有文件
	List<File> getAllFiles(String location);
}
