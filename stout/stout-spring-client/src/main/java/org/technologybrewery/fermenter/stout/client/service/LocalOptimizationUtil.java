package org.technologybrewery.fermenter.stout.client.service;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.technologybrewery.fermenter.stout.exception.UnrecoverableException;
import org.technologybrewery.fermenter.stout.messages.Messages;
import org.technologybrewery.fermenter.stout.page.PageWrapper;
import org.technologybrewery.fermenter.stout.page.json.FindByExampleCriteria;
import org.technologybrewery.fermenter.stout.service.ValueServiceResponse;
import org.technologybrewery.fermenter.stout.transfer.TransferObject;
import org.technologybrewery.fermenter.stout.util.SpringApplicationContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gc.iotools.stream.os.OutputStreamToInputStream;

/**
 * Utility methods leverages to perform locally optimized delegate calls.
 */
public final class LocalOptimizationUtil {

    private static final Logger logger = LoggerFactory.getLogger(LocalOptimizationUtil.class);

    private static LocalOptimizationUtil instance = new LocalOptimizationUtil();

    private static ObjectMapper objectMapper;

    private LocalOptimizationUtil() {
        // prevent instantiation of all static class
    }

    /**
     * Checks to see if a local service is present for optimization. Requires that the class is in the classpath AND
     * that service is registered and available via Spring.
     * 
     * If only the class is available, but the Spring bean is not, will emit a warning.
     * 
     * @param serviceClassName
     *            fully qualified class name of the service
     * @return whether or not a local optimization is available
     */
    public static boolean isLocalServicePresent(String serviceClassName) {
        boolean containsServiceBean = false;

        Class<?> serviceClass = lookupClassByName(serviceClassName);

        if (serviceClass != null) {
            ApplicationContext applicationContext = SpringApplicationContextHolder.getContext();
            if (applicationContext != null) {
                String[] beans = applicationContext.getBeanNamesForType(serviceClass);
                containsServiceBean = (beans != null && beans.length == 1);
            }

            if (!containsServiceBean) {
                String message = "{} exists in the classpath - but not as a Spring bean.  Resolution options:\n"
                        + "\t * Remove the jar containing this class from your classpath to use Stout remoting\n"
                        + "\t * Register the beans for this service so local service optimization can occur\n\n"
                        + "Remote calls will continue to happen over HTTP/S until this is resolved!";
                logger.warn(message, serviceClassName);
            }

        }

        return containsServiceBean;
    }

    /**
     * Converts a List of TransferObjects to a List of corresponding BusinessObjects.
     * 
     * @param transferObjectList
     *            list of transfer objects
     * @param objectMapper
     *            ObjectMapper to perform the transformation
     * @return list of business objects
     */
    public static Object convertToBusinessObjectViaJson(Collection<? extends TransferObject> transferObjectList,
            ObjectMapper objectMapper) {
        List<Object> resultList = (transferObjectList != null) ? new ArrayList<>(transferObjectList.size())
                : Collections.emptyList();

        for (TransferObject transferObject : transferObjectList) {
            Object businessObject = convertToBusinessObjectViaJson(transferObject, objectMapper);
            resultList.add(businessObject);
        }

        return resultList;

    }

    /**
     * Converts {@link FindByExampleCriteria} with a TransferObject probe to a one containing the equivalent
     * BusinessObject probe.
     * 
     * @param criteria
     *            criteria containing a TransferObject probe
     * @param objectMapper
     *            ObjectMapper to perform the transformation
     * @return equivalent criteria containing a BusinessObject probe
     */
    public static Object convertToBusinessObjectViaJson(FindByExampleCriteria<? extends TransferObject> criteria,
            ObjectMapper objectMapper) {
        FindByExampleCriteria<Object> businessObjectCriteria = new FindByExampleCriteria<>();
        TransferObject probe = criteria.getProbe();
        if (probe != null) {
            Object businessObjectProbe = convertToBusinessObjectViaJson(probe, objectMapper);
            businessObjectCriteria.setProbe(businessObjectProbe);
        }

        businessObjectCriteria.setContainsMatch(criteria.getContainsMatch());
        businessObjectCriteria.setCount(criteria.getCount());
        businessObjectCriteria.setSortWrapper(criteria.getSortWrapper());
        businessObjectCriteria.setStartPage(criteria.getStartPage());

        return businessObjectCriteria;

    }

    /**
     * Converts a TransferObject to a corresponding BusinessObject.
     * 
     * @param transferObject
     *            list of transfer objects
     * @param objectMapper
     *            ObjectMapper to perform the transformation
     * @return business object
     */
    public static Object convertToBusinessObjectViaJson(TransferObject transferObject, ObjectMapper objectMapper) {
        Object result = null;

        try {
            TransferObjectJsonOutputStream streamOutputToInput = instance.new TransferObjectJsonOutputStream(
                    objectMapper, transferObject.getClass());

            try {
                objectMapper.writeValue(streamOutputToInput, transferObject);

            } finally {
                IOUtils.closeQuietly(streamOutputToInput);
                result = streamOutputToInput.getResult();
            }

        } catch (Exception e) {
            throw new UnrecoverableException(
                    "Could not marshall from " + transferObject.getClass() + " to the BusinessObject equivilant!", e);
        }

        return result;
    }

    /**
     * Converts a ValueServiceResponse containing BusinessObject(s) to a ValueServiceResponse containing
     * TransferObject(s).
     * 
     * @param input
     *            the inbound ValueServiceResponse
     * @param transferObjectTargetClass
     *            the transfer object class we want to have in the result
     * @param objectMapper
     *            ObjectMapper to perform the transformation
     * @return a ValueServiceResponse with a TransferObject-based payload
     */
    public static Object convertResponseToTransferObjectViaJson(Object input, Class<?> transferObjectTargetClass,
            ObjectMapper objectMapper) {
        ValueServiceResponse<TransferObject> result = null;

        @SuppressWarnings("unchecked")
        ValueServiceResponse<Object> businessObjectServiceResponse = (ValueServiceResponse<Object>) input;

        try {
            BusinessObjectJsonOutputStream streamOutputToInput = instance.new BusinessObjectJsonOutputStream(
                    objectMapper, transferObjectTargetClass);

            try {
                objectMapper.writeValue(streamOutputToInput, businessObjectServiceResponse.getValue());

            } finally {
                IOUtils.closeQuietly(streamOutputToInput);
                result = streamOutputToInput.getResult();
                Messages originalMessages = businessObjectServiceResponse.getMessages();
                result.getMessages().addMessages(originalMessages);
            }

        } catch (Exception e) {
            Object businessObject = (businessObjectServiceResponse != null) ? businessObjectServiceResponse.getValue()
                    : null;
            String businessObjectClass = (businessObject != null) ? businessObject.getClass().getName() : null;
            throw new UnrecoverableException("Could not marshall from " + businessObjectClass + " to "
                    + transferObjectTargetClass + " equivilant!", e);
        }

        return result;
    }

    /**
     * Converts a ValueServiceResponse containing BusinessObject(s) to a ValueServiceResponse containing
     * TransferObject(s).
     * 
     * @param input
     *            the inbound ValueServiceResponse
     * @param transferObjectTargetClass
     *            the transfer object class we want to have in the result
     * @param objectMapper
     *            ObjectMapper to perform the transformation
     * @return a ValueServiceResponse with a TransferObject-based payload
     */
    public static Object convertResponseToTransferObjectListViaJson(Object input, Class<?> transferObjectTargetClass,
            ObjectMapper objectMapper) {

        @SuppressWarnings("unchecked")
        ValueServiceResponse<Collection<Object>> typedInput = (ValueServiceResponse<Collection<Object>>) input;

        Messages originalMessages = typedInput.getMessages();
        ValueServiceResponse<Collection<TransferObject>> result = new ValueServiceResponse<>();

        BusinessObjectCollectionJsonOutputStream streamOutputToInput = instance.new BusinessObjectCollectionJsonOutputStream(
                objectMapper, transferObjectTargetClass);
        try {
            try {
                objectMapper.writeValue(streamOutputToInput, typedInput.getValue());

            } finally {
                IOUtils.closeQuietly(streamOutputToInput);
                Collection<TransferObject> transferObjects = streamOutputToInput.getResult();
                result.setValue(transferObjects);
                result.getMessages().addMessages(originalMessages);
            }
        } catch (Exception e) {
            throw new UnrecoverableException("Could not convert list of BusinessObjects to a list of TransferObjects!",
                    e);
        }

        return result;

    }

    /**
     * Converts a ValueServiceResponse<PageWrapper> containing BusinessObject(s) to a ValueServiceResponse<PageWrapper> containing TransferObject(s).
     * 
     * @param input
     *            the inbound ValueServiceResponse<PageWrapper>
     * @param transferObjectTargetClass
     *            the transfer object class we want to have in the result
     * @param objectMapper
     *            ObjectMapper to perform the transformation
     * @return a ValueServiceResponse<PageWrapper> with a TransferObject-based payload
     */
    public static Object convertResponseToTransferObjectPageWrapperViaJson(Object input,
            Class<?> transferObjectTargetClass, ObjectMapper objectMapper) {

        @SuppressWarnings("unchecked")
        ValueServiceResponse<PageWrapper<Object>> typedInput = (ValueServiceResponse<PageWrapper<Object>>) input;
        PageWrapper<Object> inboundWrapper = typedInput.getValue();

        ValueServiceResponse<PageWrapper<TransferObject>> result = new ValueServiceResponse<>();       

        BusinessObjectCollectionJsonOutputStream streamOutputToInput = instance.new BusinessObjectCollectionJsonOutputStream(
                objectMapper, transferObjectTargetClass);
        try {
            try {
                objectMapper.writeValue(streamOutputToInput, inboundWrapper.getContent());

            } finally {
                IOUtils.closeQuietly(streamOutputToInput);
                List<TransferObject> transferObjects = streamOutputToInput.getResult();
                
                PageWrapper<TransferObject> pageWrapper = new PageWrapper<>();
                pageWrapper.setContent(transferObjects);
                pageWrapper.setFirst(inboundWrapper.isFirst());
                pageWrapper.setLast(inboundWrapper.isLast());
                pageWrapper.setItemsPerPage(inboundWrapper.getItemsPerPage());
                pageWrapper.setNumberOfElements(inboundWrapper.getNumberOfElements());
                pageWrapper.setStartPage(inboundWrapper.getStartPage());
                pageWrapper.setTotalPages(inboundWrapper.getTotalPages());
                pageWrapper.setTotalResults(inboundWrapper.getTotalResults());
                
                result.setValue(pageWrapper);
                
            }
        } catch (Exception e) {
            throw new UnrecoverableException("Could not convert list of BusinessObjects to a list of TransferObjects!",
                    e);
        }

        return result;

    }

    /**
     * Finds the BusinessObject class corresponding to a given TransferObject class.
     * 
     * @param transferObjectClass
     *            the specific Transfer object class (e.g., foo.bar.SimpleDomain)
     * @return the specific BusinessObject class (e.g., foo.bar.SimpleDomainBO)
     */
    public static Class<?> lookupBusinessObjectClassByTransferObject(
            Class<? extends TransferObject> transferObjectClass) {
        String className = transferObjectClass.getName();
        String boVarient = replaceLast(".transfer.", ".bizobj.", className) + "BO";
        return lookupClassByName(boVarient);
    }

    private static String replaceLast(String target, String replace, String original) {
        int lastIndex = original.lastIndexOf(target);

        if (lastIndex == -1) {
            return original;
        }

        String beginString = original.substring(0, lastIndex);
        String endString = original.substring(lastIndex + target.length());

        return beginString + replace + endString;
    }

    /**
     * Normal Class.forName(), but with implicit exception handling for cleaner overall code in this and related
     * classes.
     * 
     * @param className
     *            fully qualified class name to lookup
     * @return the Class for that lookup value (or an UnrecoverableException, if not found)
     */
    public static Class<?> lookupClassByName(String className) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);

        } catch (ClassNotFoundException e) {
            // do nothing - this is a convenience method.  If you can about it not being found, then you can deal that yourself 
            // when this returns null
        }
        
        return clazz;
    }

    /**
     * Looks up a ObjectMapperManager via reflection to avoid compile-time coupling between client and domain modules.
     * 
     * @param objectMapperManagerClassName
     *            fully qualified class name of the ObjectMapperManager to lookup
     * @return the resulting instance or an UnrecoverableException, if not found
     */
    public static ObjectMapper getObjectMapper(String objectMapperManagerClassName) {
        if (objectMapper == null) {
            Class<?> objectMapperManagerClass = lookupClassByName(objectMapperManagerClassName);
            
            if (objectMapperManagerClass == null) {
                throw new UnrecoverableException("Could not find Object Mapper Manager class " + objectMapperManagerClassName);
            }
            
            try {
                Method method = objectMapperManagerClass.getMethod("getObjectMapper");
                objectMapper = (ObjectMapper) method.invoke(null);

            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                throw new UnrecoverableException("Could not access " + objectMapperManagerClassName, e);
            }
        }

        return objectMapper;
    }

    /**
     * Converts an OutputStream into an InputStream via an underlying Pipes implementation to prevent loading the entire
     * object into memory for a TransferObject -> BusinessObject transformation. See EasyStream documentation for more
     * (somewhat confusing) information (but better than doing it all by hand).
     * 
     * Bottom line - this takes an InputStream of JSON representing a specific TransferObject and streams that into a
     * specific BusinessObject. Super obvious, right?
     */
    public class TransferObjectJsonOutputStream extends OutputStreamToInputStream<Object> {

        private ObjectMapper objectMapper;
        private Class<? extends TransferObject> transferObjectClass;

        /**
         * New instance.
         * 
         * @param objectMapper
         *            ObjectMapper to perform the transformation
         * @param transferObjectClass
         *            the specific TransferObject class being transformed
         */
        public TransferObjectJsonOutputStream(ObjectMapper objectMapper,
                Class<? extends TransferObject> transferObjectClass) {
            this.objectMapper = objectMapper;
            this.transferObjectClass = transferObjectClass;
        }

        @Override
        protected Object doRead(InputStream istream) throws Exception {
            Class<?> targetClass = lookupBusinessObjectClassByTransferObject(transferObjectClass);
            
            if (targetClass == null) {
                throw new UnrecoverableException("Can not find TransferObejct class to map to: " + transferObjectClass);
            }
            
            return objectMapper.readValue(istream, targetClass);
        }

    }

    /**
     * Converts an OutputStream into an InputStream via an underlying Pipes implementation to prevent loading the entire
     * object into memory for a ValueServiceResponse<BusinessObject or List<BusinessObject> ->
     * ValueServiceResponse<TransferObject or List<TransferObject> transformation. See EasyStream documentation for more
     * (somewhat confusing) information (but better than doing it all by hand).
     * 
     * Bottom line - this takes an InputStream of JSON representing a specific ValueServiceResponse with
     * BusinessObject(s) and streams that into a specific ValueServiceResponse with TransferObject(s). Super obvious,
     * right?
     */
    public class BusinessObjectJsonOutputStream
            extends OutputStreamToInputStream<ValueServiceResponse<TransferObject>> {

        private ObjectMapper objectMapper;
        private Class<?> transferObjectClass;

        /**
         * New instance.
         * 
         * @param objectMapper
         *            ObjectMapper to perform the transformation
         * @param transferObjectClass
         *            the specific TransferObject class being transformed
         */
        public BusinessObjectJsonOutputStream(ObjectMapper objectMapper, Class<?> transferObjectClass) {
            this.objectMapper = objectMapper;
            this.transferObjectClass = transferObjectClass;
        }

        @Override
        protected ValueServiceResponse<TransferObject> doRead(InputStream istream) throws Exception {
            TransferObject transferObject = (TransferObject) objectMapper.readValue(istream, transferObjectClass);
            return new ValueServiceResponse<>(transferObject);
        }

    }

    /**
     * Converts an OutputStream into an InputStream via an underlying Pipes implementation to prevent loading the entire
     * object into memory for a ValueServiceResponse<BusinessObject or List<BusinessObject> ->
     * ValueServiceResponse<TransferObject or List<TransferObject> transformation. See EasyStream documentation for more
     * (somewhat confusing) information (but better than doing it all by hand).
     * 
     * Bottom line - this takes an InputStream of JSON representing a specific ValueServiceResponse with
     * BusinessObject(s) and streams that into a specific ValueServiceResponse with TransferObject(s). Super obvious,
     * right?
     */
    public class BusinessObjectCollectionJsonOutputStream
            extends OutputStreamToInputStream<List<TransferObject>> {

        private ObjectMapper objectMapper;
        private Class<?> transferObjectClass;

        /**
         * New instance.
         * 
         * @param objectMapper
         *            ObjectMapper to perform the transformation
         * @param transferObjectClass
         *            the specific TransferObject class being transformed
         */
        public BusinessObjectCollectionJsonOutputStream(ObjectMapper objectMapper, Class<?> transferObjectClass) {
            this.objectMapper = objectMapper;
            this.transferObjectClass = transferObjectClass;
        }

        @Override
        protected List<TransferObject> doRead(InputStream istream) throws Exception {
            JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, transferObjectClass);
            return objectMapper.readValue(istream, type);
        }

    } 

}
