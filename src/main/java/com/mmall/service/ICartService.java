package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVo;


public interface ICartService {
    /**
     * 购物车添加商品
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);

    /**
     * 更新购物车内商品数量
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);

    /**
     * 删除购物车内某种商品
     * @param userId
     * @param productIds
     * @return
     */
    ServerResponse<CartVo> delete(Integer userId,String productIds);

    /**
     *查询，展示
     * @param userId
     * @return
     */
    ServerResponse<CartVo> list (Integer userId);

    /**
     *全选，全部不选，单选，单反选
     * @param userId
     * @param productId
     * @param checked
     * @return
     */
    ServerResponse<CartVo> selectOrUnSelect (Integer userId,Integer productId,Integer checked);
    /**
     *查询购物车的商品数量
     * @param userId
     * @return
     */
    ServerResponse<Integer> getCartProductCount(Integer userId);
}
