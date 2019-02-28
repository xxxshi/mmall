package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: mmall
 * @description: 商品控制器
 * @author: xxxshi
 * @create: 2018-10-18 12:14
 * @Version: 1.0
 **/
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;

    /**
     * 保存商品信息（更新或新增）--控制层
     * @param session
     * @param product
     * @return
     */
    @RequestMapping("/save.do")
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product) {
//        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
//        if(currentUser==null){
//            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，无法添加分类");
//        }
//        if(iUserService.checkIsAdmin((currentUser))){
//            return iProductService.saveOrUpdateProduct(product);
//        }else{
//            return ServerResponse.createByErrorMsg("您不是管理员，进行商品添加或更新");
//
//        }

        return iProductService.saveOrUpdateProduct(product);
    }

    /**
     * 改变商品的销售状态--控制层
     * @param session
     * @param productId
     * @param productStatus
     * @return
     */
    @RequestMapping("/set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, Integer productId,Integer productStatus) {
//        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
//        if(currentUser==null){
//            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"进行商品添加或更新");
//        }
//        if(iUserService.checkIsAdmin((currentUser))){
//            return iProductService.setSaleStatus(productId, productStatus);
//        }else{
//            return ServerResponse.createByErrorMsg("您不是管理员，进行商品添加或更新");
//
//        }

        return iProductService.setSaleStatus(productId, productStatus);
    }

    /**
     * 获得商品的详细信息--控制层
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping("/detail.do")
    @ResponseBody
    public ServerResponse getProductDetail(HttpSession session, Integer productId) {
//        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
//        if(currentUser==null){
//            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，无权获得商品的详细信息");
//        }
//        if(iUserService.checkIsAdmin((currentUser))){
//            return iProductService.manageProductDetail(productId);
//        }else{
//            return ServerResponse.createByErrorMsg("您不是管理员，无权获得商品的详细信息");
//
//        }

        return iProductService.manageProductDetail(productId);
    }

    /**
     * 后台商品动态分页--控制层
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/list.do")
    @ResponseBody
    public ServerResponse getProductList(HttpSession session,@RequestParam(value = "pageNum",defaultValue = "1")
    Integer pageNum,@RequestParam(value = "pageSize",defaultValue = "10")Integer
            pageSize) {

        return iProductService.getProductList(pageNum, pageSize);

    }

    /**
     * 后台查询商品功能--控制层
     * @param session
     * @param productId
     * @param productName
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/search.do")
    @ResponseBody
    public ServerResponse searchProductList(HttpSession session, Integer productId,String productName,@RequestParam
            (value = "pageNum",defaultValue = "1")Integer pageNum, @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize) {
//        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
//        if(currentUser==null){
//            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，无权查询商品信息");
//        }
//        if(iUserService.checkIsAdmin((currentUser))){
//            return iProductService.getProductListByIdAndName(productId, productName, pageNum, pageSize);
//        }else{
//            return ServerResponse.createByErrorMsg("您不是管理员，无权查询商品信息");
//
//        }

        return iProductService.getProductListByIdAndName(productId, productName, pageNum, pageSize);
    }

    /**
     * 上传文件--控制层
     * @param session
     * @param file
     * @param request
     * @return
     */
    @RequestMapping("/upload.do")
    @ResponseBody
    public ServerResponse uploadFile(HttpSession session, @RequestParam("file") MultipartFile file,
                                     HttpServletRequest request) {
//        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
//        if(currentUser==null){
//            return ServerResponse.createByErrorCodeMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，无权上传商品文件");
//        }
//        if(iUserService.checkIsAdmin((currentUser))){
//            //上传到/upload内
//            String path = request.getSession().getServletContext().getRealPath("upload");
//            String targetFileName = iFileService.upload(file,path);
//            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
//            Map<String,String> map = new HashMap();
//            map.put("uri",targetFileName);
//            map.put("url", url);
//            return ServerResponse.createBySuccess(map);
//        }else{
//            return ServerResponse.createByErrorMsg("您不是管理员，无权上传商品文件");
//        }

        //上传到/upload内
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file,path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
        Map<String,String> map = new HashMap();
        map.put("uri",targetFileName);
        map.put("url", url);
        return ServerResponse.createBySuccess(map);
    }

    /**
     * 上传富文本文件--控制层
     *和前端约定返回simditor
     * {
     *     "success",
     *     "msg",
     *     "file_path",
     * }
     * @param session
     * @param file
     * @param request
     * @return
     */
    @RequestMapping("/richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpload(HttpSession session, HttpServletResponse response,MultipartFile file,
                                HttpServletRequest
            request) {
        Map returnMap = Maps.newHashMap();
//        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
//        if(currentUser==null){
//            returnMap.put("success", false);
//            returnMap.put("msg", "请登陆管理员");
//            return returnMap;
//        }
//        if(iUserService.checkIsAdmin((currentUser))){
//            //上传到/upload内
//            String path = request.getSession().getServletContext().getRealPath("upload");
//            String targetFileName = iFileService.upload(file,path);
//            if (StringUtils.isBlank(targetFileName)) {
//                returnMap.put("success", false);
//                returnMap.put("msg", "上传文件失败");
//                return returnMap;
//            }
//            String url = PropertiesUtil.getProperty("ftp.server.http.prefix");
//            returnMap.put("success", true);
//            returnMap.put("msg", "上传文件成功");
//            //和前端的约定
//            response.setHeader("Access-Control-Allow-Headers","X-File-Name");
//            returnMap.put("file_path",url);
//            return returnMap;
//        }else{
//            returnMap.put("success", false);
//            returnMap.put("msg", "不是管理员，无上传商品文件权限");
//            return returnMap;
//        }

        //上传到/upload内
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file,path);
        if (StringUtils.isBlank(targetFileName)) {
            returnMap.put("success", false);
            returnMap.put("msg", "上传文件失败");
            return returnMap;
        }
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix");
        returnMap.put("success", true);
        returnMap.put("msg", "上传文件成功");
        //和前端的约定
        response.setHeader("Access-Control-Allow-Headers","X-File-Name");
        returnMap.put("file_path",url);
        return returnMap;
    }



}
