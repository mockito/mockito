/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * Allows creating customized argument matchers.
 * <p>
 * See {@link Matchers}
 * 
 * Use one of the {@link Matchers#argThat}, {@link Matchers#intThat}, etc. methods and implement your own {@link CustomMatcher}, e.g:
 * 
 * <pre>
 *   class IsListOfTwoElements extends CustomMatcher&lt;List&gt; {
 *      public boolean matches(List list) {
 *          return list.size() == 2;
 *      }
 *   }
 *   
 *   List mock = mock(List.class);
 *   
 *   stub(mock.addAll(argThat(new IsListOfTwoElements()))).toReturn(true);
 *   
 *   mock.addAll(Arrays.asList("one", "two"));
 *   
 *   verify(mock).addAll(argThat(new IsListOfTwoElements()));
 * </pre>
 * 
 * Custom matchers are generally used very rarely. 
 * <p>
 * To keep it readable you may want to extract method, e.g:
 * <pre>
 *   stub(mock.addAll(argThat(new IsListOfTwoElements()))).toReturn(true);
 *   //becomes
 *   stub(mock.addAll(listOfTwoElements()).toReturn(true);
 * </pre>
 * 
 * @param <T>
 */
public abstract class CustomMatcher<T> extends BaseMatcher<T> {

    public abstract boolean matches(Object argument);
    
    /* 
     * Usually not necessary but you might want to override this method to provide specific argument description 
     * (useful when errors are reported). 
     */
    public void describeTo(Description d) {
        d.appendText("<custom argument matcher>");
    }
}