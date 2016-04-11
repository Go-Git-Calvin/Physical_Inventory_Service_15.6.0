package com.cardinal.ws.rest.physicalinventory.bu;
/*********************************************************************
*
* $Workfile: InvImportProcessor $
* Copyright 2013 Cardinal Health
*
*********************************************************************
*
* Revision History:
*
* $Log: $
*Modified By             Date              Clarify#         Description of the change
 Shruti Sinha			07/31/2015		  AD Factory 15.5   Import By CSN to Physical Inventory
*********************************************************************/

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import javax.naming.NamingException;
import javax.xml.rpc.ServiceException;

import com.cardinal.webordering.common.errorhandling.BusinessException;
import com.cardinal.webordering.common.errorhandling.ErrorHandlingHelper;
import com.cardinal.webordering.common.errorhandling.SystemException;
import com.cardinal.webordering.common.logging.MessageLoggerHelper;
import com.cardinal.webordering.common.productdetails.ProductDetailsResponse;
import com.cardinal.ws.physicalinventory.common.InventoryConstants;
import com.cardinal.ws.physicalinventory.common.InventoryUtility;
import com.cardinal.ws.physicalinventory.locationdetail.valueobjects.InvImportRequest;
import com.cardinal.ws.physicalinventory.locationdetail.valueobjects.InvImportResponse;
import com.cardinal.ws.rest.physicalinventory.bu.router.InvImportRouter;
import com.cardinal.ws.rest.physicalinventory.bu.router.ProductDetailsRouter;
import com.cardinalhealth.common.intnl.ResourceBundle;
import com.cardinalhealth.common.intnl.ResourceBundleFactory;
import com.cardinalhealth.common.intnl.ResourceBundleHelper;


/**
 * @author karthigeyan.nagaraja
 *
 */
public class InvImportProcessor {
	
	private static final String CLASS_NAME = InvImportProcessor.class.getCanonicalName();
    private static final Logger LOGGER = MessageLoggerHelper.getLogger(CLASS_NAME);
	
    /**
     * The addimportedItems method will be responsible to import the items from the
     * file.
     * 
     * @param request
     *
     * @return InvImportRequest
     * @throws BusinessException 
     * @throws ServiceException 
     * @throws NamingException 
     * @throws IOException 
     * @throws SystemException 
     */
    public InvImportResponse addimportedItems(final InvImportRequest request) throws ServiceException, BusinessException, NamingException, SystemException, IOException{

    	final String methodName = "addimportedItems";

    	if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
    		LOGGER.entering(CLASS_NAME, methodName);
    		LOGGER.finer("addimportedItems Request ------->"+ request);
    	}
    	InvImportResponse importResponse = new InvImportResponse();
    	final InvImportRouter router = new InvImportRouter();

    	final byte[] byteStream =request.getImportFile();


    	final String byteString = new String(byteStream);

    	//Modify the byteString to handle below scenarios
    	//1. Byte string contains blank lines or not
    	//2. Check whether byte string contains invalid products 
    	if(byteString != null)
    	{
    		final String [] lines = byteString.split("\\n");
    		// AD Factory 15.5 Release : Import By CSN to Physical Inventory - START
    		ResourceBundle resourceBundle = ResourceBundleFactory.getBundle(InventoryConstants.RESOURCE_BUNDLE,
                    Locale.ROOT);
        	ResourceBundleHelper resourceBundleHelper = new ResourceBundleHelper(resourceBundle);
        	int maxAllowedProducts = Integer.parseInt(resourceBundleHelper.getProperty(
            		InventoryConstants.RSRC_BNDL_KEY_NAM,
            		InventoryConstants.MAXIMUM_PRODUCTS_ITEM_COUNT));
    		
        	if(lines.length > maxAllowedProducts){        	
        	// AD Factory 15.5 Release : Import By CSN to Physical Inventory - END
    			importResponse.setStatusCode("EXCEEDS");
    			importResponse.setStatusMsg("FILE CONTAINS MORE THAN "+ maxAllowedProducts +"LINES");
    			LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
						request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
						ErrorHandlingHelper.getErrorCodeByErrorKey(
								InventoryConstants.ErrorKeys.REQ_EXCEEDS_MAX_LINE_ITEM_COUNT),
						InventoryConstants.ErrorKeys.REQ_EXCEEDS_MAX_LINE_ITEM_COUNT_DESC));
    		}else{

    			//importResponse = router.addimportedItems(request);
    			final List<String> productCatalogServiceMap = new ArrayList<String>();
    			final List<String> productTableCallMap = new ArrayList<String>();
    			final List<String> invalidProducts = new ArrayList<String>();
    			if(byteString != null)
    			{
    				for(int i=0;i<lines.length;i++){
    					if(lines[i].length() > 0){
    						final String productData[] = lines[i].split(",");
    						boolean isEmpty = InventoryUtility.isEmptyImportLine(productData);
    						if(!isEmpty) {
    							for(int j=0; j<productData.length ; j++){
    								if((j==0) && !request.isCsnImport()){
    									String corpNumString = productData[j].replace("-", "");
    									corpNumString = corpNumString.replace("\"", "");
    									corpNumString = corpNumString.replace("\r", "");
    									corpNumString = corpNumString.replace("\n", "");
    									if(corpNumString.matches("^[0-9]+([;-][0-9]+)?$")){
    										if(corpNumString.length() == 6){
    											final StringBuffer buffer = new StringBuffer();
    											buffer.append(String.valueOf(10));
    											buffer.append(corpNumString);
    											//corpNumString = "10"+corpNumString;
    											productCatalogServiceMap.add(buffer.toString());
    										}else if(corpNumString.length() == 7 || corpNumString.length() == 8){ // NOPMD by stephen.perry01 on 5/24/13 2:45 PM
    											productCatalogServiceMap.add(corpNumString);
    										}else if(corpNumString.length() >= 11 || corpNumString.length() <=13){
    											//long ndcUpc=Long.parseLong(corpNumString);
    											productTableCallMap.add(corpNumString);
    										}else if(corpNumString.length() >= 6 && corpNumString.length()<=13){
    											invalidProducts.add(corpNumString);
    										}else {
    											LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
    													request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
    													ErrorHandlingHelper.getErrorCodeByErrorKey(
    															InventoryConstants.ErrorKeys.PHYS_INV_IMPORT_PROD_INVALID),
    													": Prod number not found in valid CIN map : " +
    													corpNumString + ".  Adding as Invalid product"));

    											invalidProducts.add(corpNumString);
    										}
    									} else {
    										LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
    												request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
    												ErrorHandlingHelper.getErrorCodeByErrorKey(
    														InventoryConstants.ErrorKeys.PHYS_INV_IMPORT_PROD_INVALID),
    												": Prod number does not match regular " +
    												"expression ^[0-9]+([;-][0-9]+)?$ : " + corpNumString + 
    										".  Adding as Invalid product"));

    										invalidProducts.add(corpNumString);
    									}    								
    							}
    						} 
    					}
    				}	 
    			}
    				
    				// num products returned from WebOrderingProductDetails
    				int validLineCount = 0;
    				// num cins returned from PDS
    				int invalidProductMapCount = 0;
    				int multipleProducs=0;
    				// AD Factory 15.5 Release: Import CSN to Physical Inventory - START 
    				if(request.isCsnImport()){
    				ArrayList<String> csnList = new ArrayList<String>();
    				for(int i=0;i<lines.length;i++){
    					if(lines[i].length() > 0){
    						final String productData[] = lines[i].split(InventoryConstants.COMMA);
    						boolean isEmpty = InventoryUtility.isEmptyImportLine(productData);
    						if(!isEmpty){
    								csnList.add(productData[0]);
    						}
    					}
    				}
    				HashMap<String, ArrayList<String>> csnCINMap = router.fetchCINsByCSN(request ,csnList,productCatalogServiceMap);
    				
    				request.setCsnCINMap(csnCINMap);
    				
    				if(productCatalogServiceMap.size() > maxAllowedProducts){        	
    					importResponse.setStatusCode(InventoryConstants.EXCEEDS);
    	    			importResponse.setStatusMsg("FILE CONTAINS MORE THAN "+ maxAllowedProducts +"LINES");
    	    			LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
    							request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
    							ErrorHandlingHelper.getErrorCodeByErrorKey(
    									InventoryConstants.ErrorKeys.REQ_EXCEEDS_MAX_LINE_ITEM_COUNT),
    							InventoryConstants.ErrorKeys.REQ_EXCEEDS_MAX_LINE_ITEM_COUNT_DESC));
    	    		}
    				}
    				// AD Factory 15.5 Release: Import CSN to Physical Inventory - END
    				
    				// check product details with CINs in productCatalogServiceMap map
    				final HashMap<String,String> validCINFinalMap = new HashMap<String, String>();  	
    				if (!productCatalogServiceMap.isEmpty()){
    					final ProductDetailsRouter proRouter = new ProductDetailsRouter();
    					final Map<String, ProductDetailsResponse> prdMap=proRouter.getProductDetails(productCatalogServiceMap,request);
    					
    					//prodDetailsCount = prdMap.size();
    					for(String corpNum :productCatalogServiceMap){
    						if(prdMap.containsKey(corpNum)){
    							//ProductDetailsResponse prdResponse = new ProductDetailsResponse();
    							final ProductDetailsResponse prdResponse = prdMap.get(corpNum);
    							if(!validCINFinalMap.containsKey(corpNum)){
    								validCINFinalMap.put(corpNum,prdResponse.getProductNumber());
    							}
    							// can't just take size of the map...import file might have duplicates
    							validLineCount++;
    						} else {
    							// no response returned for this invalid cin #
    							invalidProducts.add(corpNum);
    						}
    					}
    				}

    				// check NDC/UPCs in the productTableCallMap map
    				if (!productTableCallMap.isEmpty()){	  
    					final HashMap<String, String> prodCinMap = router.getProductCINSearch(productTableCallMap,request.getDistributionCenter(),request.getShipToCustomer());
    					//	prodCinSearchCount = prodCinMap.size();
    					for(final String corpNum :productTableCallMap){
    						if(prodCinMap.containsKey(corpNum)){
    							final String cin = prodCinMap.get(corpNum);    							
    							if(!validCINFinalMap.containsKey(corpNum)){
    								validCINFinalMap.put(corpNum, cin);    								
    							}
    							
    							if(cin.equalsIgnoreCase(InventoryConstants.PRODUCT_TYPE_MULTIPLE_TEXT)) {
    								invalidProductMapCount++;
    								multipleProducs++;    								
    							}else if(cin.equalsIgnoreCase(InventoryConstants.PRODUCT_TYPE_INVALID_TEXT)) {
    							 invalidProductMapCount++;    								
    							}
    							else {
    								validLineCount++;
    							}

    						} else {
    							// no response returned for this invalid NDC/UPC #
    							//invalidProductMapCount++;
    							invalidProducts.add(corpNum);
    						}
    					}
//    					validCINFinalMap.putAll(prodCinMap);
    				}
    				if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
    				LOGGER.fine("validCINFinalMap="+validCINFinalMap);
    				}
    				// if there are no valid lines, fail this request
    				if(request.isCsnImport() && productCatalogServiceMap.isEmpty()){
    					   					
    					int noOfLinesPassed = validLineCount ;
    					int noOfLinesWithException = lines.length;
    					
    					importResponse.setNoOfLinesProcessed(noOfLinesPassed + noOfLinesWithException);
						importResponse.setNoOfLinesPassed(noOfLinesPassed);	 
						importResponse.setNoOfLinesWithException(noOfLinesWithException);	  
						importResponse.setStatusCode("SUCCESS");
						importResponse.setStatusMsg("SUCCESS");
    					
    				}else{
    					if(validLineCount == 0 && multipleProducs==0) {
        					LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
    								request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
    								ErrorHandlingHelper.getErrorCodeByErrorKey(
    										InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION),
    								"Invalid file"));
        					importResponse.setStatusCode("INVALIDFILE");
    						importResponse.setStatusMsg("INVALIDFILE");
        				} else { // otherwise call router and create return counts for response
        					if(!invalidProducts.isEmpty()){
        						for(String corpNum :invalidProducts){
        							validCINFinalMap.put(corpNum, InventoryConstants.PRODUCT_TYPE_INVALID_TEXT);
        						}
        					}
        					request.setValidCINMapToImport(validCINFinalMap);
        					importResponse = router.addimportedItems(request);
        					int noOfLinesPassed = validLineCount ;
        					int noOfLinesWithException = invalidProducts.size() + invalidProductMapCount;

        					if(null != importResponse.getStatusCode() && importResponse.getStatusCode().equals("EXCEEDS")) {
        						importResponse.setNoOfLinesProcessed(noOfLinesPassed + noOfLinesWithException);
        						LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
        								request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
        								ErrorHandlingHelper.getErrorCodeByErrorKey(
        										InventoryConstants.ErrorKeys.REQ_EXCEEDS_MAX_LINE_ITEM_COUNT),
        								InventoryConstants.ErrorKeys.REQ_EXCEEDS_MAX_LINE_ITEM_COUNT_DESC));
        					} else {
        						importResponse.setNoOfLinesProcessed(noOfLinesPassed + noOfLinesWithException);
        						importResponse.setNoOfLinesPassed(noOfLinesPassed);	 
        						importResponse.setNoOfLinesWithException(noOfLinesWithException);	  
        						importResponse.setStatusCode("SUCCESS");
        						importResponse.setStatusMsg("SUCCESS");
        					}
        				}
    				}
    				
    				
    			}


    		}
    		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
    			LOGGER.finer("Response sent to resource from processor for receiveimportedItems------>" +  importResponse);
    			LOGGER.exiting(CLASS_NAME, methodName);
    		}
    	}
    	return importResponse;

    }

}

