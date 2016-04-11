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
 * The persistent class for the COST_TYPE database table.
 * 
 */
@Entity
@Table(name="COST_TYPE", schema ="OE_PI")
public class CostType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COST_TYPE_CDE")
	private int costTypeCde;

	@Column(name="COST_TYPE_DESC")
	private String costTypeDesc;

	@Column(name="ROW_ADD_STP")
	private Timestamp rowAddStp;

	@Column(name="ROW_ADD_USER_ID")
	private String rowAddUserId;

	@Column(name="ROW_UPDATE_STP")
	private Timestamp rowUpdateStp;

	@Column(name="ROW_UPDATE_USER_ID")
	private String rowUpdateUserId;

	//bi-directional many-to-one association to Location
	@OneToMany(mappedBy="costType", fetch=FetchType.LAZY)
	private List<Location> locations;

    public CostType() {
    }

	public int getCostTypeCde() {
		return this.costTypeCde;
	}

	public void setCostTypeCde(int costTypeCde) {
		this.costTypeCde = costTypeCde;
	}

	public String getCostTypeDesc() {
		return this.costTypeDesc;
	}

	public void setCostTypeDesc(String costTypeDesc) {
		this.costTypeDesc = costTypeDesc;
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
	
}