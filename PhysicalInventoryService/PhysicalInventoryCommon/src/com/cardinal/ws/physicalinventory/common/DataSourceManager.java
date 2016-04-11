/*********************************************************************
 *
 * $Workfile: DataSourceManager.java $
 * Copyright 2012 Cardinal Health
 *
 *********************************************************************
 *
 * Revision History: 
 *
 * $Log: om.cardinal.ws.physicalinventory.common.DataSourceManager.java $
 *
 *********************************************************************/
package com.cardinal.ws.physicalinventory.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.cardinal.webordering.common.errorhandling.ErrorHandlingHelper;
import com.cardinal.webordering.common.errorhandling.SystemException;
import com.cardinal.webordering.common.logging.MessageLoggerHelper;

/**
 * The DatabaseAccessor class is responsible for getting Connections and freeing them after
 * use. API's are also provided to close the Prepared Statement & Result Sets.
 *
 * @author Syed
 * @version 1.1 9/7/2012 [amol.lonker]
 */
public final class DataSourceManager {
    private static final String CLASS_NAME = DataSourceManager.class.getCanonicalName();
    private static final Logger LOGGER = MessageLoggerHelper.getLogger(CLASS_NAME);

    /** The Singleton instance of the DatabaseAccessor. */
    private static DataSourceManager dbConnectionFactory = null;

    static {
        if (dbConnectionFactory == null) {
            dbConnectionFactory = new DataSourceManager();
        }
    }

/**
         * This is the default constructor for the class, it is private and only
         * used from within the getInstance method
         */
    private DataSourceManager() {
    }

    /**
     * This should be called by the controllers to get an object reference to
     * DatabaseAccessor object.
     *
     * @return Object Reference to the DatabaseAccessor if it were successfully created, or returns
     *         a null if the creation failed
     */
    public static DataSourceManager getInstance() {
        return DataSourceManager.dbConnectionFactory;
    }

    /**
     * Retrieving <code>Connection</code> object based on datasourcename.
     *
     * @param dataSourceName JNDI Datasource lookup name
     *
     * @return Database Connection object from the container.
     *
     * @throws SystemException - for Naming and SQL exception.
     */
    public Connection getConnection(final String dataSourceName) throws SystemException {
        final String METHOD_NAME = "getConnection";

        if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
            LOGGER.entering(CLASS_NAME, METHOD_NAME);
        }

        Connection conn = null;// NOPMD by nagaraj.bhavi on 14/3/13 10:24 AM closing connection after using.
        try {
            // Do a lookup to get the Data Source information.
            final InitialContext ic = new InitialContext();
            final DataSource dataSource = (DataSource) ic.lookup(dataSourceName);
            // Get the database Connection object.
            conn = dataSource.getConnection();
        }
        catch (SQLException e) {
            // In case of a SQL Exception: Close the Connection.s
            if (conn != null) {
                DataSourceManager.freeConnection(conn);
            }

            LOGGER.warning(MessageLoggerHelper.buildErrorMessage(InventoryConstants.NO_SHIP_TO_NUMBER
                                                                 + InventoryConstants.DASH
                                                                 + InventoryConstants.NO_DIVISION_CODE,
                                                                 InventoryConstants.NO_USER_DISPLAY_NAME,
                                                                 InventoryConstants.NO_TRANSACTION_ID,
                                                                 ErrorHandlingHelper
                                                                                              .getErrorCodeByErrorKey(InventoryConstants.ErrorKeys.DATABASE_EXCEPTION),
                                                                 e.getMessage(), null, null,
                                                                 CLASS_NAME, METHOD_NAME));

            throw new SystemException(InventoryConstants.ErrorKeys.DATABASE_EXCEPTION, // NOPMD by nagaraj.bhavi on 03/14/13.This is required.
                                      ErrorHandlingHelper.getErrorCodeByErrorKey(InventoryConstants.ErrorKeys.DATABASE_EXCEPTION),
                                      e.getMessage());
        }
        catch (NamingException e) {
            LOGGER.warning(MessageLoggerHelper.buildErrorMessage(InventoryConstants.NO_SHIP_TO_NUMBER
                                                                 + InventoryConstants.DASH
                                                                 + InventoryConstants.NO_DIVISION_CODE,
                                                                 InventoryConstants.NO_USER_DISPLAY_NAME,
                                                                 InventoryConstants.NO_TRANSACTION_ID,
                                                                 ErrorHandlingHelper
                                                                                                             .getErrorCodeByErrorKey(InventoryConstants.ErrorKeys.NAMING_EXCEPTION_OCCURRED),
                                                                 e.getMessage(), null, null,
                                                                 CLASS_NAME, METHOD_NAME));

            throw new SystemException(InventoryConstants.ErrorKeys.NAMING_EXCEPTION_OCCURRED, // NOPMD by nagaraj.bhavi on 03/14/13.This is required.
                                      ErrorHandlingHelper.getErrorCodeByErrorKey(InventoryConstants.ErrorKeys.NAMING_EXCEPTION_OCCURRED),
                                      e.getMessage());
        }

        if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
            LOGGER.exiting(CLASS_NAME, METHOD_NAME);
        }

        // Return the Connection object.
        return conn;
    }

    /**
     * Calls close() method on the Connection object passed in.
     *
     * @param conn DB Connection Object
     *
     * @return returns true if successful.
     */
    public static boolean freeConnection(final Connection conn) {
        final String METHOD_NAME = "freeConnection";
        if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
            LOGGER.entering(CLASS_NAME, METHOD_NAME);
        }

        boolean isSuccess = true;
        try {
            if (conn != null) {
                conn.close();
            }
        }
        catch (Exception ignore) {
            isSuccess = false;
            LOGGER.warning(MessageLoggerHelper.buildErrorMessage(InventoryConstants.NO_SHIP_TO_NUMBER
                                                                 + InventoryConstants.DASH
                                                                 + InventoryConstants.NO_DIVISION_CODE,
                                                                 InventoryConstants.NO_USER_DISPLAY_NAME,
                                                                 InventoryConstants.NO_TRANSACTION_ID,
                                                                 ErrorHandlingHelper
                                                                                                                                                         .getErrorCodeByErrorKey(ErrorHandlingHelper.THROWABLE_ERROR_KEY),
                                                                 ignore.getMessage(), null, null,
                                                                 CLASS_NAME, METHOD_NAME));
        }

        if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
            LOGGER.exiting(CLASS_NAME, METHOD_NAME);
        }

        return isSuccess;
    }

    /**
     * Calls close() method on the Connection, PreparedStatement/Statement, ResultSet
     * objects passed in.
     *
     * @param connObj Database Connection Object
     * @param stmtObj Statement Object. The code will cast this object to either PreParedStatement
     *        or Statement
     * @param rsetObj ResultSet Object.
     *
     * @return returns true if all passed in objects are closed successfully.
     */
    public static boolean freeConnection(final Object connObj, final Object stmtObj,
                                         final Object rsetObj) {
        final String METHOD_NAME = "freeConnection";
        if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
            LOGGER.entering(CLASS_NAME, METHOD_NAME);
        }

        Connection conn = null;// NOPMD by nagaraj.bhavi on 14/3/13 10:24 AM closing connection below.
        PreparedStatement pstmt = null;
        Statement stmt = null;// NOPMD by nagaraj.bhavi on 14/3/13 10:24 AM closing statement below.
        ResultSet rset = null;// NOPMD by nagaraj.bhavi on 14/3/13 10:24 AM closing resultSet below.

        if (connObj instanceof Connection) {
            conn = (Connection) connObj;
        }

        if (connObj instanceof PreparedStatement) {
            pstmt = (PreparedStatement) connObj;
        }

        if (connObj instanceof Statement) {
            stmt = (Statement) connObj;
        }

        if (connObj instanceof ResultSet) {
            rset = (ResultSet) connObj;
        }

        if (stmtObj instanceof Connection) {
            conn = (Connection) stmtObj;
        }

        if (stmtObj instanceof PreparedStatement) {
            pstmt = (PreparedStatement) stmtObj;
        }

        if (stmtObj instanceof Statement) {
            stmt = (Statement) stmtObj;
        }

        if (stmtObj instanceof ResultSet) {
            rset = (ResultSet) stmtObj;
        }

        if (rsetObj instanceof Connection) {
            conn = (Connection) rsetObj;
        }

        if (rsetObj instanceof PreparedStatement) {
            pstmt = (PreparedStatement) rsetObj;
        }

        if (rsetObj instanceof Statement) {
            stmt = (Statement) rsetObj;
        }

        if (rsetObj instanceof ResultSet) {
            rset = (ResultSet) rsetObj;
        }

        boolean isSucess = true;
        if (rset != null) {
            try {
                rset.close();
            }
            catch (Exception e) {
                isSucess = false;
                LOGGER.warning(MessageLoggerHelper.buildErrorMessage(InventoryConstants.NO_SHIP_TO_NUMBER
                                                                     + InventoryConstants.DASH
                                                                     + InventoryConstants.NO_DIVISION_CODE,
                                                                     InventoryConstants.NO_USER_DISPLAY_NAME,
                                                                     InventoryConstants.NO_TRANSACTION_ID,
                                                                     ErrorHandlingHelper
                                                                                                                                                                                                                                                         .getErrorCodeByErrorKey(ErrorHandlingHelper.THROWABLE_ERROR_KEY),
                                                                     e.getMessage(), null, null,
                                                                     CLASS_NAME, METHOD_NAME));
            }
        }

        if (pstmt != null) {
            try {
                pstmt.close();
            }
            catch (Exception e) {
                isSucess = false;
                LOGGER.warning(MessageLoggerHelper.buildErrorMessage(InventoryConstants.NO_SHIP_TO_NUMBER
                                                                     + InventoryConstants.DASH
                                                                     + InventoryConstants.NO_DIVISION_CODE,
                                                                     InventoryConstants.NO_USER_DISPLAY_NAME,
                                                                     InventoryConstants.NO_TRANSACTION_ID,
                                                                     ErrorHandlingHelper
                                                                                                                                                                                                                                                                           .getErrorCodeByErrorKey(ErrorHandlingHelper.THROWABLE_ERROR_KEY),
                                                                     e.getMessage(), null, null,
                                                                     CLASS_NAME, METHOD_NAME));
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            }
            catch (Exception e) {
                isSucess = false;
                LOGGER.warning(MessageLoggerHelper.buildErrorMessage(InventoryConstants.NO_SHIP_TO_NUMBER
                                                                     + InventoryConstants.DASH
                                                                     + InventoryConstants.NO_DIVISION_CODE,
                                                                     InventoryConstants.NO_USER_DISPLAY_NAME,
                                                                     InventoryConstants.NO_TRANSACTION_ID,
                                                                     ErrorHandlingHelper
                                                                                                                                                                                                                                                                                             .getErrorCodeByErrorKey(ErrorHandlingHelper.THROWABLE_ERROR_KEY),
                                                                     e.getMessage(), null, null,
                                                                     CLASS_NAME, METHOD_NAME));
            }
        }

        if (conn != null) {
            try {
                conn.close();
            }
            catch (Exception e) {
                isSucess = false;
                LOGGER.warning(MessageLoggerHelper.buildErrorMessage(InventoryConstants.NO_SHIP_TO_NUMBER
                                                                     + InventoryConstants.DASH
                                                                     + InventoryConstants.NO_DIVISION_CODE,
                                                                     InventoryConstants.NO_USER_DISPLAY_NAME,
                                                                     InventoryConstants.NO_TRANSACTION_ID,
                                                                     ErrorHandlingHelper
                                                                                                                                                                                                                                                                                                               .getErrorCodeByErrorKey(ErrorHandlingHelper.THROWABLE_ERROR_KEY),
                                                                     e.getMessage(), null, null,
                                                                     CLASS_NAME, METHOD_NAME));
            }
        }

        if (MessageLoggerHelper.isEntryExitTraceEnabled(LOGGER)) {
            LOGGER.exiting(CLASS_NAME, METHOD_NAME);
        }

        return isSucess;
    }
}
