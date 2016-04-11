/*********************************************************************
*
* $Workfile: RecevShipmentRequest.java $
* Copyright 2012 Cardinal Health
*
*********************************************************************
*
* Revision History:
*
* $Log: $
*
*********************************************************************/
package com.cardinal.ws.physicalinventory.inventory.valueobjects;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.cardinal.ws.physicalinventory.common.valueobjects.InvAccount;
import com.cardinal.ws.physicalinventory.common.valueobjects.InvBaseRequest;
import com.cardinal.ws.physicalinventory.common.valueobjects.InvInventory;


/**
 * @author irfan.mohammed
 * @version $Revision: 1.0 $
 */
public class InvInventoryRequest extends InvBaseRequest {
	private static final long serialVersionUID = 1L;
	private InvInventory inventory; //Used for UpdateInventory
	private String invName;  //Used for CreateInventory
	private InvInventory[] invs; //Used for DeleteInventories
	private BigDecimal updatedValue; //Used for UpdateInventory
	
	/**
	 * @return the inventory
	 */
	public InvInventory getInventory() {
		return inventory;
	}
	
	/**
	 * @return the invName
	 */
	public String getInvName() {
		return invName;
	}
	
	/**
	 * @return the invs
	 */
	public InvInventory[] getInvs() {
		return invs;
	}
	
	/**
	 * @return the updatedValue
	 */
	public BigDecimal getUpdatedValue() {
		return updatedValue;
	}
	
	/**
	 * @param inventory the inventory to set
	 */
	public void setInventory(InvInventory inventory) {
		this.inventory = inventory;
	}
	
	/**
	 * @param invName the invName to set
	 */
	public void setInvName(String invName) {
		this.invName = invName;
	}
	
	/**
	 * @param invs the invs to set
	 */
	public void setInvs(InvInventory[] invs) {
		this.invs = invs;
	}
	
	/**
	 * @param updatedValue the updatedValue to set
	 */
	public void setUpdatedValue(BigDecimal updatedValue) {
		this.updatedValue = updatedValue;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InvInventoryRequest [inventory=");
		builder.append(inventory);
		builder.append(", invName=");
		builder.append(invName);
		builder.append(", invs=");
		builder.append(Arrays.toString(invs));
		builder.append(", updatedValue=");
		builder.append(updatedValue);
		builder.append(", toString()=");
		builder.append(super.toString());
		builder.append("]");
		return builder.toString();
	}
}