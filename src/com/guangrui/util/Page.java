package com.guangrui.util;

import java.io.Serializable;
import java.util.ArrayList;

import org.springframework.util.Assert;

/**
 * 分页对象. 包含当前页数据及分页信息如�?�记录数.
 *
 * @author lilin
 */
@SuppressWarnings("serial")
public class Page implements Serializable {

    private static int DEFAULT_PAGE_SIZE = 20;

    //private int pageSize = DEFAULT_PAGE_SIZE; // 每页的记录数 
    private int pageSize =10; // 每页的记录数 

    private long start; // 当前页第�?条数据在List中的位置,�?0�?�? 

    private Object data; // 当前页中存放的记�?,类型�?般为List 

    private long totalCount; // 总记录数

    /**
     * 构�?�方法，只构造空�?.
     */
    @SuppressWarnings("rawtypes")
    public Page() {
        this(0, 0, DEFAULT_PAGE_SIZE, new ArrayList());
    }

    /**
     * 默认构�?�方�?.
     *
     * @param start     本页数据在数据库中的起始位置
     * @param totalSize 数据库中总记录条�?
     * @param pageSize  本页容量
     * @param data      本页包含的数�?
     */
    public Page(long start, long totalSize, int pageSize, Object data) {
        this.pageSize = pageSize;
        this.start = start;
        this.totalCount = totalSize;
        this.data = data;
    }


    /**
     * 取�?�记录数.
     */
    public long getTotalCount() {
        return this.totalCount;
    }

    /**
     * 取�?�页�?.
     */
    public long getTotalPageCount() {
        if (totalCount % pageSize == 0)
            return totalCount / pageSize;
        else
            return totalCount / pageSize + 1;
    }

    /**
     * 取每页数据容�?.
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 取当前页中的记录.
     */
    public Object getResult() {
        return data;
    }

    /**
     * 设置data属�??
     *
     * @param result
     */
    public void setResult(Object result) {
        this.data = result;
    }

    /**
     * 取该页当前页�?,页码�?1�?�?.
     */
    public long getCurrentPageNo() {
        return start / pageSize + 1;
    }

    /**
     * 该页是否有下�?�?.
     */
    public boolean hasNextPage() {
        return this.getCurrentPageNo() < this.getTotalPageCount();
    }

    /**
     * 该页是否有上�?�?.
     */
    public boolean hasPreviousPage() {
        return this.getCurrentPageNo() > 1;
    }

    /**
     * 获取任一页第�?条数据在数据集的位置，每页条数使用默认�??.
     *
     * @see #getStartOfPage(int, int)
     */
    protected static int getStartOfPage(int pageNo) {
        return getStartOfPage(pageNo, DEFAULT_PAGE_SIZE);
    }

    /**
     * 获取任一页第�?条数据在数据集的位置.
     *
     * @param pageNo   �?1�?始的页号
     * @param pageSize 每页记录条数
     * @return 该页第一条数�?
     */
    public static int getStartOfPage(int pageNo, int pageSize) {
        if (pageNo == 0) {
            Assert.isTrue(pageNo >= 1, "pageNo should start from 1");
        }
        return (pageNo - 1) * pageSize;
    }
    
    public long getStart() {
		return start;
	}
} 