package org.mockito.configuration;

//TODO javadocs
//TODO move to org.mockito for consistency
/**
 * Use it to configure Mockito.
 * <p>
 * To configure Mockito globally create exactly org.mockito.MockitoConfiguration class that implements this interface.
 * <p>
 * This is completely <b>optional</b> - nothing happens if there isn't any org.mockito.MockitoConfiguration class.
 */
public interface IMockitoConfiguration {

    ReturnValues getReturnValues();

}