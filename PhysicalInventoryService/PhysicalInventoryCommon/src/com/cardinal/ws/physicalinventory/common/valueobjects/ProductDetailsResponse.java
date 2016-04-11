/*********************************************************************
 *
 * $Workfile: ProductDetailsPersisterBean.java $
 * Copyright 2011 Cardinal Health
 *
 *********************************************************************
 */
package com.cardinal.ws.physicalinventory.common.valueobjects;

import java.io.Serializable;

/**
 * Value object to set the product details response
 * when the set location cost operation is invoked
 * 
 * @author ankit.mahajan
 * 
 */
public class ProductDetailsResponse implements Serializable{

	private static final long serialVersionUID = -8971254619428815953L;
	
	private String productNumber;
	private String UOIF;
	private String accountUOIF;
	private String formUOIF;
	private double costLastPurchased;
	private double invoiceCost;
	private String nifoCost;
	private Integer packageSize;
	private Integer packageQty;
	
	/**
	 * Start of Getters and Setters for the 
	 * product details response
	 */
	
	/**
	 * 
	 * @return costLastPurchased
	 */
	public double getCostLastPurchased() {
		return costLastPurchased;
	}
	
	/**
	 * 
	 * @param costLastPurchased
	 */
	public void setCostLastPurchased(double costLastPurchased) {
		this.costLastPurchased = costLastPurchased;
	}

	/**
	 * 
	 * @return invoiceCost
	 */
	public double getInvoiceCost() {
		return invoiceCost;
	}

	/**
	 * 
	 * @param invoiceCost
	 */
	public void setInvoiceCost(double invoiceCost) {
		this.invoiceCost = invoiceCost;
	}

	/**
	 * 
	 * @return nifoCost
	 */
	public String getNifoCost() {
		return nifoCost;
	}

	/**
	 * 
	 * @param nifoCost
	 */
	public void setNifoCost(String nifoCost) {
		this.nifoCost = nifoCost;
	}
	

	/**
	 * 
	 * @return formUOIF
	 */
	public String getFormUOIF() {
		return formUOIF;
	}

	/**
	 * 
	 * @param formUOIF
	 */
	public void setFormUOIF(String formUOIF) {
		this.formUOIF = formUOIF;
	}

	/**
	 * 
	 * @return accountUOIF
	 */
	public String getAccountUOIF() {
		return accountUOIF;
	}

	/**
	 * 
	 * @param accountUOIF
	 */
	public void setAccountUOIF(String accountUOIF) {
		this.accountUOIF = accountUOIF;
	}

	/**
	 * 
	 * @return UOIF
	 */
	public String getUOIF() {
		return UOIF;
	}

	/**
	 * 
	 * @param uOIF
	 */
	public void setUOIF(String uOIF) {
		UOIF = uOIF;
	}

	/**
	 * 
	 * @return productNumber
	 */
	public String getProductNumber() {
		return productNumber;
	}

	/**
	 * 
	 * @param productNumber
	 */
	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}
	
	/**
	 * 
	 * @return packageSize
	 */
	public Integer getPackageSize() {
		return packageSize;
	}

	/**
	 * 
	 * @param packageSize
	 */
	public void setPackageSize(Integer packageSize) {
		this.packageSize = packageSize;
	}

	/**
	 * 
	 * @return packageQty
	 */
	public Integer getPackageQty() {
		return packageQty;
	}

	/**
	 * 
	 * @param packageQty
	 */
	public void setPackageQty(Integer packageQty) {
		this.packageQty = packageQty;
	}
	
	public String toString(){		
		
		StringBuilder builder = new StringBuilder();
		builder.append("ProductDetailsResponse Value Object instance has Values:: ");
		builder.append("productNumber=" + getProductNumber());
		builder.append(", costLastPurchased="+getCostLastPurchased());
		builder.append(", UOIF="+getUOIF() );
		builder.append(", accountUOIF="+getAccountUOIF());
		builder.append(", formUOIF="+getFormUOIF());
		builder.append(", invoiceCost="+getInvoiceCost());
		builder.append(", nifoCost="+getNifoCost());
		builder.append(", packageQty="+getPackageQty());
		builder.append(", packageSize="+getPackageSize());
		
		return builder.toString();
	}

}
