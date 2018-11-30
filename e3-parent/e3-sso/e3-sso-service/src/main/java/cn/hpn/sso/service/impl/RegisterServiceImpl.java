package cn.hpn.sso.service.impl;

import cn.hpn.mapper.TbUserMapper;
import cn.hpn.pojo.TbUser;
import cn.hpn.pojo.TbUserExample;
import cn.hpn.sso.service.RegisterService;
import cn.hpn.utils.E3Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private TbUserMapper userMapper;

    /**
     *
     * @param param
     * @param type
     * @return
     */
    public E3Result checkUser(String param, String type) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        if("1".equals(type)){
            //校验用户名是否存在
            criteria.andUsernameEqualTo(param);
        }else if("2".equals(type)){
            //校验电话是否存在
            criteria.andPhoneEqualTo(param);
        }else if("3".equals(type)){
            criteria.andEmailEqualTo(param);
        }else {
            return E3Result.build(201, "输入信息不正确！");
        }
        List<TbUser> list = userMapper.selectByExample(example);
        if(list != null && list.size()>0){
            return E3Result.ok(false);
        }
        return E3Result.ok(true);
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    public E3Result register(TbUser user) {
        //校验用户信息格式是否正确;
        if(StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPhone()) || StringUtils.isBlank(user.getPassword()))
            return E3Result.build(500, "用户注册信息格式不正确！");
        //补全用户信息;
        Date date = new Date();
        user.setCreated(date);
        user.setUpdated(date);
        String password = user.getPassword();
        String MD5password = DigestUtils.md5DigestAsHex(password.getBytes());
        user.setPassword(MD5password);
        userMapper.insert(user);
        return E3Result.ok();
    }
}
