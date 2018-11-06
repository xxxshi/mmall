package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @program: mmall
 * @description: 商品类别业务逻辑实现类
 * @author: xxxshi
 * @create: 2018-10-17 11:18
 * @Version: 1.0
 **/
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService{
    private org.slf4j.Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 增添商品分类--业务层
     * @param categoryName
     * @param parentId
     * @return
     */
    @Override
    public ServerResponse<String> addCategory(String categoryName, Integer parentId) {
        if(StringUtils.isBlank(categoryName)||StringUtils.isBlank(parentId.toString())){
            return ServerResponse.createByErrorMsg("商品类别名称或父节点的Id为空");
        }
        Category addCategory = new Category();
        addCategory.setName(categoryName);
        addCategory.setParentId(parentId);
        addCategory.setStatus(true);
        int resultCount = categoryMapper.insertSelective(addCategory);
        if(resultCount>0){
            return ServerResponse.createSuccessByMeg("增添新的商品类别成功");
        }
        return ServerResponse.createByErrorMsg("增添新的商品类别失败");
    }

    /**
     * 修改商品分类名称--业务层
     * @param categoryId
     * @param categoryName
     * @return
     */
    @Override
    public ServerResponse<String> setCategoryName(Integer categoryId, String categoryName) {
        if(StringUtils.isBlank(categoryName)||StringUtils.isBlank(categoryId.toString())){
            return ServerResponse.createByErrorMsg("商品类别名称或节点的Id为空");
        }
        Category updateCategory = new Category();
        updateCategory.setName(categoryName);
        updateCategory.setId(categoryId);
        int resultCount = categoryMapper.updateByPrimaryKeySelective(updateCategory);
        if (resultCount > 0) {
            return ServerResponse.createSuccessByMeg("修改商品分类名称成功");
        }
        return ServerResponse.createByErrorMsg("修改商品分类名称失败");
    }

    /**
     * 获得商品分类节点的子节点（平级，不递归）--业务层
     * @param parentId
     * @return
     */
    @Override
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer parentId) {
        System.out.println(parentId);
        List<Category> categoryList = categoryMapper.selectChildenParallelCategory(parentId);
        if (CollectionUtils.isEmpty(categoryList)) {
            logger.info("未找到商品分类子节点");
        }

        return ServerResponse.createBySuccess(categoryList);
    }

    /**
     * 获得本节点及子节点的Id(递归获得全部子节点)--业务层
     * @param categoryId
     * @return
     */
    @Override
    public ServerResponse<List<Integer>> getCategoryAndChildrenById(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        findChildrenCategory(categorySet, categoryId);

        List<Integer> categoryList = Lists.newArrayList();
        if (categorySet != null) {
            for (Category categoryItem : categorySet) {
                categoryList.add(categoryItem.getId());

            }
        }
        categoryList.add(categoryId);
        return ServerResponse.createBySuccess(categoryList);
    }

    /**
     * 递归函数，
     * @param categorySet
     * @param categoryId
     * @return
     */
    public Set<Category> findChildrenCategory(Set<Category> categorySet, Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {

            categorySet.add(category);
        }
        //递归出口，因为调用mybatis 查询结果为空时，无需验证null异常
        List<Category> categoryList = categoryMapper.selectChildenParallelCategory(categoryId);
        for (Category categoryItem : categoryList) {
            findChildrenCategory(categorySet, categoryItem.getId());
        }
        return categorySet;
    }
}
