/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.annotation;

import org.fest.assertions.Assertions;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockitoutil.TestBase;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@SuppressWarnings({"unchecked", "unused"})
public class SpyAnnotationTest extends TestBase {

    @Spy final List spiedList = new ArrayList();

    @Spy NestedClassWithNoArgConstructor staticTypeWithNoArgConstructor;

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

    @Test
    public void shouldFailIfTypeIsAnInterface() throws Exception {
		class FailingSpy {
			@Spy private List spyTypeIsInterface;
		}

        try {
            MockitoAnnotations.initMocks(new FailingSpy());
            fail();
        } catch (Exception e) {
            Assertions.assertThat(e.getMessage()).contains("an interface");
        }
    }

    @Test
    public void shouldReportWhenNoArgConstructor() throws Exception {
		class FailingSpy {
	        @Spy
            NoValidConstructor noValidConstructor;
		}

        try {
            MockitoAnnotations.initMocks(new FailingSpy());
            fail();
        } catch (Exception e) {
            Assertions.assertThat(e.getMessage()).contains("default constructor");
        }
    }
    
    @Test
    public void shouldReportWhenConstructorThrows() throws Exception {
		class FailingSpy {
	        @Spy
            ThrowingConstructor throwingConstructor;
		}

        try {
            MockitoAnnotations.initMocks(new FailingSpy());
            fail();
        } catch (Exception e) {
            Assertions.assertThat(e.getMessage()).contains("raised an exception");
        }
    }

    @Test
    public void shouldFailIfTypeIsAbstract() throws Exception {
		class FailingSpy {
			@Spy private AbstractList spyTypeIsAbstract;
		}

        try {
            MockitoAnnotations.initMocks(new FailingSpy());
            fail();
        } catch (Exception e) {
            Assertions.assertThat(e.getMessage()).contains("abstract class");
        }
    }

    @Test
    public void shouldFailIfTypeIsInnerClass() throws Exception {
		class FailingSpy {
			@Spy private TheInnerClass spyTypeIsInner;
            class TheInnerClass { }
		}

        try {
            MockitoAnnotations.initMocks(new FailingSpy());
            fail();
        } catch (MockitoException e) {
            Assertions.assertThat(e.getMessage()).contains("inner class");
        }
    }

	@Test(expected = IndexOutOfBoundsException.class)
    public void shouldResetSpies() throws Exception {
        spiedList.get(10); // see shouldInitSpy
    }

    static class NestedClassWithoutDefinedConstructor { }

    static class NestedClassWithNoArgConstructor {
        NestedClassWithNoArgConstructor() { }
        NestedClassWithNoArgConstructor(String f) { }
    }

    static class NoValidConstructor {
        NoValidConstructor(String f) { }
    }

    static class ThrowingConstructor {
        ThrowingConstructor() { throw new RuntimeException("boo!"); }
    }
}
