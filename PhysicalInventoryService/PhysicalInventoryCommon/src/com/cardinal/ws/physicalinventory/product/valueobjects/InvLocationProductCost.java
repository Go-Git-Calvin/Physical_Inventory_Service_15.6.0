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
 Vignesh Kandadi	04/05/2014		  ALM#14444		 	Updated UOIF data field from Integer to Big Decimal
*********************************************************************/

package com.cardinal.ws.physicalinventory.product.valueobjects;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class InvLocationProductCost implements Serializable {
	private static final long serialVersionUID = 1L;
	private String product;
	private BigDecimal cost;
	private BigDecimal uoiCost;
	private BigDecimal uoif;
	private Date costSetDate;
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
	 * @return the cost
	 */
	public BigDecimal getCost() {
		return cost;
	}
	/**
	 * @param cost the cost to set
	 */
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	
	
	public BigDecimal getUoiCost() {
		return uoiCost;
	}
	public void setUoiCost(BigDecimal uoiCost) {
		this.uoiCost = uoiCost;
	}
	/**
	 * @return the uoif
	 */
	public BigDecimal getUoif() {
		return uoif;
	}
	/**
	 * @param uoif the uoif to set
	 */
	public void setUoif(BigDecimal uoif) {
		this.uoif = uoif;
	}
	/**
	 * @return the costSetDate
	 */
	public Date getCostSetDate() {
		return costSetDate;
	}
	/**
	 * @param costSetDate the costSetDate to set
	 */
	public void setCostSetDate(Date costSetDate) {
		this.costSetDate = costSetDate;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InvLocationCost [product=");
		builder.append(product);
		builder.append(", cost=");
		builder.append(cost);
		builder.append(", uoiCost=");
		builder.append(uoiCost);
		builder.append(", uoif=");
		builder.append(uoif);
		builder.append(", costSetDate=");
		builder.append(costSetDate);
		builder.append("]");
		return builder.toString();
	}

}
