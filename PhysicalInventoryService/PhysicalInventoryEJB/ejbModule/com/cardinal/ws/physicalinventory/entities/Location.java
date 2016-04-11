package com.cardinal.ws.physicalinventory.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the LOCATION database table.
 * 
 */
@Entity
@Table(name="LOCATION", schema ="OE_PI")
public class Location implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LOCATIONNUM_GENERATOR", sequenceName="OE_PI.LOCATION_NUM_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LOCATIONNUM_GENERATOR")
	@Column(name="LOCATION_NUM")
	private int locationNum;

	@Column(name="CMNT_TXT")
	private String cmntTxt;

	@Column(name="COST_AFX_STP")
	private Timestamp costAfxStp;

	@Column(name="CREATE_STP")
	private Timestamp createStp;

	@Column(name="INVTRY_NUM")
	private int invtryNum;

	@Column(name="LOCATION_NAM")
	private String locationNam;

	@Column(name="ROW_ADD_STP")
	private Timestamp rowAddStp;

	@Column(name="ROW_ADD_USER_ID")
	private String rowAddUserId;

	@Column(name="ROW_UPDATE_STP")
	private Timestamp rowUpdateStp;

	@Column(name="ROW_UPDATE_USER_ID")
	private String rowUpdateUserId;

	@Column(name="TOTAL_LOCATION_VALUE_AMT")
	private BigDecimal totalLocationValueAmt;
	
	@Column(name="COUNT_TYPE_CDE")
	private int countTypeCde;
	
	@Column(name="LOCATION_STAT_CDE")
	private int locStatCde;
	
	@Column(name="COST_TYPE_CDE")
	private int costTypeCde;

	//bi-directional many-to-one association to CostType
    @ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COST_TYPE_CDE")
	private CostType costType;

	//bi-directional many-to-one association to CountType
    @ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COUNT_TYPE_CDE")
	private CountType countType;

	//bi-directional many-to-one association to LocationStat
    @ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LOCATION_STAT_CDE")
	private LocationStat locationStat;

	//bi-directional many-to-one association to LocationDetail
	@OneToMany(mappedBy="location",fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	private List<LocationDetail> locationDetails;
	//bi-directional many-to-one association to Invtry
	@ManyToOne(fetch=FetchType.LAZY)	
	@JoinColumn(name="INVTRY_NUM")
	private Invtry invtry;


    public Location() {
    }

	public int getLocationNum() {
		return this.locationNum;
	}

	public void setLocationNum(int locationNum) {
		this.locationNum = locationNum;
	}

	public String getCmntTxt() {
		return this.cmntTxt;
	}

	public void setCmntTxt(String cmntTxt) {
		this.cmntTxt = cmntTxt;
	}

	public Timestamp getCostAfxStp() {
		return this.costAfxStp;
	}

	public void setCostAfxStp(Timestamp costAfxStp) {
		this.costAfxStp = costAfxStp;
	}

	public Timestamp getCreateStp() {
		return this.createStp;
	}

	public void setCreateStp(Timestamp createStp) {
		this.createStp = createStp;
	}

	public int getInvtryNum() {
		return this.invtryNum;
	}

	public void setInvtryNum(int invtryNum) {
		this.invtryNum = invtryNum;
	}

	public String getLocationNam() {
		return this.locationNam;
	}

	public void setLocationNam(String locationNam) {
		this.locationNam = locationNam;
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

	public BigDecimal getTotalLocationValueAmt() {
		return this.totalLocationValueAmt;
	}

	public void setTotalLocationValueAmt(BigDecimal totalLocationValueAmt) {
		this.totalLocationValueAmt = totalLocationValueAmt;
	}

	public CostType getCostType() {
		return this.costType;
	}

	public void setCostType(CostType costType) {
		this.costType = costType;
	}
	
	public CountType getCountType() {
		return this.countType;
	}

	public void setCountType(CountType countType) {
		this.countType = countType;
	}
	
	public LocationStat getLocationStat() {
		return this.locationStat;
	}

	public void setLocationStat(LocationStat locationStat) {
		this.locationStat = locationStat;
	}
	
	public List<LocationDetail> getLocationDetails() {
		return this.locationDetails;
	}

	public void setLocationDetails(List<LocationDetail> locationDetails) {
		this.locationDetails = locationDetails;
	}
	
	/**
	 * @return the invtry
	 */
	public Invtry getInvtry() {
		return invtry;
	}

	/**
	 * @param invtry the invtry to set
	 */
	public void setInvtry(Invtry invtry) {
		this.invtry = invtry;
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
	 * @return the locStatCde
	 */
	public int getLocStatCde() {
		return locStatCde;
	}

	/**
	 * @param locStatCde the locStatCde to set
	 */
	public void setLocStatCde(int locStatCde) {
		this.locStatCde = locStatCde;
	}

	/**
	 * @return the costTypeCde
	 */
	public int getCostTypeCde() {
		return costTypeCde;
	}

	/**
	 * @param costTypeCde the costTypeCde to set
	 */
	public void setCostTypeCde(int costTypeCde) {
		this.costTypeCde = costTypeCde;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Location [locationNum=");
		builder.append(locationNum);
		builder.append(", cmntTxt=");
		builder.append(cmntTxt);
		builder.append(", costAfxStp=");
		builder.append(costAfxStp);
		builder.append(", createStp=");
		builder.append(createStp);
		builder.append(", invtryNum=");
		builder.append(invtryNum);
		builder.append(", locationNam=");
		builder.append(locationNam);
		builder.append(", rowAddStp=");
		builder.append(rowAddStp);
		builder.append(", rowAddUserId=");
		builder.append(rowAddUserId);
		builder.append(", rowUpdateStp=");
		builder.append(rowUpdateStp);
		builder.append(", rowUpdateUserId=");
		builder.append(rowUpdateUserId);
		builder.append(", totalLocationValueAmt=");
		builder.append(totalLocationValueAmt);
		builder.append(", countTypeCde=");
		builder.append(countTypeCde);
		builder.append(", locStatCde=");
		builder.append(locStatCde);
		builder.append(", costTypeCde=");
		builder.append(costTypeCde);
		builder.append(", costType=");
		builder.append(costType);
		builder.append(", countType=");
		builder.append(countType);
		builder.append(", locationStat=");
		builder.append(locationStat);
		builder.append(", locationDetails=");
		builder.append(locationDetails);
		builder.append(", invtry=");
		builder.append(invtry);
		builder.append("]");
		return builder.toString();
	}
	
}