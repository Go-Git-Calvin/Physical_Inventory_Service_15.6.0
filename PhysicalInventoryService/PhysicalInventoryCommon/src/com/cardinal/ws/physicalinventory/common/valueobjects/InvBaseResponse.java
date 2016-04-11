package com.cardinal.ws.physicalinventory.common.valueobjects;

import java.io.Serializable;
import java.util.List;

public class InvBaseResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	public int numberOfPages;
	public int pageNum;
	public List<String> sortColumns;
	public String sortOrder;
	public String statusCode;
	public String statusMsg;
	
	/**
	 * @return the numberOfPages
	 */
	public int getNumberOfPages() {
		return numberOfPages;
	}
	
	/**
	 * @return the pageNum
	 */
	public int getPageNum() {
		return pageNum;
	}
	
	/**
	 * @return the sortColumns
	 */
	public List<String> getSortColumns() {
		return sortColumns;
	}
	
	/**
	 * @return the sortOrder
	 */
	public String getSortOrder() {
		return sortOrder;
	}
	
	/**
	 * @return the statusCode
	 */
	public String getStatusCode() {
		return statusCode;
	}
	
	/**
	 * @return the statusMsg
	 */
	public String getStatusMsg() {
		return statusMsg;
	}
	
	/**
	 * @param numberOfPages the numberOfPages to set
	 */
	public void setNumberOfPages(int numberOfPages) {
		this.numberOfPages = numberOfPages;
	}
	
	/**
	 * @param pageNum the pageNum to set
	 */
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	/**
	 * @param sortColumns the sortColumns to set
	 */
	public void setSortColumns(List<String> sortColumns) {
		this.sortColumns = sortColumns;
	}

	/**
	 * @param sortOrder the sortOrder to set
	 */
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * @param statusMsg the statusMsg to set
	 */
	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InvBaseResponse [numberOfPages=");
		builder.append(numberOfPages);
		builder.append(", pageNum=");
		builder.append(pageNum);
		builder.append(", sortColumns=");
		builder.append(sortColumns);
		builder.append(", sortOrder=");
		builder.append(sortOrder);
		builder.append(", statusCode=");
		builder.append(statusCode);
		builder.append(", statusMsg=");
		builder.append(statusMsg);
		builder.append("]");
		return builder.toString();
	}
}