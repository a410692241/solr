package com.wei.solr.bo;

import java.util.List;

public class SolrPage<T> {

    /*初始页*/
    private Integer page = 1;

    //每页大小
    private Integer pageSize = 10;

    //当前页
    private Integer pageCount;

    //总页数
    private Integer pagSum;

    //数据
    private List<T> datas;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getPagSum() {
        return pagSum;
    }

    public void setPagSum(Integer pagSum) {
        this.pagSum = pagSum;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }
}
