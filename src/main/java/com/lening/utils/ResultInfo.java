package com.lening.utils;

import java.io.Serializable;

/**
 * @ClassName: MeunController
 * @Author: PanJunShuang
 * @Date: 2021/4/8 16:05
 * @Version: V1.0
 **/
public class ResultInfo implements Serializable {
    private boolean flag;
    private String msg;

    public ResultInfo() {
    }

    public ResultInfo(boolean flag, String msg) {
        this.flag = flag;
        this.msg = msg;
    }

    public boolean isFlag() {
        return flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ResultInfo{" +
                "flag=" + flag +
                ", msg='" + msg + '\'' +
                '}';
    }
}
