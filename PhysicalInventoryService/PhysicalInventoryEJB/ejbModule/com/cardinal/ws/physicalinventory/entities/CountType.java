package com.cardinal.ws.physicalinventory.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the COUNT_TYPE database table.
 * 
 */
@Entity
@Table(name="COUNT_TYPE", schema ="OE_PI")
public class CountType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COUNT_TYPE_CDE")
	private int countTypeCde;

	@Column(name="COUNT_TYPE_DESC")
	private String countTypeDesc;

	@Column(name="ROW_ADD_STP")
	private Timestamp rowAddStp;

	@Column(name="ROW_ADD_USER_ID")
	private String rowAddUserId;

	@Column(name="ROW_UPDATE_STP")
	private Timestamp rowUpdateStp;

	@Column(name="ROW_UPDATE_USER_ID")
	private String rowUpdateUserId;

	//bi-directional many-to-one association to Location
	@OneToMany(mappedBy="countType", fetch=FetchType.LAZY)
	private List<Location> locations;

	//bi-directional many-to-one association to LocationDetail
	@OneToMany(mappedBy="countType", fetch=FetchType.LAZY)
	private List<LocationDetail> locationDetails;

    public CountType() {
    }

	public int getCountTypeCde() {
		return this.countTypeCde;
	}

	public void setCountTypeCde(int countTypeCde) {
		this.countTypeCde = countTypeCde;
	}

	public String getCountTypeDesc() {
		return this.countTypeDesc;
	}

	public void setCountTypeDesc(String countTypeDesc) {
		this.countTypeDesc = countTypeDesc;
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

	public List<Location> getLocations() {
		return this.locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}
	
	public List<LocationDetail> getLocationDetails() {
		return this.locationDetails;
	}

	public void setLocationDetails(List<LocationDetail> locationDetails) {
		this.locationDetails = locationDetails;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CountType [countTypeCde=" + countTypeCde + ", countTypeDesc="
				+ countTypeDesc + ", rowAddStp=" + rowAddStp
				+ ", rowAddUserId=" + rowAddUserId + ", rowUpdateStp="
				+ rowUpdateStp + ", rowUpdateUserId=" + rowUpdateUserId
				+ ", locations=" + locations + ", locationDetails="
				+ locationDetails + "]";
	}
	
	
	
}