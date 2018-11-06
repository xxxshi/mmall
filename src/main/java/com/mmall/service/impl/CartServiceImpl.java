package com.mmall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.service.ICartService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: mmall
 * @description:
 * @author: xxxshi
 * @create: 2018-11-02 14:00
 * @Version:
 **/
@Service("iCartService")
public class CartServiceImpl implements ICartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    protected ProductMapper productMapper;



    /**
     * 购物车添加商品
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    @Override
    public ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count) {
        if(productId == null || count == null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT
                    .getDesc());
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        if (cart == null) {
            //购物车中没有该商品记录
            Cart cartItem = new Cart();
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setQuantity(count);
            cartItem.setChecked(1);
            cartMapper.insertSelective(cartItem);
        }else{
            int newCount = cart.getQuantity()+count;
            cart.setQuantity(newCount);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    /**
     *更新购物车内某种商品的数量
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    public ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count) {
        if (productId == null || count == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT
                    .getDesc());
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        if (cart != null) {
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }

        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);

    }

    /**
     * 删除购物车内某种商品
     * @param userId
     * @param productIds
     * @return
     */
    public ServerResponse<CartVo> delete(Integer userId,String productIds){
        List<String> productList = Splitter.on(",").splitToList(productIds);
        if(CollectionUtils.isEmpty(productList)){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT
                    .getDesc());
        }
        cartMapper.deleteByUserIdProducts(userId, productList);
        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    /**
     * 查询，展示
     * @param userId
     * @return
     */
    public ServerResponse<CartVo> list (Integer userId){
        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    /**
     * 全选，全部不选，单选，单反选
     * @param userId
     * @param productId
     * @param checked
     * @return
     */
    public ServerResponse<CartVo> selectOrUnSelect (Integer userId,Integer productId,Integer checked){
        cartMapper.checkedOrUncheckedProduct(userId,productId,checked);
        return this.list(userId);
    }

    /**
     *查询购物车的商品数量
     * @param userId
     * @return
     */
    public ServerResponse<Integer> getCartProductCount(Integer userId){
        if(userId == null){
            return ServerResponse.createBySuccess(0);
        }
        return ServerResponse.createBySuccess(cartMapper.selectCartProductCount(userId));
    }






    /**
     * 辅助方法
     * 获得该用户的购物车信息
     * @param userId
     * @return
     */
    private CartVo getCartVoLimit(Integer userId) {
        //展示给用户的对象
        CartVo cartVo = new CartVo();
        //用户全部的原始的购物车信息
        System.out.println(cartMapper);
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        // 1.在for循环中，不断设置List<CartProductVo> cartProductVoList的各个元素具体的信息
        // 2.最终将List<CartProductVo> cartProductVoList作为cartvo的属性
        List<CartProductVo> cartProductVoList = Lists.newArrayList();//// CartProductVo:结合了产品和购物车的一个抽象对象,就是在购物车内的一种商品信息，
        BigDecimal cartTotalPrice = new BigDecimal("0");//一个用户购物车的总价

        if (CollectionUtils.isNotEmpty(cartList)) {
            for (Cart cartItem : cartList) {//cartItem:用户某一种原始的购物车商品信息
                //1.在for循环中，不断设置List<CartProductVo> cartProductVoList的各个元素具体的信息
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(cartItem.getUserId());
                cartProductVo.setProductId(cartItem.getProductId());
                //判断这个商品存在,并且获得该种商品的具体信息,继续设置
                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                System.out.println(product);
                if (product != null) {
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());
                    //判断库存数量是否满足购物车内数量，
                    int buyLimitCount = 0;
                    if(product.getStock() >= cartItem.getQuantity()){
                        //库存充足的时候,购买没有受到限制
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    }else{//库存充足的时候,购买受到限制,
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        //购物车中更新有效库存,这里更新了数据库里数据
                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setId(cartItem.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }
                    //经过判断库存是否充足后，设置这种购物车内商品的数量
                    cartProductVo.setQuantity(buyLimitCount);
                    //计算购物车内这种商品的总价
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartProductVo.getQuantity()));
                    cartProductVo.setProductChecked(cartItem.getChecked());
                }
                if(Const.Cart.CHECKED==cartItem.getChecked()){
                    //如果已经勾选,增加到整个的购物车总价中
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),cartProductVo.getProductTotalPrice().doubleValue());
                }
                if(product!=null){
                    cartProductVoList.add(cartProductVo);

                }
            }
            //2.最终将List<CartProductVo> cartProductVoList作为cartvo的属性
            cartVo.setCartTotalPrice(cartTotalPrice);
            cartVo.setCartProductVoList(cartProductVoList);
            //判断用户购物车内商品是否全选
            cartVo.setAllChecked(this.getAllCheckedStatus(userId));
            cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));

            return cartVo;
        }

        return null;

    }

    /**
     * 辅助方法
     * 判断用户购物车内商品是否全选
     * @param userId
     * @return
     */
    private boolean getAllCheckedStatus(Integer userId){
        if(userId == null){
            return false;
        }
        return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;

    }

}
