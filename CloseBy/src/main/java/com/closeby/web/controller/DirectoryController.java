package com.closeby.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
public class DirectoryController {

	private static final Logger logger = Logger.getLogger(DirectoryController.class);
	
	@Autowired
	CookiesProcessor cookiesProcessor;

	@RequestMapping(value = "/directory", method = RequestMethod.GET)
	public String feed(HttpServletRequest request, Model model) throws Exception{
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
        params.put("community_id", String.valueOf(user.getNumber("community_id")));
        rc = new RestClient("http://", "127.0.01", 5000);
        api_response = rc.get("users", params);
        logger.info("api_repsonse_posts "+api_response);
		JSONArray users = new JSONArray(api_response);
		logger.info("after conv posts "+users);
		model.addAttribute("users",users);
		return "directory";
	}
	
	@RequestMapping(value = "/post-message", method = RequestMethod.POST)
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
		model.addAttribute("response",res);
		return "ajax-response";
	}


}
