package com.smartnetadmin.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.JsonObject;
import com.smartnet.grpc.SmartnetGrpc.SmartnetBlockingStub;
import com.smartnet.grpc.SmartnetOuterClass.BooleanResponse;
import com.smartnet.grpc.SmartnetOuterClass.Community;
import com.smartnet.grpc.SmartnetOuterClass.Community.Builder;
import com.smartnetadmin.web.pojo.AdminDetails;
import com.smartnetadmin.web.utils.CookiesProcessor;
import com.smartnetadmin.web.utils.RestClient;

@Controller
public class CommunityAdminController {

	@Autowired
	SmartnetBlockingStub grpcClient;
	@Autowired
	CookiesProcessor cookiesProcessor;

	private static final Logger LOGGER = LoggerFactory.getLogger(CommunityAdminController.class);

	@RequestMapping(value = "/create-community", method = RequestMethod.POST)
	public String dashboard(HttpServletRequest request, Model model) throws Exception{
		model.addAttribute("appContextPath", request.getContextPath());
		AdminDetails adminDetails = cookiesProcessor.getCookiesObject(request);
		Builder builder = Community.newBuilder();
		builder.setName(request.getParameter("community_name"));
		builder.setCity(request.getParameter("community_city"));
		builder.setStreetAddress(request.getParameter("community_address"));
		builder.setState(request.getParameter("community_state"));
		builder.setCountry(request.getParameter("community_country"));
		builder.setCreatedBy(Integer.valueOf(adminDetails.getId()));
		builder.setZipcode(request.getParameter("community_zip"));
		builder.setLat(request.getParameter("community_lat"));
		builder.setLon(request.getParameter("community_lon"));
		builder.setPlaceId(request.getParameter("community_place_id"));
		builder.setType(Integer.valueOf(request.getParameter("community_type")));
		Community community = builder.build();
		BooleanResponse response = grpcClient.createCommunity(community);
		JsonObject js = new JsonObject();
		if (response.getResult()){
			js.addProperty("result", true);
			js.addProperty("message", "Community created successfully");
		}
		else{
			js.addProperty("result", false);
			js.addProperty("message", "Community already created");
		}
		model.addAttribute("response", js);
		return "ajax-response";
	}

	@RequestMapping(value = "/create-community", method = RequestMethod.GET)
	public String contactUs(HttpServletRequest request, Model model) throws Exception{
		model.addAttribute("appContextPath", request.getContextPath());
		return "create-community";
	}

	@RequestMapping(value="/community-admin/mark-as-active",method= RequestMethod.POST)
	public String markasactive(HttpServletRequest request, Model model) throws Exception{
		String c_email = request.getParameter("communityAdminEmail");
		String c_id = request.getParameter("communityId");
		RestClient rc = new RestClient("http://", "127.0.01", 5000);
		Map<String, String> params = new HashMap<>();
		params.put("community_id", c_id);
		params.put("community_admin_email", c_email);
		String api_response = rc.get("assign-c-admin",params);
		JsonObject js = new JsonObject();
		JSONObject res = new JSONObject(api_response);
		js.addProperty("result", true);
		js.addProperty("message",res.getString("msg") );
		model.addAttribute("response", js);
		return "ajax-response";
	}

	@RequestMapping(value="/community-admin/mark-as-active-again",method= RequestMethod.POST)
	public String markpausedadian(HttpServletRequest request, Model model) throws Exception{
		String c_id = request.getParameter("communityId");
		RestClient rc = new RestClient("http://", "127.0.01", 5000);
		Map<String, String> params = new HashMap<>();
		params.put("community_id", c_id);
		String api_response = rc.get("activate-community",params);
		JSONObject res = new JSONObject(api_response);
		JsonObject js = new JsonObject();
		js.addProperty("result", true);
		js.addProperty("message", res.getString("msg"));
		model.addAttribute("response", js);
		return "ajax-response";
	}

	@RequestMapping(value="/pending-users",method= RequestMethod.GET)
	public String getPendingUsers(HttpServletRequest request, Model model) throws Exception{
		AdminDetails adminDetails = cookiesProcessor.getCookiesObject(request);
		RestClient rc = new RestClient("http://", "127.0.01", 5000);
		Map<String, String> params = new HashMap<>();
		params.put("admin_id", adminDetails.getId());
		String api_response = rc.get("pending-users",params);
		JSONArray resarr = new JSONArray(api_response); 
		model.addAttribute("pendingUsers", resarr);
		return "pending-users";
	}
	
	@RequestMapping(value="/activate-user",method= RequestMethod.POST)
	public String activateUser(HttpServletRequest request, Model model) throws Exception{
		AdminDetails adminDetails = cookiesProcessor.getCookiesObject(request);
		RestClient rc = new RestClient("http://", "127.0.01", 5000);
		Map<String, String> params = new HashMap<>();
		params.put("user_id", request.getParameter("user_id"));
		String api_response = rc.get("activate-user",params);
		JSONObject res = new JSONObject(api_response);
		JsonObject js = new JsonObject();
		js.addProperty("result", true);
		js.addProperty("message", res.getString("msg"));
		model.addAttribute("response", js);
		return "ajax-response";
	}
	
	@RequestMapping(value="/pending-inter-post",method= RequestMethod.GET)
	public String pio(HttpServletRequest request, Model model) throws Exception{
		AdminDetails adminDetails = cookiesProcessor.getCookiesObject(request);
		RestClient rc = new RestClient("http://", "127.0.01", 5000);
		Map<String, String> params = new HashMap<>();
		params.put("user_id",  adminDetails.getId());
		String api_response = rc.get("get-pending-inter-post",params);
		JSONArray resarr = new JSONArray(api_response); 
		model.addAttribute("pendingUsers", resarr);
		return "pending-ip";
	}
	
	@RequestMapping(value="/activate-ip",method= RequestMethod.POST)
	public String activateIp(HttpServletRequest request, Model model) throws Exception{
		AdminDetails adminDetails = cookiesProcessor.getCookiesObject(request);
		RestClient rc = new RestClient("http://", "127.0.01", 5000);
		Map<String, String> params = new HashMap<>();
		params.put("id", request.getParameter("id"));
		String api_response = rc.get("activate-ip",params);
		JSONObject res = new JSONObject(api_response);
		JsonObject js = new JsonObject();
		js.addProperty("result", true);
		js.addProperty("message", res.getString("msg"));
		model.addAttribute("response", js);
		return "ajax-response";
	}
}
