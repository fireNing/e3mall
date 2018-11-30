package cn.hpn.quest.service;

import cn.hpn.pojo.SearchResult;

public interface SearchService {

    public SearchResult searchItemResult(int page,int rows,String keyword) throws Exception;

}
