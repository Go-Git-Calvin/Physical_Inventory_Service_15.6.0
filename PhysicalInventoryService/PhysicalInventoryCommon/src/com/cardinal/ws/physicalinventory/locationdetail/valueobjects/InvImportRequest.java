/*********************************************************************
*
* $Workfile: InvImportRequest $
* Copyright 2013 Cardinal Health
*
*********************************************************************
*
* Revision History:
*
* $Log: $
*Modified By             Date              Clarify#         Description of the change
 Shruti Sinha			07/31/2015		  AD Factory 15.5   Import by CSN to Physical Inventory
*********************************************************************/
package com.cardinal.ws.physicalinventory.locationdetail.valueobjects;

/**
 * Request Object to hold InvImportRequest
 *
 * @author shruti.sinha
 * @version 1.0
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.cardinal.ws.physicalinventory.common.valueobjects.InvBaseRequest;

public class InvImportRequest extends InvBaseRequest  {
	private static final long serialVersionUID = 1L;

	private byte[]  importFile ;
	
	private String fileName;
	
	private String fileType;
	
	private boolean duplicateCheck;
	
	private String locationId;
	
	private HashMap<String,String> validCINMapToImport;
	// AD Factory 15.5 Release: Import By CSN to Physical Inventory -- START
	private boolean csnImport; // Added a flag for checking the File Import by CSN
	private HashMap<String, ArrayList<String>> csnCINMap;
	// AD Factory 15.5 Release: Import By CSN to Physical Inventory -- END
	/**
	 * @return the importFile
	 */
	public byte[] getImportFile() {
		return importFile;
	}

	/**
	 * @param importFile the importFile to set
	 */
	public void setImportFile(byte[] importFile) {
		this.importFile = importFile;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the fileType
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * @param fileType the fileType to set
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * @return the duplicateCheck
	 */
	public boolean isDuplicateCheck() {
		return duplicateCheck;
	}

	/**
	 * @param duplicateCheck the duplicateCheck to set
	 */
	public void setDuplicateCheck(boolean duplicateCheck) {
		this.duplicateCheck = duplicateCheck;
	}

	
	/**
	 * @param validCINMapToImport the validCINMapToImport to set
	 */
	public void setValidCINMapToImport(HashMap<String,String> validCINMapToImport) {
		this.validCINMapToImport = validCINMapToImport;
	}

	/**
	 * @return the validCINMapToImport
	 */
	public HashMap<String,String> getValidCINMapToImport() {
		return validCINMapToImport;
	}

	/**
	 * @param locationId the locationId to set
	 */
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	/**
	 * @return the locationId
	 */
	public String getLocationId() {
		return locationId;
	}

	

	public boolean isCsnImport() {
		return csnImport;
	}

	public void setCsnImport(boolean csnImport) {
		this.csnImport = csnImport;
	}

	/**
	 * @return the csnCINMap
	 */
	public HashMap<String, ArrayList<String>> getCsnCINMap() {
		return csnCINMap;
	}

	/**
	 * @param csnCINMap the csnCINMap to set
	 */
	public void setCsnCINMap(HashMap<String, ArrayList<String>> csnCINMap) {
		this.csnCINMap = csnCINMap;
	}

	@Override
	public String toString() {
		return "InvImportRequest [importFile=" + Arrays.toString(importFile)
				+ ", fileName=" + fileName + ", fileType=" + fileType
				+ ", duplicateCheck=" + duplicateCheck + ", locationId="
				+ locationId + ", validCINMapToImport=" + validCINMapToImport
				+ ", csnImport=" + csnImport + ", csnCINMap=" + csnCINMap + "]";
	}	
}