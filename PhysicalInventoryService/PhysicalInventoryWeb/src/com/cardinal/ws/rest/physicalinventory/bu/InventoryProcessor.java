/*********************************************************************
 *
 * $Workfile: OEPhysicalInvProcessor.java $
 * Copyright 2013 Cardinal Health
 *
 *********************************************************************
 *
 * Revision History:
 *
 * $Log: com/cardinal/ws/rest/physicalinventory/bu/physicalinventory $
 * Modified By        Date             Clarify#        Description of the change
 * Vignesh Kandadi    07/23/2013                       Category Code mapping corrected
 * Vignesh Kandadi	  04/05/2014	  ALM#14444 	 	Updated UOIF data field from Integer to Big Decimal
 * Ankit Mahajan	  04/29/2015						Changes made in Agile 15.4 to make direct DB call to retrieve 
 * 													   product details instead of relying in the Product Details EJB
 * 			
 ***********************************************************************************************/
package com.cardinal.ws.rest.physicalinventory.bu;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.naming.NamingException;

import com.cardinal.webordering.common.errorhandling.BusinessException;
import com.cardinal.webordering.common.errorhandling.ErrorHandlingHelper;
import com.cardinal.webordering.common.errorhandling.SystemException;
import com.cardinal.webordering.common.logging.AlertLevel;
import com.cardinal.webordering.common.logging.AlertType;
import com.cardinal.webordering.common.logging.ApplicationName;
import com.cardinal.webordering.common.logging.MessageLoggerHelper;
import com.cardinal.webordering.common.productdetails.ProductDetailsResponse;
import com.cardinal.ws.physicalinventory.common.DynamicComparer;
import com.cardinal.ws.physicalinventory.common.InventoryConstants;
import com.cardinal.ws.physicalinventory.common.InventoryPagination;
import com.cardinal.ws.physicalinventory.common.InventoryUtility;
import com.cardinal.ws.physicalinventory.common.valueobjects.InvInventory;
import com.cardinal.ws.physicalinventory.common.valueobjects.InvLocation;
import com.cardinal.ws.physicalinventory.inventory.valueobjects.InvInventoryRequest;
import com.cardinal.ws.physicalinventory.inventory.valueobjects.InvInventoryResponse;
import com.cardinal.ws.physicalinventory.location.valueobjects.InvLocationRequest;
import com.cardinal.ws.physicalinventory.location.valueobjects.InvLocationResponse;
import com.cardinal.ws.physicalinventory.locationdetail.valueobjects.InvLocationDetailRequest;
import com.cardinal.ws.physicalinventory.locationdetail.valueobjects.InvLocationDetailResponse;
import com.cardinal.ws.physicalinventory.product.valueobjects.InvLocProductResponse;
import com.cardinal.ws.physicalinventory.product.valueobjects.InvLocationProductCost;
import com.cardinal.ws.rest.physicalinventory.bu.router.InventoryRouter;
import com.cardinal.ws.rest.physicalinventory.bu.router.ProductDetailsRouter;


public class InventoryProcessor {
	
	private static final String UD = "UD";
	private static final String UU = "UU";
	private static final String UNIT_OF_USE = "UNIT OF USE";
	private static final String UNIT_DOSE = "UNIT DOSE";
	private static final String GEN_REBATE = "GEN";
	private static final String LDR_REBATE = "LDR";
	private static final String YES = "Y";
	private static final String CLASS_NAME = InventoryProcessor.class.getCanonicalName();
	private static final Logger LOGGER = MessageLoggerHelper.getLogger(CLASS_NAME);
	
		
	/**
	 * This processor method for the getInventorySummary operation.  Sorting and pagination of 
	 * results occur after retrieving the response from the router class
	 * 
	 * @param request the InvInventoryRequest request
	 * @return returns an InvInventoryResponse response
	 * @throws NamingException
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public InvInventoryResponse getInventorySummary(final InvInventoryRequest request) 
				throws NamingException, BusinessException, SystemException {
		final String methodName = "getInventorySummary";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}

		final InventoryRouter router = new InventoryRouter();
//		DistributedMap objectCache = null;
		InvInventoryResponse response = null;
		response = router.getInventorySummary(request);
        	
//		if (null != response) {
//			objectCache.put(request.getUserId() + "_Inventory_" + request.getInventory().getInventoryId(), response);
//		}

        
       // Sorting Logic
       if(null != response && null != response.getInventories() && !response.getInventories().isEmpty()){
        	if(null != request.getSortColumns() && !request.getSortColumns().isEmpty()){
        		StringBuffer sortParams=null;
        		
        		if(null !=request.getSortColumns().get(0)){
        			sortParams = new StringBuffer();
        			sortParams.append(request.getSortColumns().get(0)+" "+request.getSortOrder());
        		}
        		if(request.getSortColumns().size() > 1 && null != request.getSortColumns().get(1)){
        			sortParams.append(InventoryConstants.EMPTY_STRING);
        			sortParams.append(request.getSortColumns().get(1)+" "+request.getSortOrder());
        		}
        		DynamicComparer d = new DynamicComparer(InvInventory.class, sortParams.toString());  
        		Collections.sort(response.getInventories(), d);  
        	}
        	else{
        		DynamicComparer d = new DynamicComparer(InvInventory.class,InventoryConstants.INVENTORY_SUMMARY_DEFAULT_SORT);  
        		Collections.sort(response.getInventories(), d);  
        	}//if
        }//if			
       
       if(request.getRecordsPerPage() != 0 && request.getPageNum() !=0 && 
    		   response.getInventories() != null && !response.getInventories().isEmpty()){
       	
   		InventoryPagination invSumPage = new InventoryPagination(new ArrayList<Object>(response.getInventories()), request.getRecordsPerPage());
   		
   		response.setInventories(new ArrayList<InvInventory>(invSumPage.getPageDetails(request.getPageNum()).getContentList()));
   		response.setPageNum(request.getPageNum());
   		response.setNumberOfPages(invSumPage.getPageDetails(request.getPageNum()).getTotalPage());
   		
   	}
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to resource from processor for createInventory------>" +  response);
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		return response;
	}

	/**
	 * This processor method for the getLocationSummary operation.  Sorting and pagination of 
	 * results occur after retrieving the response from the router class
	 * 
	 * @param request the InvLocationRequest req
	 * @return returns an InvLocationResponse resp
	 * @throws NamingException
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public InvLocationResponse getLocationSummary(InvLocationRequest request)
			throws NamingException, BusinessException, SystemException {
		final String methodName = "getLocationSummary";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}


		InventoryRouter router = new InventoryRouter();
//		DistributedMap objectCache = null;
		InvLocationResponse response = null;
		response = router.getLocationSummary(request);
		
		if(null != response && null != response.getLocations() && !response.getLocations().isEmpty()){
        	if(null != request.getSortColumns() && !request.getSortColumns().isEmpty()){
        		StringBuffer sortParams=null;
        		
        		if(null !=request.getSortColumns().get(0)){
        			sortParams = new StringBuffer();
        			sortParams.append(request.getSortColumns().get(0)+" "+request.getSortOrder());
        		}
        		if(request.getSortColumns().size() > 1 && null != request.getSortColumns().get(1)){
        			sortParams.append(InventoryConstants.EMPTY_STRING);
        			sortParams.append(request.getSortColumns().get(1)+" "+request.getSortOrder());
        		}
        		DynamicComparer d = new DynamicComparer(InvLocation.class, sortParams.toString());  
        		Collections.sort(response.getLocations(), d);  
        	}
        	else{
        		DynamicComparer d = new DynamicComparer(InvLocation.class,InventoryConstants.INVENTORY_LOC_SUMMARY_DEFAULT_SORT);  
        		Collections.sort(response.getLocations(), d);  
        	}//if
        }//if			
       
       if(request.getRecordsPerPage() != 0 && request.getPageNum() !=0 && 
    		   response.getLocations() != null && !response.getLocations().isEmpty()){
       	
   		InventoryPagination invSumPage = new InventoryPagination(new ArrayList<Object>(response.getLocations()), request.getRecordsPerPage());
   		
   		response.setLocations(new ArrayList<InvLocation>(invSumPage.getPageDetails(request.getPageNum()).getContentList()));
   		response.setPageNum(request.getPageNum());
   		response.setNumberOfPages(invSumPage.getPageDetails(request.getPageNum()).getTotalPage());
   		
   	}
	
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to resource from processor for getLocationSummary------>" +  response);
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		return response;
	}

	/**
	 * This method takes request instance of InvInventoryRequest and creates new Invenotry
	 * @param request
	 * @return
	 */
	public InvInventoryResponse createInventory(InvInventoryRequest request) 
				throws BusinessException, SystemException {
		final String methodName = "createInventory";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}

		final InventoryRouter router = new InventoryRouter();
		InvInventoryResponse response = null;
		response = router.createInventory(request);
        	
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to resource from processor for createInventory------>" +  response);
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		return response;
	}
	
	/**
	 * This method takes request instance of InvInventoryRequest and creates new Inventory
	 * @param request
	 * @return
	 */
		public InvInventoryResponse updateInventory(InvInventoryRequest request) {
			final String methodName = "updateInventory";
			
			if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
				LOGGER.entering(CLASS_NAME, methodName);
			}

			final InventoryRouter router = new InventoryRouter();
			InvInventoryResponse response = null;
			response = router.updateInventory(request);
	        	
			
			if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
				LOGGER.finer("Response sent to resource from processor for createInventory------>" +  response);
				LOGGER.exiting(CLASS_NAME, methodName);
			}
			return response;
		}
		
		/**
		 * This method takes request instance of InvLocationRequest which calls router Inventory location information.
		 * @param request
		 * @return
		 */
		public InvLocationResponse updateLocation(InvLocationRequest request) {
			final String methodName = "updateLocation";

			if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
				LOGGER.entering(CLASS_NAME, methodName);
			}

			final InventoryRouter router = new InventoryRouter();
			InvLocationResponse response = null;
			response = router.updateLocation(request);


			if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
				LOGGER.finer("Response sent to resource from processor for updateLocation------>" +  response);
				LOGGER.exiting(CLASS_NAME, methodName);
			}
			return response;
		}

		/**
		 * This method takes request instance of InvLocationRequest which calls router Inventory locaton information.
		 * @param request
		 * @return
		 */
		public InvLocationResponse updateLocationStatus(InvLocationRequest request) {
			final String methodName = "updateLocation";

			if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
				LOGGER.entering(CLASS_NAME, methodName);
			}

			final InventoryRouter router = new InventoryRouter();
			InvLocationResponse response = null;
			response = router.updateLocationStatus(request);


			if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
				LOGGER.finer("Response sent to resource from processor for updateLocation------>" +  response);
				LOGGER.exiting(CLASS_NAME, methodName);
			}
			return response;
		}
		
		/**
		 * This method creates a new location from the InvLocationRequest request object
		 * 
		 * @param request an InvLocationRequest object
		 * @return returns an InvLocationResponse object
		 */
		public InvLocationResponse createLocation(InvLocationRequest request) {
			final String methodName = "createLocation";

			if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
				LOGGER.entering(CLASS_NAME, methodName);
			}

			InventoryRouter router = new InventoryRouter();
			InvLocationResponse response = null;
			response = router.createLocation(request);


			if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
				LOGGER.finer("Response sent to resource from processor for createLocation------>" +  response);
				LOGGER.exiting(CLASS_NAME, methodName);
			}
			return response;
		}

		/**
		 * This method creates new location details from the InvLocationDetailRequest request
		 * 
		 * @param request the InvLocationDetailRequest request obj
		 * @return returns an InvLocationDetailResponse response obj
		 * @throws SystemException
		 * @throws BusinessException
		 * @throws IOException
		 */
		public InvLocationDetailResponse createLocationDetail(InvLocationDetailRequest request) throws SystemException, BusinessException, IOException {
			final String methodName = "createLocationDetail";

			if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
				LOGGER.entering(CLASS_NAME, methodName);
			}

			InventoryRouter router = new InventoryRouter();
//			DistributedMap objectCache = null;
			InvLocationDetailResponse response = null;
			response = router.createLocationDetail(request);
			if(null != response && null != response.getInvLocProducts() && !response.getInvLocProducts().isEmpty()){
				final List<String> productsList=new ArrayList<String>();
				InvLocProductResponse productResponse =null;
				int locSize=response.getInvLocProducts().size();
				for(int i=0;i<locSize;i++){
					productResponse =(InvLocProductResponse)response.getInvLocProducts().get(i);

					if(InventoryUtility.isNumericAndNotNull(productResponse.getProduct()) && 
							(productResponse.getProduct().length()==8 || productResponse.getProduct().length()==7)){
						productsList.add(productResponse.getProduct());
					}
				}

				if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
					LOGGER.fine("Products List:::"+productsList);
				}
				
				final ProductDetailsRouter proRouter = new ProductDetailsRouter();
				Map<String, ProductDetailsResponse> prdMap = null;
				
				
				prdMap=proRouter.getProductDetails(productsList,request);
				
				try {
				if(prdMap!=null && !prdMap.isEmpty()){
					response.setInvLocProducts(updateLocLineProductResponse(response,prdMap,request));
					
				}
				}catch(Throwable thr) {					
					LOGGER.warning(thr.getMessage());
				}
			}

			//Column Customization Logic
			final List<InvLocProductResponse> clonedList = new ArrayList<InvLocProductResponse>();
			InvLocProductResponse clonedLstDetail ;

			if (null != response.getInvLocProducts() && !response.getInvLocProducts().isEmpty() &&
					null !=request.getColumnNames() && !request.getColumnNames().isEmpty()) {		

				for(Iterator i = response.getInvLocProducts().iterator();i.hasNext();){
					clonedLstDetail= new InvLocProductResponse();
					InvLocProductResponse lstDetail = (InvLocProductResponse) i.next();
					if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
						LOGGER.fine("lstDetail.getNdcUpc()="+lstDetail.getNdcUpc());
					}
					clonedLstDetail=(InvLocProductResponse) InventoryUtility.setValuesThroughReflection(lstDetail, request.getColumnNames());
					
					if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
						LOGGER.fine("clonedLstDetail.getNdcUpc():::"+clonedLstDetail.getNdcUpc());
					}
					clonedList.add(clonedLstDetail);
				}
				response.setInvLocProducts(clonedList);

			}


			if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
				LOGGER.finer("Response sent to resource from processor for createLocation------>" +  response);
				LOGGER.exiting(CLASS_NAME, methodName);
			}
			return response;
		}
		
		/**
		 * This method retrieves the details of a location
		 * 
		 * @param request the InvLocationDetailRequest request obj
		 * @return returns an InvLocationDetailResponse response obj
		 * @throws ParseException
		 * @throws NamingException
		 * @throws SystemException
		 * @throws BusinessException
		 * @throws IOException 
		 */
	public InvLocationDetailResponse getLocationDetails(InvLocationDetailRequest request) throws ParseException, NamingException, SystemException, BusinessException, IOException {
		final String methodName = "getLocationDetails";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}

		InvLocationDetailResponse response = null;

		final InventoryRouter invRouter = new InventoryRouter();
		
		response = invRouter.getLocationDetails(request);
		
		if(null != response && null != response.getInvLocProducts() && 
				!response.getInvLocProducts().isEmpty()) {
			final List<String> productsList=new ArrayList<String>();
			InvLocProductResponse productResponse =null;
			int locSize=response.getInvLocProducts().size();

			for(int i=0;i<locSize;i++){
				productResponse =(InvLocProductResponse)response.getInvLocProducts().get(i);

				if(InventoryUtility.isNumericAndNotNull(productResponse.getProduct()) && 
						(productResponse.getProduct().length()==8 || 
								productResponse.getProduct().length()==7)) {
					
					productsList.add(productResponse.getProduct());
				}
			}

			if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
				LOGGER.fine("Products List:::"+productsList);
			}
			final ProductDetailsRouter proRouter = new ProductDetailsRouter();
			Map<String, ProductDetailsResponse> prdMap = null;
			
			try {
				prdMap = proRouter.getProductDetails(productsList,request);
				
			} catch(Throwable t) {
				MessageLoggerHelper.alert(ApplicationName.CUSTOM, "SY13810", AlertType.SYSTEM, AlertLevel.CRITICAL,
                "Phys Inventory: Product Details service unavailable.");
				LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
						request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
						ErrorHandlingHelper.getErrorCodeByErrorKey(
								InventoryConstants.ErrorKeys.PHYS_INV_PROD_DETAILS_UNAVAILABLE),
						InventoryConstants.ErrorKeys.PHYS_INV_PROD_DETAILS_UNAVAILABLE_DESC));
				response.setStatusCode("NoProdDetails");
				response.setStatusMsg("Product Details service unavailable");
			}
			
			if(prdMap!=null && !prdMap.isEmpty()){
				response.setInvLocProducts(updateLocLineProductResponse(response,prdMap,request));
			}
			
			// Sorting Logic
			if(null != request.getSortColumns() && !request.getSortColumns().isEmpty()){
				StringBuffer sortParams=null;

				if(null !=request.getSortColumns().get(0)){
					sortParams = new StringBuffer();
					sortParams.append(request.getSortColumns().get(0)+" "+request.getSortOrder());
				}
				if(request.getSortColumns().size() > 1 && null != request.getSortColumns().get(1)){
					sortParams.append(InventoryConstants.COMMA);
					sortParams.append(request.getSortColumns().get(1)+" "+request.getSortOrder());
				}
				DynamicComparer d = new DynamicComparer(InvLocProductResponse.class, sortParams.toString());  
				Collections.sort(response.getInvLocProducts(), d);  
			}
			else{
				if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
					LOGGER.finest("Going into default Sort------>");
				}
				DynamicComparer d = new DynamicComparer(InvLocProductResponse.class, 
						InventoryConstants.INVENTORY_LOC_DETAIL_DEFAULT_SORT);  
				Collections.sort(response.getInvLocProducts(), d);  
			}//if


			//Column Customization Logic
			List<InvLocProductResponse> clonedList = new ArrayList<InvLocProductResponse>();
			InvLocProductResponse clonedLstDetail ;

			if (null != response.getInvLocProducts() && !response.getInvLocProducts().isEmpty() 
					&& null !=request.getColumnNames() && !request.getColumnNames().isEmpty()) {		

				for(Iterator i = response.getInvLocProducts().iterator();i.hasNext();){
					clonedLstDetail= new InvLocProductResponse();
					InvLocProductResponse lstDetail = (InvLocProductResponse) i.next();
					clonedLstDetail=(InvLocProductResponse) InventoryUtility.setValuesThroughReflection(lstDetail, request.getColumnNames());
					clonedList.add(clonedLstDetail);
				}
				response.setInvLocProducts(clonedList);
			}

			// pagination logic - only non-mobile requests
			if(!request.isMobileRequest() && request.getRecordsPerPage() != 0 && request.getPageNum() !=0 
					&& response.getInvLocProducts() != null && !response.getInvLocProducts().isEmpty() && response.getInvLocProducts().size()>request.getRecordsPerPage()){
				LOGGER.finest("Manual Pagination");
				InventoryPagination invLocProds = new InventoryPagination(
						new ArrayList<Object>(response.getInvLocProducts()), 
						request.getRecordsPerPage());

				response.setInvLocProducts(new ArrayList<InvLocProductResponse>(
						invLocProds.getPageDetails(request.getPageNum()).getContentList()));
				response.setPageNum(request.getPageNum());
				response.setNumberOfPages(invLocProds.getPageDetails(request.getPageNum()).getTotalPage());

			}
		}
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finest("Response sent to router from processor for getLocationDetails------>" +  response);
		}
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, methodName);
		}
        
        return response;
	}


	
//	private DistributedMap getLocationDetailCache(String locationDetailCacheJndi) {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	private DistributedMap getLocationCache(String locationContext) throws NamingException {
//		DistributedMap cache = null;
//		InitialContext context = new InitialContext();
//		cache = (DistributedMap) context.lookup(locationContext);
//		return cache;
//	}


	public InvLocationResponse closeLocations(InvLocationRequest request) {
		final String methodName = "closeLocations";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}

//		InventoryRouter router = new InventoryRouter();
//		DistributedMap cache = null;
		InvLocationResponse response = null;
		
		//for each of the request.getLocs(), open the location and update each detail line to set the Affixed Cost and Affixed UOIF 
		//to the appropriate setting, then update the location to set the affixed date to now and set the CostType to request.getCostType() 
		//as well as calculating the Locations total cost from the detail lines affixed costs.  Lastly, update the Inventories Total Cost 
		//with the newly calculated location cost. 
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to resource from processor for getLocationSummary------>" +  response);
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		return response;
	}	


	/**
	 * This method copies the location in the InvLocationRequest request object
	 * 
	 * @param request the InvLocationRequest request object
	 * @return returns the InvLocationResponse response object
	 */
	public InvLocationResponse copyLocations(InvLocationRequest request) {
		final String methodName = "copyLocations";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}

		InventoryRouter router = new InventoryRouter();
		InvLocationResponse response = null;
		response = router.copyLocations(request);

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to resource from processor for getLocationSummary------>" +  response);
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		return response;
	}

	/**
	 * This method deletes one or more location details
	 * 
	 * @param request the InvLocationDetailRequest request object
	 * @return returns an InvLocationDetailResponse response object
	 * @throws BusinessException
	 * @throws SystemException
	 */
	public InvLocationDetailResponse deleteLocationDetails(InvLocationDetailRequest request) 
				throws BusinessException, SystemException {
		final String methodName = "deleteLocationDetails";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}

		InventoryRouter router = new InventoryRouter();
//		DistributedMap objectCache = null;
		InvLocationDetailResponse response = null;
		response = router.deleteLocationDetails(request);
        	
//		if (null != response) {
//			objectCache.put(request.getUserId() + "_Location_" + response.getLocations().get(0).getLocationId(), response);
//		}

//      Filter by Status
//      if (null != request.getStatusList() && !request.getStatusList().isEmpty() && null != response && null != response.getProductList() && !response.getProductList().isEmpty()) {
//    	  for (Iterator i= response.getProductList().iterator(); i.hasNext();) {
//    		  RcvProduct product = (RcvProduct) i.next();
//    		  if (!request.getStatusList().contains(product.getRcvStatusCode())) {
//    			  i.remove();
//    		  }
//    	  }
//      }
        
//        // Sorting Logic
//        if(null != response && null != response.getProductList() && !response.getProductList().isEmpty()){
//        	if(null != request.getSortColumns() && !request.getSortColumns().isEmpty()){
//        		StringBuffer sortParams=null;
//        		
//        		if(null !=request.getSortColumns().get(0)){
//        			sortParams = new StringBuffer();
//        			sortParams.append(request.getSortColumns().get(0)+" "+request.getSortOrder());
//        		}
//        		if(request.getSortColumns().size() > 1 && null != request.getSortColumns().get(1)){
//        			sortParams.append(ReceivingConstants.EMPTY_STRING);
//        			sortParams.append(request.getSortColumns().get(1)+" "+request.getSortOrder());
//        		}
//        		DynamicComparer d = new DynamicComparer(RcvProduct.class, sortParams.toString());  
//        		Collections.sort(response.getProductList(), d);  
//        	}
//        	else{
//        		DynamicComparer d = new DynamicComparer(RcvProduct.class, ReceivingConstants.CONTDETAILS_DEFAULT_SORT);  
//        		Collections.sort(response.getProductList(), d);  
//        	}//if
//        }//if			
		
//        //Column Customization Logic
//		List<RcvProduct> clonedList = new ArrayList<RcvProduct>();
//		RcvProduct clonedPrd ;
//		
//		if (null != response.getProductList() && !response.getProductList().isEmpty() &&
//				null !=request.getColumnNames() && !request.getColumnNames().isEmpty()) {		
//		    
//			for(Iterator i = response.getProductList().iterator();i.hasNext();){
//				clonedPrd= new RcvProduct();
//				RcvProduct prd = (RcvProduct) i.next();
//				clonedPrd=(RcvProduct) ReceivingUtility.setValuesThroughReflection(prd, request.getColumnNames());
//				clonedList.add(clonedPrd);
//			}
//			response.setProductList(clonedList);
//		}
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to resource from processor for deleteLocationDetails------>" +  response);
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		return response;
	}
	
	/**
	 * This method deletes an entire location
	 * 
	 * @param request the InvLocationRequest request object
	 * @return returns an InvLocationResponse response object
	 * 
	 * @throws Exception
	 */
	public InvLocationResponse deleteLocations(InvLocationRequest request) {
		final String methodName = "deleteLocations";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}

		InventoryRouter router = new InventoryRouter();
		InvLocationResponse response = null;
		response = router.deleteLocations(request);
        	
		if(null != response && null != response.getLocations() && !response.getLocations().isEmpty()){
        	if(null != request.getSortColumns() && !request.getSortColumns().isEmpty()){
        		StringBuffer sortParams=null;
        		
        		if(null !=request.getSortColumns().get(0)){
        			sortParams = new StringBuffer();
        			sortParams.append(request.getSortColumns().get(0)+" "+request.getSortOrder());
        		}
        		if(request.getSortColumns().size() > 1 && null != request.getSortColumns().get(1)){
        			sortParams.append(InventoryConstants.EMPTY_STRING);
        			sortParams.append(request.getSortColumns().get(1)+" "+request.getSortOrder());
        		}
        		DynamicComparer d = new DynamicComparer(InvLocation.class, sortParams.toString());  
        		Collections.sort(response.getLocations(), d);  
        	}
        	else{
        		DynamicComparer d = new DynamicComparer(InvLocation.class,InventoryConstants.INVENTORY_LOC_SUMMARY_DEFAULT_SORT);  
        		Collections.sort(response.getLocations(), d);  
        	}//if
        }//if			
       
       if(request.getRecordsPerPage() != 0 && request.getPageNum() !=0 
    		   && response.getLocations() != null && !response.getLocations().isEmpty()){
       	
   		InventoryPagination invPage = new InventoryPagination(new ArrayList<Object>(response.getLocations()), request.getRecordsPerPage());

   		response.setLocations(new ArrayList<InvLocation>(invPage.getPageDetails(request.getPageNum()).getContentList()));
   		response.setPageNum(request.getPageNum());
   		response.setNumberOfPages(invPage.getPageDetails(request.getPageNum()).getTotalPage());

       }

       if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
    	   LOGGER.finer("Response sent to resource from processor for deleteLocations------>" +  response);
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		return response;
	}

	/**
	 * This method deletes an inventory
	 * @param request
	 * @return
	 */
	public InvInventoryResponse deleteInventories(InvInventoryRequest request) {
		final String methodName = "deleteInventories";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}

		InventoryRouter router = new InventoryRouter();
//		DistributedMap objectCache = null;
		InvInventoryResponse response = null;
		response = router.deleteInventories(request);
        	
        
        // Sorting Logic
        if(null != response && null != response.getInventories() && !response.getInventories().isEmpty()){
        	if(null != request.getSortColumns() && !request.getSortColumns().isEmpty()){
        		StringBuffer sortParams=null;
        		
        		if(null !=request.getSortColumns().get(0)){
        			sortParams = new StringBuffer();
        			sortParams.append(request.getSortColumns().get(0)+" "+request.getSortOrder());
        		}
        		if(request.getSortColumns().size() > 1 && null != request.getSortColumns().get(1)){
        			sortParams.append(InventoryConstants.EMPTY_STRING);
        			sortParams.append(request.getSortColumns().get(1)+" "+request.getSortOrder());
        		}
        		DynamicComparer d = new DynamicComparer(InvInventory.class, sortParams.toString());  
        		Collections.sort(response.getInventories(), d);  
        	}
        	else{
        		DynamicComparer d = new DynamicComparer(InvInventory.class, InventoryConstants.INVENTORY_SUMMARY_DEFAULT_SORT);  
        		Collections.sort(response.getInventories(), d);  
        	}//if
        }//if			
		
        if(request.getRecordsPerPage() != 0 && request.getPageNum() !=0 
        		&& response.getInventories() != null && !response.getInventories().isEmpty()){
        	
    		InventoryPagination invSumPage = new InventoryPagination(new ArrayList<Object>(response.getInventories()), request.getRecordsPerPage());
    		
    		response.setInventories(new ArrayList<InvInventory>(invSumPage.getPageDetails(request.getPageNum()).getContentList()));
    		response.setPageNum(request.getPageNum());
    		response.setNumberOfPages(invSumPage.getPageDetails(request.getPageNum()).getTotalPage());
    		
    	}
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to resource from processor for deleteInventories------>" +  response);
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		return response;
	}	

	
	/**
	 * This method reorders a location's details
	 * @param request
	 * @return
	 * @throws ParseException
	 */
	public InvLocationDetailResponse reOrderLocationDetails(InvLocationDetailRequest request) throws ParseException {
		final String methodName = "reOrderLocationDetails";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}

		InventoryRouter router = new InventoryRouter();
		InvLocationDetailResponse response = null;
		response = router.reOrderLocationDetails(request);

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to resource from processor for updateLocationDetails------>" +  response);
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		return response;
		}
	
/**
 * This method takes response of Location Details and prdMap map of ProductDetailsResponse.
 * Map each product back to location details response. then return List of InvLocProductResponse objects
 * 
 * @param response
 * @param prdMap
 * @return
 */
private List<InvLocProductResponse> updateLocLineProductResponse(final InvLocationDetailResponse response,
		final Map<String,ProductDetailsResponse> prdMap,InvLocationDetailRequest invLocationDetailRequest){
	
	final String methodName = "updateLocLineProductResponse";
	if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}
	List<InvLocProductResponse> updatedLocLinesList=null;
	if(response.getInvLocProducts()!=null && !response.getInvLocProducts().isEmpty()){
		updatedLocLinesList=new ArrayList<InvLocProductResponse>();
	
		int lineSize=response.getInvLocProducts().size();
	  InvLocProductResponse invLocProduct=null;
	  ProductDetailsResponse productDetResponse=null;
	 /** final ProductCustomFields productCustomFields = new ProductCustomFields();
      final CodeIdDescription codeIdDescription1 = new CodeIdDescription();
      final CodeIdDescription codeIdDescription2 = new CodeIdDescription();
      */
	  
	 for(int i=0;i<lineSize;i++){
		 Double uoif=0.00;
		 invLocProduct =(InvLocProductResponse)response.getInvLocProducts().get(i);
		 
       	  if(prdMap!=null && invLocProduct.getProduct()!=null && prdMap.containsKey(invLocProduct.getProduct())){     		  
       		
       	 
       		productDetResponse=(ProductDetailsResponse)prdMap.get(invLocProduct.getProduct());
       		if(productDetResponse!=null){
       			invLocProduct.setTradeName(productDetResponse.getTradeName());
       			
       			
       			invLocProduct.setGenericName(productDetResponse.getGenericName());
       			invLocProduct.setAbcRanking(productDetResponse.getAbcRanking());

       	        invLocProduct.setAbRating(productDetResponse.getAbRating());

       	        if ((null != productDetResponse.getAhfsCode()) &&
       	                !productDetResponse.getAhfsCode().isEmpty()) {
       	            invLocProduct.setAhfsCode(Long.valueOf(
       	                    productDetResponse.getAhfsCode()));
       	        }

       	        invLocProduct.setAhfsDesc(productDetResponse.getAhfsDesc());

       	        invLocProduct.setAmu(productDetResponse.getAmu());

       	        invLocProduct.setAhfsDesc(productDetResponse.getAhfsDesc());

       	        invLocProduct.setAltProductContractFlag(productDetResponse.getAltProductContractFlag());

       	        invLocProduct.setAltProductFlag(productDetResponse.getAltProductFlag());
       	        invLocProduct.setAmp(productDetResponse.getAmp());

       	        invLocProduct.setAvailabilityAlert(productDetResponse.getAvailabilityAlert());

       	        invLocProduct.setAvailabilityAlertAbbrv(productDetResponse.getAvailabilityAlertAbbrv());

       	        invLocProduct.setAvailableMessage(productDetResponse.getAvailableMessage());

       	        invLocProduct.setAwu(productDetResponse.getAwu());

       	        invLocProduct.setCallToOrder(productDetResponse.getCallToOrder());

       	        invLocProduct.setCardinalKey(productDetResponse.getCardinalKey());

       	        invLocProduct.setCardinalRefPrice(productDetResponse.getCardinalRefPrice());

       	        invLocProduct.setCaseQty(productDetResponse.getCaseQty());

       	        invLocProduct.setCatentryID(productDetResponse.getCatentryID());

       	        invLocProduct.setChangeDate(productDetResponse.getChangeDate());

       	        invLocProduct.setContract(productDetResponse.getContract());

       	        invLocProduct.setContractAlias(productDetResponse.getContractAlias());

       	        invLocProduct.setContractExpires(InventoryUtility.convertDateFromYYYYMMDD(productDetResponse.getContractExpires()));
//       	        invLocProduct.setContractExpires(productDetResponse.getContractExpires());
       	        
       	        invLocProduct.setContractGroup(productDetResponse.getContractGroup());

       	        invLocProduct.setContractGroupAffiliationNumber(productDetResponse.getContractGroupAffiliationNumber());

       	        invLocProduct.setContractGroupNumber(productDetResponse.getContractGroupNumber());

       	        invLocProduct.setContractRank(productDetResponse.getContractRank());

       	        invLocProduct.setCost(productDetResponse.getCost());

       	        invLocProduct.setCostLastChanged(productDetResponse.getCostLastChanged());

       	        invLocProduct.setCostLastPurchased(productDetResponse.getCostLastPurchased());

       	        invLocProduct.setCustomFields(productDetResponse.getCustomFields());

       	       invLocProduct.setDateLastPurchased(InventoryUtility.convertDateFromYYYYMMDD(productDetResponse.getDateLastPurchased()));
       	        invLocProduct.setDeaSchedulel(productDetResponse.getDeaSchedulel());

       	        invLocProduct.setDepartment(String.valueOf(
       	                productDetResponse.getDepartment()));

       	        invLocProduct.setDescription(productDetResponse.getDescription());

       	        invLocProduct.setDisplayNDC(productDetResponse.getDisplayNDC());

       	        invLocProduct.setDesiIndicator(productDetResponse.getDesiIndicator());

       	        invLocProduct.setEligible(productDetResponse.isEligible());

       	        invLocProduct.setEstimatedNetCost(productDetResponse.getEstimatedNetCost());

       	        invLocProduct.setEstimatedRebate(productDetResponse.getEstimatedRebate());

       	        invLocProduct.setExpireDeliveryDate(productDetResponse.getExpireDeliveryDate());

       	        invLocProduct.setFdbAdditionalDesc(productDetResponse.getFdbAdditionalDesc());

       	        invLocProduct.setFdbAWP(productDetResponse.getFdbAWP());

       	        invLocProduct.setFdbColor(productDetResponse.getFdbColor());

       	        invLocProduct.setFdbFlavorDesc(productDetResponse.getFdbFlavorDesc());

       	        invLocProduct.setFdbLabelName(productDetResponse.getFdbLabelName());

       	        invLocProduct.setFdbManuf(productDetResponse.getFdbManuf());

       	        invLocProduct.setFdbShape(productDetResponse.getFdbShape());

       	        invLocProduct.setFinelineDesc(productDetResponse.getFinelineDesc());

       	        invLocProduct.setForm(productDetResponse.getForm());

       	        invLocProduct.setFormDesc(productDetResponse.getFormDesc());

       	        invLocProduct.setGcn(productDetResponse.getGcn());

       	        invLocProduct.setGeneric(productDetResponse.getGeneric());

       	        invLocProduct.setGenericName(productDetResponse.getGenericName());
       	        if(!InventoryUtility.isCostZero(productDetResponse.getInvoiceCost())){
    	        	invLocProduct.setInvoiceCost(InventoryUtility.formatCost(productDetResponse.getInvoiceCost()));
    	        }else if(!InventoryUtility.isCostZero(productDetResponse.getCostLastPurchased())){
    	        	invLocProduct.setInvoiceCost(InventoryUtility.formatCost(productDetResponse.getCostLastPurchased()));
    	        }
       	    
       	       
       	        invLocProduct.setHammacherZoneSettings(String.valueOf(
       	                productDetResponse.getHammacherZoneSettings()));

       	        

       	        invLocProduct.setItemDesc(productDetResponse.getItemDesc());

       	        invLocProduct.setLabelSize(productDetResponse.getLabelSize());

       	        invLocProduct.setLastOrderedCost(productDetResponse.getLastOrderedCost());

       	        invLocProduct.setLastOrderedDate(productDetResponse.getOriginalDate());

       	        invLocProduct.setLastOrderedQty(productDetResponse.getLastOrderedQty());

       	        invLocProduct.setLongDesc(productDetResponse.getLongDesc());

       	        invLocProduct.setLongForm(productDetResponse.getLongForm());

       	        invLocProduct.setMarketingDesc(productDetResponse.getMarketingDesc());

       	        invLocProduct.setMedicaidJCode(productDetResponse.getMedicaidJCode());

       	        invLocProduct.setMedispanAWP(productDetResponse.getMedispanAWP());

       	        invLocProduct.setMedispanGPICode(productDetResponse.getMedispanGPICode());

       	        invLocProduct.setMfr(productDetResponse.getMfr());

       	        invLocProduct.setMfrAbbrv(productDetResponse.getMfrAbbrv());

       	        invLocProduct.setMfrPartNumber(productDetResponse.getMfrPartNumber());

       	        invLocProduct.setNdc(productDetResponse.getNdc());

       	        invLocProduct.setNewProductIndicator(productDetResponse.getNewProductIndicator());

       	        
       	        invLocProduct.setNonCardinal(productDetResponse.isNonCardinal());
       	         
       	        // if the cin is 8 digits, it's a non cardinal item
       	        //  data is not coming from product details
       	        if(invLocProduct.getProduct().length() == 8) {
    	        	invLocProduct.setNonCardinal(true);
    	        }
       	        
       	        invLocProduct.setNoOfImages(productDetResponse.getNoOfImages());

       	        invLocProduct.setOrangeBookCode(productDetResponse.getOrangeBookCode());

       	        invLocProduct.setOriginalDate(productDetResponse.getOriginalDate());

       	        invLocProduct.setPackageQty(productDetResponse.getPackageQty());

       	        invLocProduct.setPreviouslyPurchase(productDetResponse.getPreviouslyPurchase());

       	        invLocProduct.setPrimaryImageNumber(String.valueOf(
       	                productDetResponse.getPrimaryImageNumber()));

       	        if (null != productDetResponse.getCategoryCodeOne()) {
       	        	invLocProduct.setCategoryCode1(Integer.toString(
       	                    productDetResponse.getCategoryCodeOne().getCategoryCode()));

       	         invLocProduct.setCategoryDescription1(productDetResponse.getCategoryCodeOne()
       	                                                                .getCategoryDesc());
       	        }

       	        if (null != productDetResponse.getCategoryCodeTwo()) {
       	        	invLocProduct.setCategoryCode2(Integer.toString(
       	                    productDetResponse.getCategoryCodeTwo().getCategoryCode()));

       	        	invLocProduct.setCategoryDescription2(productDetResponse.getCategoryCodeTwo()
       	                                                                .getCategoryDesc());
       	        }

       	       
       	        if (null != productDetResponse.getPrdCustomAttribte()) {
       	        	invLocProduct.setCsn(productDetResponse.getPrdCustomAttribte()
       	                                                         .getCsn());

       	            if(!InventoryUtility.isNullOrEmptyWithNullValue(productDetResponse.getPrdCustomAttribte()
       	                                      .getMaxDaysOfSupply())){
       	            	invLocProduct.setMaxDaysOfSupply(String.valueOf(
       	                    productDetResponse.getPrdCustomAttribte()
       	                                      .getMaxDaysOfSupply()));
       	            }

       	         invLocProduct.setMessage(productDetResponse.getPrdCustomAttribte()
       	                                                             .getMessageText());

       	         if(!InventoryUtility.isNullOrEmptyWithNullValue(productDetResponse.getPrdCustomAttribte()
                            .getMinDaysOfSupply())){
       	        	invLocProduct.setMinDaysOfSupply(String.valueOf(
       	                    productDetResponse.getPrdCustomAttribte()
       	                                      .getMinDaysOfSupply()));
       	         }

       	         invLocProduct.setOnFormulary(productDetResponse.getPrdCustomAttribte()
       	        		 .getOnFormulary());
       	         invLocProduct.setProjectedUsage(productDetResponse.getPrdCustomAttribte()
       	        		 .getProjectedUsage());

       	         invLocProduct.setProjectedUsageExpirationDate(productDetResponse.getPrdCustomAttribte()
       	        		 .getProjectedUsageExpirationDate());

       	         invLocProduct.setUDF1(productDetResponse.getPrdCustomAttribte()
       	        		 .getUdf1());

       	         invLocProduct.setUDF2(productDetResponse.getPrdCustomAttribte()
       	        		 .getUdf2());

       	       
       	         if(productDetResponse.getPrdCustomAttribte()
       	        		 .getUoiFactor()!=null){
       	        	 uoif=productDetResponse.getPrdCustomAttribte()
       	        	 .getUoiFactor().doubleValue();
       	         }
       	        }

       	        //invLocProduct.setProductCustomFields(productCustomFields);

       	        invLocProduct.setProductImageIndicator(productDetResponse.getProductImageIndicator());
       	        

       	        invLocProduct.setProductType(productDetResponse.getProductType());

       	        invLocProduct.setQtyAvailable(productDetResponse.getQtyAvailable());

       	        invLocProduct.setQtyLastPurchased(productDetResponse.getQtyLastPurchased());

       	        
       	        
       	        

       	        invLocProduct.setReorderPoint(productDetResponse.getReorderPoint());

       	        invLocProduct.setReorderQuantity(productDetResponse.getReorderQuantity());

       	        invLocProduct.setRetailPrice(productDetResponse.getRetailPrice());

       	        invLocProduct.setRetailPriceOverride(productDetResponse.getRetailPriceOverride());

       	        if(InventoryUtility.isNullOrEmpty(productDetResponse.getReturnable())){
    	        	invLocProduct.setReturnable("N");
    	        }else{
    	        	invLocProduct.setReturnable(productDetResponse.getReturnable());
    	        }

       	        invLocProduct.setSalesRank(productDetResponse.getSalesRank());

       	        invLocProduct.setSellPrice(productDetResponse.getSellPrice());

       	        invLocProduct.setShortDesc(productDetResponse.getShortDesc());

       	        invLocProduct.setSize(productDetResponse.getSize());

       	        invLocProduct.setStockStatus(productDetResponse.getStockStatus());

       	        invLocProduct.setStockStatusAbbrv(productDetResponse.getStockStatusAbbrv());

       	        invLocProduct.setStockstatusDefinition(productDetResponse.getStockstatusDefinition());

       	        invLocProduct.setStrength(productDetResponse.getStrength());

       	        invLocProduct.setSuggestedOrderQty(productDetResponse.getSuggestedOrderQty());

       	       
       	        invLocProduct.setTotalSize(InventoryUtility.convertFloatToLong(productDetResponse.getTotalSize()));

       	        invLocProduct.setTradeName(productDetResponse.getTradeName());

       	        invLocProduct.setUnitDose(productDetResponse.getUnitDose());

       	        invLocProduct.setUnitofMeasure(productDetResponse.getUnitofMeasure());

       	        invLocProduct.setUnitOfMeasureAbbrv(productDetResponse.getUnitOfMeasureAbbrv());

       	        invLocProduct.setUnitofSale(productDetResponse.getUnitofSale());
       	        invLocProduct.setPackageSize(productDetResponse.getPackageSize());
       	     
       	     //{GCN_TXT=14263, ASSC_COLOR=#993300, MAJOR_UNIT_DESC=NOT AVAILABLE, LIST_PRICE=, UNIT_CDE=5, SRC_ITEM_ID=D0237, 
        	       //MAJOR_UNIT_ID=, UNIT_H_CDE=2, SHORT_DESC=RGDAPA, CUST_CNTR_RANK=3, DAYS_LEFT_FOR_EXP=2917074, CONTRACT_NAME=DAPA REGIONAL,
        	        //STRG_DESC=Controlled Room , DISCLAIMER=, SAME_DAY_RETAIL_FLG=, BARCODE_FLG=N, DISPENING_SIZE=1X500 EA, ACCOUNT_UOIF=1.0, CURR_CORP_NIFO_DOLLAR=78.15, CASE_SIZE=0, FORM_ID=TB, PRIVATE_LABEL_DESC=, UNIT_DOSE_CDE=1, PACK_SIZE_QTY=500.000}
        	     
        	        if(productDetResponse.getAdditionalAttributes()!=null){
        	        	Map<String,String> additionalAttributes=productDetResponse.getAdditionalAttributes();
        	        	if(additionalAttributes.containsKey("ASSC_COLOR")){
        	        		//invLocProduct.setAsscColor(additionalAttributes.get("ASSC_COLOR")); 
        	        		invLocProduct.setContrackColor(additionalAttributes.get("ASSC_COLOR"));
        	        		
        	        	}
        	        	if(additionalAttributes.containsKey("SHORT_DESC")){
        	        		invLocProduct.setShortDesc(additionalAttributes.get("SHORT_DESC"));       	        		
        	        		
        	        	}
        	        	if(additionalAttributes.containsKey("CUST_CNTR_RANK")){
        	        		invLocProduct.setContractRank(additionalAttributes.get("CUST_CNTR_RANK"));       	        		
        	        		
        	        	}
        	        	if((additionalAttributes.get("UOIF") != null) && (additionalAttributes.containsKey("UOIF"))){
        	        		
        	        		uoif = Double.parseDouble(additionalAttributes.get("UOIF").trim());
     	        		LOGGER.fine("UOIF>>>"+additionalAttributes.get("UOIF"));
        	        		
        	        	}
        	        	
        	        	if(additionalAttributes.containsKey(InventoryConstants.AFTER_REBATE_COST)) {
        	        		
        	        		if(MessageLoggerHelper.isTraceEnabled(LOGGER)) {
        	        			
        	        			LOGGER.fine("RebatePriceFeatureEnab::"+invLocationDetailRequest.getRebatePriceEnabled());
        	        		}
        	        		String rebateIndicator = InventoryUtility.getRebateIndicator(invLocationDetailRequest.getContractGrpAffiliationList()
        	        				, invLocationDetailRequest.getRebatePriceEnabled(), productDetResponse.getRebateIndicator()
        	        				, productDetResponse.getContractGroupAffiliationNumber(), Double.valueOf(additionalAttributes.get("AFTER_REBATE_COST"))
        	        				, invLocationDetailRequest.getIsRebateProgramAcct());
        	        		
        	        		invLocProduct.setRebateIndicator(rebateIndicator);
        	        	
        	        	} else {
        	        		
        	        		if(MessageLoggerHelper.isTraceEnabled(LOGGER)) {
        	        			
        	        			LOGGER.fine("AFTER_REBATE_COST is not retrieved from ProductDetailEJB");
        	        		}
        	        		String rebateIndicator = InventoryUtility.getRebateIndicator(invLocationDetailRequest.getContractGrpAffiliationList()
        	        				, invLocationDetailRequest.getRebatePriceEnabled(), productDetResponse.getRebateIndicator()
        	        				, productDetResponse.getContractGroupAffiliationNumber(), InventoryConstants.ZERO_IN_DOUBLE
        	        				, invLocationDetailRequest.getIsRebateProgramAcct());
        	        		
        	        		invLocProduct.setRebateIndicator(rebateIndicator);
        	        	}
        	        	
        	        	if(InventoryUtility.isCostZero(invLocProduct.getInvoiceCost()) && additionalAttributes.containsKey("CURR_CORP_NIFO_DOLLAR")){
        	        		BigDecimal infoCost=new BigDecimal(additionalAttributes.get("CURR_CORP_NIFO_DOLLAR"));
           	        	   invLocProduct.setInvoiceCost(infoCost.doubleValue());
        	        	
        	        }
        	    
        	       }
       	       
       	         invLocProduct.setUpc(productDetResponse.getUpc());
       	         if(InventoryUtility.isDoubleZero(uoif)){
       	        	uoif=calculateUOIF(productDetResponse);
       	         }
       	         invLocProduct.setUOIF(InventoryUtility.formatCost(uoif));
       	         invLocProduct.setUoiCost(InventoryUtility.deriveUOICost(invLocProduct.getInvoiceCost(),uoif));
				 if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {

						LOGGER.fine("invLocProduct.getProductType()="
									+ invLocProduct.getProductType());
						LOGGER.fine("invLocProduct.getNdc()="
									+ invLocProduct.getNdc());
						LOGGER.fine("invLocProduct.getUpc()="
									+ invLocProduct.getUpc());
						LOGGER.fine("uoif=" + uoif);

				 }
       	        invLocProduct.setNdcUpc(InventoryUtility.deriveDisplayNDCUPC(invLocProduct.getProductType(), invLocProduct.getNdc(), invLocProduct.getUpc()));
       	        invLocProduct.setInvoiceCost(InventoryUtility.formatCost(invLocProduct.getInvoiceCost())); 	     
       	        
       	        if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
       	        	
       	        	LOGGER.fine("invLocProduct.getUOIF="+invLocProduct.getUOIF());
       	        	LOGGER.fine("invLocProduct.getNdcUpc()="+invLocProduct.getNdcUpc());
       	        }
       		}
       	  }
       	  setNumberOfNotes(invLocProduct);

  	     	deriveDisplayContract(invLocProduct);
       		updatedLocLinesList.add(invLocProduct);
       	}
	 
	}
	return updatedLocLinesList;
}

/**
 *  This method takes List of products, request instance of InvLocationRequest, which makes the call to Product Details router to get product cost for given products.
 *  Then map product object to location map object by product based on cost type.
 *  
 *  Rule1 -0
 *   (1)  As of date cost is not Zero then Set Cost will be As of Date Cost.
 *   (2) As of date cost zero and Last Purchase Cost not zero then set cost will be  Last Purchase Cost. 
 *   (2) As of date cost, Last Purchase Cost are Zero and Invoice/NetCost  not Zero then Set Cost will be Invoice/Net Cost
 *   (3) As of date cost, invoice Cost and Last Purchase Cost are zero then use set cost will be Product NIFO Cost
 *   (4) As of date cost, invoice Cost, Last Purchase Cost and Product NIFO Cost are zero then use set cost will be "0.00"
 *  Rule2 -1
 *   (1)Last Purchase Cost is not zero then Set Cost will be Last Purchase Cost
 *   (2) Last Purchase Cost Zero and Invoice/NetCost  not Zero then Set Cost will be Invoice/Net Cost
 *   (3) invoice Cost and Last Purchase Cost zero then use set cost as Product NIFO Cost
 *   (4) invoice Cost, Last Purchase Cost and Product NIFO Cost zero then use set cost will be  "0.00"
 *   
 *  Rule3 If Cost type -2
 *   (1)Invoice Cost is not zero then Set Cost will be Invoice Cost
 *   (2) Invoice Cost Zero and Last Purchase Cost not Zero then Set Cost will be Last Purchase Cost
 *   (3) invoice Cost and Last Purchase Cost zero then use set cost will be Product NIFO Cost
 *   (4)invoice Cost, Last Purchase Cost and Product NIFO Cost zero then use set cost will be "0.00"
 *   
 *   
 *   
 * @param response
 * @param prdMap
 * @return
 * @throws BusinessException 
 * @throws SystemException 
 * @throws IOException 
 */
	private Map<String, InvLocationProductCost> getLocLineProductCost(
			final List<String> products,InvLocationRequest request, InvLocationResponse response) 
			throws SystemException, BusinessException, IOException {
	
		final String methodName = "getLocLineProductCost";
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}

		Map<String, InvLocationProductCost> prdCostMap = null;

		ProductDetailsRouter prodRouter=new ProductDetailsRouter();
		InventoryRouter invRouter = new InventoryRouter();
		
		InvLocationDetailRequest  detailsRequest=new InvLocationDetailRequest();
		detailsRequest.setShipToCustomer(request.getShipToCustomer());
		detailsRequest.setDistributionCenter(request.getDistributionCenter());

		Map<String,Double> prodPriceAsOfDateMap = null; 
		try {
			if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.info("SET COST TYPE for Customer="+detailsRequest.getDistributionCenter()+"-"+detailsRequest.getShipToCustomer()+",CostType="+request.getCostType()+ ",SET DATE ="+new Date());
			}
			if( request.getCostType()==InventoryConstants.AS_OF_DATE_COST_TYPE_CODE) {
				if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
					LOGGER.info("GETTING COST FROM TERADATA for NUmber of products::="+detailsRequest.getInvlocLines().length);
				}
				prodPriceAsOfDateMap = prodRouter.getProdPriceAsOfDate(products, detailsRequest, 
						request.getCostAsOfDate());
				if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
					if(prodPriceAsOfDateMap!=null){
					LOGGER.info("GOT RESPONSE FROM TERADATA for Number of products::="+prodPriceAsOfDateMap.size());
					}else{
						LOGGER.info("GOT RESPONSE FROM TERADATA , no products return="+prodPriceAsOfDateMap);
					}
					
				}
			}
		} catch(Throwable t) {
			MessageLoggerHelper.alert(ApplicationName.CUSTOM, "SY13817", AlertType.SYSTEM, AlertLevel.CRITICAL,
            "Phys Inventory: Purchase History service unavailable.");
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							InventoryConstants.ErrorKeys.PHYS_INV_PURCH_HISTORY_UNAVAILABLE),
					InventoryConstants.ErrorKeys.PHYS_INV_PURCH_HISTORY_UNAVAILABLE_DESC));
			response.setStatusCode("PRICINGEXCEPTION");
			response.setStatusMsg("Purchase History service unavailable");
			
			return null;
		}

		Map<String, com.cardinal.ws.physicalinventory.common.valueobjects.ProductDetailsResponse> prductMap=null;
		
			//prductMap = prodRouter.getProductDetails(products, detailsRequest);
			
			// Changes made in Agile 15.4 to make direct DB call to retrieve 
			// product details instead of relying in the Product Details EJB
			prductMap = invRouter.getProductDetails(products, detailsRequest);
			

			LOGGER.finest(MessageLoggerHelper.buildMessage(request.getShipToCustomer() + "-"
					+ request.getDistributionCenter(), request.getUserId(), request.getTransactionId(), "productMap returned from calling Inventory EJB :: "+prductMap, 
					"CIN",products.toString(),CLASS_NAME,methodName));
			
	
		
		com.cardinal.ws.physicalinventory.common.valueobjects.ProductDetailsResponse prdResponse=null;

		if (null != prductMap) {

			prdCostMap=	new HashMap<String, InvLocationProductCost>();
//			int count=1;
			InvLocationProductCost locProdCost=null;

			for (int i=0;i<products.size();i++) {
				
				if(prductMap.containsKey(products.get(i))){        		
					prdResponse = prductMap.get(products.get(i));
					 Double uoif=0.00;
					 double derivedInvoiceCost=0;
					locProdCost=new InvLocationProductCost();
					if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
						LOGGER.fine("prdResponse.getProductNumber()::"+prdResponse.getProductNumber());            	 
						LOGGER.fine("prdResponse::"+prdResponse);
					}         	
				
					if( request.getCostType()==InventoryConstants.CURRENT_COST_TYPE_CODE){
						if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
						 LOGGER.fine("CURRENT COST");  
						}
						//locProdCost.setCost(new BigDecimal(prdResponse.getCost()));
						if(!InventoryUtility.isCostZero(prdResponse.getInvoiceCost())){
							derivedInvoiceCost=prdResponse.getInvoiceCost();
						}else if(!InventoryUtility.isCostZero(prdResponse.getCostLastPurchased())){
							derivedInvoiceCost=prdResponse.getCostLastPurchased();
						}
					} else if(request.getCostType()==InventoryConstants.LAST_PAID_COST_TYPE_CODE){
						if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
							 LOGGER.fine("LAST COST");
						 }
					
						if(!InventoryUtility.isCostZero(prdResponse.getCostLastPurchased())){
							derivedInvoiceCost=prdResponse.getCostLastPurchased();
						}else if(!InventoryUtility.isCostZero(prdResponse.getInvoiceCost())){
							derivedInvoiceCost=prdResponse.getInvoiceCost();
						}
					} else if(request.getCostType()==InventoryConstants.AS_OF_DATE_COST_TYPE_CODE) {
						if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
							 LOGGER.fine("AS OF DATE  COST");
						 }
					
						// if purch hist service returns a non-null map, contains a price for the cin,
						//  and the price is non-zero...use this price
						if(prodPriceAsOfDateMap != null && 
								prodPriceAsOfDateMap.containsKey(prdResponse.getProductNumber()) &&
								!InventoryUtility.isCostZero(prodPriceAsOfDateMap.get(
										prdResponse.getProductNumber()))) {
							LOGGER.finest("Using AsOfDate price: " + prodPriceAsOfDateMap.get(
									prdResponse.getProductNumber()) + ", for Product: " + products.get(i));
							derivedInvoiceCost= prodPriceAsOfDateMap.get(prdResponse.getProductNumber());
							if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
								 LOGGER.fine("erivedInvoiceCost from TeraData="+derivedInvoiceCost);
							 }
							
						} else if(!InventoryUtility.isCostZero(prdResponse.getCostLastPurchased())){
							derivedInvoiceCost=prdResponse.getCostLastPurchased();
							if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
								 LOGGER.fine("derivedInvoiceCost LAST PAID="+derivedInvoiceCost);
							 }
							
						} else if(!InventoryUtility.isCostZero(prdResponse.getInvoiceCost())){
							
							derivedInvoiceCost=prdResponse.getInvoiceCost();
							if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
								 LOGGER.fine("derivedInvoiceCost INVOICE="+derivedInvoiceCost);
							 }
						}
					}
					
					// Commented as not needed for now
					/*if (null != prdResponse.getPrdCustomAttribte() && prdResponse.getPrdCustomAttribte()
							.getUoiFactor()!=null) {
						uoif=prdResponse.getPrdCustomAttribte()
								.getUoiFactor().doubleValue();
					}*/
					//if(prdResponse.getAdditionalAttributes()!=null){	
						if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
							 LOGGER.fine("derivedInvoiceCost FINEAL="+derivedInvoiceCost);
						 }
							
					if(InventoryUtility.isCostZero(derivedInvoiceCost) && 
							prdResponse.getNifoCost() != null){
						if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
							 LOGGER.fine("derivedInvoiceCost is Null or Zero="+derivedInvoiceCost);
						 }
						
						derivedInvoiceCost = Double.valueOf(prdResponse.getNifoCost());
						if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
							 LOGGER.fine("derivedInvoiceCost is NIFO="+derivedInvoiceCost);
						 }
					}
					
					if(InventoryUtility.isDoubleZero(uoif) && prdResponse.getUOIF() != null){    	        	
    	        			  uoif=Double.parseDouble((prdResponse.getUOIF()));
//    	        			LOGGER.info("UOIF>>>"+additionalAttributes.get("UOIF"));
    	        			  if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
    								 LOGGER.fine("uoif="+uoif);
    						 }
    	        		
    	        		}
					//}
    	        	if(InventoryUtility.isDoubleZero(uoif)){
    	        		uoif=calculateUOIF(prdResponse);
    	        	    	        		
    	        	}

					
    	        		derivedInvoiceCost=InventoryUtility.formatCost(derivedInvoiceCost);
    	        		// even after calling InventoryUtility.formatCost, BigDecimal recognized
    	        		//    all decimal places, have to set the scale
    	        		BigDecimal decDerived = new BigDecimal(derivedInvoiceCost);
    	        		BigDecimal decScaled = decDerived.setScale(2, BigDecimal.ROUND_HALF_UP);
    	        		locProdCost.setCost(decScaled);
    	        		locProdCost.setUoif(new BigDecimal(uoif));	
    	        		double uoiCost=InventoryUtility.deriveUOICost(derivedInvoiceCost,uoif);
    	        		locProdCost.setUoiCost(new BigDecimal(uoiCost));
 
					

					if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
						//LOGGER.fine("prdResponse.getCost::>>>>"+prdResponse.getCost());
						LOGGER.fine("getCostLastPurchased::>>>>"+prdResponse.getCostLastPurchased());
						LOGGER.fine("getCost::>>>>"+locProdCost.getCost());
						LOGGER.fine("locProdCost.getUoif()::>>>>"+locProdCost.getUoif());
						LOGGER.fine("getUoiCost::>>>>"+locProdCost.getUoiCost());
						LOGGER.fine("prdResponse.getProductNumber()::>>>>"+prdResponse.getProductNumber());
						LOGGER.fine("locProdCost::>>>>"+locProdCost);
					}
					prdCostMap.put(prdResponse.getProductNumber(), locProdCost);
				}
			}
		}

		return prdCostMap;
	}

	/**
	 * This method updates a location's details
	 * 
	 * @param request
	 * @return
	 * @throws ParseException
	 * @throws SystemException
	 */
	public InvLocationDetailResponse updateLocationDetails(InvLocationDetailRequest request) 
	throws ParseException, SystemException {
		final String methodName = "updateLocationDetails";

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}

		InventoryRouter router = new InventoryRouter();
		InvLocationDetailResponse response = null;
		response = router.updateLocationDetails(request);

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to resource from processor for updateLocationDetails------>" +  response);
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		return response;
	}
	
	/** 
	 * This method takes location request which contains location ids, cost type, account, dc, user id and user name
	 *  Then makes database call to get products for Given location(s), then makes call to Product Details EJB or purchase history to get product cost information 
	 *  base on cost type. Once cost retrieved which maps to location line, which makes router to persistence those information.
	 * @param request
	 * @return
	 * @throws NamingException
	 * @throws ParseException
	 * @throws SystemException
	 * @throws BusinessException
	 * @throws IOException 
	 */
	public InvLocationResponse setLocationsCost(InvLocationRequest request) 
		throws NamingException, ParseException, SystemException, BusinessException, IOException {
		final String methodName = "setLocationsCost";

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}


		InventoryRouter router = new InventoryRouter();
		InvLocationResponse response = null;

		Map<String, InvLocationProductCost> prdMap = null;
		List<String> locProducts=router.getLocationsProducts(request);
		if(null != locProducts &&  !locProducts.isEmpty()){
			final List<String> productsList=new ArrayList<String>();
			int locSize=locProducts.size();
			String productId=null;
			for(int i=0;i<locSize;i++){
				productId =(String)locProducts.get(i);

				if(InventoryUtility.isNumericAndNotNull(productId) && 
						(productId.length()==8 || productId.length()==7)){
					
					productsList.add(productId);
				}
			}

			if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
				LOGGER.fine("Products List:::"+productsList);
			}
			
			prdMap=getLocLineProductCost(productsList, request, response);        	                     

			request.setProductCostMap(prdMap);
		}

		response = router.setLocationsCost(request);		

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to resource from processor for SetLocationCost------>" +  response);
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		return response;
	}
	
	
	
    /**
     * This method takes Product details response and calculate UOIF
     * @param response
     * @return
     */
    public static Double calculateUOIF(ProductDetailsResponse response){    	
    	   Double uoif=0.00;

        if ((null != response.getPackageQty()) && (response.getPackageSize()!=null)) {
        	int tempval=response.getPackageSize() *response.getPackageQty();
        	uoif =new Double(tempval);
        }
        LOGGER.finest("uoif>>>>"+uoif);
    	return uoif;        
    		
    }	
    
    /**
     * This method takes Product details response and calculate UOIF
     * @param response
     * @return
     */
    public static Double calculateUOIF(com.cardinal.ws.physicalinventory.common.valueobjects.ProductDetailsResponse response){    	
    	   Double uoif=0.00;

        if ((null != response.getPackageQty()) && (response.getPackageSize()!=null)) {
        	int tempval=response.getPackageSize() *response.getPackageQty();
        	uoif =new Double(tempval);
        }
        LOGGER.finest("uoif>>>>"+uoif);
    	return uoif;        
    		
    }	
    /**
     * 
     * @param invLocProduct
     * @return
     */
    private static void setNumberOfNotes(InvLocProductResponse invLocProduct){
    	int notesCount=0;
    	
    	if(invLocProduct!=null){
    		if(InventoryUtility.isNotNullAndEmpty(invLocProduct.getRebateIndicator()) && 
    				(invLocProduct.getRebateIndicator().equalsIgnoreCase(GEN_REBATE) || invLocProduct.getRebateIndicator().equalsIgnoreCase(LDR_REBATE))){
    			notesCount=notesCount+1;
    		}
    		if(InventoryUtility.isNotNullAndEmpty(invLocProduct.getPreviouslyPurchase()) && invLocProduct.getPreviouslyPurchase().equalsIgnoreCase(YES) ){
    			notesCount=notesCount+1;
    		}
    		if(InventoryUtility.isNotNullAndEmpty(invLocProduct.getProductImageIndicator()) && invLocProduct.getProductImageIndicator().equalsIgnoreCase(YES)){
    			notesCount=notesCount+1;
    		}
    		if(InventoryUtility.isNotNullAndEmpty(invLocProduct.getReturnable()) && invLocProduct.getReturnable().equalsIgnoreCase("N")){
    			notesCount=notesCount+1;
    		}
    		if(InventoryUtility.isNotNullAndEmpty(invLocProduct.getUnitDose()) && (invLocProduct.getUnitDose().equalsIgnoreCase(UU)
    				|| invLocProduct.getUnitDose().equalsIgnoreCase(UD) || invLocProduct.getUnitDose().equalsIgnoreCase(UNIT_DOSE) || invLocProduct.getUnitDose().equalsIgnoreCase(UNIT_OF_USE))){
    			notesCount=notesCount+1;
    		}
    		if((InventoryUtility.isNotNullAndEmpty(invLocProduct.getNewProductIndicator()) && invLocProduct.getNewProductIndicator().equalsIgnoreCase(YES))){
    			notesCount=notesCount+1;
    		}
    		if( InventoryUtility.isNotNullAndEmpty(invLocProduct.getOnFormulary()) 
    				&& invLocProduct.getOnFormulary().equalsIgnoreCase(YES)){
    			notesCount=notesCount+1;
    		}
    	}   	invLocProduct.setNotes(notesCount);
		//return notesCount    	
    	
    }
		
  /**
   * This method creates the Contract test to display
   * 
   * @param invLocProduct
   */
   private void deriveDisplayContract(InvLocProductResponse invLocProduct){
	   
	   String displayContract=" ";
	   StringBuffer displayContractBuffer = new StringBuffer();
	   if(invLocProduct!=null){
		   
		   if(InventoryUtility.isNotNullAndEmpty(invLocProduct.getContractAlias())){
			   displayContract=invLocProduct.getContractAlias();
//			   displayContractBuffer.append(invLocProduct.getContractAlias());
		   }else if(InventoryUtility.isNotNullAndEmpty(invLocProduct.getShortDesc())){
			   displayContract=invLocProduct.getShortDesc();
//			   displayContractBuffer.append(invLocProduct.getShortDesc());
		   }else if(InventoryUtility.isNotNullAndEmpty(invLocProduct.getContract())){
			   displayContract=invLocProduct.getContract();
//			   displayContractBuffer.append(invLocProduct.getContract());
		   }
		   
		   if(InventoryUtility.isNotNullAndEmpty(invLocProduct.getContractRank()) && !invLocProduct.getContractRank().equalsIgnoreCase("0")){
//			   displayContract=invLocProduct.getContractRank()+"-"+displayContract;
//			   displayContractBuffer = new StringBuffer();
			   displayContractBuffer.append(invLocProduct.getContractRank());
			   displayContractBuffer.append(InventoryConstants.DASH.replace(" ", ""));
			   displayContractBuffer.append(displayContract);
		   } else {
			   displayContractBuffer.append(displayContract);
		   }
	   }
	  
	   invLocProduct.setContractDisplay(displayContractBuffer.toString());
   }
	
   /** 
	 * This method takes location request which contains location ids, cost type, account, dc, user id and user name
	 *  Then makes database call to get products for Given location(s), then makes call to Product Details EJB or purchase history to get product cost information 
	 *  base on cost type. Once cost retrieved which maps to location line, which makes router to persistence those information.
	 * @param request
	 * @return
	 * @throws NamingException
	 * @throws ParseException
	 * @throws SystemException
	 * @throws BusinessException
	 * @throws IOException 
	 */
	public InvLocationResponse isLocationValid(InvLocationRequest request) throws NamingException, ParseException, SystemException, BusinessException, IOException {
		final String methodName = "isLocationValid";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		}
		
		InventoryRouter router = new InventoryRouter();
		InvLocationResponse response = null;
		
		Integer locNum =(Integer)request.getLocs().toArray()[0];
		String shipToCust = request.getShipToCustomer();
		String shipToLoc = request.getDistributionCenter();
		
		response = router.getValidLocation(request, locNum.intValue(), shipToCust, shipToLoc);
		
//		if(locDetail != null) {
//			response = new InvLocationResponse();
//			
//
//		}
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		
		return response;
	}
	
	
}
