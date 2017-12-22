package com.smartnetadmin.web.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.smartnet.grpc.SmartnetGrpc.SmartnetBlockingStub;
import com.smartnet.grpc.SmartnetOuterClass.Admin;
import com.smartnet.grpc.SmartnetOuterClass.Admin.Builder;
import com.smartnetadmin.web.enums.UserType;
import com.smartnetadmin.web.pojo.AdminDetails;
import com.smartnetadmin.web.utils.CookiesProcessor;




@Controller
public class LoginController {

	@Autowired
	SmartnetBlockingStub grpcClient;
	@Autowired
	CookiesProcessor cookiesProcessor;

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);


	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(HttpServletRequest request, Model model) throws Exception{
		try{
			AdminDetails adminDetails = cookiesProcessor.getCookiesObject(request);
			model.addAttribute("adminDetails", adminDetails);
			return "redirect:/dashboard";
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return "login";
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request,HttpServletResponse response, Model model) throws Exception{
		try{
			cookiesProcessor.removeCookies(request, response);
			//model.addAttribute("adminDetails", adminDetails);
			return "redirect:/login";
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return "login";
	}
	

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String validateLogin(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{
		String username = request.getParameter("form-username");
		String password = request.getParameter("form-password");
		Builder builder = Admin.newBuilder();
		builder.setEmail(username);
		builder.setPassword(password);
		Admin loginRequest = builder.build();
		Admin res;
		try{
			res = grpcClient.adminLogin(loginRequest);
			System.out.println("Response *** "+res.toString());
		}
		catch(Exception e){
			e.printStackTrace();
			return "login";
		}
		addCookiesToResponse(res, request, response);
		return "redirect:/dashboard";
	}

	private void addCookiesToResponse(Admin adminDetails, HttpServletRequest request, HttpServletResponse response) {

		Cookie cookieRoleNames = new Cookie("role", UserType.findByValue(adminDetails.getRole()).toString());
		cookieRoleNames.setDomain(request.getServerName());
		cookieRoleNames.setPath(request.getContextPath());

		Cookie cookieFofoId = new Cookie("id", String.valueOf(adminDetails.getId()));
		cookieFofoId.setDomain(request.getServerName());
		cookieFofoId.setPath(request.getContextPath());

		Cookie cookieEmailId = new Cookie("email", adminDetails.getEmail());
		cookieEmailId.setDomain(request.getServerName());
		cookieEmailId.setPath(request.getContextPath());

		Cookie cookieName = new Cookie("name", adminDetails.getFirstName());
		cookieName.setDomain(request.getServerName());
		cookieName.setPath(request.getContextPath());

		response.addCookie(cookieFofoId);
		response.addCookie(cookieEmailId);
		response.addCookie(cookieRoleNames);
		response.addCookie(cookieName);
	}

}
