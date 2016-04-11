/*********************************************************************
*
* $Workfile: ImportResource $
* Copyright 2012 Cardinal Health
*
*********************************************************************
*
* Revision History:
*
* $Log: com/cardinal/ws/rest/export/bu/router/ImportResource.java $
*
*********************************************************************/
package com.cardinal.ws.rest.physicalinventory.resources;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.wink.common.model.multipart.InMultiPart;
import org.apache.wink.common.model.multipart.InPart;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.cardinal.webordering.common.logging.MessageLoggerHelper;
import com.cardinal.ws.physicalinventory.common.InventoryConstants;
import com.cardinal.ws.physicalinventory.common.InventoryExceptionMapper;
import com.cardinal.ws.physicalinventory.common.InventoryUtility;
import com.cardinal.ws.physicalinventory.locationdetail.valueobjects.InvImportRequest;
import com.cardinal.ws.physicalinventory.locationdetail.valueobjects.InvImportResponse;
import com.cardinal.ws.rest.physicalinventory.bu.InvImportProcessor;
import com.cardinal.ws.rest.physicalinventory.bu.InvImportValidator;

/**
 * This class is the entry point for the Import Service. This class first interacts with
 * ImportValidator class for validation of request parameter then interacts with
 * ImportProcessor class.
 *
 * @author Karthigeyan.nagaraja
 * @version 1.0
 */
@Path(value = "/PhyImport")
public class ImportResource {

	private static final String CLASS_NAME = ImportResource.class.getCanonicalName();
    private static final Logger LOGGER = MessageLoggerHelper.getLogger(CLASS_NAME);
    
    
    /**
     * The receiveimportedItemsMultipart method will be responsible to import the items from the
     * file.
     * 
     * @param headers
     * @param request
     *
     * @return the JSON Response.
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_HTML)
    @Path(value = "addImportedFileItems")
    public Response addImportedFileItems(@Context
    HttpHeaders headers, InMultiPart imp) {
        final String methodName = "addImportedFileItems";

        if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
            LOGGER.entering(CLASS_NAME, methodName);
        }

        Response.ResponseBuilder builder = Response.ok();

        String regex = "(?<=\\bname=\")([^\"]*?)(?=\")";
        Pattern pattern = Pattern.compile(regex);
        String regex2 = "(?<=\\bfilename=\")([^\"]*?)(?=\")";
        Pattern pattern2 = Pattern.compile(regex2);
        InvImportRequest request = new InvImportRequest();
        ObjectMapper mapper = new ObjectMapper();
        // suppress nulls and defaults in POJOs
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

        String file = null;
        String fileName = null;

        try {
            while (imp.hasNext()) {
                InPart part = imp.next();
                MultivaluedMap<String, String> partHeaders = part.getHeaders();
                String formData = partHeaders.getFirst("Content-Disposition");
                Matcher matcher = pattern.matcher(formData);
                String formField = null;

                if (matcher.find()) {
                    formField = matcher.group(1);
                    if ("importFile".equalsIgnoreCase(formField)) {
                    	
                        file = part.getBody(String.class, null);
                        if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
                            LOGGER.finer("File: " + file);
                        }

                        Matcher matcher2 = pattern2.matcher(formData);

                        if (matcher2.find()) {
                            fileName = InventoryUtility.getFileName(matcher2.group(1));
                        }

                        if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
                            LOGGER.finer("FileName : " + fileName);
                        }
                    }
                    else if ("json".equalsIgnoreCase(formField)) {
                        String input = part.getBody(String.class, null);
                        if (MessageLoggerHelper.isTraceEnabled(LOGGER)) { //NOPMD Irfan.Mohammed 04/13/2012
                            LOGGER.finer("JSON: " + input);
                        }

                        request = mapper.readValue(input.getBytes(), InvImportRequest.class);
                    }
                }
            }

            if (file != null) {
                request.setImportFile(file.getBytes());
            }

            request.setFileName(fileName);
            if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
                LOGGER.finer("Input: " + request);
            }

       	 InvImportValidator.validateImportRequest(request);
       	 //InvImportValidator.validateImportRequestContentType(request, contentType);
         InvImportProcessor processor = new InvImportProcessor();
       	 InvImportResponse response = processor.addimportedItems(request);
         
         String output =
             "<html><body><textarea>" + mapper.writeValueAsString(response)
             + "</textarea></body></html>";
         builder = Response.ok(output, MediaType.TEXT_HTML);
     	
        }
        catch (Throwable t) {
            if (request != null) {
            	HashMap<String, String> source = InventoryExceptionMapper.getSource(t);
    			LOGGER.log(Level.SEVERE,
    					 MessageLoggerHelper.buildErrorMessage(request.getDistributionCenter() +
    							 InventoryConstants.DASH + request.getShipToCustomer(),
    							 request.getUserId(), request.getTransactionId(),
    							 InventoryExceptionMapper.getAlertMessage(t).get(InventoryConstants.ERRORCODE),
    							 InventoryExceptionMapper.getMessage(t), null, null,
    							 source.get(InventoryConstants.CLASS_NAME),
    							 source.get(InventoryConstants.METHOD_NAME)), InventoryExceptionMapper.getThrowable(t));

            }
            InventoryUtility.logAlertMessage(t);
            builder = InventoryExceptionMapper.handleException(t);
        }
     
        if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
        	LOGGER.finer("Output for addImportedFileItems: " + builder.build().getEntity());
			LOGGER.exiting(CLASS_NAME, methodName);
		}
        return builder.build();
    }
    
   

}
