package com.idega.block.websearch.bean;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("googleBean")
@Scope("request")
public class GoogleBean {

	private String apiKey;

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}	
}