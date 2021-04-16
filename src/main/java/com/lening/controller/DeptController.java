package com.lening.controller;

import com.lening.entity.DeptBean;
import com.lening.service.DeptService;
import com.lening.utils.Page;
import com.lening.utils.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: DeptController
 * @Author: PanJunShuang
 * @Date: 2021/4/8 10:50
 * @Version: V1.0
 **/
@RestController
@RequestMapping("/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @RequestMapping("/getDeptList")
    public Page<DeptBean> getDeptList(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "3") Integer pageSize, @RequestBody DeptBean deptBean){
       return deptService.getDeptList(pageNum, pageSize, deptBean);
    }

    @RequestMapping("/toDeptPost")
    public DeptBean toDeptPost(Long id){
        return deptService.toDeptPost(id);
    }

    @RequestMapping("/saveDeptPost")
    public ResultInfo saveDeptPost(Long id, @RequestBody Long[] postids){
        try {
            deptService.saveDeptPost(id,postids);
            return new ResultInfo(true, "编辑成功");
        }catch (Exception e){
            return  new ResultInfo(false, "编辑失败");
        }
    }
}
