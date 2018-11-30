package cn.hpn.cart.interceptor;

import cn.hpn.pojo.TbUser;
import cn.hpn.sso.service.LoginService;
import cn.hpn.utils.CookieUtils;
import cn.hpn.utils.E3Result;
import com.alibaba.dubbo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断是否登录；
        String token = CookieUtils.getCookieValue(request, "token");
        if(StringUtils.isBlank(token)){
            //如果没有去到token ，用户没有登录；直接放行;
            return true;
        }
        E3Result result = loginService.getUserByToken(token);
        if(result.getStatus() != 200){
            //用户信息已经过期；放行
            return true;
        }
        //将用户信息放到request域中 到controller层判断是否存在用户;
        TbUser user = (TbUser) result.getData();
        request.setAttribute("user", user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) throws Exception {

    }
}
