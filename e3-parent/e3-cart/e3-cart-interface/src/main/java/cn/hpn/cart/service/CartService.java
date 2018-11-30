package cn.hpn.cart.service;

import cn.hpn.pojo.TbItem;
import cn.hpn.utils.E3Result;

import java.util.List;

public interface CartService {

    public E3Result addCartItemToRedis(Long userId , Long itemId ,Integer num  );
    public E3Result mergeCart(Long userId , List<TbItem> cartList);
    public List<TbItem> getItemForRedisById(Long userId);
    public E3Result updateRedisForItemNum(Long userId,Long itemId,Integer num);
    public E3Result deleterRedisForItem(Long userId, Long itemId);
    public E3Result clearRedisCart(Long userId);
}
