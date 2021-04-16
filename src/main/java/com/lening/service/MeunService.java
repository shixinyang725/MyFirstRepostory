package com.lening.service;

import com.lening.entity.MeunBean;
import com.lening.utils.Page;

/**
 * @ClassName: MeunService
 * @Author: PanJunShuang
 * @Date: 2021/4/8 16:06
 * @Version: V1.0
 **/
public interface MeunService {
    Page<MeunBean> getMeunListByPid(Integer pageNum, Integer pageSize, Long pid);

    void saveMeun(MeunBean meunBean);

    void deleteMeunById(Long id);
}
