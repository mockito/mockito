/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import java.util.List;

import org.junit.Test;
import org.mockito.Mock;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class VerificationWithTimeoutTest extends TestBase {

    @Mock private List mock;
    
    @Test
    public void shouldVerify() throws Exception {
        mock.clear();
        
        //TODO: after 1.8 implement timeout
        //verify(mock, atLeastOnce().timeout(100)).clear();
    }
}