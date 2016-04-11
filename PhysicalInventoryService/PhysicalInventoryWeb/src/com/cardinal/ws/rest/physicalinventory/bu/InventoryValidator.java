/**
 * 
 */
package com.cardinal.ws.rest.physicalinventory.bu;

import java.util.logging.Logger;

import com.cardinal.webordering.common.errorhandling.ErrorHandlingHelper;
import com.cardinal.webordering.common.logging.MessageLoggerHelper;
import com.cardinal.ws.physicalinventory.common.InventoryConstants;
import com.cardinal.ws.physicalinventory.common.InventoryUtility;
import com.cardinal.ws.physicalinventory.common.valueobjects.InvBaseRequest;
import com.cardinal.ws.physicalinventory.inventory.valueobjects.InvInventoryRequest;
import com.cardinal.ws.physicalinventory.location.valueobjects.InvLocationRequest;
import com.cardinal.ws.physicalinventory.locationdetail.valueobjects.InvLocationDetailRequest;
import com.cardinal.ws.physicalinventory.product.valueobjects.InvLocProductRequest;

/**
 * @author irfan.mohammed
 *
 */
public final class InventoryValidator {
	private static final String CLASS_NAME = InventoryValidator.class.getCanonicalName();
	private static final Logger LOGGER = MessageLoggerHelper.getLogger(CLASS_NAME);
	
	private InventoryValidator() {}
	
	/**
	 * Validate an InvInventoryRequest obj
	 * @param request the request to be validated
	 */
	public static void validateInventoryRequest(final InvInventoryRequest request) {
//		try {
			validateBaseRequest(request);
//		} 
//		catch (IllegalArgumentException e) {
//			throw e;
//		}
	}

	/**
	 * Validate an InvLocationRequest request
	 * @param request the request to be validated
	 */
	public static void validateLocationRequest(final InvLocationRequest request) {
//		try {
			validateBaseRequest(request);
//		} 
//		catch (IllegalArgumentException e) {
//			throw e;
//		}
	}
	
	/**
	 * Validate an InvLocationRequest request containing correct updateLocationRequest req
	 * @param request the request to be validated
	 */
	public static void validateUpdateLocationRequest(final InvLocationRequest request) {
//		try {
			validateBaseRequest(request);
			
			if(request.getLocs() == null || request.getLocs().isEmpty()){
				LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
						request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
						ErrorHandlingHelper.getErrorCodeByErrorKey(
								InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION),
						InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION_DESC +
						".  Location ID should not be null or empty."));
				throw new IllegalArgumentException("Location ID should not be null or empty.");
			}
			if(!InventoryUtility.isNullOrEmpty(request.getLocName()) && (request.getInventory()==null || request.getInventory().getInventoryId()==0)){
				LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
						request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
						ErrorHandlingHelper.getErrorCodeByErrorKey(
								InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION),
						InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION_DESC +
						".  Location Inventory id should not be zero."));
				throw new IllegalArgumentException("Location Inventory id should not be zero.");
			 }
						
			if(InventoryUtility.isNullOrEmpty(request.getLocName()) && request.getLocComments()==null){
				LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
						request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
						ErrorHandlingHelper.getErrorCodeByErrorKey(
								InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION),
						InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION_DESC +
						".  Location Name or Comments should not be null."));
				throw new IllegalArgumentException("Location Name or Comments should not be null.");	
			}
							 
			
//		} 
//		catch (IllegalArgumentException e) {
//			throw e;
//		}
	}

	/**
	 * Validate an InvLocationDetailRequest request containing correct locationDetail req 
	 * @param request the request to be validated
	 */
	public static void validateLocationDetailRequest(final InvLocationDetailRequest request) {
//		try {
			validateBaseRequest(request);
//		} 
//		catch (IllegalArgumentException e) {
//			throw e;
//		}
	}

	/**
	 * Validate the InvLocationRequest for a correct setLocationCost request
	 * @param request the request to be validated
	 */
	public static void validateSetLocationCostRequest(final InvLocationRequest request) {
//		try {
		
		validateBaseRequest(request);
		if(request.getInventory()==null || request.getInventory().getInventoryId()==0){
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION),
					InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION_DESC +
					".  Location is required."));
			throw new IllegalArgumentException("Location is required");
		}
		if(request.getCostType()==0){
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION),
					InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION_DESC +
					".  Cost type is required."));
			throw new IllegalArgumentException("Cost type is required");
		}
			
//		} 
//		catch (IllegalArgumentException e) {
//			throw e;
//		}
	}

	/**
	 * Validate an InvLocationRequest req for correct isValidLocation req info
	 * @param request the request to be validated
	 */
	public static void validateIsValidLocationRequest(final InvLocationRequest request) {
//		try {
		
		validateBaseRequest(request);
		if(request.getLocs() == null){
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION),
					InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION_DESC +
					".  Location is required."));
			throw new IllegalArgumentException("Location is required");
		}
		if(request.getLocs().size() > 1) {
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION),
					InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION_DESC +
					".  This request only takes one location."));
			throw new IllegalArgumentException("This request only takes one location");
		}
			
//		} 
//		catch (IllegalArgumentException e) {
//			throw e;
//		}
	}
	
	/**
	 * Validate the InvBaseRequest obj
	 * @param request the request to be validated
	 */
	private static void validateBaseRequest(final InvBaseRequest request) {
		if (request == null) {
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION),
					InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION_DESC +
					".  Request is Null."));
			throw new IllegalArgumentException("Request is Null");
		}

		if(request.getTransactionId() == null){
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION),
					InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION_DESC +
					".  Invalid Transaction ID."));
			throw new IllegalArgumentException("Invalid Transaction ID.");
		}
		
		if(request.getUserId() == null){
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION),
					InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION_DESC +
					".  Invalid User Id."));
			throw new IllegalArgumentException("Invalid User Id.");
		}
		
		if(null == request.getDistributionCenter() || request.getDistributionCenter().isEmpty() || null == request.getShipToCustomer() || request.getShipToCustomer().isEmpty() ){
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION),
					InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION_DESC +
					".  Account should not be empty."));
			throw new IllegalArgumentException("Account should not be empty");
		}
	}

	/**
	 * Validate an InvInventoryRequest req containing correct inventorySummary req info
	 * @param request the request to be validated
	 */
	public static void validateInventorySummaryRequest(final InvInventoryRequest request) {
//		try {
			validateBaseRequest(request);
//		} 
//		catch (IllegalArgumentException e) {
//			throw e;
//		}
	}

	/**
	 * Validate an InvLocationDetailRequest req containing correct getLocationDetails req info
	 * @param request the request to be validated
	 */
	public static void validateGetLocationDetailsRequest(final InvLocationDetailRequest request) {
//		try {
			validateBaseRequest(request);
//		} 
//		catch (IllegalArgumentException e) {
//			throw e;
//		}
		
	}

	/**
	 * Validate an InvLocationRequest req containing correct locationSummary req info
	 * @param request the request to be validated
	 */
	public static void validateLocationSummaryRequest(final InvLocationRequest request) {
//		try {
			validateBaseRequest(request);
//		} 
//		catch (IllegalArgumentException e) {
//			throw e;
//		}
		
	}

	/**
	 * Validate an InvLocationRequest req containing correct closeLocations req info
	 * @param request the request to be validated
	 */
	public static void validateCloseLocationsRequest(final InvLocationRequest request) {
//		try {
			validateBaseRequest(request);
//		} 
//		catch (IllegalArgumentException e) {
//			throw e;
//		}
		
	}

	/**
	 * Validate an InvLocProductRequest req containing correct product req info
	 * @param request the request to be validated
	 */
	public static void validateProductRequest(final InvLocProductRequest request) {
		// TODO Auto-generated method stub
		
	}
}
