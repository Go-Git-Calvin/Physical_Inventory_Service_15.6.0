/*********************************************************************
*
* $Workfile: InvInventory.java $
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
import java.util.Date;

/**
 * @author brian.deffenbaugh01
 * @version $Revision: 1.0 $
 */
public class InvInventory implements Serializable {
	private static final long serialVersionUID = 1L;
	public Date createDate;
	public long inventoryId;
	public String inventoryName;
	public int numberOfLocations;
	public double totalValue;
	public String inventoryStatus;
		
	public Date getCreateDate() {
		return createDate;
	}

	
	/**
	 * @return the inventoryName
	 */
	public String getInventoryName() {
		return inventoryName;
	}
	
	/**
	 * @return the numberOfLocations
	 */
	public int getNumberOfLocations() {
		return numberOfLocations;
	}
	
	
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(final Date createDate) {
		this.createDate = createDate;
	}
	
	
	public long getInventoryId() {
		return inventoryId;
	}


	public void setInventoryId(long inventoryId) {
		this.inventoryId = inventoryId;
	}


	/**
	 * @param inventoryName the inventoryName to set
	 */
	public void setInventoryName(String inventoryName) {
		this.inventoryName = inventoryName;
	}

	/**
	 * @param numberOfLocations the numberOfLocations to set
	 */
	public void setNumberOfLocations(final int numberOfLocations) {
		this.numberOfLocations = numberOfLocations;
	}

		
	/**
	 * @return the totalValue
	 */
	public double getTotalValue() {
		return totalValue;
	}


	/**
	 * @param totalValue the totalValue to set
	 */
	public void setTotalValue(final double totalValue) {
		this.totalValue = totalValue;
	}

	/**
	 * @return the inventoryStatus
	 */
	public String getInventoryStatus() {
		return inventoryStatus;
	}


	/**
	 * @param inventoryStatus the inventoryStatus to set
	 */
	public void setInventoryStatus(String inventoryStatus) {
		this.inventoryStatus = inventoryStatus;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InvInventory [createDate=");
		builder.append(createDate);
		builder.append(", inventoryId=");
		builder.append(inventoryId);
		builder.append(", inventoryName=");
		builder.append(inventoryName);
		builder.append(", numberOfLocations=");
		builder.append(numberOfLocations);
		builder.append(", totalValue=");
		builder.append(totalValue);
		builder.append(", inventoryStatus=");
		builder.append(inventoryStatus);
		builder.append("]");
		return builder.toString();
	}


	
}