package cn.hpn.sso.controller;

import cn.hpn.pojo.TbUser;
import cn.hpn.sso.service.RegisterService;
import cn.hpn.utils.E3Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RegisterController {

    @Autowired
    private RegisterService registerService;


    @RequestMapping("/page/register")
    public String regist(){
        return "register";
    }

    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public E3Result checkUser(@PathVariable String param,@PathVariable String type){

        E3Result result = registerService.checkUser(param, type);
        return result;
    }


    @RequestMapping(value = "/user/register",method = RequestMethod.POST)
    @ResponseBody
    public E3Result registerUser(TbUser user){
        E3Result result = registerService.register(user);
        return result;
    }

}
