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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the INVTRY database table.
 * 
 */
@Entity
@Table(name="INVTRY", schema ="OE_PI")
public class Invtry implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="INVTRYNUM_GENERATOR", sequenceName="OE_PI.INVTRY_NUM_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="INVTRYNUM_GENERATOR")
	@Column(name="INVTRY_NUM")
	private long invtryNum;

	@Column(name="CREATE_STP")
	private Timestamp createStp;

	@Column(name="INVTRY_NAM")
	private String invtryNam;

	@Column(name="INVTRY_TOTAL_VALUE_AMT")
	private BigDecimal invtryTotalValueAmt;

	@Column(name="ROW_ADD_STP")
	private Timestamp rowAddStp;

	@Column(name="ROW_ADD_USER_ID")
	private String rowAddUserId;

	@Column(name="ROW_UPDATE_STP")
	private Timestamp rowUpdateStp;

	@Column(name="ROW_UPDATE_USER_ID")
	private String rowUpdateUserId;

	@Column(name="SHIP_TO_CUSTOMER_NUM")
	private BigDecimal shipToCustomerNum;

	@Column(name="SHIP_TO_LOCATION_NUM")
	private BigDecimal shipToLocationNum;
	
	//bi-directional many-to-one association to Location
	@OneToMany(mappedBy="invtry", fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	
	private List<Location> locations;
	
    public Invtry() {
    }

	public long getInvtryNum() {
		return this.invtryNum;
	}

	public void setInvtryNum(long invtryNum) {
		this.invtryNum = invtryNum;
	}

	public Timestamp getCreateStp() {
		return this.createStp;
	}

	public void setCreateStp(Timestamp createStp) {
		this.createStp = createStp;
	}

	public String getInvtryNam() {
		return this.invtryNam;
	}

	public void setInvtryNam(String invtryNam) {
		this.invtryNam = invtryNam;
	}

	public BigDecimal getInvtryTotalValueAmt() {
		return this.invtryTotalValueAmt;
	}

	public void setInvtryTotalValueAmt(BigDecimal invtryTotalValueAmt) {
		this.invtryTotalValueAmt = invtryTotalValueAmt;
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

	public BigDecimal getShipToCustomerNum() {
		return this.shipToCustomerNum;
	}

	public void setShipToCustomerNum(BigDecimal shipToCustomerNum) {
		this.shipToCustomerNum = shipToCustomerNum;
	}

	public BigDecimal getShipToLocationNum() {
		return this.shipToLocationNum;
	}

	public void setShipToLocationNum(BigDecimal shipToLocationNum) {
		this.shipToLocationNum = shipToLocationNum;
	}

	public List<Location> getLocations() {
		return this.locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Invtry [invtryNum=");
		builder.append(invtryNum);
		builder.append(", createStp=");
		builder.append(createStp);
		builder.append(", invtryNam=");
		builder.append(invtryNam);
		builder.append(", invtryTotalValueAmt=");
		builder.append(invtryTotalValueAmt);
		builder.append(", rowAddStp=");
		builder.append(rowAddStp);
		builder.append(", rowAddUserId=");
		builder.append(rowAddUserId);
		builder.append(", rowUpdateStp=");
		builder.append(rowUpdateStp);
		builder.append(", rowUpdateUserId=");
		builder.append(rowUpdateUserId);
		builder.append(", shipToCustomerNum=");
		builder.append(shipToCustomerNum);
		builder.append(", shipToLocationNum=");
		builder.append(shipToLocationNum);
		builder.append(", locations=");
		builder.append(locations);
		builder.append("]");
		return builder.toString();
	}
}