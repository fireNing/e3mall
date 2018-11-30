package cn.hpn.sso.service.impl;

import cn.hpn.mapper.TbUserMapper;
import cn.hpn.pojo.TbUser;
import cn.hpn.pojo.TbUserExample;
import cn.hpn.redis.JedisClient;
import cn.hpn.sso.service.LoginService;
import cn.hpn.utils.E3Result;
import cn.hpn.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private TbUserMapper userMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    /**
     * 用户登录；
     * 1、查询用户的用户和密码；
     * 2、登录成功后设置token
     * 3、将用户信息存储到redis
     * 4、设置过期时间
     * 5、返回E3Result包装token
     * @param username
     * @param password
     * @return
     */
    public E3Result login(String username, String password) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> list = userMapper.selectByExample(example);
        if(list == null && list.size()==0){
            return E3Result.build(504, "用户名或密码不正确！");
        }
        TbUser user = list.get(0);
        String MD5password = DigestUtils.md5DigestAsHex(password.getBytes());
        if(!user.getPassword().equals(MD5password)){
            return E3Result.build(504, "用户名或密码不正确！");
        }
        String token = UUID.randomUUID().toString();
        //将token存储到redis
        user.setPassword(null);
        jedisClient.set("SESSION:" + token , JsonUtils.objectToJson(user));
        //设置过期时间
        jedisClient.expire("SESSION:" + token, SESSION_EXPIRE);
        return E3Result.ok(token);
    }

    /**
     * 根据token查询用户是否已经登录
     * @param token
     * @return
     */
    public E3Result getUserByToken(String token) {
        if(StringUtils.isBlank(token)){
            return E3Result.build(500, "用户登录已经过期");
        }
        String json = jedisClient.get("SESSION:" + token);
        TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
        jedisClient.expire("SESSION:" + token, SESSION_EXPIRE);
        return E3Result.ok(user);
    }
}
