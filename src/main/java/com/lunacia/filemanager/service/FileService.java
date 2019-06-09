package com.lunacia.filemanager.service;

import com.lunacia.filemanager.domain.File;
import com.lunacia.filemanager.domain.Record;

import java.util.List;

public interface FileService {

	void uploadFile(File file);

	void record(Record record);

	void delete(File file);

	File getFile(String absolutePath);

	File findFileByTimestamp(String timestamp);

	List<File> getAllFiles(String location);
}
