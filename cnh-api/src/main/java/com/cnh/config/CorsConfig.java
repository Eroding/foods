package com.cnh.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域配置器
 */
@Configuration
public class CorsConfig {
    public CorsConfig() {

    }

    @Bean
    public CorsFilter corsFilter() {
        //1.配置cors的配置信息
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        // config.addAllowedOrigin("http://152.136.125.49:8080");
        config.setAllowCredentials(true);     //设置是够发送cookie信息

        config.addAllowedMethod("*");  //设置请求的方式

        config.addAllowedHeader("*");  //设置允许的header

        //为url添加映射路径
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**", config);

        return new CorsFilter(corsSource);
    }
}
