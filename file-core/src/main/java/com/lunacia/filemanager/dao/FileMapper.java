package com.lunacia.filemanager.dao;

import com.lunacia.filemanager.domain.File;
import com.lunacia.filemanager.domain.Record;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FileMapper {

	@Select("SELECT * FROM t_files WHERE location=#{location} AND timestamp=#{timestamp} OR origin=#{origin}")
	File getFile(File file);

	@Select("SELECT * FROM t_files WHERE timestamp=#{timestamp}")
	File findFileByTimestamp(@Param("timestamp") String timestamp);

	@Select("SELECT * FROM t_files WHERE location like CONCAT(#{location},'%')")
	List<File> getAllFiles(@Param("location") String location);

	@Insert("INSERT INTO t_files(origin, timestamp, owner, location, size, date)" +
			"values(#{origin}, #{timestamp}, #{owner}, #{location}, #{size}, #{date})")
	void uploadFile(File file);

	@Insert("INSERT INTO t_records(username, operation, file_name, op_date)" +
			"values(#{username}, #{operation}, #{fileName}, #{operationDate})")
	void record(Record record);


	@Delete("DELETE FROM t_files WHERE id=#{id}")
	void deleteFile(File file);


}
