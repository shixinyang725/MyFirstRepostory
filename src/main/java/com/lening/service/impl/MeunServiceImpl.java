package com.lening.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lening.entity.MeunBean;
import com.lening.entity.MeunBeanExample;
import com.lening.mapper.MeunMapper;
import com.lening.service.MeunService;
import com.lening.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName: MeunServiceImpl
 * @Author: PanJunShuang
 * @Date: 2021/4/8 16:06
 * @Version: V1.0
 **/
@Service
public class MeunServiceImpl implements MeunService {

    @Autowired
    private MeunMapper meunMapper;

    @Override
    public Page<MeunBean> getMeunListByPid(Integer pageNum, Integer pageSize, Long pid) {
        //开启分页
        PageHelper.startPage(pageNum,pageSize);

        //根据pid查询
        MeunBeanExample example = new MeunBeanExample();
        MeunBeanExample.Criteria criteria = example.createCriteria();

        criteria.andPidEqualTo(pid);

        List<MeunBean> meunBeans = meunMapper.selectByExample(example);
        PageInfo<MeunBean> pageInfo = new PageInfo<>(meunBeans);
        //取出总条数
        Long total = pageInfo.getTotal();
        Page<MeunBean> page = new Page<>(pageInfo.getPageNum() + "", total.intValue(), pageInfo.getPageSize() + "");
        page.setList(meunBeans);
        return page;
    }

    @Override
    public void saveMeun(MeunBean meunBean) {
        if(meunBean!=null){
            if(meunBean.getId()!=null){
                //修改
                meunMapper.updateByPrimaryKeySelective(meunBean);
            }else{
                //添加
                meunMapper.insertSelective(meunBean);
            }
        }
    }

    Set<Long> ids = new HashSet<>();
    @Override
    public void deleteMeunById(Long id) {

        getMeunListByPidToDelete(id);

        for (Long id1 : ids) {
            meunMapper.deleteByPrimaryKey(id1);
        }

    }

    private void getMeunListByPidToDelete(Long pid){
        ids.add(pid);

        //根据pid查询
        MeunBeanExample example = new MeunBeanExample();
        MeunBeanExample.Criteria criteria = example.createCriteria();

        criteria.andPidEqualTo(pid);

        List<MeunBean> meunBeans = meunMapper.selectByExample(example);

        if(meunBeans!=null && meunBeans.size()>=1){
            for (MeunBean meunBean : meunBeans) {
                getMeunListByPidToDelete(meunBean.getId());
            }
        }

    }
}
