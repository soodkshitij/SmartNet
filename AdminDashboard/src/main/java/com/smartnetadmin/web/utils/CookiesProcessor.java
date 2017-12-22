package com.smartnetadmin.web.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.smartnetadmin.web.pojo.AdminDetails;


@Component
public class CookiesProcessor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CookiesProcessor.class);
	
	public AdminDetails getCookiesObject(HttpServletRequest request) throws Exception{
		Cookie[] cookies = request.getCookies();
		if (cookies == null){
			throw new Exception();
		}
		String id = null, email = null, role = null, name=null;
		for(Cookie cookie : cookies){
			LOGGER.info("Requested Cookie {}={}", cookie.getName(), cookie.getValue());
			if(cookie.getName().equals("role") && cookie.getValue() != null && !cookie.getValue().isEmpty()){
				role = cookie.getValue();
			}
			if(cookie.getName().equals("email") && cookie.getValue() != null && !cookie.getValue().isEmpty()){
				email = cookie.getValue();
			}
			if(cookie.getName().equals("id") && cookie.getValue() != null){
				id = cookie.getValue();
			}
			if(cookie.getName().equals("name") && cookie.getValue() != null){
				name = cookie.getValue();
			}
			if(id != null && email != null && role != null && name != null){
				break;
			}
		}
		
		if(id == null || email == null || role == null || name==null){
			LOGGER.info("roleNameString is {}", role);
			LOGGER.info("id is {}", id);
			LOGGER.info("email is {}", email);
			LOGGER.error("Requested session is not valid");
			throw new Exception();
		}else{
			AdminDetails adminDetails = new AdminDetails();
			adminDetails.setId(id);
			adminDetails.setEmail(email);
			adminDetails.setRole(role);
			adminDetails.setName(name);
 			return adminDetails;
		}
		
	}
	
	public void removeCookies(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Cookie[] cookies = request.getCookies();
		if (cookies == null){
			throw new Exception();
		}
		
		Cookie cookieId = new Cookie("id", "");
		cookieId.setMaxAge(0);
		cookieId.setPath(request.getContextPath());
		cookieId.setDomain(request.getServerName());
		
		Cookie cookieEmailId = new Cookie("email", "");
		cookieEmailId.setMaxAge(0);
		cookieEmailId.setDomain(request.getServerName());
		cookieEmailId.setPath(request.getContextPath());

		Cookie cookieRole = new Cookie("role", "");
		cookieRole.setMaxAge(0);
		cookieRole.setDomain(request.getServerName());
		cookieRole.setPath(request.getContextPath());

		Cookie cookieName = new Cookie("name", "");
		cookieName.setMaxAge(0);
		cookieName.setDomain(request.getServerName());
		cookieName.setPath(request.getContextPath());
		
		response.addCookie(cookieId);
		response.addCookie(cookieEmailId);
		response.addCookie(cookieRole);
		response.addCookie(cookieName);
	}
}
