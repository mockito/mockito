/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs.injection;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

// issue 229 : @Mock fields in super test class are not injected on @InjectMocks fields
public class ParentTestMockInjectionTest {

    @Test
    public void injectMocksShouldInjectMocksFromTestSuperClasses() {
        ImplicitTest it = new ImplicitTest();
        MockitoAnnotations.openMocks(it);

        assertNotNull(it.daoFromParent);
        assertNotNull(it.daoFromSub);
        assertNotNull(it.sut.daoFromParent);
        assertNotNull(it.sut.daoFromSub);
    }

    @Ignore
    public abstract static class BaseTest {
        @Mock protected DaoA daoFromParent;
    }

    @Ignore("JUnit test under test : don't test this!")
    public static class ImplicitTest extends BaseTest {
        @InjectMocks private TestedSystem sut = new TestedSystem();

        @Mock private DaoB daoFromSub;

        @Before
        public void setup() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        public void noNullPointerException() {
            sut.businessMethod();
        }
    }

    public static class TestedSystem {
        private DaoA daoFromParent;
        private DaoB daoFromSub;

        public void businessMethod() {
            daoFromParent.doQuery();
            daoFromSub.doQuery();
        }
    }

    public static class DaoA {
        public void doQuery() {}
    }

    public static class DaoB {
        public void doQuery() {}
    }
}
