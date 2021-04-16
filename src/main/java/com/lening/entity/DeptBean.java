package com.lening.entity;

import java.util.List;

public class DeptBean {
    private Long id;

    private String deptname;

    private List<PostBean> postlist;
    private Long[] postids;

    public List<PostBean> getPostlist() {
        return postlist;
    }

    public void setPostlist(List<PostBean> postlist) {
        this.postlist = postlist;
    }

    public Long[] getPostids() {
        return postids;
    }

    public void setPostids(Long[] postids) {
        this.postids = postids;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname == null ? null : deptname.trim();
    }
}