/*********************************************************************
*
* $Workfile: InvLocationDetail.java $
* Copyright 2013 Cardinal Health
*
*********************************************************************
*
* Revision History:
*
* $Log: $
*
*********************************************************************/

package com.cardinal.ws.physicalinventory.location.valueobjects;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.cardinal.ws.physicalinventory.common.valueobjects.InvCountType;
import com.cardinal.ws.physicalinventory.product.valueobjects.InvLocProductResponse;

/**
 * @author vignesh.kandadi
 * @version $Revision: 1.0 $
 */
public class InvLocationDetail implements Serializable {
	private static final long serialVersionUID = 1L;
	public String afxDate;
	public String costType;
	public String countType;
	public String createDate;
	public long inventoryID;
	public int lineCount;
	public int locationId;
	public String locationName;
	public String locStatus;
	public BigDecimal totalValue;
	public List<InvCountType> invCountTypes;
	public List<InvLocProductResponse> invLocProducts;
	/**
	 * @return the afxDate
	 */
	public String getAfxDate() {
		return afxDate;
	}
	/**
	 * @param afxDate the afxDate to set
	 */
	public void setAfxDate(String afxDate) {
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
	public String getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(String createDate) {
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
	/**
	 * @return the invLocProducts
	 */
	public List<InvLocProductResponse> getInvLocProducts() {
		return invLocProducts;
	}
	/**
	 * @param invLocProducts the invLocProducts to set
	 */
	public void setInvLocProducts(List<InvLocProductResponse> invLocProducts) {
		this.invLocProducts = invLocProducts;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InvLocationDetail [afxDate=" + afxDate + ", costType="
				+ costType + ", countType=" + countType + ", createDate="
				+ createDate + ", inventoryID=" + inventoryID + ", lineCount="
				+ lineCount + ", locationId=" + locationId + ", locationName="
				+ locationName + ", locStatus=" + locStatus + ", totalValue="
				+ totalValue + ", invCountTypes=" + invCountTypes
				+ ", invLocProducts=" + invLocProducts + ", toString()="
				+ super.toString() + "]";
	}
	
	

	
}