package cn.hpn.service.impl;

import cn.hpn.mapper.TbItemDescMapper;
import cn.hpn.pojo.TbItemDesc;
import cn.hpn.redis.JedisClient;
import cn.hpn.service.ItemDescService;
import cn.hpn.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ItemDescServiceImpl implements ItemDescService {

    @Autowired
    private TbItemDescMapper itemDescMapper;
    @Autowired
    private JedisClient jedisClient;

    @Value("${ITEM_INFO}")
    private String ITEM_INFO;
    @Value("${ITEM_INFO_EXPIRE}")
    private Integer ITEM_INFO_EXPIRE;

    /**
     * 根据商品Id查询商品描述
     * @param itemId
     * @return
     */
    public TbItemDesc getItemDescById(long itemId) {
        //从缓存中获取；
        try {
            String json = jedisClient.get(ITEM_INFO + ":" + itemId + ":DESC");
            if(StringUtils.isNotBlank(json)){
                TbItemDesc jsonToPojo = JsonUtils.jsonToPojo(json, TbItemDesc.class);
                return jsonToPojo;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
        //存储到缓存中;
        try {
            String itemDescString = JsonUtils.objectToJson(itemDesc);
            jedisClient.set(ITEM_INFO + ":" + itemId + ":DESC", itemDescString);
            jedisClient.expire(ITEM_INFO + ":" + itemId + ":DESC", ITEM_INFO_EXPIRE);
        }catch (Exception e){
        }
        return itemDesc;
    }
}
