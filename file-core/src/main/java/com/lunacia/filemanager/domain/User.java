package com.lunacia.filemanager.domain;


public class User {
	private int id;
	private String username;
	private String password;
	private String email;
	private String group;
	private boolean admin;
	private boolean upload;
	private boolean download;
	private boolean delete;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isUpload() {
		return upload;
	}

	public void setUpload(boolean upload) {
		this.upload = upload;
	}

	public boolean isDownload() {
		return download;
	}

	public void setDownload(boolean download) {
		this.download = download;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	@Override
	public String toString() {
		return this.username;
	}
}
