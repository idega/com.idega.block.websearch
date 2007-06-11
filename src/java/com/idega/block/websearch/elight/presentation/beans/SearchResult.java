package com.idega.block.websearch.elight.presentation.beans;

import org.w3c.dom.Document;

/**
 * 
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public class SearchResult {
	
	private String url;
	private String title;
	private Document contents;
	private String extraInfo;
	private String message;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Document getContents() {
		return contents;
	}
	public void setContents(Document contents) {
		this.contents = contents;
	}
	public String getExtraInfo() {
		return extraInfo;
	}
	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
