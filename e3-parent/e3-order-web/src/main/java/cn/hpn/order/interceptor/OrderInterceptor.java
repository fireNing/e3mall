package cn.hpn.order.interceptor;

import cn.hpn.cart.service.CartService;
import cn.hpn.pojo.TbItem;
import cn.hpn.pojo.TbUser;
import cn.hpn.sso.service.LoginService;
import cn.hpn.utils.CookieUtils;
import cn.hpn.utils.E3Result;
import cn.hpn.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OrderInterceptor implements HandlerInterceptor {
    
    @Autowired
    private LoginService loginService;
    @Autowired
    private CartService cartService;

    @Value("${LOGIN_URL}")
    private String LOGIN_URL;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //拦截用户判断是否已经登录
        String token = CookieUtils.getCookieValue(request, "token");
        if(StringUtils.isBlank(token)){
            //跳转到登录页面；
            response.sendRedirect(LOGIN_URL + "?redirect=" + request.getRequestURL());
            return false;
        }
        E3Result result = loginService.getUserByToken(token);
        if(result.getStatus() != 200){
            //登录信息过期跳转到登录页面重新登录
            response.sendRedirect(LOGIN_URL + "?redirect=" + request.getRequestURL());
            return false;
        }
        TbUser user = (TbUser) result.getData();
        request.setAttribute("user", user);
        //判断cookie中是否有购物车商品;
        String cartjson = CookieUtils.getCookieValue(request, "cart", true);
        if(StringUtils.isNotBlank(cartjson)){
            //合并购物车
            cartService.mergeCart(user.getId(), JsonUtils.jsonToList(cartjson, TbItem.class));
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) throws Exception {

    }
}
