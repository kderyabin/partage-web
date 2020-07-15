//package com.kderyabin.web.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.ViewResolver;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.view.InternalResourceViewResolver;
//import org.springframework.web.servlet.view.JstlView;
//import org.springframework.web.servlet.view.UrlBasedViewResolver;
//
//@Configuration
//@EnableWebMvc
//@ComponentScan
//public class MvcConfiguration implements WebMvcConfigurer
//{
//    @Bean
//    public ViewResolver internalResourceViewResolver() {
//        UrlBasedViewResolver bean = new UrlBasedViewResolver();
//        bean.setViewClass(JstlView.class);
//        bean.setPrefix("/WEB-INF/jsp/");
//        bean.setSuffix(".jsp");
//        return bean;
//    }
//}