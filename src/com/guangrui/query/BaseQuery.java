package com.guangrui.query;
public class BaseQuery {

    private long total = 0;
    private int pageSize = 10;
    private int p = 1;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }

    public long getTotalPage() {
        if (total % pageSize == 0) {
            return total / pageSize;
        } else {
            return total / pageSize + 1;
        }
    }
    
    public int getStartItem(){
    	return (p-1)*pageSize;
    }

}
