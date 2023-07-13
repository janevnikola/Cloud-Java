package com.nikola;

import java.util.ArrayList;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="documents")
public class Document{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length=512,nullable = false,unique = true)
	private String name;
	
	private long size;
	
	@Column(name="upload_time")
	private Date uploadTime;
	
	@Column(name = "content")
	private byte [] content;
	
	
	
	
	
	
	
	public Document() {
	
	}
	public Document(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public Date getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(  Date uploadTime) {
		this.uploadTime = uploadTime;
	}
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}

	
	
	
	
	
}