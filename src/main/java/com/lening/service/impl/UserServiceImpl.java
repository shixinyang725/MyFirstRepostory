package com.lening.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lening.entity.*;
import com.lening.mapper.DeptMapper;
import com.lening.mapper.MeunMapper;
import com.lening.mapper.UserMapper;
import com.lening.redis.RedisUtil;
import com.lening.service.UserService;
import com.lening.utils.MD5key;
import com.lening.utils.Page;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @ClassName: UserServiceImpl
 * @Author: PanJunShuang
 * @Date: 2021/4/6 19:30
 * @Version: V1.0
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MeunMapper meunMapper;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Page<UserBean> getUserList(Integer pageNum,Integer pageSize,UserBean userBean) {

        //开启分页
        PageHelper.startPage(pageNum, pageSize);
        
        //条件查询
        UserBeanExample example = new UserBeanExample();
        UserBeanExample.Criteria criteria = example.createCriteria();
        if(userBean!=null){
            //判断姓名
            if(userBean.getUname()!=null && userBean.getUname().length()>=1){
                criteria.andUnameLike("%"+userBean.getUname()+"%");
            }

            //判断年龄
            if(userBean.getAge()!=null){
                criteria.andAgeGreaterThanOrEqualTo(userBean.getAge());
            }

            if(userBean.getEage()!=null){
                criteria.andAgeLessThanOrEqualTo(userBean.getEage());
            }

        }

        List<UserBean> userBeans = userMapper.selectByExample(example);
        PageInfo<UserBean> pageInfo = new PageInfo<>(userBeans);
        Long total = pageInfo.getTotal();
        Page<UserBean> page = new Page<UserBean>(pageInfo.getPageNum()+"",total.intValue(),pageInfo.getPageSize()+"");
        page.setList(userBeans);
        return page;
    }

    @Override
    public List<MeunBean> getMeunList(UserBean ub) {
        if(ub!=null){

            List<MeunBean> list =  userMapper.getUserMeunListById(ub.getId());

            return list;
        }
        return null;
    }


    public List<MeunBean> getMeunList3(UserBean ub) {
        if(ub!=null){
            List<MeunBean> list = null;

            //从缓存中获取列表
            Object userListCache = redisUtil.getObject(ub.getId());

            //判断缓存中是否存在
            if(userListCache!=null){
                System.out.println("redis中存在，直接返回");
                list = (List<MeunBean>) userListCache;
            }else{
                System.out.println("redis中不存在，从数据库中获取");
                //查询数据库，取出数据
                list = userMapper.getUserMeunListById(ub.getId());
                //放入redis缓存
                redisUtil.putObject(ub.getId(), list);
            }
            return list;
        }
        return null;
    }

    //只查看不是按钮的菜单
    public List<MeunBean> getMeunList2(UserBean ub) {
        if(ub!=null){
            //菜单先展示不是按钮的菜单
            MeunBeanExample meunBeanExample = new MeunBeanExample();
            MeunBeanExample.Criteria criteria = meunBeanExample.createCriteria();
            criteria.andIsbuttonEqualTo(0);
            List<MeunBean> list = meunMapper.selectByExample(meunBeanExample);
            return list;
        }
        return null;
    }

    @Override
    public UserBean toUserDept(Long id) {
        //根据id查询一条用户数据
        UserBean userBean = userMapper.selectByPrimaryKey(id);

        //全查所有科室
        List<DeptBean> deptBeans = deptMapper.selectByExample(null);
        userBean.setDeptBeans(deptBeans);

        //查询已经选中的科室
        Long[] userDeptidsById = userMapper.getUserDeptidsById(id);
        userBean.setDeptids(userDeptidsById);

        return userBean;
    }

    @Override
    public void saveUserDept(Long id, Long[] deptids) {
        //删除职位
        userMapper.deleteUserPost(id);

        //删除科室
        userMapper.deleteUserDept(id);

        //添加科室
        if(deptids!=null && deptids.length>=1){
            for (Long deptid : deptids) {
                userMapper.addUserDept(id, deptid);
            }
        }
    }

    @Override
    public UserBean toUserPost(Long id) {
        //根据id查询一条用户数据
        UserBean userBean = userMapper.selectByPrimaryKey(id);

        //查询用户部门中间表查看用户是否有部门
        List<DeptBean> deptBeans = userMapper.selectDeptById(id);

        //如果用户已选择科室，则查看这些科室中已经选择的职位
        if(deptBeans!=null && deptBeans.size()>=1){
            for (DeptBean deptBean : deptBeans) {
                //查看该科室中的所有职位
                List<PostBean> postBeans = userMapper.selectPostByDeptid(deptBean.getId());

                //查看每个科室已经选择的职位
                Long[] postids = deptMapper.getUserPostByIdAndDeptid(id,deptBean.getId());

                deptBean.setPostlist(postBeans);
                deptBean.setPostids(postids);
            }
        }
        userBean.setDeptBeans(deptBeans);
        return userBean;
    }

    @Override
    public void saveUserPost(UserBean userBean) {
        if(userBean!=null){
            //删除
            userMapper.deleteUserPostById(userBean.getId());

            //添加
            if(userBean.getDeptBeans()!=null && userBean.getDeptBeans().size()>=1){
                for (DeptBean deptBean : userBean.getDeptBeans()) {
                    if(deptBean.getPostids()!=null && deptBean.getPostids().length>=1){
                        for (Long postid : deptBean.getPostids()) {
                            userMapper.saveUserPost(userBean.getId(),postid);
                        }
                    }
                }
            }
        }
    }

    @Override
    public UserBean getLogin(UserBean ub) {
         if(ub!=null){
             List<UserBean> userBeans = userMapper.getLogin(ub);
             if(userBeans!=null && userBeans.size()==1){
                 //到这里表示用户名或者手机号已将查到了
                 //开始对比密码，比对密码之前需要加密加盐
                 //加密算法有很多，常用的有MD5,bscript等
                 UserBean userBean = userBeans.get(0);
                 //页面输入的密码进行加密加盐后和数据库里面的密码进行比较，
                 String pwd = ub.getPwd();
                 //这里的加盐加密规则和注册的要一致
                 pwd = userBean.getPwdsalt() + pwd + userBean.getPwdsalt();

                 MD5key md5key = new MD5key();
                 String newpwd = md5key.getkeyBeanofStr(pwd);

                 //相等就返回，其他都是空
                 if(newpwd.equals(userBean.getPwd())){
                     return userBean;
                 }
             }
         }
        return null;
    }

    @Override
    public Set<String> getUserMeunUrlsById(UserBean ub) {
       if(ub!=null){
           Set<String> list = userMapper.getUserMeunUrlsById(ub.getId());
           return list;
       }
       return null;
    }

    @Override
    public List<PostBean> getDeputyPosition(Long id) {
        return userMapper.getDeputyPosition(id);
    }

    @Test
    public void test(){
        String pwd = "123456";
        pwd = "1234"+pwd+"1234";
        MD5key md5key = new MD5key();
        String s = md5key.getkeyBeanofStr(pwd);
        System.out.println(s);
    }


    public List<MeunBean> getMeunList2() {
        Long[] ids = {1L,2L,3L};
        List<MeunBean> meunBeans = meunMapper.selectByExample(null);

        for (Long id : ids) {
            for (MeunBean meunBean : meunBeans) {
                if(id.equals(meunBean.getId())){
                    meunBean.setChecked(true);
                    break;
                }
            }
        }

       return meunBeans;
    }
}
