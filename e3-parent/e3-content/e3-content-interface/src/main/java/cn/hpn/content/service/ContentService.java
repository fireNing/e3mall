package cn.hpn.content.service;

import cn.hpn.pojo.TbContent;
import cn.hpn.utils.E3Result;

import java.util.List;

public interface ContentService {

    public E3Result addContent(TbContent content);
    public List<TbContent> getContentListByCid(long cid);

}
