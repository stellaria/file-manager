package com.lunacia.filemanager.controller;


import com.lunacia.filemanager.domain.File;
import com.lunacia.filemanager.domain.Record;
import com.lunacia.filemanager.domain.SimpleFile;
import com.lunacia.filemanager.service.FileServiceImpl;
import com.lunacia.filemanager.utils.Encode;
import com.lunacia.filemanager.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api")
public class FileController {

	@Autowired
	private FileServiceImpl fileService;

	private static final String SECRET = "lunacia";

	private static final Long EXPIRE_TIME = 30*60*1000L;

	@Value("${location.prefix}")
	private String LOCATION_PREFIX;

	@PostMapping("/upload")
	public void uploadFile(@RequestParam("file") MultipartFile multipartFile,
	                       @RequestParam("username") String username,
	                       @RequestParam("location") String location, @RequestParam("timestamp") String timestamp) throws IOException {
		//创建file对象并保存至数据库
		File file = new File();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(Long.valueOf(timestamp));
		file.setOwner(username);
		file.setDate(simpleDateFormat.format(date));
		file.setOrigin(multipartFile.getOriginalFilename());
		file.setLocation(location+"/"+file.getOrigin());
		file.setSize(checkSize(multipartFile.getSize()));
		file.setTimestamp(timestamp+RandomUtil.suffix());
		fileService.uploadFile(file);

		java.io.File storage = new java.io.File( LOCATION_PREFIX + location + "/" + file.getTimestamp() +
				file.getOrigin().substring(file.getOrigin().lastIndexOf('.')));
		multipartFile.transferTo(storage);
		//储存上传记录至数据库
		Record record = new Record();
		record.setOperation("upload");
		record.setOperationDate(simpleDateFormat.format(date));
		record.setUsername(username);
		record.setFileName(multipartFile.getOriginalFilename());
		fileService.record(record);
	}


	/**
	 * 获取当前目录下的所有文件
	 * @param currentLocation
	 * @return
	 */
	@GetMapping("/filelists")
	public HashMap<String, Object> getFileList(@RequestParam("current") String currentLocation) {
		java.io.File file = new java.io.File(currentLocation);
		List<SimpleFile> list = new LinkedList<>();
		HashMap<String, Object> res = new HashMap<>();
		HashMap<String, Object> data = new HashMap<>();
		List<File> dbFiles;

		dbFiles = fileService.getAllFiles(currentLocation);
		if (file.exists()) {
			for (File dbFile : dbFiles) {
				SimpleFile sf = new SimpleFile();
				sf.setName(dbFile.getOrigin());
				java.io.File file1 = new java.io.File(LOCATION_PREFIX + dbFile.getLocation());
				if (file1.isFile())
					sf.setType("file");
				else
					sf.setType("folder");
				list.add(sf);
			}
			data.put("list", list);
			res.put("code", 200);
			res.put("msg", "");
			res.put("data", data);
		} else {
			res.put("code", 500);
			res.put("msg", "file not exist");
		}
		return res;
	}

	@PostMapping("/download")
	public ResponseEntity<FileSystemResource> download(@RequestParam("file") String filePath,
	                                                   @RequestParam("username") String username,
	                                                   HttpServletResponse response) {


		try {
			File myFile = fileService.getFile(URLDecoder.decode(LOCATION_PREFIX+filePath, "utf-8"));
			java.io.File file = new java.io.File(filePath.substring(0,
					filePath.lastIndexOf('/') + 1) + myFile.getTimestamp() + myFile.getOrigin().substring(
							myFile.getOrigin().lastIndexOf('.')
			));
			String fileName = myFile.getOrigin();
			// 获取本地文件系统中的文件资源
			FileSystemResource resource = new FileSystemResource(file.getAbsolutePath());

			// 解析文件的 mime 类型
			String mediaTypeStr = URLConnection.getFileNameMap().getContentTypeFor(fileName);
			// 无法判断MIME类型时，作为流类型
			mediaTypeStr = (mediaTypeStr == null) ? MediaType.APPLICATION_OCTET_STREAM_VALUE : mediaTypeStr;
			// 实例化MIME
			MediaType mediaType = MediaType.parseMediaType(mediaTypeStr);


			/*
			 * 构造响应的头
			 */
			HttpHeaders headers = new HttpHeaders();
			// 下载之后需要在请求头中放置文件名，该文件名按照ISO_8859_1编码。
			String filenames = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
			headers.setContentDispositionFormData("attachment", filenames);
			headers.setContentType(mediaType);


			//记录操作
			Record record = new Record();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			record.setFileName(myFile.getOrigin());
			record.setUsername(username);
			record.setOperationDate(simpleDateFormat.format(new Date()));
			record.setOperation("download");
			fileService.record(record);
			/*
			 * 返还资源
			 */
			return ResponseEntity.ok()
					.headers(headers)
					.contentLength(resource.getInputStream().available())
					.body(resource);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}


	/**
	 *
	 * @param absolutePath 文件夹地址
	 * @param username
	 * @return
	 */
	@PostMapping("/create")
	public HashMap<String, Object> createFolder(@RequestParam("name") String absolutePath,
	                                            @RequestParam("username") String username) {
		java.io.File file = new java.io.File(LOCATION_PREFIX+absolutePath);
		HashMap<String, Object> res = new HashMap<>();
		if (!file.exists()) {
			file.mkdirs();
			res.put("code", 200);
			res.put("msg", "");
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Record record = new Record();
			record.setOperation("create folder");
			record.setOperationDate(simpleDateFormat.format(new Date()));
			record.setUsername(username);
			record.setFileName(absolutePath.substring(absolutePath.lastIndexOf('/')+1));
			fileService.record(record);
		} else {
			res.put("code", 500);
			res.put("msg", "create failed");
		}
		return res;

	}

	/**
	 * 删除文件
	 * @param absolutePath 绝对路径
	 * @param username 用户名
	 * @return
	 */
	@PostMapping("/delete")
	public HashMap<String, Object> deleteFile(@RequestParam("path") String absolutePath,
	                                          @RequestParam("username") String username) {
		HashMap<String, Object> res = new HashMap<>();
		if (absolutePath.lastIndexOf('.') == -1) {
			java.io.File file = new java.io.File(absolutePath);
			deleteFile(file, username);
			res.put("code", 200);
			res.put("msg", "");
			return res;
		}
		File myFile = fileService.getFile(absolutePath);
		java.io.File file = new java.io.File(absolutePath.substring(0,
				absolutePath.lastIndexOf('/') + 1) + myFile.getTimestamp() +
				myFile.getOrigin().substring(myFile.getOrigin().lastIndexOf('.')));
		deleteFile(file, username);
		res.put("code", 200);
		res.put("msg", "");
		return res;
	}

	/**
	 * 判断文件大小
	 * @param size
	 * @return
	 */
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


	/**
	 * 递归删除文件及文件夹
	 * @param dirFile
	 * @param username
	 * @return
	 */
	private boolean deleteFile(java.io.File dirFile, String username) {
		if (dirFile.exists()) {
			if (dirFile.isFile()) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Record record = new Record();
				record.setFileName(fileService.getFile(dirFile.getAbsolutePath().substring(0, dirFile.getAbsolutePath().lastIndexOf('.'))).getOrigin());
				record.setUsername(username);
				record.setOperationDate(simpleDateFormat.format(new Date()));
				record.setOperation("delete");
				fileService.record(record);
				fileService.delete(fileService.getFile(dirFile.getAbsolutePath()));
				return dirFile.delete();
			}
			else {
				for (java.io.File file: dirFile.listFiles()) {
					deleteFile(file, username);
				}
			}
			dirFile.delete();
		}
		return false;
	}

	@PostMapping("/share")
	public ResponseEntity shareCode(@RequestBody Map<String, Object> body) {
		Map<String, Object> res = new HashMap<>();
		String location = (String) body.get("location");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String expire = sdf.format(new Date(System.currentTimeMillis()+EXPIRE_TIME));


		String suffix = Encode.Base64Encode(location + "_/" + SECRET + "_/" + expire);
		res.put("suffix", suffix);
		return ResponseEntity.ok(res);
	}

	@GetMapping("/share/{shareCode}")
	public ResponseEntity responseShareCode(@PathVariable String shareCode) {
		Map<String, Object> res = new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			String context = Encode.Base64Decode(shareCode);
			String[] slice = context.split("_/");
			if (slice.length != 3)
			{
				res.put("msg", "分享码错误");
				res.put("code", 500);
			}
			else if (!slice[1].equals(SECRET))
			{
				res.put("msg", "分享码错误");
				res.put("code", 500);

			}
			else if (sdf.parse(slice[2]).getTime() < System.currentTimeMillis()) {
				res.put("msg", "分享码过期");
				res.put("code", 503);
			}

			File file = fileService.getFile(slice[0]);
			if (file.getId() != null) {
				res.put("msg","");
				res.put("code",200);
				res.put("file",file);
			}
		} catch (ParseException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		return ResponseEntity.ok(res);
	}

}
