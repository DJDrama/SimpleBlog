package com.example.simpleblog.config.filter

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {
    
    //@Bean
    fun registerMyAuthenticationFilter(): FilterRegistrationBean<MyAuthenticationFilter> {
        val filterRegistrationBean = FilterRegistrationBean(MyAuthenticationFilter())
        return filterRegistrationBean.apply {
            addUrlPatterns("/api/*") // urls below api endpoint needs authentication
            order = 0
        }
    }
}