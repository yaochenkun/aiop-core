package org.bupt.common.bean;


import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * PageResult
 * Created by zlren on 2017/5/27.
 */
public class PageResult {

	private Integer rowCount;   // 当前这一页实际查询结果共有多少行
	private List<?> data;       // 实际数据
	private Integer pageTotal;  // 一共有多少页
	private Long rowTotal;      // 共有多少条记录

	public Integer getRowCount() {
		return rowCount;
	}

	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}

	public Long getRowTotal() {
		return rowTotal;
	}

	public void setRowTotal(Long rowTotal) {
		this.rowTotal = rowTotal;
	}

	public Integer getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(Integer pageTotal) {
		this.pageTotal = pageTotal;
	}


	public PageResult(Integer rowCount, List<?> data, Integer pageTotal, Long rowTotal) {
		this.rowCount = rowCount;
		this.data = data;
		this.pageTotal = pageTotal;
		this.rowTotal = rowTotal;
	}

	/**
	 * 写一个pageinfo为参数的构造方法
	 *
	 * @param pageInfo
	 */
	public PageResult(PageInfo<?> pageInfo) {
		this.data = pageInfo.getList();
		this.rowCount = data.size();
		this.pageTotal = pageInfo.getPages();
		this.rowTotal = pageInfo.getTotal();
	}
}
