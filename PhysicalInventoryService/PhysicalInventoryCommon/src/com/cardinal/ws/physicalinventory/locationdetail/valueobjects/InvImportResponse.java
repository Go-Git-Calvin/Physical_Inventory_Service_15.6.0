/**
 * 
 */
package com.cardinal.ws.physicalinventory.locationdetail.valueobjects;

import com.cardinal.ws.physicalinventory.common.valueobjects.InvBaseResponse;

/**
 * @author brian.deffenbaugh01
 *
 */
public class InvImportResponse extends InvBaseResponse {
	private static final long serialVersionUID = 1L;
	
	private int noOfLinesProcessed;
	private int noOfLinesPassed;
	private int noOfLinesWithException;
	/**
	 * @return the noOfLinesProcessed
	 */
	public int getNoOfLinesProcessed() {
		return noOfLinesProcessed;
	}
	/**
	 * @param noOfLinesProcessed the noOfLinesProcessed to set
	 */
	public void setNoOfLinesProcessed(int noOfLinesProcessed) {
		this.noOfLinesProcessed = noOfLinesProcessed;
	}
	/**
	 * @return the noOfLinesPassed
	 */
	public int getNoOfLinesPassed() {
		return noOfLinesPassed;
	}
	/**
	 * @param noOfLinesPassed the noOfLinesPassed to set
	 */
	public void setNoOfLinesPassed(int noOfLinesPassed) {
		this.noOfLinesPassed = noOfLinesPassed;
	}
	/**
	 * @return the noOfLinesWithException
	 */
	public int getNoOfLinesWithException() {
		return noOfLinesWithException;
	}
	/**
	 * @param noOfLinesWithException the noOfLinesWithException to set
	 */
	public void setNoOfLinesWithException(int noOfLinesWithException) {
		this.noOfLinesWithException = noOfLinesWithException;
	}
	
}