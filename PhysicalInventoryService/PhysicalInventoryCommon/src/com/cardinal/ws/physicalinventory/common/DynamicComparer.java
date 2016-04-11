package com.cardinal.ws.physicalinventory.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.logging.Logger;

import com.cardinal.webordering.common.logging.MessageLoggerHelper;

/**
 * This class will dynamically sort an array or ArrayList on multiple fields, in ascending or descending order, 
 * without or without case sensitivity.
 * Using this, it is no longer necessary to allow sorting by having classes implement the Comparable interface, 
 * or writing custom Comparators for each business class.
 *
 * @author rohit.bhat
 * @version $Revision: 1.0 $
 */

@SuppressWarnings("unchecked")
public class DynamicComparer implements Comparator {
	private ArrayList<SortProperty> sortprops = null;
	private Class sourceClass = null;
	
//	 private static final Logger LOGGER =
//	        MessageLoggerHelper.getLogger(InventoryUtility.class.getCanonicalName());
	 
	/**
	 * Perform a comparison between two objects taking into consideration multiple keys, desired sort order, and case sensitivity.
	 * @param x - First object to compare
	 * @param y - Second object to compare
	 */
	@Override
	public int compare(Object x, Object y) {
		int retval = 0;
		Object val1, val2 = null;
		if (x == null && y == null)
			return 0;
		for (SortProperty sp : sortprops) {
			if (sp.propertyInfo != null) {
				try {
					val1 = sp.propertyInfo.get(x);
					val2 = sp.propertyInfo.get(y);
				} catch (Exception e) {
					
					throw new RuntimeException(e.getMessage()); // NOPMD by vignesh.kandadi on 1/17/13 12:17 AM
				}
			} else if (sp.methodInfo != null) {
				try {
					val1 = sp.methodInfo.invoke(x);
					val2 = sp.methodInfo.invoke(y);
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage()); // NOPMD by vignesh.kandadi on 1/17/13 12:17 AM
				}
			}
			else {
				throw new RuntimeException("No method or property info found!"); // NOPMD by vignesh.kandadi on 1/17/13 12:17 AM
			}
			
			if (val1 == null && val2 == null)
				return 0;
			if (x == null) {
				if (sp.descending)
					return 1;
				else
					return -1;
			}
			if (y == null) {
				if (sp.descending)
					return -1;
				else
					return 1;
			}
			
			if (val1 == null) {
				if (sp.descending)
					return 1;
				else
					return -1;
			}
			if (val2 == null) {
				if (sp.descending)
					return -1;
				else
					return 1;
			}
			
			if (val1 instanceof String) {
				val1 = val1.toString();
				val2 = val2.toString();
				if (sp.caseInsensitive) {
					val1 = ((String)val1).toLowerCase();
					val2 = ((String)val2).toLowerCase();
				}
			}
			retval = ((Comparable)val1).compareTo(val2);
			if (retval != 0) {
				if (sp.descending) {
					return retval * -1;
				} else {
					return retval;
				}
			}
		}
		return 0; // No differences found.
	}
	
	/**
	 * DynamicComparer constructor.
	 * <br>
	 * See class documentation 
	 * for detailed description and usage examples.
	 * @param sourceClass - class of items in array or list
	 * @param orderBy - string containing sort criteria.
	 * <p>
	 */
	public DynamicComparer(Class sourceClass, String orderBy) {	
		this.sourceClass = sourceClass;
		ArrayList<SortProperty> tmp = parseOrderBy(orderBy);  
		checkSortProperties(tmp);
		sortprops = tmp;
	}
	
	/**
	 * Checks a list of sort properties, making sure the field is public, 
	 * exists in the class, and implements Comparable. 
	 * <br>
	 * If any of these aren't true, it throws a RuntimeException.
	 * @param sortprops - An ArrayList of SortProperty
	 * <p>
	 */
	private void checkSortProperties(ArrayList<SortProperty> sortprops) {
		// Make sure each property exists, is public, implements Comparable
		Field fi = null;
		Method mi = null;
		Class t = null;
		if (sortprops.isEmpty())
			throw new RuntimeException("No valid sort fields found!"); // NOPMD by vignesh.kandadi on 1/17/13 12:17 AM
		for (SortProperty sProp : sortprops) {
				// Look for a field with that name   
				try {
					fi = this.sourceClass.getField(sProp.name); 
					t = fi.getType();
				} catch (SecurityException e) {
					throw new RuntimeException(String.format("Security error accessing field '%s' in class '%s'!\n", sProp.name, sourceClass.getSimpleName())); // NOPMD by vignesh.kandadi on 1/17/13 12:17 AM
				} catch (NoSuchFieldException e) {
					// Look for a method with that name
					try {
						mi = this.sourceClass.getMethod(sProp.name, (Class [])null);
						t = mi.getReturnType();
					} catch (SecurityException e1) {
						throw new RuntimeException(String.format("Security error accessing field '%s' in class '%s'!\n", sProp.name, sourceClass.getSimpleName())); // NOPMD by vignesh.kandadi on 1/17/13 12:18 AM
					} catch (NoSuchMethodException e1) {
						throw new RuntimeException(String.format("Field '%s' not found in class '%s'!\n", sProp.name, sourceClass.getSimpleName())); // NOPMD by vignesh.kandadi on 1/17/13 12:18 AM
					}
				}
				// Make sure it's comparable 
				if ( !(t.isPrimitive() || Comparable.class.isAssignableFrom(t)) ) {
					throw new RuntimeException(String.format("Field '%s' (%s) in class '%s' doesn't implement Comparable.\n", sProp.name, t.getName(), sourceClass.getSimpleName())); // NOPMD by vignesh.kandadi on 1/17/13 12:18 AM
				}
			if (fi != null) {
				sProp.propertyInfo = fi;
			}
			else {
				sProp.methodInfo = mi;
			}
		}
	}
	
	/**
	 * Converts a string containing sort criteria into a list of SortProperty.
	 * @param orderBy - string containing sort criteria.
	 * @return ArrayList of SortProperty.
	 * <p>
	 */
	private ArrayList<SortProperty> parseOrderBy(String orderBy) {
		String orderByVal="";
		if (orderBy != null) {
			orderByVal = orderBy.replace(" ", " ");
			orderByVal = orderBy.trim();
		}
		if (orderBy == null || orderBy.length() == 0)
			throw new IllegalArgumentException("The \"order by\" string must not be null or empty.");
		
		String[] props = orderByVal.split(",");
		ArrayList<SortProperty> sortProps = new ArrayList<SortProperty>();
		String propName;
		boolean descending;
		boolean caseInsensitive;
		
		for (int i = 0; i < props.length; i++) {
			descending = false;
			caseInsensitive = false;
			propName = props[i].replace(" ", " ");
			
			for (;;) {
				int ctr = 0;
				
				propName = propName.trim();
				if (propName.toUpperCase().endsWith(" DESC")) {
					descending = true;
					caseInsensitive = true;
					propName = propName.substring(0, propName.length() - 5);
					ctr += 1;
				}
				
				propName = propName.trim();
				if (propName.toUpperCase().endsWith(" ASC")) {
					caseInsensitive = true;
					propName = propName.substring(0, propName.length() - 4);
					ctr += 1;
				}
				
				propName = propName.trim();
				if (propName.toUpperCase().endsWith(" CINS")) {
					caseInsensitive = true;
					propName = propName.substring(0, propName.length() - 5);
					ctr += 1;
				}
				
				if (ctr == 0)
					break;
			}
			
			// Add sort property to list
			sortProps.add(new SortProperty(propName, descending, caseInsensitive));
		}
		
		return sortProps;
	}
	
}
/**
 * A class used by DynamicComparer to store field level sorting information
 * <p>
 */
class SortProperty {
	public String name;
	public boolean descending;
	public boolean caseInsensitive;
	public Field propertyInfo;
	public Method methodInfo;
	public boolean isString;
	
	SortProperty(String propertyName, boolean sortDescending, boolean caseInsensitive) {
		if (propertyName == null || propertyName.trim().length() == 0)
			throw new IllegalArgumentException("A property cannot have an empty name.");
		this.name 				= propertyName.trim();
		this.descending 		= sortDescending;
		this.caseInsensitive 	= caseInsensitive;
	}
}
	