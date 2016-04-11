/*********************************************************************
 *
 * $Workfile: BackOrderHistoryRouter.java $
 * Copyright 2012 Cardinal Health
 *
 *********************************************************************
 *
 * Revision History:
 *
 * $Log: com/cardinal/ws/physicalinventory/PhysicalInventoryPersisterBeanLocal.java $
 * Modified By             Date              Clarify#         Description of the change
 * Shruti Sinha			08/10/2015		  AD Factory 15.5   Import By CSN to Physical Inventory
 *********************************************************************/
package com.cardinal.ws.physicalinventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import com.cardinal.ws.physicalinventory.common.valueobjects.InvBaseRequest;
import com.cardinal.ws.physicalinventory.common.valueobjects.ProductDetailsResponse;
import com.cardinal.ws.physicalinventory.inventory.valueobjects.InvInventoryRequest;
import com.cardinal.ws.physicalinventory.inventory.valueobjects.InvInventoryResponse;
import com.cardinal.ws.physicalinventory.location.valueobjects.InvLocationRequest;
import com.cardinal.ws.physicalinventory.location.valueobjects.InvLocationResponse;
import com.cardinal.ws.physicalinventory.locationdetail.valueobjects.InvImportRequest;
import com.cardinal.ws.physicalinventory.locationdetail.valueobjects.InvImportResponse;
import com.cardinal.ws.physicalinventory.locationdetail.valueobjects.InvLocationDetailRequest;
import com.cardinal.ws.physicalinventory.locationdetail.valueobjects.InvLocationDetailResponse;


@Local
public interface PhysicalInventoryPersisterBeanLocal {
	public InvInventoryResponse createInventory(InvInventoryRequest request); //(InvAccount acct, String invName);
	public InvInventoryResponse deleteInventories(InvInventoryRequest request); //(InvAccount acct, InvInventory[] invs);
	public InvInventoryResponse getInventorySummary(InvInventoryRequest request); // InvAccount account
	public InvInventoryResponse updateInventory(InvInventoryRequest request); //(InvAccount acct, InvInventory inv, BigDecimal value);
	public InvLocationResponse copyLocations(InvLocationRequest request); //(InvAccount acct, InvInventory fromInv, InvLocation[] fromLocs, InvInventory toInv, Boolean namesOnly);
	public InvLocationResponse createLocation(InvLocationRequest request); //(InvAccount acct, InvInventory inv, String locationName, int countType);
	public InvLocationResponse deleteLocations(InvLocationRequest request); //(InvAccount acct, InvInventory inv, InvLocation[] locs);
	public InvLocationResponse getLocationSummary(InvLocationRequest request); //InvAccount acct, InvInventory inv
	public InvLocationResponse updateLocationStatus(InvLocationRequest request); //(InvAccount acct, InvInventory inv, InvLocation loc, BigDecimal value);
	public InvLocationDetailResponse createLocationDetail(InvLocationDetailRequest request); //(InvAccount acct, InvInventory inv, InvLocation loc, int product, int qty);
	public InvLocationDetailResponse deleteLocationDetails(InvLocationDetailRequest request); //(InvAccount acct, InvInventory inv, InvLocation loc, InvLocationDetail[] lines);
	public InvLocationDetailResponse getLocationDetails(InvLocationDetailRequest request); //(InvAccount acct, InvInventory inv, InvLocation loc);
	public InvLocationDetailResponse updateLocationDetails(InvLocationDetailRequest request); //(InvAccount acct, InvInventory inv, InvLocation loc, InvLocationDetail[] lines);
	public InvLocationResponse setLocationsCost(InvLocationRequest request); //InvAccount acct, InvInventory inv
	public InvLocationDetailResponse reOrderLocationDetails(InvLocationDetailRequest request); 
	public List<String> getLocationsProducts(InvLocationRequest request);
	public InvLocationResponse updateLocation(InvLocationRequest request);
	public InvImportResponse addimportedItems(InvImportRequest request);
	public HashMap<String,String> getProductCINSearch(List<String> request,String shipToLoc,String shipToCustomer);
	public InvLocationResponse getValidLocation(InvLocationRequest request, int locNum, String shipToCustNum, String shipToLocNum);

	// Release 15.5 : Import By CSN to Physical Inventory : START
	public HashMap<String, ArrayList<String>> fetchCINsfromCSN(final InvImportRequest request , ArrayList<String> csn,List<String> productCatalogServiceMap);
	// Release 15.5 : Import By CSN to Physical Inventory : END
	
// Method to retrieve product details from the PDS
	public List<ProductDetailsResponse> getProductDetails(final List<String> products,final InvBaseRequest request);
}

