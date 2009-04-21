/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.misuse;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoTroubleshooter;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.exceptions.misusing.UnfinishedStubbingException;
import org.mockito.exceptions.misusing.UnfinishedVerificationException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class ExplicitFrameworkValidationTest extends TestBase {

    @Mock IMethods mock;
    
    @Test
    public void shouldValidateExplicitly() {
        verify(mock);
        try {
            MockitoTroubleshooter.validateFrameworkState();
            fail();
        } catch (UnfinishedVerificationException e) {}
    }
    
    @Test
    public void shouldDetectUnfinishedStubbing() {
        when(mock.simpleMethod());
        try {
            MockitoTroubleshooter.validateFrameworkState();
            fail();
        } catch (UnfinishedStubbingException e) {}
    }
    
    @Test
    public void shouldDetectMisplacedArgumentMatcher() {
        anyObject();
        try {
            MockitoTroubleshooter.validateFrameworkState();
            fail();
        } catch (InvalidUseOfMatchersException e) {}
    }
}