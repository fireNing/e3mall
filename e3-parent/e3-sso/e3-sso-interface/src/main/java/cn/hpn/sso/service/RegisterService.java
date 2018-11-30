package cn.hpn.sso.service;

import cn.hpn.pojo.TbUser;
import cn.hpn.utils.E3Result;

public interface RegisterService {

    public E3Result checkUser(String param , String type);
    public E3Result register(TbUser user);

}
