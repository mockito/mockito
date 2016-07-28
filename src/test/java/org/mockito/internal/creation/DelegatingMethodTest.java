package org.mockito.internal.creation;

import org.junit.Before;
import org.junit.Test;
import org.mockitoutil.TestBase;

import java.lang.reflect.Method;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class DelegatingMethodTest extends TestBase {

    private Method someMethod, otherMethod;
    private DelegatingMethod delegatingMethod;

    @Before
    public void setup() throws Exception {
        someMethod = Something.class.getMethod("someMethod", Object.class);
        otherMethod = Something.class.getMethod("otherMethod", Object.class);
        delegatingMethod = new DelegatingMethod(someMethod);
    }

    @Test
    public void equals_should_return_false_when_not_equal() throws Exception {
        DelegatingMethod notEqual = new DelegatingMethod(otherMethod);
        assertFalse(delegatingMethod.equals(notEqual));
    }

    @Test
    public void equals_should_return_true_when_equal() throws Exception {
        DelegatingMethod equal = new DelegatingMethod(someMethod);
        assertTrue(delegatingMethod.equals(equal));
    }

    @Test
    public void equals_should_return_true_when_self() throws Exception {
        assertTrue(delegatingMethod.equals(delegatingMethod));
    }

    @Test
    public void equals_should_return_false_when_not_equal_to_method() throws Exception {
        assertFalse(delegatingMethod.equals(otherMethod));
    }

    @Test
    public void equals_should_return_true_when_equal_to_method() throws Exception {
        assertTrue(delegatingMethod.equals(someMethod));
    }

    private interface Something {

        Object someMethod(Object param);

        Object otherMethod(Object param);
    }
}