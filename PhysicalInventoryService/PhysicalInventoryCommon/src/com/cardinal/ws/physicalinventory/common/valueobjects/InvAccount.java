/*********************************************************************
*
* $Workfile: InvAccount.java $
* Copyright 2012 Cardinal Health
*
*********************************************************************
*
* Revision History:
*
* $Log: $
*
*********************************************************************/
package com.cardinal.ws.physicalinventory.common.valueobjects;

import java.io.Serializable;

/**
 * This class represents a Physical Inv account
 * @author brian.deffenbaugh01
 *
 */
public class InvAccount implements Serializable, Comparable<InvAccount>{
	private static final long serialVersionUID = 1L;
	private String acctName;
	private String distributionCenter;
	private String shipToCustomer;
	private boolean sortByAcct = false;
	private boolean sortByAcctName;

	/**
	 * This method compares InvAccount objects
	 */
	@Override
	public int compareTo(InvAccount rcvAcct) {		
		String shipToLoc1 = "0";
		String shipToLoc2= "0";
		String account1 = "0";
		String account2 = "0";
		String acctName1="0";
		String acctName2="0";
		int result = 0;
		
		if (sortByAcct) {
			if (sortByAcctName) {
				if (this.getAcctName()!=null && !this.getAcctName().trim().isEmpty()){
					acctName1 = this.getAcctName();
				}
				
				if (rcvAcct.getAcctName()!=null && !rcvAcct.getAcctName().trim().isEmpty()){
					acctName2 = rcvAcct.getAcctName();
				}
				
				result = acctName1.compareTo(acctName2);				
			}
			else {
				if (this.getDistributionCenter()!=null && !this.getDistributionCenter().trim().isEmpty()) {
					shipToLoc1 = this.getDistributionCenter();
				}
				
				if (rcvAcct.getDistributionCenter()!=null && !rcvAcct.getDistributionCenter().trim().isEmpty()) {
					shipToLoc2 = rcvAcct.getDistributionCenter();
				}

				Integer shipToLocInteger1 = Integer.parseInt(shipToLoc1);
				Integer shipToLocInteger2 = Integer.parseInt(shipToLoc2);
				result = shipToLocInteger1.compareTo(shipToLocInteger2);
				
				if (result==0) {   			
					if (this.getShipToCustomer()!= null && !this.getShipToCustomer().trim().isEmpty()) { // NOPMD by stephen.perry01 on 5/24/13 4:56 PM
						account1 = this.getShipToCustomer();
					}
					
					if (rcvAcct.getDistributionCenter()!= null && !rcvAcct.getDistributionCenter().trim().isEmpty()) { // NOPMD by stephen.perry01 on 5/24/13 4:56 PM
						account2 = rcvAcct.getShipToCustomer();
					}
					
					Integer acctInteger1 = Integer.parseInt(account1);
					Integer acctInteger2 = Integer.parseInt(account2);
					result = acctInteger1.compareTo(acctInteger2);
				}
			}	
		}
	
		return result;
	}

	/**
	 * @return the acctName
	 */
	public String getAcctName() {
		return acctName;
	}

	/**
	 * @return the distributionCenter
	 */
	public String getDistributionCenter() {
		return distributionCenter;
	}

	/**
	 * @return the shipToCustomer
	 */
	public String getShipToCustomer() {
		return shipToCustomer;
	}

	/**
	 * @return the sortByAcct
	 */
	public boolean isSortByAcct() {
		return sortByAcct;
	}
	
	/**
	 * @return the sortByAcctName
	 */
	public boolean isSortByAcctName() {
		return sortByAcctName;
	}

	/**
	 * @param acctName the acctName to set
	 */
	public void setAcctName(String acctName) {
		this.acctName = acctName;
	}

	/**
	 * @param distributionCenter the distributionCenter to set
	 */
	public void setDistributionCenter(String distributionCenter) {
		this.distributionCenter = distributionCenter;
	}

	/**
	 * @param shipToCustomer the shipToCustomer to set
	 */
	public void setShipToCustomer(String shipToCustomer) {
		this.shipToCustomer = shipToCustomer;
	}

	/**
	 * @param sortByAcct the sortByAcct to set
	 */
	public void setSortByAcct(boolean sortByAcct) {
		this.sortByAcct = sortByAcct;
	}

	/**
	 * @param sortByAcctName the sortByAcctName to set
	 */
	public void setSortByAcctName(boolean sortByAcctName) {
		this.sortByAcctName = sortByAcctName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RcvAccount [acctName=");
		builder.append(acctName);
		builder.append(", shipToCustomer=");
		builder.append(shipToCustomer);
		builder.append(", distributionCenter=");
		builder.append(distributionCenter);
		builder.append("]");
		return builder.toString();
	}
}