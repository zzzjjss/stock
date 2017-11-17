package com.uf.store.shiro;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
@Configuration
public class ShiroConfig {
	@Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {	
		  ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
	        Map<String, String> filterChainDefinitionMapping = new HashMap<String, String>();
	        filterChainDefinitionMapping.put("/manager/login", "anon");
	        filterChainDefinitionMapping.put("/auth/**", "anon");
	        filterChainDefinitionMapping.put("/wxpay/callback", "anon");
	        filterChainDefinitionMapping.put("/**/auth/**", "anon");
	        filterChainDefinitionMapping.put("/targetcity/visitor/**", "anon");
	        filterChainDefinitionMapping.put("/visitor/**", "anon");
	        filterChainDefinitionMapping.put("/v2/api-docs", "anon");
	        filterChainDefinitionMapping.put("/configuration/**", "anon");
	        filterChainDefinitionMapping.put("/webjars/**", "anon");
	        filterChainDefinitionMapping.put("/swagger**", "anon");
	        filterChainDefinitionMapping.put("/manager/*", "stateless,roles[manager]");
	        shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMapping);
	        shiroFilter.setSecurityManager(securityManager);
	        Map<String, Filter> filters = new HashMap<String, Filter>();
	        filters.put("stateless", new StatelessAuthFilter());
	        shiroFilter.setFilters(filters);
	        return shiroFilter;
	}
	 @Bean(name = "securityManager")
	    public SecurityManager securityManager(CacheRealm realm) {
	        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(realm);
	         DefaultWebSessionStorageEvaluator sessionStorageEvaluator = new DefaultWebSessionStorageEvaluator();
	        sessionStorageEvaluator.setSessionStorageEnabled(false);
	        ((DefaultSubjectDAO) securityManager.getSubjectDAO()).setSessionStorageEvaluator(sessionStorageEvaluator);
	        return securityManager;
	    }
	
}
