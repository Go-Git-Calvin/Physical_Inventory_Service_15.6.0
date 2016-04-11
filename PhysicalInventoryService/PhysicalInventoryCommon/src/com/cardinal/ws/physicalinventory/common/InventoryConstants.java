/*********************************************************************
 *
3* $Workfile: InventoryConstants.java $
 * Copyright 2009 Cardinal Health
 *
 *********************************************************************
 *
 * Revision History:
 *
 * $Log:com.cardinal.ws.mngphrmcntrctinfo.constants.ServiceConstants$
 ***************************************************************************************************
 *
 * Revision History:
 * Shruti Sinha		   29/07/2015	     AD Factory 15.5 Release 	Physical Inventory Import Performance Improvement.
***************************************************************************************************/
package com.cardinal.ws.physicalinventory.common;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains constants used for Physical Inv service
 * @author stephen.perry01
 *
 */
public class InventoryConstants {
    public static final String APPCONFIG = "APPCONFIG";
    public static final String PRODUCT_TYPE_C2 = "C2";
    public static final String PRODUCT_TYPE_C3 = "C3";
    public static final String PRODUCT_TYPE_C4 = "C4";
    public static final String PRODUCT_TYPE_C5 = "C5";
    public static final String PRODUCT_TYPE_RX = "Rx";
    public static final String COMMA = ",";
    public static final String DASH = " - ";
    public static final String EMPTY_STRING = " ";
    public static final String COLON = ":";
    public static final String EQUAL = "=";
    public static final String APOSTROPHE = "'";
    public static final String CLOSE_PAREN = ")";
    public static final String NEW_LINE = "\n";
    public static final String RIGHT_ARROW = ">";
    
    public static final String ERRORALERTLEVEL = "Error_Alert_Level";
    public static final String ERRORALERTTYPE = "Error_Alert_Type";
    public static final String ERRORCODE = "Error_Code";
    public static final String ERRORMESSAGE = "Error_Message";
    public static final String PRODUCT_TYPE_HBC = "HBC";
    public static final String PRODUCT_TYPE_HHC = "HHC";
    public static final String INVALID_REQ_ERROR_MSG = "Invalid request from portal see Log file for more details";
    public static final String CLASS_NAME = "CLASS_NAME";
    public static final String METHOD_NAME = "METHOD_NAME";
  
    public static final String INVENTORY_SUMMARY_CACHE_JNDI = "services/cache/InvInventorySummary"; 
    
  
    // public static final HashMap<String,String> contDetSortCols;
   
    public static final String LOCATION_DETAIL_CACHE_JNDI = "services/cache/InvLocationDetail";
    public static final String LOCATION_SUMMARY_CACHE_JNDI = "services/cache/InvLocationSummary";
    public static final String OTHER = "OTHER";
    public static final String PHYSICAL_INVENTORY_CONTEXT_KEY_NAME = "PHYSICAL_INVENTORY_INITIAL_CONTEXT_FACTORY";
    public static final String PHYSICAL_INVENTORY_FILE_NAME = "OE_InventoryService_Rest.properties";
    public static final String PHYSICAL_INVENTORY_JNDI_KEY_NAME = "PHYSICAL_INVENTORY_JNDI_NAME";
    public static final String PHYSICAL_INVENTORY_PERSISTENCE_UNIT = "PhysicalInventory";
    public static final String PHYSICAL_INVENTORY_PROVIDER_KEY_NAME = "PHYSICAL_INVENTORY_PROVIDER_URL";
    public static final String PHYSICAL_INVENTORY_SERVICE_NAME = "OE_InventoryService_Rest";
    public static final String PIPE_CR_NL = "|\r\n";
    public static final String BLANK_SPACE_REGEX="\\s{2,}";
    
    public static final int MAXIMUM_LOCATION_ITEM_COUNT = 500;
    
    public static final String PURCH_HIST_SERVICE_ENDPOINT_KEY_NAME = "PURCH_HIST_SERVICE_ENDPOINT";
    
    public static final String PHYS_INV_REST_INVALID_CONTAINER = "SY11400";
    public static final String PHYS_INV_REST_INVALID_CONTAINER_STATE = "SY11401";
    public static final String PHYS_INV_REST_INVALID_IMPORT_TRANSACTION = "SY11404";
    public static final String PHYS_INV_REST_INVALID_ITEM_LIST = "SY11402";
    public static final String PHYS_INV_REST_INVALID_REQ = "SY11499";
    public static final String PHYS_INV_REST_INVALID_SERVICE_REQUEST = "SY11403";
	public static final String PHYS_INV_REST_QUANTITY_REMAINING = "BU11401";
	public static final String PHYS_INV_REST_UNIDENTIFIED_SERVICE = "SY11405";
	public static final String PHYS_INV_REST_VALID = "BU11400";
	
	public static final String PHYS_INV_INVALID_JNDI_NAME_CODE = "BU13810";
	public static final String PHYS_INV_DATABASE_ERROR_CODE = "BU13811";
	public static final String PHYS_INV_ILLEGAL_ARG_CODE = "BU13812";
	public static final String PHYS_INV_PERSIST_EXC_CODE = "BU13813";
	public static final String PHYS_INV_ENTITY_EXISTS_CODE = "BU13814";
	public static final String PHYS_INV_TRANSACTION_REQUIRED_CODE = "BU13815";
	public static final String PHYS_INV_UNKNOWN_EXCEPTION_CODE = "BU13816";
	
	public static final String PHYS_INV_IMPORT_PROD_INVALID_CODE = "BU13811";
	
	public static final String PHY_INVENTORY_SUCCESS = "Success";
	public static final String COPY_LOCATION_SUCCESSFUL_MESSAGE = "Copy location(s) successfully completed.";
	public static final String DUPLICATE_STATUS_CODE = "duplicate";
	public static final String DUPLICATE_INVENTORY_NAME_MESSAGE = "Inventory Name already exists for this account.";
	public static final String INVENTORY_DOES_NOT_EXITS = "Inventory does not exists in this inventory.";
	public static final String SUCCESSFULLY_UPDATED_INVENTORY = "Successfully updated inventory";
	public static final String SUCCESSFULLY_CREATED_INVENTORY = "Inventory created sucessfully.";
	public static final String SUCCESSFULLY_CREATED_LOCATION= "Location created sucessfully.";
	public static final String SUCCESSFULLY_UPDATED_LCOATION ="Successfully updated location.";
	public static final String LOCATION_DOES_NOT_EXIST ="Location Does not exist in this inventory.";
	public static final String DUPLICATE_LOCATION_MESSAGE="Location Name already exits in this inevntory.";
	public static final String SUCCESSFULLY_COST_SET_LCOATION_MESSAGE="Successfully Cost Set for the location(s).";
	
//	public static final String PHYS_INV_INV_SUMM_DATABASE_ERROR_MSG = "Database Error while retrieving inventory summary.";
//	public static final String PHYS_INV_LOC_DETAILS_DATABASE_ERROR_MSG = "Database Error while retrieving location details";
//	public static final String PHYS_INV_LOC_SUMM_DATABASE_ERROR_MSG = "Database Error while retrieving location summary";
//	public static final String PHYS_INV_CREATE_INV_DATABASE_ERROR_MSG = "Database Error while creating an inventory";
//	public static final String PHYS_INV_CREATE_LOC_DATABASE_ERROR_MSG = "Database Error while creating a location";
//	public static final String PHYS_INV_CREATE_LOC_DETAIL_DATABASE_ERROR_MSG = "Database Error while creating location details";
//	public static final String PHYS_INV_DEL_LOC_DETAILS_DATABASE_ERROR_MSG = "Database Error while deleting a location";
//	public static final String PHYS_INV_DEL_LOC_DATABASE_ERROR_MSG = "Database Error while deleting location details";
//	public static final String PHYS_INV_DEL_INV_DATABASE_ERROR_MSG = "Database Error while deleting inventory";
//	public static final String PHYS_INV_UPDATE_INV_DATABASE_ERROR_MSG = "Database Error while updating inventory";
//	public static final String PHYS_INV_UPDATE_LOC_DATABASE_ERROR_MSG = "Database Error while updating location";
//	public static final String PHYS_INV_COPY_LOC_DATABASE_ERROR_MSG = "Database Error while copying location";
//	public static final String PHYS_INV_UPDATE_LOC_DETAILS_DATABASE_ERROR_MSG = "Database Error while updating location details";
//	public static final String PHYS_INV_REORDER_LOC_DETAILS_DATABASE_ERROR_MSG = "Database Error while reordering location details";
//	public static final String PHYS_INV_SET_LOC_COST_DATABASE_ERROR_MSG = "Database Error while setting location cost";
//	public static final String PHYS_INV_GET_LOC_PRODS_DATABASE_ERROR_MSG = "Database Error while getting location products";
//	public static final String PHYS_INV_UPDATE_LOC_STATUS_DATABASE_ERROR_MSG = "Database Error while updating location status";
//	public static final String PHYS_INV_GET_VALID_LOC_DATABASE_ERROR_MSG = "Database Error while validating location";
	
	public static final String PHYS_INV_STATUS_CODE = "PHYS_INV_STATUS_CODE";
	public static final String PHYS_INV_STATUS_MSG = "PHYS_INV_STATUS_MSG";
	
	public static final String INVALID_STATUS_CODE = "Invalid";
	public static final String FAILED_STATUS_CODE = "failed";
	
	public static final String REQUESTS_STRING = " Requests";
	public static final String RX = "Rx";
	public static final String SERVICE_BUSINESS_UNIT = "RetailBU";
	public static final String SINGLE_QUOTES = "'";
	public static final String SORT_ORDER_ASC = "ASC";
	public static final String SORT_ORDER_DESC = "DESC";
	
	public static final String INVENTORY_SUMMARY_DEFAULT_SORT="createDate DESC";
	public static final String INVENTORY_LOC_SUMMARY_DEFAULT_SORT="createDate DESC";
	public static final String INVENTORY_LOC_DETAIL_DEFAULT_SORT="seq DESC";
	
	public static final String INVENTORY_SUMMARY_INVENTORY_NAME_COLUMN = "invtryNam";
	public static final String INVENTORY_SUMMARY_CREATED_COLUMN = "createStp";
	public static final String INVENTORY_SUMMARY_LOCATIONS_COLUMN = "";
	public static final String INVENTORY_SUMMARY_TOTAL_VALUE_COLUMN = "";
	
	public static final String PRODUCT_TYPE_MULTIPLE_TEXT="MULTIPLE";
	public static final String PRODUCT_TYPE_INVALID_TEXT="INVALID";
	
	public static final String STATUS_OPEN = "OPEN";
	public static final String STATUS_COUNTED = "COUNTED";
	public static final String STATUS_CLOSED = "CLOSED";
	public static final int OPEN_STATUS_CODE = 0;
	public static final int COUNTED_STATUS_CODE = 1;
	public static final int COST_SET_STATUS_CODE = 2;
	public static final int CLOSED_STATUS_CODE = 3;	
	public static final int DEFAULT_COST_TYPE_CODE=0;
	public static final int ORIGINAL_COST_TYPE_CODE=1;
	public static final int LAST_PAID_COST_TYPE_CODE=2;
	public static final int CURRENT_COST_TYPE_CODE=3;
	public static final int AS_OF_DATE_COST_TYPE_CODE=4;
	public static final int PRODUCT_TYPE_VALID=0;
	public static final int PRODUCT_TYPE_INVALID=1;
	public static final int PRODUCT_TYPE_MULTIPLE=2;
	
	 // Rebate Customer Contract Price Override changes - Start.
	 public static final String REBATE_LEADER = "LDR";
	 public static final String REBATE_GENERIC = "GEN";
	 public static final double ZERO_IN_DOUBLE = 0.0;
	 public static final String IS_A_REBATE_PRGM_ACCT = "Y";
	 public static final String AFTER_REBATE_COST = "AFTER_REBATE_COST";
	 
	 // Rebate Customer Contract Price Override changes - End.
	 
	 //AD Factory 15.5 Release: Physical Inventory Import Performance Improvement - START
	 public static final String PHYSICAL_INVENTORY_JNDI_PI = "PHYSICAL_INVENTORY_JNDI_PI";
	 public static final String PHYSICAL_INV_MAX_LOCATION_DET = "PHYSICAL_INV_MAX_LOCATION_DET";
	 public static final String PHYSICAL_INV_CREATE_LOCATION_DET_1 = "PHYSICAL_INV_CREATE_LOCATION_DET_1";
	 public static final String PHYSICAL_INV_METHOD_NAME = "createLocationDetail";
	 public static final String JDBC_SCHEMA_NAME = "JDBC_SCHEMA_NAME";
	 public static final String RESOURCE_BUNDLE = "PhysicalInventoryService";
	 public static final String RSRC_BNDL_KEY_NAM = "maxProductsAllowed";
	 public static final String MAXIMUM_PRODUCTS_ITEM_COUNT = "1500";
	 public static final String PHYSICAL_INV_FETCH_CIN = "PHYSICAL_INV_FETCH_CIN";
	 public static final String PHYSICAL_INVENTORY_JNDI_CMDS = "PHYSICAL_INVENTORY_JNDI_CMDS";
	 public static final String PHYSICAL_INVENTORY_GET_LOC_STAT_CDE = "PHYSICAL_INVENTORY_GET_LOC_STAT_CDE";
	 public static final String CLOSEDLOC = "CLOSEDLOC";
	 public static final String STATUS_MSG = "This location is closed, can't add loc detail";
	 public static final String PHYSICAL_INV_INSERT_LOC_DET = "PHYSICAL_INV_INSERT_LOC_DET";
	 public static final String PHYSICAL_INV_NEXT_VAL = "PHYSICAL_INV_NEXT_VAL";
	 public static final String PHYSICAL_INV_DUPLICATE_CHECK_1 = "PHYSICAL_INV_DUPLICATE_CHECK_1";
	 public static final String PHYSICAL_INV_DUPLICATE_CHECK_2 = "PHYSICAL_INV_DUPLICATE_CHECK_2";
	 public static final String PHYSICAL_INV_SELECT_LOC_DET = "PHYSICAL_INV_SELECT_LOC_DET";
	public static final String LOCATION_DETAIL_NUM = "LOCATION_DETAIL_NUM";
	public static final String COST_UOIF_NUM = "COST_UOIF_NUM";
	public static final String COUNT_TYPE_DESC = "COUNT_TYPE_DESC";
	public static final String PROD_ID_TYPE_CDE = "PROD_ID_TYPE_CDE";
	public static final String PROD_ID = "PROD_ID";
	public static final String PROD_ID_TYPE_DESC = "PROD_ID_TYPE_DESC";
	public static final String MANUAL_COST_OVRD_AMT = "MANUAL_COST_OVRD_AMT";
	public static final String COST_AMT = "COST_AMT";
	public static final String LINE_LEVEL_QTY = "LINE_LEVEL_QTY";
	public static final String CMNT_TXT = "CMNT_TXT";
	public static final String CALC_TOTAL_VALUE_AMT = "CALC_TOTAL_VALUE_AMT";
	public static final String SEQ_NUM = "SEQ_NUM";
	public static final String ROW_ADD_STP = "ROW_ADD_STP";
	public static final String OPEN = "Open";
	public static final String PHYSICAL_INV_LOC_TOT_AMT = "PHYSICAL_INV_LOC_TOT_AMT";
	public static final String SUCCESS = "Success.";
	public static final String EMPTY_STRING_NO_SPACE = "";
	public static final String PHYSICAL_INV_UPDATE_LOC_QUERY_1 = "PHYSICAL_INV_UPDATE_LOC_QUERY_1";
	public static final String PHYSICAL_INV_UPDATE_LOC_QUERY_2 = "PHYSICAL_INV_UPDATE_LOC_QUERY_2";
	public static final String PHYSICAL_INV_UPDATE_LOC_QUERY_3 = "PHYSICAL_INV_UPDATE_LOC_QUERY_3";
	public static final String IN = "IN";
	public static final String LEFT_PARENTHESIS = "(";
	public static final String QUESTION_MARK = "?";
	public static final String RIGHT_PARENTHESIS = ")";
	public static final String PHYSICAL_INV_FETCH_CIN_QUERY_1 = "PHYSICAL_INV_FETCH_CIN_QUERY_1";
	public static final String PHYSICAL_INV_FETCH_CIN_QUERY_2 = "PHYSICAL_INV_FETCH_CIN_QUERY_2";
	public static final String PHYSICAL_INV_FETCH_CIN_QUERY_3 = "PHYSICAL_INV_FETCH_CIN_QUERY_3";
	public static final String EXCEEDS = "EXCEEDS";
	public static final String STATUS_MESSAGE_EXCEEDS = "FILE CONTAINS MORE THAN 1500 LINES";
	
	 //AD Factory 15.5 Release: Physical Inventory Import Performance Improvement - END

public static final String INV_COLUMNS_NAME_DB = "P.CORP_ITEM_NUM,P.CURR_CORP_NIFO_DLR,APE.SELL_PRICE_DLR,ASA.DEFAULT_UOIF_TYPE_CDE,PCA.UOIF_NUM,APU2.LAST_PAID_COST_DLR,P.UNIT_H_ID,P.PACK_QTY,P.PACK_SIZE_QTY,FORM_UOIF";
    public static final String INV_COLUMNS_NAME = "CIN,CURR_CORP_NIFO_DOLLAR,INVOICE_COST,ACCOUNT_UOIF,UOIF,COST_LST_PRCHS,UOM_ABB,PACK_QTY,PACK_SIZE_QTY,FORM_UOIF";
	public static final Map<String, String> COLUMNS = new HashMap<String, String>();
	public static final Map<String, String> DB_SORT_COLUMNS = new HashMap<String, String>();
	
	public static final String CIN = "CIN";
	public static final String FORM_UOIF = "FORM_UOIF";
	public static final String ACCOUNT_UOIF = "ACCOUNT_UOIF";
	public static final String UOIF = "UOIF";
	public static final String COST_LST_PRCHS = "COST_LST_PRCHS";
	public static final String PACK_QTY = "PACK_QTY";
	public static final String PACK_SIZE_QTY = "PACK_SIZE_QTY";	
	public static final String UOM_ABB ="UOM_ABB";
	public static final String INVOICE_COST = "INVOICE_COST";
	public static final String CURR_CORP_NIFO_DOLLAR = "CURR_CORP_NIFO_DOLLAR";
	
	
	public static final String ZERO = "0";
	
	public static final String UOM_EA = "EA";
	
	public static final String KEY_PD_QUERY = "QUERY_PD";
	public static final String KEY_SPD_QUERY = "QUERY_SPD";
	public static final String KEY_NC_QUERY = "QUERY_NC";
	public static final String REGEX_VAR_CIN = "<varCIN>";
	
	public static final String KEY_PHYSICAL_INVENTORY_JNDI = "PHYSICAL_INVENTORY_JNDI_PI";
	
	public static final String NO_DIVISION_CODE = "NO Division Code";
	public static final String NO_SHIP_TO_NUMBER = "NO Ship to Number";
	public static final String NO_TRANSACTION_ID = "NO Transaction Id";
	public static final String NO_ERRORCODE = "NO Error Code";
	public static final String NO_USER_DISPLAY_NAME = "NO User Display Name";
	static {
		
	
		DB_SORT_COLUMNS.put("locLineId","locationNum");
		DB_SORT_COLUMNS.put("product","prodId");
		DB_SORT_COLUMNS.put("afxUOIF","costUoifNum");
		DB_SORT_COLUMNS.put("countType","countTypeCde");
		DB_SORT_COLUMNS.put("overrideCost","manualCostOvrdAmt");
		DB_SORT_COLUMNS.put("valuationCost","costAmt");
		DB_SORT_COLUMNS.put("quantity","lineLevelQty");
		DB_SORT_COLUMNS.put("inventoryValue","calcTotalValueAmt");
		DB_SORT_COLUMNS.put("seq","seqNum");
		DB_SORT_COLUMNS.put("locLineType","prodIdTypeCde");
		DB_SORT_COLUMNS.put("createdDate","rowAddStp");	
		DB_SORT_COLUMNS.put("lineComments","cmntTxt");
	}
	    
	static{
		
		COLUMNS.put(CIN,
		"CORP_ITEM_NUM");
		
		COLUMNS.put(ACCOUNT_UOIF,
		"DEFAULT_UOIF_TYPE_CDE");
		
		COLUMNS.put(UOIF,
		"UOIF_NUM");
		
		COLUMNS.put(COST_LST_PRCHS,
		"LAST_PAID_COST_DLR");
		
		COLUMNS.put(UOM_ABB,
		"UNIT_H_ID");
		
		COLUMNS.put(PACK_QTY,
		"PACK_QTY");
		
		COLUMNS.put(PACK_SIZE_QTY,
		"PACK_SIZE_QTY");
		
		COLUMNS.put(FORM_UOIF,
		"FORM_UOIF");
		
		COLUMNS.put(CURR_CORP_NIFO_DOLLAR,
		"CURR_CORP_NIFO_DLR");
		
		COLUMNS.put(INVOICE_COST,
		"SELL_PRICE_DLR");
	
	}
public static interface ErrorKeys { // NOPMD by stephen.perry01 on 5/24/13 4:36 PM
	
    public static final String IO_EXCEPTION = "OE_PHYS_INV_REST_J2EESERVICE_IOEXCEPTION";
    public static final String PARSE_EXCEPTION = "OE_PHYS_INV_REST_PARSE_EXCEPTION";
    public static final String SAX_EXCEPTION = "OE_PHYS_INV_REST_SAX_EXCEPTION";
    public static final String SOAP_EXCEPTION = "OE_PHYS_INV_REST_SOAP_EXCEPTION";
    public static final String BUSINESSEXCEPTION_KEY = "OE_PHYS_INV_REST_J2EESERVICE_BU_EXCEPTION";
    public static final String SECURITYEXCEPTION_KEY = "OE_PHYS_INV_REST_J2EESERVICE_SECURITY_EXCEPTION";
    public static final String SYSTEMEXCEPTION_KEY = "OE_PHYS_INV_REST_J2EESERVICE_SYS_EXCEPTION";    
	public static final String INVALID_URL = "OE_PHYS_INV_REST_INVALID_URL";
	
	public static final String MAPPING_EXCEPTION = "OE_PHYS_INV_REST_MAPPING_EXCEPTION";
	public static final String TRANSACTION_REQUIRED_EXCEPTION = "OE_PHYS_INV_REST_TRANSACTION_REQUIRED_EXCEPTION";
	public static final String UNKNOWN_EXCEPTION = "OE_PHYS_INV_REST_UNKNOWN_EXCEPTION";
    public static final String SERVICE_EXCEPTION = "OE_PHYS_INV_REST_SERVICE_EXCEPTION";
    
    public static final String REQ_EXCEEDS_MAX_LINE_ITEM_COUNT = "OE_PHYS_INV_REST_REQ_EXCEEDS_MAX_LINE_ITEM_CNT";
    public static final String REQ_EXCEEDS_MAX_LINE_ITEM_COUNT_DESC = "Request exceeds maximum line item count";
    public static final String PHYS_INV_IMPORT_PROD_INVALID = "OE_PHYS_INV_REST_IMPORT_PROD_INVALID";
    public static final String PHYS_INV_IMPORT_PROD_INVALID_DESC = "Product added through import is invalid";
    public static final String PHYS_INV_PROD_DETAILS_UNAVAILABLE = "OE_PHYS_INV_PROD_DETAILS_UNAVAILABLE";
    public static final String PHYS_INV_PROD_DETAILS_UNAVAILABLE_DESC = "Product details service is unavailable.";
    public static final String PHYS_INV_PURCH_HISTORY_UNAVAILABLE = "OE_PHYS_INV_PURCH_HISTORY_UNAVAILABLE";
    public static final String PHYS_INV_PURCH_HISTORY_UNAVAILABLE_DESC = "Purchase History service is unavailable.";
    public static final String ILLEGAL_ARG_EXCEPTION = "OE_PHYS_INV_REST_ILLEGAL_ARGS";
    public static final String ILLEGAL_ARG_EXCEPTION_DESC = "Illegal argument exception occurred";
    public static final String INVALID_EJB_JNDI_NAME = "OE_PHYS_INV_REST_INVALID_JNDI_NAME";
    public static final String INVALID_EJB_JNDI_NAME_DESC = "Invalid JNDI name exception occurred";
    public static final String DATABASE_EXCEPTION = "OE_PHYS_INV_DATABASE_ERROR_CODE";
    public static final String DATABASE_EXCEPTION_DESC = "Database exception occurred";
    public static final String EJB_EXCEPTION = "OE_PHYS_INV_EJB_EXCEPTION_ERROR_CODE";
    public static final String EJB_EXCEPTION_DESC = "EJB exception occurred";
    
    public static final String IO_EXCEPTION_DESC = "IO exception occurred";
    public static final String PARSE_EXCEPTION_DESC = "Parse exception occurred";
    public static final String SAX_EXCEPTION_DESC = "SAX exception occurred";
    public static final String SOAP_EXCEPTION_DESC = "SOAP exception occurred";
    public static final String BUSINESSEXCEPTION_KEY_DESC = "Business exception occurred";
    public static final String SECURITYEXCEPTION_KEY_DESC = "Security exception occurred";
    public static final String SYSTEMEXCEPTION_KEY_DESC = "System exception occurred";    
	public static final String INVALID_URL_DESC = "Invalid URL exception occurred";
	
	public static final String MAPPING_EXCEPTION_DESC = "Mapping exception occurred";
	public static final String TRANSACTION_REQUIRED_EXCEPTION_DESC = "Transaction required exception occurred";
	public static final String UNKNOWN_EXCEPTION_DESC = "Unknown exception occurred";
    public static final String SERVICE_EXCEPTION_DESC = "Service exception occurred";
    
	public static final String NAMING_EXCEPTION_OCCURRED = "Naming exception occurred.";
	public static final String ILLEGAL_ARG_EXCEPTION_OCCURRED = "Illegal argument exception occurred.";
	public static final String PERSISTENCE_EXCEPTION_OCCURRED = "Persistence exception occurred.";
	public static final String ENTITY_EXISTS_EXCEPTION_OCCURRED = "Entity exists exception occurred.";
	public static final String TRANSACTION_REQUIRED_EXCEPTION_OCCURRED = "Transaction required exception occurred.";
	public static final String UNKNOWN_EXCEPTION_OCCURRED = "Unknown exception occurred.";
    public static final String NO_USER_DISPLAY_NAME = "NO User Display Name";
	
	
    public static final String EJB_OBJECT_CREATION_EXCEPTION = "";

}
    private InventoryConstants(){}
}