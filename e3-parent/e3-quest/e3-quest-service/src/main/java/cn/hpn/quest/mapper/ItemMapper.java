package cn.hpn.quest.mapper;

import cn.hpn.pojo.SearchItem;

import java.util.List;

public interface ItemMapper {

    public List<SearchItem> getItemList();
    SearchItem getItemById(long itemId);

}
