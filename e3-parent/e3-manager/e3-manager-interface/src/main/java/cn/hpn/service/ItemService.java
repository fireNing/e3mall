package cn.hpn.service;

import cn.hpn.pojo.EasyUIDataGridResult;
import cn.hpn.pojo.TbItem;
import cn.hpn.utils.E3Result;

public interface ItemService {

    public EasyUIDataGridResult getItemList(int page ,int rows);

    //添加商品;
    public E3Result saveItem(TbItem item ,String desc);

    //删除商品;
   public E3Result deleteItem(long[] itemIds);

   //下架商品；
    public E3Result downItem(long[] itemIds);

    //根据商品Id查询商品；
    public TbItem getItemById(long itemId);

}
