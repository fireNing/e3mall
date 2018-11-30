package cn.hpn.content.service.impl;

import cn.hpn.content.service.ContentCategoryService;
import cn.hpn.mapper.TbContentCategoryMapper;
import cn.hpn.pojo.EasyUITreeDate;
import cn.hpn.pojo.TbContentCategory;
import cn.hpn.pojo.TbContentCategoryExample;
import cn.hpn.utils.E3Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;

    /**
     * 查询内容列表树形结构
     * @param parentId
     * @return
     */
    public List<EasyUITreeDate> contentCategoryTree(long parentId) {

        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbContentCategory> categoryList = contentCategoryMapper.selectByExample(example);
        List<EasyUITreeDate> list = new ArrayList<>();
        for (TbContentCategory tbContentCategory : categoryList) {
            EasyUITreeDate tree = new EasyUITreeDate();
            tree.setId(tbContentCategory.getId());
            tree.setText(tbContentCategory.getName());
            if(tbContentCategory.getIsParent()){
                tree.setState("closed");
            }else {
                tree.setState("open");
            }
            list.add(tree);
        }

        return list;
    }

    /**
     * 添加列表节点
     * @param parentId
     * @param name
     * @return
     */
    public E3Result addContentCategoryNode(long parentId, String name) {
        //补全contentCategory信息;
        TbContentCategory contentCategory = new TbContentCategory();
        contentCategory.setName(name);
        contentCategory.setParentId(parentId);
        Date date = new Date();
        contentCategory.setCreated(date);
        contentCategory.setUpdated(date);
        //状态。可选值:1(正常),2(删除)
        contentCategory.setStatus(1);
        contentCategory.setIsParent(false);
        //排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数
        contentCategory.setSortOrder(1);
        contentCategoryMapper.insert(contentCategory);
        //判断一下parentId是否为父节点，否就修改为是;
        TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
        if(!parent.getIsParent()){
            parent.setIsParent(true);
            contentCategoryMapper.updateByPrimaryKey(parent);
        }
        return E3Result.ok(contentCategory);
    }
}
