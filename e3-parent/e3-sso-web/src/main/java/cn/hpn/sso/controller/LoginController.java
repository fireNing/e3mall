package cn.hpn.sso.controller;

import cn.hpn.sso.service.LoginService;
import cn.hpn.utils.CookieUtils;
import cn.hpn.utils.E3Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {
    @Autowired
    private LoginService loginService;
    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;

    /**
     * 登录页面
     * @return
     */
    @RequestMapping("/page/login")
    public String loginPage(String redirect ,HttpServletRequest request){
        request.setAttribute("redirect", redirect);
        return "login";
    }

    /**
     * 登录：
     * 1、查询数据库;
     * 2、设置cookies
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value = "/user/login",method = RequestMethod.POST)
    @ResponseBody
    public E3Result login(String username , String password , HttpServletRequest request , HttpServletResponse response){
        E3Result result = loginService.login(username, password);
        //已经通过验证用户名和密码正确
        if(result.getStatus() == 200){
            String token = (String) result.getData();
            CookieUtils.setCookie(request, response, TOKEN_KEY, token);
        }
        return result;

    }

    /**
     * 单点登录系统登录查询token
     */
    @RequestMapping(value = "/user/token/{token}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Object getUserByToken(@PathVariable String token , String callback){
        E3Result result = loginService.getUserByToken(token);
        if(StringUtils.isNotBlank(callback)){
            MappingJacksonValue mapping = new MappingJacksonValue(result);
            mapping.setJsonpFunction(callback);
            return mapping;
        }
        return result;
    }

}
