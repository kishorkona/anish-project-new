package com.anish.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
public class SpringWebConfig implements WebMvcConfigurer {

	private static final String[] RESOURCE_LOCATIONS = {
			"classpath:/META-INF/resources/", "classpath:/resources/",
			"classpath:/static/", "classpath:/public/" };
	
	  @Override 
	  public void addResourceHandlers(ResourceHandlerRegistry registry) {
		  registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
		  registry.addResourceHandler("/**").addResourceLocations("/WEB-INF/");
		  if (!registry.hasMappingForPattern("/**")) {
				registry.addResourceHandler("/**").addResourceLocations(
						RESOURCE_LOCATIONS);
		  }
	  }

    @Bean
    public ViewResolver getViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setOrder(1);
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

}
