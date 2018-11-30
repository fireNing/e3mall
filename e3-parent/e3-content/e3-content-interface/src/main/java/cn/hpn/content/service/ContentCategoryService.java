package cn.hpn.content.service;

import cn.hpn.pojo.EasyUITreeDate;
import cn.hpn.utils.E3Result;

import java.util.List;

public interface ContentCategoryService {

    public List<EasyUITreeDate> contentCategoryTree(long parentId);
    public E3Result addContentCategoryNode(long parentId , String name );

}
