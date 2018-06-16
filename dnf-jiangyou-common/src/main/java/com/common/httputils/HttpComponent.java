package com.common.httputils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;

import com.alibaba.fastjson.JSONObject;

/**
 * 发送http请求组件
 * 
 * @author lindezhi 2016年5月28日 上午10:20:21
 */
public class HttpComponent {

	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	private static final int EOF = -1;

	protected final Log logger = LogFactory.getLog(getClass());

	public static final Map<String, String> HEAD_JSON = new HashMap<String, String>();

	private int defaultTimeout = 300000; // 30s

	private int longConnectionTimeout = 300000;// 5 min

	private PoolingHttpClientConnectionManager connectionManager;

	private CloseableHttpClient defaultHttpClient;

	private CloseableHttpClient longHttpClient;

	private DynamicProxyRoutePlanner routePlanner;

	private boolean needProxy;

	static {
		HEAD_JSON.put("Content-Type", "application/json");
		HEAD_JSON.put("Accept", "application/json");
	}

	private HttpClient getInstance() {
		return defaultHttpClient;
	}

	private HttpClient getLongHttpClient() {
		return longHttpClient;
	}

	public HttpComponent() {
		this(200, 50, false);
	}

	public HttpComponent(boolean needProxy) {
		this(200, 50, needProxy);
	}

	public HttpComponent(int connectionMax, int maxPerRoute) {
		this(connectionMax, maxPerRoute, false);
	}

	public HttpComponent(int connectionMax, int maxPerRoute, boolean needProxy) {
		this.needProxy = needProxy;

		SSLContext sslcontext = createIgnoreVerifySSL();
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create().register("http", PlainConnectionSocketFactory.INSTANCE).register("https", new SSLConnectionSocketFactory(sslcontext)).build();
		connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		connectionManager.setMaxTotal(connectionMax);
		connectionManager.setDefaultMaxPerRoute(maxPerRoute);

		RequestConfig defaultConfig = RequestConfig.custom().setSocketTimeout(defaultTimeout).setConnectTimeout(defaultTimeout).build();
		RequestConfig longConConfig = RequestConfig.custom().setSocketTimeout(longConnectionTimeout).setConnectTimeout(longConnectionTimeout).build();

//		if (needProxy) {
//			HttpHost proxy = IpUtils.httpHost();
//			routePlanner = new DynamicProxyRoutePlanner(proxy);
//		}

		defaultHttpClient = HttpClients.custom().setDefaultRequestConfig(defaultConfig).setConnectionManager(connectionManager).setRoutePlanner(routePlanner).build();
		longHttpClient = HttpClients.custom().setDefaultRequestConfig(longConConfig).setConnectionManager(connectionManager).setRoutePlanner(routePlanner).build();
	}

	public static SSLContext createIgnoreVerifySSL() {  
	    SSLContext sc = null;
		try {
			sc = SSLContext.getInstance("SSLv3");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}  
	  
	    // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法  
	    X509TrustManager trustManager = new X509TrustManager() {  
	        @Override
	        public void checkClientTrusted(  
	                java.security.cert.X509Certificate[] paramArrayOfX509Certificate,  
	                String paramString) throws CertificateException {  
	        }  
	  
	        @Override
	        public void checkServerTrusted(  
	                java.security.cert.X509Certificate[] paramArrayOfX509Certificate,  
	                String paramString) throws CertificateException {  
	        }  
	  
	        @Override
	        public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
	            return null;  
	        }  
	    };  
	  
	    try {
			sc.init(null, new TrustManager[] { trustManager }, null);
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}  
	    return sc;
	}

	public void close() {
		connectionManager.close();
		try {
			defaultHttpClient.close();
			longHttpClient.close();
		} catch (IOException e) {
			logger.error("[HTTP] close error", e);
		}
	}

	private String defaultEncoding = "utf-8";

	private String parseURL(String url) {
		if (url != null) {
			if (url.startsWith("http")) {
				return url;
			} else {
				return "http://" + url;
			}
		} else {
			return null;
		}
	}

	public String encodeParams(Map<String, Object> params) {
		StringBuilder sb = new StringBuilder();
		if (params != null) {
			Set<String> keys = params.keySet();
			int first = 0;
			for (String key : keys) {
				Object value = params.get(key);
				if (first > 0) {
					sb.append("&");
				}
				first++;
				sb.append(key);
				sb.append("=");
				String v = String.valueOf(value);
				try {
					String encodeValue = URLEncoder.encode(v, defaultEncoding);
					sb.append(encodeValue);
				} catch (UnsupportedEncodingException e) {
					logger.error("UnsupportedEncoding:" + defaultEncoding);
				}
			}
		}
		return sb.toString();
	}

	private void setHeaders(HttpRequestBase request, Map<String, String> headers) {
//		if (needProxy && routePlanner != null) {
//			routePlanner.setProxy(IpUtils.httpHost());
//		}
		if (request != null && headers != null) {
			Set<String> keys = headers.keySet();
			for (String key : keys) {
				String value = headers.get(key);
				request.setHeader(key, value);
			}
		}
	}

	private String getEncoding(String contentType) {
		if (contentType != null) {
			String[] strs = contentType.split(";");
			if (strs != null && strs.length > 1) {
				String charSet = strs[1].trim();
				String[] charSetKeyValues = charSet.split("=");
				if (charSetKeyValues.length == 2 && charSetKeyValues[0].equalsIgnoreCase("charset")) {
					return charSetKeyValues[1];
				}
			}
		}
		return defaultEncoding;
	}

	public HttpResponseMeta getResponse(HttpResponse response) {
		if (response != null) {
			StatusLine line = response.getStatusLine();
			if (line != null) {
				HttpResponseMeta responseMeta = new HttpResponseMeta();
				int code = line.getStatusCode();
				responseMeta.setStatusCode(code);
				Header[] headers = response.getHeaders("Set-Cookie");
				responseMeta.setHeaders(headers);
				
				if (code < 500 && code != 204) {
					try {
						InputStream inputStream = response.getEntity().getContent();
						if (inputStream != null) {
							byte[] bs = this.toByteArray(inputStream);
							responseMeta.setResponse(bs);
							Header contentType = response.getEntity().getContentType();
							if (null != contentType) {
								responseMeta.setContentType(contentType.getValue());
								responseMeta.setEncode(getEncoding(contentType.getValue()));
							}
						}
					} catch (ClientProtocolException e) {
						this.handleNetException(e);
					} catch (IOException e) {
						this.handleNetException(e);
					}
				}
				return responseMeta;
			}
		}
		throw new HttpException("http response null");
	}

	private void setBodyParameters(HttpEntityEnclosingRequestBase request, Map<String, Object> params) throws UnsupportedEncodingException {
		if (params != null) {
			List<NameValuePair> list = new LinkedList<NameValuePair>();
			Set<String> keys = params.keySet();
			for (String key : keys) {
				Object v = params.get(key);
				if (v != null) {
					list.add(new BasicNameValuePair(key, params.get(key).toString()));
				}
			}
			HttpEntity entity = new UrlEncodedFormEntity(list);
			request.setEntity(entity);
		}
	}

	public HttpResponseMeta httpPut(String url, Map<String, String> headers, Map<String, Object> params) {
		String newUrl = parseURL(url);
		HttpPut put = new HttpPut(newUrl);
		setHeaders(put, headers);
		HttpClient client = getInstance();
		try {
			this.setBodyParameters(put, params);
			HttpResponse response = client.execute(put);
			return getResponse(response);
		} catch (ClientProtocolException e) {
			this.handleNetException(e);
		} catch (IOException e) {
			this.handleNetException(e);
		}
		throw new HttpException("http " + url + " response null");
	}

	public HttpResponseMeta httpPut(String url, Map<String, Object> urlParams, Map<String, String> headers, String body) {
		String newUrl = parseURL(url);
		if (newUrl == null) {
			return null;
		}
		if (urlParams != null) {
			newUrl = newUrl + "?" + encodeParams(urlParams);
		}
		HttpPut put = new HttpPut(newUrl);
		setHeaders(put, headers);
		HttpClient client = getInstance();
		try {
			if (body != null) {
				StringEntity entity = new StringEntity(body, "utf-8");
				put.setEntity(entity);
			}
			HttpResponse response = client.execute(put);
			return getResponse(response);
		} catch (ClientProtocolException e) {
			this.handleNetException(e);
		} catch (IOException e) {
			this.handleNetException(e);
		}
		throw new HttpException("http " + url + " response null");
	}

	public HttpResponseMeta httpPut(String url, Map<String, String> headers, String body) {
		String newUrl = parseURL(url);
		HttpPut put = new HttpPut(newUrl);
		setHeaders(put, headers);
		HttpClient client = getInstance();
		try {
			if (body != null) {
				StringEntity entity = new StringEntity(body, "utf-8");
				put.setEntity(entity);
			}
			HttpResponse response = client.execute(put);
			return getResponse(response);
		} catch (ClientProtocolException e) {
			this.handleNetException(e);
		} catch (IOException e) {
			this.handleNetException(e);
		}
		throw new HttpException("http " + url + " response null");
	}

	public HttpResponseMeta httpPost(String url, Map<String, String> headers, String... pairs) {
		Map<String, Object> params = arrayToMap(pairs);
		return httpPost(url, headers, params);
	}

	private static Map<String, Object> arrayToMap(String... pairs) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (null != pairs && pairs.length % 2 == 0) {
			for (int i = 0; i < pairs.length; i += 2) {
				map.put((String) pairs[i], pairs[i + 1]);
			}
		}
		return map;
	}

	public HttpResponseMeta httpPost(String url, Map<String, String> headers, Map<String, Object> params) {
		System.out.println(JSONObject.toJSON(params));
		String newUrl = parseURL(url);
		HttpPost post = new HttpPost(newUrl);
		setHeaders(post, headers);
		HttpClient client = getInstance();
		try {
			this.setBodyParameters(post, params);
			HttpResponse response = client.execute(post);
			return getResponse(response);
		} catch (ClientProtocolException e) {
			this.handleNetException(e);
		} catch (IOException e) {
			this.handleNetException(e);
		}
		throw new HttpException("http " + url + " response null");
	}

	public HttpResponseMeta httpPost(String url, Map<String, Object> urlParams, Map<String, String> headers, String body) {
		StringEntity entity = new StringEntity(body, "utf-8");
		return this.httpPost(url, urlParams, headers, entity);
	}

	public HttpResponseMeta httpPost(String url, Map<String, Object> urlParams, Map<String, String> headers, HttpEntity entity) {
		String newUrl = parseURL(url);
		if (newUrl == null) {
			return null;
		}
		if (urlParams != null) {
			newUrl = newUrl + "?" + encodeParams(urlParams);
		}
		HttpPost post = new HttpPost(newUrl);
		setHeaders(post, headers);
		HttpClient client = getInstance();
		try {
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			return getResponse(response);
		} catch (ClientProtocolException e) {
			this.handleNetException(e);
		} catch (IOException e) {
			this.handleNetException(e);
		}
		throw new HttpException("http " + url + " response null");
	}

	public HttpResponseMeta httpPost(String url, Map<String, String> headers, String body) {
		return this.httpPost(url, null, headers, body);
	}

	public HttpResponseMeta httpDelete(String url, Map<String, String> headers, Map<String, Object> params) {
		String newUrl = parseURL(url);
		if (newUrl == null) {
			return null;
		}
		if (params != null) {
			newUrl = newUrl + "?" + encodeParams(params);
		}
		HttpDelete delete = new HttpDelete(newUrl);
		setHeaders(delete, headers);
		HttpClient client = getInstance();
		try {
			HttpResponse response = client.execute(delete);
			return getResponse(response);
		} catch (ClientProtocolException e) {
			this.handleNetException(e);
		} catch (IOException e) {
			this.handleNetException(e);
		}
		throw new HttpException("http " + url + " response null");
	}

	private void handleNetException(Exception e) {
		logger.error("http exception:" + e.getMessage(), e);
		throw new HttpException(e);
	}

	public HttpResponseMeta httpGet(String url, Map<String, String> headers, Map<String, Object> params) {
		String newUrl = parseURL(url);
		if (newUrl == null) {
			return null;
		}
		if (params != null) {
			newUrl = newUrl + "?" + encodeParams(params);
		}
		HttpGet get = new HttpGet(newUrl);
		setHeaders(get, headers);
		HttpClient client = getInstance();
		try {
			HttpResponse response = client.execute(get);
			Header[] s = get.getAllHeaders(); 
			return getResponse(response);
		} catch (ClientProtocolException e) {
			this.handleNetException(e);
		} catch (IOException e) {
			this.handleNetException(e);
		}
		throw new HttpException("http " + url + " response null");
	}

	public HttpResponseMeta httpLongGet(String url, Map<String, String> headers, Map<String, Object> params) {
		String newUrl = parseURL(url);
		if (newUrl == null) {
			return null;
		}
		if (params != null) {
			newUrl = newUrl + "?" + encodeParams(params);
		}
		HttpGet get = new HttpGet(newUrl);
		setHeaders(get, headers);
		HttpClient client = getLongHttpClient();
		try {
			HttpResponse response = client.execute(get);
			return getResponse(response);
		} catch (ClientProtocolException e) {
			this.handleNetException(e);
		} catch (IOException e) {
			this.handleNetException(e);
		}
		throw new HttpException("http " + url + " response null");
	}

	public byte[] toByteArray(InputStream ins) throws IOException {
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			copy(ins, output);
			return output.toByteArray();
		} finally {
			ins.close();
		}
	}

	public int copy(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		long count = 0;
		int n = 0;
		while (EOF != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		if (count > Integer.MAX_VALUE) {
			return -1;
		}
		return (int) count;
	}

	public class DynamicProxyRoutePlanner implements HttpRoutePlanner {

		private DefaultProxyRoutePlanner defaultProxyRoutePlanner = null;

		public DynamicProxyRoutePlanner(HttpHost host) {
			defaultProxyRoutePlanner = new DefaultProxyRoutePlanner(host);
		}

		public void setProxy(HttpHost host) {
			if (null == host) {
				return;
			}
			defaultProxyRoutePlanner = new DefaultProxyRoutePlanner(host);
		}

		public void setProxy(String proxyIP, int hostPort) {
			this.setProxy(new HttpHost(proxyIP, hostPort));
		}

		@Override
		public HttpRoute determineRoute(HttpHost target, org.apache.http.HttpRequest request, HttpContext context) throws org.apache.http.HttpException {
			return defaultProxyRoutePlanner.determineRoute(target, request, context);
		}
	}
}
