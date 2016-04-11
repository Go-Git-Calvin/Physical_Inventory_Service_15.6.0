/*********************************************************************
 *
 * $Workfile: PhysicalInventoryProductDetailsBean.java $
 * Copyright 2011 Cardinal Health
 *
 *********************************************************************
 */
package com.cardinal.ws.physicalinventory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.naming.NamingException;

import com.cardinal.webordering.common.errorhandling.ErrorHandlingHelper;
import com.cardinal.webordering.common.errorhandling.SystemException;
import com.cardinal.webordering.common.logging.MessageLoggerHelper;
import com.cardinal.ws.physicalinventory.common.DataSourceManager;
import com.cardinal.ws.physicalinventory.common.InventoryConstants;
import com.cardinal.ws.physicalinventory.common.InventoryUtility;
import com.cardinal.ws.physicalinventory.common.valueobjects.InvBaseRequest;
import com.cardinal.ws.physicalinventory.common.valueobjects.ProductDetailsResponse;

/**
 * Bean Class made for getting the product details from the PDS when the Set
 * location cost operation is invoked
 * 
 * @author ankit.mahajan
 * 
 */
public class PhysicalInventoryProductDetailsPersister {

	private static final String CLASS_NAME = PhysicalInventoryProductDetailsPersister.class
			.getCanonicalName();
	private static final Logger LOGGER = MessageLoggerHelper
			.getLogger(CLASS_NAME);

	/**
	 * Method to get the Cardinal products account and product attribute from
	 * the PDS.
	 * <P>
	 * Gets the query from the property file and populates the columns needed,
	 * return the update list to be filled in further for non-cardinal products
	 * if an exist in the request
	 */
	private List<ProductDetailsResponse> getProductDetailsCardinalPrds(
			final List<String> products, final InvBaseRequest request) {

		final String METHOD_NAME = "getProductDetailsCardinalPrds";
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		}
		StringBuffer query = new StringBuffer();
		Connection connection = null; // NOPMD by ankit.mahajan on 4/30/15 12:40 PM as connection is closed using the utility method
		ResultSet rs = null;  // NOPMD by ankit.mahajan on 4/30/15 12:40 PM as connection is closed using the utility method
		PreparedStatement ps = null;

		List<ProductDetailsResponse> lstroductDetailsResponse = new ArrayList<ProductDetailsResponse>();

		// Get the PD items query from the property file
		final String queryPDFromPropFile = InventoryUtility.getValue(
				InventoryConstants.PHYSICAL_INVENTORY_SERVICE_NAME,
				InventoryConstants.PHYSICAL_INVENTORY_FILE_NAME,
				InventoryConstants.KEY_PD_QUERY);

		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {

			LOGGER.finest(MessageLoggerHelper.buildMessage(
					request.getShipToCustomer() + "-"
							+ request.getDistributionCenter(),
					request.getUserId(), request.getTransactionId(),
					"Final PD Query Before String Replacement::"
							+ queryPDFromPropFile, "CIN", products.toString(),
					CLASS_NAME, METHOD_NAME));

		}

		// Get the SPD items query from the property file
		final String querySPDFromPropFile = InventoryUtility.getValue(
				InventoryConstants.PHYSICAL_INVENTORY_SERVICE_NAME,
				InventoryConstants.PHYSICAL_INVENTORY_FILE_NAME,
				InventoryConstants.KEY_SPD_QUERY);

		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {

			LOGGER.finest(MessageLoggerHelper.buildMessage(
					request.getShipToCustomer() + "-"
							+ request.getDistributionCenter(),
					request.getUserId(), request.getTransactionId(),
					"Final SPD Query Before String Replacement::"
							+ querySPDFromPropFile, "CIN", products.toString(),
					CLASS_NAME, METHOD_NAME));

		}

		// Do a union of the PD and SPD query
		String unionQueryPdAndSPD = queryPDFromPropFile + " UNION "
				+ querySPDFromPropFile;

		// return from the function is the query is NULL or Blank
		if (unionQueryPdAndSPD == null || "".equals(unionQueryPdAndSPD))
			return null;

		// Replace the <varCIN> attribute from the query retrieved from the
		// property files with ? depending
		// upon the CINs passed in the request
		unionQueryPdAndSPD = unionQueryPdAndSPD.replaceAll(
				InventoryConstants.REGEX_VAR_CIN,
				prepareCINParamList(products.size()));

		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {

			LOGGER.finest(MessageLoggerHelper.buildMessage(
					request.getShipToCustomer() + "-"
							+ request.getDistributionCenter(),
					request.getUserId(), request.getTransactionId(),
					"Final Combined Query PD and SPD After String Replacement::"
							+ unionQueryPdAndSPD, "CIN", products.toString(),
					CLASS_NAME, METHOD_NAME));

		}

		query.append(unionQueryPdAndSPD);

		// Code to make the DB connection, set the query with the request
		// parameters and set the response in the list
		try {

			connection = getConnection(); 

			ps = connection.prepareStatement(query.toString());

			// Set the parameters to the query
			ps.setString(1, request.getShipToCustomer());
			ps.setString(2, request.getDistributionCenter());

			int index = 3;

			for (String cin : products) {
				ps.setString(index++, cin);
			}

			ps.setString(index++, request.getShipToCustomer());
			ps.setString(index++, request.getDistributionCenter());

			for (String cin : products) {
				ps.setString(index++, cin);
			}

			ps.setString(index++, request.getShipToCustomer());
			ps.setString(index++, request.getDistributionCenter());

			ps.setString(index++, request.getShipToCustomer());
			ps.setString(index++, request.getDistributionCenter());
			for (String cin : products) {
				ps.setString(index++, cin);
			}

			ps.setString(index++, request.getShipToCustomer());
			ps.setString(index++, request.getDistributionCenter());

			ps.setString(index++, request.getShipToCustomer());
			ps.setString(index++, request.getDistributionCenter());

			for (String cin : products) {
				ps.setString(index++, cin);
			}

			ps.setString(index++, request.getShipToCustomer());
			ps.setString(index++, request.getDistributionCenter());

			ps.setString(index++, request.getShipToCustomer());
			ps.setString(index++, request.getDistributionCenter());

			for (String cin : products) {
				ps.setString(index++, cin);
			}

			// Parameters for the SPD query starts here
			ps.setString(index++, request.getShipToCustomer());
			ps.setString(index++, request.getDistributionCenter());

			ps.setString(index++, request.getShipToCustomer());
			ps.setString(index++, request.getDistributionCenter());

			for (String cin : products) {
				ps.setString(index++, cin);
			}

			rs = ps.executeQuery();

			ProductDetailsResponse productDetailsResponse = null;

			if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {

				LOGGER.finest(MessageLoggerHelper.buildMessage(
						request.getShipToCustomer() + "-"
								+ request.getDistributionCenter(),
						request.getUserId(), request.getTransactionId(),
						"Selected Column names in Query ::"
								+ InventoryConstants.INV_COLUMNS_NAME, "CIN",
						products.toString(), CLASS_NAME, METHOD_NAME));
			}

			Object accountUOIFFrmDB = null;
			Object packQtyFrmDB = null;
			Object packSizeQtyFrmDB = null;
			Object uomABBFrmDB = null;
			Object formUOIFFrmDB = null;
			Object UOIFFrmDB = null;
			Object costLastPurchFrmDB = null;
			Object invoiceCostFrmDB = null;
			Object nifoCostFrmDB = null;

			// Set the values retrieved from the Result set to the response list
			if (null != rs) {
				while (rs.next()) {

					productDetailsResponse = new ProductDetailsResponse();

					productDetailsResponse.setProductNumber(rs
							.getString(InventoryConstants.COLUMNS
									.get(InventoryConstants.CIN)));
							

					// Values of the columns from the DB
					accountUOIFFrmDB = rs.getObject(InventoryConstants.COLUMNS
							.get(InventoryConstants.ACCOUNT_UOIF));
					packQtyFrmDB = rs.getObject(InventoryConstants.COLUMNS
							.get(InventoryConstants.PACK_QTY));
					packSizeQtyFrmDB = rs.getObject(InventoryConstants.COLUMNS
							.get(InventoryConstants.PACK_SIZE_QTY));
					uomABBFrmDB = rs.getObject(InventoryConstants.COLUMNS
							.get(InventoryConstants.UOM_ABB));
					formUOIFFrmDB = rs.getObject(InventoryConstants.COLUMNS
							.get(InventoryConstants.FORM_UOIF));
					UOIFFrmDB = rs.getBigDecimal(InventoryConstants.COLUMNS
							.get(InventoryConstants.UOIF));
					costLastPurchFrmDB = rs
							.getObject(InventoryConstants.COLUMNS
									.get(InventoryConstants.COST_LST_PRCHS));
					invoiceCostFrmDB = rs.getObject(InventoryConstants.COLUMNS
							.get(InventoryConstants.INVOICE_COST));
					nifoCostFrmDB = rs.getObject(InventoryConstants.COLUMNS
							.get(InventoryConstants.CURR_CORP_NIFO_DOLLAR));

					if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {

						LOGGER.finest(MessageLoggerHelper.buildMessage(
								request.getShipToCustomer() + "-"
										+ request.getDistributionCenter(),
								request.getUserId(),
								request.getTransactionId(),
								"Values of the columns from DB ::"
										+ "accountUOIFFrmDB="
										+ accountUOIFFrmDB + ",  packQtyFrmDB="
										+ packQtyFrmDB + ", packSizeQtyFrmDB="
										+ packSizeQtyFrmDB + ", uomABBFrmDB="
										+ uomABBFrmDB + ", formUOIFFrmDB="
										+ formUOIFFrmDB + ", UOIFFrmDB="
										+ UOIFFrmDB + ", costLastPurchFrmDB="
										+ costLastPurchFrmDB
										+ ", invoiceCostFrmDB="
										+ invoiceCostFrmDB + ", nifoCostFrmDB="
										+ nifoCostFrmDB, "CIN",
								productDetailsResponse.getProductNumber(),
								CLASS_NAME, METHOD_NAME));
					}

					double uoif;

					if (null != accountUOIFFrmDB) {
						// Defect 14016 fix - removed the intvalue part
						String accountUOIFVal = String
								.valueOf(deriveAccountUOIFDouble(
										(BigDecimal) accountUOIFFrmDB,
										(BigDecimal) packQtyFrmDB,
										(BigDecimal) packSizeQtyFrmDB,
										(String) uomABBFrmDB));

						productDetailsResponse
								.setAccountUOIF(subStringUOIF(accountUOIFVal));

						uoif = deriveAccountUOIFDouble(
								(BigDecimal) accountUOIFFrmDB,
								(BigDecimal) packQtyFrmDB,
								(BigDecimal) packSizeQtyFrmDB,
								(String) uomABBFrmDB);

					} else {// Defect 14016 fix - removed the intvalue part
						String accountUOIFVal1 = String
								.valueOf(deriveAccountUOIFDouble(
										BigDecimal.ONE,
										(BigDecimal) packQtyFrmDB,
										(BigDecimal) packSizeQtyFrmDB,
										(String) uomABBFrmDB));

						productDetailsResponse
								.setAccountUOIF(subStringUOIF(accountUOIFVal1)); // NOPMD

						uoif = deriveAccountUOIFDouble(BigDecimal.ONE,
								(BigDecimal) packQtyFrmDB,
								(BigDecimal) packSizeQtyFrmDB,
								(String) uomABBFrmDB);
					}

					if ((null != formUOIFFrmDB)
							&& (((BigDecimal) formUOIFFrmDB).intValue() != -2)) {
						if (((BigDecimal) formUOIFFrmDB).intValue() == -1) {
							if (productDetailsResponse.getAccountUOIF() != null) {

								productDetailsResponse
										.setFormUOIF(subStringUOIF(String
												.valueOf(productDetailsResponse
														.getAccountUOIF())));

							}
						} else {// Defect 14016 fix - removed the intvalue part
							productDetailsResponse
									.setFormUOIF(subStringUOIF(String
											.valueOf((BigDecimal) (formUOIFFrmDB))));

							uoif = ((BigDecimal) formUOIFFrmDB).doubleValue();
						}
					}

					LOGGER.fine("before uoif is set"
							+ InventoryConstants.UOIF.toString());

					if ((null != UOIFFrmDB)
							&& (((BigDecimal) UOIFFrmDB).doubleValue() > 0)) {
						// Defect 14016 fix - removed the intvalue part
						LOGGER.fine("before put method for uoif is called"
								+ InventoryConstants.UOIF);
						productDetailsResponse.setUOIF(subStringUOIF(String
								.valueOf((BigDecimal) UOIFFrmDB)));
						uoif = ((BigDecimal) UOIFFrmDB).doubleValue();
					} else {
						productDetailsResponse.setUOIF(subStringUOIF(String
								.valueOf(uoif)));
					}
					LOGGER.fine("after uoif is set"
							+ InventoryConstants.UOIF.toString());

					if (isNotNull(costLastPurchFrmDB)) {

						productDetailsResponse.setCostLastPurchased(Double
								.valueOf(String.valueOf(costLastPurchFrmDB)));
					}

					if (isNotEmpty(String.valueOf(invoiceCostFrmDB))) {
						productDetailsResponse.setInvoiceCost(Double
								.valueOf(String.valueOf(invoiceCostFrmDB)));
					}

					if (null != (nifoCostFrmDB)) {
						productDetailsResponse.setNifoCost(String
								.valueOf(nifoCostFrmDB));

					} else {
						productDetailsResponse
								.setNifoCost(InventoryConstants.ZERO);
					}

					if (isNotEmpty((String.valueOf(packSizeQtyFrmDB)))) {
						productDetailsResponse.setPackageSize(new BigDecimal(
								String.valueOf(packSizeQtyFrmDB)).intValue());
					}

					if (isNotEmpty(String.valueOf(packQtyFrmDB))) {
						productDetailsResponse.setPackageQty(Integer
								.valueOf(String.valueOf(packQtyFrmDB)));
					}

					if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {
						LOGGER.finest(MessageLoggerHelper.buildMessage(
								request.getShipToCustomer() + "-"
										+ request.getDistributionCenter(),
								request.getUserId(),
								request.getTransactionId(),
								"productDetailsResponse object before adding to List:: "
										+ productDetailsResponse.toString(),
								"CIN",
								productDetailsResponse.getProductNumber(),
								CLASS_NAME, METHOD_NAME));
					}

					lstroductDetailsResponse.add(productDetailsResponse);

				}
			}

		} catch (SQLException se) {
			final StringBuffer message = new StringBuffer(300);
			message.append(" SQLException occured trying to read Account and product Level Attributes for Cardinal Items ");
			message.append(String.valueOf(se.getCause()));
			if (request != null) {
				LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
						request.getShipToCustomer() + InventoryConstants.DASH
								+ request.getDistributionCenter(),
						request.getUserId(),
						request.getTransactionId(),
						ErrorHandlingHelper
								.getErrorCodeByErrorKey(InventoryConstants.ErrorKeys.DATABASE_EXCEPTION),
						message.toString() + se.getMessage(), null, null,
						CLASS_NAME, METHOD_NAME));
			}
			throw new SystemException(
					InventoryConstants.ErrorKeys.DATABASE_EXCEPTION,
					ErrorHandlingHelper
							.getErrorCodeByErrorKey(InventoryConstants.ErrorKeys.DATABASE_EXCEPTION),
					MessageLoggerHelper.buildErrorMessage(
							request.getShipToCustomer()
									+ InventoryConstants.DASH
									+ request.getDistributionCenter(),
							request.getUserId(),
							request.getTransactionId(),
							ErrorHandlingHelper
									.getErrorCodeByErrorKey(InventoryConstants.ErrorKeys.DATABASE_EXCEPTION),
							message.toString() + se.getMessage(), null, null,
							CLASS_NAME, METHOD_NAME), se);
		} catch (SystemException sysException) {
			if (request != null) {
				LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
						request.getShipToCustomer() + InventoryConstants.DASH
								+ request.getDistributionCenter(),
						request.getUserId(), request.getTransactionId(),
						sysException.getErrorKey(),
						sysException.getErrorDescription(), null, null,
						CLASS_NAME, METHOD_NAME));
			}
			throw sysException;
		} catch (Throwable t) {
			if (request != null) {
				LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
						request.getShipToCustomer() + InventoryConstants.DASH
								+ request.getDistributionCenter(),
						request.getUserId(),
						request.getTransactionId(),
						ErrorHandlingHelper
								.getErrorCodeByErrorKey(ErrorHandlingHelper.THROWABLE_ERROR_KEY),
						t.getClass() + ": " + t.getCause() + ": "
								+ t.getMessage(), null, null, CLASS_NAME,
						METHOD_NAME));
			}
			throw new SystemException(// NOPMD by ankit.mahajan on 4/30/15 12:40 PM as per error handling framework
					ErrorHandlingHelper.THROWABLE_ERROR_KEY,
																
					ErrorHandlingHelper
							.getErrorCodeByErrorKey(ErrorHandlingHelper.THROWABLE_ERROR_KEY),
					t.getClass() + InventoryConstants.COLON + t.getCause()
							+ InventoryConstants.COLON + t.getMessage());
		} finally {

			DataSourceManager.freeConnection(connection, ps, rs);
		}
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}
		return lstroductDetailsResponse;

	}

	/**
	 * Method to get the Account and Product 
	 * level attributes for the Non-Cardinal Items
	 * <P>
	 * Calls the DB and sets values to the passed list already
	 * populated with the Cardinal item  details is any in the
	 * request.
	 *  
	 * @param products
	 * @param request
	 * @param lstPrdDetailsResponse
	 * @return
	 */
	private List<ProductDetailsResponse> getProductDetailsNonCardinalPrds(
			final List<String> products, final InvBaseRequest request,
			final List<ProductDetailsResponse> lstPrdDetailsResponse) {

		final String METHOD_NAME = "getProductDetailsNonCardinalPrds";
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		}
		
		final List<ProductDetailsResponse> lstAllPrdDetailsResponse=new ArrayList<ProductDetailsResponse>();
		
		// 15.5 AGILE Changes for production issue on NULL Pointer Exception
		if(null != lstPrdDetailsResponse && !lstPrdDetailsResponse.isEmpty()){
			lstAllPrdDetailsResponse.addAll(lstPrdDetailsResponse);
		}
	
		StringBuffer query = new StringBuffer();

		String queryNCFromPropFile = InventoryUtility.getValue(
				InventoryConstants.PHYSICAL_INVENTORY_SERVICE_NAME,
				InventoryConstants.PHYSICAL_INVENTORY_FILE_NAME,
				InventoryConstants.KEY_NC_QUERY);

		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {

			LOGGER.finest(MessageLoggerHelper.buildMessage(
					request.getShipToCustomer() + "-"
							+ request.getDistributionCenter(),
					request.getUserId(), request.getTransactionId(),
					"NC Query Before String Replacement::"
							+ queryNCFromPropFile, "CIN", products.toString(),
					CLASS_NAME, METHOD_NAME));

		}

		if (queryNCFromPropFile == null || "".equals(queryNCFromPropFile))
			return null;

		queryNCFromPropFile = queryNCFromPropFile.replaceAll(
				InventoryConstants.REGEX_VAR_CIN,
				prepareCINParamList(products.size()));

		if (MessageLoggerHelper.isTraceEnabled(LOGGER)) {

			LOGGER.finest(MessageLoggerHelper.buildMessage(
					request.getShipToCustomer() + "-"
							+ request.getDistributionCenter(),
					request.getUserId(), request.getTransactionId(),
					"Final Non-Cardinal Query After String Replacement::"
							+ queryNCFromPropFile, "CIN", products.toString(),
					CLASS_NAME, METHOD_NAME));

		}

		query.append(queryNCFromPropFile);

		Connection connection = null;// NOPMD
		ResultSet rs = null; // NOPMD
		PreparedStatement ps = null;

		try {

			connection = getConnection();

			ps = connection.prepareStatement(query.toString());

			int index = 1;
			
			
			ps.setString(index++, request.getShipToCustomer());
			ps.setString(index++, request.getDistributionCenter());
			

			ps.setString(index++, request.getShipToCustomer());
			ps.setString(index++, request.getDistributionCenter());

			for (String cin : products) {
				ps.setString(index++, cin);
			}

			rs = ps.executeQuery();

			ProductDetailsResponse productDetailsResponse = null;

			Object accountUOIFFrmDB = null;
			Object packQtyFrmDB = null;
			Object packSizeQtyFrmDB = null;
			Object uomABBFrmDB = null;
			Object formUOIFFrmDB = null;
			Object UOIFFrmDB = null;
			Object invoiceCostFrmDB = null;

			while (rs.next()) {

				accountUOIFFrmDB = rs.getObject(2);
				invoiceCostFrmDB = rs.getObject(3);
				UOIFFrmDB = rs.getObject(4);
				uomABBFrmDB = rs.getObject(5);
				packQtyFrmDB = rs.getObject(6);
				packSizeQtyFrmDB = rs.getObject(7);
				formUOIFFrmDB = rs.getObject(8);

				productDetailsResponse = new ProductDetailsResponse();

				productDetailsResponse.setProductNumber(String.valueOf(rs
						.getObject(1)));

				// defect # 14016 fix - change for UOIF from int to double to
				// include decimal values
				double uoif;
				DecimalFormat decFormatter = new DecimalFormat("#0.00");
				if (null != accountUOIFFrmDB) {
					// Defect 15404 Changes
					// Added formatter to handle exponential values

					String accountUOIF = decFormatter
							.format(deriveAccountUOIFDouble(
									(BigDecimal) accountUOIFFrmDB,// ASA.DEFAULT_UOIF_TYPE_CDE
									(BigDecimal) packQtyFrmDB, // P.PACK_QTY
									(BigDecimal) packSizeQtyFrmDB, // P.PACK_SIZE_NUM
									String.valueOf(uomABBFrmDB))); // UHC.UNIT_H_ID

					productDetailsResponse.setAccountUOIF(accountUOIF);

					uoif = deriveAccountUOIFDouble(
							(BigDecimal) accountUOIFFrmDB,
							(BigDecimal) packQtyFrmDB,
							(BigDecimal) packSizeQtyFrmDB,
							String.valueOf(uomABBFrmDB));
				} else {
					// Defect 15404 Changes
					// Added formatter to handle exponential values
					final String accountUOIF = decFormatter
							.format(deriveAccountUOIFDouble(BigDecimal.ONE,
									(BigDecimal) packQtyFrmDB,
									(BigDecimal) packSizeQtyFrmDB,
									String.valueOf(uomABBFrmDB)));

					productDetailsResponse.setAccountUOIF(accountUOIF);

					uoif = deriveAccountUOIFDouble(BigDecimal.ONE,
							(BigDecimal) packQtyFrmDB,
							(BigDecimal) packSizeQtyFrmDB,
							String.valueOf(uomABBFrmDB));
				}

				if ((null != formUOIFFrmDB) // Form UOIF
						&& (((BigDecimal) formUOIFFrmDB).intValue() != -2)) {
					if (((BigDecimal) formUOIFFrmDB).intValue() == -1) {
						if (productDetailsResponse.getAccountUOIF() != null) {
							productDetailsResponse
									.setFormUOIF(subStringUOIF(productDetailsResponse
											.getAccountUOIF()));
						}
					} else {

						// Defect 15404 Changes
						// Added formatter to handle exponential values
						final String formUOIF = decFormatter
								.format(((BigDecimal) formUOIFFrmDB)
										.doubleValue());

						productDetailsResponse
								.setFormUOIF(subStringUOIF(formUOIF));

						uoif = ((BigDecimal) formUOIFFrmDB).doubleValue();
					}
				}

				// Defect 15404 Changes
				// Added formatter to handle exponential values
				String strUoif = decFormatter.format(uoif);

				if ((null != UOIFFrmDB)
						&& (((BigDecimal) UOIFFrmDB).doubleValue() > 0)) {
					productDetailsResponse.setUOIF(subStringUOIF(String
							.valueOf(((BigDecimal) UOIFFrmDB).doubleValue())));
					uoif = ((BigDecimal) UOIFFrmDB).doubleValue();
				} else {
					productDetailsResponse.setUOIF(subStringUOIF(String
							.valueOf(strUoif)));
				}

				if (null != invoiceCostFrmDB) {

					productDetailsResponse.setInvoiceCost(Double.valueOf(String
							.valueOf(invoiceCostFrmDB)));

				}

				if (isNotEmpty((String.valueOf(packSizeQtyFrmDB)))) {
					productDetailsResponse.setPackageSize(new BigDecimal(String
							.valueOf(packSizeQtyFrmDB)).intValue());
				}

				if (isNotEmpty(String.valueOf(packQtyFrmDB))) {
					productDetailsResponse.setPackageQty(Integer.valueOf(String
							.valueOf(packQtyFrmDB)));
				}

				lstAllPrdDetailsResponse.add(productDetailsResponse);
			}

		} catch (SQLException se) {
			final StringBuffer message = new StringBuffer(300);
			message.append("SQLException occured trying to read Account and product Level Attributes for Non-Cardinal Items");
			message.append(String.valueOf(se.getCause()));
			if (request != null) {
				LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
						request.getShipToCustomer() + InventoryConstants.DASH
								+ request.getDistributionCenter(),
						request.getUserId(),
						request.getTransactionId(),
						ErrorHandlingHelper
								.getErrorCodeByErrorKey(InventoryConstants.ErrorKeys.DATABASE_EXCEPTION),
						message.toString() + se.getMessage(), null, null,
						CLASS_NAME, METHOD_NAME));
			}
			throw new SystemException(
					InventoryConstants.ErrorKeys.DATABASE_EXCEPTION,
					ErrorHandlingHelper
							.getErrorCodeByErrorKey(InventoryConstants.ErrorKeys.DATABASE_EXCEPTION),
					MessageLoggerHelper.buildErrorMessage(
							request.getShipToCustomer()
									+ InventoryConstants.DASH
									+ request.getDistributionCenter(),
							request.getUserId(),
							request.getTransactionId(),
							ErrorHandlingHelper
									.getErrorCodeByErrorKey(InventoryConstants.ErrorKeys.DATABASE_EXCEPTION),
							message.toString() + se.getMessage(), null, null,
							CLASS_NAME, METHOD_NAME), se);
		} catch (SystemException sysException) {
			if (request != null) {
				LOGGER.warning(MessageLoggerHelper.buildErrorMessage(
						request.getShipToCustomer() + InventoryConstants.DASH
								+ request.getDistributionCenter(),
						request.getUserId(), request.getTransactionId(),
						sysException.getErrorKey(),
						sysException.getErrorDescription(), null, null,
						CLASS_NAME, METHOD_NAME));
			}
			throw sysException;
		} catch (Throwable t) {
			if (request != null) {
				LOGGER.severe(MessageLoggerHelper.buildErrorMessage(
						request.getShipToCustomer() + InventoryConstants.DASH
								+ request.getDistributionCenter(),
						request.getUserId(),
						request.getTransactionId(),
						ErrorHandlingHelper
								.getErrorCodeByErrorKey(ErrorHandlingHelper.THROWABLE_ERROR_KEY),
						t.getClass() + ": " + t.getCause() + ": "
								+ t.getMessage(), null, null, CLASS_NAME,
						METHOD_NAME));
			}
			throw new SystemException(// NOPMD by ankit.mahajan on 4/30/15 12:40 PM as per error handling framework
					ErrorHandlingHelper.THROWABLE_ERROR_KEY, // NOPMD 
																
					ErrorHandlingHelper
							.getErrorCodeByErrorKey(ErrorHandlingHelper.THROWABLE_ERROR_KEY),
					t.getClass() + InventoryConstants.COLON + t.getCause()
							+ InventoryConstants.COLON + t.getMessage());
		} finally {

			DataSourceManager.freeConnection(connection, ps, rs);
		}
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}

		return lstAllPrdDetailsResponse;
	}

	/**
	 * Method to get the product details, call the 
	 * getProductDetailsCardinalPrds() for cardinal products
	 * and getProductDetailsNonCardinalPrds() for non cardinal products
	 */
	public List<ProductDetailsResponse> getProductDetails(
			final List<String> products, final InvBaseRequest request) {

		/*
		 * // final String accountNum =
		 * productDetailsRequest.getShipToCustomer() +
		 * ProductDetailsConstants.DASH +
		 * productDetailsRequest.getShipToLocation();
		 * 
		 * // we will open this validation call after tomorrow QA migration
		 * ProductDetailsValidator.validateRequest(productDetailsRequest);
		 */

		final String METHOD_NAME = "getProductDetails";
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		}
		List<ProductDetailsResponse> lstPrdDetailsResponse = null;

		if (!getPrdNum(products, false, request).isEmpty()) { // Cardinal
																// Products

			lstPrdDetailsResponse = getProductDetailsCardinalPrds(
					getPrdNum(products, false, request), request);
		}

		if (!getPrdNum(products, true, request).isEmpty()) { // Non-Cardinal
																// Products
			lstPrdDetailsResponse = getProductDetailsNonCardinalPrds(
					getPrdNum(products, true, request), request,
					lstPrdDetailsResponse);
		}
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}
		return lstPrdDetailsResponse;

	}

	/**
	 * Creates a parameterized list for cins
	 * 
	 * @param cinListSize
	 *            - size of the cin list.
	 * 
	 * @return String - paramterized string for queries
	 */
	private String prepareCINParamList(int cinListSize) {

		final StringBuffer cinListparams = new StringBuffer();
		cinListparams.append("IN (");

		for (int i = 0; i < cinListSize; i++) {
			if (i > 0) {
				cinListparams.append(", ");
			}

			cinListparams.append("? ");
		}

		cinListparams.append(") ");

		return cinListparams.toString();

	}

	/**
	 * This method is used for deriving the UOIF value.
	 * 
	 * 
	 * @param sellPriceDlr
	 *            BigDecimal
	 * @param packQty
	 *            BigDecimal
	 * 
	 * @return accountDefaultUOIF
	 */
	private final double deriveAccountUOIFDouble(
			final BigDecimal accountDefatutType, final BigDecimal packQty,
			final BigDecimal packSize, final String uom) {
		double accountDefaultUOIF;
		NumberFormat formatter = new DecimalFormat("#0.00");
		if (accountDefatutType.intValue() == 2) {
			accountDefaultUOIF = packQty.doubleValue() * packSize.doubleValue();
		} else {
			if (InventoryConstants.UOM_EA.equalsIgnoreCase(uom)
					&& (packQty.doubleValue() == 1)) {
				accountDefaultUOIF = packSize.doubleValue();
			} else {
				accountDefaultUOIF = packQty.doubleValue();
			}
		}

		return Double.parseDouble(formatter.format(accountDefaultUOIF));
	}

	/**
	 * This method trims the UOIF string to upto two decimal places
	 * 
	 * @param uoifValue
	 *            the value to trim
	 * @return uoifValue
	 */
	private String subStringUOIF(final String uoifValue) {
		String uoiftoBeChanged = "0.0";

		NumberFormat formatter = new DecimalFormat("#0.0000");
		LOGGER.fine("inside subStringUOIF method" + uoifValue);
		if (uoifValue != null && !"".equalsIgnoreCase(uoifValue)) {
			uoiftoBeChanged = uoifValue;
			int endIndex = 0;
			int startIndex = 0;
			startIndex = uoiftoBeChanged.indexOf('.');

			if (startIndex != -1) {

				endIndex = startIndex + 5;

				if (uoiftoBeChanged.length() >= endIndex) {
					uoiftoBeChanged = uoiftoBeChanged.substring(0, endIndex);

				}
			}
		}
		return formatter.format(Double.valueOf(uoiftoBeChanged).doubleValue()); // NOPMD
																				// by
																				// mayur.c.jain
																				// on
																				// 4/1/14
																				// 10:02
																				// AM
	}

	/**
	 * Validates the String for not empty value.
	 * 
	 * @param stringToValidate
	 *            - a string to validate.
	 * 
	 * @return true - if String is not empty
	 */
	private static boolean isNotEmpty(final String stringToValidate) {
		boolean isEmpty = false;

		if ((stringToValidate != null) && ("null" != stringToValidate) && // NOPMD
				(stringToValidate.trim().length() > 0)) { // NOPMD
			isEmpty = true;
		}

		return isEmpty;
	}

	/**
	 * Validates the String for not empty value.
	 * 
	 * @param stringToValidate
	 *            - a string to validate.
	 * 
	 * @return true - if String is not empty
	 */
	private static boolean isNotNull(final Object object) {
		boolean isEmpty = false;

		if (null != object) {
			isEmpty = true;
		}

		return isEmpty;
	}

	/**
	 * This method used to find cardinal/Non cardinal product number
	 */
	private List<String> getPrdNum(final List<String> prdNumList,
			final boolean isNonCardinalPrdNum, final InvBaseRequest request) {

		final String METHOD_NAME = "getPrdNum";
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		}
		List<String> prdIdList = new ArrayList<String>();

		for (String cin : prdNumList) {
			String prdNum = String.valueOf(Long.parseLong(cin));

			if ((prdNum.trim().length() == 8) && isNonCardinalPrdNum) {
				prdIdList.add(prdNum);
			}

			if ((prdNum.trim().length() != 8) && !isNonCardinalPrdNum) {
				prdIdList.add(prdNum);
			}
		}

		if (MessageLoggerHelper.isTraceEnabled(LOGGER) && isNonCardinalPrdNum) {

			LOGGER.finest(MessageLoggerHelper.buildMessage(
					request.getShipToCustomer() + "-"
							+ request.getDistributionCenter(),
					request.getUserId(), request.getTransactionId(),
					"NON - Cardinal Products Count in the Request::"
							+ prdIdList.size(), "CIN List::",
					prdIdList.toString(), CLASS_NAME, METHOD_NAME));

		}

		if (MessageLoggerHelper.isTraceEnabled(LOGGER) && !isNonCardinalPrdNum) {

			LOGGER.finest(MessageLoggerHelper.buildMessage(
					request.getShipToCustomer() + "-"
							+ request.getDistributionCenter(),
					request.getUserId(),
					request.getTransactionId(),
					"Cardinal Products Count in the Request::"
							+ prdIdList.size(), "CIN List::",
					prdIdList.toString(), CLASS_NAME, METHOD_NAME));

		}
		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}
		return prdIdList;
	}

	/**
	 * This method retrieves the <code>Connection</code> object for HSCS-P ODS
	 * Database. It Checks the null value for connection object initially.
	 * Because some time <code>Connection</code> might be created in the stand
	 * alone test class.
	 * 
	 * @return {@link Connection}
	 * 
	 * @throws SystemException
	 *             occurred due to failed to create the connection reference.
	 * @throws NamingException
	 * @throws SQLException
	 */
	protected Connection getConnection() throws SystemException {
		final String METHOD_NAME = "getConnection";

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);
		}
		Connection connection = null; // NOPMD by ankit.mahajan on 4/30/15 12:40 PM closed using utility method call

		final DataSourceManager dBAccessor = DataSourceManager.getInstance();

		final String jndiName = InventoryUtility.getValue(
				InventoryConstants.PHYSICAL_INVENTORY_SERVICE_NAME,
				InventoryConstants.PHYSICAL_INVENTORY_FILE_NAME,
				InventoryConstants.KEY_PHYSICAL_INVENTORY_JNDI);

		if (MessageLoggerHelper.isTraceEnabled(LOGGER) ) {

			LOGGER.finest("jndiName retrived from the property files for Physcial inventory::"+jndiName);

		}
		connection = dBAccessor.getConnection(jndiName);

		if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}
		return connection;
	}

}
