package cn.hpn.cart.controller;

import cn.hpn.cart.service.CartService;
import cn.hpn.pojo.TbItem;
import cn.hpn.pojo.TbUser;
import cn.hpn.service.ItemService;
import cn.hpn.utils.CookieUtils;
import cn.hpn.utils.E3Result;
import cn.hpn.utils.JsonUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private ItemService itemService;
    @Value("${COOKIE_TIME_EXPIRE}")
    private Integer COOKIE_TIME_EXPIRE;
    @Autowired
    private CartService cartService;


    @RequestMapping("/cart/add/{itemId}")
    public String cartPage(@PathVariable Long itemId , @RequestParam(defaultValue = "1") Integer num,
                           HttpServletRequest request , HttpServletResponse response){
        //获取用户时候登录；
        TbUser user = (TbUser) request.getAttribute("user");
        if(user!=null){
            //用户已经登录;
            //合并cookie数据到数据库；
            cartService.addCartItemToRedis(user.getId(), itemId, num);
            //返回逻辑视图;
            return "cartSuccess";
        }

        //没有登录将商品添加到cookie
        List<TbItem> cartList = getCookieForItemListById(request);
        //设置标志位；判断是否已经在cookie中存在商品
        Boolean flag = false;
        //cookie中有商品需要合并
        for (TbItem item : cartList) {
            //在cookie中找到了添加购物车的商品，需要进行合并数量
            if(item.getId().intValue() == itemId){
                item.setNum(item.getNum() + num);
                flag = true;
                break;
            }
        }
        //商品不存在cookie中
        if(!flag){
            TbItem item = itemService.getItemById(itemId);
            item.setNum(num);
            String image = item.getImage();
            if(image != null){
                item.setImage(image.split(",")[0]);
            }
            cartList.add(item);
        }
        //将商品添加到cookie中
        CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList),COOKIE_TIME_EXPIRE,true);
        return "cartSuccess";
    }

    public List<TbItem> getCookieForItemListById(HttpServletRequest request){
        String cart = CookieUtils.getCookieValue(request, "cart", true);
        //cookie中没有商品数据
        if(StringUtils.isBlank(cart)){
            return new ArrayList<>();
        }
        List<TbItem> cartList = JsonUtils.jsonToList(cart, TbItem.class);
        return cartList;
    }

    /**
     * 购物车展示页面
     * @param request
     * @return
     */
    @RequestMapping("/cart/cart")
    public String cartItem(HttpServletRequest request ,HttpServletResponse response){
        List<TbItem> cartList = getCookieForItemListById(request);
        //判断用户是否登录;
        TbUser user = (TbUser) request.getAttribute("user");
        if(user!=null){
            //有用户登录需要合并未登录的token消息;
           cartService.mergeCart(user.getId(), cartList);
           //删除cookie
            CookieUtils.deleteCookie(request, response, "cart");
            //根据用户Id从redis中获取购物车信息;
            cartList = cartService.getItemForRedisById(user.getId());
        }

        //没有登录；从cookie中获取添加购物车的商品；
        request.setAttribute("cartList", cartList);
        //返回逻辑视图
        return "cart";
    }

    /**
     * 修改购物车商品的数量
     * @param itemId
     * @param num
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public E3Result updateItemNum(@PathVariable Long itemId , @PathVariable Integer num ,
                                  HttpServletRequest request , HttpServletResponse response){
        List<TbItem> itemList = getCookieForItemListById(request);
        //判断用户是否登录;
        TbUser user = (TbUser) request.getAttribute("user");
        if(user!=null){
            //从redis中获取数据，修改数量；
            cartService.updateRedisForItemNum(user.getId(), itemId, num);
            return E3Result.ok();
        }
        //未登录，查询cookie 将cookie中商品数量修改；
        for (TbItem item : itemList) {
            if(item.getId().intValue() == itemId){
                item.setNum(num);
                break;
            }
        }
        //将商品写回去cookie
        CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(itemList),COOKIE_TIME_EXPIRE ,true);
        return E3Result.ok();

    }

    /**
     * 删除购物车商品
     * @param itemId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/cart/delete/{itemId}")
    public String deleteItemForCart(@PathVariable Long itemId ,
                                    HttpServletRequest request , HttpServletResponse response){
        //判断用户是否登录;
        TbUser user = (TbUser) request.getAttribute("user");
        //已经登录
        if(user!=null){
            cartService.deleterRedisForItem(user.getId(), itemId);
            return "redirect:/cart/cart.html";
        }
        //未登录
        List<TbItem> itemList = getCookieForItemListById(request);
        for (TbItem item : itemList) {
            if(item.getId().intValue() == itemId){
                itemList.remove(item);
                break;
            }
        }
        CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(itemList),COOKIE_TIME_EXPIRE , true);
        return "redirect:/cart/cart.html";

    }

}
