package cn.hpn.controller;

import cn.hpn.content.service.ContentService;
import cn.hpn.pojo.TbContent;
import cn.hpn.utils.E3Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ContentController {
    @Autowired
    private ContentService contentService;

    @RequestMapping(value = "/content/save",method = RequestMethod.POST)
    @ResponseBody
    public E3Result saveContent(TbContent content){
        E3Result e3Result = contentService.addContent(content);
        return e3Result;
    }

}
