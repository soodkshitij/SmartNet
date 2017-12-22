package com.closeby.web.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.closeby.web.controller.HomeController;



@Component
public class CookiesProcessor {
	
	private static final Logger logger = Logger.getLogger(HomeController.class);

	
	public String getCookiesObject(HttpServletRequest request) throws Exception{
		Cookie[] cookies = request.getCookies();
		if (cookies == null){
			throw new Exception();
		}
		String token=null;
		for(Cookie cookie : cookies){
			logger.info("Requested Cookie"+ cookie.getName()+" "+ cookie.getValue());
			if(cookie.getName().equals("token") && cookie.getValue() != null && !cookie.getValue().isEmpty()){
				token = cookie.getValue();
			}
			
		}
		
		if(token==null || token.length()==0){
			throw new Exception();
		}else{
 			return token;
		}
		
	}
	
	public void removeCookies(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Cookie[] cookies = request.getCookies();
		if (cookies == null){
			throw new Exception();
		}
		
		Cookie cookieId = new Cookie("token", "");
		cookieId.setMaxAge(0);
		cookieId.setPath(request.getContextPath());
		cookieId.setDomain(request.getServerName());
		
		response.addCookie(cookieId);
	}
}
