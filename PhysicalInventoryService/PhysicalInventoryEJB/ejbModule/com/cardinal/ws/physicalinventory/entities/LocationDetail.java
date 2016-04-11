package com.cardinal.ws.physicalinventory.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the LOCATION_DETAIL database table.
 * 
 */
@Entity
@Table(name="LOCATION_DETAIL", schema ="OE_PI")
public class LocationDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LOCATION_DETAILNUM_GENERATOR", sequenceName="OE_PI.LOCATION_DETAIL_NUM_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LOCATION_DETAILNUM_GENERATOR")
	@Column(name="LOCATION_DETAIL_NUM")
	private int locationNum;

	@Column(name="CALC_TOTAL_VALUE_AMT")
	private BigDecimal calcTotalValueAmt;

	@Column(name="CMNT_TXT")
	private String cmntTxt;

	@Column(name="COST_AMT")
	private BigDecimal costAmt;

	@Column(name="COST_UOIF_NUM")
	private BigDecimal costUoifNum;

	@Column(name="LAST_UPDATE_USER_NAM")
	private String lastUpdateUserNam;

	@Column(name="LINE_LEVEL_QTY")
	private BigDecimal lineLevelQty;

	@Column(name="MANUAL_COST_OVRD_AMT")
	private BigDecimal manualCostOvrdAmt;

	@Column(name="PROD_ID")
	private String prodId;

	@Column(name="ROW_ADD_STP")
	private Timestamp rowAddStp;

	@Column(name="ROW_ADD_USER_ID")
	private String rowAddUserId;

	@Column(name="ROW_UPDATE_STP")
	private Timestamp rowUpdateStp;

	@Column(name="ROW_UPDATE_USER_ID")
	private String rowUpdateUserId;

	@Column(name="SEQ_NUM")
	private int seqNum;
	
	@Column(name="COUNT_TYPE_CDE")
	private int countTypeCde;
	
	@Column(name="PROD_ID_TYPE_CDE")
	private int prodIdTypeCde;
	

	//bi-directional many-to-one association to CountType
    @ManyToOne (fetch=FetchType.LAZY)
	@JoinColumn(name="COUNT_TYPE_CDE")
	private CountType countType;

	//bi-directional many-to-one association to Location
    @ManyToOne (fetch=FetchType.LAZY)
	@JoinColumn(name="LOCATION_NUM")
	private Location location;

	//bi-directional many-to-one association to ProdIdType
    @ManyToOne (fetch=FetchType.LAZY)
	@JoinColumn(name="PROD_ID_TYPE_CDE")
	private ProdIdType prodIdType;

    public LocationDetail() {
    }

	public int getLocationNum() {
		return this.locationNum;
	}

	public void setLocationNum(int locationNum) {
		this.locationNum = locationNum;
	}

	public BigDecimal getCalcTotalValueAmt() {
		return this.calcTotalValueAmt;
	}

	public void setCalcTotalValueAmt(BigDecimal calcTotalValueAmt) {
		this.calcTotalValueAmt = calcTotalValueAmt;
	}

	public String getCmntTxt() {
		return this.cmntTxt;
	}

	public void setCmntTxt(String cmntTxt) {
		this.cmntTxt = cmntTxt;
	}

	public BigDecimal getCostAmt() {
		return this.costAmt;
	}

	public void setCostAmt(BigDecimal costAmt) {
		this.costAmt = costAmt;
	}

	public BigDecimal getCostUoifNum() {
		return this.costUoifNum;
	}

	public void setCostUoifNum(BigDecimal costUoifNum) {
		this.costUoifNum = costUoifNum;
	}

	public String getLastUpdateUserNam() {
		return this.lastUpdateUserNam;
	}

	public void setLastUpdateUserNam(String lastUpdateUserNam) {
		this.lastUpdateUserNam = lastUpdateUserNam;
	}

	public BigDecimal getLineLevelQty() {
		return this.lineLevelQty;
	}

	public void setLineLevelQty(BigDecimal lineLevelQty) {
		this.lineLevelQty = lineLevelQty;
	}

	public BigDecimal getManualCostOvrdAmt() {
		return this.manualCostOvrdAmt;
	}

	public void setManualCostOvrdAmt(BigDecimal manualCostOvrdAmt) {
		this.manualCostOvrdAmt = manualCostOvrdAmt;
	}

	public String getProdId() {
		return this.prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public Timestamp getRowAddStp() {
		return this.rowAddStp;
	}

	public void setRowAddStp(Timestamp rowAddStp) {
		this.rowAddStp = rowAddStp;
	}

	public String getRowAddUserId() {
		return this.rowAddUserId;
	}

	public void setRowAddUserId(String rowAddUserId) {
		this.rowAddUserId = rowAddUserId;
	}

	public Timestamp getRowUpdateStp() {
		return this.rowUpdateStp;
	}

	public void setRowUpdateStp(Timestamp rowUpdateStp) {
		this.rowUpdateStp = rowUpdateStp;
	}

	public String getRowUpdateUserId() {
		return this.rowUpdateUserId;
	}

	public void setRowUpdateUserId(String rowUpdateUserId) {
		this.rowUpdateUserId = rowUpdateUserId;
	}

	public int getSeqNum() {
		return this.seqNum;
	}

	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}

	public CountType getCountType() {
		return this.countType;
	}

	public void setCountType(CountType countType) {
		this.countType = countType;
	}
	
	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	public ProdIdType getProdIdType() {
		return this.prodIdType;
	}

	public void setProdIdType(ProdIdType prodIdType) {
		this.prodIdType = prodIdType;
	}

	/**
	 * @return the countTypeCde
	 */
	public int getCountTypeCde() {
		return countTypeCde;
	}

	/**
	 * @param countTypeCde the countTypeCde to set
	 */
	public void setCountTypeCde(int countTypeCde) {
		this.countTypeCde = countTypeCde;
	}

	/**
	 * @return the prodIdTypeCde
	 */
	public int getProdIdTypeCde() {
		return prodIdTypeCde;
	}

	/**
	 * @param prodIdTypeCde the prodIdTypeCde to set
	 */
	public void setProdIdTypeCde(int prodIdTypeCde) {
		this.prodIdTypeCde = prodIdTypeCde;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LocationDetail [locationNum=");
		builder.append(locationNum);
		builder.append(", calcTotalValueAmt=");
		builder.append(calcTotalValueAmt);
		builder.append(", cmntTxt=");
		builder.append(cmntTxt);
		builder.append(", costAmt=");
		builder.append(costAmt);
		builder.append(", costUoifNum=");
		builder.append(costUoifNum);
		builder.append(", lastUpdateUserNam=");
		builder.append(lastUpdateUserNam);
		builder.append(", lineLevelQty=");
		builder.append(lineLevelQty);
		builder.append(", manualCostOvrdAmt=");
		builder.append(manualCostOvrdAmt);
		builder.append(", prodId=");
		builder.append(prodId);
		builder.append(", rowAddStp=");
		builder.append(rowAddStp);
		builder.append(", rowAddUserId=");
		builder.append(rowAddUserId);
		builder.append(", rowUpdateStp=");
		builder.append(rowUpdateStp);
		builder.append(", rowUpdateUserId=");
		builder.append(rowUpdateUserId);
		builder.append(", seqNum=");
		builder.append(seqNum);
		builder.append(", countTypeCde=");
		builder.append(countTypeCde);
		builder.append(", prodIdTypeCde=");
		builder.append(prodIdTypeCde);
		builder.append(", countType=");
		builder.append(countType);
		builder.append(", location=");
		builder.append(location);
		builder.append(", prodIdType=");
		builder.append(prodIdType);
		builder.append("]");
		return builder.toString();
	}
	
}