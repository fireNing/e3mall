package cn.hpn.content.service.impl;

import cn.hpn.content.service.ContentService;
import cn.hpn.mapper.TbContentMapper;
import cn.hpn.pojo.TbContent;
import cn.hpn.pojo.TbContentExample;
import cn.hpn.redis.JedisClient;
import cn.hpn.utils.E3Result;
import cn.hpn.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private TbContentMapper contentMapper;
    
    @Autowired
    private JedisClient jedisClient;

    @Value("${CONTENT_LIST}")
    private String CONTENT_LIST;

    /**
     * 添加内容
     * @param content
     * @return
     */
    public E3Result addContent(TbContent content) {
        //补全信息;
        Date date = new Date();
        content.setCreated(date);
        content.setUpdated(date);
        contentMapper.insert(content);
        //添加成功后，同步缓存信息；将缓存删除，
        // 第一次访问的时候查询数据库再次存储到缓存
        try {
            jedisClient.hdel(CONTENT_LIST,content.getCategoryId().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return E3Result.ok();
    }

    /**
     * 根据Cid查询商品轮播图
     * @param cid
     * @return
     */
    public List<TbContent> getContentListByCid(long cid) {
        //将轮播图的信息存储到redis中缓存; 第一次查询缓存中没有需要到数据库中查找
        try {
            String json = jedisClient.hget(CONTENT_LIST, cid + "");
            if (StringUtils.isNotBlank(json)){
                //判断一下缓存中是不是存在，如果存在直接返回;
                List<TbContent> jsonToList = JsonUtils.jsonToList(json, TbContent.class);
                return jsonToList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(cid);
        List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
        //第一查询存储到缓存中
        try {
            jedisClient.hset(CONTENT_LIST, cid+"", JsonUtils.objectToJson(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
