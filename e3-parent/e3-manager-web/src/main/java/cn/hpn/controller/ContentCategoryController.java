package cn.hpn.controller;

import cn.hpn.content.service.ContentCategoryService;
import cn.hpn.pojo.EasyUITreeDate;
import cn.hpn.utils.E3Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUITreeDate> contentCategoryTree(@RequestParam(value="id",defaultValue = "0") long parentId){
        List<EasyUITreeDate> list = contentCategoryService.contentCategoryTree(parentId);
        return list;
    }
    @RequestMapping("content/category/create")
    @ResponseBody
    public E3Result addContentCategoryNode(long parentId,String name){
        E3Result e3Result = contentCategoryService.addContentCategoryNode(parentId, name);
        return e3Result;
    }

}
