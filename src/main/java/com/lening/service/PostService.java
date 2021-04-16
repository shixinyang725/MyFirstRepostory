package com.lening.service;

import com.lening.entity.MeunBean;
import com.lening.entity.PostBean;
import com.lening.utils.Page;

import java.util.List;

/**
 * @ClassName: PostService
 * @Author: PanJunShuang
 * @Date: 2021/4/8 12:53
 * @Version: V1.0
 **/
public interface PostService {
    Page<PostBean> getPostList(Integer pageNum, Integer pageSize, PostBean postBean);

    List<MeunBean> getMeunListById(Long id);

    void savePostMeun(Long postid, Long[] ids);
}
