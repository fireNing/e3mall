package cn.hpn.order.service;

import cn.hpn.order.pojo.OrderInfo;
import cn.hpn.utils.E3Result;

public interface OrderService {

    public E3Result createOrder(OrderInfo orderInfo);

}
