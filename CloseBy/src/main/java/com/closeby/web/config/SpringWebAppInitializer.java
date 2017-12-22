package com.closeby.web.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.closeby.web.controller.HomeController;
import org.apache.log4j.Logger;


public class SpringWebAppInitializer implements WebApplicationInitializer{
	
	private static final Logger logger = Logger.getLogger(SpringWebAppInitializer.class);
	
	private static final String DISPATCHER_SERVLET_NAME="DispatcherServlet";
	private static final String SPRING_CONFIG_PACKAGE_LOCATION="com.closeby.web.config";
	
	public void onStartup(ServletContext servletContext) throws ServletException {
		WebApplicationContext context = getContext();
        servletContext.addListener(new ContextLoaderListener(context));
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet(DISPATCHER_SERVLET_NAME, new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
        logger.info("Context is loaded");
	}
	
	private AnnotationConfigWebApplicationContext getContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation(SPRING_CONFIG_PACKAGE_LOCATION);
        return context;
    }
	
	
}