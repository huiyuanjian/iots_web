package io.renren.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * cors 解决跨域配置
   *    请求 : 1. 简单请求 2. 复杂请求
 * @author yywboruo@163.com
 * @since 3.0.0 2017-10-22
 */
@Configuration
public class CorsConfig {

    /**
     * cors 配置
     * @return
     */
    private CorsConfiguration buildConfig(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); // 允许任何域名使用
        corsConfiguration.addAllowedHeader("*"); // 允许任何请求头
        corsConfiguration.addAllowedMethod("*"); // 允许任何请求方式(post get)
        corsConfiguration.setAllowCredentials(true);  //前后端分离时 , 解决跨域以及携带cookie, 前后端都得进行设置
        return corsConfiguration;
    }

    /**
     *   filter 解决跨域
     * @return
     */
    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig()); //对接口配置跨域设置
        return new CorsFilter(source);
    }
}
