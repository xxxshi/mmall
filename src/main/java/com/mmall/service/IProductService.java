package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;


public interface IProductService {

    /**
     * 保存商品信息（更新或新增）--业务层
     * @param product
     * @return
     */
    ServerResponse saveOrUpdateProduct(Product product);

    /**
     * 改变商品销售状态--业务层
     * @param productId
     * @param productStatus
     * @return
     */
    ServerResponse setSaleStatus(Integer productId, Integer productStatus);

    /**
     * 获得商品详细信息--业务层
     * @param productId
     * @return
     */
    ServerResponse manageProductDetail(Integer productId);

    /**
     *后台商品动态分页--业务层
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize);

    /**
     * 后台查询查询商品--业务层
     * @param productId
     * @param productName
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> getProductListByIdAndName(Integer productId,String productName,Integer pageNum, Integer pageSize);

    /**
     * 通过商品id获得商品细节信息
     * @param productId
     * @return
     */
    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    /**
     *前台商品搜索，动态排序功能开发 --业务层
     * @param keyword
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,
                                                         String orderBy);

}
