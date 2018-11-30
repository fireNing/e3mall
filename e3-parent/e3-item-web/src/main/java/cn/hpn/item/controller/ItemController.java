package cn.hpn.item.controller;

import cn.hpn.pojo.Item;
import cn.hpn.pojo.TbItem;
import cn.hpn.pojo.TbItemDesc;
import cn.hpn.service.ItemDescService;
import cn.hpn.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemDescService itemDescService;

    @RequestMapping("/item/{itemId}")
    public String showItemInfo(@PathVariable Long itemId , Model model){

        //获取商品描述;
        TbItemDesc itemDesc = itemDescService.getItemDescById(itemId);
        //获取商品信息；
        TbItem tbItem = itemService.getItemById(itemId);
        Item item = new Item(tbItem);
        //传递到页面;
        model.addAttribute("item", item);
        model.addAttribute("itemDesc", itemDesc);
        return "item";

    }

}
