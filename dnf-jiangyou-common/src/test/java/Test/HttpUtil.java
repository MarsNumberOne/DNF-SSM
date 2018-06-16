package Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtil {

//	public static Logger logger = Logger.getLogger(HttpUtil.class);

	/**
	 * Http请求超时设置
	 */
	public static int HTTPCLIENT_CONNECT_TIMEOUT = 3 * 1000;

	/**
	 * Http读取超时设置
	 */
	public static int HTTPCLIENT_SO_TIMEOUT = 3 * 1000;

	/**
	 * @param uri
	 * @return
	 */
	public static String getContent(String uri) {
		try {
			HttpClient client = genDefaultHttpClient();

			uri = uri.startsWith("http") ? uri : "http://" + uri;
			HttpGet get = new HttpGet(uri);
			get.setConfig(genDefaultRequestConfig());
			HttpResponse response = client.execute(get);
			StatusLine line = response.getStatusLine();
			if (line.getStatusCode() == HttpStatus.SC_OK) {
				return EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (IOException e) {
//			logger.error(e);
		}

		return null;
	}

	public static HttpResponse sendPost(String uri, List<NameValuePair> param) {
		try {
			HttpClient client = genDefaultHttpClient();
			uri = uri.startsWith("http") ? uri : "http://" + uri;
			HttpPost post = new HttpPost(uri);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(20 * HTTPCLIENT_CONNECT_TIMEOUT)
					.setConnectTimeout(20 * HTTPCLIENT_SO_TIMEOUT).build();
			post.setConfig(requestConfig);
			post.setEntity(new UrlEncodedFormEntity(param, Charset.forName("UTF-8")));
			HttpResponse response = client.execute(post);
			return response;
		} catch (Exception e) {
//			logger.error("Send post error! URI : " + uri + ", param : " + JsonUtils.toJson(param), e);
		}
		return null;
	}

	public static String getPostResult(String url, List<NameValuePair> nameValuePairs) {
		HttpResponse resultResponse = sendPost(url, nameValuePairs);
		if (resultResponse == null) {
//			logger.error("[sendPost]post to[" + url + "] response is null.nameValuePair:" + nameValuePairs);
			return null;
		}
		String result = null;
		try {
			result = EntityUtils.toString(resultResponse.getEntity());
//			logger.info("[sendPost]post to[" + url + "].result:" + result);
		} catch (ParseException e) {
//			logger.error("[sendPost]post to[" + url + "] fail." + nameValuePairs, e);
		} catch (IOException e) {
//			logger.error("[sendPost]post to [" + url + "] fail." + nameValuePairs, e);
		}
		return result;
	}

	public static String sendHttpPostString(String uri, String param) throws ThirdException {
		try {
			HttpClient client = genDefaultHttpClient();
			uri = uri.startsWith("http") ? uri : "http://" + uri;
			HttpPost post = new HttpPost(uri);
			post.setConfig(genDefaultRequestConfig());
			post.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			post.setHeader("api-version", "2.0");
			ByteArrayEntity byteArrayEntity = new ByteArrayEntity(param.getBytes());
			post.setEntity(byteArrayEntity);
			HttpResponse response = client.execute(post);
			String result = EntityUtils.toString(response.getEntity());
			return result;
		} catch (Exception e) {
			throw new ThirdException("请求第三方异常,uri=" + uri + ",param:" + param, e);
		} 
	}

	public static HttpResponse sendHttpPostJson(String uri, String json) throws ThirdException {
		try {

			HttpClient client = genDefaultHttpClient();
			uri = uri.startsWith("http") ? uri : "http://" + uri;
			HttpPost post = new HttpPost(uri);
			post.setConfig(genDefaultRequestConfig());
			post.setHeader("Content-Type", "application/json");

			StringEntity stringEntity = new StringEntity(json, "UTF-8");
			stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_ENCODING, "UTF-8"));
			post.setEntity(stringEntity);

			HttpResponse response = client.execute(post);

			return response;

		} catch (Exception e) {
			throw new ThirdException("请求第三方异常,uri=" + uri + ",json:" + json, e);
		}
	}

	public static String sendHttpPostJsonResult(String uri, String json) throws ThirdException {
		try {

			HttpClient client = genDefaultHttpClient();
			uri = uri.startsWith("http") ? uri : "http://" + uri;
			HttpPost post = new HttpPost(uri);
			post.setConfig(genDefaultRequestConfig());
			post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			StringEntity stringEntity = new StringEntity(json, "UTF-8");
			stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_ENCODING, "UTF-8"));
			post.setEntity(stringEntity);

			HttpResponse response = client.execute(post);
			String result = EntityUtils.toString(response.getEntity());
			return result;

		} catch (Exception e) {
			throw new ThirdException("请求第三方异常,uri=" + uri + ",json:" + json, e);
		}
	}

	public static HttpResponse sendHttpPostXml(String uri, String xmlContent) {
		try {
			HttpClient client = genDefaultHttpClient();
			uri = uri.startsWith("http") ? uri : "http://" + uri;
			HttpPost post = new HttpPost(uri);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(20 * HTTPCLIENT_CONNECT_TIMEOUT)
					.setConnectTimeout(20 * HTTPCLIENT_SO_TIMEOUT).build();
			post.setConfig(requestConfig);
			post.setHeader("Content-Type", "application/x-www-form-urlencoded");
			post.setEntity(new StringEntity(xmlContent));
			HttpResponse response = client.execute(post);
			return response;
		} catch (Exception e) {
//			logger.error(e);
		}
		return null;
	}

	// public static HttpResponse sendHttpPostXml(String uri,
	// List<NameValuePair> nameValuePairs){
	// try {
	// HttpClient client = genDefaultHttpClient();
	// uri = uri.startsWith("http") ? uri : "http://" + uri;
	// HttpPost post = new HttpPost(uri);
	// RequestConfig requestConfig = RequestConfig.custom()
	// .setSocketTimeout(10 * HTTPCLIENT_CONNECT_TIMEOUT)
	// .setConnectTimeout(10 * HTTPCLIENT_SO_TIMEOUT)
	// .build();
	// post.setConfig(requestConfig);
	// post.setHeader("Content-Type", "application/x-www-form-urlencoded");
	// post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	// HttpResponse response = client.execute(post);
	// return response;
	// } catch (Exception e) {
	// logger.error(e);
	// }
	// return null;
	// }

	public static HttpResponse sendHttpsPostJson(String uri, String json) {
		try {
			HttpClient client = genDefaultHttpClient();
			uri = uri.startsWith("https") ? uri : "https://" + uri;
			HttpPost post = new HttpPost(uri);
			post.setConfig(genDefaultRequestConfig());
			post.setHeader("Content-Type", "application/json");
			post.setEntity(new StringEntity(json));

			HttpResponse response = client.execute(post);

			return response;
		} catch (Exception e) {
//			logger.error(e);
		}
		return null;
	}

	public static String sendHttpsPostJsonResult(String uri, String json) {
		try {
			HttpClient client = genDefaultHttpClient();
			HttpPost post = new HttpPost(uri);
			post.setConfig(genDefaultRequestConfig());
			post.setHeader("Content-Type", "application/json; charset=utf-8");
			StringEntity stringEntity = new StringEntity(json, "UTF-8");
			stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_ENCODING, "UTF-8"));
			post.setEntity(stringEntity);

			HttpResponse response = client.execute(post);
			String result = EntityUtils.toString(response.getEntity());
			return result;
		} catch (Exception e) {
//			logger.error(e);
		}
		return null;
	}

	public static HttpResponse sendGet(String uri) {
		try {
			HttpClient client = genDefaultHttpClient();
			uri = uri.startsWith("http") ? uri : "http://" + uri;
			HttpGet get = new HttpGet(uri);
			get.setConfig(genDefaultRequestConfig());
			HttpResponse response = client.execute(get);
			return response;
		} catch (IOException e) {
//			logger.error(e);
		}

		return null;
	}

	public static String getGetResult(String uri) {
		String result = null;
		try {
			HttpClient client = genDefaultHttpClient();
			uri = uri.startsWith("http") ? uri : "http://" + uri;
			HttpGet get = new HttpGet(uri);
			get.setConfig(genDefaultRequestConfig());
			HttpResponse response = client.execute(get);
			result = EntityUtils.toString(response.getEntity());
			return result;
		} catch (ParseException e) {
//			logger.error(e);
		} catch (IOException e) {
//			logger.error(e);
		}

		return result;
	}
	
	public static String getGetResult(String uri, Map<String, String>header) {
		String result = null;
		try {
			HttpClient client = genDefaultHttpClient();
			uri = uri.startsWith("http") ? uri : "http://" + uri;
			HttpGet get = new HttpGet(uri);
			get.setConfig(genDefaultRequestConfig());
			if (null != header) {
				for (String name : header.keySet()) {
					get.setHeader(name, header.get(name));
				}
			}
			HttpResponse response = client.execute(get);
			result = EntityUtils.toString(response.getEntity());
			return result;
		} catch (ParseException e) {
//			logger.error(e);
		} catch (IOException e) {
//			logger.error(e);
		}

		return result;
	}
	
	public static String getShortUrl(String url_long) {
		Map<String, String> header = new HashMap<String, String>();
		header.put("Referer","http://surl.sinaapp.com/");
		String response = getGetResult("http://gzbusnow.duapp.com/surl/surl_proxy.php?source=1681459862&callback=jsonpshorturl123&url_long=" + URLEncoder.encode(url_long), header );
		if (null != response) {
			int length = "jsonpshorturl123(".length();
			if (response.length() > length + 2) {
				response = response.substring(length, response.length() - 2);
			}
		}
		String url_short = "";
		JSONObject jsonObject = JSONObject.parseObject(response);
		JSONArray jsonArray = jsonObject.getJSONArray("urls");
		for (Object object : jsonArray) {
			JSONObject rs = (JSONObject)object;
			if (StringUtils.isNotBlank(rs.getString("url_short"))) {
				url_short=rs.getString("url_short");
			}
		}
		if (StringUtils.isBlank(url_short)) {
			return url_long;
		}
		return url_short;
	}

	/**
	 * @return
	 */
	public static HttpClient genDefaultHttpClient() {
		HttpClient httpClient = HttpClients.createDefault();
		return httpClient;
	}

	public static RequestConfig genDefaultRequestConfig() {
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2 * HTTPCLIENT_SO_TIMEOUT)
				.setConnectTimeout(2 * HTTPCLIENT_CONNECT_TIMEOUT).build();
		return requestConfig;
	}

	public static void main(String[] args) {
		System.out.println(getShortUrl("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=object%E8%BD%ACjson%E7%9A%84%E6%96%B9%E6%B3%95&rsv_pq=898b79f50002600c&rsv_t=3f92vL3Ts6EWUQ8RdyyWjmvMrkvk783DBN2%2FEyUzsbPzy7kuEtiCh%2BdEJlM&rqlang=cn&rsv_enter=1&sug=object&inputT=3720&rsv_sug3=9&rsv_sug2=0&rsv_sug4=4298&rsv_jmp=slow"));
	
	}
}
