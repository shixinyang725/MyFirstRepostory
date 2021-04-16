package com.lening.service;

import com.lening.entity.MeunBean;
import com.lening.entity.PostBean;
import com.lening.entity.UserBean;
import com.lening.utils.Page;

import java.util.List;
import java.util.Set;

/**
 * @ClassName: UserService
 * @Author: PanJunShuang
 * @Date: 2021/4/6 19:30
 * @Version: V1.0
 **/
public interface UserService {
    Page<UserBean> getUserList(Integer pageNum, Integer pageSize, UserBean userBean);

    List<MeunBean> getMeunList(UserBean ub);

    UserBean toUserDept(Long id);

    void saveUserDept(Long id, Long[] deptids);

    UserBean toUserPost(Long id);

    void saveUserPost(UserBean userBean);

    UserBean getLogin(UserBean ub);

    Set<String> getUserMeunUrlsById(UserBean ub);

    List<PostBean> getDeputyPosition(Long id);
}
