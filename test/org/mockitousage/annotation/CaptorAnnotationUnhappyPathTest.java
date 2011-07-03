/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.annotation;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoException;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class CaptorAnnotationUnhappyPathTest extends TestBase {
    
    @Captor List notACaptorField;

    @Before
    @Override
    public void init() {
        //we need to get rid of parent implementation this time
    }
    
    @Test
    public void shouldFailIfCaptorHasWrongType() throws Exception {
        try {
            //when
            MockitoAnnotations.initMocks(this);
            fail();
        } catch (MockitoException e) {
            //then
            assertContains("notACaptorField", e.getMessage());
            assertContains("wrong type", e.getMessage());
        }
    }
}