package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @program: mmall
 * @description:
 * @author: xxxshi
 * @create: 2018-11-02 20:35
 * @Version:
 **/
@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {
    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ServerResponse add(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int result = shippingMapper.insertSelective(shipping);
        if (result > 0) {
            Map map = Maps.newHashMap();
            map.put("shippingId", shipping.getId());
            return ServerResponse.createBySuccess("新建收货地址成功", map);
        }
        return ServerResponse.createByErrorMsg("新建收货地址失败");
    }

    @Override
    public ServerResponse<String> delete(Integer userId, Integer shippingId) {
        int deleteResult = shippingMapper.deleteByShippingIdUserId(userId, shippingId);
        if (deleteResult > 0) {
            return ServerResponse.createBySuccess("删除地址成功");
        }else {
            return ServerResponse.createByErrorMsg("删除地址失败");
        }
    }

    @Override
    public ServerResponse<String> update(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int result = shippingMapper.updateByShipping(shipping);
        if (result > 0) {
            return ServerResponse.createBySuccess("更新收货地址成功");
        }
        return ServerResponse.createByErrorMsg("更新收货地址失败");
    }

    @Override
    public ServerResponse<Shipping> search(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectByShippingIdUserId(userId, shippingId);
        if (shipping != null) {
            return ServerResponse.createBySuccess(shipping);
        }
        return ServerResponse.createByErrorMsg("查询收货地址失败");
    }

    /**
     * 收货地址分页
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }

}
