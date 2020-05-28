package com.zouyu.shiro.realm;

import com.zouyu.dao.UserDao;
import com.zouyu.vo.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author zouyu
 * @description
 * @date 2019/4/4
 */
public class CustomRelam extends AuthorizingRealm {
    @Resource
    private UserDao userDao;

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String userName = (String)principalCollection.getPrimaryPrincipal();
        Set<String> roles=getRolesByuserName(userName);
        Set<String> permissions=getPermissionsByuserName(userName);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(permissions);
        simpleAuthorizationInfo.setRoles(roles);
         return simpleAuthorizationInfo;
    }

    private Set<String> getPermissionsByuserName(String userName) {
        Set<String> sets=new HashSet<String>();
        sets.add("user:select");
        sets.add("user:add");
        return sets;
    }

    private Set<String> getRolesByuserName(String userName) {
        System.out.println("从数据库中获取权限数据");
        List<String> list = userDao.queryRolesByUsername(userName);
        return new HashSet<>(list);
    }

    /**
     * @Author zouyu
     * @Description //TODO
     * @Date
     * @Param 认证
     * @return
     **/
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //从主体传过来的认证信息中获取用户名
        String userName = (String) authenticationToken.getPrincipal();
        //通过用户名到数据库获取凭证
        String password= getPassWordByUserName(userName);
        if(password==null || password.equals("")){
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo =new SimpleAuthenticationInfo(userName,password,"customRealm");
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(userName));
        return authenticationInfo;
    }

    private String getPassWordByUserName(String userName) {
        User user = userDao.queryPassword(userName);
        if(user != null){
            return user.getPassWord();
        }
        return null;
    }
    public  static void main (String[] args){
        Md5Hash md5Hash = new Md5Hash("123","zy");
        System.out.println(md5Hash.toString());
        String s = new SecureRandomNumberGenerator().nextBytes().toHex();
        System.out.println(s);

        String s1 = new SimpleHash("md5", "admin", ByteSource.Util.bytes("admin5016b9942433201bf3fe61992eacba71"), 2).toHex();
        System.out.println(s1);
    }
}
