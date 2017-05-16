package com.uf.util;
import java.util.ArrayList;
import java.util.List;

public class PageQueryResult<T> {
    private List<T> pageData = new ArrayList<T>();
    // total page number
    private int totalPage=-1;
    // current page index
    private int pageIndex=-1;
    // the record number of each page
    private int pageSize=-1;
    // the total record .
    private long totalRecord=-1;

    public List<T> getPageData() {
        return pageData;
    }

    public void setPageData(List<T> pageData) {
        this.pageData = pageData;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(long totalRecord) {
        this.totalRecord = totalRecord;
    }

}

