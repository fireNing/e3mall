package cn.hpn.order.service.impl;

import cn.hpn.mapper.TbOrderItemMapper;
import cn.hpn.mapper.TbOrderMapper;
import cn.hpn.mapper.TbOrderShippingMapper;
import cn.hpn.order.pojo.OrderInfo;
import cn.hpn.order.service.OrderService;
import cn.hpn.pojo.TbOrderItem;
import cn.hpn.pojo.TbOrderShipping;
import cn.hpn.redis.JedisClient;
import cn.hpn.utils.E3Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 订单处理;
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private TbOrderMapper orderMapper;
    @Autowired
    private TbOrderItemMapper orderItemMapper;
    @Autowired
    private TbOrderShippingMapper orderShippingMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${ORDER_ID_GEN_KEY}")
    private String ORDER_ID_GEN_KEY;
    @Value("${ORDER_START_NUM}")
    private String ORDER_START_NUM;
    @Value("${ORDER_DESC_ID_GEN_KEY}")
    private String ORDER_DESC_ID_GEN_KEY;


    @Override
    public E3Result createOrder(OrderInfo orderInfo) {
        //使用redis自增长生成orderid
        if (!jedisClient.exists(ORDER_ID_GEN_KEY)) {
            //id不存在 设置id
            jedisClient.set(ORDER_ID_GEN_KEY, ORDER_START_NUM);
        }
        //ID自增长
        Long orderId = jedisClient.incr(ORDER_ID_GEN_KEY);
        List<TbOrderItem> orderItemList = orderInfo.getOrderItemList();
        //补全订单信息；
        Date date = new Date();
        orderInfo.setOrderId(orderId.toString());
        orderInfo.setCreateTime(date);
        orderInfo.setUpdateTime(date);
        //状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
        orderInfo.setStatus(1);
        //插入表单;
        orderMapper.insert(orderInfo);
        //循环插入订单详情
        for (TbOrderItem tbOrderItem : orderItemList) {
            tbOrderItem.setId(jedisClient.incr(ORDER_DESC_ID_GEN_KEY).toString());
            tbOrderItem.setOrderId(orderId.toString());
            orderItemMapper.insert(tbOrderItem);
        }
        //向订单物流表插入数据
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        orderShipping.setCreated(date);
        orderShipping.setOrderId(orderId.toString());
        orderShipping.setUpdated(date);
        orderShippingMapper.insert(orderShipping);
        //返回插入成功
        return E3Result.ok(orderId.toString());
    }
}
