package com.smartnetadmin.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.smartnetadmin.web.pojo.AdminDetails;
import com.smartnetadmin.web.utils.CookiesProcessor;


@Controller
public class DashboardController {

	@Autowired
	CookiesProcessor cookiesProcessor;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DashboardController.class);
	
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String dashboard(HttpServletRequest request, Model model) throws Exception{
		model.addAttribute("appContextPath", request.getContextPath());
		try{
			AdminDetails adminDetails = cookiesProcessor.getCookiesObject(request);
			model.addAttribute("adminDetails", adminDetails);
		}
		catch(Exception e){
			e.printStackTrace();
			return "redirect:/login";
		}
		return "dashboard";
	}
	
	
}
