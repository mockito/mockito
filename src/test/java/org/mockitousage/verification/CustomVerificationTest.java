/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.invocation.Invocation;
import org.mockito.verification.VerificationMode;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;

public class CustomVerificationTest extends TestBase {

    @Mock IMethods mock;

    @Test
    public void custom_verification_with_old_api() {
        //given:
        mock.simpleMethod("a", 10);

        //expect:
        verify(mock, ignoreParametersUsingOldApi()).simpleMethod();

        try {
            verify(mock, ignoreParametersUsingOldApi()).otherMethod();
            fail();
        } catch (MockitoAssertionError e) {}
    }

    //Old api still supported, see https://github.com/mockito/mockito/issues/730
    private VerificationMode ignoreParametersUsingOldApi() {
        return new VerificationMode() {
            public void verify(VerificationData data) {
                //use old api
                InvocationMatcher target = data.getWanted();

                //sanity check the new api
                if (data.getTarget() != target) {
                    throw new RuntimeException("Sanity check");
                }

                //look for the relevant invocation and exit if found
                for (Invocation invocation : data.getAllInvocations()) {
                    if (target.getInvocation().getMethod().getName().equals(invocation.getMethod().getName())) {
                        return;
                    }
                }

                //verification failed!
                throw new MockitoAssertionError("Expected method with name: " + target + " not found in:\n" + data.getAllInvocations());
            }
            public VerificationMode description(String description) {
                return this;
            }
        };
    }
}
