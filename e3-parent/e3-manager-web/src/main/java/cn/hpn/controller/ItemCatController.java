package cn.hpn.controller;

import cn.hpn.pojo.EasyUITreeDate;
import cn.hpn.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    @RequestMapping("/item/cat/list")
    @ResponseBody
    public List<EasyUITreeDate> getItemCatList(@RequestParam(value="id", defaultValue="0") long parentId){
        List<EasyUITreeDate> list = itemCatService.getItemCatList(parentId);
        return list;
    }

}
