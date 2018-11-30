package cn.hpn.quest.listener;

import cn.hpn.pojo.SearchItem;
import cn.hpn.quest.mapper.ItemMapper;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class SynchroniseListener implements MessageListener {
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private SolrServer solrServer;

    /**
     * 获取新添加的商品Id添加到索引库
     * @param message
     */
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            long itemId = Long.parseLong(text);
            //同步索引库
            //等待添加商品提交事务；
            Thread.sleep(1000);
            //查询数据库
            SearchItem item = itemMapper.getItemById(itemId);
            //将查询的数据同步到索引库
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id", item.getId());
            document.addField("item_title", item.getTitle());
            document.addField("item_price", item.getPrice());
            document.addField("item_sell_point", item.getSell_point());
            document.addField("item_image", item.getImage());
            document.addField("item_category_name", item.getCategory_name());
            solrServer.add(document);
            //提交
            solrServer.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
