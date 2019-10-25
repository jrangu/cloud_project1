package model;

import java.sql.Timestamp;

import com.google.gson.annotations.SerializedName;

public class UserFileRelation {
	@SerializedName("file_key")
	public String fileKey;
	
	@SerializedName("user_id")
	public String userId;

	@SerializedName("first_name")
	public String firstName;

	@SerializedName("last_name")
	public String lastName;

	@SerializedName("file_name")
	public String fileName;

	@SerializedName("creation_timestamp")
	public Timestamp createdTimestamp;
	
	@SerializedName("last_updated_timestamp")
	public Timestamp updatedTimestamp;
	
	@SerializedName("delete_flag")
	public Boolean isDeleted;

	public String getFileKey() {
		return fileKey;
	}

	public UserFileRelation setFileKey(String fileKey) {
		this.fileKey = fileKey;
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public UserFileRelation setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public String getFirstName() {
		return firstName;
	}

	public UserFileRelation setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public String getLastName() {
		return lastName;
	}

	public UserFileRelation setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public String getFileName() {
		return fileName;
	}

	public UserFileRelation setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public Timestamp getCreatedTimestamp() {
		return createdTimestamp;
	}

	public UserFileRelation setCreatedTimestamp(Timestamp createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
		return this;
	}

	public Timestamp getUpdatedTimestamp() {
		return updatedTimestamp;
	}

	public UserFileRelation setUpdatedTimestamp(Timestamp updatedTimestamp) {
		this.updatedTimestamp = updatedTimestamp;
		return this;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public UserFileRelation setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
		return this;
	}
}
