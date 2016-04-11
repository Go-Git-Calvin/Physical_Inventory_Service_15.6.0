package com.cardinal.ws.physicalinventory.product.valueobjects;

import java.io.Serializable;

import com.cardinal.ws.physicalinventory.common.InventoryConstants;

public class CodeIdDescription implements Serializable {
    private static final long serialVersionUID = 8004292651433452544L;
    public String code;
    public String id;
    public String description;

    /**
     *Default Constructor
     */
    public CodeIdDescription() {
        code = null;
        id = null;
        description = null;
    }

    /**
     * Getter for code
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Setter for code
     *
     * @param code the code to set
     */
    public void setCode(final String code) {
        this.code = code;
    }

    /**
     * Getter for id
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Setter for id
     *
     * @param id the id to set
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Getter for description
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for description
     *
     * @param description the description to set
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * This method returns a string representation of the CodeIdDescription object.
     *
     * @return A string representation of the CodeIdDescription object.
     */
    public String toString() {
        final StringBuffer stringBuffer = new StringBuffer(110);
        stringBuffer.append("\nCodeIdDescription:-code:<");
        stringBuffer.append(getCode());
        stringBuffer.append(">,id:<");
        stringBuffer.append(getId());
        stringBuffer.append(">, description:<");
        stringBuffer.append(getDescription());
        stringBuffer.append(InventoryConstants.RIGHT_ARROW);

        return stringBuffer.toString();
    }
}
