package cn.hpn.service.impl;

import cn.hpn.mapper.TbItemDescMapper;
import cn.hpn.mapper.TbItemMapper;
import cn.hpn.pojo.EasyUIDataGridResult;
import cn.hpn.pojo.TbItem;
import cn.hpn.pojo.TbItemDesc;
import cn.hpn.pojo.TbItemExample;
import cn.hpn.redis.JedisClient;
import cn.hpn.service.ItemService;
import cn.hpn.utils.E3Result;
import cn.hpn.utils.IDUtils;
import cn.hpn.utils.JsonUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbItemDescMapper itemDescMapper;

    //注入消息模版
    @Autowired
    private JmsTemplate jmsTemplate;
    //注入发送者
    @Resource
    private Destination topicDestination;

    //注入缓存redis
    @Autowired
    private JedisClient jedisClient;
    //缓存前缀;
    @Value("${ITEM_INFO}")
    private String ITEM_INFO;
    //缓存时间;
    @Value("${ITEM_INFO_EXPIRE}")
    private Integer ITEM_INFO_EXPIRE;


    /**
     * 添加新商品
     * @param item
     * @param desc
     * @return
     */
    public E3Result saveItem(TbItem item, String desc) {
        Date date = new Date();
        final long itemId = IDUtils.genItemId();
        item.setId(itemId);
        //商品状态，1-正常，2-下架，3-删除
        item.setStatus((byte) 1);
        //商品创建时间
        item.setCreated(date);
        //商品更新时间
        item.setUpdated(date);
        itemMapper.insert(item);
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemId(itemId);
        itemDesc.setCreated(date);
        itemDesc.setUpdated(date);
        itemDesc.setItemDesc(desc);
        itemDescMapper.insert(itemDesc);
        //每次添加商品时发送添加商品的id，更新索引库;
        jmsTemplate.send(topicDestination, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(itemId+ "" );
                return textMessage;
            }
        });
        return E3Result.ok();
    }

    //分页查询商品列表

    public EasyUIDataGridResult getItemList(int page, int rows) {
        //设置分页;
        PageHelper.startPage(page, rows);
        //执行查询
        TbItemExample example = new TbItemExample();
        List<TbItem> list = itemMapper.selectByExample(example);
        PageInfo<TbItem> info = new PageInfo<>(list);

        EasyUIDataGridResult result = new EasyUIDataGridResult();
        long total = info.getTotal();
        result.setTotal(total);
        result.setRows(list);
        return result;
    }
    /**
     * 删除商品
     * @param itemIds
     * @return
     */
    public E3Result deleteItem(long[] itemIds) {
        for (long itemId : itemIds) {
            try {
                itemMapper.deleteByPrimaryKey(itemId);
            } catch (Exception e) {
                e.printStackTrace();
                E3Result result = E3Result.build(500, "删除失败！", null);
                return result;
            }
        }
        return E3Result.ok();
    }

    /**
     * 下架商品
     * @param itemIds
     * @return
     */
    public E3Result downItem(long[] itemIds) {
        for (long itemId : itemIds) {

            try {
                TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
                tbItem.setStatus((byte)2);
                itemMapper.updateByPrimaryKey(tbItem);
            } catch (Exception e) {
                e.printStackTrace();
                E3Result.build(500, "下架商品失败！");
            }
        }
        return E3Result.ok();
    }

    /**
     * 根据商品id查询商品
     * @param itemId
     * @return
     */
    public TbItem getItemById(long itemId) {
        //从缓存中回去商品；
        try {
            String json = jedisClient.get(ITEM_INFO + ":" + itemId + ":BASE");
            if(StringUtils.isNotBlank(json)){
                TbItem jsonToPojo = JsonUtils.jsonToPojo(json, TbItem.class);
                return jsonToPojo;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
        //将商品信息存储到缓存中;
        try {
            String itemString = JsonUtils.objectToJson(tbItem);
            jedisClient.set(ITEM_INFO + ":" + itemId + ":BASE", itemString);
            jedisClient.expire(ITEM_INFO + ":" + itemId + ":BASE", ITEM_INFO_EXPIRE);
        }catch (Exception e){
            e.printStackTrace();
        }
        return tbItem;
    }
}
