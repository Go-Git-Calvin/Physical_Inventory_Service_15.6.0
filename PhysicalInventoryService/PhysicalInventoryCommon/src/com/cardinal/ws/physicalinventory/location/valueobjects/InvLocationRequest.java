/*********************************************************************
*
* $Workfile: RecevBaseRequest.java $
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

import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.cardinal.ws.physicalinventory.common.valueobjects.InvBaseRequest;
import com.cardinal.ws.physicalinventory.common.valueobjects.InvInventory;
import com.cardinal.ws.physicalinventory.product.valueobjects.InvLocationProductCost;

/**
 * @author brian.deffenbaugh01
 *
 */
public class InvLocationRequest extends InvBaseRequest {
	private static final long serialVersionUID = 1L;
	private Boolean copyNamesOnly; //Used in CopyLocations
	private long copyToInv; //Used in CopyLocations
	private String copyToInvName; //Used in CopyLocations
	private int costType; //Used in UpdateLocations
	private int countType; //Used in CreateLocation, UpdateLocations
	private InvInventory inventory;
	private String locName;  //Used for CreateLocation
	private Set<Integer> locs; //Used in DeleteLocations and CopyLocations
	private Date costAsOfDate;	
	private String locComments;
	private Map<String, InvLocationProductCost> productCostMap;
	private int statusType;
	
	public Boolean getCopyNamesOnly() {
		return copyNamesOnly;
	}
	public void setCopyNamesOnly(Boolean copyNamesOnly) {
		this.copyNamesOnly = copyNamesOnly;
	}
	public long getCopyToInv() {
		return copyToInv;
	}
	public void setCopyToInv(long copyToInv) {
		this.copyToInv = copyToInv;
	}
	public String getCopyToInvName() {
		return copyToInvName;
	}
	public void setCopyToInvName(String copyToInvName) {
		this.copyToInvName = copyToInvName;
	}
	public int getCostType() {
		return costType;
	}
	public void setCostType(int costType) {
		this.costType = costType;
	}
	public int getCountType() {
		return countType;
	}
	public void setCountType(int countType) {
		this.countType = countType;
	}
	public InvInventory getInventory() {
		return inventory;
	}
	public void setInventory(InvInventory inventory) {
		this.inventory = inventory;
	}
	public String getLocName() {
		return locName;
	}
	public void setLocName(String locName) {
		this.locName = locName;
	}
	public Set<Integer> getLocs() {
		return locs;
	}
	public void setLocs(Set<Integer> locs) {
		this.locs = locs;
	}
	public Date getCostAsOfDate() {
		return costAsOfDate;
	}
	public void setCostAsOfDate(Date costAsOfDate) {
		this.costAsOfDate = costAsOfDate;
	}
	public String getLocComments() {
		return locComments;
	}
	public void setLocComments(String locComments) {
		this.locComments = locComments;
	}
	public Map<String, InvLocationProductCost> getProductCostMap() {
		return productCostMap;
	}
	public void setProductCostMap(Map<String, InvLocationProductCost> productCostMap) {
		this.productCostMap = productCostMap;
	}
	public int getStatusType() {
		return statusType;
	}
	public void setStatusType(int statusType) {
		this.statusType = statusType;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InvLocationRequest [copyNamesOnly=");
		builder.append(copyNamesOnly);
		builder.append(", copyToInv=");
		builder.append(copyToInv);
		builder.append(", copyToInvName=");
		builder.append(copyToInvName);
		builder.append(", costType=");
		builder.append(costType);
		builder.append(", countType=");
		builder.append(countType);
		builder.append(", inventory=");
		builder.append(inventory);
		builder.append(", locName=");
		builder.append(locName);
		builder.append(", locs=");
		builder.append(locs);
		builder.append(", costAsOfDate=");
		builder.append(costAsOfDate);
		builder.append(", locComments=");
		builder.append(locComments);
		builder.append(", productCostMap=");
		builder.append(productCostMap);
		builder.append(", statusType=");
		builder.append(statusType);
		builder.append("]");
		return builder.toString();
	}
	
}