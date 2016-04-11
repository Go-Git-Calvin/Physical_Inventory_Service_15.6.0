/*********************************************************************
 *
 * $Workfile: BackOrderHistoryRouter.java $
 * Copyright 2012 Cardinal Health
 *
 *********************************************************************
 *
 * Revision History:
 *
 * $Log: com/cardinal/ws/rest/export/bu/router/InventoryResource.java $
 *
 *********************************************************************/
package com.cardinal.ws.rest.physicalinventory.resources;

import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cardinal.webordering.common.logging.MessageLoggerHelper;
import com.cardinal.ws.physicalinventory.common.InventoryConstants;
import com.cardinal.ws.physicalinventory.common.InventoryExceptionMapper;
import com.cardinal.ws.physicalinventory.common.InventoryUtility;
import com.cardinal.ws.physicalinventory.inventory.valueobjects.InvInventoryRequest;
import com.cardinal.ws.physicalinventory.inventory.valueobjects.InvInventoryResponse;
import com.cardinal.ws.physicalinventory.location.valueobjects.InvLocationRequest;
import com.cardinal.ws.physicalinventory.location.valueobjects.InvLocationResponse;
import com.cardinal.ws.physicalinventory.locationdetail.valueobjects.InvLocationDetailRequest;
import com.cardinal.ws.physicalinventory.locationdetail.valueobjects.InvLocationDetailResponse;
import com.cardinal.ws.rest.physicalinventory.bu.InventoryProcessor;
import com.cardinal.ws.rest.physicalinventory.bu.InventoryValidator;

@Path(value = "/Inventory")
public class InventoryResource {
	private static final String CLASS_NAME = InventoryResource.class.getCanonicalName();
	private static final Logger LOGGER = MessageLoggerHelper.getLogger(CLASS_NAME);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "CloseLocations")
	public Response closeLocations(@Context HttpHeaders headers, InvLocationRequest request) {
		String methodName = "closeLocations";

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
			LOGGER.info("Input: " + request.toString());
		}

		Response.ResponseBuilder builder = Response.ok();

		try {
			InventoryValidator.validateCloseLocationsRequest(request);
			InventoryProcessor invProcessor = new InventoryProcessor();
			InvLocationResponse invResponse = invProcessor.closeLocations(request);
			builder = Response.ok(invResponse, MediaType.APPLICATION_JSON);
		}
		catch (Throwable t) { //NOPMD Vignesh.Kandadi 06/18/2012  want to catch even Errors and return appropriate HTTP error codes to client
			if (request != null) {
				LOGGER.log(Level.SEVERE,
						MessageLoggerHelper.buildErrorMessage(request.getDistributionCenter() +
								InventoryConstants.DASH + request.getShipToCustomer(),
								request.getUserId(), request.getTransactionId(),
								InventoryExceptionMapper.getAlertMessage(t).get(InventoryConstants.ERRORCODE),
								t.getMessage(), null, null,
								InventoryExceptionMapper.getSource(t).get(CLASS_NAME),
								InventoryExceptionMapper.getSource(t).get(methodName)), t);
			}
			//InventoryUtility.logAlertMessage(t);
			builder = InventoryExceptionMapper.handleException(t);
		}

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to portal for Close Locations " + "|\r\n" + builder.build().getEntity());
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		return builder.build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "CopyLocations")
	public Response copyLocations(@Context HttpHeaders headers, InvLocationRequest request)  {
		//Request {"copyNamesOnly": false, "copyToInv": 0, "copyToInvName":"Copy of Partial Inventory", "costType":0, "countType":0, "inventory":{"createDate":"2012-11-28", "inventoryId": 5, "inventoryName": null, "numberOfLocations":3, "totalValue": 0}, "locName":"Pharmacy", "locs": [14,16], "transactionId": "123456789", "userId": "defbri02", "storeId": "123", "timeZone": "EST", "shipToCustomer": "12092", "distributionCenter": "34", "recordsPerPage": 100, "pageNum": 1, "numberOfDays": 3, "refreshFlag": false}
		//Response {"numberOfPages": 1,"pageNum": 1,"statusCode": "0","statusMsg": "Success.","inventory": {"createDate": "2012-11-20","inventoryId": 2,"inventoryName": "Inventory #2","numberOfLocations": 0,"totalValue": 0},"locations": [{"afxDate": "01/01/1900","costType": "Original Price","countType": "Full","createDate": "11/20/2012","inventoryID": 2,"lineCount": 0,"locationId": 571000000,"locationName": "Pharmacy","locStatus": "Created","totalValue": 0}]}
		String methodName = "copyLocations";

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
			LOGGER.info("Input: " + request.toString());
		}

		Response.ResponseBuilder builder = Response.ok();

		try {
			InventoryValidator.validateLocationRequest(request);
			InventoryProcessor invProcessor = new InventoryProcessor();
			InvLocationResponse invResponse = invProcessor.copyLocations(request);
			builder = Response.ok(invResponse, MediaType.APPLICATION_JSON);
		}
		catch (Throwable t) { //NOPMD Vignesh.Kandadi 06/18/2012  want to catch even Errors and return appropriate HTTP error codes to client
			if (request != null) {
				LOGGER.log(Level.SEVERE,
						MessageLoggerHelper.buildErrorMessage(request.getDistributionCenter() +
								InventoryConstants.DASH + request.getShipToCustomer(),
								request.getUserId(), request.getTransactionId(),
								InventoryExceptionMapper.getAlertMessage(t).get(InventoryConstants.ERRORCODE),
								t.getMessage(), null, null,
								InventoryExceptionMapper.getSource(t).get(CLASS_NAME),
								InventoryExceptionMapper.getSource(t).get(methodName)), t);
			}
			//InventoryUtility.logAlertMessage(t);
			builder = InventoryExceptionMapper.handleException(t);
		}

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to portal for Copy Locations " + "|\r\n" + builder.build().getEntity());
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		return builder.build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "CreateInventory")
	public Response createInventory(@Context HttpHeaders headers, InvInventoryRequest request) {
		//Request {"inventory":{"createDate":"2012-11-19", "inventoryId": null, "inventoryName": null, "numberOfLocations":0, "totalValue": 0}, "invName":"Inventory #2", "invs":null, "updatedValue": null, "transactionId": "123456789", "userId": "defbri02", "storeId": "123", "timeZone": "EST", "shipToCustomer": "12092", "distributionCenter": "34", "recordsPerPage": 100, "pageNum": 1, "numberOfDays": 3, "refreshFlag": false}
		//Response {"numberOfPages": 1,"pageNum": 1,"statusCode": "0","statusMsg": "Success.","inventories": [{"createDate": "11/20/2012","inventoryId": 2,"inventoryName": "Inventory #2","numberOfLocations": 0,"totalValue": 0}, {"createDate": "11/14/2012","inventoryId": 1,"inventoryName": "Test Inventory #1","numberOfLocations": 1,"totalValue": 0.00}]}
		String methodName = "createInventory";

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
			LOGGER.info("Input: " + request.toString());
		}

		Response.ResponseBuilder builder = Response.ok();

		try {
			InventoryValidator.validateInventoryRequest(request);
			InventoryProcessor invProcessor = new InventoryProcessor();
			InvInventoryResponse invResponse = invProcessor.createInventory(request);
			builder = Response.ok(invResponse, MediaType.APPLICATION_JSON);
		}
		catch (Throwable t) { //NOPMD Vignesh.Kandadi 06/18/2012  want to catch even Errors and return appropriate HTTP error codes to client
			if (request != null) {
				LOGGER.log(Level.SEVERE,
						MessageLoggerHelper.buildErrorMessage(request.getDistributionCenter() +
								InventoryConstants.DASH + request.getShipToCustomer(),
								request.getUserId(), request.getTransactionId(),
								InventoryExceptionMapper.getAlertMessage(t).get(InventoryConstants.ERRORCODE),
								t.getMessage(), null, null,
								InventoryExceptionMapper.getSource(t).get(CLASS_NAME),
								InventoryExceptionMapper.getSource(t).get(methodName)), t);
			}
			//InventoryUtility.logAlertMessage(t);
			builder = InventoryExceptionMapper.handleException(t);
		}

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to portal for Create Inventory " + "|\r\n" + builder.build().getEntity());
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		return builder.build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "UpdateInventory")
	public Response updateInventory(@Context HttpHeaders headers, InvInventoryRequest request) {
		//Request {"inventory":{"createDate":"2012-11-19", "inventoryId": null, "inventoryName": null, "numberOfLocations":0, "totalValue": 0}, "invName":"Inventory #2", "invs":null, "updatedValue": null, "transactionId": "123456789", "userId": "defbri02", "storeId": "123", "timeZone": "EST", "shipToCustomer": "12092", "distributionCenter": "34", "recordsPerPage": 100, "pageNum": 1, "numberOfDays": 3, "refreshFlag": false}
		//Response {"numberOfPages": 1,"pageNum": 1,"statusCode": "0","statusMsg": "Success.","inventories": [{"createDate": "11/20/2012","inventoryId": 2,"inventoryName": "Inventory #2","numberOfLocations": 0,"totalValue": 0}, {"createDate": "11/14/2012","inventoryId": 1,"inventoryName": "Test Inventory #1","numberOfLocations": 1,"totalValue": 0.00}]}
		String methodName = "updateInventory";

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
			LOGGER.info("Input: " + request.toString());
		}

		Response.ResponseBuilder builder = Response.ok();

		try {
			InventoryValidator.validateInventoryRequest(request);
			InventoryProcessor invProcessor = new InventoryProcessor();
			InvInventoryResponse invResponse = invProcessor.updateInventory(request);
			builder = Response.ok(invResponse, MediaType.APPLICATION_JSON);
		}
		catch (Throwable t) { //NOPMD Vignesh.Kandadi 06/18/2012  want to catch even Errors and return appropriate HTTP error codes to client
			if (request != null) {
				LOGGER.log(Level.SEVERE,
						MessageLoggerHelper.buildErrorMessage(request.getDistributionCenter() +
								InventoryConstants.DASH + request.getShipToCustomer(),
								request.getUserId(), request.getTransactionId(),
								InventoryExceptionMapper.getAlertMessage(t).get(InventoryConstants.ERRORCODE),
								t.getMessage(), null, null,
								InventoryExceptionMapper.getSource(t).get(CLASS_NAME),
								InventoryExceptionMapper.getSource(t).get(methodName)), t);
			}
			//InventoryUtility.logAlertMessage(t);
			builder = InventoryExceptionMapper.handleException(t);
		}

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to portal for Update Inventory " + "|\r\n" + builder.build().getEntity());
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		return builder.build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "CreateLocation")
	public Response createLocation(@Context HttpHeaders headers, InvLocationRequest request) { 
		//Request {"copyNamesOnly":false, "copyToInv":null, "costType":0, "countType":0, "inventory":{"createDate":"2012-11-20", "inventoryId": 2, "inventoryName": "Inventory #2", "numberOfLocations":0, "totalValue": 0}, "locName":"Pharmacy", "locs":null, "transactionId": "123456789", "userId": "defbri02", "storeId": "123", "timeZone": "EST", "shipToCustomer": "12092", "distributionCenter": "34", "recordsPerPage": 100, "pageNum": 1, "numberOfDays": 3, "refreshFlag": false}
		//Response {"numberOfPages": 1,"pageNum": 1,"statusCode": "0","statusMsg": "Success.","inventory": {"createDate": "2012-11-20","inventoryId": 2,"inventoryName": "Inventory #2","numberOfLocations": 0,"totalValue": 0},"locations": [{"afxDate": "01/01/1900","costType": "Original Price","countType": "Full","createDate": "11/20/2012","inventoryID": 2,"lineCount": 0,"locationId": 571000000,"locationName": "Pharmacy","locStatus": "Created","totalValue": 0}]}
		String methodName = "createLocation";

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
			LOGGER.info("Input: " + request.toString());
		}

		Response.ResponseBuilder builder = Response.ok();

		try {
			InventoryValidator.validateLocationRequest(request);
			InventoryProcessor invProcessor = new InventoryProcessor();
			InvLocationResponse invResponse = invProcessor.createLocation(request);
			builder = Response.ok(invResponse, MediaType.APPLICATION_JSON);
		}
		catch (Throwable t) { //NOPMD Vignesh.Kandadi 06/18/2012  want to catch even Errors and return appropriate HTTP error codes to client
			if (request != null) {
				LOGGER.log(Level.SEVERE,
						MessageLoggerHelper.buildErrorMessage(request.getDistributionCenter() +
								InventoryConstants.DASH + request.getShipToCustomer(),
								request.getUserId(), request.getTransactionId(),
								InventoryExceptionMapper.getAlertMessage(t).get(InventoryConstants.ERRORCODE),
								t.getMessage(), null, null,
								InventoryExceptionMapper.getSource(t).get(CLASS_NAME),
								InventoryExceptionMapper.getSource(t).get(methodName)), t);
			}
			//InventoryUtility.logAlertMessage(t);
			builder = InventoryExceptionMapper.handleException(t);
		}

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to portal for Create Location " + "|\r\n" + builder.build().getEntity());
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		return builder.build();
	}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "UpdateLocation")
	public Response updateLocation(@Context HttpHeaders headers, InvLocationRequest request) { 
		//Request {"copyNamesOnly":false, "copyToInv":null, "costType":0, "countType":0, "inventory":{"createDate":"2012-11-20", "inventoryId": 2, "inventoryName": "Inventory #2", "numberOfLocations":0, "totalValue": 0}, "locName":"Pharmacy", "locs":null, "transactionId": "123456789", "userId": "defbri02", "storeId": "123", "timeZone": "EST", "shipToCustomer": "12092", "distributionCenter": "34", "recordsPerPage": 100, "pageNum": 1, "numberOfDays": 3, "refreshFlag": false}
		//Response {"numberOfPages": 1,"pageNum": 1,"statusCode": "0","statusMsg": "Success.","inventory": {"createDate": "2012-11-20","inventoryId": 2,"inventoryName": "Inventory #2","numberOfLocations": 0,"totalValue": 0},"locations": [{"afxDate": "01/01/1900","costType": "Original Price","countType": "Full","createDate": "11/20/2012","inventoryID": 2,"lineCount": 0,"locationId": 571000000,"locationName": "Pharmacy","locStatus": "Created","totalValue": 0}]}
		String methodName = "updateLocation";

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
			LOGGER.info("Input: " + request.toString());
		}

		Response.ResponseBuilder builder = Response.ok();

		try {
			InventoryValidator.validateUpdateLocationRequest(request);
			InventoryProcessor invProcessor = new InventoryProcessor();
			InvLocationResponse invResponse = invProcessor.updateLocation(request);
			builder = Response.ok(invResponse, MediaType.APPLICATION_JSON);
		}
		catch (Throwable t) { //NOPMD Vignesh.Kandadi 06/18/2012  want to catch even Errors and return appropriate HTTP error codes to client
			if (request != null) {
				LOGGER.log(Level.SEVERE,
						MessageLoggerHelper.buildErrorMessage(request.getDistributionCenter() +
								InventoryConstants.DASH + request.getShipToCustomer(),
								request.getUserId(), request.getTransactionId(),
								InventoryExceptionMapper.getAlertMessage(t).get(InventoryConstants.ERRORCODE),
								t.getMessage(), null, null,
								InventoryExceptionMapper.getSource(t).get(CLASS_NAME),
								InventoryExceptionMapper.getSource(t).get(methodName)), t);
			}
			//InventoryUtility.logAlertMessage(t);
			builder = InventoryExceptionMapper.handleException(t);
		}

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to portal for Update Location " + "|\r\n" + builder.build().getEntity());
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		return builder.build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "UpdateLocationStatus")
	public Response updateLocationStatus(@Context HttpHeaders headers, InvLocationRequest request) { 
		//Request {"copyNamesOnly":false, "copyToInv":null, "costType":0, "countType":0, "inventory":{"createDate":"2012-11-20", "inventoryId": 2, "inventoryName": "Inventory #2", "numberOfLocations":0, "totalValue": 0}, "locName":"Pharmacy", "locs":null, "transactionId": "123456789", "userId": "defbri02", "storeId": "123", "timeZone": "EST", "shipToCustomer": "12092", "distributionCenter": "34", "recordsPerPage": 100, "pageNum": 1, "numberOfDays": 3, "refreshFlag": false}
		//Response {"numberOfPages": 1,"pageNum": 1,"statusCode": "0","statusMsg": "Success.","inventory": {"createDate": "2012-11-20","inventoryId": 2,"inventoryName": "Inventory #2","numberOfLocations": 0,"totalValue": 0},"locations": [{"afxDate": "01/01/1900","costType": "Original Price","countType": "Full","createDate": "11/20/2012","inventoryID": 2,"lineCount": 0,"locationId": 571000000,"locationName": "Pharmacy","locStatus": "Created","totalValue": 0}]}
		String methodName = "updateLocationStatus";

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
			LOGGER.info("Input: " + request.toString());
		}

		Response.ResponseBuilder builder = Response.ok();

		try {
			//InventoryValidator.validateUpdateLocationRequest(request);
			InventoryProcessor invProcessor = new InventoryProcessor();
			InvLocationResponse invResponse = invProcessor.updateLocationStatus(request);
			builder = Response.ok(invResponse, MediaType.APPLICATION_JSON);
		}
		catch (Throwable t) { //NOPMD Vignesh.Kandadi 06/18/2012  want to catch even Errors and return appropriate HTTP error codes to client
			if (request != null) {
				LOGGER.log(Level.SEVERE,
						MessageLoggerHelper.buildErrorMessage(request.getDistributionCenter() +
								InventoryConstants.DASH + request.getShipToCustomer(),
								request.getUserId(), request.getTransactionId(),
								InventoryExceptionMapper.getAlertMessage(t).get(InventoryConstants.ERRORCODE),
								t.getMessage(), null, null,
								InventoryExceptionMapper.getSource(t).get(CLASS_NAME),
								InventoryExceptionMapper.getSource(t).get(methodName)), t);
			}
			//InventoryUtility.logAlertMessage(t);
			builder = InventoryExceptionMapper.handleException(t);
		}

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to portal for Update Location(s) status " + "|\r\n" + builder.build().getEntity());
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		return builder.build();
	}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "CreateLocationDetail")
	public Response createLocationDetail(@Context HttpHeaders headers, InvLocationDetailRequest request) {
		//Request {"cost":0.00, "lines": null, "location": {"afxDate": null, "costType": null, "countType": null, "createDate": null, "inventoryID": 1, "lineCount": null, "locationId": 1, "locationName": null, "locStatus": null, "totalValue": 0}, "product": 1110097, "quantity": 127, "uoif":12, "transactionId": "123456789", "userId": "defbri02", "storeId": "123", "timeZone": "EST", "shipToCustomer": "12092", "distributionCenter": "34","recordsPerPage": 100, "pageNum": 1, "numberOfDays": 3, "refreshFlag": false}
		//Response {"numberOfPages": 1,"pageNum": 1,"statusCode": "0","statusMsg": "Success.","lines": [{"afxCost": 0.00,"afxUOIF": 0,"countType": "Full","origCost": 17.95,"origUOIF": 12,"product": {"accCode": "AccCode","contract": "Contract","corpDescription": "Corporate Description","cost": "9.99", "csn": "Custom Stock Number","deaSchedNum": 1,"deptName": "Department Rx","form": "Form","genericName": "Generic Name","hamFinelineCode": "Hamacher Fineline Code","manufacturerName": "Manufactureer Name","ndc": "1234567890123","ndcUpc": "1231231231231","packQty": 12,"poNum": "Purchase Order Number","product": "1110097","qty": 27,"size": "Size","specialHandling": "Special Handling","strength": "Strength","totalCost": "27.99","tradeName": "Trade Name","udf1": "UDF 1","unitOfMeasure": "Unit of Measure","uoif": 1,"upc": "123456789012","vendor": "Vendor"},"qty": 1}],"location": {"inventoryID": 1,"lineCount": 0,"locationId": 1,"totalValue": 0}}
		String methodName = "createLocationDetail";

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
			LOGGER.info("Input: " + request.toString());
		}

		Response.ResponseBuilder builder = Response.ok();

		try {
			InventoryValidator.validateLocationDetailRequest(request);
			InventoryProcessor invProcessor = new InventoryProcessor();
			InvLocationDetailResponse invResponse = invProcessor.createLocationDetail(request);
			
			builder = Response.ok(invResponse, MediaType.APPLICATION_JSON);
		}
		catch (Throwable t) { //NOPMD Vignesh.Kandadi 06/18/2012  want to catch even Errors and return appropriate HTTP error codes to client
			if (request != null) {
				LOGGER.log(Level.SEVERE,
						MessageLoggerHelper.buildErrorMessage(request.getDistributionCenter() +
								InventoryConstants.DASH + request.getShipToCustomer(),
								request.getUserId(), request.getTransactionId(),
								InventoryExceptionMapper.getAlertMessage(t).get(InventoryConstants.ERRORCODE),
								t.getMessage(), null, null,
								InventoryExceptionMapper.getSource(t).get(CLASS_NAME),
								InventoryExceptionMapper.getSource(t).get(methodName)), t);
			}
			//InventoryUtility.logAlertMessage(t);
			builder = InventoryExceptionMapper.handleException(t);
		}

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to portal for Create Inventory Location Detail " + "|\r\n" + builder.build().getEntity());
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		return builder.build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "DeleteInventory")
	public Response deleteInventories(@Context HttpHeaders headers, InvInventoryRequest request) {
		//Request {"inventory":null, "invName": null, "invs": [{"createDate": null, "inventoryId": 2, "inventoryName": null, "numberOfLocations": 1, "totalValue":0.00}], "updatedValue": null, "transactionId": "123456789", "userId": "defbri02", "storeId": "123", "timeZone": "EST", "shipToCustomer": "12092", "distributionCenter": "34","recordsPerPage": 100, "pageNum": 1, "numberOfDays": 3, "refreshFlag": false}
		//Response {"numberOfPages": 1,"pageNum": 1,"statusCode": "0","statusMsg": "Success.","inventories": [{"createDate": "11/19/2012","inventoryId": 1,"inventoryName": "Inventory #1","numberOfLocations": 3,"totalValue": 25.00}]}
		String methodName = "deleteInventories";

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}

		Response.ResponseBuilder builder = Response.ok();

		try {
			InventoryValidator.validateInventoryRequest(request);
			InventoryProcessor invProcessor = new InventoryProcessor();
			InvInventoryResponse invResponse = invProcessor.deleteInventories(request);
			builder = Response.ok(invResponse, MediaType.APPLICATION_JSON);
		}
		catch (Throwable t) { //NOPMD Vignesh.Kandadi 06/18/2012  want to catch even Errors and return appropriate HTTP error codes to client
			if (request != null) {
				LOGGER.log(Level.SEVERE,
						MessageLoggerHelper.buildErrorMessage(request.getDistributionCenter() +
								InventoryConstants.DASH + request.getShipToCustomer(),
								request.getUserId(), request.getTransactionId(),
								InventoryExceptionMapper.getAlertMessage(t).get(InventoryConstants.ERRORCODE),
								t.getMessage(), null, null,
								InventoryExceptionMapper.getSource(t).get(CLASS_NAME),
								InventoryExceptionMapper.getSource(t).get(methodName)), t);
			}
			//InventoryUtility.logAlertMessage(t);
			builder = InventoryExceptionMapper.handleException(t);
		}

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to portal for Delete Inventory " + "|\r\n" + builder.build().getEntity());
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		return builder.build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "DeleteLocationDetail")
	public Response deleteLocationDetails(@Context HttpHeaders headers, InvLocationDetailRequest request) {
		//Request {"cost":0.00, "lines": [{"afxCost": 0.00, "afxUOIF": 0, "countType": "", "origCost":0.00, "origUOIF":0, "product": {"accCode": null,"contract": null,"corpDescription": null,"cost": null,"csn": null,"deaSchedNum": 0,"deptName": null,"form": null,"genericName": null,"hamFinelineCode": null,"manufacturerName": null,"ndc": null,"ndcUpc": null,"packQty": 0,"poNum": null,"product": "1110097","qty": 0,"size": null,"specialHandling": null,"strength": null,"totalCost": null,"tradeName": null,"udf1": null,"unitOfMeasure": null,"uoif": 0,"upc": null,"vendor": null}, "qty":0, "seq":0}], "location": {"afxDate": null, "costType": null, "countType": null, "createDate": null, "inventoryID": 1, "lineCount": null, "locationId": 1, "locationName": null, "locStatus": null, "totalValue": 0}, "product": 1110097, "quantity": 127, "uoif":12, "transactionId": "123456789", "userId": "defbri02", "storeId": "123", "timeZone": "EST", "shipToCustomer": "12092", "distributionCenter": "34","recordsPerPage": 100, "pageNum": 1, "numberOfDays": 3, "refreshFlag": false}
		//Response {"numberOfPages": 1,"pageNum": 1,"statusCode": "0","statusMsg": "Success.","lines": [{"afxCost": 0.00,"afxUOIF": 0,"countType": "Full","origCost": 17.95,"origUOIF": 12,"product": {"accCode": "AccCode","contract": "Contract","corpDescription": "Corporate Description","cost": "9.99", "csn": "Custom Stock Number","deaSchedNum": 1,"deptName": "Department Rx","form": "Form","genericName": "Generic Name","hamFinelineCode": "Hamacher Fineline Code","manufacturerName": "Manufactureer Name","ndc": "1234567890123","ndcUpc": "1231231231231","packQty": 12,"poNum": "Purchase Order Number","product": "1110097","qty": 27,"size": "Size","specialHandling": "Special Handling","strength": "Strength","totalCost": "27.99","tradeName": "Trade Name","udf1": "UDF 1","unitOfMeasure": "Unit of Measure","uoif": 1,"upc": "123456789012","vendor": "Vendor"},"qty": 1}],"location": {"inventoryID": 1,"lineCount": 0,"locationId": 1,"totalValue": 0}}
		String methodName = "deleteLocationDetails";

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
			LOGGER.info("Input: " + request.toString());
		}

		Response.ResponseBuilder builder = Response.ok();

		try {
			InventoryValidator.validateLocationDetailRequest(request);
			InventoryProcessor invProcessor = new InventoryProcessor();
			InvLocationDetailResponse invResponse = invProcessor.deleteLocationDetails(request);
			builder = Response.ok(invResponse, MediaType.APPLICATION_JSON);
		}
		catch (Throwable t) { //NOPMD Vignesh.Kandadi 06/18/2012  want to catch even Errors and return appropriate HTTP error codes to client
			if (request != null) {
				LOGGER.log(Level.SEVERE,
						MessageLoggerHelper.buildErrorMessage(request.getDistributionCenter() +
								InventoryConstants.DASH + request.getShipToCustomer(),
								request.getUserId(), request.getTransactionId(),
								InventoryExceptionMapper.getAlertMessage(t).get(InventoryConstants.ERRORCODE),
								t.getMessage(), null, null,
								InventoryExceptionMapper.getSource(t).get(CLASS_NAME),
								InventoryExceptionMapper.getSource(t).get(methodName)), t);
			}
			//InventoryUtility.logAlertMessage(t);
			builder = InventoryExceptionMapper.handleException(t);
		}

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to portal for Delete Inventory Location Detail " + "|\r\n" + builder.build().getEntity());
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		return builder.build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "DeleteLocations")
	public Response deleteLocations(@Context HttpHeaders headers, InvLocationRequest request) {
		//Request {"cost":0.00, "lines": [{"afxCost": 0.00, "afxUOIF": 0, "countType": "", "origCost":0.00, "origUOIF":0, "product": {"accCode": null,"contract": null,"corpDescription": null,"cost": null,"csn": null,"deaSchedNum": 0,"deptName": null,"form": null,"genericName": null,"hamFinelineCode": null,"manufacturerName": null,"ndc": null,"ndcUpc": null,"packQty": 0,"poNum": null,"product": "1110097","qty": 0,"size": null,"specialHandling": null,"strength": null,"totalCost": null,"tradeName": null,"udf1": null,"unitOfMeasure": null,"uoif": 0,"upc": null,"vendor": null}, "qty":0, "seq":0}], "location": {"afxDate": null, "costType": null, "countType": null, "createDate": null, "inventoryID": 1, "lineCount": null, "locationId": 1, "locationName": null, "locStatus": null, "totalValue": 0}, "product": 1110097, "quantity": 127, "uoif":12, "transactionId": "123456789", "userId": "defbri02", "storeId": "123", "timeZone": "EST", "shipToCustomer": "12092", "distributionCenter": "34","recordsPerPage": 100, "pageNum": 1, "numberOfDays": 3, "refreshFlag": false}
		//Response {"numberOfPages": 1,"pageNum": 1,"statusCode": "0","statusMsg": "Success.","lines": [{"afxCost": 0.00,"afxUOIF": 0,"countType": "Full","origCost": 17.95,"origUOIF": 12,"product": {"accCode": "AccCode","contract": "Contract","corpDescription": "Corporate Description","cost": "9.99", "csn": "Custom Stock Number","deaSchedNum": 1,"deptName": "Department Rx","form": "Form","genericName": "Generic Name","hamFinelineCode": "Hamacher Fineline Code","manufacturerName": "Manufactureer Name","ndc": "1234567890123","ndcUpc": "1231231231231","packQty": 12,"poNum": "Purchase Order Number","product": "1110097","qty": 27,"size": "Size","specialHandling": "Special Handling","strength": "Strength","totalCost": "27.99","tradeName": "Trade Name","udf1": "UDF 1","unitOfMeasure": "Unit of Measure","uoif": 1,"upc": "123456789012","vendor": "Vendor"},"qty": 1}],"location": {"inventoryID": 1,"lineCount": 0,"locationId": 1,"totalValue": 0}}
		String methodName = "deleteLocations";

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}

		Response.ResponseBuilder builder = Response.ok();

		try {
			InventoryValidator.validateLocationRequest(request);
			InventoryProcessor invProcessor = new InventoryProcessor();
			InvLocationResponse invResponse = invProcessor.deleteLocations(request);
			builder = Response.ok(invResponse, MediaType.APPLICATION_JSON);
		}
		catch (Throwable t) { //NOPMD Vignesh.Kandadi 06/18/2012  want to catch even Errors and return appropriate HTTP error codes to client
			if (request != null) {
				LOGGER.log(Level.SEVERE,
						MessageLoggerHelper.buildErrorMessage(request.getDistributionCenter() +
								InventoryConstants.DASH + request.getShipToCustomer(),
								request.getUserId(), request.getTransactionId(),
								InventoryExceptionMapper.getAlertMessage(t).get(InventoryConstants.ERRORCODE),
								t.getMessage(), null, null,
								InventoryExceptionMapper.getSource(t).get(CLASS_NAME),
								InventoryExceptionMapper.getSource(t).get(methodName)), t);
			}
			//InventoryUtility.logAlertMessage(t);
			builder = InventoryExceptionMapper.handleException(t);
		}

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to portal for Delete Inventory Location " + "|\r\n" + builder.build().getEntity());
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		return builder.build();
	}


	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "InventorySummary")
	public Response getInventorySummary(@Context HttpHeaders headers, InvInventoryRequest request) {
		String methodName = "getInventorySummary";

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
			LOGGER.info("Input: " + request);
		}

		Response.ResponseBuilder builder = Response.ok();

		try {
			InventoryValidator.validateInventorySummaryRequest(request);
			InventoryProcessor invProcessor = new InventoryProcessor();
			InvInventoryResponse invResponse = invProcessor.getInventorySummary(request);
			builder = Response.ok(invResponse, MediaType.APPLICATION_JSON);
		}
		catch (Throwable t) { //NOPMD Vignesh.Kandadi 06/18/2012  want to catch even Errors and return appropriate HTTP error codes to client
			if (request != null) {
				LOGGER.log(Level.SEVERE,
						MessageLoggerHelper.buildErrorMessage(request.getDistributionCenter() +
								InventoryConstants.DASH + request.getShipToCustomer(),
								request.getUserId(), request.getTransactionId(),
								InventoryExceptionMapper.getAlertMessage(t).get(InventoryConstants.ERRORCODE),
								t.getMessage(), null, null,
								InventoryExceptionMapper.getSource(t).get(CLASS_NAME),
								InventoryExceptionMapper.getSource(t).get(methodName)), t);
			}
			//InventoryUtility.logAlertMessage(t);
			builder = InventoryExceptionMapper.handleException(t);
		}

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to portal for get Inventory Summary " + "|\r\n" + builder.build().getEntity());
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		return builder.build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "LocationDetails")
	public Response getLocationDetails(@Context HttpHeaders headers, InvLocationDetailRequest request) {
		String methodName = "getLocationDetails";

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
			LOGGER.info("Input: " + request.toString());
		}

		Response.ResponseBuilder builder = Response.ok();

		try {
			InventoryValidator.validateGetLocationDetailsRequest(request);
			InventoryProcessor invProcessor = new InventoryProcessor();
			InvLocationDetailResponse invResponse = invProcessor.getLocationDetails(request);
			
			builder = Response.ok(invResponse, MediaType.APPLICATION_JSON);
		}
		catch (Throwable t) { //NOPMD Vignesh.Kandadi 06/18/2012  want to catch even Errors and return appropriate HTTP error codes to client
			if (request != null) {
				LOGGER.log(Level.SEVERE,
						MessageLoggerHelper.buildErrorMessage(request.getDistributionCenter() +
								InventoryConstants.DASH + request.getShipToCustomer(),
								request.getUserId(), request.getTransactionId(),
								InventoryExceptionMapper.getAlertMessage(t).get(InventoryConstants.ERRORCODE),
								t.getMessage(), null, null,
								InventoryExceptionMapper.getSource(t).get(CLASS_NAME),
								InventoryExceptionMapper.getSource(t).get(methodName)), t);
			}
			//InventoryUtility.logAlertMessage(t);
			builder = InventoryExceptionMapper.handleException(t);
		}

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to portal for get Location Details " + "|\r\n" + builder.build().getEntity());
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		return builder.build();
	}

	/**
	 * Inventory - Location Summary
	 * @param headers
	 * @param request
	 * @return Inventory Detail 
	 * @throws Exception
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path(value = "LocationSummary")
	public Response getLocationSummary(@Context HttpHeaders headers, InvLocationRequest request) {
		String methodName = "getLocationSummary";

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
			LOGGER.info("Input: " + request.toString());
		}

		Response.ResponseBuilder builder = Response.ok();

		try {
			InventoryValidator.validateLocationSummaryRequest(request);
			InventoryProcessor invProcessor = new InventoryProcessor();
			InvLocationResponse invResponse = invProcessor.getLocationSummary(request);
			builder = Response.ok(invResponse, MediaType.APPLICATION_JSON);
		}
		catch (Throwable t) { //NOPMD Vignesh.Kandadi 06/18/2012  want to catch even Errors and return appropriate HTTP error codes to client
			if (request != null) {
				LOGGER.log(Level.SEVERE,
						MessageLoggerHelper.buildErrorMessage(request.getDistributionCenter() +
								InventoryConstants.DASH + request.getShipToCustomer(),
								request.getUserId(), request.getTransactionId(),
								InventoryExceptionMapper.getAlertMessage(t).get(InventoryConstants.ERRORCODE),
								t.getMessage(), null, null,
								InventoryExceptionMapper.getSource(t).get(CLASS_NAME),
								InventoryExceptionMapper.getSource(t).get(methodName)), t);
			}
			//InventoryUtility.logAlertMessage(t);
			builder = InventoryExceptionMapper.handleException(t);
		}

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to portal for get Location Summary " + "|\r\n" + builder.build().getEntity());
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		return builder.build();
	}

    
    
     
     @POST
     @Consumes(MediaType.APPLICATION_JSON)
     @Produces(MediaType.APPLICATION_JSON)
     @Path(value = "UpdateLocationDetails")
     public Response updateLocationDetails(@Context HttpHeaders headers, InvLocationDetailRequest request) {

     	
     	String methodName = "UpdateLocationDetails";
     	
         if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
 			LOGGER.entering(CLASS_NAME, methodName);
     		LOGGER.info("Input: " + request.toString());
         }

     	Response.ResponseBuilder builder = Response.ok();
    	 
     	try {
     		InventoryValidator.validateLocationDetailRequest(request);
     		InventoryProcessor invProcessor = new InventoryProcessor();
     		InvLocationDetailResponse invResponse = invProcessor.updateLocationDetails(request);
     		builder = Response.ok(invResponse, MediaType.APPLICATION_JSON);
     	}
     	catch (Throwable t) { //NOPMD Vignesh.Kandadi 06/18/2012  want to catch even Errors and return appropriate HTTP error codes to client
     		if (request != null) {
     			LOGGER.log(Level.SEVERE,
     					MessageLoggerHelper.buildErrorMessage(request.getDistributionCenter() +
     							InventoryConstants.DASH + request.getShipToCustomer(),
     							request.getUserId(), request.getTransactionId(),
     							InventoryExceptionMapper.getAlertMessage(t).get(InventoryConstants.ERRORCODE),
     							t.getMessage(), null, null,
     							InventoryExceptionMapper.getSource(t).get(CLASS_NAME),
     							InventoryExceptionMapper.getSource(t).get(methodName)), t);
     		}
     		//InventoryUtility.logAlertMessage(t);
     		builder = InventoryExceptionMapper.handleException(t);
     	}
      
        if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
            LOGGER.finer("Response sent to portal for update Location Details " + "|\r\n" + builder.build().getEntity());
            LOGGER.exiting(CLASS_NAME, methodName);
        }
        return builder.build();
     }
     
     @POST
     @Consumes(MediaType.APPLICATION_JSON)
     @Produces(MediaType.APPLICATION_JSON)
     @Path(value = "setLocationsCost")
     public Response setLocationsCost(@Context HttpHeaders headers, InvLocationRequest request) {

     	
     	String methodName = "setLocationsCost";
     	
         if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
 			LOGGER.entering(CLASS_NAME, methodName);
     		LOGGER.info("Input REQUEST: " + request.toString());
         }
        /* final SimpleDateFormat dateFormat =
             new SimpleDateFormat(ReceivingConstants.DATE_TIME_FORMAT);
         mapper.getSerializationConfig().setDateFormat(dateFormat);*/
     	Response.ResponseBuilder builder = Response.ok();
    	
     	try {
     	
     		//InventoryValidator.validateSetLocationCostRequest(request);
     		InventoryProcessor invProcessor = new InventoryProcessor();
     		InvLocationResponse invResponse = invProcessor.setLocationsCost(request);
     		
     		builder = Response.ok(invResponse, MediaType.APPLICATION_JSON);
     	}
     	catch (Throwable t) { //NOPMD Vignesh.Kandadi 06/18/2012  want to catch even Errors and return appropriate HTTP error codes to client
     		if (request != null) {
     			LOGGER.log(Level.SEVERE,
     					MessageLoggerHelper.buildErrorMessage(request.getDistributionCenter() +
     							InventoryConstants.DASH + request.getShipToCustomer(),
     							request.getUserId(), request.getTransactionId(),
     							InventoryExceptionMapper.getAlertMessage(t).get(InventoryConstants.ERRORCODE),
     							t.getMessage(), null, null,
     							InventoryExceptionMapper.getSource(t).get(CLASS_NAME),
     							InventoryExceptionMapper.getSource(t).get(methodName)), t);
     		}
     		//InventoryUtility.logAlertMessage(t);
     		builder = InventoryExceptionMapper.handleException(t);
     	}
      
        if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
            LOGGER.finer("Response sent to portal for setLocationsCost  " + "|\r\n" + builder.build().getEntity());
            LOGGER.exiting(CLASS_NAME, methodName);
        }
        return builder.build();
     }
     
     @POST
     @Consumes(MediaType.APPLICATION_JSON)
     @Produces(MediaType.APPLICATION_JSON)
     @Path(value = "ReOrderLocationDetails")
     public Response reOrderLocationDetails(@Context HttpHeaders headers, InvLocationDetailRequest request) {

     	
     	String methodName = "ReOrderLocationDetails";
     	
         if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
 			LOGGER.entering(CLASS_NAME, methodName);
     		LOGGER.info("Input: " + request.toString());
         }

     	Response.ResponseBuilder builder = Response.ok();
    	 
     	try {
     		InventoryValidator.validateLocationDetailRequest(request);
     		InventoryProcessor invProcessor = new InventoryProcessor();
     		InvLocationDetailResponse invResponse = invProcessor.reOrderLocationDetails(request);
     		builder = Response.ok(invResponse, MediaType.APPLICATION_JSON);
     	}
     	catch (Throwable t) { //NOPMD Vignesh.Kandadi 06/18/2012  want to catch even Errors and return appropriate HTTP error codes to client
     		if (request != null) {
     			LOGGER.log(Level.SEVERE,
     					MessageLoggerHelper.buildErrorMessage(request.getDistributionCenter() +
     							InventoryConstants.DASH + request.getShipToCustomer(),
     							request.getUserId(), request.getTransactionId(),
     							InventoryExceptionMapper.getAlertMessage(t).get(InventoryConstants.ERRORCODE),
     							t.getMessage(), null, null,
     							InventoryExceptionMapper.getSource(t).get(CLASS_NAME),
     							InventoryExceptionMapper.getSource(t).get(methodName)), t);
     		}
     		//InventoryUtility.logAlertMessage(t);
     		builder = InventoryExceptionMapper.handleException(t);
     	}
      
        if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
            LOGGER.finer("Response sent to portal for Reorder location details " + "|\r\n" + builder.build().getEntity());
            LOGGER.exiting(CLASS_NAME, methodName);
        }
        return builder.build();
     }
    
   
    
//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path(value = "ImportData")
//    public Response importData(InvAccount acct, InvInventory inv, InvLocation loc, InvProduct[] items) {
//    	return null;
//    }

//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path(value = "PrintLocations")
//    public Response printLocations(InvAccount acct, InvInventory inv, InvLocation[] locs) {
//    	return null;
//    }

//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path(value = "UpdateLocationDetail")
//    public Response updateLocationDetails(@Context HttpHeaders headers, InvLocationDetailRequest request) throws Exception {
//    	return null;
//    }

//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path(value = "UpdateLocations")
//    public Response updateLocations(@Context HttpHeaders headers, InvLocationRequest request) throws Exception {
//    	return null;
//    }
     
     @POST
     @Consumes(MediaType.APPLICATION_JSON)
     @Produces(MediaType.APPLICATION_JSON)
     @Path(value = "isLocationValid")
     public Response isLocationValid(@Context HttpHeaders headers, InvLocationRequest request) {

    	 String methodName = "isLocationValid";

    	 if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
    		 LOGGER.entering(CLASS_NAME, methodName);
    		 LOGGER.info("Input: " + request.toString());
    	 }

    	 Response.ResponseBuilder builder = Response.ok();

    	 try {

    		 InventoryValidator.validateIsValidLocationRequest(request);
    		 InventoryProcessor invProcessor = new InventoryProcessor();
    		 InvLocationResponse invResponse = invProcessor.isLocationValid(request);

    		 builder = Response.ok(invResponse, MediaType.APPLICATION_JSON);
    
    	 }
    	 catch (Throwable t) { //NOPMD Vignesh.Kandadi 06/18/2012  want to catch even Errors and return appropriate HTTP error codes to client
    		
    		 if (request != null) {
    			 LOGGER.log(Level.SEVERE,
    					 MessageLoggerHelper.buildErrorMessage(request.getDistributionCenter() +
    							 InventoryConstants.DASH + request.getShipToCustomer(),
    							 request.getUserId(), request.getTransactionId(),
    							 InventoryExceptionMapper.getAlertMessage(t).get(InventoryConstants.ERRORCODE),
    							 t.getMessage(), null, null,
    							 InventoryExceptionMapper.getSource(t).get(CLASS_NAME),
    							 InventoryExceptionMapper.getSource(t).get(methodName)), t);
    		 }
    		 //InventoryUtility.logAlertMessage(t);
    		 builder = InventoryExceptionMapper.handleException(t);
    	 }

    	 if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
    		 LOGGER.finer("Response sent to portal for isLocationValid " + "|\r\n" + builder.build().getEntity());
    		 LOGGER.exiting(CLASS_NAME, methodName);
    	 }
    	 return builder.build();
     }


}