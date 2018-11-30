package cn.hpn.quest.dao;

import cn.hpn.pojo.SearchItem;
import cn.hpn.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品搜索过滤条件
 */
@Repository
public class SearchDao {

    @Autowired
    private SolrServer solrServer;

    public SearchResult searchItemResult(SolrQuery solrQuery) throws Exception {
        //根据条件查询索引库；
        QueryResponse queryResponse = solrServer.query(solrQuery);
        //获取查询结果的总记录数;
        SolrDocumentList results = queryResponse.getResults();
        long totalCount = results.getNumFound();
        //创建一个返回结果对象;
        SearchResult searchResult = new SearchResult();
        searchResult.setRecourdCount((int)totalCount);
        //创建一个ItemList对象;
        List<SearchItem> itemList = new ArrayList<>();
        //取商品列表；
        //取高亮后的结果;
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
        for (SolrDocument document : results) {
            SearchItem searchItem = new SearchItem();
            searchItem.setId((String) document.get("id"));
            searchItem.setCategory_name((String) document.get("item_category_name"));
            searchItem.setImage((String) document.get("item_image"));
            searchItem.setPrice((Long) document.get("item_price"));
            searchItem.setSell_point((String) document.get("item_sell_point"));
            //高亮显示商品标题内容；
            List<String> list = highlighting.get(document.get("id")).get("item_title");
            String title = "";
            if(list!=null && list.size()>0){
                //高亮显示;
                title = list.get(0);
            }else {
                title = (String) document.get("item_title");
            }
            searchItem.setTitle(title);
            itemList.add(searchItem);
        }
        searchResult.setItemList(itemList);
        //返回结果;
        return searchResult;

    }


}
