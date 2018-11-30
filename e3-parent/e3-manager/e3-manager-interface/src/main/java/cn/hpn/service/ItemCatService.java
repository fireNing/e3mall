package cn.hpn.service;

import cn.hpn.pojo.EasyUITreeDate;

import java.util.List;

public interface ItemCatService {
    public List<EasyUITreeDate> getItemCatList(long parentId);
}
