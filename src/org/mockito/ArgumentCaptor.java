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
 * <p>
 * See the full documentation on Mockito in javadoc for {@link Mockito} class.    
 */
@SuppressWarnings("unchecked")
public class ArgumentCaptor<T> extends ArgumentMatcher<T> {
    
    private LinkedList<Object> arguments = new LinkedList<Object>();

    /**
     * <b>Don't call this method directly.</b> It is used internally by the framework to store argument value. 
     */
    public boolean matches(Object argument) {
        this.arguments.add(argument);
        return true;
    }

    /**
     * Use it to capture the argument. This method <b>must be used inside of verification</b>.
     * <p>
     * Internally, this method registers a special implementation of an {@link ArgumentMatcher}.
     * This argument matcher stores the argument value so that you can use it later to perform assertions.  
     * <p>
     * See examples in javadoc for {@link ArgumentCaptor} class.
     * 
     * @return null
     */
    public T capture() {
        Mockito.argThat(this);
        return null;
    }

    /**
     * Returns the captured value of the argument
     * <p>
     * If the method was called multiple times then it returns the latest captured value
     * <p>
     * See examples in javadoc for {@link ArgumentCaptor} class.
     * 
     * @return captured argument value
     */
    public T getValue() {
        if (arguments.isEmpty()) {
            new Reporter().noArgumentValueWasCaptured();
        } else {
            return (T) arguments.getLast();
        }
        return (T) arguments;
    }

    /**
     * Returns the captured value of the argument
     * <p>
     * If the method was called multiple times then it returns the latest captured value
     * <p>
     * See examples in javadoc for {@link ArgumentCaptor} class.
     * 
     * @return captured argument value
     */
    public List<T> getAllValues() {
        return (List) arguments;
    }

    /**
     * <b>Don't call this method directly.</b> It is used internally by the framework to print friendly matcher description.
     */
    public void describeTo(Description description) {
        description.appendText("<Capturing argument>");
    }
}