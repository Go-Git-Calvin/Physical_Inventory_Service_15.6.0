package com.cardinal.ws.physicalinventory.common.valueobjects;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class InvLocation implements Serializable {
	private static final long serialVersionUID = 1L;
	public Date afxDate;
	public String costType;
	public String countType;
	public Date createDate;
	public long inventoryID;
	public int lineCount;
	public int locationId;
	public String locationName;
	public String locStatus;
	public BigDecimal totalValue;
	public String locComments;
	private List<InvCountType> invCountTypes;
	/**
	 * @return the afxDate
	 */
	public Date getAfxDate() {
		return afxDate;
	}
	/**
	 * @param afxDate the afxDate to set
	 */
	public void setAfxDate(Date afxDate) {
		this.afxDate = afxDate;
	}
	/**
	 * @return the costType
	 */
	public String getCostType() {
		return costType;
	}
	/**
	 * @param costType the costType to set
	 */
	public void setCostType(String costType) {
		this.costType = costType;
	}
	/**
	 * @return the countType
	 */
	public String getCountType() {
		return countType;
	}
	/**
	 * @param countType the countType to set
	 */
	public void setCountType(String countType) {
		this.countType = countType;
	}
	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the inventoryID
	 */
	public long getInventoryID() {
		return inventoryID;
	}
	/**
	 * @param inventoryID the inventoryID to set
	 */
	public void setInventoryID(long inventoryID) {
		this.inventoryID = inventoryID;
	}
	/**
	 * @return the lineCount
	 */
	public int getLineCount() {
		return lineCount;
	}
	/**
	 * @param lineCount the lineCount to set
	 */
	public void setLineCount(int lineCount) {
		this.lineCount = lineCount;
	}
	/**
	 * @return the locationId
	 */
	public int getLocationId() {
		return locationId;
	}
	/**
	 * @param locationId the locationId to set
	 */
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	/**
	 * @return the locationName
	 */
	public String getLocationName() {
		return locationName;
	}
	/**
	 * @param locationName the locationName to set
	 */
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	/**
	 * @return the locStatus
	 */
	public String getLocStatus() {
		return locStatus;
	}
	/**
	 * @param locStatus the locStatus to set
	 */
	public void setLocStatus(String locStatus) {
		this.locStatus = locStatus;
	}
	/**
	 * @return the totalValue
	 */
	public BigDecimal getTotalValue() {
		return totalValue;
	}
	/**
	 * @param totalValue the totalValue to set
	 */
	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = totalValue;
	}
	/**
	 * @return the locComments
	 */
	public String getLocComments() {
		return locComments;
	}
	/**
	 * @param locComments the locComments to set
	 */
	public void setLocComments(String locComments) {
		this.locComments = locComments;
	}
	/**
	 * @return the invCountTypes
	 */
	public List<InvCountType> getInvCountTypes() {
		return invCountTypes;
	}
	/**
	 * @param invCountTypes the invCountTypes to set
	 */
	public void setInvCountTypes(List<InvCountType> invCountTypes) {
		this.invCountTypes = invCountTypes;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InvLocation [afxDate=");
		builder.append(afxDate);
		builder.append(", costType=");
		builder.append(costType);
		builder.append(", countType=");
		builder.append(countType);
		builder.append(", createDate=");
		builder.append(createDate);
		builder.append(", inventoryID=");
		builder.append(inventoryID);
		builder.append(", lineCount=");
		builder.append(lineCount);
		builder.append(", locationId=");
		builder.append(locationId);
		builder.append(", locationName=");
		builder.append(locationName);
		builder.append(", locStatus=");
		builder.append(locStatus);
		builder.append(", totalValue=");
		builder.append(totalValue);
		builder.append(", locComments=");
		builder.append(locComments);
		builder.append(", invCountTypes=");
		builder.append(invCountTypes);
		builder.append("]");
		return builder.toString();
	}	
	
	

}
