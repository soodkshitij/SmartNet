package com.closeby.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.closeby.services.RestClient;
import com.closeby.web.utils.CookiesProcessor;
import com.google.gson.JsonObject;




@Controller
public class InboxController {

	private static final Logger logger = Logger.getLogger(InboxController.class);
	
	@Autowired
	CookiesProcessor cookiesProcessor;

	@RequestMapping(value = "/inbox", method = RequestMethod.GET)
	public String loginPage(HttpServletRequest request, Model model) throws Exception{
		model.addAttribute("appContextPath", request.getContextPath());
		String token="";
		try{
			token = cookiesProcessor.getCookiesObject(request);
		}
		catch(Exception e){
			e.printStackTrace();
			return "redirect:/sign-in";
		}
		JsonObject js = new JsonObject();
		js.addProperty("token", token);
		RestClient rc = new RestClient("http://", "127.0.01", 5000);
		String api_response = rc.postJson("decode", js);
		logger.info("post-upate "+api_response);
		JSONObject res = new JSONObject(api_response);
		JSONObject user = res.getJSONObject("user");
		model.addAttribute("user",user);
		Map<String, String> params = new HashMap<>();
        params.put("to_user_id", String.valueOf(user.getInt("id")));
        rc = new RestClient("http://", "127.0.01", 5000);
        api_response = rc.get("message-users-list", params);
        logger.info("api_repsonse_posts "+api_response);
		JSONArray users = new JSONArray(api_response);
		logger.info("after conv posts "+users);
		model.addAttribute("users",users);
		return "inbox";
	}
	
	@RequestMapping(value = "/messages-from-user", method = RequestMethod.GET)
	public String messagesFromUser(HttpServletRequest request, Model model) throws Exception{
		model.addAttribute("appContextPath", request.getContextPath());
		String token="";
		try{
			token = cookiesProcessor.getCookiesObject(request);
		}
		catch(Exception e){
			e.printStackTrace();
			return "redirect:/sign-in";
		}
		JsonObject js = new JsonObject();
		js.addProperty("token", token);
		RestClient rc = new RestClient("http://", "127.0.01", 5000);
		String api_response = rc.postJson("decode", js);
		logger.info("post-upate "+api_response);
		JSONObject res = new JSONObject(api_response);
		JSONObject user = res.getJSONObject("user");
		model.addAttribute("user",user);
		Map<String, String> params = new HashMap<>();
		params.put("from_user_id", request.getParameter("from_user_id"));
        params.put("to_user_id", request.getParameter("to_user_id"));
        rc = new RestClient("http://", "127.0.01", 5000);
        api_response = rc.get("messages", params);
        logger.info("api_repsonse_posts "+api_response);
		JSONArray msgs = new JSONArray(api_response);
		logger.info("after conv posts "+msgs);
		model.addAttribute("msgs",msgs);
		return "messages-ajax";
	}
	
	@RequestMapping(value = "/post-message-ajax", method = RequestMethod.POST)
	public String postMessage(HttpServletRequest request, Model model) throws Exception{
		model.addAttribute("appContextPath", request.getContextPath());
		String token="";
		try{
			token = cookiesProcessor.getCookiesObject(request);
		}
		catch(Exception e){
			e.printStackTrace();
			return "redirect:/sign-in";
		}
		JsonObject js = new JsonObject();
		js.addProperty("token", token);
		RestClient rc = new RestClient("http://", "127.0.01", 5000);
		String api_response = rc.postJson("decode", js);
		logger.info("post-upate "+api_response);
		JSONObject res = new JSONObject(api_response);
		JSONObject user = res.getJSONObject("user");
		js = new JsonObject();
		js.addProperty("content", request.getParameter("content"));
		js.addProperty("from_user_id", user.getInt("id"));
		js.addProperty("to_user_id",Integer.valueOf(request.getParameter("to_user_id")));
		rc = new RestClient("http://", "127.0.01", 5000);
		String resp = rc.postJson("postMessage", js);
		res = new JSONObject(resp);
		model.addAttribute("content",request.getParameter("content"));
		return "message-ajax-response";
	}



}
