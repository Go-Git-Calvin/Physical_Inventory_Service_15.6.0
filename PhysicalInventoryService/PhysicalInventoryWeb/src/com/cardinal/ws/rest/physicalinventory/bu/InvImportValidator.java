/**
 * 
 */
package com.cardinal.ws.rest.physicalinventory.bu;

import java.util.logging.Logger;

import com.cardinal.webordering.common.errorhandling.ErrorHandlingHelper;
import com.cardinal.webordering.common.logging.MessageLoggerHelper;
import com.cardinal.ws.physicalinventory.common.InventoryConstants;
import com.cardinal.ws.physicalinventory.common.valueobjects.InvBaseRequest;
import com.cardinal.ws.physicalinventory.locationdetail.valueobjects.InvImportRequest;

/**
 * @author karthigeyan.nagaraja
 *
 */
public class InvImportValidator { // NOPMD by stephen.perry01 on 5/24/13 3:00 PM
	private static final String CLASS_NAME = InvImportValidator.class.getCanonicalName();
	private static final Logger LOGGER = MessageLoggerHelper.getLogger(CLASS_NAME);
	private InvImportValidator() {};
	
	/**
     * The validateImportRequest method validates the request parameter of import operation
     *
     * @param request
     *
     * @throws IllegalArgumentException
     */
	public static void validateImportRequest(final InvImportRequest request) {
		
		final String methodName = "validateImportRequest";

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}
		validateBaseRequest(request);
		
		if (request.getFileName()== null || request.getFileName().isEmpty()) {
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION),
					InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION_DESC +
					".  Invalid File Name  : value='" + request.getFileName() + "'"));
			throw new IllegalArgumentException("Invalid File Name  : value='"
					+ request.getFileName() + "'");
		}
		
		if (request.getFileType()== null || request.getFileType().isEmpty()) {
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION),
					InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION_DESC +
					".  Invalid File   Type: value='" + request.getFileType() + "'"));
			throw new IllegalArgumentException("Invalid File   Type: value='"
					+ request.getFileType() + "'");
		}
		
		if (request.getImportFile()== null || request.getImportFile().length==0) {
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION),
					InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION_DESC +
					".  Invalid  Import File  : value='" + request.getImportFile() + "'"));
			throw new IllegalArgumentException("Invalid  Import File  : value='"
					+ request.getImportFile() + "'");
		}
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, methodName);
		
		}
		
	}
	
	/**
	 * Validate the content type of the InvImportRequest object
	 * @param contentType
	 */
	public static void validateImportRequestContentType(final InvImportRequest request, 
			final String contentType) {
		final String methodName = "validateImportRequestContentType";

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}
		
		if (contentType== null || contentType.isEmpty() || contentType.contains("image")) {
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION),
					InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION_DESC +
					".  Invalid Import File Content Type : value='" + request.getImportFile() + 
					"', Content Type='" + contentType + "'"));
			throw new IllegalArgumentException("Invalid content type : value='"
					+ request.getImportFile() + "', Content Type='" + contentType + "'");
		}
		
		// text/plain, application/vnd.ms-excel
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, methodName);
		
		}
	}
	
	/**
	 * Validate the base request
	 * @param request
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
					".  Invalid Transaction ID!!!"));
			throw new IllegalArgumentException("Invalid Transaction ID!!!");
		}
		
		if(request.getUserId() == null){
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION),
					InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION_DESC +
					".  Invalid User Id!!!"));
			throw new IllegalArgumentException("Invalid User Id!!!");
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
	
}
