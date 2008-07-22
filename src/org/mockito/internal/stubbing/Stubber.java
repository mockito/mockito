/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

/**
 * Allows to choose a method when stubbing in doThrow()|doAnswer()|doNothing()|doReturn() style
 * <p> 
 * Example:
 * <pre>
 *   doThrow(new RuntimeException()).
 *   when(mockedList).clear();
 *   
 *   //following throws RuntimeException:
 *   mockedList.clear();
 * </pre>
 * 
 * Read more about those methods:
 * <p>
 * {@link Mockito#doThrow(Throwable)}
 * <p>
 * {@link Mockito#doAnswer(Answer)}
 * <p>
 * {@link Mockito#doNothing()}
 * <p>
 * {@link Mockito#doReturn(Object)}
 * <p>
 * 
 * See examples in javadoc for {@link Mockito}
 */
@SuppressWarnings("unchecked")
public interface Stubber {

    /**
     * Allows to choose a method when stubbing in doThrow()|doAnswer()|doNothing()|doReturn() style
     * <p> 
     * Example:
     * <pre>
     *   doThrow(new RuntimeException()).
     *   when(mockedList).clear();
     *   
     *   //following throws RuntimeException:
     *   mockedList.clear();
     * </pre>
     * 
     * Read more about those methods:
     * <p>
     * {@link Mockito#doThrow(Throwable)}
     * <p>
     * {@link Mockito#doAnswer(Answer)}
     * <p>
     * {@link Mockito#doNothing()}
     * <p>
     * {@link Mockito#doReturn(Object)}
     * <p>
     * 
     *  See examples in javadoc for {@link Mockito}
     * 
     * @param mock
     * @return select method for stubbing
     */
    <T> T when(T mock);
    
    /**
     * Use it for stubbing consecutive calls in {@link Mockito#doReturn(Object)} style.
     * <p>
     * See javadoc for {@link Mockito#doReturn(Object)}
     * 
     * @param toBeReturned
     * @return stubber - to select a method for stubbing
     */
    Stubber doReturn(Object toBeReturned);
    
    /**
     * Use it for stubbing consecutive calls in {@link Mockito#doNothing()} style.
     * <p>
     * See javadoc for {@link Mockito#doNothing()}
     * 
     * @return stubber - to select a method for stubbing
     */
    Stubber doNothing();
    
    /**
     * Use it for stubbing consecutive calls in {@link Mockito#doThrow(Throwable)} style.
     * <p>
     * See javadoc for {@link Mockito#doThrow(Throwable)}
     * 
     * @param toBeThrown
     * @return stubber - to select a method for stubbing
     */
    Stubber doThrow(Throwable toBeThrown);
    
    /**
     * Use it for stubbing consecutive calls in {@link Mockito#doAnswer(Answer)} style.
     * <p>
     * See javadoc for {@link Mockito#doAnswer(Answer)}
     * 
     * @param answer
     * @return stubber - to select a method for stubbing
     */
    Stubber doAnswer(Answer answer);
}