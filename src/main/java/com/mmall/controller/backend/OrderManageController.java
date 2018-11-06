package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @program: mmall
 * @description: 后台管理订单
 * @author: xxxshi
 * @create: 2018-11-05 17:22
 * @Version:
 **/
@Controller
@RequestMapping("/manage/order")
public class OrderManageController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IOrderService iOrderService;
    /**
     * 获得用户订单列表 -- 控制层
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse manageList(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int
            pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser==null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，无法添加分类");
        }
        if(iUserService.checkIsAdmin((currentUser))){
            //管理员操作
            return iOrderService.manageList(pageNum, pageSize);

        }else{
            return ServerResponse.createByErrorMsg("您不是管理员，进行商品添加或更新");

        }
    }

    /**
     * 获得用户订单列表 -- 控制层
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse manageDetail(HttpSession session,Long orderNo) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser==null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，无法添加分类");
        }
        if(iUserService.checkIsAdmin((currentUser))){
            //管理员操作
            return iOrderService.manageDetail(orderNo);

        }else{
            return ServerResponse.createByErrorMsg("您不是管理员，进行商品添加或更新");

        }
    }

    /**
     * 管理员查询订单--控制器
     * @param session
     * @param orderNo
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderSearch(HttpSession session, Long orderNo, @RequestParam(value = "pageNum",
            defaultValue
            = "1") int
            pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser==null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，无法添加分类");
        }
        if(iUserService.checkIsAdmin((currentUser))){
            //管理员操作
            return iOrderService.manageSearch(orderNo, pageNum, pageSize);

        }else{
            return ServerResponse.createByErrorMsg("您不是管理员，进行商品添加或更新");

        }
    }

    /**
     * 管理员发货 --控制器
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping("send_goods.do")
    @ResponseBody
    public ServerResponse manageSendGoods(HttpSession session,Long orderNo) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser==null){
            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，无法添加分类");
        }
        if(iUserService.checkIsAdmin((currentUser))){
            //管理员操作
            return iOrderService.manageSendGoods(orderNo);

        }else{
            return ServerResponse.createByErrorMsg("您不是管理员，进行商品添加或更新");

        }
    }


}
