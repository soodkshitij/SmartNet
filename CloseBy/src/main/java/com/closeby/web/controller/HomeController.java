package com.closeby.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
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
public class HomeController {

	private static final Logger logger = Logger.getLogger(HomeController.class);
	
	@Autowired
	CookiesProcessor cookiesProcessor;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String loginPage(HttpServletRequest request, Model model) throws Exception{
		model.addAttribute("appContextPath", request.getContextPath());
		return "landing-page";
	}
	
	@RequestMapping(value = "/sign-in", method = RequestMethod.GET)
	public String signIn(HttpServletRequest request, Model model) throws Exception{
		model.addAttribute("appContextPath", request.getContextPath());
		return "sign-in";
	}
	
	
	
	@RequestMapping(value = "/sign-in", method = RequestMethod.POST)
	public String signInPost(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{
		model.addAttribute("appContextPath", request.getContextPath());
		JsonObject js = new JsonObject();
		js.addProperty("email", request.getParameter("email"));
		js.addProperty("password", request.getParameter("password"));
		RestClient rc = new RestClient("http://", "127.0.01", 5000);
		String api_response = rc.postJson("login", js);
		JSONObject res = new JSONObject(api_response);
		if (res.getBoolean("verified") && res.getString("token")!=null && res.getString("token").toString().length()!=0){
			addCookiesToResponse(res.getString("token"), res.getInt("community_id") ,request, response);
			return "redirect:/feed";
		}
		else{
			model.addAttribute("msg",res.getString("message"));
			return "sign-in";
		}
	}
	
	
	@RequestMapping(value = "/sign-up", method = RequestMethod.GET)
	public String signupDummy(HttpServletRequest request, Model model) throws Exception{
		return "redirect:/";
	}
	
	@RequestMapping(value = "/sign-up", method = RequestMethod.POST)
	public String signup(HttpServletRequest request, Model model) throws Exception{
		model.addAttribute("appContextPath", request.getContextPath());
		if (request.getParameter("address")==null || request.getParameter("address").trim().length()==0){
			return "redirect:/";
				
		}
		//community_place_id
		
		RestClient rc = new RestClient("http://", "127.0.01", 5000);
		Map<String, String> params = new HashMap<>();
        params.put("lat", request.getParameter("lat"));
        params.put("lon", request.getParameter("lon"));
        String api_response = rc.get("get-community-id", params);
        JSONObject res = new JSONObject(api_response);
        model.addAttribute("com_res",res);
		model.addAttribute("community_place_id",request.getParameter("community_place_id"));
		model.addAttribute("address",request.getParameter("address"));
		return "sign-up";
	}
	
	@RequestMapping(value = "/signup-user", method = RequestMethod.POST)
	public String signupUser(HttpServletRequest request, Model model) throws Exception{
		logger.info("inside signup-user");
		model.addAttribute("appContextPath", request.getContextPath());
		model.addAttribute("community_place_id",request.getParameter("community_place_id"));
		model.addAttribute("address",request.getParameter("address"));
		JsonObject js = new JsonObject();
		js.addProperty("firstName", request.getParameter("firstName"));
		js.addProperty("lastName", request.getParameter("lastName"));
		js.addProperty("phoneNumber", request.getParameter("phoneNumber"));
		js.addProperty("password", request.getParameter("password"));
		js.addProperty("email", request.getParameter("email"));
		js.addProperty("gender", request.getParameter("gender"));
		js.addProperty("community_id", request.getParameter("com_id"));
		RestClient rc = new RestClient("http://", "127.0.01", 5000);
		String response = rc.postJson("signup", js);
		logger.info("res "+response);
		System.out.println("Response is "+response);
		JSONObject res = new JSONObject(response);
		if (res.getBoolean("success")){
			return "pending-user";
		}
		else{
			model.addAttribute("msg",res.get("message"));
			return "sign-up";
		}
	}
	
	private void addCookiesToResponse(String token, int community_id ,HttpServletRequest request, HttpServletResponse response) {

		
		Cookie cookieName = new Cookie("token", token);
		cookieName.setDomain(request.getServerName());
		cookieName.setPath(request.getContextPath());
		
		response.addCookie(cookieName);
	}


}
