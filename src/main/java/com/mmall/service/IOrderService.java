package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;

import java.util.Map;

/**
 *
 * Created by Administrator on 2018/11/4 0004.
 */
public interface IOrderService {
    //支付模块----------------------------------------------------------------------------------------------------------
    /**
     * 支付宝当面付 -- 业务层
     * @param orderNo
     * @param userId
     * @param path
     * @return
     */
    ServerResponse pay(Long orderNo, Integer userId, String path);

    /**
     * 支付宝回调的业务逻辑 -- 业务层
     * @param params
     * @return
     */
    ServerResponse aliCallback(Map<String,String> params);

    /**
     *查询用户谋订单的支付状态 -- 业务层
     * @param userId
     * @param orderNo
     * @return
     */
    ServerResponse queryOrderPayStatus(Integer userId,Long orderNo);

    //前台业务逻辑-----------------------------------------------------------------------------------------------------
    /**
     * 创建订单 -- 业务层
     * @param userId
     * @param shippingId
     * @return
     */
    ServerResponse createOrder(Integer userId,Integer shippingId);

    /**
     *取消订单 -- 业务层
     * @param userId
     * @param orderNo
     * @return
     */
    ServerResponse<String> cancel(Integer userId, Long orderNo);

    /**
     * 获得购物车内选中的商品信息
     * @param userId
     * @return
     */
    ServerResponse getOrderCartProduct(Integer userId);

    /**
     * 获得订单详细信息 --业务层
     * @param userId
     * @param orderNo
     * @return
     */
    ServerResponse getOrderDetail(Integer userId, Long orderNo);

    /**
     * 获得某用户的订单列表 --业务层
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);

    //后台管理业务逻辑---------------------------------------------------------------------------------------------------
    /**
     * 管理员获得订单列表 --业务层
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> manageList(int pageNum, int pageSize);


    /**
     *管理员通过订单号获得订单详情
     * @param orderNo
     * @return
     */
    ServerResponse manageDetail(Long orderNo);

    /**
     * 管理员查询订单
     * @param orderNo
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse manageSearch(Long orderNo,int pageNum,int pageSize);

    /**
     * 管理员发货
     * @param orderNo
     * @return
     */
    ServerResponse<String> manageSendGoods(Long orderNo);
}
