package com.common.httputils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.Header;

/**
 * 
 * @author lindezhi
 * 2016年5月28日 上午10:17:57
 */
public class HttpResponseMeta {
	
	private int statusCode;
	private String encode;
	private byte[] response;
	private String contentType;
	
	private Header[] headers;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	public String getResponseAsString() {
		if (response != null) {
			if (encode != null) {
				try {
					return new String(response, encode);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					return null;
				}
			} else {
				return new String(response);
			}
		} else {
			return null;
		}
	}
	
	public long getResponseSize(){
		return response.length;
	}
	
	public InputStream getResponseAsInputStream(){
		return new ByteArrayInputStream(response);
	}

	public void setResponse(byte[] response) {
		this.response = response;
	}

	public byte[] getResponseAsBytes() {
		return response;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Header[] getHeaders() {
		return headers;
	}

	public void setHeaders(Header[] headers) {
		this.headers = headers;
	}
}
