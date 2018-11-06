package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;

public interface ICategoryService {
    /**
     * 增添商品分类--业务层
     * @param categoryName
     * @param parentId
     * @return
     */
    ServerResponse<String> addCategory(String categoryName,Integer parentId);

    /**
     * 修改商品分类名称--业务层
     * @param categoryId
     * @param categoryName
     * @return
     */
    ServerResponse<String> setCategoryName(Integer categoryId, String categoryName);

    /**
     * 获得商品分类节点的子节点（平级，不递归）--业务层
     * @param parentId
     * @return
     */
    ServerResponse<List<Category>> getChildrenParallelCategory(Integer parentId);

    /**
     * 获得本节点及子节点的Id(递归获得全部子节点)--业务层
     * @param categoryId
     * @return
     */
    ServerResponse<List<Integer>> getCategoryAndChildrenById(Integer categoryId);
}
