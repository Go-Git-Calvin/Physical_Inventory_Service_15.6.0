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
*Modified By        Date             Clarify#        Description of the change
 Vignesh Kandadi	  04/05/2014	ALM#14444	 	Updated UOIF data field from Integer to Big Decimal
*********************************************************************/
package com.cardinal.ws.physicalinventory.product.valueobjects;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author vignesh.kandadi
 *
 */
public class InvLocProductResponse extends InvProduct implements Serializable {
	
	private static final long serialVersionUID = -6790528675018254322L;
	
	public int locLineId;
	public String product;
	public BigDecimal afxUOIF;
	public String countType;
	public double overrideCost;
	public double valuationCost; 
	public double quantity;
	public double inventoryValue;
	public int seq;
	public int locLineType;
	public Date createdDate;	
	public String lineComments;
	public boolean duplicate;
	/**
	 * @return the locLineId
	 */
	public int getLocLineId() {
		return locLineId;
	}
	/**
	 * @param locLineId the locLineId to set
	 */
	public void setLocLineId(int locLineId) {
		this.locLineId = locLineId;
	}
	/**
	 * @return the product
	 */
	public String getProduct() {
		return product;
	}
	/**
	 * @param product the product to set
	 */
	public void setProduct(String product) {
		this.product = product;
	}
	/**
	 * @return the afxUOIF
	 */
	public BigDecimal getAfxUOIF() {
		return afxUOIF;
	}
	/**
	 * @param afxUOIF the afxUOIF to set
	 */
	public void setAfxUOIF(BigDecimal afxUOIF) {
		this.afxUOIF = afxUOIF;
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
	 * @return the overrideCost
	 */
	public double getOverrideCost() {
		return overrideCost;
	}
	/**
	 * @param overrideCost the overrideCost to set
	 */
	public void setOverrideCost(double overrideCost) {
		this.overrideCost = overrideCost;
	}
	/**
	 * @return the valuationCost
	 */
	public double getValuationCost() {
		return valuationCost;
	}
	/**
	 * @param valuationCost the valuationCost to set
	 */
	public void setValuationCost(double valuationCost) {
		this.valuationCost = valuationCost;
	}
	/**
	 * @return the quantity
	 */
	public double getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	/**
	 * @return the inventoryValue
	 */
	public double getInventoryValue() {
		return inventoryValue;
	}
	/**
	 * @param inventoryValue the inventoryValue to set
	 */
	public void setInventoryValue(double inventoryValue) {
		this.inventoryValue = inventoryValue;
	}
	/**
	 * @return the seq
	 */
	public int getSeq() {
		return seq;
	}
	/**
	 * @param seq the seq to set
	 */
	public void setSeq(int seq) {
		this.seq = seq;
	}
	/**
	 * @return the locLineType
	 */
	public int getLocLineType() {
		return locLineType;
	}
	/**
	 * @param locLineType the locLineType to set
	 */
	public void setLocLineType(int locLineType) {
		this.locLineType = locLineType;
	}
	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @return the lineComments
	 */
	public String getLineComments() {
		return lineComments;
	}
	/**
	 * @param lineComments the lineComments to set
	 */
	public void setLineComments(String lineComments) {
		this.lineComments = lineComments;
	}
	
	
	/**
	 * @return the duplicate
	 */
	public boolean isDuplicate() {
		return duplicate;
	}
	/**
	 * @param duplicate the duplicate to set
	 */
	public void setDuplicate(boolean duplicate) {
		this.duplicate = duplicate;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InvLocProductResponse [locLineId=");
		builder.append(locLineId);
		builder.append(", product=");
		builder.append(product);
		builder.append(", afxUOIF=");
		builder.append(afxUOIF);
		builder.append(", countType=");
		builder.append(countType);
		builder.append(", overrideCost=");
		builder.append(overrideCost);
		builder.append(", valuationCost=");
		builder.append(valuationCost);
		builder.append(", quantity=");
		builder.append(quantity);
		builder.append(", inventoryValue=");
		builder.append(inventoryValue);
		builder.append(", seq=");
		builder.append(seq);
		builder.append(", locLineType=");
		builder.append(locLineType);
		builder.append(", createdDate=");
		builder.append(createdDate);
		builder.append(", lineComments=");
		builder.append(lineComments);
		builder.append(", duplicate=");
		builder.append(duplicate);
		builder.append("]");
		return builder.toString();
	}
	
	
}
