/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.annotation;

import static org.junit.Assert.assertTrue;
import static org.mockito.internal.util.MockUtil.isMock;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.internal.util.MockUtil;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class SpyAnnotationInitializedInBaseClassTest extends TestBase {

    class BaseClass {

        @Spy List list = new LinkedList();
    }

    class SubClass extends BaseClass {}

    @Test
    public void shouldInitSpiesInBaseClass() throws Exception {
        // given
        SubClass subClass = new SubClass();
        // when
        MockitoAnnotations.openMocks(subClass);
        // then
        assertTrue(MockUtil.isMock(subClass.list));
    }

    @Before
    @Override
    public void init() {
        // we need to get rid of parent implementation this time
    }

    @Before
    public void before() {
        MockitoAnnotations.openMocks(this);
    }

    @Spy List spyInBaseclass = new LinkedList();

    public static class SubTest extends SpyAnnotationInitializedInBaseClassTest {

        @Spy List spyInSubclass = new LinkedList();

        @Test
        public void shouldInitSpiesInHierarchy() throws Exception {
            assertTrue(isMock(spyInSubclass));
            assertTrue(isMock(spyInBaseclass));
        }
    }
}
