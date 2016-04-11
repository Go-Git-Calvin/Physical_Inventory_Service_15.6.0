/**
 * 
 */
package com.cardinal.ws.physicalinventory.location.valueobjects;

import java.util.List;

import com.cardinal.ws.physicalinventory.common.valueobjects.InvBaseResponse;
import com.cardinal.ws.physicalinventory.common.valueobjects.InvInventory;
import com.cardinal.ws.physicalinventory.common.valueobjects.InvLocation;

/**
 * @author brian.deffenbaugh01
 *
 */
public class InvLocationResponse extends InvBaseResponse {
	private static final long serialVersionUID = 1L;
	private InvInventory inventory;
	private List<InvLocation> locations;
	private List<String> locationStatuses;
	
	/**
	 * @return the inventory
	 */
	public InvInventory getInventory() {
		return inventory;
	}
	
	/**
	 * @return the locations
	 */
	public List<InvLocation> getLocations() {
		return locations;
	}
	
	/**
	 * @return the locationStatuses
	 */
	public List<String> getLocationStatuses() {
		return locationStatuses;
	}
	
	/**
	 * @param inventory the inventory to set
	 */
	public void setInventory(InvInventory inventory) {
		this.inventory = inventory;
	}
	
	/**
	 * @param locations the locations to set
	 */
	public void setLocations(List<InvLocation> locations) {
		this.locations = locations;
	}

	/**
	 * @param locationStatuses the locationStatuses to set
	 */
	public void setLocationStatuses(List<String> locationStatuses) {
		this.locationStatuses = locationStatuses;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InvLocationResponse [inventory=");
		builder.append(inventory);
		builder.append(", locations=");
		builder.append(locations);
		builder.append(", locationStatuses=");
		builder.append(locationStatuses);
		builder.append(", toString()=");
		builder.append(super.toString());
		builder.append("]");
		return builder.toString();
	}
}