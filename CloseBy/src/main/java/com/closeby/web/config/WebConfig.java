package com.closeby.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@EnableWebMvc
@Configuration
@ComponentScan({"com.closeby.*"})
public class WebConfig extends WebMvcConfigurerAdapter{
	
	private static final String RESOURCES_PATTERN="/resources/**";
	private static final String RESOURCES_LOCATION="/resources/";
	
	
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	   registry.addResourceHandler(RESOURCES_PATTERN).addResourceLocations(RESOURCES_LOCATION);
	}
	
	
}
