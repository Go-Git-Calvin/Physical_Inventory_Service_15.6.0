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
 * 
 * Modified By        Date             Clarify#        Description of the change
 * Ankit Mahajan		04/29/2015						Added method getProductDetails
 *
 *********************************************************************/
package com.cardinal.ws.rest.physicalinventory.bu.router;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.cardinal.webordering.common.errorhandling.BusinessException;
import com.cardinal.webordering.common.errorhandling.ErrorHandlingHelper;
import com.cardinal.webordering.common.errorhandling.SecurityException;
import com.cardinal.webordering.common.errorhandling.SystemException;
import com.cardinal.webordering.common.logging.MessageLoggerHelper;
import com.cardinal.webordering.common.productdetails.ProductDetailsRequest;
import com.cardinal.ws.physicalinventory.PhysicalInventoryPersisterBeanLocal;
import com.cardinal.ws.physicalinventory.common.InventoryConstants;
import com.cardinal.ws.physicalinventory.common.InventoryUtility;
import com.cardinal.ws.physicalinventory.common.valueobjects.InvBaseRequest;
import com.cardinal.ws.physicalinventory.common.valueobjects.ProductDetailsResponse;
import com.cardinal.ws.physicalinventory.inventory.valueobjects.InvInventoryRequest;
import com.cardinal.ws.physicalinventory.inventory.valueobjects.InvInventoryResponse;
import com.cardinal.ws.physicalinventory.location.valueobjects.InvLocationRequest;
import com.cardinal.ws.physicalinventory.location.valueobjects.InvLocationResponse;
import com.cardinal.ws.physicalinventory.locationdetail.valueobjects.InvLocationDetailRequest;
import com.cardinal.ws.physicalinventory.locationdetail.valueobjects.InvLocationDetailResponse;


public class InventoryRouter {
	private static final String CLASS_NAME = InventoryRouter.class.getCanonicalName();
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
	 * This method routes the getInventorySummary req to the persister bean
	 * @param request the request to route
	 * @return returns a populated InvInventoryResponse obj
	 * @throws SystemException
	 */
	public InvInventoryResponse getInventorySummary(InvInventoryRequest request) 
			throws SystemException {
		final String methodName = "getInventorySummary";	

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}
		
        Context context = null;
        PhysicalInventoryPersisterBeanLocal local;
        InvInventoryResponse response = null ;
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
        env.put(Context.PROVIDER_URL, providerUrl);
        
        try {
        	context = new InitialContext(env);
			Object result = context.lookup(jndiHomeName);
			local = (PhysicalInventoryPersisterBeanLocal) javax.rmi.PortableRemoteObject.narrow(result,	PhysicalInventoryPersisterBeanLocal.class);
			response = local.getInventorySummary(request);
		} 
        catch (NamingException e) {
        	
        	LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC + " "
					+ methodName +": "+ e.getCause()+": "+
					"Naming exception occurred attempting to retrieve inventory summary for inv(s) " 
					+ "--------->" + request.toString()));
    
        	String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
        			InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME);
        	throw new SystemException(
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME,
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC, e);
		} catch (EJBException e) {
		
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					InventoryConstants.ErrorKeys.EJB_EXCEPTION,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC +": "+methodName + 
					": " + e.getCause()));
			
			String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
					InventoryConstants.ErrorKeys.EJB_EXCEPTION);
			throw new SystemException( 
					InventoryConstants.ErrorKeys.EJB_EXCEPTION,
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC, e);

			
		}
		
		return response;
	}
	
	/**
	 * This method routes a getLocatinoDetails request to the persister bean
	 * @param request the request to route
	 * @return returns a populated InvLocationDetailResponse obj
	 * @throws ParseException 
	 * @throws SystemException
	 */
	public InvLocationDetailResponse getLocationDetails(InvLocationDetailRequest request) 
			throws ParseException, SystemException {
		final String methodName = "getLocationDetails";	
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}
		
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
	    env.put(Context.PROVIDER_URL, providerUrl);
	    Context context = null;
        PhysicalInventoryPersisterBeanLocal local;
        InvLocationDetailResponse response = null ;
        
        try {
			context = new InitialContext(env);
			Object result = context.lookup(jndiHomeName);
			local = (PhysicalInventoryPersisterBeanLocal) javax.rmi.PortableRemoteObject.narrow(result,
					PhysicalInventoryPersisterBeanLocal.class);
			response = local.getLocationDetails(request);
		} 
        catch (NamingException e) {
        	String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
        			InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME);
        	LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
        			request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
        			statusCode,
        			InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC + " "
        			+ methodName +": "+ e.getCause()+": "+
        			"Naming exception occurred attempting to retrieve location details for " 
        			+ "--------->" + request.toString()));
        	
        	throw new SystemException(
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME,
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC, e);
		} catch (EJBException e) {
			String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
					InventoryConstants.ErrorKeys.EJB_EXCEPTION);
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC +": "+methodName + 
					": " + e.getCause()));
			
			throw new SystemException( 
					InventoryConstants.ErrorKeys.EJB_EXCEPTION,
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC, e);

			
		}
		
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finest("Response sent to processor  from router for getContainerDeatil------>" +  response);
		}
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, methodName);
		}

		return response;
	}

	/**
	 * This method routes a getLocationSummary request to the persister bean
	 * @param request the request to route
	 * @return returns a populated InvLocationResponse obj
	 * @throws SystemException
	 */
	public InvLocationResponse getLocationSummary(InvLocationRequest request) 
				throws SystemException {
		final String methodName = "getLocationSummary";	
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}
		
		Context context = null;
		PhysicalInventoryPersisterBeanLocal local;
		//TODO Move the EJB look up code  to a common method for reuse
		InvLocationResponse response = new InvLocationResponse();
		Hashtable<String, String> env = new Hashtable<String, String>();
		final String jndiHomeName = InventoryUtility.getValue(InventoryConstants.PHYSICAL_INVENTORY_SERVICE_NAME, InventoryConstants.PHYSICAL_INVENTORY_FILE_NAME, 
					InventoryConstants.PHYSICAL_INVENTORY_JNDI_KEY_NAME);
		final String initialContextFactory =InventoryUtility.getValue(InventoryConstants.PHYSICAL_INVENTORY_SERVICE_NAME, InventoryConstants.PHYSICAL_INVENTORY_FILE_NAME,
					InventoryConstants.PHYSICAL_INVENTORY_CONTEXT_KEY_NAME);
		final String providerUrl =InventoryUtility.getValue(InventoryConstants.PHYSICAL_INVENTORY_SERVICE_NAME, InventoryConstants.PHYSICAL_INVENTORY_FILE_NAME,
					InventoryConstants.PHYSICAL_INVENTORY_PROVIDER_KEY_NAME);

		env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
		env.put(Context.PROVIDER_URL, providerUrl);

		try {
			context = new InitialContext(env);
			Object result = context.lookup(jndiHomeName);
			local = (PhysicalInventoryPersisterBeanLocal) javax.rmi.PortableRemoteObject.narrow(result,	PhysicalInventoryPersisterBeanLocal.class);
			response = local.getLocationSummary(request);
		} 
		catch (NamingException e) {
			String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
        			InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME);
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC + " "
					+ methodName +": "+ e.getCause()+": "+
					"Naming exception occurred attempting to retrieve location summary for " 
					+ "--------->" + request.toString()));
			
        	throw new SystemException(
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME,
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC, e);
		} catch (EJBException e) {
			String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
					InventoryConstants.ErrorKeys.EJB_EXCEPTION);
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC +": "+methodName + 
					": " + e.getCause()));
			
			throw new SystemException( 
					InventoryConstants.ErrorKeys.EJB_EXCEPTION,
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC, e);


		}
       
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to processor from router for getLocationSummary------>" + response);
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		
		return response;
	}
	
	/**
	 * 
	 * This method is used to set the request and pass that request to invoke local PhysicalInventoryPersitance bean for creating new inventory
	 * in response  which is returned to the resource class.
	 *
	 * @param request instance of InvInventoryRequest
	 * @param response instance of InvInventoryResponse;
	 * @throws SystemException
	*/
				
	public InvInventoryResponse createInventory(InvInventoryRequest request)
				throws SystemException {
		final String methodName = "createInventory";	

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}
		
        Context context = null;
        PhysicalInventoryPersisterBeanLocal local;
        InvInventoryResponse response = null ;
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
        env.put(Context.PROVIDER_URL, providerUrl);
        
        try {
        	context = new InitialContext(env);
			Object result = context.lookup(jndiHomeName);
			local = (PhysicalInventoryPersisterBeanLocal) javax.rmi.PortableRemoteObject.narrow(result,	PhysicalInventoryPersisterBeanLocal.class);
			response = local.createInventory(request);
		} 
        catch (NamingException e) {
        	String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
        			InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME);
        	LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC + " "
					+ methodName +": "+ e.getCause()+": "+
					"Naming exception occurred attempting to create inventory for inv(s) " 
					+ "--------->" + request.toString()));
        	
        	throw new SystemException(
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME,
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC, e);
		} catch (EJBException e) {
			String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
					InventoryConstants.ErrorKeys.EJB_EXCEPTION);
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC +": "+methodName + 
					": " + e.getCause()));
			
			throw new SystemException( 
					InventoryConstants.ErrorKeys.EJB_EXCEPTION,
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC, e);
		}
      
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
        	LOGGER.finer("Response sent to processor from router for createInventory------>" +  response);
			LOGGER.exiting(CLASS_NAME, methodName);
		}
	
		return response;
	}

	/**
	 * This method routes a createLocation request to the persister bean
	 * @param request the request to route
	 * @return returns a populated InvLocationResponse obj
	 * @throws SystemException
	 */
	public InvLocationResponse createLocation(InvLocationRequest request) 
				throws SystemException {
		final String methodName = "createLocation";	

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}
		
        Context context = null;
        PhysicalInventoryPersisterBeanLocal local;
        InvLocationResponse response = null ;
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
        env.put(Context.PROVIDER_URL, providerUrl);
        
        try {
        	context = new InitialContext(env);
			Object result = context.lookup(jndiHomeName);
			local = (PhysicalInventoryPersisterBeanLocal) javax.rmi.PortableRemoteObject.narrow(result,	PhysicalInventoryPersisterBeanLocal.class);
			response = local.createLocation(request);
		} 
        catch (NamingException e) {
        	String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
        			InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME);
        	LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC + " "
					+ methodName +": "+ e.getCause()+": "+
					"Naming exception occurred attempting to create a location for loc(s) " 
					+ "--------->" + request.toString()));
        	
        	throw new SystemException(
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME,
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC, e);
		} catch (EJBException e) {
			String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
					InventoryConstants.ErrorKeys.EJB_EXCEPTION);
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC +": "+methodName + 
					": " + e.getCause()));
			
			throw new SystemException( 
					InventoryConstants.ErrorKeys.EJB_EXCEPTION,
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC, e);

			
		}
      
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
        	LOGGER.finer("Response sent to processor from router for createLocation------>" +  response);
			LOGGER.exiting(CLASS_NAME, methodName);
		}
	
		return response;
	}

	/**
	 * This method routes a createLocationDetail request to the persister bean
	 * @param request the request to route
	 * @return returns a populated InvLocationDetailResponse obj
	 * @throws SystemException
	 */
	public InvLocationDetailResponse createLocationDetail(InvLocationDetailRequest request) 
				throws SystemException {
		final String methodName = "createLocationDetail";	

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}
		
        Context context = null;
        PhysicalInventoryPersisterBeanLocal local;
        InvLocationDetailResponse response = null ;
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
        env.put(Context.PROVIDER_URL, providerUrl);
        
        try {
        	context = new InitialContext(env);
			Object result = context.lookup(jndiHomeName);
			local = (PhysicalInventoryPersisterBeanLocal) javax.rmi.PortableRemoteObject.narrow(result,	PhysicalInventoryPersisterBeanLocal.class);
			response = local.createLocationDetail(request);
		} 
        catch (NamingException e) {
        	String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
        			InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME);
        	LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
        			request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
        			statusCode,
        			InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC + " "
        			+ methodName +": "+ e.getCause()+": "+
        			"Naming exception occurred attempting to create location details for loc(s) " 
        			+ "--------->" + request.toString()));
        	
        	throw new SystemException(
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME,
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC, e);
		} catch (EJBException e) {
			String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
					InventoryConstants.ErrorKeys.EJB_EXCEPTION);
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC +": "+methodName + 
					": " + e.getCause()));
			
			throw new SystemException( 
					InventoryConstants.ErrorKeys.EJB_EXCEPTION,
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC, e);
		}
			
		
      
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
        	LOGGER.finer("Response sent to processor from router for createLocation------>" +  response);
			LOGGER.exiting(CLASS_NAME, methodName);
		}
	
		return response;
	}

	/**
	 * This method routes a deleteLocationDetails request to the persister bean
	 * @param request the request to route
	 * @return returns a populated InvLocationDetailResponse obj
	 * @throws SystemException
	 */
	public InvLocationDetailResponse deleteLocationDetails(InvLocationDetailRequest request) 
				throws SystemException {
		final String methodName = "deleteLocationDetails";	

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}
		
        Context context = null;
        PhysicalInventoryPersisterBeanLocal local;
        InvLocationDetailResponse response = null ;
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
        env.put(Context.PROVIDER_URL, providerUrl);
        
        try {
        	context = new InitialContext(env);
			Object result = context.lookup(jndiHomeName);
			local = (PhysicalInventoryPersisterBeanLocal) javax.rmi.PortableRemoteObject.narrow(result,	PhysicalInventoryPersisterBeanLocal.class);
			response = local.deleteLocationDetails(request);
		} 
        catch (NamingException e) {
        	String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
        			InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME);
        	LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC + " "
					+ methodName +": "+ e.getCause()+": "+
					"Naming exception occurred attempting to delete location details for loc(s) " 
					+ "--------->" + request.toString()));
        	
        	throw new SystemException(
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME,
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC, e);
		} catch (EJBException e) {
			String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
					InventoryConstants.ErrorKeys.EJB_EXCEPTION);
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC +": "+methodName + 
					": " + e.getCause()));
			
			throw new SystemException( 
					InventoryConstants.ErrorKeys.EJB_EXCEPTION,
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC, e);
			
		} 
      
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
        	LOGGER.finer("Response sent to processor from router for deleteLocationDetails------>" +  response);
			LOGGER.exiting(CLASS_NAME, methodName);
		}
	
		return response;
	}

	/**
	 * This method routes a deleteLocations request to the persister bean
	 * @param request the request to route
	 * @return returns a populated InvLocationResponse obj
	 * @throws SystemException
	 */
	public InvLocationResponse deleteLocations(InvLocationRequest request) 
				throws SystemException {
		final String methodName = "deleteLocations";	

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}
		
        Context context = null;
        PhysicalInventoryPersisterBeanLocal local;
        InvLocationResponse response = null ;
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
        env.put(Context.PROVIDER_URL, providerUrl);
        
        try {
        	context = new InitialContext(env);
			Object result = context.lookup(jndiHomeName);
			local = (PhysicalInventoryPersisterBeanLocal) javax.rmi.PortableRemoteObject.narrow(result,	PhysicalInventoryPersisterBeanLocal.class);
			response = local.deleteLocations(request);
		} 
        catch (NamingException e) {
        	String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
        			InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME);
        	LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC + " "
					+ methodName +": "+ e.getCause()+": "+
					"Naming exception occurred attempting to delete location{s} for loc(s) " 
					+ "--------->" + request.toString()));

        	throw new SystemException(
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME,
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC, e);
		} catch (EJBException e) {
			String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
					InventoryConstants.ErrorKeys.EJB_EXCEPTION);
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC +": "+methodName + 
					": " + e.getCause()));
			
			throw new SystemException( 
					InventoryConstants.ErrorKeys.EJB_EXCEPTION,
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC, e);

		} catch (Throwable e) {
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					InventoryConstants.ErrorKeys.UNKNOWN_EXCEPTION,
					InventoryConstants.ErrorKeys.UNKNOWN_EXCEPTION_OCCURRED +": "+methodName + 
					": " + e.getCause()));
			
			String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
					InventoryConstants.ErrorKeys.UNKNOWN_EXCEPTION);
			throw new SystemException( 
					InventoryConstants.ErrorKeys.UNKNOWN_EXCEPTION,
					statusCode,
					InventoryConstants.ErrorKeys.UNKNOWN_EXCEPTION_OCCURRED, e);
		}
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
        	LOGGER.finer("Response sent to processor from router for deleteLocations------>" +  response);
			LOGGER.exiting(CLASS_NAME, methodName);
		}
	
		return response;
	}

	/**
	 * This method routes a deleteInventories request to the persister bean
	 * @param request the request to route
	 * @return returns a populated InvInventoryResponse obj
	 * @throws SystemException
	 */
	public InvInventoryResponse deleteInventories(InvInventoryRequest request) 
				throws SystemException {
		final String methodName = "deleteInventories";	

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}
		
        Context context = null;
        PhysicalInventoryPersisterBeanLocal local;
        InvInventoryResponse response = null ;
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
        env.put(Context.PROVIDER_URL, providerUrl);
        
        try {
        	context = new InitialContext(env);
			Object result = context.lookup(jndiHomeName);
			local = (PhysicalInventoryPersisterBeanLocal) javax.rmi.PortableRemoteObject.narrow(result,	PhysicalInventoryPersisterBeanLocal.class);
			response = local.deleteInventories(request);
		} 
        catch (NamingException e) {
        	String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
        			InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME);
        	LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC + " "
					+ methodName +": "+ e.getCause()+": "+
					"Naming exception occurred attempting to delete inventory for inv(s) " 
					+ "--------->" + request.toString()));

        	throw new SystemException(
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME,
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC, e);
		} catch (EJBException e) {
			String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
					InventoryConstants.ErrorKeys.EJB_EXCEPTION);
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC +": "+methodName + 
					": " + e.getCause()));
			
			throw new SystemException( 
					InventoryConstants.ErrorKeys.EJB_EXCEPTION,
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC, e);

		} catch (Throwable e) {
			String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
					InventoryConstants.ErrorKeys.UNKNOWN_EXCEPTION);
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.UNKNOWN_EXCEPTION_OCCURRED +": "+methodName + 
					": " + e.getCause()));
			
			throw new SystemException( 
					InventoryConstants.ErrorKeys.UNKNOWN_EXCEPTION,
					statusCode,
					InventoryConstants.ErrorKeys.UNKNOWN_EXCEPTION_OCCURRED, e);
		}
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
        	LOGGER.finer("Response sent to processor from router for deleteInventories------>" +  response);
			LOGGER.exiting(CLASS_NAME, methodName);
		}
	
		return response;
	}
	
	/**
	 * This method routes a updateInventory request to the persister bean
	 * @param request the request to route
	 * @return returns a populated InvInventoryResponse obj
	 * @throws SystemException
	 */
	public InvInventoryResponse updateInventory(InvInventoryRequest request) 
				throws SystemException {
		final String methodName = "updateInventory";	

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}
		
        Context context = null;
        PhysicalInventoryPersisterBeanLocal local;
        InvInventoryResponse response = null ;
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
        env.put(Context.PROVIDER_URL, providerUrl);
        
        try {
        	context = new InitialContext(env);
			Object result = context.lookup(jndiHomeName);
			local = (PhysicalInventoryPersisterBeanLocal) javax.rmi.PortableRemoteObject.narrow(result,	PhysicalInventoryPersisterBeanLocal.class);
			response = local.updateInventory(request);
		} 
        catch (NamingException e) {
        	String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
        			InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME);
        	LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC + " "
					+ methodName +": "+ e.getCause()+": "+
					"Naming exception occurred attempting to update inventory for inv(s) " 
					+ "--------->" + request.toString()));

        	throw new SystemException(
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME,
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC, e);
		} catch (EJBException e) {
			String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
					InventoryConstants.ErrorKeys.EJB_EXCEPTION);
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC +": "+methodName + 
					": " + e.getCause()));
			
			throw new SystemException( 
					InventoryConstants.ErrorKeys.EJB_EXCEPTION,
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC, e);

		}
		
		return response;
	}

	/**
	 * This method routes a updateLocation request to the persister bean
	 * @param request the request to route
	 * @return returns a populated InvLocationResponse obj
	 * @throws SystemException
	 */
	public InvLocationResponse updateLocation(InvLocationRequest request) 
				throws SystemException {
		final String methodName = "updateLocation";	

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}
		
        Context context = null;
        PhysicalInventoryPersisterBeanLocal local;
        InvLocationResponse response = null ;
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
        env.put(Context.PROVIDER_URL, providerUrl);
        
        try {
        	context = new InitialContext(env);
			Object result = context.lookup(jndiHomeName);
			local = (PhysicalInventoryPersisterBeanLocal) javax.rmi.PortableRemoteObject.narrow(result,	PhysicalInventoryPersisterBeanLocal.class);
			response = local.updateLocation(request);
		} 
        catch (NamingException e) {
        	String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
        			InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME);
        	LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC + " "
					+ methodName +": "+ e.getCause()+": "+
					"Naming exception occurred attempting to update location for loc(s) " 
					+ "--------->" + request.toString()));

        	throw new SystemException(
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME,
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC, e);
		} catch (EJBException e) {
			String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
					InventoryConstants.ErrorKeys.EJB_EXCEPTION);
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC +": "+methodName + 
					": " + e.getCause()));
			
			throw new SystemException( 
					InventoryConstants.ErrorKeys.EJB_EXCEPTION,
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC, e);

		}
		
		return response;
	}
	
	/**
	 * This method routes a copyLocations request to the persister bean
	 * @param request the request to route
	 * @return returns a populated InvLocationResponse obj
	 * @throws SystemException
	 */
	public InvLocationResponse copyLocations(InvLocationRequest request) 
				throws SystemException{
		final String methodName = "copyLocations";	

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}
		
        Context context = null;
        PhysicalInventoryPersisterBeanLocal local;
        InvLocationResponse response = null ;
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
        env.put(Context.PROVIDER_URL, providerUrl);
        
        try {
        	context = new InitialContext(env);
			Object result = context.lookup(jndiHomeName);
			local = (PhysicalInventoryPersisterBeanLocal) javax.rmi.PortableRemoteObject.narrow(result,	PhysicalInventoryPersisterBeanLocal.class);
			response = local.copyLocations(request);
		} 
        catch (NamingException e) {
        	String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
        			InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME);
        	LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC + " "
					+ methodName +": "+ e.getCause()+": "+
					"Naming exception occurred attempting to copy location for loc(s) " 
					+ "--------->" + request.toString()));

        	throw new SystemException(
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME,
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC, e);
		} catch (EJBException e) {
			String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
					InventoryConstants.ErrorKeys.EJB_EXCEPTION);
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC +": "+methodName + 
					": " + e.getCause()));
			
			throw new SystemException( 
					InventoryConstants.ErrorKeys.EJB_EXCEPTION,
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC, e);

			
		} catch (Exception e) {
			String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
					InventoryConstants.ErrorKeys.UNKNOWN_EXCEPTION);
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.UNKNOWN_EXCEPTION_OCCURRED +": "+methodName + 
					": " + e.getCause()));
			
			throw new SystemException( 
					InventoryConstants.ErrorKeys.UNKNOWN_EXCEPTION,
					statusCode,
					InventoryConstants.ErrorKeys.UNKNOWN_EXCEPTION_OCCURRED, e);
		}
      
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
        	LOGGER.finer("Response sent to processor from router for deleteInventories------>" +  response);
			LOGGER.exiting(CLASS_NAME, methodName);
		}

		return response;
	}
	
	
	
	/**
	 * This method routes a updateLocationDetails request to the persister bean
	 * @param request the request to route
	 * @return returns a populated InvLocationDetailResponse obj
	 * @throws ParseException
	 * @throws SystemException
	 */
	public InvLocationDetailResponse updateLocationDetails(InvLocationDetailRequest request) 
			throws ParseException, SystemException {
		final String methodName = "updateLocationDetails";	
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}
		
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
	    env.put(Context.PROVIDER_URL, providerUrl);
	    Context context = null;
        PhysicalInventoryPersisterBeanLocal local;
        InvLocationDetailResponse response = null ;
        
        try {
			context = new InitialContext(env);
			Object result = context.lookup(jndiHomeName);
			local = (PhysicalInventoryPersisterBeanLocal) javax.rmi.PortableRemoteObject.narrow(result,
					PhysicalInventoryPersisterBeanLocal.class);
			response = local.updateLocationDetails(request);
		} 
        catch (NamingException e) {
        	String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
        			InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME);
        	LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC + " "
					+ methodName +": "+ e.getCause()+": "+
					"Naming exception occurred attempting to updating location details for loc(s) " 
					+ "--------->" + request.toString()));

        	throw new SystemException(
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME,
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC, e);
        } catch (EJBException e) {
        	String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
					InventoryConstants.ErrorKeys.EJB_EXCEPTION);
        	LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC +": "+methodName + 
					": " + e.getCause()));
			
			throw new SystemException( 
					InventoryConstants.ErrorKeys.EJB_EXCEPTION,
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC, e);

		}
		
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finest("Response sent to processor  from router for getContainerDeatil------>" +  response);
		}
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, methodName);
		}

		return response;
	}
	

	/**
	 * This method routes a reOrderLocationDetails request to the persister bean
	 * @param request the request to route
	 * @return returns a populated InvLocationDetailResponse obj
	 * @throws ParseException 
	 * @throws SystemException
	 */
	public InvLocationDetailResponse reOrderLocationDetails(InvLocationDetailRequest request) 
			throws ParseException, SystemException {
		final String methodName = "reOrderLocationDetails";	
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}
		
		Hashtable<String, String> env = new Hashtable<String, String>();
		final String jndiHomeName = InventoryUtility.getValue(InventoryConstants.PHYSICAL_INVENTORY_SERVICE_NAME, InventoryConstants.PHYSICAL_INVENTORY_FILE_NAME, 
				InventoryConstants.PHYSICAL_INVENTORY_JNDI_KEY_NAME);
		final String initialContextFactory =InventoryUtility.getValue(InventoryConstants.PHYSICAL_INVENTORY_SERVICE_NAME, InventoryConstants.PHYSICAL_INVENTORY_FILE_NAME,
				InventoryConstants.PHYSICAL_INVENTORY_CONTEXT_KEY_NAME);
		final String providerUrl =InventoryUtility.getValue(InventoryConstants.PHYSICAL_INVENTORY_SERVICE_NAME, InventoryConstants.PHYSICAL_INVENTORY_FILE_NAME,
				InventoryConstants.PHYSICAL_INVENTORY_PROVIDER_KEY_NAME);
		env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
	    env.put(Context.PROVIDER_URL, providerUrl);
	    Context context = null;
        PhysicalInventoryPersisterBeanLocal local;
        InvLocationDetailResponse response = null;
        
        try {
			context = new InitialContext(env);
			Object result = context.lookup(jndiHomeName);
			local = (PhysicalInventoryPersisterBeanLocal) javax.rmi.PortableRemoteObject.narrow(result,
					PhysicalInventoryPersisterBeanLocal.class);
			response = local.reOrderLocationDetails(request);
		} 
        catch (NamingException e) {
        	String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
        			InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME);
        	LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC + " "
					+ methodName +": "+ e.getCause()+": "+
					"Naming exception occurred attempting to reorder location seq for loc(s) " 
					+ "--------->" + request.toString()));

        	throw new SystemException(
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME,
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC, e);
		} catch (EJBException e) {
			String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
					InventoryConstants.ErrorKeys.EJB_EXCEPTION);
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC +": "+methodName + 
					": " + e.getCause()));
			
			throw new SystemException( 
					InventoryConstants.ErrorKeys.EJB_EXCEPTION,
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC, e);

		}
		
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finest("Response sent to processor  from router for getContainerDeatil------>" +  response);
		}
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, methodName);
		}

		return response;
	}
	

	/**
	 * This method routes a setLocationsCost request to the persister bean
	 * @param request the request to route
	 * @return returns a populated InvLocationResponse obj
	 */
	public InvLocationResponse setLocationsCost(InvLocationRequest request) {
		final String methodName = "setLocationsCost";	
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}
		
		Context context = null;
		PhysicalInventoryPersisterBeanLocal local;
		//TODO Move the EJB look up code  to a common method for reuse
		InvLocationResponse response = new InvLocationResponse();
		Hashtable<String, String> env = new Hashtable<String, String>();
		final String jndiHomeName = InventoryUtility.getValue(InventoryConstants.PHYSICAL_INVENTORY_SERVICE_NAME, InventoryConstants.PHYSICAL_INVENTORY_FILE_NAME, 
					InventoryConstants.PHYSICAL_INVENTORY_JNDI_KEY_NAME);
		final String initialContextFactory =InventoryUtility.getValue(InventoryConstants.PHYSICAL_INVENTORY_SERVICE_NAME, InventoryConstants.PHYSICAL_INVENTORY_FILE_NAME,
					InventoryConstants.PHYSICAL_INVENTORY_CONTEXT_KEY_NAME);
		final String providerUrl =InventoryUtility.getValue(InventoryConstants.PHYSICAL_INVENTORY_SERVICE_NAME, InventoryConstants.PHYSICAL_INVENTORY_FILE_NAME,
					InventoryConstants.PHYSICAL_INVENTORY_PROVIDER_KEY_NAME);

		env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
		env.put(Context.PROVIDER_URL, providerUrl);

		try {
			context = new InitialContext(env);
			Object result = context.lookup(jndiHomeName);
			local = (PhysicalInventoryPersisterBeanLocal) javax.rmi.PortableRemoteObject.narrow(result,	PhysicalInventoryPersisterBeanLocal.class);
			response = local.setLocationsCost(request);
		} 
		catch (NamingException e) {
			String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
        			InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME);
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC + " "
					+ methodName +": "+ e.getCause()+": "+
					"Naming exception occurred attempting to set location cost for loc(s) " 
					+ "--------->" + request.toString()));

        	throw new SystemException(
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME,
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC, e);
		} catch (EJBException e) {
			String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
					InventoryConstants.ErrorKeys.EJB_EXCEPTION);
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC +": "+methodName + 
					": " + e.getCause()));
			
			throw new SystemException( 
					InventoryConstants.ErrorKeys.EJB_EXCEPTION,
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC, e);


		}
       
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to processor from router for getLocationSummary------>" + response);
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		
		return response;
	}
	
	/**
	 * This method routes a getLocationsProducts request to the persister bean
	 * @param request the request to route
	 * @return returns a populated List<String> of products
	 */
	public List<String> getLocationsProducts(InvLocationRequest request) {
		final String methodName = "getLocationsProducts";	
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}
		
		Context context = null;
		PhysicalInventoryPersisterBeanLocal local;
		//TODO Move the EJB look up code  to a common method for reuse
		List<String> response =null;
		final Hashtable<String, String> env = new Hashtable<String, String>();
		final String jndiHomeName = InventoryUtility.getValue(InventoryConstants.PHYSICAL_INVENTORY_SERVICE_NAME, InventoryConstants.PHYSICAL_INVENTORY_FILE_NAME, 
					InventoryConstants.PHYSICAL_INVENTORY_JNDI_KEY_NAME);
		final String initialContextFactory =InventoryUtility.getValue(InventoryConstants.PHYSICAL_INVENTORY_SERVICE_NAME, InventoryConstants.PHYSICAL_INVENTORY_FILE_NAME,
					InventoryConstants.PHYSICAL_INVENTORY_CONTEXT_KEY_NAME);
		final String providerUrl =InventoryUtility.getValue(InventoryConstants.PHYSICAL_INVENTORY_SERVICE_NAME, InventoryConstants.PHYSICAL_INVENTORY_FILE_NAME,
					InventoryConstants.PHYSICAL_INVENTORY_PROVIDER_KEY_NAME);

		env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
		env.put(Context.PROVIDER_URL, providerUrl);

		try {
			context = new InitialContext(env);
			Object result = context.lookup(jndiHomeName);
			local = (PhysicalInventoryPersisterBeanLocal) javax.rmi.PortableRemoteObject.narrow(result,	PhysicalInventoryPersisterBeanLocal.class);
			response = local.getLocationsProducts(request);
		} 
		catch (NamingException e) {
			String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
        			InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME);
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC + " "
					+ methodName +": "+ e.getCause()+": "+
					"Naming exception occurred attempting to retrieve location products for loc(s) " 
					+ "--------->" + request.toString()));

        	throw new SystemException(
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME,
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC, e);
		} catch (EJBException e) {
			String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
					InventoryConstants.ErrorKeys.EJB_EXCEPTION);
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC +": "+methodName + 
					": " + e.getCause()));
			
			throw new SystemException( 
					InventoryConstants.ErrorKeys.EJB_EXCEPTION,
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC, e);

		}
       
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to processor from router for getLocationSummary------>" + response);
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		
		return response;
	}
	
	/**
	 * This method routes a updateLocationStatus request to the persister bean
	 * @param request the request to route
	 * @return returns a populated InvLocationResponse obj
	 */
	public InvLocationResponse updateLocationStatus(InvLocationRequest request) {
		final String methodName = "updateLocationStatus";	

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}
		
        Context context = null;
        PhysicalInventoryPersisterBeanLocal local;
        InvLocationResponse response = null ;
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
        env.put(Context.PROVIDER_URL, providerUrl);
        
        try {
        	context = new InitialContext(env);
			Object result = context.lookup(jndiHomeName);
			local = (PhysicalInventoryPersisterBeanLocal) javax.rmi.PortableRemoteObject.narrow(result,	PhysicalInventoryPersisterBeanLocal.class);
			response = local.updateLocationStatus(request);
		} 
        catch (NamingException e) {
        	String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
        			InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME);
        	LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC + " "
					+ methodName +": "+ e.getCause()+": "+
					"Naming exception occurred attempting to update location status for loc(s) " 
					+ "--------->" + request.toString()));

        	throw new SystemException(
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME,
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC, e);
		} catch (EJBException e) {
			String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
					InventoryConstants.ErrorKeys.EJB_EXCEPTION);
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC +": "+methodName + 
					": " + e.getCause()));
			
			throw new SystemException( 
					InventoryConstants.ErrorKeys.EJB_EXCEPTION,
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC, e);

		}
      
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
        	LOGGER.finer("Response sent to processor from router for createLocation------>" +  response);
			LOGGER.exiting(CLASS_NAME, methodName);
		}
	
		return response;
	}
	
	/**
	 * This method routes a getValidLocation request to the persister bean
	 * @param request the request to route
	 * @return returns a populated InvLocationResponse obj
	 */
	public InvLocationResponse getValidLocation(InvLocationRequest request, int locNum, String shipToCust, String shipToLoc) {
		final String methodName = "getValidLocation";	

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}
		
        Context context = null;
        PhysicalInventoryPersisterBeanLocal local;
        InvLocationResponse response = null ;
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
        env.put(Context.PROVIDER_URL, providerUrl);
        
        try {
        	context = new InitialContext(env);
			Object result = context.lookup(jndiHomeName);
			local = (PhysicalInventoryPersisterBeanLocal) javax.rmi.PortableRemoteObject.narrow(result,	PhysicalInventoryPersisterBeanLocal.class);
			response = local.getValidLocation(request, locNum, shipToCust, shipToLoc);
		} 
        catch (NamingException e) {
        	String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
        			InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME);
        	LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC + " "
					+ methodName +": "+ e.getCause()+": "+
					"Naming exception occurred attempting to validate location for loc(s) " 
					+ "--------->" + request.toString()));

        	throw new SystemException(
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME,
					statusCode,
					InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC, e);
		} catch (EJBException e) {
			String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
					InventoryConstants.ErrorKeys.EJB_EXCEPTION);
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC +": "+methodName + 
					": " + e.getCause()));
			
			throw new SystemException( 
					InventoryConstants.ErrorKeys.EJB_EXCEPTION,
					statusCode,
					InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC, e);

		}
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
        	LOGGER.finer("Response sent to processor from router for getValidLocation------>" +  response);
			LOGGER.exiting(CLASS_NAME, methodName);
		}
	
		return response;
	}
	
	/**
	 *Changes made in Agile 15.4 to make direct DB call to retrieve 
	 *product details instead of relying in the Product Details EJB
	 *
	 * @param products
	 * @param request
	 * @return
	 * @throws SystemException
	 * @throws BusinessException
	 * @throws IOException
	 */
	 public  Map<String,ProductDetailsResponse>  getProductDetails(
	    		final List<String> products,final InvBaseRequest request)
				throws SystemException, BusinessException, IOException {
			
		 final String METHOD_NAME = "getProductDetails";

			if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
				LOGGER.entering(CLASS_NAME, METHOD_NAME);
			}

		
			ProductDetailsRequest prdUtilityRequest = null;
			Map<String, ProductDetailsResponse> response = null;

			LOGGER.info(MessageLoggerHelper.buildMessage(
					request.getShipToCustomer() + InventoryConstants.DASH
							+ request.getDistributionCenter(),
					request.getUserName(), request.getTransactionId(), null, null,
					null, CLASS_NAME, METHOD_NAME));

	        Context context = null;
	        PhysicalInventoryPersisterBeanLocal local;
	       
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
	        env.put(Context.PROVIDER_URL, providerUrl);
	        
			// Get product details			

			try {

				List<ProductDetailsResponse> productDetailsResponseList = null;

				if (products != null && !products.isEmpty()) {

				
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

					//productDetailsResponseList = productDetailsUtility
						//	.getProductDetails(prdUtilityRequest);
					
				 	context = new InitialContext(env);
					Object result = context.lookup(jndiHomeName);
					local = (PhysicalInventoryPersisterBeanLocal) javax.rmi.PortableRemoteObject.narrow(result,	PhysicalInventoryPersisterBeanLocal.class);
					productDetailsResponseList = local.getProductDetails(products, request);

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
			catch (NamingException e) {
	        	String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
	        			InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME);
	        	LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
						request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
						statusCode,
						InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC + " "
						+ METHOD_NAME +": "+ e.getCause()+": "+
						"Naming exception occurred attempting to validate location for loc(s) " 
						+ "--------->" + request.toString()));

	        	throw new SystemException(
						InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME,
						statusCode,
						InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC, e);
			} catch (EJBException e) {
				String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(
						InventoryConstants.ErrorKeys.EJB_EXCEPTION);
				LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
						request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
						statusCode,
						InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC +": "+METHOD_NAME + 
						": " + e.getCause()));
				
				throw new SystemException( 
						InventoryConstants.ErrorKeys.EJB_EXCEPTION,
						statusCode,
						InventoryConstants.ErrorKeys.EJB_EXCEPTION_DESC, e);

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
		
			
			LOGGER.info(MessageLoggerHelper.buildMessage(
					request.getShipToCustomer() + InventoryConstants.DASH
							+ request.getDistributionCenter(),
					request.getUserName(), request.getTransactionId(), "ProductDetails Response:::: " + response, null,
					null, CLASS_NAME, METHOD_NAME));

		
			if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
				LOGGER.exiting(CLASS_NAME, METHOD_NAME);
			}		

			return response;
		}
}