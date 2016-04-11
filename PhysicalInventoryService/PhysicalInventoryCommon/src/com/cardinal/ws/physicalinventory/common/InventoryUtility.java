/*********************************************************************
 *
 * $Workfile: BaseDataMapper.java $
 * Copyright 2012 Cardinal Health
 *
 *********************************************************************
 *
 * Revision History:
 *
 * $Log: /com/cardinal/ws/getph/mapper/BaseDataMapper.java $
 *Modified By               Date                defect ID#        Description of the change.

 * Rohit Bhat            09/22/2014            DEC Service Migration    Added new utility method toXMLGregorianCalendar
 *********************************************************************/

package com.cardinal.ws.physicalinventory.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.cardinal.webordering.common.logging.ApplicationName;
import com.cardinal.webordering.common.logging.MessageLoggerHelper;


/**
 * This class contains utility methods used by the Physical Inv service
 *
 * @author rohit.bhat
 */
public final class InventoryUtility {
    private static final String CLASS_NAME = InventoryUtility.class.getName();
    private static final String CONFIG_DIRECTORY = System.getProperty(InventoryConstants.APPCONFIG);
    private static final Logger LOGGER =
        MessageLoggerHelper.getLogger(InventoryUtility.class.getCanonicalName());
    private static final char UNIX_SEPARATOR = '/';
    private static final char WINDOWS_SEPARATOR = '\\';

    private InventoryUtility() {}
    
    /**
     * This method will convert a date from one timezone to another.
     *
     * @param inputDate - The date to convert.
     * @param fromTZ - The timezone to convert from.
     * @param toTZ - The timezone to convert to.
     *
     * @return A new Date representing the original date converted to the new timezone.
     */
    public static Date dateTimeZoneConverter(Date inputDate, String fromTZ, String toTZ) {
        Calendar fromCal = Calendar.getInstance();
        Calendar toCal = Calendar.getInstance();
        fromCal.setTimeInMillis(inputDate.getTime());
        toCal.setTimeInMillis(inputDate.getTime());

        TimeZone fromZone = TimeZone.getTimeZone(fromTZ);
        TimeZone toZone = TimeZone.getTimeZone(toTZ);

        fromCal.setTimeZone(fromZone);
        toCal.setTimeZone(toZone);

        int fromOffset = fromZone.getRawOffset();
        int toOffset = toZone.getRawOffset();

        if (fromZone.inDaylightTime(new Date())) {
            fromOffset = fromOffset + fromZone.getDSTSavings();
        }

        if (toZone.inDaylightTime(new Date())) {
            toOffset = toOffset + toZone.getDSTSavings();
        }

        int fromOffsetHrs = fromOffset / 1000 / 60 / 60;
        int fromOffsetMins = (fromOffset / 1000 / 60) % 60;

        int toOffsetHrs = toOffset / 1000 / 60 / 60;
        int toOffsetMins = (toOffset / 1000 / 60) % 60;

        toCal.add(Calendar.HOUR_OF_DAY, (-(fromOffsetHrs - toOffsetHrs)));
        toCal.add(Calendar.MINUTE, (-(fromOffsetMins - toOffsetMins)));

        return toCal.getTime();
    }

    /**
     * Formats the given string (which should contain up to 11 numeric characters) into the
     * standard UPC format.
     *
     * @param upc The string to be converted
     *
     * @return The formatted UPC or an empty string if UPC is null
     *
     * @throws NumberFormatException if UPC cannot be parsed as a long
     */
    public static String applyUPCFormat(final String upc) throws NumberFormatException {
        String formatted = "";
        String upcStr = "";

        if (upc != null) {
            if (upc.length() == 10) {
                upcStr = upc.replaceAll("[ -/]*[:-~]*", ""); //added the replaceAll method for defect 4105
                                                             // This will throw a NumberFormatException if the NDC is not numeric

                Long.parseLong(upcStr);
                // Insert the '-' where appropriate
                formatted = upcStr.substring(0, 5) + "-";
                formatted += upcStr.substring(5); // NOPMD by vandana.sharma on 3/2/10 12:43 PM
            }
            else if (upc.length() == 12) {
                upcStr = upc.replaceAll("[ -/]*[:-~]*", ""); //added the replaceAll method for defect 4105
                                                             // This will throw a NumberFormatException if the NDC is not numeric

                Long.parseLong(upcStr);

                // Insert the '-' where appropriate
                formatted = upcStr.substring(0, 6) + "-";
                formatted += upcStr.substring(6); // NOPMD by vandana.sharma on 3/2/10 12:44 PM
            }
            else {
                String formattedUPC = padLeft(upc, 12, '0');
                formattedUPC = formattedUPC.replaceAll("[ -/]*[:-~]*", ""); //added the replaceAll method for defect 4105
                                                                            // This will throw a NumberFormatException if the NDC is not numeric

                Long.parseLong(formattedUPC);
                // Insert the '-' where appropriate
                formatted = formattedUPC.substring(0, 6) + "-";
                formatted += formattedUPC.substring(6); // NOPMD by vandana.sharma on 3/2/10 12:45 PM
            }
        }

        return formatted.trim();
    }

    /**
     * This method pads the left of the input string with a character to n length.
     *
     * @param stringToPad String to be padded
     * @param totalLength The total length of final string.
     * @param padChar Fill character that will be used to pad.
     *
     * @return The left padded string
     */
    public static String padLeft(final String stringToPad, final int totalLength, final char padChar) {
        final String methodName = "padLeft";
        if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
            LOGGER.entering(CLASS_NAME, methodName);
        }

        String paddedString = stringToPad;

        for (int i = stringToPad.length(); i < totalLength; ++i) {
            paddedString = padChar + paddedString; // NOPMD by vandana.sharma on 3/2/10 12:44 PM
        }

        if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
            LOGGER.exiting(CLASS_NAME, methodName);
        }

        return paddedString;
    }

    /**
     * Find the Fields in the class or the super class
     *
     * @param clazz
     * @param fieldName
     *
     * @return field whose name matches fieldName parameter
     */
    @SuppressWarnings("unchecked")
    public static Field getField(Class clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        }
        catch (NoSuchFieldException e) {
            Class superClass = clazz.getSuperclass();
            if ((superClass == null) || (superClass == Object.class)) {
                return null;
            }
            else {
                return getField(superClass, fieldName);
            }
        }
    }

    /**
     * Gets the name minus the path from a full filename.  This method will handle a file
     * in either Unix or Windows format.  The text after the last forward or backslash is
     * returned.
     *
     * @param filename the filename to query, null returns null
     *
     * @return the name of the file without the path, or an empty string if none exists
     */
    public static String getFileName(String filename) {
        if (filename == null) {
            return null;
        }

        int index = indexOfLastSeparator(filename);
        return filename.substring(index + 1);
    }

    /**
     * Returns the index of the last directory separator character.<p>This method will
     * handle a file in either Unix or Windows format. The position of the last forward or
     * backslash is returned.</p>
     *  <p>The output will be the same irrespective of the machine that the code is running
     * on.</p>
     *
     * @param filename the filename to find the last path separator in, null returns -1
     *
     * @return the index of the last separator character, or -1 if there is no such character
     */
    public static int indexOfLastSeparator(String filename) {
        if (filename == null) {
            return -1;
        }

        int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);
        int lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR);
        return Math.max(lastUnixPos, lastWindowsPos);
    }

    /**
     * This method retrieves a value from the service property file
     *
     * @author //TODO:
     *
     * @param serviceName the name of the service (folder name)
     * @param fileName the filename to retrieve the value from
     * @param key the key in the file for the value
     *
     * @return the value
     */
    public static String getValue(final String serviceName, final String fileName, final String key) {
        Properties result = new Properties();
        String resultValue = null;
        if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
            LOGGER.finer("APPCONFIG DIR:" + System.getProperty(InventoryConstants.APPCONFIG));
        }

        try {
            result.load(new FileInputStream(CONFIG_DIRECTORY + File.separator + serviceName
                                            + File.separator + fileName));
            resultValue = result.getProperty(key);
        }
        catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE,
                       "Unable to read property file " + fileName + " for  service " + serviceName
                       + " for key " + key, e);
        }
        catch (IOException e) {
            LOGGER.log(Level.SEVERE,
                       "Unable to read property file " + fileName + " for  service " + serviceName
                       + " for key " + key, e);
        }

        return resultValue;
    }

    /**
     * This method generates a displayable NDC/UPC number given the product type, ndc, and upc
     *
     * @author //TODO:
     *
     * @param productType the type of product - Rx, C2, C3, etc
     * @param ndc the ndc number
     * @param upc the upc number
     *
     * @return the displayable NDC/UPC number
     */
    public static String deriveDisplayNDCUPC(final String productType, final String ndc,
                                             final String upc) {
        String result = "";

        if (!(InventoryConstants.PRODUCT_TYPE_RX.equalsIgnoreCase(productType)
                || (InventoryConstants.PRODUCT_TYPE_C2.equalsIgnoreCase(productType))
                || (InventoryConstants.PRODUCT_TYPE_C3.equalsIgnoreCase(productType))
                || (InventoryConstants.PRODUCT_TYPE_C4.equalsIgnoreCase(productType))
                || (InventoryConstants.PRODUCT_TYPE_C5.equals(productType)))) {
            try {
                if (upc != null) {
                    result = applyUPCFormat(upc);
                }
            }
            catch (NumberFormatException nfe) {
                result = upc;
            }
        }
        else if (ndc != null) {
            try {
                result = applyNDCFormat(ndc);
            }
            catch (NumberFormatException nfe) {
                result = ndc;
            }
        }

        return result;
    }

    /**
     * This method rounds the input double to two decimal places
     *
     * @author //TODO:
     *
     * @param d the double to round
     *
     * @return returns the double rounded to two decimal places in String format
     */
    public static String roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("$0.00");
        return twoDForm.format(d);
    }

    /**
     * This method rounds the input double to four decimal places
     *
     * @author //TODO:
     *
     * @param d the double value to round
     *
     * @return returns the double rounded to four decimal places in String format
     */
    public static String roundFourDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("$0.0000");
        return twoDForm.format(d);
    }

    /**
     * Creates a cloned object of the source object passed by setting only the fields that
     * are passed in as columnNames ,this method presently cannot handle objects that don't have
     * non argument constructor and also cannot set  the fields of a associated object of a
     * associated object in the source object passed in
     *
     * @param source
     * @param columnNames
     *
     * @return cloned object desired fields set
     */
    public static Object setValuesThroughReflection(Object source, List<String> columnNames) {
        final String methodName = "setValuesThroughReflection";

        if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
            LOGGER.entering(CLASS_NAME, methodName);
            LOGGER.finer("Columns whose values have to be set through reflection"
                         + InventoryConstants.PIPE_CR_NL + columnNames);
        }

        Class<?extends Object> class1 = source.getClass();
        Object cloned = null;
        List<String> allcol = new ArrayList<String>();
        allcol.addAll(columnNames);

        List<String> removeCol = new ArrayList<String>();
        Field fieldTOSet = null;
        Field composedField1 = null;
        StringTokenizer stringTokenizer = null;
        try {
            cloned = class1.newInstance();
        }
        catch (IllegalAccessException e) {
            LOGGER.log(Level.SEVERE, "Error setting values through reflection", e);
            return null;
        }
        catch (InstantiationException e) {
            LOGGER.log(Level.SEVERE, "Error setting values through reflection", e);
            return null;
        }

        for (String string : allcol) {
            try {
                if ((cloned != null) && (string.indexOf(".") == -1)) {
                    fieldTOSet = getField(cloned.getClass(), string);
                    if (fieldTOSet != null) {
                        fieldTOSet.setAccessible(true);
                        if(MessageLoggerHelper.isTraceEnabled(LOGGER)) {
                        	
                        	LOGGER.fine("Column Name : "+string);
                            LOGGER.fine("Value : "+fieldTOSet.get(source));
                        }
                        fieldTOSet.set(cloned, fieldTOSet.get(source));
                        removeCol.add(string);
                    }
                }

                if ((cloned != null) && (string.indexOf(".") != -1)) {
                    stringTokenizer = new StringTokenizer(string, ".");

                    if (stringTokenizer.countTokens() == 2) {
                        composedField1 = getField(source.getClass(), stringTokenizer.nextToken());
                        if (composedField1 != null) { //NOPMD Irfan.Mohammed  04/13/2012
                            composedField1.setAccessible(true);
                            if (composedField1.get(source) != null) {
                                fieldTOSet = getField(composedField1.getType(),
                                                      stringTokenizer.nextToken());
                                if (fieldTOSet != null) {
                                    fieldTOSet.setAccessible(true);
                                    if (composedField1.get(cloned) == null) {
                                        composedField1.set(cloned,
                                                           composedField1.getType().newInstance());
                                    }
                                    fieldTOSet.set(composedField1.get(cloned),
                                                   fieldTOSet.get(composedField1.get(source)));
                                    removeCol.add(string);
                                }
                            }
                        }
                    }
                }
            }
            catch (InstantiationException e) {
                LOGGER.log(Level.SEVERE, "Error setting values through reflection", e);
            }
            catch (SecurityException e) {
                LOGGER.log(Level.SEVERE, "Error setting values through reflection", e);
            }
            catch (IllegalArgumentException e) {
                LOGGER.log(Level.SEVERE, "Error setting values through reflection", e);
            }
            catch (IllegalAccessException e) {
                LOGGER.log(Level.SEVERE, "Error setting values through reflection", e);
            }
        }

        allcol.removeAll(removeCol);
        if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
            LOGGER.finer("Columns whose values were not set through reflection--------> "
                         + InventoryConstants.PIPE_CR_NL + allcol);
        }

        return cloned;
    }

    /**
     * Utility method to format input data as required by the FulfillmentSystem  Formats
     * the string to the given decimal format.
     *
     * @param value value to format
     * @param fmt format return
     *
     * @return the value formatted
     *
     * @throws ParseException
     */
    private final static String toLeadingZeros(final String value, final String fmt) // NOPMD by stephen.perry01 on 5/24/13 4:41 PM  Keeping unused method
                                        throws ParseException {
        String retValue = value;
        if (retValue == null) {
            retValue = "0";
        }

        NumberFormat nf = new DecimalFormat(fmt);
        Number nbr = nf.parse(retValue);
        retValue = nf.format(nbr.longValue());

        return retValue;
    }

    /**
     * Logs Alert Messages for Tivoli monitoring
     *
     * @param t
     *
     * @return //TODO: DOCUMENT ME!
     */
    public static Map<String, String> logAlertMessage(Throwable t) {
        Map<String, String> errorMap = InventoryExceptionMapper.getAlertMessage(t);
        if (errorMap != null) {
            MessageLoggerHelper.alert(ApplicationName.CUSTOM,
                                      errorMap.get(InventoryConstants.ERRORCODE),
                                      errorMap.get(InventoryConstants.ERRORALERTTYPE),
                                      errorMap.get(InventoryConstants.ERRORALERTLEVEL),
                                      errorMap.get(InventoryConstants.ERRORMESSAGE));
        }

        return errorMap;
    }

    /**
     * Checks if the given string is numeric and not null. An empty string will not be
     * considered numeric.
     *
     * @param string A string to check
     *
     * @return 'true' if the given string consists of digits only; false otherwise.
     */
    public static boolean isNumericAndNotNull(final String string) {
        if (string == null) {
            return false;
        }

        return string.matches("\\d+");
    }

    /**
     * Checks if the given double is is zero or not If cost zero then return
     *
     * @param cost A string to check
     *
     * @return 'true' if the given string consists of digits only; false otherwise.
     */
    public static boolean isCostZero(final double cost) {
        if ((cost == 0) || (cost == 0.0) || (cost == 0.00) || (cost == 0.000) || (cost == 0.0000)) {
            return true;
        }

        return false;
    }

    /**
     * Checks if the given double is is zero or not If value is null or  zero then return true otherwise false
     *
     * @param Value a Double
     * 
     * @return 'true' if the given string consists of digits only; false otherwise.
     */
    public static boolean isDoubleZero(final Double value) {
        if ((value==null) || (value.doubleValue() == 0) || (value.doubleValue() == 0.0) || (value.doubleValue() == 0.00) || (value.doubleValue() == 0.000) || (value.doubleValue() == 0.0000)) {
            return true;
        }

        return false;
    }

    /**
     * Checks if the given double is is zero or not If cost zero then return
     *
     * @param cost A string to check
     *
     * @return 'true' if the given string consists of digits only; false otherwise.
     */
    public static boolean isCostZero(final BigDecimal cost) {
        if ((cost == null) || (cost.compareTo(BigDecimal.ZERO) <= 0)) {
            return true;
        }

        return false;
    }

    /**
     * Validate the pass value should be null or blank
     *
     * @param value
     *
     * @return
     */
    public static boolean isNotNullAndEmpty(final String value) {
        boolean result = true;

        if ((value == null) || "".equals(value.trim())) {
            result = false;
        }

        return result;
    }

    /**
     * Validate the pass value should not be null or blank
     *
     * @param value
     *
     * @return
     */
    public static boolean isNullOrEmpty(final String value) {
        boolean result = false;

        if ((value == null) || "".equals(value.trim())) {
            result = true;
        }

        return result;
    }

    /**
     * Validat the pass value should not be null
     *
     * @param value
     *
     * @return
     */
    public static boolean isNull(final String value) {
        boolean result = false;

        if ((value == null) || "".equals(value.trim())) {
            result = true;
        }

        return result;
    }

    /**
     * Formats the given string (which should contain up to 11 numeric characters) into the
     * standard NDC format.
     *
     * @param ndc The string to be converted
     *
     * @return The formatted NDC or an empty string if NDC is null
     *
     * @throws NumberFormatException if ndc cannot be parsed as a long
     */
    public static String applyNDCFormat(String ndc) throws NumberFormatException {
        final String methodName = "applyNDCFormat";
        if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
            LOGGER.entering(CLASS_NAME, methodName);
        }

        String formatted = "";
        String ndc1 = "";

        //To avoid exception for bad data in NDC
        if (ndc != null) {
            // This will throw a NumberFormatException if the NDC is not numeric
            ndc1 = ndc.replaceAll("[ -/]*[:-~]*", "");
            Long.parseLong(ndc1);

            // pad with zeros
            final String myNdc = padLeft(ndc1, 11, '0');
            // Then insert the '-' where appropriate
            formatted = myNdc.substring(0, 5) + "-";
            formatted += (myNdc.substring(5, 9) + "-"); // NOPMD by vandana.sharma on 3/2/10 12:42 PM	        
            formatted += myNdc.substring(9); // NOPMD by vandana.sharma on 3/2/10 12:42 PM	        
        }

        if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
            LOGGER.exiting(CLASS_NAME, methodName);
        }

        return formatted.trim();
    }

    /**
     * This method is used for deriving the UOI cost value.
     *
     * @param invoiceCost BigDecimal
     * @param uoif BigDecimal
     *
     * @return uoiCost
     */
    public static double deriveUOICost(final double invoiceCost, Double uoif) {
        NumberFormat formatter = new DecimalFormat("#0.0000");
        double uoiCost = 0.0000;
//        LOGGER.info("invoiceCost: " + invoiceCost);
        // UOIF cannot be zero, check
        if(uoif!=null && uoif.doubleValue() > 0) {
        	uoiCost = invoiceCost / uoif.doubleValue();
        } else {
        	// if uoif cost is <= 0, use invoiceCost as uoiCost
        	uoiCost = invoiceCost;
        }
//        LOGGER.info("uoiCost: " + uoiCost);
        return Double.parseDouble(formatter.format(uoiCost));
    }

    /**
     * This method check is the given date is Data base default date or not. if the Date
     * year exits "1900" then it treated  as Database default date and returns false, other wise
     * return true.
     *
     * @param dateObj
     *
     * @return
     */
    public static boolean isNotDefaultDate(Date dateObj) {
        if ((dateObj == null) || (dateObj.toString().indexOf("1900") != -1)) {
            return false;
        }

        // 02/01/1900
        return true;
    }

    /**
     * Validat the pass value should not be null or blank or null value
     *
     * @param value
     *
     * @return
     */
    public static boolean isNullOrEmptyWithNullValue(final Integer value) {
        boolean result = false;

        if ((value == null) || "".equals(value) || "null".equalsIgnoreCase(value.toString())) {
            result = true;
        }

        return result;
    }

    /**
     * This method takes String as request first checks is the given string null or empty.
     * If its not null and empty then it will remove all the blank spaces when there is more than
     * 1 in given text
     *
     * @param str : its String request
     *
     * @return str: it returns as String
     */
    public static String removeBlankSpaces(final String str) {
        String rtnStr = str;
        if (isNotNullAndEmpty(str)) {
            rtnStr = str.trim();
            rtnStr = rtnStr.replaceAll(InventoryConstants.BLANK_SPACE_REGEX, " ");
        }

        return rtnStr;
    }
    
    /**
     * 
     * @param floatVal
     * @return
     */
    public static long convertFloatToLong(float floatVal){
    	
    	return (long)floatVal;
    }
    
    /**
     *   This method converts a Date obj from YYYYMMDD to yyyy-MM-dd format
     *   
     * @param date the date to convert
     * @return returns a Date object in the correct format
     */
    public static Date convertDateFromYYYYMMDD(final Date date){
    	final String methodName = "formatDate";

    	if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
    		LOGGER.entering(CLASS_NAME, methodName);
    		LOGGER.fine("date>>>" +date );
    	}
    	Date formatDate=null;
    	try {

    		if(date!=null){

    			DateFormat formatter ; 
    			Calendar cal=Calendar.getInstance();
    			formatter = new SimpleDateFormat("yyyy-MM-dd");

    			//date = (Date);



    			cal.setTime(formatter.parse(date.toString()));
    			formatDate=cal.getTime();
    			if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
    				LOGGER.fine("Calender>>>>>" +cal );
    				LOGGER.fine("cal.getTime()>>>> " +formatDate);
    			}

    		}
    	} catch (ParseException e) {
    		LOGGER.severe(CLASS_NAME + ":" + methodName + ": ParseException caught" + e);
    	}
    	if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
    		LOGGER.exiting(CLASS_NAME, methodName);
    	}
    	return formatDate;

    }
    /**
     *   This method converts date in String format from YYYYMMDD to MM/dd/yyyy
     *   
     * @param date the date to be converted
     * @return returns a String of the date in MM/dd/yyyy format
     */
    public static String convertDateFromYYYYMMDD(final String date){
    	final String methodName = "convertDateFromYYYYMMDD";

    	if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
    		LOGGER.entering(CLASS_NAME, methodName);
    		LOGGER.info("date>>>" +date );
    	}
    	Date formatDate=null;
    	String returnDate = "";
    	try {
    		if(date!=null && !date.isEmpty()){
    			DateFormat formatter ; 
    			Calendar cal=Calendar.getInstance();
    			formatter = new SimpleDateFormat("yyyyMMdd");
    			cal.setTime(formatter.parse(date));
    			formatDate=cal.getTime();
    			if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
    				LOGGER.fine("Calender>>>>>" +cal );
    				LOGGER.fine("cal.getTime()>>>> " +formatDate);
    			}
    			DateFormat formatter2 = new SimpleDateFormat("MM/dd/yyyy");
    			returnDate = formatter2.format(formatDate);
    		}
    	} catch (ParseException e) {
    		// TODO Auto-generated catch block
    		LOGGER.severe(CLASS_NAME + ":" + methodName + ": ParseException caught" + e);
    	}
    	if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
    		LOGGER.exiting(CLASS_NAME, methodName);
    	}
    	return returnDate;

    }
    
    /**
     * This method creates a date in the year 2100 (or so).  Used for sorting of LastPurchPaid 
     * cost type details
     * 
     * @return returns a Date far into the future
     */
    public static Date createMaxDate() {
    	Calendar cal = Calendar.getInstance();
		cal.set(2100, 12, 31);
		Date maxDate = cal.getTime();
		
		return maxDate;
    }
    
    /**
     * This method returns true if the import line is empty. i.e. [,,]
     * @param productData the string array of an import line [prodId,countType,Qty]
     * @return
     */
    public static boolean isEmptyImportLine(String productData[]) {
    	boolean isEmpty = true;
    	for(String data : productData) {
    		if(data != null && !data.trim().isEmpty()) {
    			isEmpty = false;
    		}
    	}
    	return isEmpty;
    }
    
   
    /**
     * Formats a double to two decimal places.
     *
     * @param price The price to format.
     * @return The formatted decimal.
     */
    public static double formatCost(final double price) {
    	final NumberFormat formatter = new DecimalFormat("#0.00");
        final String tempFormatprice = formatter.format(price);
        final double convertedPrice = Double.parseDouble(tempFormatprice);       
        
        return convertedPrice;
    }
    
    /**
     * This method formats a quantity to import file business rules.
     * 
     * @param importQty
     * @return
     */
    public static double formatImportQty(String importQty) {
    	double qty = 0.0;
    	
    	String[] splitQty = importQty.split("\\.");
		if(splitQty.length == 2) {
			String wholeNum = splitQty[0];
			String wholeQty = wholeNum;
			if(wholeNum.length() > 4) {
				wholeQty = wholeNum.substring((wholeNum.length() - 4), (wholeNum.length()));
			}
			
			String decNum = splitQty[1];
			String decQty = decNum;
			if(decNum.length() > 2) {
				decQty = decNum.substring(0, 2);
			}
			qty = Double.valueOf(wholeQty + "." + decQty);
		} else if(splitQty.length == 1) {
			String wholeNum = splitQty[0];
			String wholeQty = wholeNum;
			if(wholeNum.length() > 4) {
				wholeQty = wholeNum.substring((wholeNum.length() - 4), (wholeNum.length()));
			}
			qty = Double.valueOf(wholeQty);
		}
		
    	return qty;
    }
    
    /**
	 * This method calculate the rebate indicator.
	 * 
	 * @Param userContext : userContext Object
	 * @Param rebateType : rebateType Object
	 * @Param contractGrpAffNum : contractGrpAffNum Object
	 * 
	 */
	public static String getRebateIndicator(
			final List<String> getContractGrpAffiliationList,
			final String getRebatePriceEnabled, final String rebateType,
			final String contractGrpAffNum, double afterRebateCost, String isRebatePrgmAcct) {

		final String METHOD_NAME = "getRebateIndicator"; 
		String rebateIndicator = ""; 
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);
		}
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.fine("RebateType " + rebateType);
		}
		if(null != rebateType) {
			
			if(!InventoryConstants.REBATE_LEADER.equalsIgnoreCase(rebateType) 
					&& null != isRebatePrgmAcct
					&& InventoryConstants.IS_A_REBATE_PRGM_ACCT.equalsIgnoreCase(isRebatePrgmAcct)) {

				// Rebate Customer Contract Price Override changes - Start.
				if (afterRebateCost > InventoryConstants.ZERO_IN_DOUBLE) {

					rebateIndicator = InventoryConstants.REBATE_GENERIC;

				} else {

					if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {

						LOGGER.fine("Setting the Rebate Indicator as blank.");
					}
				}
				// Rebate Customer Contract Price Override changes - End.

			} else if ("1".equalsIgnoreCase(getRebatePriceEnabled)) { // NOPMD
				
				if (InventoryConstants.REBATE_LEADER.equalsIgnoreCase(rebateType)) {

					rebateIndicator = InventoryConstants.REBATE_LEADER;

				} else if ((getContractGrpAffiliationList != null)
						&& !getContractGrpAffiliationList.isEmpty()
						&& getContractGrpAffiliationList
								.contains(contractGrpAffNum)) { // NOPMD

					rebateIndicator = InventoryConstants.REBATE_GENERIC;
				}
			}
		} else {
			
			LOGGER.warning("Rebate_Indicator from Database is null.");
		}
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.fine("RebateIndicator " + rebateIndicator);
		}

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}
		return rebateIndicator;
	}
	
	/**
	 * The toXMLGregorianCalendar method will convert the input calendar object to the
	 * XMLGregorianCalendar format.
	 *  
	 * @param c
	 * @return
	 * @throws DatatypeConfigurationException
	 */
	public static XMLGregorianCalendar toXMLGregorianCalendar(Calendar c)
	throws DatatypeConfigurationException {
		
		GregorianCalendar gc = new GregorianCalendar();	
		gc.setTimeInMillis(c.getTimeInMillis());
		XMLGregorianCalendar xc = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
		return xc;
	}
}
