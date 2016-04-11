package com.cardinal.ws.physicalinventory.product.valueobjects;

import java.io.Serializable;

public class InvLocProductRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public int locLineId;
	public String product;
	public Double overrideCost;
	public String countType;
	public Double quantity;
	public int seq;
	public String lineComments;	
	public String productType;
	public int getLocLineId() {
		return locLineId;
	}
	public void setLocLineId(int locLineId) {
		this.locLineId = locLineId;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public Double getOverrideCost() {
		return overrideCost;
	}
	public void setOverrideCost(Double overrideCost) {
		this.overrideCost = overrideCost;
	}
	public String getCountType() {
		return countType;
	}
	public void setCountType(String countType) {
		this.countType = countType;
	}

	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getLineComments() {
		return lineComments;
	}
	public void setLineComments(String lineComments) {
		this.lineComments = lineComments;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	@Override
	public String toString() {
		return "InvLocProductRequest [locLineId=" + locLineId + ", product="
				+ product + ", overrideCost=" + overrideCost + ", countType="
				+ countType + ", quantity=" + quantity + ", seq=" + seq
				+ ", lineComments=" + lineComments + ", productType="
				+ productType + "]";
	}
	

	
}
