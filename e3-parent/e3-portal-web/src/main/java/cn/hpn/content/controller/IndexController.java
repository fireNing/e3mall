package cn.hpn.content.controller;

import cn.hpn.content.service.ContentService;
import cn.hpn.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class IndexController {

    @Value("${AD_LUNBOTU_ID}")
    private long AD_LUNBOTU_ID;

    @Autowired
    private ContentService contentService;

    @RequestMapping("/index")
    public String indexPage(Model model){
        List<TbContent> adList = contentService.getContentListByCid(AD_LUNBOTU_ID);
        model.addAttribute("ad1List", adList);
        return "index";

    }
}
