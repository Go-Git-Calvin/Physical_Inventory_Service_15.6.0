package com.cardinal.ws.physicalinventory.locationdetail.valueobjects;

import java.io.Serializable;
import java.util.List;

import com.cardinal.ws.physicalinventory.common.valueobjects.InvBaseResponse;
import com.cardinal.ws.physicalinventory.common.valueobjects.InvLocation;
import com.cardinal.ws.physicalinventory.product.valueobjects.InvLocProductResponse;

public class InvLocationDetailResponse extends InvBaseResponse implements Serializable{
	private static final long serialVersionUID = 1L;
	private List<InvLocProductResponse> invLocProducts;
	private InvLocation location;
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
	/**
	 * @return the location
	 */
	public InvLocation getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(InvLocation location) {
		this.location = location;
	}

	
	
	

}