package cn.hpn.sso.service;

import cn.hpn.utils.E3Result;

public interface LoginService {

    public E3Result login(String username,String password);
    //根据token查询用户是否登录;
    public E3Result getUserByToken(String token);
}
