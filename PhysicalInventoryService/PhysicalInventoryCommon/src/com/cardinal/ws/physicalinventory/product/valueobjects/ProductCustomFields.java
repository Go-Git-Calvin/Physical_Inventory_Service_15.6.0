/**
 * 
 */
package com.cardinal.ws.physicalinventory.product.valueobjects;

import java.io.Serializable;
import java.util.Date;

import com.cardinal.ws.physicalinventory.common.InventoryConstants;

/**
 * @author vignesh.kandadi
 *
 */
public class ProductCustomFields implements Serializable {
    private static final long serialVersionUID = 2483816622042090082L;
    public CodeIdDescription codeIdDescription1;
    public CodeIdDescription codeIdDescription2;
    public String csnVo;
    public String message;
    public String onFormulary;
    public String udf1Vo;
    public String udf2Vo;
    public Integer uoifVo;
    public String minDaysOfSupply;
    public String maxDaysOfSupply;
    public Long projectedUsage;
    public Date projectedUsageExpirationDate;

    public ProductCustomFields() {
        codeIdDescription1 = null;
        codeIdDescription2 = null;
        csnVo = null;
        message = null;
        onFormulary = null;
        udf1Vo = null;
        udf2Vo = null;
        uoifVo = null;
        minDaysOfSupply = null;
        maxDaysOfSupply = null;
        projectedUsage = null;
        projectedUsageExpirationDate = null;
    }

    /**
     * Getter for codeIdDescription1
     *
     * @return the codeIdDescription1
     */
    public CodeIdDescription getCodeIdDescription1() {
        return codeIdDescription1;
    }

    /**
     * Setter for codeIdDescription1
     *
     * @param codeIdDescription1 the codeIdDescription1 to set
     */
    public void setCodeIdDescription1(final CodeIdDescription codeIdDescription1) {
        this.codeIdDescription1 = codeIdDescription1;
    }

    /**
     * Getter for codeIdDescription2
     *
     * @return the codeIdDescription2
     */
    public CodeIdDescription getCodeIdDescription2() {
        return codeIdDescription2;
    }

    /**
     * Setter for codeIdDescription2
     *
     * @param codeIdDescription2 the codeIdDescription2 to set
     */
    public void setCodeIdDescription2(final CodeIdDescription codeIdDescription2) {
        this.codeIdDescription2 = codeIdDescription2;
    }

    /**
     * Getter for csnVo
     *
     * @return the csnVo
     */
    public String getCSN() {
        return csnVo;
    }

    /**
     * Setter for csnVo
     *
     * @param csn the csn to set
     */
    public void setCSN(final String csn) {
        csnVo = csn;
    }

    /**
     * Getter for message
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter for message
     *
     * @param message the message to set
     */
    public void setMessage(final String message) {
        this.message = message;
    }

    /**
     * Getter for onFormulary
     *
     * @return the onFormulary
     */
    public String getOnFormulary() {
        return onFormulary;
    }

    /**
     * Setter for onFormulary
     *
     * @param onFormulary the onFormulary to set
     */
    public void setOnFormulary(final String onFormulary) {
        this.onFormulary = onFormulary;
    }

    /**
     * Getter for udf1
     *
     * @return the udf1
     */
    public String getUDF1() {
        return udf1Vo;
    }

    /**
     * Setter for udf1
     *
     * @param udf1 the udf1 to set
     */
    public void setUDF1(final String udf1) {
        udf1Vo = udf1;
    }

    /**
     * Getter for udf2
     *
     * @return the udf2
     */
    public String getUDF2() {
        return udf2Vo;
    }

    /**
     * Setter for udf2
     *
     * @param udf2 the udf2 to set
     */
    public void setUDF2(final String udf2) {
        udf2Vo = udf2;
    }

    /**
     * Getter for UOIF
     *
     * @return the UOIF
     */
    public Integer getUOIF() {
        return uoifVo;
    }

    /**
     * Setter for UOIF
     *
     * @param uoif the UOIF to set
     */
    public void setUOIF(final Integer uoif) {
        uoifVo = uoif;
    }

    /**
     * Getter for minDaysOfSupply
     *
     * @return the minDaysOfSupply
     */
    public String getMinDaysOfSupply() {
        return minDaysOfSupply;
    }

    /**
     * Setter for minDaysOfSupply
     *
     * @param minDaysOfSupply the minDaysOfSupply to set
     */
    public void setMinDaysOfSupply(final String minDaysOfSupply) {
        this.minDaysOfSupply = minDaysOfSupply;
    }

    /**
     * Getter for maxDaysOfSupply
     *
     * @return the maxDaysOfSupply
     */
    public String getMaxDaysOfSupply() {
        return maxDaysOfSupply;
    }

    /**
     * Setter for maxDaysOfSupply
     *
     * @param maxDaysOfSupply the maxDaysOfSupply to set
     */
    public void setMaxDaysOfSupply(final String maxDaysOfSupply) {
        this.maxDaysOfSupply = maxDaysOfSupply;
    }

    /**
     * Getter for projectedUsage
     *
     * @return the projectedUsage
     */
    public Long getProjectedUsage() {
        return projectedUsage;
    }

    /**
     * Setter for projectedUsage
     *
     * @param projectedUsage the projectedUsage to set
     */
    public void setProjectedUsage(final Long projectedUsage) {
        this.projectedUsage = projectedUsage;
    }

    /**
     * Getter for projectedUsageExpirationDate
     *
     * @return the projectedUsageExpirationDate
     */
    public Date getProjectedUsageExpirationDate() {
        return projectedUsageExpirationDate;
    }

    /**
     * Setter for projectedUsageExpirationDate
     *
     * @param projectedUsageExpirationDate the Date to set
     */
    public void setProjectedUsageExpirationDate(final Date projectedUsageExpirationDate) {
        this.projectedUsageExpirationDate = projectedUsageExpirationDate;
    }

    /**
     * Answers a string containing a concise, human-readable description of the receiver.
     *
     * @return a printable representation for the receiver.
     */
    public String toString() {
        final StringBuffer stringBuffer = new StringBuffer(250);
        stringBuffer.append("ProductCustomFields < codeIdDescription1");
        stringBuffer.append(getCodeIdDescription1());
        stringBuffer.append(", codeIdDescription2 = ");
        stringBuffer.append(getCodeIdDescription2());
        stringBuffer.append(", CSN = ");
        stringBuffer.append(getCSN());
        stringBuffer.append(", message = ");
        stringBuffer.append(getMessage());
        stringBuffer.append(", onFormulary = ");
        stringBuffer.append(getOnFormulary());
        stringBuffer.append(", UDF1 = ");
        stringBuffer.append(getUDF1());
        stringBuffer.append(", UDF2 = ");
        stringBuffer.append(getUDF2());
        stringBuffer.append(", UOIF = ");
        stringBuffer.append(getUOIF());
        stringBuffer.append(", minDaysOfSupply = ");
        stringBuffer.append(getMinDaysOfSupply());
        stringBuffer.append(", maxDaysOfSupply = ");
        stringBuffer.append(getMaxDaysOfSupply());
        stringBuffer.append(", projectedUsage = ");
        stringBuffer.append(getProjectedUsage());
        stringBuffer.append(", projectedUsageExpirationDate = ");
        stringBuffer.append(getProjectedUsageExpirationDate());
        stringBuffer.append(InventoryConstants.RIGHT_ARROW);
        return stringBuffer.toString();
    }
}
