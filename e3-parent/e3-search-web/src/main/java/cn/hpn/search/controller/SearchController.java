package cn.hpn.search.controller;

import cn.hpn.pojo.SearchResult;
import cn.hpn.quest.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;
    @Value("${PAGE_SIZE}")
    private Integer PAGE_SIZE;

    @RequestMapping("/search")
    public String search(@RequestParam(defaultValue = "1") Integer page , String keyword , Model model) throws Exception {
        //解决中文乱码;
        keyword = new String(keyword.getBytes("iso-8859-1"),"utf-8");
        SearchResult searchResult = searchService.searchItemResult(page, PAGE_SIZE, keyword);
        model.addAttribute("query", keyword);
        model.addAttribute("totalPages", searchResult.getTotalPages());
        model.addAttribute("recourdCount", searchResult.getRecourdCount());
        model.addAttribute("page", page);
        model.addAttribute("itemList", searchResult.getItemList());
        //测试异常;
//        int i = 1/0;
        return "search";
    }


}
