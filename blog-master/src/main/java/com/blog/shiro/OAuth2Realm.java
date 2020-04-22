package com.blog.shiro;

import com.blog.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class OAuth2Realm extends AuthorizingRealm {

    @Autowired
    IUserService userService;

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //String userName=(String) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> roles=new HashSet<String>();
        roles.add("admin");
        /*List<Role> rolesByUserName = roleDao.getRolesByUserName(userName);
        for(Role role:rolesByUserName) {
            roles.add(role.getRoleName());
        }
        List<Permission> permissionsByUserName = permissionDao.getPermissionsByUserName(userName);
        for(Permission permission:permissionsByUserName) {
            info.addStringPermission(permission.getPermissionName());
        }*/
        info.setRoles(roles);
        return info;
    }

    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        //注意token.getUsername()是指email！！
        AccountProfile profile = userService.login(token.getUsername(), String.valueOf(token.getPassword()));

        log.info("---------------->进入认证步骤");

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(profile, token.getCredentials(), getName());
        SecurityUtils.getSubject().getSession().setAttribute("profile", profile);
        return info;
    }
}
