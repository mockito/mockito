/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class DescriptionTest {

    @Mock
    private VerificationMode mockVerificationMode;

    @Mock
    private VerificationData mockVerificationData;

    @Before
    public void setUp() {
        initMocks(this);
    }

    /**
     * Test of verify method, of class Description. This test validates that the custom message is prepended to the
     * error message when verification fails.
     */
    @Test
    public void verification_failure_should_prepend_expected_message() {
        String failureMessage = "message should be prepended to the original message";
        String exceptionMessage = "original error message";
        String expectedResult = failureMessage + "\n" + exceptionMessage;
        MockitoAssertionError error = new MockitoAssertionError(exceptionMessage);
        doThrow(error).when(mockVerificationMode).verify(mockVerificationData);

        Description instance = new Description(mockVerificationMode, failureMessage);

        try {
            instance.verify(mockVerificationData);
            verify(mockVerificationMode).verify(mockVerificationData);
            fail("Should not have made it this far");

        } catch (MockitoAssertionError e) {
            assertEquals(expectedResult, e.getMessage());
        }
    }
}
