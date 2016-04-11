package com.cardinal.ws.physicalinventory.locationdetail.valueobjects;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import com.cardinal.ws.physicalinventory.common.valueobjects.InvBaseRequest;
import com.cardinal.ws.physicalinventory.common.valueobjects.InvLocation;
import com.cardinal.ws.physicalinventory.product.valueobjects.InvLocProductRequest;

public class InvLocationDetailRequest extends InvBaseRequest {
	
	private static final long serialVersionUID = 1L;
	private BigDecimal cost;
	private InvLocProductRequest[] invlocLines; //Used for Delete/Update/Add Location Lines
	private InvLocation location;
	
	// Rebate Customer Contract Price Override requirement - 14.4.0 - Start.
	private String rebatePriceEnabled;
    private List<String> contractGrpAffiliationList;
    private String genericRebatePercentage;
    private String ldrRebatePercentage;
    private String isRebateProgramAcct ;
    // Rebate Customer Contract Price Override requirement - 14.4.0 - End.
    
    
	/**
	 * @return the cost
	 */
	public BigDecimal getCost() {
		return cost;
	}
	/**
	 * @return the isRebateProgramAcct
	 */
	public String getIsRebateProgramAcct() {
		return isRebateProgramAcct;
	}
	/**
	 * @param isRebateProgramAcct the isRebateProgramAcct to set
	 */
	public void setIsRebateProgramAcct(String isRebateProgramAcct) {
		this.isRebateProgramAcct = isRebateProgramAcct;
	}
	/**
	 * @return the rebatePriceEnabled
	 */
	public String getRebatePriceEnabled() {
		return rebatePriceEnabled;
	}
	/**
	 * @param rebatePriceEnabled the rebatePriceEnabled to set
	 */
	public void setRebatePriceEnabled(String rebatePriceEnabled) {
		this.rebatePriceEnabled = rebatePriceEnabled;
	}
	/**
	 * @return the contractGrpAffiliationList
	 */
	public List<String> getContractGrpAffiliationList() {
		return contractGrpAffiliationList;
	}
	/**
	 * @param contractGrpAffiliationList the contractGrpAffiliationList to set
	 */
	public void setContractGrpAffiliationList(
			List<String> contractGrpAffiliationList) {
		this.contractGrpAffiliationList = contractGrpAffiliationList;
	}
	/**
	 * @return the genericRebatePercentage
	 */
	public String getGenericRebatePercentage() {
		return genericRebatePercentage;
	}
	/**
	 * @param genericRebatePercentage the genericRebatePercentage to set
	 */
	public void setGenericRebatePercentage(String genericRebatePercentage) {
		this.genericRebatePercentage = genericRebatePercentage;
	}
	/**
	 * @return the ldrRebatePercentage
	 */
	public String getLdrRebatePercentage() {
		return ldrRebatePercentage;
	}
	/**
	 * @param ldrRebatePercentage the ldrRebatePercentage to set
	 */
	public void setLdrRebatePercentage(String ldrRebatePercentage) {
		this.ldrRebatePercentage = ldrRebatePercentage;
	}
	/**
	 * @param cost the cost to set
	 */
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	/**
	 * @return the invlocLines
	 */
	public InvLocProductRequest[] getInvlocLines() {
		return invlocLines;
	}
	/**
	 * @param invlocLines the invlocLines to set
	 */
	public void setInvlocLines(InvLocProductRequest[] invlocLines) {
		this.invlocLines = invlocLines;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InvLocationDetailRequest [cost=" + cost + ", invlocLines="
				+ Arrays.toString(invlocLines) + ", location=" + location
				+ ", toString()=" + super.toString() + "]";
	}
		
	
}