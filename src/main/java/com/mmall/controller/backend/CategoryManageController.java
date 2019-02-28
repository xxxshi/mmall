package com.mmall.controller.backend;
/**
 *
 * Created by Administrator on 2018/10/17 0017.
 */

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @program: mmall
 * @description: 商品类别控制器
 * @author: xxxshi
 * @create: 2018-10-17 11:14
 * @Version: 1.0
 **/
@Controller
@RequestMapping("/manage/category/")
public class CategoryManageController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 增添商品分类--控制层
     * @param session
     * @param categoryName
     * @param parentId
     * @return
     */
    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse<String> addCategory(HttpSession session,String categoryName,@RequestParam(value = "parentId", defaultValue = "0") Integer parentId){
//        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
//        if(currentUser==null){
//            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，无法添加分类");
//        }
//        if(iUserService.checkIsAdmin((currentUser))){
//            return iCategoryService.addCategory(categoryName,parentId);
//
//        }
//        return ServerResponse.createByErrorMsg("您不是管理员，无权进行商品分类添加");
        //通过拦截器
        return iCategoryService.addCategory(categoryName,parentId);
    }

    /**
     * 修改商品分类名称
     * @param session
     * @param categoryId
     * @param categoryName
     * @return
     */
    @RequestMapping("set_category_name.do")
    @ResponseBody
    public ServerResponse<String> setCategoryName(HttpSession session , Integer categoryId, String categoryName) {
//        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
//        if(currentUser==null){
//            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，无法添加分类");
//        }
//        if(iUserService.checkIsAdmin((currentUser))){
//            return iCategoryService.setCategoryName(categoryId, categoryName);
//        }
//        return ServerResponse.createByErrorMsg("您不是管理员，无权修改商品品类名称");

        return iCategoryService.setCategoryName(categoryId, categoryName);
    }

    /**
     * 获得商品分类节点的子节点（平级，不递归）--控制层
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping("get_category.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpSession session, @RequestParam(value="categoryId", defaultValue="0") Integer categoryId) {
//        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
//        if(currentUser==null){
//            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，无法添加分类");
//        }
//        if(iUserService.checkIsAdmin((currentUser))){
//            return iCategoryService.getChildrenParallelCategory(categoryId);
//        }
//        return ServerResponse.createByErrorMsg("您不是管理员，无权获得商品分类");

        return iCategoryService.getChildrenParallelCategory(categoryId);
    }

    /**
     * 获得商品分类节点的全部子节点（递归）--控制层
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryDeepChildrenParallelCategory(HttpSession session, Integer categoryId) {
//        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
//        if(currentUser==null){
//            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，无法添加分类");
//        }
//        if(iUserService.checkIsAdmin((currentUser))){
//            return iCategoryService.getCategoryAndChildrenById(categoryId);
//        }
//        return ServerResponse.createByErrorMsg("您不是管理员，无权获得商品分类");

        return iCategoryService.getCategoryAndChildrenById(categoryId);

    }
}
