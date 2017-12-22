package com.smartnetadmin.web.config;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver;

@SuppressWarnings("deprecation")
@Configuration
@ComponentScan("com.smartnetadmin.*")
public class AppConfig {

	private static final String VELOCITY_PATH_PREFIX="/WEB-INF/views/ftl/";
	private static final String VELOCITY_PATH_SUFFIX=".vm";
	private static final String MESSAGE_PATH_SOURCE_NAME="classpath:message";
	private static final Logger LOGGER=LoggerFactory.getLogger(AppConfig.class);
	private static Resource resource;
	
	public static Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		AppConfig.resource = resource;
	}

	@Bean
	public ViewResolver viewResolver()
	{
		VelocityLayoutViewResolver bean = new VelocityLayoutViewResolver();
		bean.setCache(false);
		bean.setPrefix("");
		bean.setSuffix(VELOCITY_PATH_SUFFIX);
		bean.setRequestContextAttribute("rc");
		return bean;
	}

	@Bean
	public VelocityConfigurer velocityConfig() {
		VelocityConfigurer velocityConfigurer = new VelocityConfigurer();
		velocityConfigurer.setResourceLoaderPath(VELOCITY_PATH_PREFIX);
		return velocityConfigurer;
	}

	@Bean(name="messageSource")
	public ReloadableResourceBundleMessageSource getReloadableResourceBundleMessageSource() {
		LOGGER.debug("creating messageSource bean with message path source name : "+MESSAGE_PATH_SOURCE_NAME);
		ReloadableResourceBundleMessageSource messageSource=new ReloadableResourceBundleMessageSource();
		messageSource.setBasename(MESSAGE_PATH_SOURCE_NAME);
		return messageSource;
	}

	@Bean(name="multipartResolver")
	public CommonsMultipartResolver getCommonsMultipartResolver() {
		LOGGER.info("creating common multipart resolver bean");
		return new CommonsMultipartResolver();
	}

	@Bean
	public PropertySourcesPlaceholderConfigurer propertyConfigurer1() {
		String activeProfile;

		PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer =  new PropertySourcesPlaceholderConfigurer();

		Properties properties = new Properties();
		try {
			properties.load(this.getClass().getClassLoader().getResourceAsStream("META-INF/env.property"));
		} catch (IOException e) {
			LOGGER.error("Error in reading env property file.Adding default property"+e);
			properties.put("profile", "dev");
		}
		activeProfile = (String) properties.get("profile");


		if ("prod".equals(activeProfile)) {
			resource = new ClassPathResource("/META-INF/prod.properties");
		} else if ("staging".equals(activeProfile)) {
			resource = new ClassPathResource("/META-INF/staging.properties");
		} else {
			resource = new ClassPathResource("/META-INF/dev.properties");
		}

		propertySourcesPlaceholderConfigurer.setLocation(resource);

		return propertySourcesPlaceholderConfigurer;
	}

}