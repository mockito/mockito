/**
 * 
 */
package org.mockito;

import java.util.LinkedList;
import java.util.List;

import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;

/**
 * Use it to assert on 
 * <pre>
 *   Argument&lt;Person&gt; argument = new Argument&ltPerson&gt();
 *   verify(mock).sendTo(argument.capture());
 *   assertEquals("John", argument.value().getName());
 * </pre>
 */
public class Argument<T> extends ArgumentMatcher<T> {
    private LinkedList<Object> arguments = new LinkedList<Object>();

    public boolean matches(Object argument) {
        this.arguments.add(argument);
        return true;
    }

    public T capture() {
        Mockito.argThat(this);
        return null;
    }

    public T value() {
        if (arguments.isEmpty()) {
            new Reporter().argumentValueNotYetCaptured();
        } else {
            // TODO: after 1.7 nice instanceof check here?
            return (T) arguments.getLast();
        }
        return (T) arguments;
    }

    public List<T> allValues() {
        return (List) arguments;
    }
}