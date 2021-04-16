package com.lening.service;

import com.lening.entity.DeptBean;
import com.lening.utils.Page;

/**
 * @ClassName: DeptService
 * @Author: PanJunShuang
 * @Date: 2021/4/8 10:56
 * @Version: V1.0
 **/
public interface DeptService {
    Page<DeptBean> getDeptList(Integer pageNum, Integer pageSize, DeptBean deptBean);

    DeptBean toDeptPost(Long id);

    void saveDeptPost(Long id, Long[] postids);
}
