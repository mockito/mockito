/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.description;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoAssertionError;

public class VerificationWithDescriptionTest {

    @Mock private List<?> mock;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void assertion_error_message_should_start_with_the_custom_specified_message() {

        String failureMessage = "Verification failed!";
        try {
            verify(mock, description(failureMessage)).clear();
            fail("Should not have made it this far");

        } catch (MockitoAssertionError e) {
            assertTrue(e.getMessage().startsWith(failureMessage));
        }
    }
}
