/*********************************************************************
*
* $Workfile: RecevShipmentResponse.java $
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

import java.io.Serializable;
import java.util.List;

import com.cardinal.ws.physicalinventory.common.valueobjects.InvBaseResponse;
import com.cardinal.ws.physicalinventory.common.valueobjects.InvInventory;


/**
 * @author irfan.mohammed
 * @version $Revision: 1.0 $
 */
public class InvInventoryResponse extends InvBaseResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<InvInventory> inventories;
	
	/**
	 * @return the inventories
	 */
	public List<InvInventory> getInventories() {
		return inventories;
	}
	
	/**
	 * @param inventories the inventories to set
	 */
	public void setInventories(List<InvInventory> inventories) {
		this.inventories = inventories;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InvInventoryResponse [inventories=");
		builder.append(inventories);
		builder.append(", toString()=");
		builder.append(super.toString());
		builder.append("]");
		return builder.toString();
	}
}