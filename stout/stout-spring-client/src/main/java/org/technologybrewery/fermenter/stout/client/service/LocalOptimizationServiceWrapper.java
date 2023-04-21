package org.technologybrewery.fermenter.stout.client.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.technologybrewery.fermenter.stout.exception.UnrecoverableException;
import org.technologybrewery.fermenter.stout.util.SpringApplicationContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * Wraps a "locally optimized" service call to support reflection and reflection caching. This allows references to
 * service implementation via runtime information so that we don't have a hard, compile-time binding between the client
 * and domain modules.
 */
public class LocalOptimizationServiceWrapper {

    private static final Logger logger = LoggerFactory.getLogger(LocalOptimizationServiceWrapper.class);

    private Class<?> serviceClass;
    private Map<String, Method> methodsByName = new ConcurrentHashMap<>();

    /**
     * New instance that will wrap and cache the reflection methods for the given class name, if the class is found in
     * the classpath.
     * 
     * @param serviceClassName
     *            fully qualified name of the class
     */
    public LocalOptimizationServiceWrapper(String serviceClassName) {
        if (serviceClassName == null) {
            throw new UnrecoverableException("Cannot optimize a null class!");
        }

        this.serviceClass = LocalOptimizationUtil.lookupClassByName(serviceClassName);
        
        if (this.serviceClass == null) {
            throw new UnrecoverableException("Clazz " + serviceClassName + "could not be found in classpath!");
        }
        
        
        indexClassForLocalOptimization();
    }

    /**
     * Because we bind our services to RESTful endpoints where the service name is used in the URL, each method name is
     * unique. You can't overload a URL with this structure. This understanding forms the basis for how this class
     * works.
     * 
     * We pull all public methods off and use them to look up parameter types dynamically. This is needed because you
     * cannot dynamically create a runtime representation of a service "many parameter" (e.g., List<SomeEntity>).
     * Instead, we use the method name and then pull this parameter type off the method. In doing so, we can avoid
     * having any compile-time reference to a BusinessObject, allowing the deployment and module structure for local
     * optimization to be much more straightforward.
     * 
     * This also provide inherent caching of reflection lookups.
     */
    private void indexClassForLocalOptimization() {
        Method[] methods = serviceClass.getMethods();

        for (Method method : methods) {
            methodsByName.put(method.getName(), method);
        }

    }

    /**
     * Invokes the wrapped service with the passed method name. See comment for indexClassForLocalOptimization for more
     * detail about how we determine parameter types.
     * 
     * @param methodName
     *            name of the method to invoke
     * @param params
     *            the values of the parameters to pass to the method
     * @return the result of the method
     */
    public Object invokeLocalService(String methodName, Object[] params) {
        Object response = null;

        ApplicationContext applicationContext = SpringApplicationContextHolder.getContext();
        String[] beans = applicationContext.getBeanNamesForType(serviceClass);
        if (beans != null && beans.length == 1) {
            Object localService = applicationContext.getBean(beans[0]);
            Method method = methodsByName.get(methodName);

            try {
                response = method.invoke(localService, params);

            } catch (SecurityException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {                
                String message = String.format("Could not invoke: %s.%s with the parameters types %s", serviceClass.getName(), methodName,
                        method.getParameterTypes());
                throw new UnrecoverableException(message, e);
                
            }
        }

        return response;
    }

    /**
     * Invokes the wrapped service with the passed method name and parameter types.
     * 
     * @param methodName
     *            name of the method to invoke
     * @param paramTypes
     *            the types of the parameters to pass to the method
     * @param params
     *            the values of the parameters to pass to the method
     * @return the result of the method
     */
    public Object invokeLocalService(String methodName, Class<?>[] parameterTypes, Object[] params) {
        Object response = null;

        ApplicationContext applicationContext = SpringApplicationContextHolder.getContext();
        String[] beans = applicationContext.getBeanNamesForType(serviceClass);
        if (beans != null && beans.length == 1) {
            Object localService = applicationContext.getBean(beans[0]);

            try {
                Method method = serviceClass.getMethod(methodName, parameterTypes);
                response = method.invoke(localService, params);

            } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                    | NoSuchMethodException e) {
                logger.error("Could not invoke: {}.{} with the parameters types {}", serviceClass.getName(), methodName,
                        parameterTypes);
                throw new UnrecoverableException("Could not invoke expected method (" + methodName + ")!", e);
            }
        }

        return response;
    }

}
