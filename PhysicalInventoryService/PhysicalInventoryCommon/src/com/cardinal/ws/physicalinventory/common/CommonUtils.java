package com.cardinal.ws.physicalinventory.common;

import java.util.Properties;
import java.util.logging.Logger;

import com.cardinal.webordering.common.errorhandling.SystemException;
import com.cardinal.webordering.common.logging.MessageLoggerHelper;
import com.cardinal.ws.common.util.PropertyManager;

public final class CommonUtils {
    private static final String CLASS_NAME = CommonUtils.class.getName();
    private static final Logger LOGGER = MessageLoggerHelper.getLogger(CLASS_NAME);

    private CommonUtils() {  // NOPMD by nagaraj.bhavi on 03/18/13.
    }

    /**
     * The findServiceProperty method is a common method which loads property file and
     * return  the required property value against the property key. based on the passed
     * paramemters.
     *
     * @param serviceName as String.
     * @param propFileName as String.
     * @param findProperty as String.
     *
     * @return propValue as String.
     */
    public static String findServiceProperty(final String serviceName, final String propFileName,
                                             final String findProperty) {
        final String METHOD_NAME = "findServiceProperty";
        if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
            LOGGER.entering(CLASS_NAME, METHOD_NAME);
        }

        String propValue = "";
        try {
            //get instance of propertyManager 
            PropertyManager propertyManager = PropertyManager.getInstance();

            //pass serviceName and propertyFile Name as parameters to get all properties for JPMPaymentService.
            Properties properties = propertyManager.getProperties(serviceName, propFileName);
            //load the specific property.
            propValue = properties.getProperty(findProperty);
            LOGGER.info(MessageLoggerHelper.buildMessage(CLASS_NAME, METHOD_NAME,
                                                         "Property Value : ", propValue));
        }
        catch (SystemException se) {
            final StringBuffer message = new StringBuffer(40);
            message.append("Invalid Property : ");
            message.append(propValue);
            LOGGER.warning(MessageLoggerHelper.buildErrorMessage(CLASS_NAME, METHOD_NAME,
                                                                 "SystemException",
                                                                 message.toString(), null));
            propValue = null;
        }

        if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
            LOGGER.exiting(CLASS_NAME, METHOD_NAME);
        }

        return propValue;
    }
}
