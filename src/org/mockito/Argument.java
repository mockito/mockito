/**
 * 
 */
package org.mockito;

import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Description;
import org.mockito.exceptions.Reporter;

/**
 * Use it to capture argument values for further assertions.
 * <p>
 * Mockito verifies argument values in typical java style: by using an equals() method.
 * This is also the recommended way of matching arguments because it makes tests clean & simple.
 * In some situations though, it is helpful to assert on certain arguments after the actual verification.
 * For example:
 * <pre>
 *   Argument&lt;Person&gt; argument = new Argument&ltPerson&gt();
 *   verify(mock).doSomething(argument.capture());
 *   assertEquals("John", argument.value().getName());
 * </pre>
 *
 * Warning: Usually, capturing arguments makes sense only with verification <b>but not</b> with stubbing.  
 */
@SuppressWarnings("unchecked")
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
            new Reporter().noArgumentValueWasCaptured();
        } else {
            return (T) arguments.getLast();
        }
        return (T) arguments;
    }

    public List<T> allValues() {
        return (List) arguments;
    }
    
    @Override
    public void describeTo(Description description) {
        description.appendText("<Capturing argument>");
    }
}