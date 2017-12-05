package com.uf.store.shiro;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uf.store.dao.mysql.po.Customer;
import com.uf.store.dao.mysql.po.Manager;
import com.uf.store.service.cache.CacheService;

@Component
public class CacheRealm extends AuthorizingRealm {
	@Autowired
	private CacheService cache;

	public boolean supports(AuthenticationToken token) {
		return token != null && UserToken.class.isAssignableFrom(token.getClass());
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Object principal = principals.getPrimaryPrincipal();
		Set<String> userRoles = new HashSet<>();
		if (principal instanceof Manager) {
			userRoles.add("manager");
		}else if(principal instanceof Customer) {
			userRoles.add("customer");
		}
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		authorizationInfo.addRoles(userRoles);
		return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		UserToken token = (UserToken) authenticationToken;
		String tokenString = token.getToken();
		Object object = cache.getCachedObject(tokenString);
		if (object != null) {
			return new SimpleAuthenticationInfo(object, tokenString, getName());
		}
		return null;
	}
}
