package com.lening.controller;

import com.lening.entity.MeunBean;
import com.lening.entity.PostBean;
import com.lening.entity.UserBean;
import com.lening.service.UserService;
import com.lening.utils.Page;
import com.lening.utils.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * @ClassName: UserController
 * @Author: PanJunShuang
 * @Date: 2021/4/6 19:30
 * @Version: V1.0
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //全查人员+分页+搜索
    @RequestMapping("/getUserList")
    public Page<UserBean> getUserList(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "3") Integer pageSize, @RequestBody UserBean userBean){
        Page<UserBean> userList = userService.getUserList(pageNum, pageSize, userBean);
        return userList;
    }

    //登录
    @RequestMapping("/getLogin")
    public ResultInfo getLogin(@RequestBody UserBean ub, HttpServletRequest request){
        UserBean userBean = userService.getLogin(ub);
        if(userBean==null){
            return new ResultInfo(false,"登录失败，用户名或者密码错误！");
        }else{
            request.getSession().setAttribute("ub", userBean);
            return new ResultInfo(true,"登录成功");
        }
    }

    //获取菜单数据
    @RequestMapping("/getMeunList")
    public List<MeunBean> getMeunList(HttpServletRequest request){
        /**
         * 在这里需要知道，是谁登录的（使用主职位登录）
         * 还要查询出不是按钮的菜单
         */
        UserBean ub = (UserBean) request.getSession().getAttribute("ub");

        /**
         * 查询这个用户有哪些url
         */
        Set<String> urls = userService.getUserMeunUrlsById(ub);
        request.getSession().setAttribute("urls", urls);

        return userService.getMeunList(ub);
    }

    //分配科室（回显）
    @RequestMapping("/toUserDept")
    public  UserBean toUserDept(Long id){
        return userService.toUserDept(id);
    }

    //保存科室
    @RequestMapping("/saveUserDept")
    public ResultInfo saveUserDept(Long id,@RequestBody Long[] deptids){
        try {
            userService.saveUserDept(id,deptids);
            return new ResultInfo(true, "编辑成功");
        }catch (Exception e){
            return new ResultInfo(false, "编辑失败");
        }
    }

    @RequestMapping("/toUserPost")
    public UserBean toUserPost(Long id){
        return userService.toUserPost(id);
    }

    @RequestMapping("/saveUserPost")
    public ResultInfo saveUserPost(@RequestBody UserBean userBean){
        try {
            userService.saveUserPost(userBean);
            return new ResultInfo(true, "编辑成功");
        }catch (Exception e){
            return new ResultInfo(false, "编辑失败");
        }
    }

    @RequestMapping("/getLoginUser")
    public UserBean getLoginUser(HttpServletRequest request){
        UserBean ub = (UserBean) request.getSession().getAttribute("ub");
        return ub;
    }

    @RequestMapping("/getDeputyPosition")
    public List<PostBean> getDeputyPosition(HttpServletRequest request){
        UserBean ub = (UserBean) request.getSession().getAttribute("ub");
        return userService.getDeputyPosition(ub.getId());
    }
}
