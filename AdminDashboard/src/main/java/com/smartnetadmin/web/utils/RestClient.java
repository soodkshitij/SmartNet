package com.smartnetadmin.web.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import com.google.gson.JsonObject;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Set;



public class RestClient {

	private static final Logger logger = Logger.getLogger(RestClient.class);

	private CloseableHttpClient httpClient;
	private String hostName;
	private int port;
	private final String url;
	private String scheme;

	public RestClient(String scheme, String hostName, int port){
		this.setScheme(scheme);
		this.hostName = hostName;
		this.port = port;
		this.url = scheme==null ? "http://" : scheme  + hostName + ":" + port + "/";
		//this.headers = new HttpHeaders();
		this.httpClient = HttpClients.createDefault();
		//headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
	}

	public String getHostName() {
		return hostName;
	}
	public int getPort() {
		return port;
	}
	public String getUrl() {
		return url;
	}

	public String postJson(String uri, JsonObject json) throws IOException{
		String responseString="";
		try {
			logger.info("url tp call "+this.url+uri);
			HttpPost request = new HttpPost(this.url+uri);
			StringEntity params = new StringEntity(json.toString());
			request.addHeader("Accept", "application/json");
			request.addHeader("Content-type", "application/json");
			request.setEntity(params);
			CloseableHttpResponse res = httpClient.execute(request);
			responseString = this.toString(res.getEntity().getContent());
		} catch (Exception ex) {
			logger.error("error while posting "+ex);
		} finally {
			httpClient.close();
		}
		return responseString;
	}

	public String get(String uri, Map<String, String> params)
			throws Exception{
		String responseString="";
		try{
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + uri);
			Set<String> keys = params.keySet();
			for(String key : keys){
				builder.queryParam(key, params.get(key));
			}
			logger.info("builder "+url + uri);
			HttpGet request = new HttpGet(builder.build().encode().toUri());
			request.addHeader("Accept", "application/json");
			request.addHeader("Content-type", "text/html");
			CloseableHttpResponse response = httpClient.execute(request);
			responseString = this.toString(response.getEntity().getContent());
		}
		catch (Exception ex) {
			logger.error("error while geting "+ex);
		} finally {
			httpClient.close();
		}
		return responseString;
	}


	private String toString(InputStream inputStream){
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder responseString = new StringBuilder();
		String line = null;
		try {
			while((line = reader.readLine()) != null){
				responseString.append(line);
			}
			inputStream.close();
		} catch (IOException e) {
			throw new RuntimeException();
		}
		return responseString.toString();
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}
}