/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.annotation;

import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockitoutil.TestBase;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;

@SuppressWarnings({"unchecked", "unused"})
public class SpyAnnotationTest extends TestBase {

    @Spy
    final List spiedList = new ArrayList();

    @Spy
    NestedClassWithNoArgConstructor staticTypeWithNoArgConstructor;

    @Spy
    NestedClassWithoutDefinedConstructor staticTypeWithoutDefinedConstructor;

	@Test
    public void shouldInitSpies() throws Exception {
        doReturn("foo").when(spiedList).get(10);

        assertEquals("foo", spiedList.get(10));
        assertTrue(spiedList.isEmpty());
    }

    @Test
    public void shouldInitSpyIfNestedStaticClass() throws Exception {
		assertNotNull(staticTypeWithNoArgConstructor);
		assertNotNull(staticTypeWithoutDefinedConstructor);
    }

    @Test(expected = MockitoException.class)
    public void shouldFailIfTypeIsAnInterface() throws Exception {
		class FailingSpy {
			@Spy private List spyTypeIsInterface;
		}

		MockitoAnnotations.initMocks(new FailingSpy());
    }

    @Test(expected = MockitoException.class)
    public void shouldFailIfTypeIsAbstract() throws Exception {
		class FailingSpy {
			@Spy private AbstractList spyTypeIsAbstract;
		}

		MockitoAnnotations.initMocks(new FailingSpy());
    }

    @Test(expected = MockitoException.class)
    public void shouldFailIfTypeIsInnerClass() throws Exception {
		class FailingSpy {
			@Spy private List spyTypeIsInner;
            class TheInnerClass { }
		}

		MockitoAnnotations.initMocks(new FailingSpy());
    }

	@Test(expected = IndexOutOfBoundsException.class)
    public void shouldResetSpies() throws Exception {
        spiedList.get(10); // see shouldInitSpy
    }

    static class NestedClassWithoutDefinedConstructor { }

    static class NestedClassWithNoArgConstructor {
        NestedClassWithNoArgConstructor() { }
        NestedClassWithNoArgConstructor(String _1arg) { }
    }
}
