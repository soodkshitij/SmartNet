package com.smartnetadmin.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.smartnet.grpc.SmartnetGrpc.SmartnetBlockingStub;
import com.smartnet.grpc.SmartnetOuterClass.Community;
import com.smartnet.grpc.SmartnetOuterClass.CommunityList;
import com.smartnet.grpc.SmartnetOuterClass.CommunityListRequest;
import com.smartnet.grpc.SmartnetOuterClass.CommunityListRequest.Builder;
import com.smartnetadmin.web.enums.CommunityStatus;
import com.smartnetadmin.web.enums.NeighbourhoodType;
import com.smartnetadmin.web.pojo.AdminDetails;
import com.smartnetadmin.web.pojo.CommunityPojo;
import com.smartnetadmin.web.utils.CookiesProcessor;


@Controller
public class CommunityListController {

	@Autowired
	CookiesProcessor cookiesProcessor;
	@Autowired
	SmartnetBlockingStub grpcClient;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CommunityListController.class);
	
	@RequestMapping(value = "/community-list", method = RequestMethod.GET)
	public String dashboard(HttpServletRequest request, Model model) throws Exception{
		model.addAttribute("appContextPath", request.getContextPath());
		AdminDetails adminDetails = cookiesProcessor.getCookiesObject(request);
		String status = request.getParameter("status");
		Builder builder = CommunityListRequest.newBuilder();
		builder.setStatus(Integer.valueOf(status));
		builder.setAdminId(Integer.valueOf(adminDetails.getId()));
		CommunityListRequest cl = builder.build();
		CommunityList clo = grpcClient.getListOfCommunities(cl);
		List<Community> clist = clo.getCommunityList();
		List<CommunityPojo> cpList = new ArrayList<>();
		for (Community c : clist){
			CommunityPojo cp = new CommunityPojo();
			cp.setId(c.getId());
			cp.setName(c.getName());
			cp.setState(c.getState());
			cp.setType(StringUtils.capitalise(NeighbourhoodType.findByValue(c.getType()).toString()));
			cp.setCity(c.getCity());
			cp.setCreated_at(new java.util.Date(c.getCreatedTimestamp()).toString());
			cpList.add(cp);
		}
		model.addAttribute("clo", cpList);
		model.addAttribute("type",StringUtils.capitalize(CommunityStatus.findByValue(Integer.valueOf(status)).toString())+ " Communities");
		model.addAttribute("status",Integer.valueOf(status));
		return "created-communities";
	}
	
	
}
