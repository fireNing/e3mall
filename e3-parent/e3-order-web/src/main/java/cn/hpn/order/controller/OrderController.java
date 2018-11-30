package cn.hpn.order.controller;

import cn.hpn.cart.service.CartService;
import cn.hpn.order.pojo.OrderInfo;
import cn.hpn.order.service.OrderService;
import cn.hpn.pojo.TbItem;
import cn.hpn.pojo.TbUser;
import cn.hpn.utils.E3Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;

    @RequestMapping("/order/order-cart")
    public String orderItem(HttpServletRequest request){
        TbUser user = (TbUser) request.getAttribute("user");
        List<TbItem> cartlist = cartService.getItemForRedisById(user.getId());
        request.setAttribute("cartList", cartlist);
        return "order-cart";
    }

    @RequestMapping(value = "/order/create" , method = RequestMethod.POST)
    public String createOrder(OrderInfo orderInfo,HttpServletRequest request){
        TbUser user = (TbUser) request.getAttribute("user");
        orderInfo.setUserId(user.getId());
        E3Result result = orderService.createOrder(orderInfo);
        if(result.getStatus() == 200){
            //添加成功删除购物车;
            cartService.clearRedisCart(user.getId());
        }
        request.setAttribute("orderId", result.getData());
        request.setAttribute("payment", orderInfo.getPayment());
        return "success";
    }


}
