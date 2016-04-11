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
 * The persistent class for the LOCATION_STAT database table.
 * 
 */
@Entity
@Table(name="LOCATION_STAT", schema ="OE_PI")
public class LocationStat implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="LOCATION_STAT_CDE")
	private int locationStatCde;

	@Column(name="LOCATION_STAT_DESC")
	private String locationStatDesc;

	@Column(name="ROW_ADD_STP")
	private Timestamp rowAddStp;

	@Column(name="ROW_ADD_USER_ID")
	private String rowAddUserId;

	@Column(name="ROW_UPDATE_STP")
	private Timestamp rowUpdateStp;

	@Column(name="ROW_UPDATE_USER_ID")
	private String rowUpdateUserId;

	//bi-directional many-to-one association to Location
	@OneToMany(mappedBy="locationStat", fetch=FetchType.LAZY)
	private List<Location> locations;

    public LocationStat() {
    }

	public int getLocationStatCde() {
		return this.locationStatCde;
	}

	public void setLocationStatCde(int locationStatCde) {
		this.locationStatCde = locationStatCde;
	}

	public String getLocationStatDesc() {
		return this.locationStatDesc;
	}

	public void setLocationStatDesc(String locationStatDesc) {
		this.locationStatDesc = locationStatDesc;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LocationStat [locationStatCde=");
		builder.append(locationStatCde);
		builder.append(", locationStatDesc=");
		builder.append(locationStatDesc);
		builder.append(", rowAddStp=");
		builder.append(rowAddStp);
		builder.append(", rowAddUserId=");
		builder.append(rowAddUserId);
		builder.append(", rowUpdateStp=");
		builder.append(rowUpdateStp);
		builder.append(", rowUpdateUserId=");
		builder.append(rowUpdateUserId);
		builder.append(", locations=");
		builder.append(locations);
		builder.append("]");
		return builder.toString();
	}
	
}