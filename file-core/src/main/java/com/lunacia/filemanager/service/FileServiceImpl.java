package com.lunacia.filemanager.service;


import com.lunacia.filemanager.dao.FileMapper;
import com.lunacia.filemanager.domain.File;
import com.lunacia.filemanager.domain.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileServiceImpl implements FileService{

	@Autowired
	private FileMapper fileMapper;


	@Override
	public void uploadFile(File file) {
		fileMapper.uploadFile(file);
	}

	@Override
	public void record(Record record) {
		fileMapper.record(record);
	}

	@Override
	public void delete(File file) {
		fileMapper.deleteFile(file);
	}

	@Override
	public File getFile(String absolutePath) {
		File file = new File();
		if (absolutePath.substring(absolutePath.lastIndexOf('/') + 1).matches("\\d+"))
			file.setTimestamp(absolutePath.substring(absolutePath.lastIndexOf('/') + 1));
		else
			file.setOrigin(absolutePath.substring(absolutePath.lastIndexOf('/') + 1));
		file.setLocation(absolutePath.substring(0, absolutePath.lastIndexOf('/')));
		return fileMapper.getFile(file);
	}

	@Override
	public File findFileByTimestamp(String timestamp) {
		return fileMapper.findFileByTimestamp(timestamp);
	}

	@Override
	public List<File> getAllFiles(String location) {
		return fileMapper.getAllFiles(location);
	}

	public static void main(String[] args) {
		java.io.File file = new java.io.File("/users/tiefblau/sublimeproject/myHead.h");
		System.out.println(file.exists());
		System.out.println(file.getAbsolutePath());
	}
}
