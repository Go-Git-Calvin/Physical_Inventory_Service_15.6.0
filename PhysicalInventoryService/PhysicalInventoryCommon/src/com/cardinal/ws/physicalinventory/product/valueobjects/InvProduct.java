package com.cardinal.ws.physicalinventory.product.valueobjects;

import java.io.Serializable;
import java.util.Date;

import com.cardinal.ws.physicalinventory.common.valueobjects.InvBaseResponse;

public class InvProduct extends InvBaseResponse implements Serializable{ 
	public static final long serialVersionUID = 1L;

	public String genericName;
	public String tradeName;
	public String stockStatus;
	public String description;
	public String productNumber;
	public boolean isNonCardinal;
	public String ndc;
	public String upc;
	public String ndcUpc;
	public String contract;
	public String contractAlias;
	public String contractExpires;
	public String strength;
	public String size;
	public String form;
	public String abRating;
	public String hammacherZoneSettings;
	public String generic;
	public String reorderPoint;
	public long reorderQuantity;
	public double cost;
	public double medispanAWP;
	public double uoiCost;
	public Date dateLastPurchased;
	public Integer qtyLastPurchased;
	public Date costLastChanged;
	public double costLastPurchased;
	public Integer amu;
	public String abcRanking;
	public String labelSize;
	public String mfr;
	public String mfrPartNumber;
	public String productType;
	public long totalSize;
	public double retailPrice;
	public String retailPriceOverride;
	public String returnable;
	public String awu;
	public String cardinalKey;
	public Integer gcn;
	public Integer packageQty;
	public Integer packageSize;
	public Integer caseQty;
	public String unitDose;
	public String unitofMeasure;
	public String unitofSale;
	public long ahfsCode;
	public String ahfsDesc;
	public Integer deaSchedulel;
	public String fdbManuf;
	public String fdbLabelName;
	public String fdbFlavorDesc;
	public String fdbShape;
	public String fdbColor;
	public String fdbAdditionalDesc;
	public String finelineDesc;
	public String medispanGPICode;
	public String medicaidJCode;
	public String shortDesc;
	public String longDesc;
	public String itemDesc;
	public String marketingDesc;
	public String contractRank;
	public String contractGroup;
	public String contractGroupAffiliationNumber;
	public String mfrAbbrv;
	public String department;
	public String amp;
	public String fdbAWP;
	public String salesRank;
	public String orangeBookCode;
	public String customFields;
	public String additionalComment;
	public String altProductContractFlag;
	public String altProductFlag;
	public String catentryID;
	public String contractGroupNumber;
	public String desiIndicator;
	public String displayNDC;
	public double estimatedNetCost;
	public String estimatedRebate;
	public String formDesc;
	public double invoiceCost;
	public String primaryImageNumber;
	public String stockStatusAbbrv;
	public String suggestedOrderQty;
	public String unitOfMeasureAbbrv;
	public String noOfImages;
	public Date lastOrderedDate;
	public String lastOrderedQty;
	public String lastOrderedCost;
	public String stockstatusDefinition;
	public String previouslyPurchase;
	public boolean isEligible;
	public String callToOrder;
	public String longForm;
	public String availabilityAlert;
	public String availabilityAlertAbbrv;
	public String availableMessage;
	public Date changeDate;
	public Date originalDate;
	public Date expireDeliveryDate;
	public double cardinalRefPrice;
	public double sellPrice;
	public String rebateIndicator;
	public String newProductIndicator;
	public String productImageIndicator;
	public String nonreturnableIndicator;
	public String qtyAvailable;
	public int notes;
	public String onFormulary;
	public String UDF1; // NOPMD by stephen.perry01 on 5/25/13 1:07 PM  Don't want to break schema
	public String UDF2; // NOPMD by stephen.perry01 on 5/25/13 1:07 PM  Don't want to break schema
	public Double uoif;
	public String categoryCode1;
	public String categoryDescription1;
	public String categoryCode2;
	public String categoryDescription2;
	public String contrackColor;
	public String asscColor;
	public String contractDisplay;
	public String csn;
    public String message;
    public String minDaysOfSupply;
    public String maxDaysOfSupply;
    public Long projectedUsage;
    public Date projectedUsageExpirationDate;
	/**
	 * @return the genericName
	 */
	public String getGenericName() {
		return genericName;
	}
	/**
	 * @param genericName the genericName to set
	 */
	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}
	/**
	 * @return the tradeName
	 */
	public String getTradeName() {
		return tradeName;
	}
	/**
	 * @param tradeName the tradeName to set
	 */
	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}
	/**
	 * @return the stockStatus
	 */
	public String getStockStatus() {
		return stockStatus;
	}
	/**
	 * @param stockStatus the stockStatus to set
	 */
	public void setStockStatus(String stockStatus) {
		this.stockStatus = stockStatus;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the productNumber
	 */
	public String getProductNumber() {
		return productNumber;
	}
	/**
	 * @param productNumber the productNumber to set
	 */
	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}
	/**
	 * @return the isNonCardinal
	 */
	public boolean isNonCardinal() {
		return isNonCardinal;
	}
	/**
	 * @param isNonCardinal the isNonCardinal to set
	 */
	public void setNonCardinal(boolean isNonCardinal) {
		this.isNonCardinal = isNonCardinal;
	}
	/**
	 * @return the ndc
	 */
	public String getNdc() {
		return ndc;
	}
	/**
	 * @param ndc the ndc to set
	 */
	public void setNdc(String ndc) {
		this.ndc = ndc;
	}
	/**
	 * @return the upc
	 */
	public String getUpc() {
		return upc;
	}
	/**
	 * @param upc the upc to set
	 */
	public void setUpc(String upc) {
		this.upc = upc;
	}
	/**
	 * @return the ndcUpc
	 */
	public String getNdcUpc() {
		return ndcUpc;
	}
	/**
	 * @param ndcUpc the ndcUpc to set
	 */
	public void setNdcUpc(String ndcUpc) {
		this.ndcUpc = ndcUpc;
	}
	/**
	 * @return the contract
	 */
	public String getContract() {
		return contract;
	}
	/**
	 * @param contract the contract to set
	 */
	public void setContract(String contract) {
		this.contract = contract;
	}
	/**
	 * @return the contractAlias
	 */
	public String getContractAlias() {
		return contractAlias;
	}
	/**
	 * @param contractAlias the contractAlias to set
	 */
	public void setContractAlias(String contractAlias) {
		this.contractAlias = contractAlias;
	}
	/**
	 * @return the contractExpires
	 */
	public String getContractExpires() {
		return contractExpires;
	}
	/**
	 * @param contractExpires the contractExpires to set
	 */
	public void setContractExpires(String contractExpires) {
		this.contractExpires = contractExpires;
	}
	/**
	 * @return the strength
	 */
	public String getStrength() {
		return strength;
	}
	/**
	 * @param strength the strength to set
	 */
	public void setStrength(String strength) {
		this.strength = strength;
	}
	/**
	 * @return the size
	 */
	public String getSize() {
		return size;
	}
	/**
	 * @param size the size to set
	 */
	public void setSize(String size) {
		this.size = size;
	}
	/**
	 * @return the form
	 */
	public String getForm() {
		return form;
	}
	/**
	 * @param form the form to set
	 */
	public void setForm(String form) {
		this.form = form;
	}
	/**
	 * @return the abRating
	 */
	public String getAbRating() {
		return abRating;
	}
	/**
	 * @param abRating the abRating to set
	 */
	public void setAbRating(String abRating) {
		this.abRating = abRating;
	}
	/**
	 * @return the hammacherZoneSettings
	 */
	public String getHammacherZoneSettings() {
		return hammacherZoneSettings;
	}
	/**
	 * @param hammacherZoneSettings the hammacherZoneSettings to set
	 */
	public void setHammacherZoneSettings(String hammacherZoneSettings) {
		this.hammacherZoneSettings = hammacherZoneSettings;
	}
	/**
	 * @return the generic
	 */
	public String getGeneric() {
		return generic;
	}
	/**
	 * @param generic the generic to set
	 */
	public void setGeneric(String generic) {
		this.generic = generic;
	}
	/**
	 * @return the reorderPoint
	 */
	public String getReorderPoint() {
		return reorderPoint;
	}
	/**
	 * @param reorderPoint the reorderPoint to set
	 */
	public void setReorderPoint(String reorderPoint) {
		this.reorderPoint = reorderPoint;
	}
	/**
	 * @return the reorderQuantity
	 */
	public long getReorderQuantity() {
		return reorderQuantity;
	}
	/**
	 * @param reorderQuantity the reorderQuantity to set
	 */
	public void setReorderQuantity(long reorderQuantity) {
		this.reorderQuantity = reorderQuantity;
	}
	/**
	 * @return the cost
	 */
	public double getCost() {
		return cost;
	}
	/**
	 * @param cost the cost to set
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}
	/**
	 * @return the medispanAWP
	 */
	public double getMedispanAWP() {
		return medispanAWP;
	}
	/**
	 * @param medispanAWP the medispanAWP to set
	 */
	public void setMedispanAWP(double medispanAWP) {
		this.medispanAWP = medispanAWP;
	}
	/**
	 * @return the uoiCost
	 */
	public double getUoiCost() {
		return uoiCost;
	}
	/**
	 * @param uoiCost the uoiCost to set
	 */
	public void setUoiCost(double uoiCost) {
		this.uoiCost = uoiCost;
	}
	/**
	 * @return the dateLastPurchased
	 */
	public Date getDateLastPurchased() {
		return dateLastPurchased;
	}
	/**
	 * @param dateLastPurchased the dateLastPurchased to set
	 */
	public void setDateLastPurchased(Date dateLastPurchased) {
		this.dateLastPurchased = dateLastPurchased;
	}
	/**
	 * @return the qtyLastPurchased
	 */
	public Integer getQtyLastPurchased() {
		return qtyLastPurchased;
	}
	/**
	 * @param qtyLastPurchased the qtyLastPurchased to set
	 */
	public void setQtyLastPurchased(Integer qtyLastPurchased) {
		this.qtyLastPurchased = qtyLastPurchased;
	}
	/**
	 * @return the costLastChanged
	 */
	public Date getCostLastChanged() {
		return costLastChanged;
	}
	/**
	 * @param costLastChanged the costLastChanged to set
	 */
	public void setCostLastChanged(Date costLastChanged) {
		this.costLastChanged = costLastChanged;
	}
	/**
	 * @return the costLastPurchased
	 */
	public double getCostLastPurchased() {
		return costLastPurchased;
	}
	/**
	 * @param costLastPurchased the costLastPurchased to set
	 */
	public void setCostLastPurchased(double costLastPurchased) {
		this.costLastPurchased = costLastPurchased;
	}
	/**
	 * @return the amu
	 */
	public Integer getAmu() {
		return amu;
	}
	/**
	 * @param amu the amu to set
	 */
	public void setAmu(Integer amu) {
		this.amu = amu;
	}
	/**
	 * @return the abcRanking
	 */
	public String getAbcRanking() {
		return abcRanking;
	}
	/**
	 * @param abcRanking the abcRanking to set
	 */
	public void setAbcRanking(String abcRanking) {
		this.abcRanking = abcRanking;
	}
	/**
	 * @return the labelSize
	 */
	public String getLabelSize() {
		return labelSize;
	}
	/**
	 * @param labelSize the labelSize to set
	 */
	public void setLabelSize(String labelSize) {
		this.labelSize = labelSize;
	}
	/**
	 * @return the mfr
	 */
	public String getMfr() {
		return mfr;
	}
	/**
	 * @param mfr the mfr to set
	 */
	public void setMfr(String mfr) {
		this.mfr = mfr;
	}
	/**
	 * @return the mfrPartNumber
	 */
	public String getMfrPartNumber() {
		return mfrPartNumber;
	}
	/**
	 * @param mfrPartNumber the mfrPartNumber to set
	 */
	public void setMfrPartNumber(String mfrPartNumber) {
		this.mfrPartNumber = mfrPartNumber;
	}
	/**
	 * @return the productType
	 */
	public String getProductType() {
		return productType;
	}
	/**
	 * @param productType the productType to set
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}
	/**
	 * @return the totalSize
	 */
	public long getTotalSize() {
		return totalSize;
	}
	/**
	 * @param totalSize the totalSize to set
	 */
	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}
	/**
	 * @return the retailPrice
	 */
	public double getRetailPrice() {
		return retailPrice;
	}
	/**
	 * @param retailPrice the retailPrice to set
	 */
	public void setRetailPrice(double retailPrice) {
		this.retailPrice = retailPrice;
	}
	/**
	 * @return the retailPriceOverride
	 */
	public String getRetailPriceOverride() {
		return retailPriceOverride;
	}
	/**
	 * @param retailPriceOverride the retailPriceOverride to set
	 */
	public void setRetailPriceOverride(String retailPriceOverride) {
		this.retailPriceOverride = retailPriceOverride;
	}
	/**
	 * @return the returnable
	 */
	public String getReturnable() {
		return returnable;
	}
	/**
	 * @param returnable the returnable to set
	 */
	public void setReturnable(String returnable) {
		this.returnable = returnable;
	}
	/**
	 * @return the awu
	 */
	public String getAwu() {
		return awu;
	}
	/**
	 * @param awu the awu to set
	 */
	public void setAwu(String awu) {
		this.awu = awu;
	}
	/**
	 * @return the cardinalKey
	 */
	public String getCardinalKey() {
		return cardinalKey;
	}
	/**
	 * @param cardinalKey the cardinalKey to set
	 */
	public void setCardinalKey(String cardinalKey) {
		this.cardinalKey = cardinalKey;
	}
	/**
	 * @return the gcn
	 */
	public Integer getGcn() {
		return gcn;
	}
	/**
	 * @param gcn the gcn to set
	 */
	public void setGcn(Integer gcn) {
		this.gcn = gcn;
	}
	/**
	 * @return the packageQty
	 */
	public Integer getPackageQty() {
		return packageQty;
	}
	/**
	 * @param packageQty the packageQty to set
	 */
	public void setPackageQty(Integer packageQty) {
		this.packageQty = packageQty;
	}
	/**
	 * @return the packageSize
	 */
	public Integer getPackageSize() {
		return packageSize;
	}
	/**
	 * @param packageSize the packageSize to set
	 */
	public void setPackageSize(Integer packageSize) {
		this.packageSize = packageSize;
	}
	/**
	 * @return the caseQty
	 */
	public Integer getCaseQty() {
		return caseQty;
	}
	/**
	 * @param caseQty the caseQty to set
	 */
	public void setCaseQty(Integer caseQty) {
		this.caseQty = caseQty;
	}
	/**
	 * @return the unitDose
	 */
	public String getUnitDose() {
		return unitDose;
	}
	/**
	 * @param unitDose the unitDose to set
	 */
	public void setUnitDose(String unitDose) {
		this.unitDose = unitDose;
	}
	/**
	 * @return the unitofMeasure
	 */
	public String getUnitofMeasure() {
		return unitofMeasure;
	}
	/**
	 * @param unitofMeasure the unitofMeasure to set
	 */
	public void setUnitofMeasure(String unitofMeasure) {
		this.unitofMeasure = unitofMeasure;
	}
	/**
	 * @return the unitofSale
	 */
	public String getUnitofSale() {
		return unitofSale;
	}
	/**
	 * @param unitofSale the unitofSale to set
	 */
	public void setUnitofSale(String unitofSale) {
		this.unitofSale = unitofSale;
	}
	/**
	 * @return the ahfsCode
	 */
	public long getAhfsCode() {
		return ahfsCode;
	}
	/**
	 * @param ahfsCode the ahfsCode to set
	 */
	public void setAhfsCode(long ahfsCode) {
		this.ahfsCode = ahfsCode;
	}
	/**
	 * @return the ahfsDesc
	 */
	public String getAhfsDesc() {
		return ahfsDesc;
	}
	/**
	 * @param ahfsDesc the ahfsDesc to set
	 */
	public void setAhfsDesc(String ahfsDesc) {
		this.ahfsDesc = ahfsDesc;
	}
	/**
	 * @return the deaSchedulel
	 */
	public Integer getDeaSchedulel() {
		return deaSchedulel;
	}
	/**
	 * @param deaSchedulel the deaSchedulel to set
	 */
	public void setDeaSchedulel(Integer deaSchedulel) {
		this.deaSchedulel = deaSchedulel;
	}
	/**
	 * @return the fdbManuf
	 */
	public String getFdbManuf() {
		return fdbManuf;
	}
	/**
	 * @param fdbManuf the fdbManuf to set
	 */
	public void setFdbManuf(String fdbManuf) {
		this.fdbManuf = fdbManuf;
	}
	/**
	 * @return the fdbLabelName
	 */
	public String getFdbLabelName() {
		return fdbLabelName;
	}
	/**
	 * @param fdbLabelName the fdbLabelName to set
	 */
	public void setFdbLabelName(String fdbLabelName) {
		this.fdbLabelName = fdbLabelName;
	}
	/**
	 * @return the fdbFlavorDesc
	 */
	public String getFdbFlavorDesc() {
		return fdbFlavorDesc;
	}
	/**
	 * @param fdbFlavorDesc the fdbFlavorDesc to set
	 */
	public void setFdbFlavorDesc(String fdbFlavorDesc) {
		this.fdbFlavorDesc = fdbFlavorDesc;
	}
	/**
	 * @return the fdbShape
	 */
	public String getFdbShape() {
		return fdbShape;
	}
	/**
	 * @param fdbShape the fdbShape to set
	 */
	public void setFdbShape(String fdbShape) {
		this.fdbShape = fdbShape;
	}
	/**
	 * @return the fdbColor
	 */
	public String getFdbColor() {
		return fdbColor;
	}
	/**
	 * @param fdbColor the fdbColor to set
	 */
	public void setFdbColor(String fdbColor) {
		this.fdbColor = fdbColor;
	}
	/**
	 * @return the fdbAdditionalDesc
	 */
	public String getFdbAdditionalDesc() {
		return fdbAdditionalDesc;
	}
	/**
	 * @param fdbAdditionalDesc the fdbAdditionalDesc to set
	 */
	public void setFdbAdditionalDesc(String fdbAdditionalDesc) {
		this.fdbAdditionalDesc = fdbAdditionalDesc;
	}
	/**
	 * @return the finelineDesc
	 */
	public String getFinelineDesc() {
		return finelineDesc;
	}
	/**
	 * @param finelineDesc the finelineDesc to set
	 */
	public void setFinelineDesc(String finelineDesc) {
		this.finelineDesc = finelineDesc;
	}
	/**
	 * @return the medispanGPICode
	 */
	public String getMedispanGPICode() {
		return medispanGPICode;
	}
	/**
	 * @param medispanGPICode the medispanGPICode to set
	 */
	public void setMedispanGPICode(String medispanGPICode) {
		this.medispanGPICode = medispanGPICode;
	}
	/**
	 * @return the medicaidJCode
	 */
	public String getMedicaidJCode() {
		return medicaidJCode;
	}
	/**
	 * @param medicaidJCode the medicaidJCode to set
	 */
	public void setMedicaidJCode(String medicaidJCode) {
		this.medicaidJCode = medicaidJCode;
	}
	/**
	 * @return the shortDesc
	 */
	public String getShortDesc() {
		return shortDesc;
	}
	/**
	 * @param shortDesc the shortDesc to set
	 */
	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}
	/**
	 * @return the longDesc
	 */
	public String getLongDesc() {
		return longDesc;
	}
	/**
	 * @param longDesc the longDesc to set
	 */
	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}
	/**
	 * @return the itemDesc
	 */
	public String getItemDesc() {
		return itemDesc;
	}
	/**
	 * @param itemDesc the itemDesc to set
	 */
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	/**
	 * @return the marketingDesc
	 */
	public String getMarketingDesc() {
		return marketingDesc;
	}
	/**
	 * @param marketingDesc the marketingDesc to set
	 */
	public void setMarketingDesc(String marketingDesc) {
		this.marketingDesc = marketingDesc;
	}
	/**
	 * @return the contractRank
	 */
	public String getContractRank() {
		return contractRank;
	}
	/**
	 * @param contractRank the contractRank to set
	 */
	public void setContractRank(String contractRank) {
		this.contractRank = contractRank;
	}
	/**
	 * @return the contractGroup
	 */
	public String getContractGroup() {
		return contractGroup;
	}
	/**
	 * @param contractGroup the contractGroup to set
	 */
	public void setContractGroup(String contractGroup) {
		this.contractGroup = contractGroup;
	}
	/**
	 * @return the contractGroupAffiliationNumber
	 */
	public String getContractGroupAffiliationNumber() {
		return contractGroupAffiliationNumber;
	}
	/**
	 * @param contractGroupAffiliationNumber the contractGroupAffiliationNumber to set
	 */
	public void setContractGroupAffiliationNumber(
			String contractGroupAffiliationNumber) {
		this.contractGroupAffiliationNumber = contractGroupAffiliationNumber;
	}
	/**
	 * @return the mfrAbbrv
	 */
	public String getMfrAbbrv() {
		return mfrAbbrv;
	}
	/**
	 * @param mfrAbbrv the mfrAbbrv to set
	 */
	public void setMfrAbbrv(String mfrAbbrv) {
		this.mfrAbbrv = mfrAbbrv;
	}
	/**
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}
	/**
	 * @param department the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}
	/**
	 * @return the amp
	 */
	public String getAmp() {
		return amp;
	}
	/**
	 * @param amp the amp to set
	 */
	public void setAmp(String amp) {
		this.amp = amp;
	}
	/**
	 * @return the fdbAWP
	 */
	public String getFdbAWP() {
		return fdbAWP;
	}
	/**
	 * @param fdbAWP the fdbAWP to set
	 */
	public void setFdbAWP(String fdbAWP) {
		this.fdbAWP = fdbAWP;
	}
	/**
	 * @return the salesRank
	 */
	public String getSalesRank() {
		return salesRank;
	}
	/**
	 * @param salesRank the salesRank to set
	 */
	public void setSalesRank(String salesRank) {
		this.salesRank = salesRank;
	}
	/**
	 * @return the orangeBookCode
	 */
	public String getOrangeBookCode() {
		return orangeBookCode;
	}
	/**
	 * @param orangeBookCode the orangeBookCode to set
	 */
	public void setOrangeBookCode(String orangeBookCode) {
		this.orangeBookCode = orangeBookCode;
	}
	/**
	 * @return the customFields
	 */
	public String getCustomFields() {
		return customFields;
	}
	/**
	 * @param customFields the customFields to set
	 */
	public void setCustomFields(String customFields) {
		this.customFields = customFields;
	}
	/**
	 * @return the additionalComment
	 */
	public String getAdditionalComment() {
		return additionalComment;
	}
	/**
	 * @param additionalComment the additionalComment to set
	 */
	public void setAdditionalComment(String additionalComment) {
		this.additionalComment = additionalComment;
	}
	/**
	 * @return the altProductContractFlag
	 */
	public String getAltProductContractFlag() {
		return altProductContractFlag;
	}
	/**
	 * @param altProductContractFlag the altProductContractFlag to set
	 */
	public void setAltProductContractFlag(String altProductContractFlag) {
		this.altProductContractFlag = altProductContractFlag;
	}
	/**
	 * @return the altProductFlag
	 */
	public String getAltProductFlag() {
		return altProductFlag;
	}
	/**
	 * @param altProductFlag the altProductFlag to set
	 */
	public void setAltProductFlag(String altProductFlag) {
		this.altProductFlag = altProductFlag;
	}
	/**
	 * @return the catentryID
	 */
	public String getCatentryID() {
		return catentryID;
	}
	/**
	 * @param catentryID the catentryID to set
	 */
	public void setCatentryID(String catentryID) {
		this.catentryID = catentryID;
	}
	/**
	 * @return the contractGroupNumber
	 */
	public String getContractGroupNumber() {
		return contractGroupNumber;
	}
	/**
	 * @param contractGroupNumber the contractGroupNumber to set
	 */
	public void setContractGroupNumber(String contractGroupNumber) {
		this.contractGroupNumber = contractGroupNumber;
	}
	/**
	 * @return the desiIndicator
	 */
	public String getDesiIndicator() {
		return desiIndicator;
	}
	/**
	 * @param desiIndicator the desiIndicator to set
	 */
	public void setDesiIndicator(String desiIndicator) {
		this.desiIndicator = desiIndicator;
	}
	/**
	 * @return the displayNDC
	 */
	public String getDisplayNDC() {
		return displayNDC;
	}
	/**
	 * @param displayNDC the displayNDC to set
	 */
	public void setDisplayNDC(String displayNDC) {
		this.displayNDC = displayNDC;
	}
	/**
	 * @return the estimatedNetCost
	 */
	public double getEstimatedNetCost() {
		return estimatedNetCost;
	}
	/**
	 * @param estimatedNetCost the estimatedNetCost to set
	 */
	public void setEstimatedNetCost(double estimatedNetCost) {
		this.estimatedNetCost = estimatedNetCost;
	}
	/**
	 * @return the estimatedRebate
	 */
	public String getEstimatedRebate() {
		return estimatedRebate;
	}
	/**
	 * @param estimatedRebate the estimatedRebate to set
	 */
	public void setEstimatedRebate(String estimatedRebate) {
		this.estimatedRebate = estimatedRebate;
	}
	/**
	 * @return the formDesc
	 */
	public String getFormDesc() {
		return formDesc;
	}
	/**
	 * @param formDesc the formDesc to set
	 */
	public void setFormDesc(String formDesc) {
		this.formDesc = formDesc;
	}
	/**
	 * @return the invoiceCost
	 */
	public double getInvoiceCost() {
		return invoiceCost;
	}
	/**
	 * @param invoiceCost the invoiceCost to set
	 */
	public void setInvoiceCost(double invoiceCost) {
		this.invoiceCost = invoiceCost;
	}
	/**
	 * @return the primaryImageNumber
	 */
	public String getPrimaryImageNumber() {
		return primaryImageNumber;
	}
	/**
	 * @param primaryImageNumber the primaryImageNumber to set
	 */
	public void setPrimaryImageNumber(String primaryImageNumber) {
		this.primaryImageNumber = primaryImageNumber;
	}
	/**
	 * @return the stockStatusAbbrv
	 */
	public String getStockStatusAbbrv() {
		return stockStatusAbbrv;
	}
	/**
	 * @param stockStatusAbbrv the stockStatusAbbrv to set
	 */
	public void setStockStatusAbbrv(String stockStatusAbbrv) {
		this.stockStatusAbbrv = stockStatusAbbrv;
	}
	/**
	 * @return the suggestedOrderQty
	 */
	public String getSuggestedOrderQty() {
		return suggestedOrderQty;
	}
	/**
	 * @param suggestedOrderQty the suggestedOrderQty to set
	 */
	public void setSuggestedOrderQty(String suggestedOrderQty) {
		this.suggestedOrderQty = suggestedOrderQty;
	}
	/**
	 * @return the unitOfMeasureAbbrv
	 */
	public String getUnitOfMeasureAbbrv() {
		return unitOfMeasureAbbrv;
	}
	/**
	 * @param unitOfMeasureAbbrv the unitOfMeasureAbbrv to set
	 */
	public void setUnitOfMeasureAbbrv(String unitOfMeasureAbbrv) {
		this.unitOfMeasureAbbrv = unitOfMeasureAbbrv;
	}
	/**
	 * @return the noOfImages
	 */
	public String getNoOfImages() {
		return noOfImages;
	}
	/**
	 * @param noOfImages the noOfImages to set
	 */
	public void setNoOfImages(String noOfImages) {
		this.noOfImages = noOfImages;
	}
	/**
	 * @return the lastOrderedDate
	 */
	public Date getLastOrderedDate() {
		return lastOrderedDate;
	}
	/**
	 * @param lastOrderedDate the lastOrderedDate to set
	 */
	public void setLastOrderedDate(Date lastOrderedDate) {
		this.lastOrderedDate = lastOrderedDate;
	}
	/**
	 * @return the lastOrderedQty
	 */
	public String getLastOrderedQty() {
		return lastOrderedQty;
	}
	/**
	 * @param lastOrderedQty the lastOrderedQty to set
	 */
	public void setLastOrderedQty(String lastOrderedQty) {
		this.lastOrderedQty = lastOrderedQty;
	}
	/**
	 * @return the lastOrderedCost
	 */
	public String getLastOrderedCost() {
		return lastOrderedCost;
	}
	/**
	 * @param lastOrderedCost the lastOrderedCost to set
	 */
	public void setLastOrderedCost(String lastOrderedCost) {
		this.lastOrderedCost = lastOrderedCost;
	}
	/**
	 * @return the stockstatusDefinition
	 */
	public String getStockstatusDefinition() {
		return stockstatusDefinition;
	}
	/**
	 * @param stockstatusDefinition the stockstatusDefinition to set
	 */
	public void setStockstatusDefinition(String stockstatusDefinition) {
		this.stockstatusDefinition = stockstatusDefinition;
	}
	/**
	 * @return the previouslyPurchase
	 */
	public String getPreviouslyPurchase() {
		return previouslyPurchase;
	}
	/**
	 * @param previouslyPurchase the previouslyPurchase to set
	 */
	public void setPreviouslyPurchase(String previouslyPurchase) {
		this.previouslyPurchase = previouslyPurchase;
	}
	/**
	 * @return the isEligible
	 */
	public boolean isEligible() {
		return isEligible;
	}
	/**
	 * @param isEligible the isEligible to set
	 */
	public void setEligible(boolean isEligible) {
		this.isEligible = isEligible;
	}
	/**
	 * @return the categoryCode1
	 */
	public String getCategoryCode1() {
		return categoryCode1;
	}
	/**
	 * @param categoryCode1 the categoryCode1 to set
	 */
	public void setCategoryCode1(String categoryCode1) {
		this.categoryCode1 = categoryCode1;
	}
	/**
	 * @return the categoryDescription1
	 */
	public String getCategoryDescription1() {
		return categoryDescription1;
	}
	/**
	 * @param categoryDescription1 the categoryDescription1 to set
	 */
	public void setCategoryDescription1(String categoryDescription1) {
		this.categoryDescription1 = categoryDescription1;
	}
	/**
	 * @return the categoryCode2
	 */
	public String getCategoryCode2() {
		return categoryCode2;
	}
	/**
	 * @param categoryCode2 the categoryCode2 to set
	 */
	public void setCategoryCode2(String categoryCode2) {
		this.categoryCode2 = categoryCode2;
	}
	/**
	 * @return the categoryDescription2
	 */
	public String getCategoryDescription2() {
		return categoryDescription2;
	}
	/**
	 * @param categoryDescription2 the categoryDescription2 to set
	 */
	public void setCategoryDescription2(String categoryDescription2) {
		this.categoryDescription2 = categoryDescription2;
	}
	/**
	 * @return the callToOrder
	 */
	public String getCallToOrder() {
		return callToOrder;
	}
	/**
	 * @param callToOrder the callToOrder to set
	 */
	public void setCallToOrder(String callToOrder) {
		this.callToOrder = callToOrder;
	}
	/**
	 * @return the longForm
	 */
	public String getLongForm() {
		return longForm;
	}
	/**
	 * @param longForm the longForm to set
	 */
	public void setLongForm(String longForm) {
		this.longForm = longForm;
	}
	/**
	 * @return the availabilityAlert
	 */
	public String getAvailabilityAlert() {
		return availabilityAlert;
	}
	/**
	 * @param availabilityAlert the availabilityAlert to set
	 */
	public void setAvailabilityAlert(String availabilityAlert) {
		this.availabilityAlert = availabilityAlert;
	}
	/**
	 * @return the availabilityAlertAbbrv
	 */
	public String getAvailabilityAlertAbbrv() {
		return availabilityAlertAbbrv;
	}
	/**
	 * @param availabilityAlertAbbrv the availabilityAlertAbbrv to set
	 */
	public void setAvailabilityAlertAbbrv(String availabilityAlertAbbrv) {
		this.availabilityAlertAbbrv = availabilityAlertAbbrv;
	}
	/**
	 * @return the availableMessage
	 */
	public String getAvailableMessage() {
		return availableMessage;
	}
	/**
	 * @param availableMessage the availableMessage to set
	 */
	public void setAvailableMessage(String availableMessage) {
		this.availableMessage = availableMessage;
	}
	/**
	 * @return the changeDate
	 */
	public Date getChangeDate() {
		return changeDate;
	}
	/**
	 * @param changeDate the changeDate to set
	 */
	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}
	/**
	 * @return the originalDate
	 */
	public Date getOriginalDate() {
		return originalDate;
	}
	/**
	 * @param originalDate the originalDate to set
	 */
	public void setOriginalDate(Date originalDate) {
		this.originalDate = originalDate;
	}
	/**
	 * @return the expireDeliveryDate
	 */
	public Date getExpireDeliveryDate() {
		return expireDeliveryDate;
	}
	/**
	 * @param expireDeliveryDate the expireDeliveryDate to set
	 */
	public void setExpireDeliveryDate(Date expireDeliveryDate) {
		this.expireDeliveryDate = expireDeliveryDate;
	}
	/**
	 * @return the cardinalRefPrice
	 */
	public double getCardinalRefPrice() {
		return cardinalRefPrice;
	}
	/**
	 * @param cardinalRefPrice the cardinalRefPrice to set
	 */
	public void setCardinalRefPrice(double cardinalRefPrice) {
		this.cardinalRefPrice = cardinalRefPrice;
	}
	/**
	 * @return the sellPrice
	 */
	public double getSellPrice() {
		return sellPrice;
	}
	/**
	 * @param sellPrice the sellPrice to set
	 */
	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}
	/**
	 * @return the rebateIndicator
	 */
	public String getRebateIndicator() {
		return rebateIndicator;
	}
	/**
	 * @param rebateIndicator the rebateIndicator to set
	 */
	public void setRebateIndicator(String rebateIndicator) {
		this.rebateIndicator = rebateIndicator;
	}
	/**
	 * @return the newProductIndicator
	 */
	public String getNewProductIndicator() {
		return newProductIndicator;
	}
	/**
	 * @param newProductIndicator the newProductIndicator to set
	 */
	public void setNewProductIndicator(String newProductIndicator) {
		this.newProductIndicator = newProductIndicator;
	}
	/**
	 * @return the productImageIndicator
	 */
	public String getProductImageIndicator() {
		return productImageIndicator;
	}
	/**
	 * @param productImageIndicator the productImageIndicator to set
	 */
	public void setProductImageIndicator(String productImageIndicator) {
		this.productImageIndicator = productImageIndicator;
	}
	/**
	 * @return the nonreturnableIndicator
	 */
	public String getNonreturnableIndicator() {
		return nonreturnableIndicator;
	}
	/**
	 * @param nonreturnableIndicator the nonreturnableIndicator to set
	 */
	public void setNonreturnableIndicator(String nonreturnableIndicator) {
		this.nonreturnableIndicator = nonreturnableIndicator;
	}
	/**
	 * @return the qtyAvailable
	 */
	public String getQtyAvailable() {
		return qtyAvailable;
	}
	/**
	 * @param qtyAvailable the qtyAvailable to set
	 */
	public void setQtyAvailable(String qtyAvailable) {
		this.qtyAvailable = qtyAvailable;
	}
	/**
	 * @return the notes
	 */
	public int getNotes() {
		return notes;
	}
	/**
	 * @param notes the notes to set
	 */
	public void setNotes(int notes) {
		this.notes = notes;
	}
	/**
	 * @return the onFormulary
	 */
	public String getOnFormulary() {
		return onFormulary;
	}
	/**
	 * @param onFormulary the onFormulary to set
	 */
	public void setOnFormulary(String onFormulary) {
		this.onFormulary = onFormulary;
	}
	/**
	 * @return the uDF1
	 */
	public String getUDF1() {
		return UDF1;
	}
	/**
	 * @param uDF1 the uDF1 to set
	 */
	public void setUDF1(String uDF1) {
		UDF1 = uDF1;
	}
	/**
	 * @return the uDF2
	 */
	public String getUDF2() {
		return UDF2;
	}
	/**
	 * @param uDF2 the uDF2 to set
	 */
	public void setUDF2(String uDF2) {
		UDF2 = uDF2;
	}
	/**
	 * @return the uoif
	 */
	public Double getUOIF() {
		return uoif;
	}
	/**
	 * @param uoif the uoif to set
	 */
	public void setUOIF(Double uoif) {
		this.uoif = uoif;
	}
	
	
	/**
	 * @return the contrackColor
	 */
	public String getContrackColor() {
		return contrackColor;
	}
	/**
	 * @param contrackColor the contrackColor to set
	 */
	public void setContrackColor(String contrackColor) {
		this.contrackColor = contrackColor;
	}
	/**
	 * @return the asscColor
	 */
	public String getAsscColor() {
		return asscColor;
	}
	/**
	 * @param asscColor the asscColor to set
	 */
	public void setAsscColor(String asscColor) {
		this.asscColor = asscColor;
	}
	/**
	 * @return the contractConverted
	 */
	public String getContractDisplay() {
		return contractDisplay;
	}
	/**
	 * @param contractConverted the contractConverted to set
	 */
	public void setContractDisplay(String Display) {
		this.contractDisplay = Display;
	}
	/**
	 * @return the csn
	 */
	public String getCsn() {
		return csn;
	}
	/**
	 * @param csn the csn to set
	 */
	public void setCsn(String csn) {
		this.csn = csn;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the minDaysOfSupply
	 */
	public String getMinDaysOfSupply() {
		return minDaysOfSupply;
	}
	/**
	 * @param minDaysOfSupply the minDaysOfSupply to set
	 */
	public void setMinDaysOfSupply(String minDaysOfSupply) {
		this.minDaysOfSupply = minDaysOfSupply;
	}
	/**
	 * @return the maxDaysOfSupply
	 */
	public String getMaxDaysOfSupply() {
		return maxDaysOfSupply;
	}
	/**
	 * @param maxDaysOfSupply the maxDaysOfSupply to set
	 */
	public void setMaxDaysOfSupply(String maxDaysOfSupply) {
		this.maxDaysOfSupply = maxDaysOfSupply;
	}
	/**
	 * @return the projectedUsage
	 */
	public Long getProjectedUsage() {
		return projectedUsage;
	}
	/**
	 * @param projectedUsage the projectedUsage to set
	 */
	public void setProjectedUsage(Long projectedUsage) {
		this.projectedUsage = projectedUsage;
	}
	/**
	 * @return the projectedUsageExpirationDate
	 */
	public Date getProjectedUsageExpirationDate() {
		return projectedUsageExpirationDate;
	}
	/**
	 * @param projectedUsageExpirationDate the projectedUsageExpirationDate to set
	 */
	public void setProjectedUsageExpirationDate(Date projectedUsageExpirationDate) {
		this.projectedUsageExpirationDate = projectedUsageExpirationDate;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InvProduct [genericName=");
		builder.append(genericName);
		builder.append(", tradeName=");
		builder.append(tradeName);
		builder.append(", stockStatus=");
		builder.append(stockStatus);
		builder.append(", description=");
		builder.append(description);
		builder.append(", productNumber=");
		builder.append(productNumber);
		builder.append(", isNonCardinal=");
		builder.append(isNonCardinal);
		builder.append(", ndc=");
		builder.append(ndc);
		builder.append(", upc=");
		builder.append(upc);
		builder.append(", ndcUpc=");
		builder.append(ndcUpc);
		builder.append(", contract=");
		builder.append(contract);
		builder.append(", contractAlias=");
		builder.append(contractAlias);
		builder.append(", contractExpires=");
		builder.append(contractExpires);
		builder.append(", strength=");
		builder.append(strength);
		builder.append(", size=");
		builder.append(size);
		builder.append(", form=");
		builder.append(form);
		builder.append(", abRating=");
		builder.append(abRating);
		builder.append(", hammacherZoneSettings=");
		builder.append(hammacherZoneSettings);
		builder.append(", generic=");
		builder.append(generic);
		builder.append(", reorderPoint=");
		builder.append(reorderPoint);
		builder.append(", reorderQuantity=");
		builder.append(reorderQuantity);
		builder.append(", cost=");
		builder.append(cost);
		builder.append(", medispanAWP=");
		builder.append(medispanAWP);
		builder.append(", uoiCost=");
		builder.append(uoiCost);
		builder.append(", dateLastPurchased=");
		builder.append(dateLastPurchased);
		builder.append(", qtyLastPurchased=");
		builder.append(qtyLastPurchased);
		builder.append(", costLastChanged=");
		builder.append(costLastChanged);
		builder.append(", costLastPurchased=");
		builder.append(costLastPurchased);
		builder.append(", amu=");
		builder.append(amu);
		builder.append(", abcRanking=");
		builder.append(abcRanking);
		builder.append(", labelSize=");
		builder.append(labelSize);
		builder.append(", mfr=");
		builder.append(mfr);
		builder.append(", mfrPartNumber=");
		builder.append(mfrPartNumber);
		builder.append(", productType=");
		builder.append(productType);
		builder.append(", totalSize=");
		builder.append(totalSize);
		builder.append(", retailPrice=");
		builder.append(retailPrice);
		builder.append(", retailPriceOverride=");
		builder.append(retailPriceOverride);
		builder.append(", returnable=");
		builder.append(returnable);
		builder.append(", awu=");
		builder.append(awu);
		builder.append(", cardinalKey=");
		builder.append(cardinalKey);
		builder.append(", gcn=");
		builder.append(gcn);
		builder.append(", packageQty=");
		builder.append(packageQty);
		builder.append(", packageSize=");
		builder.append(packageSize);
		builder.append(", caseQty=");
		builder.append(caseQty);
		builder.append(", unitDose=");
		builder.append(unitDose);
		builder.append(", unitofMeasure=");
		builder.append(unitofMeasure);
		builder.append(", unitofSale=");
		builder.append(unitofSale);
		builder.append(", ahfsCode=");
		builder.append(ahfsCode);
		builder.append(", ahfsDesc=");
		builder.append(ahfsDesc);
		builder.append(", deaSchedulel=");
		builder.append(deaSchedulel);
		builder.append(", fdbManuf=");
		builder.append(fdbManuf);
		builder.append(", fdbLabelName=");
		builder.append(fdbLabelName);
		builder.append(", fdbFlavorDesc=");
		builder.append(fdbFlavorDesc);
		builder.append(", fdbShape=");
		builder.append(fdbShape);
		builder.append(", fdbColor=");
		builder.append(fdbColor);
		builder.append(", fdbAdditionalDesc=");
		builder.append(fdbAdditionalDesc);
		builder.append(", finelineDesc=");
		builder.append(finelineDesc);
		builder.append(", medispanGPICode=");
		builder.append(medispanGPICode);
		builder.append(", medicaidJCode=");
		builder.append(medicaidJCode);
		builder.append(", shortDesc=");
		builder.append(shortDesc);
		builder.append(", longDesc=");
		builder.append(longDesc);
		builder.append(", itemDesc=");
		builder.append(itemDesc);
		builder.append(", marketingDesc=");
		builder.append(marketingDesc);
		builder.append(", contractRank=");
		builder.append(contractRank);
		builder.append(", contractGroup=");
		builder.append(contractGroup);
		builder.append(", contractGroupAffiliationNumber=");
		builder.append(contractGroupAffiliationNumber);
		builder.append(", mfrAbbrv=");
		builder.append(mfrAbbrv);
		builder.append(", department=");
		builder.append(department);
		builder.append(", amp=");
		builder.append(amp);
		builder.append(", fdbAWP=");
		builder.append(fdbAWP);
		builder.append(", salesRank=");
		builder.append(salesRank);
		builder.append(", orangeBookCode=");
		builder.append(orangeBookCode);
		builder.append(", customFields=");
		builder.append(customFields);
		builder.append(", additionalComment=");
		builder.append(additionalComment);
		builder.append(", altProductContractFlag=");
		builder.append(altProductContractFlag);
		builder.append(", altProductFlag=");
		builder.append(altProductFlag);
		builder.append(", catentryID=");
		builder.append(catentryID);
		builder.append(", contractGroupNumber=");
		builder.append(contractGroupNumber);
		builder.append(", desiIndicator=");
		builder.append(desiIndicator);
		builder.append(", displayNDC=");
		builder.append(displayNDC);
		builder.append(", estimatedNetCost=");
		builder.append(estimatedNetCost);
		builder.append(", estimatedRebate=");
		builder.append(estimatedRebate);
		builder.append(", formDesc=");
		builder.append(formDesc);
		builder.append(", invoiceCost=");
		builder.append(invoiceCost);
		builder.append(", primaryImageNumber=");
		builder.append(primaryImageNumber);
		builder.append(", stockStatusAbbrv=");
		builder.append(stockStatusAbbrv);
		builder.append(", suggestedOrderQty=");
		builder.append(suggestedOrderQty);
		builder.append(", unitOfMeasureAbbrv=");
		builder.append(unitOfMeasureAbbrv);
		builder.append(", noOfImages=");
		builder.append(noOfImages);
		builder.append(", lastOrderedDate=");
		builder.append(lastOrderedDate);
		builder.append(", lastOrderedQty=");
		builder.append(lastOrderedQty);
		builder.append(", lastOrderedCost=");
		builder.append(lastOrderedCost);
		builder.append(", stockstatusDefinition=");
		builder.append(stockstatusDefinition);
		builder.append(", previouslyPurchase=");
		builder.append(previouslyPurchase);
		builder.append(", isEligible=");
		builder.append(isEligible);
		builder.append(", callToOrder=");
		builder.append(callToOrder);
		builder.append(", longForm=");
		builder.append(longForm);
		builder.append(", availabilityAlert=");
		builder.append(availabilityAlert);
		builder.append(", availabilityAlertAbbrv=");
		builder.append(availabilityAlertAbbrv);
		builder.append(", availableMessage=");
		builder.append(availableMessage);
		builder.append(", changeDate=");
		builder.append(changeDate);
		builder.append(", originalDate=");
		builder.append(originalDate);
		builder.append(", expireDeliveryDate=");
		builder.append(expireDeliveryDate);
		builder.append(", cardinalRefPrice=");
		builder.append(cardinalRefPrice);
		builder.append(", sellPrice=");
		builder.append(sellPrice);
		builder.append(", rebateIndicator=");
		builder.append(rebateIndicator);
		builder.append(", newProductIndicator=");
		builder.append(newProductIndicator);
		builder.append(", productImageIndicator=");
		builder.append(productImageIndicator);
		builder.append(", nonreturnableIndicator=");
		builder.append(nonreturnableIndicator);
		builder.append(", qtyAvailable=");
		builder.append(qtyAvailable);
		builder.append(", notes=");
		builder.append(notes);
		builder.append(", onFormulary=");
		builder.append(onFormulary);
		builder.append(", UDF1=");
		builder.append(UDF1);
		builder.append(", UDF2=");
		builder.append(UDF2);
		builder.append(", uoif=");
		builder.append(uoif);
		builder.append(", categoryCode1=");
		builder.append(categoryCode1);
		builder.append(", categoryDescription1=");
		builder.append(categoryDescription1);
		builder.append(", categoryCode2=");
		builder.append(categoryCode2);
		builder.append(", categoryDescription2=");
		builder.append(categoryDescription2);
		builder.append(", contrackColor=");
		builder.append(contrackColor);
		builder.append(", asscColor=");
		builder.append(asscColor);
		builder.append(", contractDisplay=");
		builder.append(contractDisplay);
		builder.append(", csn=");
		builder.append(csn);
		builder.append(", message=");
		builder.append(message);
		builder.append(", minDaysOfSupply=");
		builder.append(minDaysOfSupply);
		builder.append(", maxDaysOfSupply=");
		builder.append(maxDaysOfSupply);
		builder.append(", projectedUsage=");
		builder.append(projectedUsage);
		builder.append(", projectedUsageExpirationDate=");
		builder.append(projectedUsageExpirationDate);
		builder.append("]");
		return builder.toString();
	}
	
	//public ProductCustomFields productCustomFields;
	
}
