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
public class FeedsController {

	private static final Logger logger = Logger.getLogger(FeedsController.class);
	
	@Autowired
	CookiesProcessor cookiesProcessor;
	
	
	
	@RequestMapping(value = "/feed", method = RequestMethod.GET)
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
        params.put("offset", "0");
        params.put("limit", "10");
        params.put("community_id", String.valueOf(user.getNumber("community_id")));
        rc = new RestClient("http://", "127.0.01", 5000);
        api_response = rc.get("intra-post", params);
        logger.info("api_repsonse_posts "+api_response);
		JSONArray posts = new JSONArray(api_response);
		logger.info("after conv posts "+posts);
		model.addAttribute("posts",posts);
		rc = new RestClient("http://", "127.0.01", 5000);
		params = new HashMap<>();
        params.put("community_id", String.valueOf(user.getNumber("community_id")));
        api_response = rc.get("not-communities",params);
        model.addAttribute("nearbyCommunities", new JSONArray(api_response));
        rc = new RestClient("http://", "127.0.01", 5000);
		params = new HashMap<>();
        params.put("community_id", String.valueOf(user.getNumber("community_id")));
        api_response = rc.get("getMembersCount",params);
        model.addAttribute("count", new JSONObject(api_response));
        rc = new RestClient("http://", "127.0.01", 5000);
		params = new HashMap<>();
        params.put("community_id", String.valueOf(user.getNumber("community_id")));
        api_response = rc.get("getCommunityById",params);
        model.addAttribute("communityObj", new JSONArray(api_response));
		return "home";
	}
	
	@RequestMapping(value = "/category-feed", method = RequestMethod.GET)
	public String categoryFeed(HttpServletRequest request, Model model) throws Exception{
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
        params.put("offset", "0");
        params.put("limit", "10");
        params.put("category", request.getParameter("category"));
        params.put("community_id", String.valueOf(user.getNumber("community_id")));
        rc = new RestClient("http://", "127.0.01", 5000);
        api_response = rc.get("intra-post", params);
        logger.info("api_repsonse_posts "+api_response);
		JSONArray posts = new JSONArray(api_response);
		logger.info("after conv posts "+posts);
		model.addAttribute("posts",posts);
		rc = new RestClient("http://", "127.0.01", 5000);
		params = new HashMap<>();
        params.put("community_id", String.valueOf(user.getNumber("community_id")));
        api_response = rc.get("not-communities",params);
        model.addAttribute("nearbyCommunities", new JSONArray(api_response));
        model.addAttribute("category",request.getParameter("category"));
        rc = new RestClient("http://", "127.0.01", 5000);
		params = new HashMap<>();
        params.put("community_id", String.valueOf(user.getNumber("community_id")));
        api_response = rc.get("getMembersCount",params);
        model.addAttribute("count", new JSONObject(api_response));
        rc = new RestClient("http://", "127.0.01", 5000);
		params = new HashMap<>();
        params.put("community_id", String.valueOf(user.getNumber("community_id")));
        api_response = rc.get("getCommunityById",params);
        model.addAttribute("communityObj", new JSONArray(api_response));
		return "category-feed";
	}
	
	@RequestMapping(value = "/category-feed-ajax", method = RequestMethod.GET)
	public String categoryFeedAjax(HttpServletRequest request, Model model) throws Exception{
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
        params.put("offset", request.getParameter("offset"));
        params.put("limit", "10");
        params.put("category", request.getParameter("category"));
        
        params.put("community_id", String.valueOf(user.getNumber("community_id")));
        rc = new RestClient("http://", "127.0.01", 5000);
        api_response = rc.get("intra-post", params);
        logger.info("api_repsonse_posts "+api_response);
		JSONArray posts = new JSONArray(api_response);
		logger.info("after conv posts "+posts);
		model.addAttribute("posts",posts);
		model.addAttribute("newOffset",String.valueOf(Integer.valueOf(request.getParameter("offset"))+10));
		model.addAttribute("category",request.getParameter("category"));
		return "category-posts-ajax";
	}
	
	@RequestMapping(value = "/my-posts", method = RequestMethod.GET)
	public String myPosts(HttpServletRequest request, Model model)  throws Exception{
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
        params.put("offset", "0");
        params.put("limit", "10");
        params.put("category", request.getParameter("category"));
        params.put("user_id", String.valueOf(user.getNumber("id")));
        rc = new RestClient("http://", "127.0.01", 5000);
        api_response = rc.get("intrapost-user-id", params);
        logger.info("api_repsonse_posts "+api_response);
		JSONArray posts = new JSONArray(api_response);
		logger.info("after conv posts "+posts);
		model.addAttribute("posts",posts);
		rc = new RestClient("http://", "127.0.01", 5000);
		params = new HashMap<>();
        params.put("community_id", String.valueOf(user.getNumber("community_id")));
        api_response = rc.get("not-communities",params);
        model.addAttribute("nearbyCommunities", new JSONArray(api_response));
        model.addAttribute("category",request.getParameter("category"));
        rc = new RestClient("http://", "127.0.01", 5000);
		params = new HashMap<>();
        params.put("community_id", String.valueOf(user.getNumber("community_id")));
        api_response = rc.get("getMembersCount",params);
        model.addAttribute("count", new JSONObject(api_response));
        rc = new RestClient("http://", "127.0.01", 5000);
		params = new HashMap<>();
        params.put("community_id", String.valueOf(user.getNumber("community_id")));
        api_response = rc.get("getCommunityById",params);
        model.addAttribute("communityObj", new JSONArray(api_response));
		//model.addAttribute("newOffset",String.valueOf(Integer.valueOf(request.getParameter("offset"))+10));
		return "my-posts";
	}
	
	@RequestMapping(value = "/my-inter-posts", method = RequestMethod.GET)
	public String myInterPosts(HttpServletRequest request, Model model)  throws Exception{
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
        params.put("offset", "0");
        params.put("limit", "10");
        params.put("category", request.getParameter("category"));
        params.put("user_id", String.valueOf(user.getNumber("id")));
        rc = new RestClient("http://", "127.0.01", 5000);
        api_response = rc.get("interpost-user-id", params);
        logger.info("api_repsonse_posts "+api_response);
		JSONArray posts = new JSONArray(api_response);
		logger.info("after conv posts "+posts);
		model.addAttribute("posts",posts);
		rc = new RestClient("http://", "127.0.01", 5000);
		params = new HashMap<>();
        params.put("community_id", String.valueOf(user.getNumber("community_id")));
        api_response = rc.get("not-communities",params);
        model.addAttribute("nearbyCommunities", new JSONArray(api_response));
        model.addAttribute("category",request.getParameter("category"));
        rc = new RestClient("http://", "127.0.01", 5000);
		params = new HashMap<>();
        params.put("community_id", String.valueOf(user.getNumber("community_id")));
        api_response = rc.get("getMembersCount",params);
        model.addAttribute("count", new JSONObject(api_response));
        rc = new RestClient("http://", "127.0.01", 5000);
		params = new HashMap<>();
        params.put("community_id", String.valueOf(user.getNumber("community_id")));
        api_response = rc.get("getCommunityById",params);
        model.addAttribute("communityObj", new JSONArray(api_response));
        model.addAttribute("my-inter-posts","true");
		//model.addAttribute("newOffset",String.valueOf(Integer.valueOf(request.getParameter("offset"))+10));
		return "my-posts";
	}
	
	
	@RequestMapping(value = "/feed-ajax", method = RequestMethod.GET)
	public String feedAjax(HttpServletRequest request, Model model) throws Exception{
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
        params.put("offset", request.getParameter("offset"));
        params.put("limit", "10");
        params.put("community_id", String.valueOf(user.getNumber("community_id")));
        rc = new RestClient("http://", "127.0.01", 5000);
        api_response = rc.get("intra-post", params);
        logger.info("api_repsonse_posts "+api_response);
		JSONArray posts = new JSONArray(api_response);
		logger.info("after conv posts "+posts);
		model.addAttribute("posts",posts);
		model.addAttribute("newOffset",String.valueOf(Integer.valueOf(request.getParameter("offset"))+10));
		return "posts-ajax";
	}
	
	@RequestMapping(value = "/post-comment", method = RequestMethod.POST)
	public String postComment(HttpServletRequest request, Model model) throws Exception{
		String token="";
		try{
			token = cookiesProcessor.getCookiesObject(request);
		}
		catch(Exception e){
			e.printStackTrace();
			return "redirect:/sign-in";
		}
		JsonObject js = new JsonObject();
		js.addProperty("content",request.getParameter("cmt"));
		js.addProperty("postId",request.getParameter("post_id"));
		js.addProperty("token",token);
		RestClient rc = new RestClient("http://", "127.0.01", 5000);
		String api_response = rc.postJson("comment", js);
		JSONObject res = new JSONObject(api_response);
		if (!res.getBoolean("success")){
			throw new Exception();
		}
		model.addAttribute("cmt_id", res.getInt("id"));
		rc = new RestClient("http://", "127.0.01", 5000);
		api_response = rc.postJson("decode", js);
		logger.info("post-upate "+api_response);
		res = new JSONObject(api_response);
		JSONObject user = res.getJSONObject("user");
		model.addAttribute("cmt", request.getParameter("cmt"));
		model.addAttribute("user", user);
		return "comment-success";
	}
	
	@RequestMapping(value = "/get-comment", method = RequestMethod.GET)
	public String getComments(HttpServletRequest request, Model model) throws Exception{
		String token="";
		try{
			token = cookiesProcessor.getCookiesObject(request);
		}
		catch(Exception e){
			e.printStackTrace();
			return "redirect:/sign-in";
		}
		JsonObject js = new JsonObject();
		
		Map<String, String> params = new HashMap<>();
        params.put("post_id", request.getParameter("post_id"));
        RestClient rc = new RestClient("http://", "127.0.01", 5000);
        String api_response = rc.get("getCommentsByPostId", params);
        logger.info("api_repsonse_posts "+api_response);
		JSONArray comments = new JSONArray(api_response);
		logger.info("after conv posts "+comments);
		model.addAttribute("comments",comments);
		return "comments";
	}

	@RequestMapping(value = "/post-update", method = RequestMethod.POST)
	public String postUpdate(HttpServletRequest request, Model model) throws Exception{
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
		js =  new  JsonObject();
		js.addProperty("title", request.getParameter("subject"));
		js.addProperty("content", request.getParameter("message"));
		js.addProperty("communityId", user.getNumber("community_id"));
		js.addProperty("category", request.getParameter("category"));
		js.addProperty("image_ids", request.getParameter("image_ids"));
		js.addProperty("token",token);
		rc = new RestClient("http://", "127.0.01", 5000);
		api_response = rc.postJson("intra-post", js);
		res = new JSONObject(api_response);
		model.addAttribute("response",res);
		return "ajax-response";
	}
	
	@RequestMapping(value = "/post-inter-post", method = RequestMethod.POST)
	public String interPostUpdate(HttpServletRequest request, Model model) throws Exception{
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
		js =  new  JsonObject();
		js.addProperty("title", request.getParameter("subject"));
		js.addProperty("content", request.getParameter("message"));
		js.addProperty("communityId", user.getNumber("community_id"));
		js.addProperty("category", request.getParameter("category"));
		js.addProperty("communityIds", request.getParameter("communityIds"));
		js.addProperty("image_ids", request.getParameter("image_ids"));
		js.addProperty("token",token);
		rc = new RestClient("http://", "127.0.01", 5000);
		api_response = rc.postJson("inter-post", js);
		res = new JSONObject(api_response);
		model.addAttribute("response",res);
		return "ajax-response";
	}

}
