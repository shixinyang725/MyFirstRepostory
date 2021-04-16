package com.lening.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lening.entity.DeptBean;
import com.lening.entity.DeptBeanExample;
import com.lening.entity.PostBean;
import com.lening.mapper.DeptMapper;
import com.lening.mapper.PostMapper;
import com.lening.service.DeptService;
import com.lening.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: DeptServiceImpl
 * @Author: PanJunShuang
 * @Date: 2021/4/8 10:57
 * @Version: V1.0
 **/
@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private PostMapper postMapper;

    @Override
    public Page<DeptBean> getDeptList(Integer pageNum, Integer pageSize, DeptBean deptBean) {
        PageHelper.startPage(pageNum, pageSize);
        DeptBeanExample example = new DeptBeanExample();
        DeptBeanExample.Criteria criteria = example.createCriteria();
        //根据科室名称模糊查询
        if(deptBean!=null){
            if(deptBean.getDeptname()!=null && deptBean.getDeptname().length()>=1){
                criteria.andDeptnameLike("%"+deptBean.getDeptname()+"%");
            }
        }
        List<DeptBean> deptBeans = deptMapper.selectByExample(example);
        PageInfo<DeptBean> pageInfo = new PageInfo<>(deptBeans);
        Long total = pageInfo.getTotal();
        Page<DeptBean> page = new Page<>(pageInfo.getPageNum() + "", total.intValue(), pageInfo.getPageSize() + "");
        page.setList(deptBeans);
        return page;
    }

    @Override
    public DeptBean toDeptPost(Long id) {
        //根据id查询一条科室数据
        DeptBean deptBean = deptMapper.selectByPrimaryKey(id);

        //查询所有的职位
        List<PostBean> postBeans = postMapper.selectByExample(null);
        deptBean.setPostlist(postBeans);

        //查询该科室已经拥有的职位
        Long[] longs = deptMapper.selectPostById(id);
        deptBean.setPostids(longs);
        return deptBean;
    }

    @Override
    public void saveDeptPost(Long id, Long[] postids) {
        deptMapper.deletePostByDeptid(id);
        if(postids!=null && postids.length>=1){
            for (Long postid : postids) {
                deptMapper.addPost(id, postid);
            }
        }
    }
}
