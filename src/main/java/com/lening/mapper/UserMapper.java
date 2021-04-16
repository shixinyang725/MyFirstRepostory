package com.lening.mapper;

import com.lening.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface UserMapper {
    long countByExample(UserBeanExample example);

    int deleteByExample(UserBeanExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserBean record);

    int insertSelective(UserBean record);

    List<UserBean> selectByExample(UserBeanExample example);

    UserBean selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserBean record, @Param("example") UserBeanExample example);

    int updateByExample(@Param("record") UserBean record, @Param("example") UserBeanExample example);

    int updateByPrimaryKeySelective(UserBean record);

    int updateByPrimaryKey(UserBean record);

    Long[] getUserDeptidsById(Long id);

    void deleteUserPost(Long userid);

    void deleteUserDept(Long userid);

    void addUserDept(@Param("userid") Long userid, @Param("deptid") Long deptid);

    List<DeptBean> selectDeptById(Long id);

    List<PostBean> selectPostByDeptid(Long id);

    void deleteUserPostById(Long id);

    void saveUserPost(@Param("userid") Long id, @Param("postid") Long postid);

    List<UserBean> getLogin(UserBean ub);

    List<MeunBean> getUserMeunListById(Long id);

    Set<String> getUserMeunUrlsById(Long id);

    List<PostBean> getDeputyPosition(Long id);
}