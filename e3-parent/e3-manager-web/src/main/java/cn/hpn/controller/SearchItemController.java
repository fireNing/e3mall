package cn.hpn.controller;

import cn.hpn.quest.service.QuestItemService;
import cn.hpn.utils.E3Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SearchItemController {

    @Autowired
    private QuestItemService questItemService;


    @RequestMapping("/index/item/import")
    @ResponseBody
    public E3Result importAllItem(){
        E3Result result = questItemService.importAllItems();
        return result;
    }

}
