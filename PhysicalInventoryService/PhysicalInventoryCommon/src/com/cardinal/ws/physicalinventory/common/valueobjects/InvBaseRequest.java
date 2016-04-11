/*********************************************************************
*
* $Workfile: RecevBaseRequest.java $
* Copyright 2012 Cardinal Health
*
*********************************************************************
*
* Revision History:
*
* $Log: $
*
*********************************************************************/
package com.cardinal.ws.physicalinventory.common.valueobjects;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author brian.deffenbaugh01
 * @version $Revision: 1.0 $
 */
public class InvBaseRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<String> columnNames;
	private String distributionCenter;
	private boolean mobileRequest;
	private int pageNum;
	private int recordsPerPage;
	private boolean refreshFlag;
	private String shipToCustomer ;
	private List<String> sortColumns;
	private String sortOrder;
	private String storeId;
	private String timeZone;
	private String transactionId;
	private String userId;
	private String userName;
	/**
	 * @return the columnNames
	 */
	public List<String> getColumnNames() {
		return columnNames;
	}
	/**
	 * @param columnNames the columnNames to set
	 */
	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}
	/**
	 * @return the distributionCenter
	 */
	public String getDistributionCenter() {
		return distributionCenter;
	}
	/**
	 * @param distributionCenter the distributionCenter to set
	 */
	public void setDistributionCenter(String distributionCenter) {
		this.distributionCenter = distributionCenter;
	}
	/**
	 * @return the mobileRequest
	 */
	public boolean isMobileRequest() {
		return mobileRequest;
	}
	/**
	 * @param mobileRequest the mobileRequest to set
	 */
	public void setMobileRequest(boolean mobileRequest) {
		this.mobileRequest = mobileRequest;
	}
	/**
	 * @return the pageNum
	 */
	public int getPageNum() {
		return pageNum;
	}
	/**
	 * @param pageNum the pageNum to set
	 */
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	/**
	 * @return the recordsPerPage
	 */
	public int getRecordsPerPage() {
		return recordsPerPage;
	}
	/**
	 * @param recordsPerPage the recordsPerPage to set
	 */
	public void setRecordsPerPage(int recordsPerPage) {
		this.recordsPerPage = recordsPerPage;
	}
	/**
	 * @return the refreshFlag
	 */
	public boolean isRefreshFlag() {
		return refreshFlag;
	}
	/**
	 * @param refreshFlag the refreshFlag to set
	 */
	public void setRefreshFlag(boolean refreshFlag) {
		this.refreshFlag = refreshFlag;
	}
	/**
	 * @return the shipToCustomer
	 */
	public String getShipToCustomer() {
		return shipToCustomer;
	}
	/**
	 * @param shipToCustomer the shipToCustomer to set
	 */
	public void setShipToCustomer(String shipToCustomer) {
		this.shipToCustomer = shipToCustomer;
	}
	/**
	 * @return the sortColumns
	 */
	public List<String> getSortColumns() {
		return sortColumns;
	}
	/**
	 * @param sortColumns the sortColumns to set
	 */
	public void setSortColumns(List<String> sortColumns) {
		this.sortColumns = sortColumns;
	}
	/**
	 * @return the sortOrder
	 */
	public String getSortOrder() {
		return sortOrder;
	}
	/**
	 * @param sortOrder the sortOrder to set
	 */
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	/**
	 * @return the storeId
	 */
	public String getStoreId() {
		return storeId;
	}
	/**
	 * @param storeId the storeId to set
	 */
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	/**
	 * @return the timeZone
	 */
	public String getTimeZone() {
		return timeZone;
	}
	/**
	 * @param timeZone the timeZone to set
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	/**
	 * @return the transactionId
	 */
	public String getTransactionId() {
		return transactionId;
	}
	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InvBaseRequest [columnNames=");
		builder.append(columnNames);
		builder.append(", distributionCenter=");
		builder.append(distributionCenter);
		builder.append(", mobileRequest=");
		builder.append(mobileRequest);
		builder.append(", pageNum=");
		builder.append(pageNum);
		builder.append(", recordsPerPage=");
		builder.append(recordsPerPage);
		builder.append(", refreshFlag=");
		builder.append(refreshFlag);
		builder.append(", shipToCustomer=");
		builder.append(shipToCustomer);
		builder.append(", sortColumns=");
		builder.append(sortColumns);
		builder.append(", sortOrder=");
		builder.append(sortOrder);
		builder.append(", storeId=");
		builder.append(storeId);
		builder.append(", timeZone=");
		builder.append(timeZone);
		builder.append(", transactionId=");
		builder.append(transactionId);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", userName=");
		builder.append(userName);
		builder.append("]");
		return builder.toString();
	}
	
	
}