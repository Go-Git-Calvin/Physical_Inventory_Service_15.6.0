package com.cardinal.ws.rest.physicalinventory.application;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Logger;

import javax.ws.rs.core.Application;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.cardinal.webordering.common.logging.MessageLoggerHelper;
import com.cardinal.ws.rest.physicalinventory.resources.ImportResource;
import com.cardinal.ws.rest.physicalinventory.resources.InventoryResource;

public class PhysicalInventoryApplication extends Application {
	
//	 private static final String CLASS_NAME = PhysicalInventoryApplication.class.getCanonicalName();
//	 private static final Logger LOGGER = MessageLoggerHelper.getLogger(CLASS_NAME);

	
	
	 public PhysicalInventoryApplication()  { 
		 super();
	 }

    @Override
    public Set<Class<?>> getClasses() {
        final Set <Class<?>> classes = new HashSet<Class<?>>();
        classes.add(InventoryResource.class);
        classes.add(ImportResource.class);
        return classes;
    }

	@SuppressWarnings("deprecation")
	@Override
	public Set<Object> getSingletons() {
		final Set<Object> s = new HashSet<Object>();

        // Register the Jackson provider for JSON
        final ObjectMapper mapper = new ObjectMapper();
        // suppress nulls and defaults in POJOs
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_DEFAULT);
        final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy"); // NOPMD by stephen.perry01 on 5/24/13 3:32 PM
        mapper.getSerializationConfig().setDateFormat(dateFormat);
        mapper.getDeserializationConfig().setDateFormat(dateFormat);

        final JacksonJsonProvider basicProvider = new JacksonJsonProvider();
        basicProvider.setMapper(mapper);

        s.add(basicProvider);
        return s;
	}
}