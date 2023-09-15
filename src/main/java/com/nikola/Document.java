package com.nikola;


import java.util.Date;




public class Document{
	
	
	private Long id;
	

	private String name;
	
	private long size;
	

	private Date uploadTime;
	
	
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
	public void setContent(byte[] new_content) {
		//you need to append, not overwrite 
	//	this.content+=new_content;
	
	    if (this.content == null) {
	        this.content = new_content.clone();
	    } else {
	        byte[] newLine = System.getProperty("line.separator").getBytes();
	        byte[] combinedContent = new byte[this.content.length + newLine.length + new_content.length];
	        System.arraycopy(this.content, 0, combinedContent, 0, this.content.length);
	        System.arraycopy(newLine, 0, combinedContent, this.content.length, newLine.length);
	        System.arraycopy(new_content, 0, combinedContent, this.content.length + newLine.length, new_content.length);
	        this.content = combinedContent;
	    }

	}
	
	
	
	
}
