package com.cardinal.ws.physicalinventory.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the PROD_ID_TYPE database table.
 * 
 */
@Entity
@Table(name="PROD_ID_TYPE", schema ="OE_PI")
public class ProdIdType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PROD_ID_TYPE_CDE")
	private int prodIdTypeCde;

	@Column(name="PROD_ID_TYPE_DESC")
	private String prodIdTypeDesc;

	@Column(name="ROW_ADD_STP")
	private Timestamp rowAddStp;

	@Column(name="ROW_ADD_USER_ID")
	private String rowAddUserId;

	@Column(name="ROW_UPDATE_STP")
	private Timestamp rowUpdateStp;

	@Column(name="ROW_UPDATE_USER_ID")
	private String rowUpdateUserId;

	//bi-directional many-to-one association to LocationDetail
	@OneToMany(mappedBy="prodIdType",fetch=FetchType.LAZY)
	private List<LocationDetail> locationDetails;

    public ProdIdType() {
    }

	public int getProdIdTypeCde() {
		return this.prodIdTypeCde;
	}

	public void setProdIdTypeCde(int prodIdTypeCde) {
		this.prodIdTypeCde = prodIdTypeCde;
	}

	public String getProdIdTypeDesc() {
		return this.prodIdTypeDesc;
	}

	public void setProdIdTypeDesc(String prodIdTypeDesc) {
		this.prodIdTypeDesc = prodIdTypeDesc;
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

	public List<LocationDetail> getLocationDetails() {
		return this.locationDetails;
	}

	public void setLocationDetails(List<LocationDetail> locationDetails) {
		this.locationDetails = locationDetails;
	}
	
}