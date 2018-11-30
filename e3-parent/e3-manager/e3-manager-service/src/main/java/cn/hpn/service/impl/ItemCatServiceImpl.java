package cn.hpn.service.impl;

import cn.hpn.mapper.TbItemCatMapper;
import cn.hpn.pojo.EasyUITreeDate;
import cn.hpn.pojo.TbItemCat;
import cn.hpn.pojo.TbItemCatExample;
import cn.hpn.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper itemCatMapper;

    public List<EasyUITreeDate> getItemCatList(long parentId) {
        //创建查询条件模版
        TbItemCatExample example = new TbItemCatExample();
        //创建Criteria对象
        TbItemCatExample.Criteria criteria = example.createCriteria();
        //添加查询条件
        criteria.andParentIdEqualTo(parentId);
        List<TbItemCat> list = itemCatMapper.selectByExample(example);
        //将数据封装成[｛"id" :"" ,"text":"","state":""｝,{"id" :"" ,"text":"","state":""}]
        List<EasyUITreeDate> treeList = new ArrayList<>();
        for(TbItemCat itemCat : list){
            EasyUITreeDate tree = new EasyUITreeDate();
            tree.setId(itemCat.getId());
            tree.setText(itemCat.getName());
            if(itemCat.getIsParent()){
                tree.setState("closed");
            }else{
                tree.setState("open");
            }
            treeList.add(tree);
        }
        return treeList;
    }
}
