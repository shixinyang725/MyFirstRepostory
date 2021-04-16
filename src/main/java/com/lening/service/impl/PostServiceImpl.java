package com.lening.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lening.entity.MeunBean;
import com.lening.entity.PostBean;
import com.lening.entity.PostBeanExample;
import com.lening.mapper.MeunMapper;
import com.lening.mapper.PostMapper;
import com.lening.service.PostService;
import com.lening.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: PostServiceImpl
 * @Author: PanJunShuang
 * @Date: 2021/4/8 12:53
 * @Version: V1.0
 **/
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private MeunMapper meunMapper;

    @Override
    public Page<PostBean> getPostList(Integer pageNum, Integer pageSize, PostBean postBean) {
        PageHelper.startPage(pageNum, pageSize);
        PostBeanExample example = new PostBeanExample();
        PostBeanExample.Criteria criteria = example.createCriteria();
        //根据科室名称模糊查询
        if(postBean!=null){
            if(postBean.getPostname()!=null && postBean.getPostname().length()>=1){
                criteria.andPostnameLike("%"+postBean.getPostname()+"%");
            }
        }
        List<PostBean> postBeans = postMapper.selectByExample(example);
        PageInfo<PostBean> pageInfo = new PageInfo<PostBean>(postBeans);
        Long total = pageInfo.getTotal();
        Page<PostBean> page = new Page<>(pageInfo.getPageNum() + "", total.intValue(), pageInfo.getPageSize() + "");
        page.setList(postBeans);
        return page;
    }

    @Override
    public List<MeunBean> getMeunListById(Long id) {

        //全查菜单
        List<MeunBean> meunBeans = meunMapper.selectByExample(null);

        //当前职位拥有哪些菜单，中间表查询
        List<Long> meunIds = postMapper.selectMeunById(id);

        //做Ztree回显
        if(meunIds!=null && meunIds.size()>=1){
            for (Long meunId : meunIds) {
                for (MeunBean meunBean : meunBeans) {
                    if(meunId.equals(meunBean.getId())){
                        meunBean.setChecked(true);
                        break;
                    }
                }
            }
        }
        return  meunBeans;
    }

    @Override
    public void savePostMeun(Long postid, Long[] ids) {
        //先删除中间表的数据
        postMapper.deletePostMeun(postid);

        //循环添加中间表数据
        if(ids!=null && ids.length>=1){
            for (Long id : ids) {
                postMapper.addPostMeun(postid, id);
            }
        }
    }
}
