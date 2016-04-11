/*********************************************************************
 *
 * $Workfile: BackOrderHistoryRouter.java $
 * Copyright 2012 Cardinal Health
 *
 *********************************************************************
 *
 * Revision History:
 *
 * $Log: com/cardinal/ws/rest/export/bu/router/InventoryRouter.java $
 * Modified By             Date              Clarify#         Description of the change
 * Shruti Sinha			08/04/2015		  AD Factory 15.5   Import By CSN to Physical Inventory
 *********************************************************************/
package com.cardinal.ws.rest.physicalinventory.bu.router;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.cardinal.webordering.common.errorhandling.ErrorHandlingHelper;
import com.cardinal.webordering.common.errorhandling.SystemException;
import com.cardinal.webordering.common.logging.MessageLoggerHelper;
import com.cardinal.ws.physicalinventory.PhysicalInventoryPersisterBeanLocal;
import com.cardinal.ws.physicalinventory.common.InventoryConstants;
import com.cardinal.ws.physicalinventory.common.InventoryUtility;
import com.cardinal.ws.physicalinventory.locationdetail.valueobjects.InvImportRequest;
import com.cardinal.ws.physicalinventory.locationdetail.valueobjects.InvImportResponse;

public class InvImportRouter {
	private static final String CLASS_NAME = InvImportRouter.class.getCanonicalName();
	private static final Logger LOGGER = MessageLoggerHelper.getLogger(CLASS_NAME);
	final String jndiHomeName = InventoryUtility.getValue(
			InventoryConstants.PHYSICAL_INVENTORY_SERVICE_NAME, 
			InventoryConstants.PHYSICAL_INVENTORY_FILE_NAME, 
			InventoryConstants.PHYSICAL_INVENTORY_JNDI_KEY_NAME);
	final String initialContextFactory = InventoryUtility.getValue(
			InventoryConstants.PHYSICAL_INVENTORY_SERVICE_NAME, 
			InventoryConstants.PHYSICAL_INVENTORY_FILE_NAME,
			InventoryConstants.PHYSICAL_INVENTORY_CONTEXT_KEY_NAME);
	final String providerUrl = InventoryUtility.getValue(
			InventoryConstants.PHYSICAL_INVENTORY_SERVICE_NAME, 
			InventoryConstants.PHYSICAL_INVENTORY_FILE_NAME,
			InventoryConstants.PHYSICAL_INVENTORY_PROVIDER_KEY_NAME);

	/**
	 * This method routes a addimportedItems request to the persister bean
	 * @param request the request to route
	 * @return returns a populated InvImportResponse obj
	 */
	public InvImportResponse addimportedItems(InvImportRequest request) {
		final String methodName = "addimportedItems";	

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}
		
        Context context = null;
        PhysicalInventoryPersisterBeanLocal local;
        InvImportResponse response = null ;
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
        env.put(Context.PROVIDER_URL, providerUrl);
        
        try {
        	context = new InitialContext(env);
			Object result = context.lookup(jndiHomeName);
			local = (PhysicalInventoryPersisterBeanLocal) javax.rmi.PortableRemoteObject.narrow(result,	PhysicalInventoryPersisterBeanLocal.class);
			response = local.addimportedItems(request);
		} 
        catch (NamingException e) {
        	String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
        			InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME);
        	LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC + " "
					+ methodName +": "+ e.getCause()));

        	throw new SystemException(
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME,
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC, e);
		}
        if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		return response;
	}
	
	/**
	 * This method routes a getProductCINSearch request to the persister bean
	 * @param request the request to route
	 * @return returns a populated HashMap<String, String> map of NDC/UPC to CIN numbers
	 */
	public HashMap<String, String> getProductCINSearch(final List<String> request,final String shipToLoc,final String shipToCustomer) {
		final String methodName = "getProductCINSearch";	

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}
		
        Context context = null;
        PhysicalInventoryPersisterBeanLocal local;
        HashMap<String, String> response = null ;
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
        env.put(Context.PROVIDER_URL, providerUrl);
        
        try {
        	context = new InitialContext(env);
			Object result = context.lookup(jndiHomeName);
			local = (PhysicalInventoryPersisterBeanLocal) javax.rmi.PortableRemoteObject.narrow(result,	PhysicalInventoryPersisterBeanLocal.class);
			response = local.getProductCINSearch(request,shipToLoc,shipToCustomer);
		} 
        catch (NamingException e) {
        	String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
        			InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME);
        	LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
        			shipToLoc, shipToCustomer,null,
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC + " "
					+ methodName +": "+ e.getCause()));

        	throw new SystemException(
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME,
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC, e);
		}
        if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		return response;
	}
	// AD Factory 15.5 Release: Import by CSN to Physical Inventory
	/**
	 * This method routes fetchCINsByCSN request to the persister bean
	 * @param csnList
	 * @param request
	 * @param productCatalogServiceMap
	 * @return response
	 */
	public HashMap<String, ArrayList<String>> fetchCINsByCSN(final InvImportRequest request, ArrayList<String> csnList, List<String> productCatalogServiceMap) {
		final String methodName = "fetchCINsByCSN";	

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}
		
        Context context = null;
        PhysicalInventoryPersisterBeanLocal local;
        HashMap<String, ArrayList<String>> response = null ;
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
        env.put(Context.PROVIDER_URL, providerUrl);
        
        try {
        	context = new InitialContext(env);
			Object result = context.lookup(jndiHomeName);
			local = (PhysicalInventoryPersisterBeanLocal) javax.rmi.PortableRemoteObject.narrow(result,	PhysicalInventoryPersisterBeanLocal.class);
			response = local.fetchCINsfromCSN(request ,csnList, productCatalogServiceMap);
		} 
        catch (NamingException e) {
        	String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
        			InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME);
        	LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
        			request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC + " "
					+ methodName +": "+ e.getCause()));

        	throw new SystemException(
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME,
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC, e);
		}
        if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		return response;
	}
}