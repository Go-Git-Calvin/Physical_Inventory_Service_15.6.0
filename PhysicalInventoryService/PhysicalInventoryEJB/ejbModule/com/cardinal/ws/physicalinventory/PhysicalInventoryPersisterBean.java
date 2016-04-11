/*********************************************************************
*
* $Workfile: PhysicalInventoryPersisterBean $
* Copyright 2013 Cardinal Health
*
*********************************************************************
*
* Revision History:
*
* $Log: $
*Modified By             Date              Clarify#         Description of the change
 Vignesh Kandadi	   04/05/2014	      ALM#14444	 	 Updated UOIF data field from Integer to Big Decimal
 Sudhakar Yadav		   08/14/2014	      Defect#16058   Assign max item count as seq value when user enter value greater than that
 Vignesh Kandadi	   10/17/2014	      Defect#		 GetLocation Summary JPA query taking more than usual time, so converted to native query to fix the performance issue.
 Sandeep Chandrashekar 10/28/2014         Defect#16773   Added code to re-arrange the sequence number when the user deletes one or more LocationDetails from a Location. 
 Shruti Sinha			07/28/2015		  AD Factory 15.5 Physical Inventory Import Performance Improvement
 Shruti Sinha		    11/26/2015		  15.6 			 Defect #21102
*********************************************************************/
package com.cardinal.ws.physicalinventory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.cardinal.webordering.common.errorhandling.ErrorHandlingHelper;
import com.cardinal.webordering.common.errorhandling.SystemException;
import com.cardinal.webordering.common.logging.MessageLoggerHelper;
import com.cardinal.ws.common.util.DataSourceManager;
import com.cardinal.ws.common.util.PropertyManager;
import com.cardinal.ws.physicalinventory.common.CommonUtils;
import com.cardinal.ws.physicalinventory.common.InventoryConstants;
import com.cardinal.ws.physicalinventory.common.InventoryUtility;
import com.cardinal.ws.physicalinventory.common.valueobjects.InvBaseRequest;
import com.cardinal.ws.physicalinventory.common.valueobjects.InvInventory;
import com.cardinal.ws.physicalinventory.common.valueobjects.InvLocation;
import com.cardinal.ws.physicalinventory.common.valueobjects.ProductDetailsResponse;
import com.cardinal.ws.physicalinventory.entities.CostType;
import com.cardinal.ws.physicalinventory.entities.CountType;
import com.cardinal.ws.physicalinventory.entities.Invtry;
import com.cardinal.ws.physicalinventory.entities.Location;
import com.cardinal.ws.physicalinventory.entities.LocationDetail;
import com.cardinal.ws.physicalinventory.entities.LocationStat;
import com.cardinal.ws.physicalinventory.entities.ProdIdType;
import com.cardinal.ws.physicalinventory.inventory.valueobjects.InvInventoryRequest;
import com.cardinal.ws.physicalinventory.inventory.valueobjects.InvInventoryResponse;
import com.cardinal.ws.physicalinventory.location.valueobjects.InvLocationRequest;
import com.cardinal.ws.physicalinventory.location.valueobjects.InvLocationResponse;
import com.cardinal.ws.physicalinventory.locationdetail.valueobjects.InvImportRequest;
import com.cardinal.ws.physicalinventory.locationdetail.valueobjects.InvImportResponse;
import com.cardinal.ws.physicalinventory.locationdetail.valueobjects.InvLocationDetailRequest;
import com.cardinal.ws.physicalinventory.locationdetail.valueobjects.InvLocationDetailResponse;
import com.cardinal.ws.physicalinventory.product.valueobjects.InvLocProductRequest;
import com.cardinal.ws.physicalinventory.product.valueobjects.InvLocProductResponse;
import com.cardinal.ws.physicalinventory.product.valueobjects.InvLocationProductCost;
import com.cardinalhealth.common.intnl.ResourceBundle;
import com.cardinalhealth.common.intnl.ResourceBundleFactory;
import com.cardinalhealth.common.intnl.ResourceBundleHelper;


/**
 * @author brian.deffenbaugh01
 *
 * Session Bean implementation class physicalinventoryPersisterBean
 */

@Stateless
public class PhysicalInventoryPersisterBean implements PhysicalInventoryPersisterBeanRemote, PhysicalInventoryPersisterBeanLocal {
 
	
	public static final String LAST_PRICE_PAID_COST_TYPE = "Last Cost Paid";
	private static final String CLASS_NAME = PhysicalInventoryPersisterBean.class.getCanonicalName();
    private static final Logger LOGGER = MessageLoggerHelper.getLogger(CLASS_NAME);
    private static final Integer ZERO = new Integer("0");
//    private static final Integer NEGATIVE_ONE = new Integer("-1");
	private static final String SLASH_APOS = "\'";
    private static final int UNKNOWN_PROD_TYPE = 1;
    private static final int MULTIPLE_PROD_TYPE = 2;
	@PersistenceContext(unitName=InventoryConstants.PHYSICAL_INVENTORY_PERSISTENCE_UNIT)
    private EntityManager entityMgr;	
	
	
    /**
     * Default constructor. 
     */
    public PhysicalInventoryPersisterBean() {}

    /**
     * This method processes a copyLocations request.  Creates a copy of an existing lcoation
     */
    @Override
    public InvLocationResponse copyLocations(final InvLocationRequest request)  {
		//(InvAccount acct, InvInventory fromInv, InvLocation[] fromLocs, InvInventory toInv, Boolean namesOnly)
		//{"account": {"distributionCenter":"6","shipToCustomer": "632018","acctName": "","sortByAcct": false,"sortByAcctName": false},"transactionId": "123456789","userId": "defbri02","storeId": "123","timeZone": "EST","shipToCustomer": "632018","distributionCenter": "6","recordsPerPage": 100,"pageNum": 1,"numberOfDays": 3,"refreshFlag": false}
		final String METHOD_NAME = "copyLocations";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);
			
		}
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("copyLocations Request ------->"+ request.toString());
		}

    	//for each of the request.getLocs(), create a copy in request.getCopyToInv().  
		//if request.getCopyNamesOnly(), create them as empty locations, otherwise, 
		//create locations with same products as original location with quantities of zero.
		InvInventory newInvtry=null;
				
		final Calendar nonAfxDate = new GregorianCalendar(1900,01,01);
		final InvLocationResponse response = new InvLocationResponse();
		final Set<Integer> copyLocs = request.getLocs();
		Location newLoc=null;
		Location oldLoc=null;
//		response = getLocationSummary(request);
		try {
			if (null != copyLocs && copyLocs.size() > 0 && request.getCopyToInv()>0) { // NOPMD by stephen.perry01 on 5/24/13 3:55 PM  Not comparing to 0, cannot use isEmpty()
				Invtry newInv = entityMgr.find(Invtry.class, (request.getCopyToInv()));
				final InvInventory invReq=new InvInventory();
				invReq.setInventoryId(newInv.getInvtryNum());
				request.setInventory(invReq);
				Iterator<Integer> it=copyLocs.iterator();
				Integer locId=null;
				while(it.hasNext()){
					 locId=(Integer)it.next();
					oldLoc = entityMgr.find(Location.class, (locId.intValue()));

					newLoc = new Location();
					newLoc.setCostAfxStp(new Timestamp(nonAfxDate.getTimeInMillis()));
					newLoc.setCostType(entityMgr.find(CostType.class,0));
					newLoc.setCountType(oldLoc.getCountType());
					newLoc.setCreateStp(new Timestamp(System.currentTimeMillis()));							
					newLoc.setInvtry(newInv);

					if (!request.getCopyNamesOnly()) {
						final List<LocationDetail> newLines = new ArrayList<LocationDetail>();
						final List<LocationDetail> oldLines = oldLoc.getLocationDetails();
						for (int x = 0; x < oldLines.size(); x++) {
							LocationDetail oldLine = oldLines.get(x);
							LocationDetail newLine = new LocationDetail();
							newLine.setProdId(oldLine.getProdId());
							newLine.setCostUoifNum(BigDecimal.ZERO);
							newLine.setCountType(oldLine.getCountType());
							newLine.setLocation(newLoc);
							newLine.setLineLevelQty(BigDecimal.ZERO);						
							newLine.setCostAmt(BigDecimal.ZERO);
							newLine.setProdIdType(entityMgr.find(ProdIdType.class,oldLine.getProdIdType().getProdIdTypeCde()));
							newLine.setCalcTotalValueAmt(BigDecimal.ZERO);
							newLine.setManualCostOvrdAmt(BigDecimal.ZERO);
							newLine.setLastUpdateUserNam(request.getUserName());						
							newLine.setRowAddStp(new Timestamp(System.currentTimeMillis()));
							newLine.setRowAddUserId(request.getUserId());
							newLine.setRowUpdateStp(new Timestamp(System.currentTimeMillis()));
							newLine.setRowUpdateUserId(request.getUserId());
							newLine.setSeqNum(oldLine.getSeqNum());
							newLine.setCmntTxt("");							
							newLines.add(newLine);
						}
						newLoc.setLocationDetails(newLines); 
					}	


					newLoc.setLocationNam(getCopyLocationUniqueName(request, oldLoc.getLocationNam()));
					newLoc.setLocationStat(entityMgr.find(LocationStat.class, InventoryConstants.OPEN_STATUS_CODE));
					newLoc.setRowAddStp(new Timestamp(System.currentTimeMillis()));
					newLoc.setRowAddUserId(request.getUserId());
					newLoc.setRowUpdateStp(new Timestamp(System.currentTimeMillis()));
					newLoc.setRowUpdateUserId(request.getUserId());
					newLoc.setTotalLocationValueAmt(BigDecimal.ZERO);
					newLoc.setCmntTxt("");
					entityMgr.persist(newLoc);

					entityMgr.flush();

				}

				newInvtry = new InvInventory();
				newInvtry.setInventoryId(newInv.getInvtryNum());
				newInvtry.setInventoryName(newInv.getInvtryNam());
				newInvtry.setNumberOfLocations(newInv.getLocations().size());
				newInvtry.setTotalValue(newInv.getInvtryTotalValueAmt().doubleValue());
				newInvtry.setCreateDate(new Date(newInv.getCreateStp().getTime()));

				//newInvtry.setTotalValue(totalValue)

			}
			else {
				LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
						request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
						InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION,
						InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION_DESC +
						".  You did not supply any locations to copy."));
				
				throw new Exception("You did not supply any locations to copy.");
			}
		} catch (Throwable t) {
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY),
							t.getCause()+": "+t.getMessage()));

			throw new SystemException(ErrorHandlingHelper.THROWABLE_ERROR_KEY,
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY), t.getCause()+": "+t.getMessage(), t);

		}
		
		response.setInventory(newInvtry);
		response.setStatusCode(InventoryConstants.PHY_INVENTORY_SUCCESS);
		response.setStatusMsg(InventoryConstants.COPY_LOCATION_SUCCESSFUL_MESSAGE);

		//response = getLocationSummary(request);

		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to router from persister for copyLocations------>" +  response);
		}

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}

		return response;
	}
    
    /**
     * This method creates a new Inventory
     */
	@Override
	public InvInventoryResponse createInventory(InvInventoryRequest request) {
		final String METHOD_NAME = "createInventory";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);	
		}
		
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("createInventory Request ------->"+ request.toString());
		}
		final InvInventoryResponse response = new InvInventoryResponse();
		try {
			if(isInventoryNameUnique(request)){

				final Timestamp now = new Timestamp(System.currentTimeMillis());
				Invtry inventory = new Invtry();
				inventory.setShipToLocationNum(new BigDecimal(request.getDistributionCenter().trim()));
				inventory.setShipToCustomerNum(new BigDecimal(request.getShipToCustomer().trim()));
				inventory.setInvtryNam(request.getInvName().trim());
				inventory.setInvtryTotalValueAmt(new BigDecimal(ZERO.intValue()));		
				inventory.setCreateStp(now);
				inventory.setRowAddStp(now);
				inventory.setRowAddUserId(request.getUserId().trim());
				inventory.setRowUpdateStp(now);
				inventory.setRowUpdateUserId(request.getUserId().trim());
				entityMgr.persist(inventory);
				entityMgr.flush();


				response.setStatusCode(InventoryConstants.PHY_INVENTORY_SUCCESS);
				response.setStatusMsg(InventoryConstants.SUCCESSFULLY_CREATED_INVENTORY);
				InvInventory createdInventory = new InvInventory();
				createdInventory.setInventoryId(inventory.getInvtryNum());
				List<InvInventory> createInventoryList = new ArrayList<InvInventory>();
				createInventoryList.add(createdInventory);
				response.setInventories(createInventoryList);	
			} else{
				LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
						request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
						InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION,
						InventoryConstants.DUPLICATE_INVENTORY_NAME_MESSAGE));
				response.setStatusCode(InventoryConstants.DUPLICATE_STATUS_CODE);
				response.setStatusMsg(InventoryConstants.DUPLICATE_INVENTORY_NAME_MESSAGE);

			}
		} catch (Throwable t) {

			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY),
							t.getCause()+": "+t.getMessage()));

			throw new SystemException(ErrorHandlingHelper.THROWABLE_ERROR_KEY,
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY), t.getCause()+": "+t.getMessage(), t);

		}
		
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to router from persister for createInventory------>" +  response);
		}

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}

		return response;
	}

	/**
	 * This method creates a new location
	 */
	@Override
	public InvLocationResponse createLocation(InvLocationRequest request) {
		final String METHOD_NAME = "createLocation";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);
		}
		
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("createLocation Request ------->"+ request.toString());
		}
		
		InvLocationResponse response =  new InvLocationResponse();
		try {
			if(!isLocationNameByInventoryUnique(request)){
				Location location = new Location();
				final Timestamp now = new Timestamp(System.currentTimeMillis());
				final Calendar nonAfxDate = new GregorianCalendar(1900,01,01);		
				location.setCostAfxStp(new Timestamp(nonAfxDate.getTimeInMillis()));
				location.setCostType(entityMgr.find(CostType.class, request.getCostType()));
				location.setCountType(entityMgr.find(CountType.class, request.getCountType()));
				location.setCreateStp(now);
				location.setInvtry(entityMgr.find(Invtry.class, request.getInventory().getInventoryId()));
				location.setLocationNam(request.getLocName().trim());
				location.setLocationStat(entityMgr.find(LocationStat.class, 0));
				location.setTotalLocationValueAmt(BigDecimal.ZERO);
				location.setCmntTxt("");
				location.setRowAddStp(now);
				location.setRowAddUserId(request.getUserId().trim());
				location.setRowUpdateStp(now);
				location.setRowUpdateUserId(request.getUserId().trim());

				entityMgr.persist(location);
				entityMgr.flush();


				InvLocation invLocation = new InvLocation();
				invLocation.setLocationId(location.getLocationNum());
				List<InvLocation> invLocationList = new ArrayList<InvLocation>();
				invLocationList.add(invLocation);
				response.setLocations(invLocationList);
				response.setStatusCode(InventoryConstants.PHY_INVENTORY_SUCCESS);
				response.setStatusMsg(InventoryConstants.SUCCESSFULLY_CREATED_LOCATION);
			}else{
				LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
						request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
						InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION,
						"Location Name already exists in this inventory."));
				response.setStatusCode(InventoryConstants.DUPLICATE_STATUS_CODE);
				response.setStatusMsg("Location Name already exists in this inventory.");
			}
		} catch (Throwable t) {

			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY),
							t.getCause()+": "+t.getMessage()));

			throw new SystemException(ErrorHandlingHelper.THROWABLE_ERROR_KEY,
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY), t.getCause()+": "+t.getMessage(), t);

		}
		
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to router from persister for createLocation------>" +  response);
		}

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}

		return response;
	}
	
	/**
	 * This method updates a location
	 */
	@Override
	public InvLocationResponse updateLocation(InvLocationRequest request) {
		final String METHOD_NAME = "updateLocation";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);
		}
		
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("updateLocation Request ------->"+ request.toString());
		}
		
		InvLocationResponse response =  new InvLocationResponse();
		
		try {
			if(!isLocationNameByInventoryUnique(request)){
				
				Location location = entityMgr.find(Location.class,request.getLocs().toArray()[0]);
				if(location!=null){
					if(InventoryUtility.isNotNullAndEmpty(request.getLocName())){
						location.setLocationNam(request.getLocName().trim());
					}

					if(request.getLocComments()!=null){
						location.setCmntTxt(request.getLocComments());
					}

					location.setRowUpdateStp(new Timestamp(System.currentTimeMillis()));
					location.setRowUpdateUserId(request.getUserId().trim());

					entityMgr.merge(location);
					entityMgr.flush();

					final InvLocation invLocation = new InvLocation();
					invLocation.setLocationId(location.getLocationNum());
					invLocation.setLocationName(location.getLocationNam());
					invLocation.setLocComments(location.getCmntTxt());
					invLocation.setCountType(location.getCountType().getCountTypeDesc());
					invLocation.setLocStatus(location.getLocationStat().getLocationStatDesc());
					final List<InvLocation> invLocationList = new ArrayList<InvLocation>();
					invLocationList.add(invLocation);
					response.setLocations(invLocationList);
					response.setStatusCode(InventoryConstants.PHY_INVENTORY_SUCCESS);
					response.setStatusMsg(InventoryConstants.SUCCESSFULLY_UPDATED_LCOATION);
				}else{
					LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
							request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
							InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION,
							InventoryConstants.LOCATION_DOES_NOT_EXIST));
					response.setStatusCode(InventoryConstants.INVALID_STATUS_CODE);
					response.setStatusMsg(InventoryConstants.LOCATION_DOES_NOT_EXIST);
				}
			}else{
				LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
						request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
						InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION,
						InventoryConstants.DUPLICATE_LOCATION_MESSAGE));
				response.setStatusCode(InventoryConstants.DUPLICATE_STATUS_CODE);
				response.setStatusMsg(InventoryConstants.DUPLICATE_LOCATION_MESSAGE);
			}
		} catch (Throwable t) {

			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY),
							t.getCause()+": "+t.getMessage()));

			throw new SystemException(ErrorHandlingHelper.THROWABLE_ERROR_KEY,
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY), t.getCause()+": "+t.getMessage(), t);

		}
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to router from persister for createLocation------>" +  response);
		}

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}

		return response;
	}
	
	/**
     * This method creates a database connection
     *
     * @param dataSourceJndi
     *            - data source JNDI name.
     *
     * @return A database connection
     *
     * @throws NamingException
     *             This is thrown when the JNDI name is not defined.
     * @throws SQLException
     *             This is thrown when an error occurs while creating a database
     *             connection.
     */
    private Connection getConnection(final String dataSourceJndi)
        throws NamingException, SQLException {
        final String METHOD_NAME = "getConnection";

        if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
            LOGGER.entering(CLASS_NAME, METHOD_NAME);
        }
        //Added for static variable fix start
        Properties properties = PropertyManager.getInstance().getProperties(InventoryConstants.PHYSICAL_INVENTORY_SERVICE_NAME,
        		InventoryConstants.PHYSICAL_INVENTORY_FILE_NAME);
      //Added for static variable fix end
        final String jndiName = properties.getProperty(dataSourceJndi);

        if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
            LOGGER.fine("Connection JNDI NAME : " + jndiName);
        }

        final Connection connection = DataSourceManager.getInstance() // NOPMD
                                                       .getConnection(jndiName);// NOPMD

        if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
            LOGGER.exiting(CLASS_NAME, METHOD_NAME);
        }

        return connection;
    }
	/**
	 * Returns SQL Query.
	 * 
	 * @param sqlName
	 * @param methodName
	 * 
	 * @param propertyKey
	 *            - property Key.
	 * 
	 * @return SQL query.`
	 */
    private String getSQLQuery(final String propertyKey) {
        return CommonUtils.findServiceProperty(
        		InventoryConstants.PHYSICAL_INVENTORY_SERVICE_NAME, InventoryConstants.PHYSICAL_INVENTORY_FILE_NAME,
                propertyKey);
    }
    	
    //AD Factory 15.5 Release PCR: Physical Inventory Import Performance Improvement
    //This method has been modified to create JDBC connection and write JDBC sqls in place of existing JPA sqls.
	/**
	 * This method creates a new location detail, ie adds another line item to the location
	 * 
	 */
	@Override
	public InvLocationDetailResponse createLocationDetail(InvLocationDetailRequest request) {
		//{"cost":0.00, "lines": null, "location": {"afxDate": null, "costType": null, "countType": null, "createDate": null, "inventoryID": 2, "lineCount": null, "locationId": 571000000, "locationName": null, "locStatus": null, "totalValue": 0}, "product": 1110097, "quantity": 127, "transactionId": "123456789", "userId": "defbri02", "storeId": "123", "timeZone": "EST", "shipToCustomer": "12092", "distributionCenter": "34","recordsPerPage": 100, "pageNum": 1, "numberOfDays": 3, "refreshFlag": false}
		final String METHOD_NAME = "createLocationDetail";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);			
		}

		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("createLocationDetail Request ------->"+ request.toString());
		}
		InvLocationDetailResponse response=null;
		// AD Factory 15.5 Release: Physical Inventory Import Performance Improvement
		Connection connection = null; // NOPMD	
		PreparedStatement preparedStatement = null; // NOPMD , Used DataSourceManager to free the PreparedStatement object.
		PreparedStatement mobileRequestPrepStmt = null; // NOPMD , Used DataSourceManager to free the PreparedStatement object.
		PreparedStatement insertLocDetPreparedStmt = null; // NOPMD , Used DataSourceManager to free the PreparedStatement object.
		PreparedStatement nextValuePreparedStatement = null; // NOPMD , Used DataSourceManager to free the PreparedStatement object.
		PreparedStatement updateLocPrepStmt = null; // NOPMD , Used DataSourceManager to free the PreparedStatement object.
		PreparedStatement locNumPreparedStatement = null; // NOPMD , Used DataSourceManager to free the PreparedStatement object.
		PreparedStatement selectLocationDetailsStmt = null; // NOPMD , Used DataSourceManager to free the PreparedStatement object.
		PreparedStatement updateUserPrepStmt = null; // NOPMD , Used DataSourceManager to free the PreparedStatement object.
		PreparedStatement locStatusStmt = null; // NOPMD , Used DataSourceManager to free the PreparedStatement object.
		
		ResultSet resultSet = null; // NOPMD , Used DataSourceManager to free the ResultSet object.
		ResultSet mobileRequestResultSet = null; // NOPMD , Used DataSourceManager to free the ResultSet object.
		ResultSet nextValueResultSet = null; // NOPMD , Used DataSourceManager to free the ResultSet object.
		ResultSet locNumResultSet = null; // NOPMD , Used DataSourceManager to free the ResultSet object.
		ResultSet locationDetailsResultSet = null; // NOPMD , Used DataSourceManager to free the ResultSet object.
		ResultSet rsStatusStmt = null; // NOPMD , Used DataSourceManager to free the ResultSet object.
		
	

		try {
			// AD Factory 15.5 Release PCR: Physical Inventory Import Performance Improvement - START
			connection = getConnection(InventoryConstants.PHYSICAL_INVENTORY_JNDI_PI);
			
			// If location is closed, do not add any location details
			//int locCde = getLocationStatus(request.getLocation().getLocationId());
			int locCde = getLocStatus(request,connection,locStatusStmt,rsStatusStmt);
			if(locCde == InventoryConstants.CLOSED_STATUS_CODE) {
				response = new InvLocationDetailResponse();
				response.setStatusCode(InventoryConstants.CLOSEDLOC);
				response.setStatusMsg(InventoryConstants.STATUS_MSG);
				
				return response;
			}
			
			if(null != request.getInvlocLines() && 0 < request.getInvlocLines().length){
				response = new InvLocationDetailResponse();
				// get the num of line items and maximum seq num in the location
				long currItemCount = 0;
				int seqNum = 0;
				String sql = getSQLQuery(InventoryConstants.PHYSICAL_INV_MAX_LOCATION_DET);
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, request.getLocation().getLocationId());
				resultSet = preparedStatement.executeQuery();
				
				if (resultSet.next()) {
					currItemCount = resultSet.getLong(1);
					seqNum = resultSet.getInt(2);
		
				}			
				
				// if number of items to be added + current item count is greater
				//  than the max allowed, do not add prods to locations
				
				final int numItemsToBeAdded = request.getInvlocLines().length;
				final ResourceBundle resourceBundle = ResourceBundleFactory.getBundle(InventoryConstants.RESOURCE_BUNDLE,
                        Locale.ROOT);
            	final ResourceBundleHelper resourceBundleHelper = new ResourceBundleHelper(resourceBundle);
            	int maxAllowedProducts = Integer.parseInt(resourceBundleHelper.getProperty(
                		InventoryConstants.RSRC_BNDL_KEY_NAM,
                		InventoryConstants.MAXIMUM_PRODUCTS_ITEM_COUNT));
				
            	
            	if(MessageLoggerHelper.isTraceEnabled(LOGGER)){
					LOGGER.finest("maxAllowedProducts: " + maxAllowedProducts);
				}
            	
				if((currItemCount + numItemsToBeAdded) <= maxAllowedProducts) {
					
					final List<InvLocProductResponse> itemsAdd = new ArrayList<InvLocProductResponse>();
						
					final List<String> prodIdList = new ArrayList<String>();				
					for(InvLocProductRequest prdRequest : request.getInvlocLines()){
						String prodId = prdRequest.getProduct();
							prodIdList.add(prodId);
					}
					final String insertTableSQL = getSQLQuery(InventoryConstants.PHYSICAL_INV_INSERT_LOC_DET);
					
					if(MessageLoggerHelper.isTraceEnabled(LOGGER)){
						LOGGER.finest("INSERT SQL:" + insertTableSQL);
					}
					
					insertLocDetPreparedStmt = connection.prepareStatement(insertTableSQL);
					
					nextValuePreparedStatement = connection.prepareStatement(getSQLQuery(InventoryConstants.PHYSICAL_INV_NEXT_VAL));
					final List<Integer> newLocationDetails = new ArrayList<Integer>();
					 int counter=0;
					
					 final List<String> mobileDupProdIds = new ArrayList<String>();
					 // If the request is from Mobile, check is request products already present in Location with zero quantity, if its present they those lines will not addd again
					 if(request.isMobileRequest()){
						 final StringBuffer dupSqlQuery=new StringBuffer(500);
						 dupSqlQuery.append(getSQLQuery(InventoryConstants.PHYSICAL_INV_DUPLICATE_CHECK_1));
						 dupSqlQuery.append(createInClause(prodIdList));
						 dupSqlQuery.append(getSQLQuery(InventoryConstants.PHYSICAL_INV_DUPLICATE_CHECK_2));
						 mobileRequestPrepStmt = connection.prepareStatement(dupSqlQuery.toString());
						
						 if(MessageLoggerHelper.isTraceEnabled(LOGGER)){
								LOGGER.finest("dupSqlQuery=" + dupSqlQuery.toString());
							}
						
						 mobileRequestPrepStmt.setInt(1, request.getLocation().getLocationId());
						 int prepCtr=2;
						 for(String prodStr : prodIdList){
							 mobileRequestPrepStmt.setString(prepCtr, prodStr);
							 prepCtr++;
								
						 }
						 mobileRequestResultSet = mobileRequestPrepStmt.executeQuery();
							if(mobileRequestResultSet!=null){
							 while(mobileRequestResultSet.next()){
								final String product = mobileRequestResultSet.getString(InventoryConstants.PROD_ID);							
								
								if(!mobileDupProdIds.contains(product)){
								final InvLocProductResponse locDetObj=populateLocDetailResultsObj(mobileRequestResultSet);								
								mobileDupProdIds.add(product);	
								locDetObj.setDuplicate(true);
								itemsAdd.add(locDetObj);
								}
							}
						}	
						 
					 }
					for (InvLocProductRequest prdRequest : request.getInvlocLines()) {						
						if(!mobileDupProdIds.contains(prdRequest.getProduct())){
							seqNum++; // add 1 for the seqNum of the next item to be added
							counter++;
							insertLocDetPreparedStmt = insertQueryForLocationDetail(request,
									seqNum,nextValuePreparedStatement,
									prdRequest, insertLocDetPreparedStmt,newLocationDetails, nextValueResultSet);
						}				
						
					}	
					if(counter>0){
					insertLocDetPreparedStmt.executeBatch();
					}
					LOGGER.info("SEQ NUM : "+seqNum);
					// fetches the locations details of newly added product
					if(!newLocationDetails.isEmpty()){
						getNewLocationDetails(request, itemsAdd, newLocationDetails, connection, selectLocationDetailsStmt, locationDetailsResultSet);
					}
						
					InvLocation locUpdated = new InvLocation();
					locUpdated.setLocationId(request.getLocation().getLocationId());

					if(locCde == InventoryConstants.COST_SET_STATUS_CODE){
						int locationsDetUpdated = resetLocationSetCostStatusForCreateLocation(
								request.getLocation().getLocationId(), request.getUserId(), connection, updateLocPrepStmt);
						if(1 == locationsDetUpdated){
							locUpdated.setLocStatus(InventoryConstants.OPEN);
						}
					}
					locNumPreparedStatement = connection.prepareStatement(getSQLQuery(InventoryConstants.PHYSICAL_INV_LOC_TOT_AMT));
					locNumPreparedStatement.setInt(1, request.getLocation().getLocationId());
					locNumResultSet = locNumPreparedStatement.executeQuery();				
					
					// update location's row update stamp and userid
					updateLocationRowUpdateStpAndUserForLocDetail(request.getLocation().getLocationId(), 
							request.getUserId(),connection,updateUserPrepStmt);
					
					// AD Factory 15.5 Release PCR: Physical Inventory Import Performance Improvement - END					
					
					if(locNumResultSet.next()){
						locUpdated.setTotalValue(locNumResultSet.getBigDecimal(1));
						locUpdated.setLineCount(locNumResultSet.getInt(2));
					}
					response.setLocation(locUpdated);
					
					response.setInvLocProducts(itemsAdd);
					//response = getLocationDetails(request);
					//response.setNumberOfPages((request.getRecordsPerPage() > response.getInvLocProducts().size()) ? new Integer("1") : (response.getInvLocProducts().size() / request.getRecordsPerPage()));
					response.setPageNum(request.getPageNum());
					response.setSortColumns(request.getSortColumns());
					response.setSortOrder(request.getSortOrder());
					response.setStatusCode(String.valueOf(ZERO.intValue()));
					response.setStatusMsg("Success.");
				} 
				else {
					LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
							request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
							InventoryConstants.ErrorKeys.REQ_EXCEEDS_MAX_LINE_ITEM_COUNT,
							InventoryConstants.ErrorKeys.REQ_EXCEEDS_MAX_LINE_ITEM_COUNT_DESC));
					
					response.setStatusCode("EXCEEDS");
					response.setStatusMsg(InventoryConstants.ErrorKeys.REQ_EXCEEDS_MAX_LINE_ITEM_COUNT_DESC  
							+ ": " + InventoryConstants.MAXIMUM_LOCATION_ITEM_COUNT);
					
				}
			
		}
			} 
		
		catch(SQLException se){		
			LOGGER.info("EXCEPTION::" +se.getNextException());		
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY),
							se.getCause()+": "+se.getMessage()));

			throw new SystemException(ErrorHandlingHelper.THROWABLE_ERROR_KEY,
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY), se.getCause()+": "+se.getMessage(), se);
		}
		
		catch (Throwable t) {

			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY),
							t.getCause()+": "+t.getMessage()));

			throw new SystemException(ErrorHandlingHelper.THROWABLE_ERROR_KEY,
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY), t.getCause()+": "+t.getMessage(), t);

		}finally{
			DataSourceManager.freeConnection(connection, preparedStatement, resultSet);
			DataSourceManager.freeConnection(connection, mobileRequestPrepStmt, mobileRequestResultSet);
			DataSourceManager.freeConnection(connection, insertLocDetPreparedStmt , null);
			DataSourceManager.freeConnection(connection, nextValuePreparedStatement , nextValueResultSet);
			DataSourceManager.freeConnection(connection, updateLocPrepStmt , null);
			DataSourceManager.freeConnection(connection, locNumPreparedStatement , locNumResultSet);
			DataSourceManager.freeConnection(connection, selectLocationDetailsStmt , locationDetailsResultSet);
			DataSourceManager.freeConnection(connection, updateUserPrepStmt, null);
			DataSourceManager.freeConnection(connection, locStatusStmt, rsStatusStmt);
			
		}

		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to router from persister for createLocationDetail------>" +  response);
		}

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}

		return response;
	}
	// AD Factory 15.5 Release: Physical Inventory Performance Improvement
	/** This method gets the status code based on the location number.
	 * @param request
	 * @param rsStatusStmt
	 * @param locStatusStmt
	 * @return locCde
	 * @throws SQLException
	 */
	private int getLocStatus(InvLocationDetailRequest request, Connection connection, PreparedStatement locStatusStmt, ResultSet rsStatusStmt)// NOPMD
			throws SQLException {
		String 	METHOD_NAME = "getLocStatus";
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);
		}
		locStatusStmt = connection.prepareStatement(getSQLQuery(InventoryConstants.PHYSICAL_INVENTORY_GET_LOC_STAT_CDE));
		locStatusStmt.setInt(1, request.getLocation().getLocationId());
		rsStatusStmt = locStatusStmt.executeQuery();
		int locCde = 0;
		if(rsStatusStmt.next()){
			locCde = rsStatusStmt.getInt(1);
		}
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}
		return locCde;
	}
	// AD Factory 15.5 Release: Physical Inventory Performance Improvement
	/** This method fetches the location details of the newly added product
	 *
	 * @param request
	 * @param itemsAdd
	 * @param newLocationDetails
	 * @param connection 
	 * @param locationDetailsResultSet2 
	 * @param selectLocationDetailsStmt2 
	 * @throws SQLException
	 */
	private void getNewLocationDetails(InvLocationDetailRequest request,
			final List<InvLocProductResponse> itemsAdd,
			List<Integer> newLocationDetails, Connection connection, PreparedStatement selectLocationDetailsStmt, ResultSet locationDetailsResultSet) throws SQLException {// NOPMD
		
		String METHOD_NAME = "getNewLocationDetails";
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);
		}
		final StringBuffer getNewLocationDetails = new StringBuffer();
		
		getNewLocationDetails.append(getSQLQuery(InventoryConstants.PHYSICAL_INV_SELECT_LOC_DET));
		getNewLocationDetails.append(createIntClause(newLocationDetails));
		
		if(MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			
			LOGGER.fine("Get New Locations :"+getNewLocationDetails.toString());
		}
		
		selectLocationDetailsStmt = connection.prepareStatement(getNewLocationDetails.toString());
		selectLocationDetailsStmt.setInt(1, request.getLocation().getLocationId());
		int i = 2;					
		for(Integer location : newLocationDetails){						
			selectLocationDetailsStmt.setInt(i, location);
			i++;
		}					
		locationDetailsResultSet = 	selectLocationDetailsStmt.executeQuery();
		InvLocProductResponse locLineObj = null;  
		while (locationDetailsResultSet.next()){
			locLineObj=populateLocDetailResultsObj(locationDetailsResultSet);		
			itemsAdd.add(locLineObj);
			
		}
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}
	}
private InvLocProductResponse populateLocDetailResultsObj(final ResultSet locationDetailsResultSet) throws SQLException{
	
	String METHOD_NAME = "populateLocDetailResultsObj";
	if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
	}
	final InvLocProductResponse locLineObj=new InvLocProductResponse();
	
	locLineObj.setLocLineId(locationDetailsResultSet.getInt(InventoryConstants.LOCATION_DETAIL_NUM));
	locLineObj.setAfxUOIF(locationDetailsResultSet.getBigDecimal(InventoryConstants.COST_UOIF_NUM));
	locLineObj.setCountType(locationDetailsResultSet.getString(InventoryConstants.COUNT_TYPE_DESC));
	locLineObj.setLocLineType(locationDetailsResultSet.getInt(InventoryConstants.PROD_ID_TYPE_CDE));
	if(MessageLoggerHelper.isTraceEnabled(LOGGER)) {
		
		LOGGER.fine("LocationLineType in PhysicalInventoryPersisterBean :"+locLineObj.getLocLineType());
	}
	if(1 == locLineObj.getLocLineType()|| 3 == locLineObj.getLocLineType()){
		locLineObj.setNdcUpc(locationDetailsResultSet.getString(InventoryConstants.PROD_ID));
		locLineObj.setTradeName(locationDetailsResultSet.getString(InventoryConstants.PROD_ID_TYPE_DESC));
	}else{
		locLineObj.setProduct(locationDetailsResultSet.getString(InventoryConstants.PROD_ID));

	}
	locLineObj.setOverrideCost(locationDetailsResultSet.getDouble(InventoryConstants.MANUAL_COST_OVRD_AMT));
	locLineObj.setValuationCost(locationDetailsResultSet.getDouble(InventoryConstants.COST_AMT));
	locLineObj.setQuantity(locationDetailsResultSet.getDouble(InventoryConstants.LINE_LEVEL_QTY));
	locLineObj.setLineComments(locationDetailsResultSet.getString(InventoryConstants.CMNT_TXT));
	locLineObj.setInventoryValue(locationDetailsResultSet.getDouble(InventoryConstants.CALC_TOTAL_VALUE_AMT));						
	locLineObj.setSeq(locationDetailsResultSet.getInt(InventoryConstants.SEQ_NUM));
	locLineObj.setCreatedDate(locationDetailsResultSet.getTimestamp(InventoryConstants.ROW_ADD_STP));
	if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
	}
	return locLineObj;	
	
}
	//AD Factory 15.5 Release PCR: Physical Inventory Import Performance Improvement
   	/**
	 * This method updates the location detail
   	 * @param updateUserPrepStmt 
	 * 
	 */
	private void updateLocationRowUpdateStpAndUserForLocDetail(int locationId,
			String userId, Connection connection, PreparedStatement updateUserPrepStmt) throws SQLException {// NOPMD
		final String METHOD_NAME = "updateLocationRowUpdateStpAndUserForLocDetail";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);	
		}
		
		final Timestamp now = new Timestamp(System.currentTimeMillis());
		try {
			updateUserPrepStmt = connection
					.prepareStatement(getSQLQuery(InventoryConstants.PHYSICAL_INV_UPDATE_LOC_QUERY_1));
			updateUserPrepStmt.setString(1, userId);
			updateUserPrepStmt.setString(2, now.toString());
			updateUserPrepStmt.setInt(3, locationId);
			updateUserPrepStmt.executeUpdate();
		} catch (SQLException sqlExc) {
			LOGGER.info("EXCEPTION::" +sqlExc.getNextException());
		}
		
		if(MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}
		
	}
	// AD Factory 15.5 Release : Physical Inventory Performance Improvement
	/** This method inserts data in the Location Detail table
	 * @param request
	 * @param seqNum
	 * @param now
	 * @param nextValuePreparedStatement
	 * @param prdRequest
	 * @param insertLocDetPreparedStmt 
	 * @param newLocationDetails 
	 * @param nextValueResultSet 
	 * @return
	 * @throws SQLException
	 */
	private PreparedStatement insertQueryForLocationDetail(InvLocationDetailRequest request,
			int seqNum, PreparedStatement nextValuePreparedStatement,
			InvLocProductRequest prdRequest, PreparedStatement insertLocDetPreparedStmt, List<Integer> newLocationDetails, ResultSet nextValueResultSet) throws SQLException {// NOPMD
		
		final String METHOD_NAME = "insertQueryForLocationDetail";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);	
		}
		final Timestamp now = new Timestamp(System.currentTimeMillis());
		
		nextValueResultSet = nextValuePreparedStatement.executeQuery();

		if (nextValueResultSet.next()){
			insertLocDetPreparedStmt.setInt(1, nextValueResultSet.getInt(1));
		}
		
		newLocationDetails.add(Integer.parseInt(String.valueOf(nextValueResultSet.getInt(1))));
		
		LOGGER.info("nextValueResultSet.getInt(1):: "+ nextValueResultSet.getInt(1));

		insertLocDetPreparedStmt.setString(2, prdRequest.getProduct());
		LOGGER.info("prdRequest.getProduct()"+ prdRequest.getProduct());
		insertLocDetPreparedStmt.setBigDecimal(3, BigDecimal.ZERO);
		insertLocDetPreparedStmt.setBigDecimal(4, BigDecimal.ZERO);
		LOGGER.info("prdRequest.getCountType(): "+prdRequest.getCountType());
		//#Defect 21102 - START
		insertLocDetPreparedStmt.setInt(5,null!=prdRequest.getCountType()? Integer.valueOf(prdRequest.getCountType()):0);
		//#Defect 21102 - END
		insertLocDetPreparedStmt.setInt(6, request.getLocation().getLocationId());
		
		LOGGER.info("request.getLocation().getLocationId()"+ request.getLocation().getLocationId());
		insertLocDetPreparedStmt.setInt(7, null != prdRequest.getProductType()? Integer.valueOf(prdRequest.getProductType()):0);
		insertLocDetPreparedStmt.setBigDecimal(8, BigDecimal.ZERO);
		insertLocDetPreparedStmt.setString(9, InventoryConstants.EMPTY_STRING_NO_SPACE);
		if (null != prdRequest.getQuantity() && 0.0 != prdRequest.getQuantity().doubleValue()){
			insertLocDetPreparedStmt.setBigDecimal(10, BigDecimal.valueOf(prdRequest.getQuantity()));
		}else{
			insertLocDetPreparedStmt.setBigDecimal(10, BigDecimal.ZERO);
		}
		insertLocDetPreparedStmt.setTimestamp(11, now);
		insertLocDetPreparedStmt.setString(12, null != request.getUserId()? request.getUserId().trim():request.getUserId());
		insertLocDetPreparedStmt.setTimestamp(13, now);
		insertLocDetPreparedStmt.setString(14, null != request.getUserId()? request.getUserId().trim():request.getUserId());
		insertLocDetPreparedStmt.setString(15, request.getUserName());
		insertLocDetPreparedStmt.setBigDecimal(16, BigDecimal.ZERO);
		insertLocDetPreparedStmt.setInt(17, seqNum);
		LOGGER.info("SEQ:: "+ seqNum);
		
		insertLocDetPreparedStmt.addBatch();
		
		if(MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}
		
		return insertLocDetPreparedStmt;
	}
	/**
	 * This method takes request as InvInventoryRequest delete the inventories. When you delete inventory it also deletes
	 *  Inventory Locations and Location lines from table, After successful deletion returns as Inventory summary in InvInventoryResponse object.
	 *  If there is no inventories for selected inventories still it returns InvInventoryResponse.
	 * 
	 */
	@Override
	public InvInventoryResponse deleteInventories(final InvInventoryRequest request) {
		final String METHOD_NAME = "deleteInventories";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);
		}
		
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("deleteInventories Request ------->"+ request.toString());
		}
		
		InvInventoryResponse response = null;
		try {
			InvInventory[] invs = request.getInvs(); // Inventories to delete
			if (null != invs && invs.length > 0) {
				for (int i = 0; i < invs.length; i++) {						
					//Now delete the inventory.
					long inventoryId = invs[i].getInventoryId();
					Invtry invtry = entityMgr.find(Invtry.class, inventoryId); //getInventory(Integer.parseInt(request.getDistributionCenter()), Integer.parseInt(request.getShipToCustomer()), invs[i].getInventoryId());
					if (null != invtry) {
						entityMgr.remove(invtry);
					}
				}
				entityMgr.flush();
			}
			response = getInventorySummary(request);

		} catch (Throwable t) {
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY),
							t.getCause()+": "+t.getMessage()));

			throw new SystemException(ErrorHandlingHelper.THROWABLE_ERROR_KEY,
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY), t.getCause()+": "+t.getMessage(), t);

		}
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to router from persister for deleteInventories------>" +  response);
		}

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}

		return response;
	}

	/**
	 * This method deletes a location detail, ie - a line item of the location
	 */
	@Override
	public InvLocationDetailResponse deleteLocationDetails(InvLocationDetailRequest request)  {
		final String METHOD_NAME = "deleteLocationDetails";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);
		}
		
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("deleteLocationDetails Request ------->"+ request.toString());
		}
		
		InvLocationDetailResponse response = new InvLocationDetailResponse();
		
		try {
			InvLocProductRequest[] lines = request.getInvlocLines();
			InvLocation locUpdated = new InvLocation();
			StringBuffer lineIds=new StringBuffer();
			for (int x = 0; x < lines.length; x++) {
				InvLocProductRequest thisItem = lines[x];
				if(x!=0){
					lineIds.append(InventoryConstants.COMMA);
				}
				lineIds.append(thisItem.getLocLineId());
			}
			/**LocationDetail dtl = new LocationDetail();
			dtl = getLocationDetail(request.getLocation().getLocationId(), thisItem.getLocLineId(), thisItem.getSeq());
			 */
			String deleteQuery="DELETE FROM OE_PI.LOCATION_DETAIL AS LOC WHERE LOC.LOCATION_DETAIL_NUM IN("+lineIds+") AND LOC.LOCATION_NUM="+request.getLocation().getLocationId();
			Query updateLoc = entityMgr.createNativeQuery(deleteQuery);

			int locationsDetUpdated = updateLoc.executeUpdate();
			if (locationsDetUpdated==1) {

				locUpdated.setLocationId(request.getLocation().getLocationId());				
				locationsDetUpdated=resetLocationSetCostStatus(request.getLocation().getLocationId(), request.getUserId());
				if(locationsDetUpdated==1){
					locUpdated.setLocStatus("Open");

				}

				entityMgr.flush();
			}
			// Method added to re-sequence the location details after line item deletion.
			updateSequenceNumber(request);
			final Query query=entityMgr
			.createQuery("SELECT sum(loc.calcTotalValueAmt) AS totalAmount, COUNT(loc.locationNum) AS total FROM LocationDetail AS loc  WHERE loc.location.locationNum=:locationNum");
			query.setParameter("locationNum",request.getLocation().getLocationId());	
			final List<Object[]> results =query.getResultList(); 

			if(results!=null && !results.isEmpty()){
				final Object[] result=results.get(0);
				locUpdated.setTotalValue((BigDecimal)(result[0]));	
//				BigDecimal bdTotalValue = (BigDecimal)result[0];
//				BigDecimal scaledTotalLinesAmt = bdTotalValue.setScale(2, BigDecimal.ROUND_HALF_UP);
//				locUpdated.setTotalValue(scaledTotalLinesAmt);
				locUpdated.setLineCount(((Number)result[1]).intValue());

			}
			
			response.setLocation(locUpdated);			
			response.setStatusCode(InventoryConstants.PHY_INVENTORY_SUCCESS);
			response.setStatusMsg("Successfully deleted lines.");

		} catch (Throwable t) {
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY),
							t.getCause()+": "+t.getMessage()));

			throw new SystemException(ErrorHandlingHelper.THROWABLE_ERROR_KEY,
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY), t.getCause()+": "+t.getMessage(), t);

		}
		
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to router from persister for deleteLocationDetails------>" +  response);
		}

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}

		return response;
	}
	
	/**
	 * This method deletes the entire location
	 */
	@Override
	public InvLocationResponse deleteLocations(InvLocationRequest request)  {
		// TODO (InvAccount acct, InvInventory inv, InvLocation[] locs)
		//{"account": {"distributionCenter":"6","shipToCustomer": "632018","acctName": "","sortByAcct": false,"sortByAcctName": false},"lines": null, "location": {"afxDate": null, "costType": "Original", "countType": "Full", "createDate": "10/18/2012", "inventoryID": 4, "lineCount": 4, "locationId": 44, "locationName": "Location 4", "locStatus": "Assigned", "totalValue": 0}, "product": 1234567, "quantity": 27, "transactionId": "123456789", "userId": "defbri02", "storeId": "123", "timeZone": "EST", "shipToCustomer": "632018", "distributionCenter": "6","recordsPerPage": 100, "pageNum": 1, "numberOfDays": 3, "refreshFlag": false}
		final String METHOD_NAME = "deleteLocations";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);
		}
		
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("deleteLocations Request ------->"+ request.toString());
		}
		
		InvLocationResponse response = null;
		
		try {
			if(null != request.getLocs() && !request.getLocs().isEmpty()){
				for(Integer loc : request.getLocs()){
					Location locEnty=entityMgr.find(Location.class, loc);
					entityMgr.remove(locEnty);
					entityMgr.flush();
				}
			}		
			response = getLocationSummary(request);

		} catch (Throwable t) {
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY),
							t.getCause()+": "+t.getMessage()));

			throw new SystemException(ErrorHandlingHelper.THROWABLE_ERROR_KEY,
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY), t.getCause()+": "+t.getMessage(), t);

		}
		
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to router from persister for deleteLocations------>" +  response);
		}

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}

		return response;
	}
	

	/**
	 * This method retrieves all inventorys for a given account 
	 */
	@Override
	public InvInventoryResponse getInventorySummary(InvInventoryRequest request) {
		//{"account": {"distributionCenter":"6","shipToCustomer": "632018","acctName": "","sortByAcct": false,"sortByAcctName": false},"transactionId": "123456789","userId": "defbri02","storeId": "123","timeZone": "EST","shipToCustomer": null, "distributionCenter": null, "recordsPerPage": 100,"pageNum": 1, "numberOfDays": 3, "refreshFlag": false}
		final String METHOD_NAME = "getInventorySummary";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);
		}
		
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("getInventorySummary Request ------->"+ request.toString());
		}

		InvInventoryResponse response = null;
		
		try {
			//SQL Defined IN orm.xml 
			//String invSummarySQL ="InventorySummarySql";
			//Query invSummaryQuery = entityMgr.createNamedQuery(invSummarySQL);
			final String invSummarySQL = "SELECT inv FROM Invtry inv WHERE inv.shipToLocationNum = :locNum AND inv.shipToCustomerNum = :custNum ORDER BY inv.invtryNam ASC";
			Query invSummaryQuery = entityMgr.createQuery(invSummarySQL);
			invSummaryQuery.setParameter("locNum", new BigDecimal(request.getDistributionCenter()));
			invSummaryQuery.setParameter("custNum", new BigDecimal(request.getShipToCustomer()));
			List<Invtry> resultList = invSummaryQuery.getResultList();

			response = new InvInventoryResponse();
			List<InvInventory> inventories = new ArrayList<InvInventory>();

			if (null != resultList && !resultList.isEmpty()) {
				InvInventory inv=null;
				Invtry nextRec=null;
				for(int i = 0; i < resultList.size(); i++) {
					inv = new InvInventory();
					nextRec = resultList.get(i);
					inv.setCreateDate(new Date(nextRec.getCreateStp().getTime()));
					inv.setInventoryId(nextRec.getInvtryNum());
					inv.setInventoryName(nextRec.getInvtryNam());

					updateInventoryLocationInfo(inv);

					inventories.add(inv);

				}
			}
			response.setInventories(inventories);

			/*response.setNumberOfPages((request.getRecordsPerPage() > inventories.size()) ? new Integer("1") : (inventories.size() / request.getRecordsPerPage()));
		response.setPageNum(request.getPageNum());
		response.setSortColumns(request.getSortColumns());
		response.setSortOrder(request.getSortOrder());*/
			response.setStatusCode(String.valueOf(ZERO.intValue()));
			response.setStatusMsg("Success.");

		} catch (Throwable t) {
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY),
							t.getCause()+": "+t.getMessage()));

			throw new SystemException(ErrorHandlingHelper.THROWABLE_ERROR_KEY,
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY), ": "+t.getMessage(), t);

		}
		
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to router from persister for getInventorySummary------>" +  response);
		}

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}

		return response;
	}


	
	/**
	 * This method retrieves the details of a location
	 */
	@Override
	public InvLocationDetailResponse getLocationDetails(final InvLocationDetailRequest request) {
		final String METHOD_NAME = "getLocationDetails";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);
			
		}
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("getLocationDetails Request ------->"+ request.toString());
		}

		InvLocationDetailResponse response =null;
		
		try {
			final StringBuffer invLocationDetailSQL =new StringBuffer("SELECT dtl FROM LocationDetail dtl WHERE dtl.location.locationNum = :locNum ");
			final String invLocationNumDetailsSQL = "SELECT COUNT(dtl) FROM LocationDetail dtl WHERE dtl.location.locationNum = :locNum";
			
			String sortColumn="";
			String sortOrder=InventoryConstants.SORT_ORDER_DESC;
       	 if(request.getSortColumns()!=null && !request.getSortColumns().isEmpty()){
       		sortColumn=request.getSortColumns().get(0);   		
       	 }else{
       		sortColumn="seq";
       	 }
       	if(request.getSortOrder()!=null){
       		sortOrder=request.getSortOrder();
       	 }
       	 if(InventoryConstants.DB_SORT_COLUMNS.containsKey(sortColumn)){
       		 
       		invLocationDetailSQL.append(" ORDER BY");
       		invLocationDetailSQL.append(InventoryConstants.EMPTY_STRING);
       		invLocationDetailSQL.append("dtl.");
           	invLocationDetailSQL.append(InventoryConstants.DB_SORT_COLUMNS.get(sortColumn));
           	invLocationDetailSQL.append(InventoryConstants.EMPTY_STRING);
           	invLocationDetailSQL.append(sortOrder);
       	 }
       	 
       
			
       	if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("invLocationDetailSQL="+invLocationDetailSQL.toString());					
       	}
			
			final Location location = entityMgr.find(Location.class, request.getLocation().getLocationId());
			if(location!=null){
				final Map<Integer,String> countTypes=getCountTypes();
				final Map<Integer,String> costTypes=getCostTypes();
				final Map<Integer,String> statusTypes=getStatusList();
				
				final Query invLocationDetailQuery = entityMgr.createQuery(invLocationDetailSQL.toString());
				invLocationDetailQuery.setParameter("locNum", request.getLocation().getLocationId());
				
				// for Desktop, get first 500 lines from the location.
				// for Mobile, retrieve the requested amount
				int reqRecPerPage = request.getRecordsPerPage();
				final boolean isMobile = request.isMobileRequest();
				final ResourceBundle resourceBundle = ResourceBundleFactory.getBundle(InventoryConstants.RESOURCE_BUNDLE,
                        Locale.ROOT);
            	final ResourceBundleHelper resourceBundleHelper = new ResourceBundleHelper(resourceBundle);
            	int maxAllowedProducts = Integer.parseInt(resourceBundleHelper.getProperty(
                		InventoryConstants.RSRC_BNDL_KEY_NAM,
                		InventoryConstants.MAXIMUM_PRODUCTS_ITEM_COUNT));
            
            	
				if((!isMobile && !InventoryConstants.DB_SORT_COLUMNS.containsKey(sortColumn)) || (reqRecPerPage==0)) {
					invLocationDetailQuery.setMaxResults(maxAllowedProducts);	
					reqRecPerPage=1;
				} else {
					invLocationDetailQuery.setMaxResults(reqRecPerPage);
					int startResult = 0;
					if(request.getPageNum() > 0) {
						startResult = reqRecPerPage * (request.getPageNum() - 1);
					}
					invLocationDetailQuery.setFirstResult(startResult);
				}
				
				
				final List<LocationDetail> resultList = invLocationDetailQuery.getResultList();
				LOGGER.finest(METHOD_NAME + ": Retrieved " + resultList.size() + " results.");
				
				final Query invLocationNumDetailsQuery = entityMgr.createQuery(invLocationNumDetailsSQL);
				invLocationNumDetailsQuery.setParameter("locNum", request.getLocation().getLocationId());
				final Number countResult=(Number) invLocationNumDetailsQuery.getSingleResult();
				int numDetails = countResult.intValue();

				LOGGER.finest(METHOD_NAME + ": Num details for location " + 
						request.getLocation().getLocationId() + ": " + numDetails);
				
				response = new InvLocationDetailResponse();
				List<InvLocProductResponse> items=null;

				// do not add up lines total value amt, must query now because of the 500 item cap
				double totalLinesAmt=0.00;
				final String getTotalValueOfLocationSQL = 
					"SELECT sum(dtl.calcTotalValueAmt) AS totalAmount " +
					"FROM LocationDetail dtl WHERE dtl.location.locationNum = :locNum  ";
				final Query getTotalValueOfLocationQuery = entityMgr.createQuery(getTotalValueOfLocationSQL);
				getTotalValueOfLocationQuery.setParameter("locNum", request.getLocation().getLocationId());
				final Object totalValObj = getTotalValueOfLocationQuery.getSingleResult();
				if(totalValObj != null) {
					final Number numTotalValue=(Number) totalValObj;
					totalLinesAmt = numTotalValue.doubleValue();
				}
				
				if (null != resultList && !resultList.isEmpty()) {
					
					final Map<Integer,String> prodIdTypes=getProductIdTypes();					
					items = new ArrayList<InvLocProductResponse>();
					InvLocProductResponse locLineObj=null;
					LocationDetail detailLine=null;
					for(int i = 0; i < resultList.size(); i++) {
						locLineObj=new InvLocProductResponse();
						 detailLine = resultList.get(i);	
						locLineObj.setLocLineId(detailLine.getLocationNum());				
						locLineObj.setAfxUOIF(detailLine.getCostUoifNum());
						locLineObj.setCountType(readValueFromMap(detailLine.getCountTypeCde(),countTypes));

						locLineObj.setOverrideCost(detailLine.getManualCostOvrdAmt().doubleValue());
						if(detailLine.getLineLevelQty()!=null){
							locLineObj.setQuantity(detailLine.getLineLevelQty().doubleValue());
						}
						if(detailLine.getCostAmt()!=null){
							locLineObj.setValuationCost(detailLine.getCostAmt().doubleValue());
						}
						locLineObj.setLineComments(detailLine.getCmntTxt());
						if(detailLine.getCalcTotalValueAmt()!=null){
							locLineObj.setInventoryValue(detailLine.getCalcTotalValueAmt().doubleValue());
							//totalLinesAmt=totalLinesAmt+locLineObj.getInventoryValue();
						}
						locLineObj.setLocLineType(detailLine.getProdIdTypeCde());
						if(detailLine.getProdIdTypeCde()==UNKNOWN_PROD_TYPE || 
								detailLine.getProdIdTypeCde()==MULTIPLE_PROD_TYPE){
							locLineObj.setNdcUpc(detailLine.getProdId());
							locLineObj.setTradeName(readValueFromMap(detailLine.getProdIdTypeCde(),prodIdTypes));
						}else{
							locLineObj.setProduct(detailLine.getProdId());

						}
						locLineObj.setSeq(detailLine.getSeqNum());
						locLineObj.setCreatedDate(detailLine.getRowAddStp());
						items.add(locLineObj);
					}
				}
				response.setInvLocProducts(items);

				final InvLocation invLocation = request.getLocation();	
				invLocation.setLocationId(location.getLocationNum());
				invLocation.setCostType(readValueFromMap(location.getCostTypeCde(),costTypes));
				
				// scale and round total lines amount
				BigDecimal bdTotalLinesAmt = new BigDecimal(totalLinesAmt);
				BigDecimal scaledTotalLinesAmt = bdTotalLinesAmt.setScale(2, BigDecimal.ROUND_HALF_UP);
				invLocation.setTotalValue(scaledTotalLinesAmt);
				invLocation.setLocComments(location.getCmntTxt());
				invLocation.setCreateDate(location.getCreateStp());
				invLocation.setCountType(readValueFromMap(location.getCountTypeCde(),countTypes));
				if(!LAST_PRICE_PAID_COST_TYPE.equalsIgnoreCase(invLocation.getCostType()) && InventoryUtility.isNotDefaultDate(location.getCostAfxStp())){
					invLocation.setAfxDate(location.getCostAfxStp());
				}
				invLocation.setLocationName(location.getLocationNam());
				invLocation.setLocStatus(readValueFromMap(location.getLocStatCde(),statusTypes));

//				if(items!=null){
//					invLocation.setLineCount(items.size());
//				}
					
				invLocation.setLineCount(numDetails);
				int numPages =	((numDetails - 1) / reqRecPerPage) + 1;
				response.setNumberOfPages(numPages);
				response.setPageNum(request.getPageNum());
				//invLocation.setLineCount(items.size());
				response.setLocation(invLocation);

				//		response.setLocation(request.getLocation());
				
				response.setPageNum(request.getPageNum());
				response.setSortColumns(request.getSortColumns());
				response.setSortOrder(request.getSortOrder());
				response.setStatusCode(InventoryConstants.PHY_INVENTORY_SUCCESS);
				response.setStatusMsg(InventoryConstants.SUCCESS);
			}

		} catch (Throwable t) {			
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY),
							t.getCause()+": "+t.getMessage()));

			throw new SystemException(ErrorHandlingHelper.THROWABLE_ERROR_KEY,
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY), t.getCause()+": "+t.getMessage(), t);

		}
		
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to router from persister for getLocationDetails------>" +  response);
		}

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}

		return response;
	}
	

	/**
	 * This method retrieves the products of a location
	 */
	@Override
	public List<String> getLocationsProducts(InvLocationRequest request) {
		final String METHOD_NAME = "getLocationsProducts";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);
			
		}
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("getLocationsProducts Request ------->"+ request.toString());
		}

		List<String> resultList = null;
		
		try {
			final String invLocationDetailSQL = "SELECT DISTINCT dtl.prodId FROM LocationDetail dtl WHERE dtl.location.locationNum in(:locNum)";

			//SQL Defined IN orm.xml 
			//String invLocationDetailSQL ="LocationDetailSql";
			//Query invLocationDetailQuery = entityMgr.createNamedQuery(invLocationDetailSQL);
			Query invLocationDetailQuery = entityMgr.createQuery(invLocationDetailSQL);
			invLocationDetailQuery.setParameter("locNum", request.getLocs());

			resultList = invLocationDetailQuery.getResultList();

		} catch (Throwable t) {

			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY),
							t.getCause()+": "+t.getMessage()));

			throw new SystemException(ErrorHandlingHelper.THROWABLE_ERROR_KEY,
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY), t.getCause()+": "+t.getMessage(), t);

		}
		
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to router from persister for getLocationsProducts------>" +  resultList);
		}

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}

		return resultList;
	}


	/**
	 * This method retrieves all locations given an inventory
	 */
	@Override
	public InvLocationResponse getLocationSummary(InvLocationRequest request) {
		final String METHOD_NAME = "getLocationSummary";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);
			
		}
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("getLocationSummary Request ------->"+ request.toString());
		}
		
		InvLocationResponse response = new InvLocationResponse();
		
		try {
			if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
				LOGGER.finer("<--locationStatSql START------->");
			}
			final String locationStatSql = "SELECT locsum.locationStatDesc FROM LocationStat locsum ORDER BY locsum.locationStatCde ASC";
			
			final Query locationStatQuery = entityMgr.createQuery(locationStatSql);
			// Retrieve the list of status from Location Status table
			final List<String> locStats = locationStatQuery.getResultList();
			if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
				LOGGER.finer("<--locationStatSql END------->");
				LOGGER.finer("<--GET INVENTORY START------->");
			}			
					
			final long inventoryId = request.getInventory().getInventoryId();
			// Retrieve Inventory information for requested Inventory
			final Invtry inv = entityMgr.find(Invtry.class, inventoryId);
			
			if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
				LOGGER.finer("<--GET INVENTORY END------->");
				LOGGER.finer("<--GET LOCATION FROM INVENTORY START------->");
			}
			InvInventory inventory=null;		
			
			final StringBuffer locSqlQuery=new StringBuffer(500);
			locSqlQuery.append("SELECT loc.LOCATION_NUM,loc.LOCATION_NAM,loc.INVTRY_NUM,locStatus.LOCATION_STAT_DESC,cntTyp.COUNT_TYPE_DESC,");
			locSqlQuery.append(" costTyp.COST_TYPE_DESC,loc.CREATE_STP,loc.COST_AFX_STP,loc.TOTAL_LOCATION_VALUE_AMT, ");
			locSqlQuery.append(" loc.CMNT_TXT FROM OE_PI.LOCATION loc,OE_PI.LOCATION_STAT locStatus,OE_PI.COUNT_TYPE cntTyp,");
			locSqlQuery.append(" OE_PI.COST_TYPE costTyp WHERE ");
			locSqlQuery.append(" locStatus.LOCATION_STAT_CDE=loc.LOCATION_STAT_CDE ");
			locSqlQuery.append(" AND costTyp.COST_TYPE_CDE=loc.COST_TYPE_CDE ");
			locSqlQuery.append(" AND cntTyp.COUNT_TYPE_CDE=loc.COUNT_TYPE_CDE ");
			locSqlQuery.append("AND loc.INVTRY_NUM=?");
			locSqlQuery.append(" WITH UR ");
			
			if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
				LOGGER.finer("locSqlQuery.toString()="+locSqlQuery.toString());
			}
			final Query locationQuery = entityMgr.createNativeQuery(locSqlQuery.toString());
			if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
				LOGGER.finer("set Inventory Id as parameter="+inventoryId);
			}
			locationQuery.setParameter(1,inventoryId);
			final List<Object> locResultsSet=locationQuery.getResultList();
			
			// Define and declare Locations list object which holds the list of locations 
			final List<InvLocation> locations = new ArrayList<InvLocation>();
			// Which sum the total locations amount
			BigDecimal invTotal=BigDecimal.ZERO;
			
			if(locResultsSet!=null && !locResultsSet.isEmpty()){
			
				Date afxDte=null;				
					
			
				BigDecimal totalAmt=BigDecimal.ZERO;
				//Calendar calendar = Calendar.getInstance(); 	
				
				final StringBuffer locIds=new StringBuffer(200);
				for(int j = 0; j < locResultsSet.size(); j++) {
					Object[] resultObj=(Object[])locResultsSet.get(j);
					 final InvLocation newLoc = new InvLocation();
					 if(resultObj!=null){
						 if(resultObj[0]!=null){
							 newLoc.setLocationId((Integer)resultObj[0]);
						 if(j!=0){
							 locIds.append(InventoryConstants.COMMA);
						 }
						 locIds.append((Integer)resultObj[0]);
						 
						 }
						 if(resultObj[1]!=null){
							 newLoc.setLocationName(((String)resultObj[1]).trim());
						 }
						 if(resultObj[2]!=null){
							 newLoc.setInventoryID((Integer)resultObj[2]);
						 }
						 
						 if(resultObj[3]!=null){
							 newLoc.setLocStatus((String)resultObj[3]);
						 }
						 if(resultObj[4]!=null){
							 newLoc.setCountType((String)resultObj[4]);
						 }
						 if(resultObj[5]!=null){
							 newLoc.setCostType((String)resultObj[5]);
						 }
						 if(resultObj[6]!=null){
							 newLoc.setCreateDate((Date)resultObj[6]);
						 }
						 if(resultObj[7]!=null){
							 afxDte=(Date)resultObj[7];
							 if(InventoryUtility.isNotDefaultDate(afxDte)){
									if(LAST_PRICE_PAID_COST_TYPE.equalsIgnoreCase(newLoc.getCostType())) {
										// set the date of Last Price Paid cost types to a date in the future
										final Date maxDate = InventoryUtility.createMaxDate();
										newLoc.setAfxDate(maxDate);
									} else {
										newLoc.setAfxDate(afxDte);
									}
								}
						 }
						 if(resultObj[8]!=null){
							 totalAmt=(BigDecimal)resultObj[8];
							 newLoc.setTotalValue(totalAmt);
							// Add Each location total to amout to calculate Inventory Total Amount
							 invTotal=invTotal.add(totalAmt);
						 }
						 if(resultObj[9]!=null){
							 newLoc.setLocComments((String)resultObj[9]);
						 }							
							
							locations.add(newLoc);
					 }
				}
				if(locations!=null && !locations.isEmpty()){
				final Map<Integer,Integer> locCntMap=getLocLinesCountByLocation(locIds.toString());
					if(locCntMap!=null){
						for(InvLocation locObj:locations){
							if(locObj!=null && locCntMap.containsKey(locObj.getLocationId())){								
								locObj.setLineCount(locCntMap.get(locObj.getLocationId()));
								if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
									LOGGER.finer("LOC COUNT="+locObj.getLineCount());
								}
							}
						}
					}
				}	
				
			}
			/*List<Location> resultList = inv.getLocations();

			List<InvLocation> locations = new ArrayList<InvLocation>();
			//Calendar calendar = Calendar.getInstance(); 	
			BigDecimal invTotal=BigDecimal.ZERO;
			//Date afxDate=null;
			if (null != resultList && !resultList.isEmpty()) {
				//Number countResult=null;
				for(int i = 0; i < resultList.size(); i++) {
					Location location = resultList.get(i);
					InvLocation newLoc = new InvLocation();
					//java.util.Date parsedDate = df.parse(location.getCostAfxStp().t); 				   
					//calendar.setTime(parsedDate);  

					newLoc.setCostType(location.getCostType().getCostTypeDesc());
					newLoc.setCountType(location.getCountType().getCountTypeDesc());				
					newLoc.setCreateDate(location.getCreateStp());
					newLoc.setInventoryID(location.getInvtry().getInvtryNum());

					if(InventoryUtility.isNotDefaultDate(location.getCostAfxStp())){
						if(LAST_PRICE_PAID_COST_TYPE.equalsIgnoreCase(newLoc.getCostType())) {
							// set the date of Last Price Paid cost types to a date in the future
							Date maxDate = InventoryUtility.createMaxDate();
							newLoc.setAfxDate(maxDate);
						} else {
							newLoc.setAfxDate(location.getCostAfxStp());
						}
					}
					Query locCountQuery = entityMgr.createQuery(locDetailsCountSql);
				locCountQuery.setParameter("locationNum", location.getLocationNum());
					if(location.getLocationDetails()!=null){
						newLoc.setLineCount(location.getLocationDetails().size());	
				}
					newLoc.setLocationId(location.getLocationNum());
					newLoc.setLocationName(location.getLocationNam().trim());
					if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
						LOGGER.finer("<--GET location.getLocationStat().getLocationStatDesc() START------->");
					}
					
					newLoc.setLocStatus(location.getLocationStat().getLocationStatDesc());
					if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
						LOGGER.finer("<--GET location.getLocationStat().getLocationStatDesc() END------->");
					}
					if(location.getTotalLocationValueAmt()!=null){
//						invTotal=invTotal+location.getTotalLocationValueAmt().doubleValue();
						invTotal=invTotal.add((BigDecimal)(location.getTotalLocationValueAmt()));
					}
					newLoc.setTotalValue(location.getTotalLocationValueAmt());
					newLoc.setLocComments(location.getCmntTxt());
					locations.add(newLoc);
				}
			}*/
			if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
				LOGGER.finer("<--GET LOCATION FROM INVENTORY END =------>");
			}
			if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
				LOGGER.finer("<--MAP FINAL RESPONSE START--->");
			}
			
			inventory=new InvInventory();
			inventory.setCreateDate(inv.getCreateStp());
			inventory.setInventoryId(inv.getInvtryNum());
			inventory.setInventoryName(inv.getInvtryNam());
			inventory.setTotalValue(invTotal.doubleValue());
			if(locations!=null && !locations.isEmpty()){
				inventory.setNumberOfLocations(locations.size());
			}
			response.setInventory(inventory);
			response.setLocations(locations);
			response.setLocationStatuses(locStats);
			response.setStatusCode(String.valueOf(ZERO.intValue()));
			response.setStatusMsg("Success.");
			if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
				LOGGER.finer("<-- MAP FINAL RESPONSE END --->");
			}
		} catch (Throwable t) {
			
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY),
							t.getCause()+": "+t.getMessage()));

			throw new SystemException(ErrorHandlingHelper.THROWABLE_ERROR_KEY,
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY), t.getCause()+": "+t.getMessage(), t);

		}
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to router from persister for getLocationSummary------>" +  response);
		}

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}

		return response;
	}
	
/*
 * (non-Javadoc)
 * @see com.cardinal.ws.physicalinventory.PhysicalInventoryPersisterBeanLocal#setLocationsCost(com.cardinal.ws.physicalinventory.location.valueobjects.InvLocationRequest)
 */

	/**
	 * This method calculates the total cost of a location by current cost, last purchased cost
	 * or as of a certain date.  The total cost is updated in the DB
	 */
	@Override
	public InvLocationResponse setLocationsCost(InvLocationRequest request) {
		final String METHOD_NAME = "setLocationsCost";

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);

		}
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("setLocationsCost Request ------->"+ request.toString());
		}

		//final String invLocationDetailSQL = "SELECT dtl FROM LocationDetail dtl WHERE dtl.location.locationNum = :locNum ORDER BY dtl.seqNum ASC";
		InvLocationResponse response=new InvLocationResponse();

		try { 
			
			InvLocationProductCost costObj=null;
			Map<String,InvLocationProductCost>	prodMap=null;
			BigDecimal calLineTotalVal=BigDecimal.ZERO;
			prodMap=request.getProductCostMap();
			Location location=null;
			InvLocation invLocResponse=null;
			List<InvLocation> invLocList=null;
			if(request.getLocs()!=null){
				invLocList=new ArrayList<InvLocation>();
				
				Iterator<Integer> it=request.getLocs().iterator();
				Integer locId=null;
				while(it.hasNext()){
					 locId=(Integer)it.next();				
					 invLocResponse=new InvLocation();
					
					location = entityMgr.find(Location.class,locId);
					
					int locStatCde = location.getLocationStat().getLocationStatCde();
					// do not set cost of locations that are closed
					if(locStatCde == InventoryConstants.CLOSED_STATUS_CODE) {
						LOGGER.warning("Can't set cost for a closed location: " + locId);
					} else {
						final List<LocationDetail> updatedLines=new ArrayList<LocationDetail>();
						BigDecimal  calLocTotalVal=BigDecimal.ZERO;

						final List<LocationDetail> resultList =location.getLocationDetails();	

						for(int i = 0; i < resultList.size(); i++) {

							LocationDetail detailLine = resultList.get(i);
							if(detailLine!=null){
								int prodIdCode=detailLine.getProdIdType().getProdIdTypeCde();
								if(prodIdCode==0 && prodMap.containsKey(detailLine.getProdId())){
									costObj=prodMap.get(detailLine.getProdId());
									if(costObj!=null ){
										detailLine.setCostAmt(costObj.getCost());
										detailLine.setCostUoifNum(costObj.getUoif());	
										calLineTotalVal=calculateLineTotalValue(detailLine.getCountType().getCountTypeCde(),detailLine.getManualCostOvrdAmt(),detailLine.getLineLevelQty(),costObj.getCost(),costObj.getUoiCost());
									}else{
										detailLine.setCostAmt(detailLine.getManualCostOvrdAmt());
										calLineTotalVal=calculateLineTotalValue(detailLine.getCountType().getCountTypeCde(),detailLine.getManualCostOvrdAmt(),detailLine.getLineLevelQty(),null,null);
									}

								}else{
									detailLine.setCostAmt(detailLine.getManualCostOvrdAmt());
									calLineTotalVal=calculateLineTotalValue(detailLine.getCountType().getCountTypeCde(),detailLine.getManualCostOvrdAmt(),detailLine.getLineLevelQty(),null,null); 
								}
								calLocTotalVal=calLocTotalVal.add(calLineTotalVal);
								detailLine.setCalcTotalValueAmt(calLineTotalVal);
								detailLine.setLastUpdateUserNam(request.getUserName());
								detailLine.setRowUpdateStp(new Timestamp(System.currentTimeMillis()));
								detailLine.setRowUpdateUserId(request.getUserId());
								updatedLines.add(detailLine);				
							}

						}
						location.setTotalLocationValueAmt(calLocTotalVal);
						if(request.getCostAsOfDate()!=null){
							location.setCostAfxStp(new Timestamp(request.getCostAsOfDate().getTime()));
						}else{
							location.setCostAfxStp(new Timestamp(System.currentTimeMillis()));
						}
						location.setCostType(entityMgr.find(CostType.class, request.getCostType()));
						location.setLocationStat(entityMgr.find(LocationStat.class,InventoryConstants.COST_SET_STATUS_CODE));
						location.setLocationDetails(updatedLines);
						location.setRowUpdateStp(new Timestamp(System.currentTimeMillis()));
						location.setRowUpdateUserId(request.getUserId());

						invLocResponse.setCostType(location.getCostType().getCostTypeDesc());
						invLocResponse.setLocationId(location.getLocationNum());
						invLocResponse.setLocationName(location.getLocationNam());
						invLocResponse.setLocComments(location.getCmntTxt());
						invLocResponse.setLocStatus(location.getLocationStat().getLocationStatDesc());
						invLocResponse.setAfxDate(location.getCostAfxStp());
						invLocList.add(invLocResponse);
						entityMgr.merge(location);
					} 
				}
				entityMgr.flush();
			}

			response.setLocations(invLocList);
			response.setPageNum(request.getPageNum());
			response.setSortColumns(request.getSortColumns());
			response.setSortOrder(request.getSortOrder());
			response.setStatusCode(InventoryConstants.PHY_INVENTORY_SUCCESS);
			response.setStatusMsg(InventoryConstants.SUCCESSFULLY_COST_SET_LCOATION_MESSAGE);

		} catch (Throwable t) {        
			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY),
							t.getCause()+": "+t.getMessage()));

			throw new SystemException(ErrorHandlingHelper.THROWABLE_ERROR_KEY,
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY), t.getCause()+": "+t.getMessage(), t);

		}
		
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("Response sent to router from persister for getLocationDetails------>" +  response);
		}

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}

		return response;
	}


	
	
	/**
	 * This method Calculates the total values for each locations corresponding to a Inventory
	 * with Location status as "CLOSED" (4).Then update the total value for that inventory.
	 * 
	 * @param request
	 *
	 * @return the InvInventoryResponse response
	 *
	 */
	public InvInventoryResponse updateInventory(InvInventoryRequest request) {
		
		final String METHOD_NAME = "updateInventory";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);	
		}
		
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("updateInventory Request ------->"+ request.toString());
		}
		InvInventoryResponse response = new InvInventoryResponse();
		
		try {
			if(isInventoryNameUnique(request)){
				Invtry inventory =  entityMgr.find(Invtry.class, request.getInventory().getInventoryId());
				if(inventory!=null){
					inventory.setRowUpdateStp(new Timestamp(System.currentTimeMillis()));
					inventory.setRowUpdateUserId(request.getUserId().trim());
					inventory.setInvtryNam(request.getInvName());

					entityMgr.merge(inventory);
					entityMgr.flush();

					response.setStatusCode(InventoryConstants.PHY_INVENTORY_SUCCESS);
					response.setStatusMsg(InventoryConstants.SUCCESSFULLY_UPDATED_INVENTORY);
				}else{
					LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
							request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
							InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION,
							InventoryConstants.INVENTORY_DOES_NOT_EXITS));
					response.setStatusCode(InventoryConstants.INVALID_STATUS_CODE);
					response.setStatusMsg(InventoryConstants.INVENTORY_DOES_NOT_EXITS);
				}
			}else{
				LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
						request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
						InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION,
						InventoryConstants.DUPLICATE_INVENTORY_NAME_MESSAGE));
				response.setStatusCode(InventoryConstants.DUPLICATE_STATUS_CODE);
				response.setStatusMsg(InventoryConstants.DUPLICATE_INVENTORY_NAME_MESSAGE);
			}

		} catch (Throwable t) {

			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY),
							t.getCause()+": "+t.getMessage()));

			throw new SystemException(ErrorHandlingHelper.THROWABLE_ERROR_KEY,
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY), t.getCause()+": "+t.getMessage(), t);

		}
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}

		return response;
	}
	
	
	
	
	/**
	 * This method takes request as InvLocationRequest which contains list of location id(s) and status to update.
	 * location id(s) iterate through the list and updates the status to Open/Counted/Closed based on request.
	 * The status will only update to other if location status not updated.
	 * After successful update response will include status code as Success and Status message to "Location(s) status successfully updated." 
	 *  
	 */
	
	@Override
	public InvLocationResponse updateLocationStatus(InvLocationRequest request) {

		final String METHOD_NAME = "updateLocationStatus";
			
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);	
		}
		
		if(MessageLoggerHelper.isTraceEnabled(LOGGER)){
			LOGGER.finest("Location Status: "+ request.getStatusType());
		}
		
		InvLocationResponse response = new InvLocationResponse();
		StringBuffer locationKeys =null;
		StringBuffer updateLocBuffer = new StringBuffer();
		
		try {
			if(null != request.getLocs() && !request.getLocs().isEmpty()){

				for(Integer loc : request.getLocs()){
					if(null == locationKeys){
						locationKeys = new StringBuffer();
						locationKeys.append(loc);
					}
					else{
						locationKeys.append(InventoryConstants.COMMA);
						locationKeys.append(loc);
					}
				}			
				final Timestamp now = new Timestamp(System.currentTimeMillis());
				
				updateLocBuffer.append("UPDATE ");
				updateLocBuffer.append("OE_PI.LOCATION loc ");
				updateLocBuffer.append("SET loc.LOCATION_STAT_CDE =");
				updateLocBuffer.append(request.getStatusType());
				updateLocBuffer.append(InventoryConstants.COMMA);
				updateLocBuffer.append(" loc.ROW_UPDATE_USER_ID='");
				updateLocBuffer.append(request.getUserId());
				updateLocBuffer.append(InventoryConstants.APOSTROPHE);
				
				updateLocBuffer.append(",loc.ROW_UPDATE_STP='");
				updateLocBuffer.append(now.toString());
				updateLocBuffer.append(InventoryConstants.APOSTROPHE);
				updateLocBuffer.append(" WHERE ");
				updateLocBuffer.append("loc.LOCATION_NUM IN (");
				updateLocBuffer.append(locationKeys.toString());
				updateLocBuffer.append(InventoryConstants.CLOSE_PAREN);
				updateLocBuffer.append(" AND ");
				updateLocBuffer.append(" loc.LOCATION_STAT_CDE<>");
				updateLocBuffer.append(InventoryConstants.CLOSED_STATUS_CODE);

				Query updateLoc = entityMgr.createNativeQuery(updateLocBuffer.toString());

				int locationsUpdated = updateLoc.executeUpdate();

				if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
					LOGGER.finer("Locations Updated ------->"+ locationsUpdated);
				}

				response.setStatusCode(InventoryConstants.PHY_INVENTORY_SUCCESS);
				response.setStatusMsg("Location(s) status successfully updated.");

				//If the status is Other than "Closed", then set the sell amt for location details,total value for locations to 0.
				//Otherwise, Fetch the product cost based on cost type and update the location details and location table
				/*if(request.getStatusType() != 4){
				updateInvForStatus(request);
			}else{
				updateInvForClosedStatus(request);
			}*/

				//Now Recalculate the Total value amount for the inventory after the location and location detail records are updated.
				//recalInventoryTotalAmt(request);
			}
		} catch (Throwable t) {

			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY),
							t.getCause()+": "+t.getMessage()));

			throw new SystemException(ErrorHandlingHelper.THROWABLE_ERROR_KEY,
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY), t.getCause()+": "+t.getMessage(), t);

		}
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}

		return response;
		
	}
	
	/**
	 * This method reorders the details of a location by sequence number
	 */
	@Override
	public InvLocationDetailResponse reOrderLocationDetails(InvLocationDetailRequest request) {

		
		final String METHOD_NAME = "reOrderLocationDetails";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);	
		}
		
		InvLocationDetailResponse response = new InvLocationDetailResponse();
		
		try {
			if(null != request.getInvlocLines() && 0!=request.getInvlocLines().length){
				final String invLocationReorderDetailSQL = "SELECT dtl FROM LocationDetail dtl WHERE dtl.location.locationNum = :locNum ORDER BY dtl.seqNum ASC";

				Query invLocationReorderDetailQuery = 
					entityMgr.createQuery(invLocationReorderDetailSQL);
				invLocationReorderDetailQuery.setParameter("locNum", 
						request.getLocation().getLocationId());
				//invLocationReorderDetailQuery.setParameter("seqNum", request.getInvlocLines()[0].getSeq());
				if(0 != request.getInvlocLines()[0].getLocLineId()){
					List<LocationDetail> resultList = invLocationReorderDetailQuery.getResultList();
					LocationDetail updateLocationDetail = null;
					updateLocationDetail = entityMgr.find(LocationDetail.class, request.getInvlocLines()[0].getLocLineId());
					final Timestamp now = new Timestamp(System.currentTimeMillis());
					if(null != resultList && !resultList.isEmpty()) {
						int seqCount = request.getInvlocLines()[0].getSeq();						
						//Defect#16058: Assigning sequence value 1 for line if it is seq value coming in request is 0 
						if(seqCount == 0){
							request.getInvlocLines()[0].setSeq(1);
							seqCount = 1;
						}
						if(seqCount > resultList.get(resultList.size()-1).getSeqNum()){
							request.getInvlocLines()[0].setSeq(resultList.size());							
							int count = 1;
							for(int i = 0; i < resultList.size(); i++) {
								LocationDetail otherLocationDetail = null;
								LocationDetail detailLine = resultList.get(i);
								if(detailLine.getLocationNum() != updateLocationDetail.getLocationNum()){
									otherLocationDetail = entityMgr.find(LocationDetail.class, detailLine.getLocationNum());
									otherLocationDetail.setSeqNum(count);
									otherLocationDetail.setRowUpdateStp(now);
									otherLocationDetail.setRowAddUserId(request.getUserId());
									entityMgr.persist(otherLocationDetail);
									count++;
								}
							}
						 //Defect #16058: End
						}else{

							for(int i = 0; i < resultList.size(); i++) {
								LocationDetail otherLocationDetail = null;
								LocationDetail detailLine = resultList.get(i);
								otherLocationDetail = entityMgr.find(LocationDetail.class, detailLine.getLocationNum());
								if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
									LOGGER.fine("otherLocationDetail>>>>"+otherLocationDetail.getLocationNum());
									LOGGER.fine("otherLocationDetail  seq :>>>>"+otherLocationDetail.getSeqNum());
									LOGGER.fine("updateLocationDetail  seq :>>>>"+updateLocationDetail.getSeqNum());
									LOGGER.fine("updateLocationDetail>>>>"+updateLocationDetail.getLocationNum());
								}
								if(otherLocationDetail.getSeqNum()<= request.getInvlocLines()[0].getSeq() && otherLocationDetail.getSeqNum()> updateLocationDetail.getSeqNum()){

									LOGGER.fine("Inside IF");
									otherLocationDetail.setSeqNum(otherLocationDetail.getSeqNum()-1);
									otherLocationDetail.setRowUpdateStp(now);
									otherLocationDetail.setRowAddUserId(request.getUserId());
									entityMgr.persist(otherLocationDetail);
								} else if(otherLocationDetail.getSeqNum()== request.getInvlocLines()[0].getSeq() && otherLocationDetail.getSeqNum() < updateLocationDetail.getSeqNum() ){
									
									LOGGER.fine("Inside elseIF");
									seqCount++;
									otherLocationDetail.setSeqNum(seqCount);
									otherLocationDetail.setRowUpdateStp(now);
									otherLocationDetail.setRowAddUserId(request.getUserId());
									entityMgr.persist(otherLocationDetail);
									
								} else if(otherLocationDetail.getSeqNum() > request.getInvlocLines()[0].getSeq() && otherLocationDetail.getSeqNum() < updateLocationDetail.getSeqNum()){

									LOGGER.fine("Inside second elseIF");
									seqCount++;
									otherLocationDetail.setSeqNum(seqCount);
									otherLocationDetail.setRowUpdateStp(now);
									otherLocationDetail.setRowAddUserId(request.getUserId());
									entityMgr.persist(otherLocationDetail);
								}
							}
						}
					}
					if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
						LOGGER.fine("seq changes:>>>>"+request.getInvlocLines()[0].getSeq());
					}
					updateLocationDetail.setSeqNum(request.getInvlocLines()[0].getSeq());
					updateLocationDetail.setRowUpdateStp(now);
					updateLocationDetail.setRowAddUserId(request.getUserId());
					entityMgr.persist(updateLocationDetail);
					
					// also do row updates on the location (ROW_UPDATE_STP and ROW_UPDATE_USR_ID)
					int locId = request.getLocation().getLocationId();
					String userId = request.getUserId();
					updateLocationRowUpdateStpAndUser(locId, userId, now);
					
					if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
						LOGGER.fine("updateLocationDetail  seq :>>>>"+updateLocationDetail.getSeqNum());
					}


				}
				entityMgr.flush();
			}
			response.setStatusCode(InventoryConstants.PHY_INVENTORY_SUCCESS);
			response.setStatusMsg("Success.");		

		} catch (Throwable t) {

			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY),
							t.getCause()+": "+t.getMessage()));

			throw new SystemException(ErrorHandlingHelper.THROWABLE_ERROR_KEY,
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY), t.getCause()+": "+t.getMessage(), t);

		}
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}
		return response;	

	}
	
	/**
	 * This method updates the quantity, override cost, comment, and count type of a location detail
	 */
	@Override
	public InvLocationDetailResponse updateLocationDetails(InvLocationDetailRequest request) {


		final String METHOD_NAME = "updateLocationDetails";

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);	
		}

		InvLocationDetailResponse response = new InvLocationDetailResponse();
		
		try {	
			
			// If location is closed, do not update any location details
			int locCde = getLocationStatus(request.getLocation().getLocationId());
			if(locCde == InventoryConstants.CLOSED_STATUS_CODE) {
				response.setStatusCode("CLOSEDLOC");
				response.setStatusMsg("This location is closed, can't update loc detail");
				
				return response;
			}
			
			StringBuffer updateLocDetBuffer = new StringBuffer();

			int locationsDetUpdated =0;
			final Timestamp now = new Timestamp(System.currentTimeMillis());
			
			if(null != request.getInvlocLines() && 0 !=request.getInvlocLines().length){
				boolean updateFlag=false;
				for(InvLocProductRequest line : request.getInvlocLines()){		
					
					updateLocDetBuffer.append("UPDATE ");
					updateLocDetBuffer.append(" OE_PI.LOCATION_DETAIL lcd ");
					updateLocDetBuffer.append("SET ");
					if(line.getQuantity()!=null){
						updateFlag=true;
					updateLocDetBuffer.append(" lcd.LINE_LEVEL_QTY = ");
					updateLocDetBuffer.append(line.getQuantity().doubleValue());
					}
					if(line.getOverrideCost()!=null){
						if(updateFlag){
						updateLocDetBuffer.append(", ");
					 	}
						updateFlag=true;
						updateLocDetBuffer.append(" lcd.MANUAL_COST_OVRD_AMT = ");
						updateLocDetBuffer.append(line.getOverrideCost());
					}
					if(line.getLineComments()!=null){
						if(updateFlag){
							updateLocDetBuffer.append(", ");
						 	}
							updateFlag=true;
						updateLocDetBuffer.append(" lcd.CMNT_TXT = \'");
						updateLocDetBuffer.append(line.getLineComments());
						updateLocDetBuffer.append(SLASH_APOS);
					}
					if(line.getCountType()!=null){
						if(updateFlag){
							updateLocDetBuffer.append(", ");
						 	}
							updateFlag=true;
						updateLocDetBuffer.append(" lcd.COUNT_TYPE_CDE = ");
						updateLocDetBuffer.append(line.getCountType());
					}
					updateLocDetBuffer.append(InventoryConstants.COMMA);
					updateLocDetBuffer.append(" lcd.ROW_UPDATE_USER_ID='");
					updateLocDetBuffer.append(request.getUserId());
					updateLocDetBuffer.append(InventoryConstants.APOSTROPHE);
					
					updateLocDetBuffer.append(",lcd.ROW_UPDATE_STP='");
					updateLocDetBuffer.append(now.toString());
					updateLocDetBuffer.append(InventoryConstants.APOSTROPHE);
					updateLocDetBuffer.append(" WHERE ");
					updateLocDetBuffer.append("lcd.LOCATION_DETAIL_NUM = ");
					updateLocDetBuffer.append(line.getLocLineId());

					if(updateFlag){
						Query updateLocDet = entityMgr.createNativeQuery(updateLocDetBuffer.toString());
					
						locationsDetUpdated = locationsDetUpdated + updateLocDet.executeUpdate();
						
					}
				}	

				if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
					LOGGER.finer("Location Details Updated ------->"+ locationsDetUpdated);
				}



				//entityMgr.flush();
				InvLocation locUpdated = new InvLocation();
				String status="";
				locUpdated.setCountType("");
				locUpdated.setLocationId(request.getLocation().getLocationId());
				if(locationsDetUpdated==1){
					locationsDetUpdated=resetLocationSetCostStatus(request.getLocation().getLocationId(), request.getUserId());
					if(locationsDetUpdated==1){
						status="Open";
					}
					// also do row updates on the location (ROW_UPDATE_STP and ROW_UPDATE_USR_ID)
					int locId = request.getLocation().getLocationId();
					String userId = request.getUserId();
					updateLocationRowUpdateStpAndUser(locId, userId, now);
				}

				entityMgr.flush();
				locUpdated.setLocStatus(status);
				response.setLocation(locUpdated);
			}

			response.setStatusCode(String.valueOf(ZERO.intValue()));
			response.setStatusMsg("Success.");		

		} catch (Throwable t) {

			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY),
							t.getCause()+": "+t.getMessage()));

			throw new SystemException(ErrorHandlingHelper.THROWABLE_ERROR_KEY,
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY), t.getCause()+": "+t.getMessage(), t);

		}

		return response;
	}
	/**
	 * This method takes Location Id and user id as request, then reset the location status to OPEN(0) if Location  current not closed  and Current Status Set Cost Status.
	 * if record updated then which return as true otherwise  false.
	 * @param locId
	 * @param userId
	 * @return
	 */
	private int resetLocationSetCostStatus(final int locId,final String userId){
		final String METHOD_NAME = "resetLocationSetCostStatus";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);	
		}
		final Timestamp now = new Timestamp(System.currentTimeMillis());
	//Location locObj = entityMgr.find(Location.class, locId);
		StringBuffer updateLocBuffer=new StringBuffer(); // NOPMD by stephen.perry01 on 5/24/13 4:30 PM
		updateLocBuffer.append("UPDATE ");
		updateLocBuffer.append("OE_PI.LOCATION loc ");
		updateLocBuffer.append("SET loc.LOCATION_STAT_CDE =");
		updateLocBuffer.append(InventoryConstants.OPEN_STATUS_CODE);
		updateLocBuffer.append(InventoryConstants.COMMA);
		updateLocBuffer.append(" loc.ROW_UPDATE_USER_ID='");
		updateLocBuffer.append(userId);
		updateLocBuffer.append(InventoryConstants.APOSTROPHE);
		
		updateLocBuffer.append(",loc.ROW_UPDATE_STP='");
		updateLocBuffer.append(now.toString());
		updateLocBuffer.append(InventoryConstants.APOSTROPHE);
		updateLocBuffer.append(" WHERE ");
		updateLocBuffer.append("loc.LOCATION_NUM=");
		updateLocBuffer.append(locId);
		updateLocBuffer.append(" AND ");
		updateLocBuffer.append(" loc.LOCATION_STAT_CDE<>");
		updateLocBuffer.append(InventoryConstants.CLOSED_STATUS_CODE);
		updateLocBuffer.append(" AND ");
		updateLocBuffer.append(" loc.LOCATION_STAT_CDE=");
		updateLocBuffer.append(InventoryConstants.COST_SET_STATUS_CODE);
		Query updateLoc = entityMgr.createNativeQuery(updateLocBuffer.toString());
		
		int locationsUpdated = updateLoc.executeUpdate();
		
	if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("Locations Updated ------->"+ locationsUpdated);
		}
		
		
	 return locationsUpdated;		
	}
	// AD Factory 15.5 Release: Physical Inventory Performance Improvement.
	// Created new method for JDBC Sqls.
	/**
	 * This method takes Location Id and user id as request, then reset the location status to OPEN(0) if Location  current not closed  and Current Status Set Cost Status.
	 * if record updated then which return as true otherwise  false.
	 * @param locId
	 * @param userId
	 * @param updateLocPrepStmt2 
	 * @return
	 * @throws SQLException 
	 */
	private int resetLocationSetCostStatusForCreateLocation(final int locId,final String userId, Connection connection, PreparedStatement updateLocPrepStmt ) throws SQLException{// NOPMD
		final String METHOD_NAME = "resetLocationSetCostStatusForCreateLocation";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);	
		}
		final Timestamp now = new Timestamp(System.currentTimeMillis());
	
		StringBuffer updateLocBuffer=new StringBuffer(); // NOPMD by stephen.perry01 on 5/24/13 4:30 PM
		updateLocBuffer.append("UPDATE ");
		updateLocBuffer.append("OE_PI.LOCATION loc ");
		updateLocBuffer.append("SET loc.LOCATION_STAT_CDE =");
		updateLocBuffer.append(InventoryConstants.OPEN_STATUS_CODE);
		updateLocBuffer.append(InventoryConstants.COMMA);
		updateLocBuffer.append(" loc.ROW_UPDATE_USER_ID='");
		updateLocBuffer.append(userId);
		updateLocBuffer.append(InventoryConstants.APOSTROPHE);
		
		updateLocBuffer.append(",loc.ROW_UPDATE_STP='");
		updateLocBuffer.append(now.toString());
		updateLocBuffer.append(InventoryConstants.APOSTROPHE);
		updateLocBuffer.append(" WHERE ");
		updateLocBuffer.append("loc.LOCATION_NUM=");
		updateLocBuffer.append(locId);
		updateLocBuffer.append(" AND ");
		updateLocBuffer.append(" loc.LOCATION_STAT_CDE<>");
		updateLocBuffer.append(InventoryConstants.CLOSED_STATUS_CODE);
		updateLocBuffer.append(" AND ");
		updateLocBuffer.append(" loc.LOCATION_STAT_CDE=");
		updateLocBuffer.append(InventoryConstants.COST_SET_STATUS_CODE);
		updateLocPrepStmt = connection.prepareStatement(updateLocBuffer.toString());
		int locationsUpdated = updateLocPrepStmt.executeUpdate();
		
	if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("Locations Updated ------->"+ locationsUpdated);
		}
		
		
	 return locationsUpdated;		
	}
	
	/**
	 * This method updates a location's ROW_UPDATE_STP and ROW_UPDATE_USR_ID columns
	 * @param locId The location to update
	 * @param userId The userId updating the location
	 * @param ts The updated timestamp of the location
	 * @param connection 
	 */
	private void updateLocationRowUpdateStpAndUser(final int locId, final String userId,
			final Timestamp ts) {
		final String METHOD_NAME = "updateLocationRowUpdateStpAndUser";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);	
		}
		
		StringBuffer updateLocBuffer=new StringBuffer(); // NOPMD by stephen.perry01 on 5/24/13 4:30 PM
		updateLocBuffer.append("UPDATE ");
		updateLocBuffer.append("OE_PI.LOCATION loc ");
		updateLocBuffer.append("SET loc.ROW_UPDATE_USER_ID='");
		updateLocBuffer.append(userId);
		updateLocBuffer.append(InventoryConstants.APOSTROPHE);
		updateLocBuffer.append(",loc.ROW_UPDATE_STP='");
		updateLocBuffer.append(ts.toString());
		updateLocBuffer.append(InventoryConstants.APOSTROPHE);
		updateLocBuffer.append(" WHERE ");
		updateLocBuffer.append("loc.LOCATION_NUM=");
		updateLocBuffer.append(locId);
		
		Query updateLoc = entityMgr.createNativeQuery(updateLocBuffer.toString());
		updateLoc.executeUpdate();
		
		if(MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}
	}
	
	/**
	 * Retrieves the location status description
	 * @param locId
	 * @return
	 */
	private int getLocationStatus(final int locId) {
		final String METHOD_NAME = "getLocationStatus";
		
		// default is non-existent code
		int locStatCde = -1;
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);	
		}
		Location loc = entityMgr.find(Location.class, locId);
		locStatCde = loc.getLocationStat().getLocationStatCde();
		
		if(MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}		
		return locStatCde;
	}
	// AD Factory Release 15.5 : Import by CSN to Physical Inventory
	/**
	 * This method fetches list of CINs against CSN 
	 * @param productCatalogServiceMap 
	 * @param productData 
	 */
	@Override
	public HashMap<String, ArrayList<String>> fetchCINsfromCSN(final InvImportRequest request , ArrayList<String> csnList, List<String> productCatalogServiceMap){
		final String METHOD_NAME = "fetchCINsfromCSN";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);	
		}
		Connection connection = null; // NOPMD Connection has been closed in Finally block
		PreparedStatement fetchCSNStmt = null;// NOPMD PreparedStatement has been closed in Finally block
		ResultSet rsCinList = null;// NOPMD ResultSet	 has been closed in Finally block
		HashMap<String,ArrayList<String>> csnCINMap = new HashMap<String, ArrayList<String>>();
		ArrayList<String> cinList = new ArrayList<String>();
		try{
			
			connection = getConnection(InventoryConstants.PHYSICAL_INVENTORY_JNDI_CMDS);
			final StringBuffer sql = new StringBuffer();
						
			sql.append(getSQLQuery(InventoryConstants.PHYSICAL_INV_FETCH_CIN_QUERY_1));
			sql.append(createInClause(csnList));
			
			LOGGER.info("CSN String SQL: "+sql.toString());
			fetchCSNStmt = connection.prepareStatement(sql.toString());
			
			fetchCSNStmt.setString(1, request.getDistributionCenter());
			fetchCSNStmt.setString(2, request.getShipToCustomer());
			int i =3;
			for (String csn : csnList) {
				fetchCSNStmt.setString(i, csn);
				i++;
			}
			
			rsCinList = fetchCSNStmt.executeQuery();// NOPMD ResultSet	 has been closed in Finally block
			
			while(rsCinList.next()){
				
				String csn = rsCinList.getString(1);
				String cin = rsCinList.getString(2);
				String nonCardCin = rsCinList.getString(3);
				
				if("-1".equals(cin)){
					cin = nonCardCin;
				}
				productCatalogServiceMap.add(cin);
				
				if(null == csnCINMap.get(csn)){
					
					cinList = new ArrayList<String>();
					cinList.add(cin);				
					csnCINMap.put(csn,cinList);
				}else{	
					csnCINMap.get(csn).add(cin);
				}				
			}	
		}catch (Throwable t) {

			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY),
							t.getCause()+": "+t.getMessage()));

			throw new SystemException(ErrorHandlingHelper.THROWABLE_ERROR_KEY,
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY), t.getCause()+": "+t.getMessage(), t);

		}finally{
			DataSourceManager.freeConnection(connection, fetchCSNStmt, rsCinList);
		}
		
		if(MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}
		return csnCINMap;
	}
	// AD Factory 15.5 Release
	/**
	 * createInClause : for creating csn List
	 * 
	 * @param List
	 *            of csn
	 * 
	 * @return StringBuffer
	 */
    	private StringBuffer createInClause(final List<String> csns) {
    		final StringBuffer csnList = new StringBuffer();
    		csnList.append(InventoryConstants.IN);
    		csnList.append(InventoryConstants.EMPTY_STRING);
    		csnList.append(InventoryConstants.LEFT_PARENTHESIS);
    		for (int i = 0; i < csns.size(); i++) {
    			if (i > 0 && i <= csns.size()) {
    				csnList.append(InventoryConstants.COMMA);
    			}
    			if (i <= csns.size()) {
    				csnList.append(InventoryConstants.QUESTION_MARK);
    			}
    		}

    		csnList.append(InventoryConstants.RIGHT_PARENTHESIS);
    		return csnList;
    	}
    	
    	private StringBuffer createIntClause(final List<Integer> location) {
    		final StringBuffer csnList = new StringBuffer();
    		csnList.append(InventoryConstants.IN);
    		csnList.append(InventoryConstants.EMPTY_STRING);
    		csnList.append(InventoryConstants.LEFT_PARENTHESIS);
    		for (int i = 0; i < location.size(); i++) {
    			if (i > 0 && i <= location.size()) {
    				csnList.append(InventoryConstants.COMMA);
    			}
    			if (i <= location.size()) {
    				csnList.append(InventoryConstants.QUESTION_MARK);
    			}
    		}

    		csnList.append(InventoryConstants.RIGHT_PARENTHESIS);
    		return csnList;
    	}

	/**
	 * This method adds location details from an import list to the location
	 */
	@Override
	public InvImportResponse addimportedItems(final InvImportRequest request) {

		
		final String METHOD_NAME = "addimportedItems";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);	
		}
		
		InvImportResponse response = new InvImportResponse();
		
		try {
			HashMap<String,String> validCINMap = null;
			final InvLocationDetailRequest locRequest = new InvLocationDetailRequest();
			List<InvLocProductRequest> productToAddList = new ArrayList<InvLocProductRequest>(); 
			final InvLocation loc = new InvLocation();
			loc.setLocationId(Integer.valueOf(request.getLocationId()));
			locRequest.setLocation(loc);
			validCINMap = request.getValidCINMapToImport();
			final byte[] byteStream =request.getImportFile();
			final String byteString = new String(byteStream);
			if(!validCINMap.isEmpty() && (byteString != null)) {
				
				String [] lines = byteString.split("\\n");
				// AD Factory 15.5 Release : Import By CSN to Physical Inventory: START
				if(request.isCsnImport()){
					productToAddList = fetchProductsToBeAddedForCSN(request, validCINMap,
							productToAddList, lines);
				}else{
					productToAddList = fetchProductsToBeAddedForCIN(request, validCINMap, 
							productToAddList, lines);
				}
				// AD Factory 15.5 Release : Import By CSN to Physical Inventory: END
				
			}
			
			InvLocProductRequest[] locPrdReqArr = new InvLocProductRequest[productToAddList.size()];

			locRequest.setInvlocLines(productToAddList.toArray(locPrdReqArr));
			locRequest.setDistributionCenter(request.getDistributionCenter());
			locRequest.setShipToCustomer(request.getShipToCustomer());
			locRequest.setUserId(request.getUserId());
			locRequest.setUserName(request.getUserName());
			InvLocationDetailResponse addLocationDetail = createLocationDetail(locRequest);
			response.setStatusCode(addLocationDetail.getStatusCode());
			response.setStatusMsg(addLocationDetail.getStatusMsg());		
		} catch (Throwable t) {

			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY),
							t.getCause()+": "+t.getMessage()));

			throw new SystemException(ErrorHandlingHelper.THROWABLE_ERROR_KEY,
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY), t.getCause()+": "+t.getMessage(), t);

		}
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);	
		}
		return response;
	}

	/**
	 * @param request
	 * @param validCINMap
	 * @param productToAddList
	 * @param lines
	 */
	private List<InvLocProductRequest> fetchProductsToBeAddedForCIN(final InvImportRequest request,
			HashMap<String, String> validCINMap,
			List<InvLocProductRequest> productToAddList, String[] lines) {
		
		String METHOD_NAME = "fetchProductsToBeAddedForCIN";
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);	
		}
		
		for(int i=0;i<lines.length;i++){
			if(lines[i].length() > 0){
				InvLocProductRequest productToAdd = new InvLocProductRequest();
				// set default count type (FULL) and quantity (0) in case
				//   file does not specify
				productToAdd.setCountType("0"); 
				productToAdd.setQuantity(new Double(0));

				final String productData[] = lines[i].split(",");
				boolean isEmpty = InventoryUtility.isEmptyImportLine(productData);
				if(!isEmpty) {
					for(int j=0; j<productData.length ; j++){
						// j==0 is the product num (cin,ndc,etc)
						if(j==0){
							String corpNumString = productData[j].replace("-", "");
							corpNumString = corpNumString.replace("\"", "");
							corpNumString = corpNumString.replace("\r", "");
							corpNumString = corpNumString.replace("\n", "");

							String cinMapSearchString = corpNumString;
							
							// truncate corpNumString if necessary - DB only supports 25 chars
							if(corpNumString.length() >= 25) {
								corpNumString = corpNumString.substring(0, 25);
							}
							
							if(corpNumString.matches("^[0-9]+([;-][0-9]+)?$")){
								if(corpNumString.length() == 6){
									cinMapSearchString = "10"+corpNumString;
									//corpNumString = "10"+corpNumString;
								}
																	
								// check map for the string
								if(validCINMap.containsKey(cinMapSearchString)){											
									if(validCINMap.get(cinMapSearchString).equalsIgnoreCase("MULTIPLE")){
										productToAdd.setProduct(corpNumString);
										productToAdd.setProductType("2");
									}else if(validCINMap.get(cinMapSearchString).equalsIgnoreCase("INVALID")){
										productToAdd.setProduct(corpNumString);
										productToAdd.setProductType("1");
									}else{
										productToAdd.setProduct(validCINMap.get(cinMapSearchString));												
										productToAdd.setProductType("0");
									}

								} else {
									LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
											request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
											InventoryConstants.ErrorKeys.PHYS_INV_IMPORT_PROD_INVALID,
											METHOD_NAME + " : " + lines[i] + " - Prod number not found in valid CIN map : " +
											corpNumString + ".  Adding as Invalid product type"));
									productToAdd.setProduct(corpNumString);
									productToAdd.setProductType("1");
								}
							} else if (validCINMap.get(cinMapSearchString).equalsIgnoreCase("INVALID")){
								LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
										request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
										InventoryConstants.ErrorKeys.PHYS_INV_IMPORT_PROD_INVALID,
										METHOD_NAME + " : " + lines[i] + " - Prod number does not match regular " +
										"expression ^[0-9]+([;-][0-9]+)?$ : " + corpNumString + 
								".  Adding as Invalid product type"));
								
								productToAdd.setProduct(corpNumString);
								productToAdd.setProductType("1");
							}
						}
						// j==1 is the count type, PARTIAL or FULL
						if(j==1){
							if(productData[j] != null) {
								String countTypeString = productData[j].trim();
								if("PARTIAL".equalsIgnoreCase(countTypeString)){
									productToAdd.setCountType("1");
								} else { // any other string will result in a Full type
									productToAdd.setCountType("0");
								}
							} else {
								// set 0 as count type if none specified
								productToAdd.setCountType("0");
								LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
										request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
										InventoryConstants.ErrorKeys.PHYS_INV_IMPORT_PROD_INVALID,
										METHOD_NAME + " : " + lines[i] + " - Count Type not specified - default is FULL"));
							}
						}
						// j==2 is the Quantity
						if(j==2){
							if(productData[j] != null) {
								String quantity = productData[j].trim();
								if (quantity.isEmpty()) {
									// set qty as 0 if not specified
									productToAdd.setQuantity(new Double(0));
								} else if(!quantity.matches("^(\\d{0,4}+\\.?\\d{0,2})$")){
									// failed check for format xxxx.xx, but check if numeric
									boolean isNumeric = quantity.matches("[-+]?\\d*\\.?\\d+");
									if(isNumeric) {
										if(productToAdd.getCountType().equals("1")) {
											double qty = InventoryUtility.formatImportQty(quantity);
											int intQuantity = (int)qty;
											productToAdd.setQuantity((double)intQuantity);
										} else {
											double qty = InventoryUtility.formatImportQty(quantity);
											productToAdd.setQuantity(qty);
										}
									} else {
										productToAdd.setQuantity(new Double(0));
										LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
												request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
												InventoryConstants.ErrorKeys.PHYS_INV_IMPORT_PROD_INVALID,
												METHOD_NAME + " : " + lines[i] + " - Qty is not numeric, default - 0"));
									}
								} else{
									// if Count Type is PARTIAL, return only whole numbers
									if(productToAdd.getCountType().equals("1")) {
										double qty = InventoryUtility.formatImportQty(quantity);
										int intQuantity = (int)qty;
										productToAdd.setQuantity((double)intQuantity);
									}else {
										double qty = InventoryUtility.formatImportQty(quantity);
										productToAdd.setQuantity(qty);
									}
								}
							} else {
								// send 0 as qty if not specified
								productToAdd.setQuantity(new Double(0));LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
										request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
										InventoryConstants.ErrorKeys.PHYS_INV_IMPORT_PROD_INVALID,
										METHOD_NAME + " : " + lines[i] + " - Qty is not specified, default - 0"));
							}
						}
					} 
					productToAddList.add(productToAdd);
				}
			}	 
		}
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);	
		}
		return productToAddList;
	}
	
	// AD Factory Release 15.5 : Import by CSN to Physical Inventory
	/**
	 * This method sets the Products to be Imported based on CSN Import.
	 * @param request
	 * @param validCINMap
	 * @param productToAddList
	 * @param lines
	 */
	private List<InvLocProductRequest> fetchProductsToBeAddedForCSN(final InvImportRequest request,
			HashMap<String, String> validCINMap,
			final List<InvLocProductRequest> productToAddList, String[] lines) {
		
		final String METHOD_NAME = "fetchProductsToBeAddedForCSN";

		 if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			 LOGGER.entering(CLASS_NAME, METHOD_NAME);

		 }
		
		InvLocProductRequest productToAdd = null;
		
		for(int i=0;i<lines.length;i++){
			if(lines[i].length() > 0){
				
				final String productData[] = lines[i].split(InventoryConstants.COMMA);
				boolean isEmpty = InventoryUtility.isEmptyImportLine(productData);
				if(!isEmpty) {
					int numberOfcins =0;
					if(null != request.getCsnCINMap() && request.getCsnCINMap().containsKey(productData[0])){
			
						numberOfcins = request.getCsnCINMap().get(productData[0]).size();
						
						LOGGER.info("CIN COUNT::" + numberOfcins);
						
						for(int numCins = 0; numCins < numberOfcins ; numCins++ ){
							
							productToAdd = new InvLocProductRequest();
							for(int j=0; j<productData.length ; j++){
								
								// set default count type (FULL) and quantity (0) in case
								//   file does not specify
								//Defect #21101 - START -- Commented the default value for Count Type.
								/*productToAdd.setCountType(InventoryConstants.ZERO); */
								//Defect #21101 - END
								productToAdd.setQuantity(new Double(0));
	
								// j==0 is the product num (cin,ndc,etc)
								if(j==0){					
									
									String cin = request.getCsnCINMap().get(productData[j]).get(numCins);
									
									LOGGER.info("CIN FROM MAP::" + cin);
									String corpNumString = cin.replace("-", "");
									corpNumString = corpNumString.replace("\"", "");
									corpNumString = corpNumString.replace("\r", "");
									corpNumString = corpNumString.replace("\n", "");
	
									String cinMapSearchString = corpNumString;
									
									// truncate corpNumString if necessary - DB only supports 25 chars
									if(corpNumString.length() >= 25) {
										corpNumString = corpNumString.substring(0, 25);
									}
									
									if(corpNumString.matches("^[0-9]+([;-][0-9]+)?$")){
										if(corpNumString.length() == 6){
											cinMapSearchString = "10"+corpNumString;
											//corpNumString = "10"+corpNumString;
										}
										
										LOGGER.info("cinMapSearchString:::" + cinMapSearchString);
																			
										// check map for the string
										if(validCINMap.containsKey(cinMapSearchString)){
											
											LOGGER.info("VALID CIN");
											
											if(validCINMap.get(cinMapSearchString).equalsIgnoreCase("MULTIPLE")){
												productToAdd.setProduct(corpNumString);
												productToAdd.setProductType("2");
											}else if(validCINMap.get(cinMapSearchString).equalsIgnoreCase("INVALID")){
												productToAdd.setProduct(corpNumString);
												productToAdd.setProductType("1");
											}else{
												
												LOGGER.info("VALID CIN::" + validCINMap.get(cinMapSearchString));
												
												productToAdd.setProduct(validCINMap.get(cinMapSearchString));												
												productToAdd.setProductType("0");
											}
	
										} else {
											LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
													request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
													InventoryConstants.ErrorKeys.PHYS_INV_IMPORT_PROD_INVALID,
													METHOD_NAME + " : " + lines[i] + " - Prod number not found in valid CIN map : " +
													corpNumString + ".  Adding as Invalid product type"));
											productToAdd.setProduct(corpNumString);
											productToAdd.setProductType("1");
										}
									} else if (validCINMap.get(cinMapSearchString).equalsIgnoreCase("INVALID")){
										LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
												request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
												InventoryConstants.ErrorKeys.PHYS_INV_IMPORT_PROD_INVALID,
												METHOD_NAME + " : " + lines[i] + " - Prod number does not match regular " +
												"expression ^[0-9]+([;-][0-9]+)?$ : " + corpNumString + 
										".  Adding as Invalid product type"));
										
										productToAdd.setProduct(corpNumString);
										productToAdd.setProductType("1");
									}
								}
								// j==1 is the count type, PARTIAL or FULL
								if(j==1){
									
									if(productData[j] != null) {
										String countTypeString = productData[j].trim();
										if("PARTIAL".equalsIgnoreCase(countTypeString)){
											productToAdd.setCountType("1");
										} else { // any other string will result in a Full type
											productToAdd.setCountType("0");
										}
									} else {
										// set 0 as count type if none specified
										productToAdd.setCountType("0");
										LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
												request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
												InventoryConstants.ErrorKeys.PHYS_INV_IMPORT_PROD_INVALID,
												METHOD_NAME + " : " + lines[i] + " - Count Type not specified - default is FULL"));
									}
								}
								// j==2 is the Quantity
								if(j==2){
									if(productData[j] != null) {
										String quantity = productData[j].trim();
										if (quantity.isEmpty()) {
											// set qty as 0 if not specified
											productToAdd.setQuantity(new Double(0));
										} else if(!quantity.matches("^(\\d{0,4}+\\.?\\d{0,2})$")){
											// failed check for format xxxx.xx, but check if numeric
											boolean isNumeric = quantity.matches("[-+]?\\d*\\.?\\d+");
											if(isNumeric) {
												if(productToAdd.getCountType().equals("1")) {
													double qty = InventoryUtility.formatImportQty(quantity);
													int intQuantity = (int)qty;
													productToAdd.setQuantity((double)intQuantity);
												} else {
													double qty = InventoryUtility.formatImportQty(quantity);
													productToAdd.setQuantity(qty);
												}
											} else {
												productToAdd.setQuantity(new Double(0));
												LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
														request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
														InventoryConstants.ErrorKeys.PHYS_INV_IMPORT_PROD_INVALID,
														METHOD_NAME + " : " + lines[i] + " - Qty is not numeric, default - 0"));
											}
										} else{
											// if Count Type is PARTIAL, return only whole numbers
											if(productToAdd.getCountType().equals("1")) {
												double qty = InventoryUtility.formatImportQty(quantity);
												int intQuantity = (int)qty;
												productToAdd.setQuantity((double)intQuantity);
											}else {
												double qty = InventoryUtility.formatImportQty(quantity);
												productToAdd.setQuantity(qty);
											}
										}
									} else {
										// send 0 as qty if not specified
										productToAdd.setQuantity(new Double(0));LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
												request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
												InventoryConstants.ErrorKeys.PHYS_INV_IMPORT_PROD_INVALID,
												METHOD_NAME + " : " + lines[i] + " - Qty is not specified, default - 0"));
									}
								}
							} 
							productToAddList.add(productToAdd);
						}
					}
				}
			}	 
		}
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);	
		}
		return productToAddList;
	}
	
	 /**
	 * This method invokes the JPA calls to database and search the input numeric value in the scan code 
	 * and products table.
	 * 
	 * @param request
	 *
	 * @return the RcvProductResponse 
	 *
	 */
	public HashMap<String,String> getProductCINSearch(final List<String> corpItemNumberList,final String shipToLoc,final String shipToCustomer) {
		
		final String methodName = "getProductCINSearch";

    	if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
    		LOGGER.entering(CLASS_NAME, methodName);    		
    	}
    	final HashMap<Long,String> requestProdMap = new HashMap<Long,String>();
    	
    	
		final HashMap<String, String> finalSortedMap = new HashMap<String, String>();
		try{
		final StringBuffer findProductsQuery = new StringBuffer(2000);
		StringBuffer corpItemStr = new StringBuffer();
		for(String corpItem : corpItemNumberList ){
			Long product=Long.parseLong(corpItem);
			
			if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
				LOGGER.finer("corpItem="+corpItem); 
				LOGGER.finer("product="+product);
			}
			if(!requestProdMap.containsKey(product)){
			corpItemStr.append(InventoryConstants.APOSTROPHE);
			corpItemStr.append(corpItem);
			corpItemStr.append(InventoryConstants.APOSTROPHE);
			corpItemStr.append(InventoryConstants.COMMA);
			}
		
			requestProdMap.put(product,corpItem);
		}
		if(corpItemStr.lastIndexOf(",") ==  corpItemStr.length()-1){
			corpItemStr.replace(corpItemStr.length()-1, corpItemStr.length(), "");
		}
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("requestProdMap="+requestProdMap);
		}
		findProductsQuery.append("SELECT DISTINCT ");
		findProductsQuery
				.append("pr.CORP_ITEM_NUM,pr.NDC_CDE,pr.UPC_NUM FROM HSCSP_DDS.PRODUCT pr  ");
		findProductsQuery.append(" INNER JOIN HSCSP_ODS.ACCT_PROD_ELIGIBILITY AEP ");
		findProductsQuery.append(" ON AEP.CORP_ITEM_NUM=pr.CORP_ITEM_NUM ");
		findProductsQuery.append(" AND AEP.SHIP_TO_CUSTOMER_NUM=");		
		findProductsQuery.append(shipToCustomer);
		findProductsQuery.append(" AND AEP.SHIP_TO_LOCATION_NUM= ");
		findProductsQuery.append(shipToLoc);
		findProductsQuery.append(" WHERE ");
		findProductsQuery.append(" pr.UPC_NUM IN ");
		findProductsQuery.append("(" + corpItemStr.toString().trim() + ")");
		findProductsQuery.append(" OR ");
		findProductsQuery.append("pr.NDC_CDE IN ");
		findProductsQuery.append("(" + corpItemStr.toString().trim() + ")");		
		findProductsQuery.append(" FOR FETCH ONLY WITH UR ");
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
    		LOGGER.finer("findProductsQuery="+findProductsQuery.toString()); 
		}
		// Execute the query
		final Query query = entityMgr.createNativeQuery(findProductsQuery.toString());

		final List<Object[]> resultList = query.getResultList();
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
    		LOGGER.finer("Executing updateProductsMap for eligible products for NDC/UPC "); 
		}
		updateProductsMap(resultList,requestProdMap,finalSortedMap);
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
    		LOGGER.finer("END OF updateProductsMap for eligible products for NDC/UPC "); 
    		LOGGER.finer("Eligible Products Map:"+finalSortedMap); 
		}
			corpItemStr=new StringBuffer();
			int notFound=0;
			for(String product:corpItemNumberList){				
				if (!finalSortedMap.containsKey(product)) {
					notFound++;
					corpItemStr.append(InventoryConstants.APOSTROPHE);
					corpItemStr.append(product);
					corpItemStr.append(InventoryConstants.APOSTROPHE);
					corpItemStr.append(InventoryConstants.COMMA);
				}
					
				
			}
			
			if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
	    		LOGGER.finer("Number of non eligible products for NDC/UPC="+notFound); 
	    		
			}
			
			if(notFound>0){
			if(corpItemStr.lastIndexOf(",") ==  corpItemStr.length()-1){
				corpItemStr.replace(corpItemStr.length()-1, corpItemStr.length(), "");
			}
			final StringBuffer findNonEligibleProdQry=new StringBuffer(300);
			findNonEligibleProdQry.append("SELECT DISTINCT ");
			findNonEligibleProdQry
					.append(" pr.CORP_ITEM_NUM,pr.NDC_CDE,pr.UPC_NUM FROM HSCSP_DDS.PRODUCT pr  ");
			findNonEligibleProdQry.append(" WHERE ");
			findNonEligibleProdQry.append(" pr.UPC_NUM IN ");
			findNonEligibleProdQry.append("(" + corpItemStr.toString().trim() + ")");
			findNonEligibleProdQry.append(" OR ");
			findNonEligibleProdQry.append("pr.NDC_CDE IN ");
			findNonEligibleProdQry.append("(" + corpItemStr.toString().trim() + ")");		
			findNonEligibleProdQry.append(" FOR FETCH ONLY WITH UR ");
			if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
	    		LOGGER.finer("findNonEligibleProdQry="+findNonEligibleProdQry.toString()); 
			}
			// Execute the query
			final Query nonEliProduery = entityMgr.createNativeQuery(findNonEligibleProdQry.toString());

			final List<Object[]> nonEligibleItems = nonEliProduery.getResultList();
			
			if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
	    		LOGGER.finer("Executing updateProductsMap for  non eligible products for NDC/UPC "); 
			}
			updateProductsMap(nonEligibleItems,requestProdMap,finalSortedMap);
			if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
	    		LOGGER.finer("END OF updateProductsMap for non eligible products for NDC/UPC "); 
	    		LOGGER.finer("Non Eligible Products Map:"+finalSortedMap); 
			}
			
		}		
			
		entityMgr.flush();
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
    		LOGGER.finer("Final Results Map:"+finalSortedMap); 
		}
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {		
			LOGGER.exiting(CLASS_NAME, methodName);
		}
		}catch(Exception ex){
			LOGGER.severe("Error occurered::"+ex.getMessage() + "with Cause::"+ex.getCause());
		}
		return finalSortedMap;
	}
/**
 * This method takes results map, requested products and Results map where it check reults vers requested products list then generates results map
 * @param resultList
 * @param corpItemNumberList
 * @param resultsMap 
 */
	private  void updateProductsMap(
			final List<Object[]> resultList,final HashMap<Long, String> requestProdMap,final HashMap<String, String> resultsMap){
		final String methodName = "updateProductsMap";

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		} 
		if (resultList!=null && !resultList.isEmpty()) {	
			if(MessageLoggerHelper.isTraceEnabled(LOGGER)) {
	    		LOGGER.fine("resultList="+resultList.size()); 
			}
			 Long ndcNum =new Long(0);
			 Long upcNum=new Long(0);
			for (Iterator<Object[]> i = resultList.iterator(); i.hasNext();) {
				Object[] product = (Object[]) i.next();
				 if(MessageLoggerHelper.isTraceEnabled(LOGGER)) {
					 LOGGER.fine("product[1].toString().trim()="+product[1].toString().trim());  
					 LOGGER.fine("product[2].toString().trim()="+product[2].toString().trim());  
				 }
				if(InventoryUtility.isNumericAndNotNull(product[1].toString())){
					ndcNum =Long.parseLong(product[1].toString().trim());
				}
				
				if(InventoryUtility.isNumericAndNotNull(product[2].toString().trim())){
					 LOGGER.fine("inside UPC="+product[2].toString().trim());  
				  upcNum=Long.parseLong(product[2].toString().trim());
				}
				String ndcUpc=null;
				
			 if(requestProdMap.containsKey(ndcNum)){
				 ndcUpc= requestProdMap.get(ndcNum);
			 }else if(requestProdMap.containsKey(upcNum)){
				 ndcUpc= requestProdMap.get(upcNum);
			 }
	 
	 if(MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.fine("upcNum="+upcNum+",ndcNum="+ndcNum); 
			LOGGER.fine("ndcUpc="+ndcUpc); 
		}	

			if (ndcUpc!=null) {
				if (resultsMap.containsKey(ndcUpc)) {
					resultsMap.put(ndcUpc, InventoryConstants.PRODUCT_TYPE_MULTIPLE_TEXT);
				} else {
					resultsMap.put(ndcUpc, product[0].toString());
				}
			}
	 }
	}
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {		
			LOGGER.exiting(CLASS_NAME, methodName);
		}	
 }

			/**
	 * This method calculates the total line value of a location detail given the count type, 
	 * override cost, cost, quantity, and UOIF num
	 * 
	 * @param countTypeCde the count type of the location detail
	 * @param overideCost the override cost of the location detail
	 * @param quantity the quantity of the location detail
	 * @param cost the cost of the location detail
	 * @param uoifNum the UOIF num of the location detail
	 * @return returns the total line value
	 */
	private BigDecimal calculateLineTotalValue(final int countTypeCde,final BigDecimal overideCost,final BigDecimal quantity,final BigDecimal cost,final BigDecimal uoiCost){
		final String methodName = "calculateLineTotalValue";

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, methodName);
		} 

		BigDecimal totalVal = BigDecimal.ZERO;
		BigDecimal decScaled = BigDecimal.ZERO;
		
		if(!InventoryUtility.isCostZero(overideCost)){
			totalVal=quantity.multiply(overideCost); 
		}else if(cost!=null){
			if(countTypeCde==0){			  
				totalVal=quantity.multiply(cost); 
			}else if(uoiCost!=null){				
				totalVal=quantity.multiply(uoiCost);
			}
		}
		totalVal=new BigDecimal(InventoryUtility.formatCost(totalVal.doubleValue()));
		// scale and round
		decScaled = totalVal.setScale(2, BigDecimal.ROUND_HALF_UP);

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.fine("COST :>>>>"+cost);
			LOGGER.fine("Count Type Code :>>>>"+countTypeCde);
			LOGGER.fine("Manual Overide Cose :>>>>"+overideCost);
			LOGGER.fine("uoiCost :>>>>"+uoiCost);
			LOGGER.fine("totalVal :>>>>"+totalVal);
			LOGGER.fine("decScaled :>>>>"+decScaled);
		}

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, methodName);
		}



		return decScaled;


	}
	 
	 	
	 	
	 	/**
	 	 * This method  takes InvLocationRequest as request, checks supplies Inventory location name already exits in the data base or not.
	 	 * If Name already exists then return true other wise false 
	 	 * @param request
	 	 * @return
	 	 */
	 	private boolean isLocationNameByInventoryUnique(final InvLocationRequest request){
	 		
			final String METHOD_NAME = "checkUniqueInventoryName";
			
			if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
				LOGGER.entering(CLASS_NAME, METHOD_NAME);	
			}
			boolean rtnFlag=false;
			if(InventoryUtility.isNotNullAndEmpty(request.getLocName())){
			final String locNameCountSql="SELECT count(loc.locationNum) FROM Location loc WHERE lower(loc.locationNam)=:locationNam AND loc.invtryNum=:invtryNum";
	 		//final String countTypeSql = "SELECT count FROM CountType count WHERE count.countTypeCde=0 or count.countTypeCde=1";
	 		
			Query locNamCountQuery = entityMgr.createQuery(locNameCountSql);
			final String locName=InventoryUtility.removeBlankSpaces(request.getLocName());
			locNamCountQuery.setParameter("locationNam", locName.toLowerCase());
			locNamCountQuery.setParameter("invtryNum", request.getInventory().getInventoryId());
						
			final Number countResult=(Number) locNamCountQuery.getSingleResult();
			if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
				LOGGER.fine("Location Unique Name Counter >>>"+countResult);
			}
			if(countResult!=null && countResult.intValue()>0){
				if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
					LOGGER.fine("Location Name already exits for this Inventory: "+ request.getInventory().getInventoryId());
				}
				rtnFlag=true;
			}
			
			
	 		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
				LOGGER.exiting(CLASS_NAME, METHOD_NAME);
			}
		}
	return rtnFlag;
	 				
			
	 	}
	 	/**
	 	 * This is method gives request as InvInventoryRequest, checks give Inventory name is is unique for the Account and DC. 
	 	 * If Inventory name already exits for the given account then it returns returns false otherwise true.
	 	 * @param request : Takes InvInventoryRequest reques object
	 	 * @return boolean : returns true or false
	 	 */
 	private boolean isInventoryNameUnique(InvInventoryRequest request){
	 		
			final String METHOD_NAME = "checkUniqueInventoryName";
			
			if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
				LOGGER.entering(CLASS_NAME, METHOD_NAME);	
			}
			final String inventoryNameCountSql="SELECT count(inv.invtryNum) FROM Invtry inv WHERE lower(inv.invtryNam)=:invtryNam AND inv.shipToLocationNum=:shipToLocationNum AND inv.shipToCustomerNum=:shipToCustomerNum";
	 		//final String countTypeSql = "SELECT count FROM CountType count WHERE count.countTypeCde=0 or count.countTypeCde=1";
	 		
			Query invNamCountQuery = entityMgr.createQuery(inventoryNameCountSql);
			final String invName=InventoryUtility.removeBlankSpaces(request.getInvName());
						
			invNamCountQuery.setParameter("invtryNam", invName.toLowerCase());
			invNamCountQuery.setParameter("shipToLocationNum", new BigDecimal(request.getDistributionCenter()));
			invNamCountQuery.setParameter("shipToCustomerNum", new BigDecimal(request.getShipToCustomer()));
			
			Number countResult=(Number) invNamCountQuery.getSingleResult();
			if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
				LOGGER.fine("Invenotry Unique Name Counter >>>"+countResult);
			}
			if(countResult==null || countResult.intValue()==0){
				if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
					LOGGER.fine("Invenotry Name not present for this Ship To Account: "+ request.getDistributionCenter()+" - " +request.getShipToCustomer());
				}
				return true;
			}
			if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
				LOGGER.fine("Invenotry Name already present for this Ship To Account: "+ request.getDistributionCenter()+" - " +request.getShipToCustomer());
			}
			
	 		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
				LOGGER.exiting(CLASS_NAME, METHOD_NAME);
			}
			return false;
	 		
	 		
	 	}
 	/**
 	 * this method takes request of InvLocationRequest and location and check checks given location name unique for that inventory by invoking isLocationNameByInventoryUnique method 
	 * If location name not unique  then returns location name as it is. 
 	 * If  location name is unique the call same method passing request of InvLocationRequest and prefix location name with Copy of.
 	 *  This will keep executing until find unique name in given inventory.
 	 * @param request
 	 * @param locName
 	 * @return
 	 */
 	
 private String getCopyLocationUniqueName(InvLocationRequest request, String locName){
	 final String METHOD_NAME = "getCopyLocationsUniqueName";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);	
		}
	
	 int count=1;
	 boolean flag=true;
	 String suffixName="";
	 String actaulLocName=locName;
	 if(locName.indexOf('(')!=-1){
		 int index=locName.indexOf('(');
		 int lastIndex=locName.indexOf(')');
		 actaulLocName=locName.substring(0,index-1);
		 String subString=locName.substring(index+6,lastIndex);
		 count=Integer.parseInt(subString.trim());
	 }
	 for(boolean i=false;i!=flag;){		
		 request.setLocName(actaulLocName+suffixName);
		 if(isLocationNameByInventoryUnique(request)){
			  suffixName=" (Copy "+count+")";
			 count++;
		 }else{
			 
			 return request.getLocName();
		 }
		 
		 
	 }
	 
	
		
		 

	 
	 if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}
	return request.getLocName();
	 
	 
 }
 
 /**
  * This method takes Inventory object and fetch all total cost, total lines and status calculation for give inventory
  * @param inv
  */
 private void updateInventoryLocationInfo(InvInventory inv){
	 final String METHOD_NAME = "updateInventoryLocationInfo";

	 if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
		 LOGGER.entering(CLASS_NAME, METHOD_NAME);

	 }
	 final Query query=entityMgr
	 .createQuery("SELECT loc.locationStat.locationStatDesc as status , sum(loc.totalLocationValueAmt) AS totalAmount, COUNT(loc.locationNum) AS total FROM Location AS loc  WHERE loc.invtryNum=:invtryNum GROUP BY loc.locationStat.locationStatDesc");
	 query.setParameter("invtryNum",inv.getInventoryId());	
	 final List<Object[]> results =query.getResultList(); 
	 int locCount=0;

	 String inventoryStatus = null;
	 
	 BigDecimal locTotalAmt=BigDecimal.ZERO;
	
	 if(results!=null && !results.isEmpty()){
		 

		 for (Object[] result : results) {
			 
			 
			 locTotalAmt=locTotalAmt.add((BigDecimal)(result[1]));		   
			 locCount = locCount+((Number) result[2]).intValue();
		 }
		 // Get inventory status
		 if(results.size() > 1) { // if there are locations with more than one type of status desc
			 inventoryStatus = "";
			 int tempStatus = 4; // initial status is higher than all possible 
			 for(int i=0; i < results.size(); i++) {
				 Object[] result = results.get(i);
				 // invStatus is first column returned
				 String status = (String) result[0];
				 if(status != null && status.equalsIgnoreCase("Open")) {
					 if(tempStatus > InventoryConstants.OPEN_STATUS_CODE) {
						 tempStatus = InventoryConstants.OPEN_STATUS_CODE;
						 inventoryStatus = "Open";
					 }
				 } else if(status != null && status.equalsIgnoreCase("Counted")) {
					 if(tempStatus > InventoryConstants.COUNTED_STATUS_CODE) {
						 tempStatus = InventoryConstants.COUNTED_STATUS_CODE;
						 inventoryStatus = "Counted";
					 }
				 } else if(status != null && status.equalsIgnoreCase("Cost Set")) {
					 if(tempStatus > InventoryConstants.COST_SET_STATUS_CODE) {
						 tempStatus = InventoryConstants.COST_SET_STATUS_CODE;
						 inventoryStatus = "Cost Set";
					 }
				 } else if(status != null && status.equalsIgnoreCase("Closed") &&
						 (tempStatus > InventoryConstants.CLOSED_STATUS_CODE)) {

					 tempStatus = InventoryConstants.CLOSED_STATUS_CODE;
					 inventoryStatus = "Closed";
				 }
			 }
			 
		 } else {  // all locations have the same status desc
			 Object[] result = results.get(0);
			 inventoryStatus = (String) result[0];
		 }
	 } else if(results.isEmpty()) {
		 inventoryStatus = "Closed";
	 }
	 
	 if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
		 LOGGER.fine("locInventoryStatus::"+ inventoryStatus);
		 LOGGER.fine("locTotalAmt::"+ locTotalAmt);
		 LOGGER.fine("locCount::"+ locCount);
	 }

	 inv.setInventoryStatus(inventoryStatus);

	 inv.setTotalValue(locTotalAmt.doubleValue());
	 inv.setNumberOfLocations(locCount);
	 if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
		 LOGGER.exiting(CLASS_NAME, METHOD_NAME);
	 }
 } 

 	/**
 	 * This method determines if the location id is valid for the shipToCust and distCenter 
 	 * values passed with it in the request 
 	 * @param location the location number
 	 * @param shipToCustomer the ship to location number
 	 * @param distCenter the dc number
  	* @return returns success or failure code 
 	*/
 	public InvLocationResponse getValidLocation(InvLocationRequest request, int location, 
 				String shipToCustomer, String distCenter) {
 		final String METHOD_NAME = "getValidLocation";
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);
		}

		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer(METHOD_NAME + " Request -------> Location=" + location 
					+ ", ShipToCustomer=" + shipToCustomer + ", DistributionCenter=" 
					+ distCenter);
		}

		InvLocationResponse locResp = new InvLocationResponse();
		
		try {
			// initially set this code as failure
			locResp.setStatusCode("FAILURE");

			Location loc = entityMgr.find(Location.class, location);

			if(loc != null) {
				long invNum = loc.getInvtryNum();
				LOGGER.finest(METHOD_NAME + " Location found: " + invNum);

				Invtry inv = entityMgr.find(Invtry.class, invNum);

				// if there's a result, instantiate InvInventory obj 
				if(inv != null) {
					LOGGER.finest(METHOD_NAME + " Inventory found");
					String invShipToCust = inv.getShipToCustomerNum().toString();
					String invShipToLoc = inv.getShipToLocationNum().toString();

					LOGGER.finest(METHOD_NAME + " Inv returned shipToCust:" + invShipToCust);
					LOGGER.finest(METHOD_NAME + " Inv returned shipToLoc:" + invShipToLoc);

					// check if the returned inventory matches request ship to cust and loc
					if(shipToCustomer.equalsIgnoreCase(invShipToCust.trim()) && 
							distCenter.equalsIgnoreCase(invShipToLoc.trim())) {
						LOGGER.finest(METHOD_NAME + " Inv info and request info match");

						List<InvLocation> locations = new ArrayList<InvLocation>();
						InvLocation invLoc = new InvLocation();
						invLoc.setInventoryID(inv.getInvtryNum());
						invLoc.setLocationId(loc.getLocationNum());
						invLoc.setLocationName(loc.getLocationNam());
						invLoc.setLocStatus(loc.getLocationStat().getLocationStatDesc());
						invLoc.setCountType(loc.getCountType().getCountTypeDesc());

						locations.add(invLoc);

						InvInventory inventory = new InvInventory();
						inventory.setInventoryId(inv.getInvtryNum());
						inventory.setInventoryName(inv.getInvtryNam());

						locResp.setInventory(inventory);
						locResp.setLocations(locations);
						// this location has been validated and status code is success
						locResp.setStatusCode("SUCCESS");
					} 

				} 
			}
		} catch (Throwable t) {

			LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
					request.getShipToCustomer(), request.getUserId(), request.getTransactionId(),
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY),
							t.getCause()+": "+t.getMessage()));

			throw new SystemException(ErrorHandlingHelper.THROWABLE_ERROR_KEY,
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							ErrorHandlingHelper.THROWABLE_ERROR_KEY), t.getCause()+": "+t.getMessage(), t);

		}
		
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);
		}
		
		return locResp;
 	}
 	/**
 	 * This method will take list of location id as comma separated String and query the data base  for each location count 
 	 * by group then return in location and count in map
 	 * @param locIds
 	 * @return
 	 */
 		private Map<Integer,Integer> getLocLinesCountByLocation(String locIds){
 			final String METHOD_NAME = "getLocLinesCountByLocation";

 			if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
 				LOGGER.entering(CLASS_NAME, METHOD_NAME);

 			}
 			Map<Integer,Integer> locCntMap=null;
 			final StringBuffer locsCountSql=new StringBuffer(300);
 			locsCountSql.append("SELECT  loc.LOCATION_NUM,COUNT(loc.LOCATION_DETAIL_NUM)");
 			locsCountSql.append(" FROM OE_PI.LOCATION_DETAIL loc WHERE loc.LOCATION_NUM IN(");
 			locsCountSql.append(locIds);
 			locsCountSql.append(InventoryConstants.CLOSE_PAREN);
 			locsCountSql.append(" GROUP BY loc.LOCATION_NUM WITH UR");
 			
 			final Query locLinesCntQuery = entityMgr.createNativeQuery(locsCountSql.toString());	
 			if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
 				LOGGER.finer("Location Lines Count by Location Query=" +  locLinesCntQuery);
 			}
 			final List<Object> locCntResultsSet=locLinesCntQuery.getResultList();
 			 if(locCntResultsSet!=null && !locCntResultsSet.isEmpty()){
 				 locCntMap=new HashMap<Integer,Integer>();
 				Object[] resultObj=null;
 				 for(int i=0;i<locCntResultsSet.size();i++){
 					  resultObj=(Object[])locCntResultsSet.get(i);
 					 if(resultObj!=null && resultObj[0]!=null && resultObj[1]!=null){
 						 if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
 							 LOGGER.finer("Location Id="+(Integer)resultObj[0] +"- Loc Line Count="+(Integer)resultObj[1]);						 
 						 }
 						 locCntMap.put((Integer)resultObj[0], (Integer)resultObj[1]);
 					 }
 				 }
 			 }
 			 if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
 					LOGGER.exiting(CLASS_NAME, METHOD_NAME);
 				}
 			return locCntMap;
 			
 		}
 		/**
 		 * This method will query Location Status  table and retrieve all the available Location Status Type code and Descriptions
 		 * Then  generate map and return 
 		 * @return The statusMap
 		 */	
 	private Map<Integer,String> getStatusList(){

			final String METHOD_NAME = "getStatusList";

			if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
				LOGGER.entering(CLASS_NAME, METHOD_NAME);

			}
			Map<Integer,String> statusMap=null;
			final String locStatusSql="SELECT locStat.LOCATION_STAT_CDE,locStat.LOCATION_STAT_DESC FROM OE_PI.LOCATION_STAT locStat WITH UR";
			
			
			final Query locStatusQuery = entityMgr.createNativeQuery(locStatusSql);	
			if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
				LOGGER.finer("Location STATUS Query=" +  locStatusQuery);
			}
			final List<Object> locStatResultsSet=locStatusQuery.getResultList();
			 if(locStatResultsSet!=null && !locStatResultsSet.isEmpty()){
				 statusMap=new HashMap<Integer,String>();
				 for(int i=0;i<locStatResultsSet.size();i++){
					 Object[] resultObj=(Object[])locStatResultsSet.get(i);
					 if(resultObj!=null && resultObj[0]!=null && resultObj[1]!=null){
						 if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
							 LOGGER.finer("Status Id="+(Integer)resultObj[0] +"- Status Description="+(String)resultObj[1]);						 
						 }
						 statusMap.put((Integer)resultObj[0], (String)resultObj[1]);
					 }
				 }
			 }
			 if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
					LOGGER.exiting(CLASS_NAME, METHOD_NAME);
				}
			return statusMap;	
		
 		
 	}
 	
 	/**
	 * This method will query Count Type table and retrieve all the available Count Type code and Descriptions
	 * Then  generate map and return 
	 * @return The cntTypMap
	 */
  private Map<Integer,String> getCountTypes(){

		final String METHOD_NAME = "getCountTypes";

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);

		}
		Map<Integer,String> cntTypMap=null;
		final String cntTypSql="SELECT cntTyp.COUNT_TYPE_CDE,cntTyp.COUNT_TYPE_DESC FROM OE_PI.COUNT_TYPE cntTyp WITH UR";
		
		
		final Query cntTypQuery = entityMgr.createNativeQuery(cntTypSql);	
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("Location Count Type Query=" +  cntTypQuery);
		}
		final List<Object> cntTypResultsSet=cntTypQuery.getResultList();
		 if(cntTypResultsSet!=null && !cntTypResultsSet.isEmpty()){
			 cntTypMap=new HashMap<Integer,String>();
			 for(int i=0;i<cntTypResultsSet.size();i++){
				 Object[] resultObj=(Object[])cntTypResultsSet.get(i);
				 if(resultObj!=null && resultObj[0]!=null && resultObj[1]!=null){
					 if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
						 LOGGER.finer("Count Type Id="+(Integer)resultObj[0] +"- Count Type Description="+(String)resultObj[1]);						 
					 }
					 cntTypMap.put((Integer)resultObj[0], (String)resultObj[1]);
				 }
			 }
		 }
		 if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
				LOGGER.exiting(CLASS_NAME, METHOD_NAME);
			}
		return cntTypMap;	
	
		
	}
/**
	 * This method will query COST Type table and retrieve all the available Cost Types code and Descriptions
	 * Then  generate map and return 
	 * @return The costTypMap
	 */
 	private Map<Integer,String> getCostTypes(){

		final String METHOD_NAME = "getCostTypes";

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);

		}
		Map<Integer,String> costTypMap=null;
		final String costTypSql="SELECT costTyp.COST_TYPE_CDE,costTyp.COST_TYPE_DESC FROM OE_PI.COST_TYPE costTyp WITH UR";		
		
		final Query costTypSqlQuery = entityMgr.createNativeQuery(costTypSql);	
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("Location costTypSqlQuery=" +  costTypSqlQuery);
		}
		final List<Object> costTypResultsSet=costTypSqlQuery.getResultList();
		 if(costTypResultsSet!=null && !costTypResultsSet.isEmpty()){
			 costTypMap=new HashMap<Integer,String>();
			 for(int i=0;i<costTypResultsSet.size();i++){
				 Object[] resultObj=(Object[])costTypResultsSet.get(i);
				 if(resultObj!=null && resultObj[0]!=null && resultObj[1]!=null){
					 if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
						 LOGGER.finer("Cost Type Id="+(Integer)resultObj[0] +"- Cost Type Description="+(String)resultObj[1]);						 
					 }
					 costTypMap.put((Integer)resultObj[0], (String)resultObj[1]);
				 }
			 }
		 }
		 if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
				LOGGER.exiting(CLASS_NAME, METHOD_NAME);
			}
		return costTypMap;	
	
		
	}
 	/**
 	 * This method will query Product ID Type table and retrieve all the available product Types codes and Descriptions
 	 * Then  generate map and return 
 	 * @return The prodIdMap
 	 */
 	private Map<Integer,String> getProductIdTypes(){

		final String METHOD_NAME = "getProductTypeList";

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);

		}
		Map<Integer,String> prodIdMap=null;
		final String locProdSql="SELECT prdTyp.PROD_ID_TYPE_CDE,prdTyp.PROD_ID_TYPE_DESC FROM OE_PI.PROD_ID_TYPE prdTyp WITH UR";
		
		
		final Query locProdIdQuery = entityMgr.createNativeQuery(locProdSql);	
		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			LOGGER.finer("Location Product ID type Query=" +  locProdIdQuery);
		}
		final List<Object> locProdIdResultsSet=locProdIdQuery.getResultList();
		 if(locProdIdResultsSet!=null && !locProdIdResultsSet.isEmpty()){
			 prodIdMap=new HashMap<Integer,String>();
			 for(int i=0;i<locProdIdResultsSet.size();i++){
				 Object[] resultObj=(Object[])locProdIdResultsSet.get(i);
				 if(resultObj!=null && resultObj[0]!=null && resultObj[1]!=null){
					 if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
						 LOGGER.finer("Product ID Type Code="+(Integer)resultObj[0] +"- Product ID Type  Description="+(String)resultObj[1]);						 
					 }
					 prodIdMap.put((Integer)resultObj[0], (String)resultObj[1]);
				 }
			 }
		 }
		 if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
				LOGGER.exiting(CLASS_NAME, METHOD_NAME);
			}
		return prodIdMap;
	
		
	}
 	/**
 	 * This method will take Integer and HashMap as input check if requesed key prsent in map or not. 
 	 * if key present in map then return the value otherwise blank 
 	 * @param key
 	 * @param dataMap
 	 * @return
 	 */
	private String readValueFromMap(final Integer key,final Map<Integer,String> dataMap){
		String rtnVal="";
		if(dataMap!=null && key!=null && dataMap.containsKey(key) ){
			rtnVal=dataMap.get(key);
		}
		return rtnVal;
	}
	
	/**
	 * The below method updates the sequence number of each LocationDetail for a particular Location 
	 * when the user deletes one or more LocationDetail from a Location. The method retrieves the LocationDetails from the database in ascending order
	 * and then assigns new sequence number starting from '1' to each LocationDetail in increments of '1'.
	 *  
	 * @param request - Request object containing the Location details which needs to be updated.
	 */
	private void updateSequenceNumber(InvLocationDetailRequest request) {

		final String METHOD_NAME = "updateSequenceNumber";
		int updatedSeqNum = 0;
		final Timestamp now;
		long methodStartTime = System.currentTimeMillis();
		List<LocationDetail> updatedLocationDetails = null;
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);

		}
		LocationDetail detailLine = null;
		if (null != request) {

			final String invLocationDetailSQL = "SELECT dtl FROM LocationDetail dtl WHERE dtl.location.locationNum = :locNum ORDER BY dtl.seqNum ASC";
			final Query invLocationDetailQuery = entityMgr.createQuery(invLocationDetailSQL);
			invLocationDetailQuery.setParameter("locNum", request.getLocation().getLocationId());
			final List<LocationDetail> resultList = invLocationDetailQuery.getResultList();
			if (null != resultList 
					&& !resultList.isEmpty()) {
				now = new Timestamp(System.currentTimeMillis());
				updatedLocationDetails = new ArrayList<LocationDetail>();
				for (int i = 0; i < resultList.size(); i++) {
					
					updatedSeqNum++;
					detailLine = resultList.get(i);
					detailLine.setSeqNum(updatedSeqNum);
					detailLine.setRowUpdateStp(now);
					detailLine.setRowAddUserId(request.getUserId());
					
					updatedLocationDetails.add(detailLine);
				}
				if(null != updatedLocationDetails 
						&& !updatedLocationDetails.isEmpty()) {
					
					if(MessageLoggerHelper.isTraceEnabled(LOGGER)) {						
						LOGGER.fine("Number of location details to be updated: "+updatedLocationDetails.size());
					}
					for(LocationDetail eachLocationDetail : updatedLocationDetails) {
						
						entityMgr.persist(eachLocationDetail);
					}
				}
				entityMgr.flush();
			}
		}
		long methodEndTime = System.currentTimeMillis();
		if(MessageLoggerHelper.isTraceEnabled(LOGGER)) {
			
			long executionTime = methodEndTime - methodStartTime;
			LOGGER.fine("Time taken for updateSequenceNumber method to execute:: "+executionTime+"ms");
		}
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}
	}
	

	/**
	 * Get the product details for cardinal and non cardinal products 
	 * by making direct to PDS
	 */
	public List<ProductDetailsResponse> getProductDetails(final List<String> products,final InvBaseRequest request){
		
		
		/*// final String accountNum =
	            productDetailsRequest.getShipToCustomer() + ProductDetailsConstants.DASH
	            + productDetailsRequest.getShipToLocation();

	        // we will open this validation call after tomorrow QA migration
	        ProductDetailsValidator.validateRequest(productDetailsRequest);
		*/
		
		
		PhysicalInventoryProductDetailsPersister objPhysicalInventoryProductDetails = new PhysicalInventoryProductDetailsPersister();		
		return objPhysicalInventoryProductDetails.getProductDetails(products,request);
		
	}

	
}