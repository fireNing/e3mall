package cn.hpn.order.pojo;

import cn.hpn.pojo.TbOrder;
import cn.hpn.pojo.TbOrderItem;
import cn.hpn.pojo.TbOrderShipping;

import java.io.Serializable;
import java.util.List;

public class OrderInfo extends TbOrder implements Serializable {

    private List<TbOrderItem> orderItemList;
    private TbOrderShipping orderShipping;

    public List<TbOrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<TbOrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public TbOrderShipping getOrderShipping() {
        return orderShipping;
    }

    public void setOrderShipping(TbOrderShipping orderShipping) {
        this.orderShipping = orderShipping;
    }
}
