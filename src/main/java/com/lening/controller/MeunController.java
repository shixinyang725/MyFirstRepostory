package com.lening.controller;

import com.lening.entity.MeunBean;
import com.lening.service.MeunService;
import com.lening.utils.Page;
import com.lening.utils.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: MeunController
 * @Author: PanJunShuang
 * @Date: 2021/4/8 16:05
 * @Version: V1.0
 **/
@RestController
@RequestMapping("/meun")
public class MeunController {

    @Autowired
    private MeunService meunService;

    @RequestMapping("/getMeunListByPid")
    public Page<MeunBean> getMeunListByPid(@RequestParam(defaultValue = "1") Long pid, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "3") Integer pageSize){
       return meunService.getMeunListByPid(pageNum,pageSize,pid);
    }

    @RequestMapping("/saveMeun")
    public ResultInfo saveMeun(@RequestBody MeunBean meunBean){
        try {
            meunService.saveMeun(meunBean);
            return new ResultInfo(true, "编辑成功");
        }catch (Exception e){
            return new ResultInfo(false, "编辑失败");
        }
    }

    @RequestMapping("/deleteMeunById")
    public ResultInfo deleteMeunById(Long id){
        try {
            meunService.deleteMeunById(id);
            return new ResultInfo(true, "删除成功");
        }catch (Exception e){
            return new ResultInfo(false, "删除失败");
        }
    }

}
