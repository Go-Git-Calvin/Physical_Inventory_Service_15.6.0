/*********************************************************************
 *
 * $Workfile: BackOrderHistoryRouter.java $
 * Copyright 2012 Cardinal Health
 *
 *********************************************************************
 *
 * Revision History:
 *
 * $Log: com/cardinal/ws/rest/export/bu/router/ProductDetailsRouter.java $
 * *Modified By               Date                defect ID#        Description of the change.

 * Rohit Bhat            09/22/2014            DEC Service Migration    Modified getProdPriceAsOfDate methos
 *********************************************************************/
package com.cardinal.ws.rest.physicalinventory.bu.router;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.cardinal.webordering.common.errorhandling.BusinessException;
import com.cardinal.webordering.common.errorhandling.ErrorHandlingHelper;
import com.cardinal.webordering.common.errorhandling.SecurityException;
import com.cardinal.webordering.common.errorhandling.SystemException;
import com.cardinal.webordering.common.logging.MessageLoggerHelper;
import com.cardinal.webordering.common.productdetails.ProductDetailsRequest;
import com.cardinal.webordering.common.productdetails.ProductDetailsResponse;
import com.cardinal.webordering.common.productdetails.ProductDetailsUtility;
import com.cardinal.ws.getpurchasehistoryservice._2009_10_14.GetProdPriceAsOfDateRequest;
import com.cardinal.ws.getpurchasehistoryservice._2009_10_14.GetProdPriceAsOfDateResponse;
import com.cardinal.ws.getpurchasehistoryservice._2009_10_14.GetPurchaseHistoryService_v_1_1Proxy;
import com.cardinal.ws.getpurchasehistoryservice._2009_10_14.ProdPriceAsOfDateRequest;
import com.cardinal.ws.getpurchasehistoryservice_v_1_1.common_v_1_1._2012_08_14.AccountParamType;
import com.cardinal.ws.getpurchasehistoryservice_v_1_1.common_v_1_1._2012_08_14.ObjectFactory;
import com.cardinal.ws.getpurchasehistoryservice_v_1_1.common_v_1_1._2012_08_14.ProdPriceAsOfDateDetailType;
import com.cardinal.ws.getpurchasehistoryservice_v_1_1.common_v_1_1._2012_08_14.ProdPriceAsOfDateRequestType;
import com.cardinal.ws.getpurchasehistoryservice_v_1_1.common_v_1_1._2012_08_14.ProdPriceAsOfDateResponseType;
import com.cardinal.ws.getpurchasehistoryservice_v_1_1.common_v_1_1._2012_08_14.ProductParamType;
import com.cardinal.ws.isc.schemas.env._2008_10_10.MessageContextType;
import com.cardinal.ws.physicalinventory.common.InventoryConstants;
import com.cardinal.ws.physicalinventory.common.InventoryUtility;
import com.cardinal.ws.physicalinventory.common.valueobjects.InvBaseRequest;

/**
 * @author vignesh.kandadi
 *
 */
public class ProductDetailsRouter {
	
	 private static final String CLASS_NAME = ProductDetailsRouter.class.getCanonicalName();
	    private static final Logger LOGGER = MessageLoggerHelper.getLogger(CLASS_NAME);
	    
	
	    public ProductDetailsRouter() {}

    /**
     * This method is used to set the request and pass that request to invoke the
     * Product Details EJB for getting Product Details map in response of Product as key for each product which is returned to
     * the processor class.
     *
     * @param request instance of InvLocationDetailRequest and list of products
     * @param pageNumber
     *
     * @return response Map
     *
     * @throws SystemException throws when any system validation fails.
     * @throws BusinessException throws when any business validation fails.
     * @throws SecurityException throws when any Security validation fails.
     * @throws IOException
     * 
     */
    public Map<String,ProductDetailsResponse > getProductDetails(
    		final List<String> products,final InvBaseRequest request)
			throws SystemException, BusinessException, IOException {
		final String METHOD_NAME = "getProductDetails";

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);
		}

		LOGGER.info(MessageLoggerHelper.buildMessage(
				request.getShipToCustomer() + InventoryConstants.DASH
						+ request.getDistributionCenter(),
				request.getUserName(), request.getTransactionId(), null, null,
				null, CLASS_NAME, METHOD_NAME));

		Map<String, ProductDetailsResponse> response = null;
		ProductDetailsUtility productDetailsUtility = null;
		ProductDetailsRequest prdUtilityRequest = null;

		try {

			List<ProductDetailsResponse> productDetailsResponseList = null;

			if (products != null && !products.isEmpty()) {

				productDetailsUtility = new ProductDetailsUtility();
				prdUtilityRequest = new ProductDetailsRequest();
				// Product detail utility call
				prdUtilityRequest.setShipToCustomer(Long.valueOf(request
						.getShipToCustomer()));

				prdUtilityRequest.setShipToLocation(Long.valueOf(request
						.getDistributionCenter()));

				/*
				 * if (null != request.getColumnNames()) {
				 * prdUtilityRequest.setColumnsName
				 * (request.getColumnNames().toString()); }
				 */

				prdUtilityRequest.setCinList(products);

				productDetailsResponseList = productDetailsUtility
						.getProductDetails(prdUtilityRequest);

				if (null != productDetailsResponseList && !productDetailsResponseList.isEmpty()) {
					response = new HashMap<String, ProductDetailsResponse>();
					for (ProductDetailsResponse prdResponse : productDetailsResponseList) {
						if (MessageLoggerHelper.isTraceEnabled(LOGGER)) { // NOPMD by stephen.perry01 on 5/24/13 3:12 PM
							LOGGER.fine("prdResponse.getProductNumber()::"
									+ prdResponse.getProductNumber());
							LOGGER.fine("prdResponse::" + prdResponse);
						}
						response.put(prdResponse.getProductNumber(),
								prdResponse);
					}
				}
			}
		}

		catch (SystemException sys) {
			LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer() + InventoryConstants.DASH
							+ request.getDistributionCenter(),
					request.getUserName(),
					request.getTransactionId(),
					ErrorHandlingHelper
							.getErrorCodeByErrorKey(InventoryConstants.ErrorKeys.SYSTEMEXCEPTION_KEY),
					sys.getMessage(), null, null, CLASS_NAME, METHOD_NAME));

			throw new SystemException(
					InventoryConstants.ErrorKeys.SYSTEMEXCEPTION_KEY, // NOPMD
																		// Follows
																		// Error
																		// Handling
																		// Framework
					ErrorHandlingHelper
							.getErrorCodeByErrorKey(InventoryConstants.ErrorKeys.SYSTEMEXCEPTION_KEY),
					sys.getClass() + InventoryConstants.COLON + sys.getCause()
							+ InventoryConstants.COLON + sys.getMessage(), sys);
		} catch (SecurityException sec) {
			LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer() + InventoryConstants.DASH
							+ request.getDistributionCenter(),
					request.getUserName(),
					request.getTransactionId(),
					ErrorHandlingHelper
							.getErrorCodeByErrorKey(InventoryConstants.ErrorKeys.SECURITYEXCEPTION_KEY),
					sec.getMessage(), null, null, CLASS_NAME, METHOD_NAME));

			throw new SecurityException(
					InventoryConstants.ErrorKeys.SECURITYEXCEPTION_KEY, // NOPMD
																		// Follows
					// Error Handling
					// Framework
					ErrorHandlingHelper
							.getErrorCodeByErrorKey(InventoryConstants.ErrorKeys.SECURITYEXCEPTION_KEY),
					sec.getClass() + InventoryConstants.COLON + sec.getCause()
							+ InventoryConstants.COLON + sec.getMessage(), sec);
		} catch (Throwable thr) {
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer() + InventoryConstants.DASH
							+ request.getDistributionCenter(),
					request.getUserName(),
					request.getTransactionId(),
					ErrorHandlingHelper
							.getErrorCodeByErrorKey(ErrorHandlingHelper.THROWABLE_ERROR_KEY),
					thr.getMessage(), null, null, CLASS_NAME, METHOD_NAME));

			throw new SecurityException(
					ErrorHandlingHelper.THROWABLE_ERROR_KEY, // NOPMD Follows
																// Error
																// Handling
																// Framework
					ErrorHandlingHelper
							.getErrorCodeByErrorKey(ErrorHandlingHelper.THROWABLE_ERROR_KEY),
					thr.getClass() + InventoryConstants.COLON + thr.getCause()
							+ InventoryConstants.COLON + thr.getMessage(), thr);

		}
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.info("ProductDetails Response:::: " + response);
		}

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}

		return response;
	}

    /**
     * This method sends a request to the GetPurchaseHistoryService's getProdPriceAsOfDate 
     * operation to retrieve pricing information based on a list of CINs
     * @param cins
     * @param request
     * @param costAsOfDate
     * @return
     */
    public Map<String, Double> getProdPriceAsOfDate(
    		final List<String> cins, final InvBaseRequest request, final Date costAsOfDate) {
    	final String METHOD_NAME = "getProdPriceAsOfDate";

    	if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
    		LOGGER.entering(CLASS_NAME, METHOD_NAME);
    	}
    	
    	Map<String, Double> prodPriceAsOfDateMap = null;

    	final GetPurchaseHistoryService_v_1_1Proxy proxy = new GetPurchaseHistoryService_v_1_1Proxy();

    	final String purchHistUrl =InventoryUtility.getValue(InventoryConstants.PHYSICAL_INVENTORY_SERVICE_NAME, InventoryConstants.PHYSICAL_INVENTORY_FILE_NAME,
				InventoryConstants.PURCH_HIST_SERVICE_ENDPOINT_KEY_NAME);
    	
    	proxy._getDescriptor().setEndpoint(purchHistUrl);
    	final GetProdPriceAsOfDateRequest prodPriceAsOfDateRequest = new GetProdPriceAsOfDateRequest();
    	final ProdPriceAsOfDateRequest prodPriceReq = new ProdPriceAsOfDateRequest();

    	// create the request content
    	final com.cardinal.ws.getpurchasehistoryservice.prodpriceasofdaterequest._2013_04_12.RequestContent reqContent = 
    		new com.cardinal.ws.getpurchasehistoryservice.prodpriceasofdaterequest._2013_04_12.RequestContent();

    	final ProdPriceAsOfDateRequestType reqType = new ProdPriceAsOfDateRequestType();

    	// remove cins with length > 7 
    	List<String> modifiedCinList = new ArrayList<String>();
    	for(String cin : cins) {
    		if(cin.length() <= 7) 
    			modifiedCinList.add(cin);
    	}
    	
    	try {
	    	
    		// only send the request to GetPurchHistory if there are CINs
    		//  blank requests cause exceptions
	    	if(modifiedCinList.size() > 0) { // NOPMD by stephen.perry01 on 5/24/13 3:17 PM not comparing to zero
	    		// set the product(s) parameter
	    		List<ProductParamType> products = new ArrayList<ProductParamType>();
	    		ProductParamType prod = null;
	    		for(int i=0; i<modifiedCinList.size(); i++) {
	    			prod = new ProductParamType();
	    			prod.setCardinalItemNumber(modifiedCinList.get(i));
	    			products.add(prod);
	    			LOGGER.finest("Adding CIN to request: " + prod.getCardinalItemNumber());
	    		}
	
	    		reqType.getProductParam().addAll(products);
	    		
	    		ObjectFactory objFactory = new ObjectFactory();
	
	    		// set the account type parameter
	    		AccountParamType acctParam = new AccountParamType();
	    		acctParam.setShipToAccount(objFactory.createAccountParamTypeShipToAccount(request.getShipToCustomer()));
	    		acctParam.setDivisionNumber(objFactory.createAccountParamTypeDivisionNumber(request.getDistributionCenter()));
	    		reqType.setAccountParam(acctParam);
	    		LOGGER.info("Acct info:  shiptTo:" + request.getShipToCustomer() + ", divNum: " + request.getDistributionCenter());
	
	    		// set the as of date
	    		Calendar cal = Calendar.getInstance();
	    		cal.setTime(costAsOfDate);
	    		
	    		reqType.setAsOfDate(InventoryUtility.toXMLGregorianCalendar(cal));
	    		LOGGER.info("Date: " + reqType.getAsOfDate().toString());
	
	    		reqContent.setProdPriceAsOfDateRequestContent(reqType);
	    		prodPriceReq.setRequestContent(reqContent);
	
	    		// create the request context
	    		MessageContextType context = new MessageContextType();
	    		context.setBusinessUnit("RetailBU");
	    		context.setConsumerName(CLASS_NAME);
	    		context.setServiceName("GetPurchaseHistory");
	    		context.setServiceCommand("getProdPriceAsOfDate");
	    		context.setServiceVersionNumber("1.1");
	    		context.setSubmitTimestamp((new Date()).toString());
	    		context.setMessageId(request.getTransactionId());
	
	    		prodPriceReq.setRequestContext(context);
	
	    		prodPriceAsOfDateRequest.setProdPriceAsOfDateRequest(prodPriceReq);
	
	    		
	    			GetProdPriceAsOfDateResponse resp = proxy.getProdPriceAsOfDate(prodPriceAsOfDateRequest);
	    			ProdPriceAsOfDateResponseType respType = 
	    				resp.getProdPriceAsOfDateResponse().getResponseContent().getProdPriceAsOfDateResponseContent();
	
	
	    			List<ProdPriceAsOfDateDetailType> prodPriceDetails = respType.getProdPrices();
	    			if(null != prodPriceDetails) {
	    				LOGGER.finest("Response contents of GetPurchaseHistory request");
	    				prodPriceAsOfDateMap = new HashMap<String,Double>();
	    				for(ProdPriceAsOfDateDetailType detail : prodPriceDetails) {
	    					LOGGER.finest("Cin: " + detail.getCin());
	    					LOGGER.finest("Price: " + detail.getPrice());
	    					prodPriceAsOfDateMap.put(detail.getCin(), detail.getPrice());
	    				}
	    			} else {
	    				LOGGER.finest("Response contents returned null");
	    			}
	    		}  
    	}catch (com.cardinal.ws.getpurchasehistoryservice._2009_10_14.SystemException sys) {
    		LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
    				request.getShipToCustomer() + InventoryConstants.DASH + 
    				request.getDistributionCenter(), request.getUserName(), 
    				request.getTransactionId(), 
    				ErrorHandlingHelper.getErrorCodeByErrorKey(
    						InventoryConstants.ErrorKeys.SYSTEMEXCEPTION_KEY),
    						sys.getMessage(), null, null, CLASS_NAME, METHOD_NAME));

    		throw new SystemException(
    				InventoryConstants.ErrorKeys.SYSTEMEXCEPTION_KEY, 
    				ErrorHandlingHelper.getErrorCodeByErrorKey(
    						InventoryConstants.ErrorKeys.SYSTEMEXCEPTION_KEY),
    						sys.getClass() + InventoryConstants.COLON + sys.getCause() + 
    						InventoryConstants.COLON + sys.getMessage(), sys);
    	} catch (com.cardinal.ws.getpurchasehistoryservice._2009_10_14.SecurityException sec) {
    		LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
    				request.getShipToCustomer() + InventoryConstants.DASH + 
    				request.getDistributionCenter(), request.getUserName(), 
    				request.getTransactionId(), 
    				ErrorHandlingHelper.getErrorCodeByErrorKey(
    						InventoryConstants.ErrorKeys.SECURITYEXCEPTION_KEY),
    						sec.getMessage(), null, null, CLASS_NAME, METHOD_NAME));

    		throw new SecurityException(
    				InventoryConstants.ErrorKeys.SECURITYEXCEPTION_KEY, 
    				ErrorHandlingHelper.getErrorCodeByErrorKey(
    						InventoryConstants.ErrorKeys.SECURITYEXCEPTION_KEY),
    						sec.getClass() + InventoryConstants.COLON + sec.getCause() + 
    						InventoryConstants.COLON + sec.getMessage(), sec);
    	} catch (com.cardinal.ws.getpurchasehistoryservice._2009_10_14.BusinessException be) {
    		LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
    				request.getShipToCustomer() + InventoryConstants.DASH + 
    				request.getDistributionCenter(), request.getUserName(), 
    				request.getTransactionId(), 
    				ErrorHandlingHelper.getErrorCodeByErrorKey(
    						InventoryConstants.ErrorKeys.BUSINESSEXCEPTION_KEY),
    						be.getMessage(), null, null, CLASS_NAME, METHOD_NAME));
    		
    		throw new SystemException(
    				InventoryConstants.ErrorKeys.BUSINESSEXCEPTION_KEY, 
    				ErrorHandlingHelper.getErrorCodeByErrorKey(
    						InventoryConstants.ErrorKeys.BUSINESSEXCEPTION_KEY),
    						be.getClass() + InventoryConstants.COLON + be.getCause() + 
    						InventoryConstants.COLON + be.getMessage(), be);
    	} catch (Throwable thr) {
    		LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
    				request.getShipToCustomer() + InventoryConstants.DASH +
    				request.getDistributionCenter(), request.getUserName(),
    				request.getTransactionId(), 
    				ErrorHandlingHelper.getErrorCodeByErrorKey(
    						ErrorHandlingHelper.THROWABLE_ERROR_KEY),
    						thr.getMessage(), null, null, CLASS_NAME, METHOD_NAME));
    		
    		throw new SecurityException(
    				ErrorHandlingHelper.THROWABLE_ERROR_KEY,
    				ErrorHandlingHelper.getErrorCodeByErrorKey(
    						ErrorHandlingHelper.THROWABLE_ERROR_KEY),
    						thr.getClass() + InventoryConstants.COLON + 
    						thr.getCause() + InventoryConstants.COLON + 
    						thr.getMessage(), thr);
    	}
    
    	if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
    		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
    	}
    	
    	return prodPriceAsOfDateMap;
    }
}
