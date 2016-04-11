/**
 * 
 */
package com.cardinal.ws.physicalinventory.common.valueobjects;

import java.io.Serializable;

/**
 * @author vignesh.kandadi
 *
 */
public class InvCountType implements Serializable {
private static final long serialVersionUID = 1L;

public int countTypeCode;
public String countTypeDesc;
/**
 * @return the countTypeCode
 */
public int getCountTypeCode() {
	return countTypeCode;
}
/**
 * @param countTypeCode the countTypeCode to set
 */
public void setCountTypeCode(int countTypeCode) {
	this.countTypeCode = countTypeCode;
}
/**
 * @return the countTypeDesc
 */
public String getCountTypeDesc() {
	return countTypeDesc;
}
/**
 * @param countTypeDesc the countTypeDesc to set
 */
public void setCountTypeDesc(String countTypeDesc) {
	this.countTypeDesc = countTypeDesc;
}
/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Override
public String toString() {
	return "InvCountType [countTypeCode=" + countTypeCode + ", countTypeDesc="
			+ countTypeDesc + "]";
}



}
