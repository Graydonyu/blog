package com.blog.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.blog.shiro.OAuth2Realm;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Configuration
public class ShiroConfig {

    @Bean("SecurityManager")
    public SecurityManager securityManager(OAuth2Realm oAuth2Realm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(oAuth2Realm);

        log.info("------------->securityManager注入完成");
        return defaultWebSecurityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("SecurityManager")SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 配置登录的url和登录成功的url
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/user/center");
        // 配置未授权跳转页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/error/403");

        Map<String, String> hashMap = new LinkedHashMap<>();
        hashMap.put("/login", "anon");
        hashMap.put("/user*", "user");
        hashMap.put("/user/**", "user");
        hashMap.put("/post/**", "user");
        hashMap.put("/comment/**", "user");
        hashMap.put("/collection/**", "user");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(hashMap);

        return shiroFilterFactoryBean;
    }

    @Bean
    public ShiroDialect shiroDialect(){
        return new ShiroDialect();
    }
}
