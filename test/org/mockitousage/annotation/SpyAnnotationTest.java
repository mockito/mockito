/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.annotation;

import org.fest.assertions.Assertions;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockitoutil.TestBase;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
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
    public void spyInterface() throws Exception {
		class WithSpy {
			@Spy List<String> list;
		}

		WithSpy withSpy = new WithSpy();
        MockitoAnnotations.initMocks(withSpy);
        assertEquals(0, withSpy.list.size());
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
        } catch (MockitoException e) {
            Assertions.assertThat(e.getMessage()).contains("Unable to create mock instance");
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
        } catch (MockitoException e) {
            Assertions.assertThat(e.getMessage()).contains("Unable to create mock instance");
        }
    }

    @Test
    public void spyAbstractClass() throws Exception {
		class SpyAbstractClass {
			@Spy AbstractList<String> list;
			
			List<String> asSingletonList(String s) {
				when(list.size()).thenReturn(1);
				when(list.get(0)).thenReturn(s);
				return list;
			}
		}
		SpyAbstractClass withSpy = new SpyAbstractClass();
        MockitoAnnotations.initMocks(withSpy);
        assertEquals(Arrays.asList("a"), withSpy.asSingletonList("a"));
    }

    @Test
    public void spyInnerClass() throws Exception {
    	 
     class WithMockAndSpy {
    		@Spy private InnerStrength strength;
    		@Mock private List<String> list;

            abstract class InnerStrength {
            	private final String name;

            	InnerStrength() {
            		// Make sure that @Mock fields are always injected before @Spy fields.
            		assertNotNull(list);
            		// Make sure constructor is indeed called.
            		this.name = "inner";
            	}
            	
            	abstract String strength();
            	
            	String fullStrength() {
            		return name + " " + strength();
            	}
            }
    	}
		WithMockAndSpy outer = new WithMockAndSpy();
        MockitoAnnotations.initMocks(outer);
        when(outer.strength.strength()).thenReturn("strength");
        assertEquals("inner strength", outer.strength.fullStrength());
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
