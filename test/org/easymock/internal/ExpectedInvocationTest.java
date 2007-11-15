package org.easymock.internal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;

import org.junit.Test;

public class ExpectedInvocationTest {

    @Test
    public void shouldImplementHashcodeToBeHashMapsCitizen() throws Exception {
        Object[] args = new Object[] { "" };
        Method m = Object.class.getMethod("equals", new Class[] { Object.class });
        Invocation invocation = new Invocation(null, m, args);
        assertThat(new ExpectedInvocation(invocation, null).hashCode(), equalTo(1));
    }
}
