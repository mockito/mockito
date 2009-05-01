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
 *   ArgumentCaptor&lt;Person&gt; argument = new ArgumentCaptor&ltPerson&gt();
 *   verify(mock).doSomething(argument.capture());
 *   assertEquals("John", argument.getValue().getName());
 * </pre>
 *
 * Warning: it is recommended to use ArgumentCaptor with verification <b>but not</b> with stubbing.
 * Using ArgumentCaptor with stubbing may decrease test readability because captor is created outside of assert (aka verify or 'then') block.
 * Also it may reduce defect localization because if stubbed method was not called then no argument is captured.   
 */
@SuppressWarnings("unchecked")
public class ArgumentCaptor<T> extends ArgumentMatcher<T> {
    
    private LinkedList<Object> arguments = new LinkedList<Object>();

    public boolean matches(Object argument) {
        this.arguments.add(argument);
        return true;
    }

    public T capture() {
        Mockito.argThat(this);
        return null;
    }

    public T getValue() {
        if (arguments.isEmpty()) {
            new Reporter().noArgumentValueWasCaptured();
        } else {
            return (T) arguments.getLast();
        }
        return (T) arguments;
    }

    public List<T> getAllValues() {
        return (List) arguments;
    }
    
    @Override
    public void describeTo(Description description) {
        description.appendText("<Capturing argument>");
    }
}