package com.hit.server;

import java.io.Serializable;
import java.util.Map;

public class Request<T> extends Object implements Serializable {

	private static final long serialVersionUID = 2L;
	
	Map<String, String> headers;
	T body;

	public Request(Map<String, String> headers, T body) {
		this.headers = headers;
		this.body = body;
	}
	
	public Map<String, String> getHeaders(){
		
		return headers;
	}
	
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	public T getBody() {
		return body;
	}
	
	public void setBody(T body) {
		this.body = body;
	}
	
	public String toString(){
		
		return headers.toString() + " " + body.toString();
	}
}
