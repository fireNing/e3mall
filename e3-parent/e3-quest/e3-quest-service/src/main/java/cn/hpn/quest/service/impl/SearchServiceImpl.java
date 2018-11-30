package cn.hpn.quest.service.impl;

import cn.hpn.pojo.SearchResult;
import cn.hpn.quest.dao.SearchDao;
import cn.hpn.quest.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchDao searchDao;

    @Override
    public SearchResult searchItemResult(int page, int rows, String keyword) throws Exception {
        //创建一个solrQuery对象；
        SolrQuery solrQuery = new SolrQuery();
        //设置查询条件;
        solrQuery.setQuery(keyword);
        //设置分页条件;
        if(page<1) solrQuery.setStart(1);
        solrQuery.setStart((page - 1) * rows);
        solrQuery.setRows(rows);
        //设置默认搜索域;
        solrQuery.set("df", "item_title");
        //开启高亮;
        solrQuery.setHighlight(true);
        //设置高亮样式;
        solrQuery.addHighlightField("item_title");
        solrQuery.setHighlightSimplePre("<em style=\"color:red\">");
        solrQuery.setHighlightSimplePost("</em>");
        //获取查询结果;
        SearchResult searchResult = searchDao.searchItemResult(solrQuery);
        //计算总页数;
        int recourdCount = searchResult.getRecourdCount();
        double totalPage = Math.ceil(recourdCount*1.0/rows*1.0);
        //添加返回结果;
        searchResult.setTotalPages((int) totalPage);
        //返回结果
        return searchResult;
    }
}
