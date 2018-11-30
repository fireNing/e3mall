package cn.hpn.item.listener;

import cn.hpn.pojo.Item;
import cn.hpn.pojo.TbItem;
import cn.hpn.pojo.TbItemDesc;
import cn.hpn.service.ItemDescService;
import cn.hpn.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class HtmlGenListener implements MessageListener {
    //注入service
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemDescService itemDescService;
    @Autowired
    private FreeMarkerConfig freemarkerConfig;
    @Value("${HTML_GEN_PATH}")
    private String HTML_GEN_PATH;


    @Override
    public void onMessage(Message message) {

        try {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            Long itemId = Long.parseLong(text);
            //等待事务提交
            Thread.sleep(1000);
            //根据商品ID查询商品
            TbItem tbItem = itemService.getItemById(itemId);
            Item item = new Item(tbItem);
            //根据商品ID查询描述信息;
            TbItemDesc itemDesc = itemDescService.getItemDescById(itemId);
            //创建一个数据集
            Map data = new HashMap();
            data.put("item", item);
            data.put("itemDesc", itemDesc);
            //将信息写到静态页面；
            Configuration configuration = freemarkerConfig.getConfiguration();
            //加载模版对象
            Template template = configuration.getTemplate("item.ftl");
            //创建一个输出流-->指定地址和名称；
            Writer out = new FileWriter(HTML_GEN_PATH + itemId + ".html");
            //生成静态页面;
            template.process(data, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
