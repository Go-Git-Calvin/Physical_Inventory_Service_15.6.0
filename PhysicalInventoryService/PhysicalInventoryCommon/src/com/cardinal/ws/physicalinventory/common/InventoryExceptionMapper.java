package com.cardinal.ws.physicalinventory.common;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.core.Response.Status.Family;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.xml.sax.SAXException;

import com.cardinal.webordering.common.errorhandling.BusinessException;
import com.cardinal.webordering.common.errorhandling.ErrorHandlingHelper;
//import com.cardinal.webordering.common.errorhandling.ErrorHandlingHelper;
import com.cardinal.webordering.common.errorhandling.SystemException;
import com.cardinal.webordering.common.logging.AlertLevel;
import com.cardinal.webordering.common.logging.AlertType;

 
public final class InventoryExceptionMapper {

	private InventoryExceptionMapper() {}

	public enum Status implements StatusType {
        /**
         * 200 OK, see {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.2.1">HTTP/1.1 documentation</a>}.
         */
        OK(200, "OK"),
        /**
         * 201 Created, see {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.2.2">HTTP/1.1 documentation</a>}.
         */
        CREATED(201, "Created"),
        /**
         * 202 Accepted, see {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.2.3">HTTP/1.1 documentation</a>}.
         */
        ACCEPTED(202, "Accepted"),
        /**
         * 204 No Content, see {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.2.5">HTTP/1.1 documentation</a>}.
         */
        NO_CONTENT(204, "No Content"),
        /**
         * 303 See Other, see {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.3.2">HTTP/1.1 documentation</a>}.
         */
        MOVED_PERMANENTLY(301, "Moved Permanently"),
        /**
         * 303 See Other, see {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.3.4">HTTP/1.1 documentation</a>}.
         */
        SEE_OTHER(303, "See Other"),
        /**
         * 304 Not Modified, see {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.3.5">HTTP/1.1 documentation</a>}.
         */
        NOT_MODIFIED(304, "Not Modified"),
        /**
         * 307 Temporary Redirect, see {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.3.8">HTTP/1.1 documentation</a>}.
         */
        TEMPORARY_REDIRECT(307, "Temporary Redirect"),
        /**
         * 400 Bad Request, see {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.1">HTTP/1.1 documentation</a>}.
         */
        BAD_REQUEST(400, "Bad Request"),
        /**
         * 401 Unauthorized, see {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.2">HTTP/1.1 documentation</a>}.
         */
        UNAUTHORIZED(401, "Unauthorized"),
        /**
         * 403 Forbidden, see {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.4">HTTP/1.1 documentation</a>}.
         */
        FORBIDDEN(403, "Forbidden"),
        /**
         * 404 Not Found, see {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.5">HTTP/1.1 documentation</a>}.
         */
        NOT_FOUND(404, "Not Found"),
        /**
         * 406 Not Acceptable, see {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.7">HTTP/1.1 documentation</a>}.
         */
        NOT_ACCEPTABLE(406, "Not Acceptable"),
        /**
         * 409 Conflict, see {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.10">HTTP/1.1 documentation</a>}.
         */
        CONFLICT(409, "Conflict"),
        /**
         * 410 Gone, see {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.11">HTTP/1.1 documentation</a>}.
         */
        GONE(410, "Gone"),
        /**
         * 412 Precondition Failed, see {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.13">HTTP/1.1 documentation</a>}.
         */
        PRECONDITION_FAILED(412, "Precondition Failed"),
        /**
         * 415 Unsupported Media Type, see {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.16">HTTP/1.1 documentation</a>}.
         */
        UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
        /**
         * 500 Internal Server Error, see {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.5.1">HTTP/1.1 documentation</a>}.
         */
        INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
        /**
         * 501 Service Unavailable, see {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.5.4">HTTP/1.1 documentation</a>}.
         */
        SERVICE_UNAVAILABLE(501, "Service Unavailable"),
        /**
         * 600 Service Unavailable, see {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.5.4">HTTP/1.1 documentation</a>}.
         */
        INTERNAL_SERVER__ERROR_NEW(600, "Internal Server new"),
        /**
         * 603 Service Unavailable, see {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.5.4">HTTP/1.1 documentation</a>}.
         */
        SERVICE_UNAVAILABLE_NEW(603, "Service Unavailable new"),
        /**
         * 605 Service Unavailable, see {@link <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.5.4">HTTP/1.1 documentation</a>}.
         */
        SYSTEM_EXCEPTION(605, "System Exception");
       

        private final int code;
        private final String reason;
        private javax.ws.rs.core.Response.Status.Family family;

        /**
         * An enumeration representing the class of status code. Family is used
         * here since class is overloaded in Java.
         */
       // public enum Family {INFORMATIONAL, SUCCESSFUL, REDIRECTION, CLIENT_ERROR, SERVER_ERROR, OTHER};

        Status(final int statusCode, final String reasonPhrase) {
            this.code = statusCode;
            this.reason = reasonPhrase;
            switch(code/100) {
                case 1: this.family = Family.INFORMATIONAL; break;
                case 2: this.family = Family.SUCCESSFUL; break;
                case 3: this.family = Family.REDIRECTION; break;
                case 4: this.family = Family.CLIENT_ERROR; break;
                case 6: this.family = Family.SERVER_ERROR; break;
                default: this.family = Family.OTHER; break;
            }
        }

        /**
         * Get the class of status code
         * @return the class of status code
         */
        public javax.ws.rs.core.Response.Status.Family getFamily() {
            return family;
        }

        /**
         * Get the associated status code
         * @return the status code
         */
        public int getStatusCode() {
            return code;
        }

        /**
         * Get the reason phrase
         * @return the reason phrase
         */
        public String getReasonPhrase() {
            return toString();
        }

        /**
         * Get the reason phrase
         * @return the reason phrase
         */
        @Override
        public String toString() {
            return reason;
        }

        /**
         * Convert a numerical status code into the corresponding Status
         * @param statusCode the numerical status code
         * @return the matching Status or null is no matching Status is defined
         */
        public static Status fromStatusCode(final int statusCode) {
            for (Status s : Status.values()) {
                if (s.code == statusCode) {
                    return s;
                }
            }
            return null;
        }
    }
    
	public static ResponseBuilder handleException(Throwable t) {
		
		 if (t instanceof IllegalArgumentException) {
			 String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(InventoryConstants.ErrorKeys.ILLEGAL_ARG_EXCEPTION);
	            Response.ResponseBuilder builder = Response.status(InventoryExceptionMapper.Status.BAD_REQUEST);
	            builder.header(InventoryConstants.PHYS_INV_STATUS_CODE,statusCode);
	            builder.header(InventoryConstants.PHYS_INV_STATUS_MSG,ErrorHandlingHelper.getUserFriendlyByCode(statusCode));
	            builder.type("application/json").entity(t.getMessage());
	            return builder;
	        }
	        else if (t instanceof JsonParseException) {
	        	String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(InventoryConstants.ErrorKeys.PARSE_EXCEPTION);
	            Response.ResponseBuilder builder = Response.status(InventoryExceptionMapper.Status.SYSTEM_EXCEPTION);
	            builder.header(InventoryConstants.PHYS_INV_STATUS_CODE,statusCode );
	            builder.header(InventoryConstants.PHYS_INV_STATUS_MSG, ErrorHandlingHelper.getUserFriendlyByCode(statusCode));
	            builder.type("application/json").entity(t.getMessage());
	            return builder;
	        }
	        else if (t instanceof JsonMappingException) {
	        	String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(InventoryConstants.ErrorKeys.MAPPING_EXCEPTION);
	            Response.ResponseBuilder builder = Response.status(InventoryExceptionMapper.Status.SYSTEM_EXCEPTION);
	            builder.header(InventoryConstants.PHYS_INV_STATUS_CODE, statusCode);
	            builder.header(InventoryConstants.PHYS_INV_STATUS_MSG,ErrorHandlingHelper.getUserFriendlyByCode(statusCode));
	            builder.type("application/json").entity(t.getMessage());
	            return builder;
	        }
	        else if (t instanceof NamingException) {
	        	String statusCode= ErrorHandlingHelper.getErrorCodeByErrorKey(InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME);
	            Response.ResponseBuilder builder = Response.status(InventoryExceptionMapper.Status.SYSTEM_EXCEPTION);
	            builder.header(InventoryConstants.PHYS_INV_STATUS_CODE,statusCode);
	            builder.header(InventoryConstants.PHYS_INV_STATUS_MSG,ErrorHandlingHelper.getUserFriendlyByCode(statusCode));
	            builder.type("application/json").entity(t.getMessage());
	            return builder;
	        } 
	        else if(t instanceof SystemException) {
	        	String statusCode= ErrorHandlingHelper.getErrorCodeByErrorKey(InventoryConstants.ErrorKeys.SYSTEMEXCEPTION_KEY);
	            Response.ResponseBuilder builder = Response.status(InventoryExceptionMapper.Status.INTERNAL_SERVER__ERROR_NEW);
	            builder.header(InventoryConstants.PHYS_INV_STATUS_CODE,statusCode);
	            builder.header(InventoryConstants.PHYS_INV_STATUS_MSG,ErrorHandlingHelper.getUserFriendlyByCode(statusCode));
	            builder.type("application/json").entity(t.getMessage());
	            return builder;
	        } else if (t instanceof BusinessException) {
	        	String statusCode= ErrorHandlingHelper.getErrorCodeByErrorKey(InventoryConstants.ErrorKeys.BUSINESSEXCEPTION_KEY);
	            Response.ResponseBuilder builder = Response.status(InventoryExceptionMapper.Status.SYSTEM_EXCEPTION);
	            builder.header(InventoryConstants.PHYS_INV_STATUS_CODE,statusCode);
	            builder.header(InventoryConstants.PHYS_INV_STATUS_MSG,ErrorHandlingHelper.getUserFriendlyByCode(statusCode));
	            builder.type("application/json").entity(t.getMessage());
	            return builder;
	        } else if (t instanceof SecurityException) {
	        	String statusCode= ErrorHandlingHelper.getErrorCodeByErrorKey(InventoryConstants.ErrorKeys.SECURITYEXCEPTION_KEY);
	            Response.ResponseBuilder builder = Response.status(InventoryExceptionMapper.Status.UNAUTHORIZED);
	            builder.header(InventoryConstants.PHYS_INV_STATUS_CODE,statusCode);
	            builder.header(InventoryConstants.PHYS_INV_STATUS_MSG,ErrorHandlingHelper.getUserFriendlyByCode(statusCode));
	            builder.type("application/json").entity(t.getMessage());
	            return builder;
	        } else if (t instanceof IOException) {
	        	String statusCode= ErrorHandlingHelper.getErrorCodeByErrorKey(InventoryConstants.ErrorKeys.IO_EXCEPTION);
	            Response.ResponseBuilder builder = Response.status(InventoryExceptionMapper.Status.UNAUTHORIZED);
	            builder.header(InventoryConstants.PHYS_INV_STATUS_CODE,statusCode);
	            builder.header(InventoryConstants.PHYS_INV_STATUS_MSG,ErrorHandlingHelper.getUserFriendlyByCode(statusCode));
	            builder.type("application/json").entity(t.getMessage());
	            return builder;
	        } else if (t instanceof MalformedURLException) {
	        	String statusCode= ErrorHandlingHelper.getErrorCodeByErrorKey(InventoryConstants.ErrorKeys.INVALID_URL);
	            Response.ResponseBuilder builder = Response.status(InventoryExceptionMapper.Status.SYSTEM_EXCEPTION);
	            builder.header(InventoryConstants.PHYS_INV_STATUS_CODE,statusCode);
	            builder.header(InventoryConstants.PHYS_INV_STATUS_MSG,ErrorHandlingHelper.getUserFriendlyByCode(statusCode));
	            builder.type("application/json").entity(t.getMessage());
	            return builder;
	        } else if (t instanceof SAXException) {
	        	String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(InventoryConstants.ErrorKeys.SAX_EXCEPTION);
	            Response.ResponseBuilder builder = Response.status(InventoryExceptionMapper.Status.SYSTEM_EXCEPTION);
	            builder.header(InventoryConstants.PHYS_INV_STATUS_CODE,statusCode);
	            builder.header(InventoryConstants.PHYS_INV_STATUS_MSG,ErrorHandlingHelper.getUserFriendlyByCode(statusCode));
	            builder.type("application/json").entity(t.getMessage());
	            return builder;
	        }
	        else if (t instanceof ParserConfigurationException) {
	        	String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(InventoryConstants.ErrorKeys.PARSE_EXCEPTION);
	            Response.ResponseBuilder builder = Response.status(InventoryExceptionMapper.Status.BAD_REQUEST);
	            builder.header(InventoryConstants.PHYS_INV_STATUS_CODE,statusCode);
	            builder.header(InventoryConstants.PHYS_INV_STATUS_MSG,ErrorHandlingHelper.getUserFriendlyByCode(statusCode));
	            builder.type("application/json").entity(t.getMessage());
	            return builder;
	        }
	        else if (t instanceof SOAPException) {
	        	String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(InventoryConstants.ErrorKeys.SOAP_EXCEPTION);
	            Response.ResponseBuilder builder = Response.status(InventoryExceptionMapper.Status.BAD_REQUEST);
	            builder.header(InventoryConstants.PHYS_INV_STATUS_CODE,statusCode);
	            builder.header(InventoryConstants.PHYS_INV_STATUS_MSG,ErrorHandlingHelper.getUserFriendlyByCode(statusCode));
	            builder.type("application/json").entity(t.getMessage());
	            return builder;
	        }
	        else {
	        	String statusCode=ErrorHandlingHelper.getErrorCodeByErrorKey(InventoryConstants.ErrorKeys.UNKNOWN_EXCEPTION);
	            Response.ResponseBuilder builder = Response.serverError();
	            builder.type("application/json").entity(t.getMessage());
	            builder.header(InventoryConstants.PHYS_INV_STATUS_CODE,statusCode);
	            builder.header(InventoryConstants.PHYS_INV_STATUS_MSG,ErrorHandlingHelper.getUserFriendlyByCode(statusCode));
	           
	            return builder;
	        }
	}
	
	public static Map<String, String> getAlertMessage(Throwable t) {
		Map<String, String> errorMap = null;
		
		if (t instanceof IllegalArgumentException || 
				t instanceof JsonParseException ||
				t instanceof JsonMappingException ||
				t instanceof ParseException) {
			errorMap = new HashMap<String, String>();
			errorMap.put(InventoryConstants.ERRORCODE, 
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							InventoryConstants.PHYS_INV_REST_INVALID_REQ));
			errorMap.put(InventoryConstants.ERRORMESSAGE, InventoryConstants.INVALID_REQ_ERROR_MSG);
			errorMap.put(InventoryConstants.ERRORALERTLEVEL, AlertLevel.WARNING);
			errorMap.put(InventoryConstants.ERRORALERTTYPE, AlertType.BUSINESS);
		}
		else if (t instanceof NamingException ) { // Needs fixed 
			errorMap = new HashMap<String, String>();
			errorMap.put(InventoryConstants.ERRORCODE, 
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME));
			errorMap.put(InventoryConstants.ERRORMESSAGE, InventoryConstants.ErrorKeys.INVALID_EJB_JNDI_NAME_DESC);
			errorMap.put(InventoryConstants.ERRORALERTLEVEL, AlertLevel.WARNING);
			errorMap.put(InventoryConstants.ERRORALERTTYPE, AlertType.BUSINESS);
		} else if (t instanceof SystemException){
			errorMap = new HashMap<String, String>();
			errorMap.put(InventoryConstants.ERRORCODE, ((SystemException) t).getErrorCode());
			errorMap.put(InventoryConstants.ERRORMESSAGE, ((SystemException) t).getErrorDescription());
			errorMap.put(InventoryConstants.ERRORALERTLEVEL, AlertLevel.WARNING);
			errorMap.put(InventoryConstants.ERRORALERTTYPE, AlertType.SYSTEM);
		}else if (t instanceof BusinessException){
			errorMap = new HashMap<String, String>();
			errorMap.put(InventoryConstants.ERRORCODE, ((BusinessException) t).getErrorCode());
			errorMap.put(InventoryConstants.ERRORMESSAGE, ((BusinessException) t).getErrorDescription());
			errorMap.put(InventoryConstants.ERRORALERTLEVEL, AlertLevel.WARNING);
			errorMap.put(InventoryConstants.ERRORALERTTYPE, AlertType.BUSINESS);
		}else {
			errorMap = new HashMap<String, String>();
			errorMap.put(InventoryConstants.ERRORCODE, 
					ErrorHandlingHelper.getErrorCodeByErrorKey(
							InventoryConstants.ErrorKeys.UNKNOWN_EXCEPTION));
			errorMap.put(InventoryConstants.ERRORMESSAGE, InventoryConstants.ErrorKeys.UNKNOWN_EXCEPTION_DESC);
			errorMap.put(InventoryConstants.ERRORALERTLEVEL, AlertLevel.WARNING);
			errorMap.put(InventoryConstants.ERRORALERTTYPE, AlertType.BUSINESS);
		}
		
		return errorMap;
	}

    /**
     * The getSource method will fetch the class and method which throws exception.
     *
     * @param t Throwable
     *
     * @return Map containing source class and method
     */
    public static HashMap<String, String> getSource(Throwable t) {
        HashMap<String, String> sourceMap = new HashMap<String, String>();

        if (t != null) {
            StackTraceElement[] traceElement = t.getStackTrace();

            if ((traceElement != null) && (traceElement.length > 0) && (traceElement[0] != null)) {
                sourceMap.put("CLASS_NAME", traceElement[0].getClassName());
                sourceMap.put("METHOD_NAME", traceElement[0].getMethodName());
            }
        }

        return sourceMap;
    }
    
    /**
     * The getMessage method returns exception cause
     * @param t
     * @return message
     */
	  public static String getMessage(Throwable t) {
	        
	        String message = null;
	        
			if (t != null) {
				if (t instanceof SystemException
						&& ((SystemException) t).getCause() != null) {
					message = ((SystemException) t).getCause().getMessage();
				} else if (t instanceof BusinessException
						&& ((BusinessException) t).getThrowable() != null) {
					message = ((BusinessException) t).getThrowable().getMessage();
				} else {
					message = t.getMessage();
				}
			}
			return message;
	    }
	  
	  /**
		 * The getThrowable return the appropriate Throwable 
		 * @param t
		 * @return Throwable
		 */
		public static Throwable getThrowable(Throwable t) {

			Throwable throwable = null;

			if (t != null) {
				if (t instanceof SystemException) {
					throwable = ((SystemException) t).getCause();
				} else if (t instanceof BusinessException) {
					throwable = ((BusinessException) t).getThrowable();
				} else {
					throwable = t;
				}
			}
			return throwable;
		}
}
