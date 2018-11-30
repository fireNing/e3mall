package cn.hpn.controller;

import cn.hpn.pojo.EasyUIDataGridResult;
import cn.hpn.pojo.TbItem;
import cn.hpn.service.ItemService;
import cn.hpn.utils.E3Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    /**
     * 查询商品
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDataGridResult getPageList(Integer page , Integer rows){
        EasyUIDataGridResult result = itemService.getItemList(page,rows);
        return result;
    }

    /**
     * 保存商品
     * @param item
     * @param desc
     * @return
     */
    @RequestMapping("item/save")
    @ResponseBody
    public E3Result saveItem(TbItem item , String desc){
        E3Result e3Result = itemService.saveItem(item, desc);
        return e3Result;
    }

    /**
     * s删除商品
     * @param ids
     * @return
     */
    @RequestMapping("/rest/item/delete")
    @ResponseBody
    public E3Result deleteItem(long[] ids){
        E3Result e3Result = itemService.deleteItem(ids);
        return e3Result;
    }

    /**
     * 下架商品
     */
    @RequestMapping("/rest/item/instock")
    @ResponseBody
    public E3Result downItem(long[] ids){
        E3Result result = itemService.downItem(ids);
        return result;
    }

}
