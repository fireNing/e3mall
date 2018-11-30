package cn.hpn.cart.service.impl;

import cn.hpn.cart.service.CartService;
import cn.hpn.mapper.TbItemMapper;
import cn.hpn.pojo.TbItem;
import cn.hpn.redis.JedisClient;
import cn.hpn.utils.E3Result;
import cn.hpn.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private JedisClient jedisClient;
    @Value("${REDIS_CART_PRE}")
    private String REDIS_CART_PRE;
    @Autowired
    private TbItemMapper itemMapper;

    /**
     * 添加购物车数据到redis
     * @param userId
     * @param itemId
     * @param num
     * @return
     */
    public E3Result addCartItemToRedis(Long userId, Long itemId, Integer num) {
        //向redis中添加购物车。
        //数据类型是hash key：用户id field：商品id value：商品信息
        //判断商品是否存在
        Boolean hexists = jedisClient.hexists(REDIS_CART_PRE + ":" + userId, itemId + "");
        if(hexists){
            //商品已经存在;
            String cartItem = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
            TbItem item = JsonUtils.jsonToPojo(cartItem, TbItem.class);
            //修改商品数量存储到redis
            item.setNum(item.getNum()+num);
            jedisClient.hset(REDIS_CART_PRE +":" + userId, itemId +"", JsonUtils.objectToJson(item));
            return E3Result.ok();
        }
        //商品不存在;
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        item.setNum(num);
        String images = item.getImage();
        String[] split = images.split(",");
        String image = split[0];
        item.setImage(image);
        jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
        return E3Result.ok();
    }

    /**
     * 用户已经登录，合并未登录时cookie的商品
     * @param userId
     * @param cartList
     * @return
     */
    public E3Result mergeCart(Long userId, List<TbItem> cartList) {
        if(cartList != null && cartList.size()>0){
            for (TbItem item : cartList) {
                addCartItemToRedis(userId, item.getId(), item.getNum());
            }
        }
        return E3Result.ok();
    }

    /**
     * 根据id从redis中获取购物车商品
     * @param userId
     * @return
     */
    public List<TbItem> getItemForRedisById(Long userId) {
        List<String> cartListStr = jedisClient.hvals(REDIS_CART_PRE + ":" + userId);
        List<TbItem> cartList = new ArrayList<>();
        for (String json : cartListStr) {
            TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
            cartList.add(item);
        }
        return cartList;
    }

    /**
     * 更新redis中商品的数量
     * @param userId
     * @param itemId
     * @param num
     * @return
     */
    public E3Result updateRedisForItemNum(Long userId, Long itemId, Integer num) {
        String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
        TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
        item.setNum(item.getNum()+num);
        //回写到redis
        jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId+"", JsonUtils.objectToJson(item));
        return E3Result.ok();
    }

    /**
     * 删除redis中的商品
     * @param userId
     * @param itemId
     * @return
     */
    public E3Result deleterRedisForItem(Long userId, Long itemId) {
        String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
        if(StringUtils.isNotBlank(json)){
            jedisClient.hdel(REDIS_CART_PRE + ":" + userId ,itemId+"");
        }
        //删除成功
        return E3Result.ok();
    }

    /**
     * 清空购物车
     * @param userId
     * @return
     */
    public E3Result clearRedisCart(Long userId) {
        jedisClient.del(REDIS_CART_PRE + ":" +userId);
        return E3Result.ok();
    }
}
