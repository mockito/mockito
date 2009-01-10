package org.mockito;

/**
 * Use it to configure Mockito. For now there are not many configuration options but it may change in future.
 * <p>
 * Currently, configuring Mockito can help you changing {@link ReturnValues} for all mocks. ReturnValues determines the return values for unstubbed invocations.
 * <p>
 * To configure Mockito create exactly <b>org.mockito.MockitoConfiguration</b> class that implements this interface.
 * <p>
 * Configuring Mockito is completely <b>optional</b> - nothing happens if there isn't any <b>org.mockito.MockitoConfiguration</b> on the classpath. 
 * <p>
 * <b>org.mockito.MockitoConfiguration</b> must implement IMockitoConfiguration or extend {@link DefaultMockitoConfiguration}
 * <p>
 * Mockito will store single instance of org.mockito.MockitoConfiguration per thread (using ThreadLocal). 
 * For sanity of your tests, don't make the implementation stateful.   
 * 
 */
public interface IMockitoConfiguration {

    ReturnValues getReturnValues();

}