/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.annotation;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockitoutil.TestBase;

import java.util.*;

import static junit.framework.TestCase.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SuppressWarnings({"unchecked", "unused"})
public class SpyAnnotationTest extends TestBase {

    @Spy final List<String> spiedList = new ArrayList<String>();

    @Spy NestedClassWithNoArgConstructor staticTypeWithNoArgConstructor;

    @Spy
    NestedClassWithoutDefinedConstructor staticTypeWithoutDefinedConstructor;
  
    @Rule public final ExpectedException shouldThrow = ExpectedException.none();

    @Test
    public void should_init_spy_by_instance() throws Exception {
        doReturn("foo").when(spiedList).get(10);
        assertEquals("foo", spiedList.get(10));
        assertTrue(spiedList.isEmpty());
    }

    @Test
    public void should_init_spy_and_automatically_create_instance() throws Exception {
        when(staticTypeWithNoArgConstructor.toString()).thenReturn("x");
        when(staticTypeWithoutDefinedConstructor.toString()).thenReturn("y");
        assertEquals("x", staticTypeWithNoArgConstructor.toString());
        assertEquals("y", staticTypeWithoutDefinedConstructor.toString());
    }

    @Test
    public void should_prevent_spying_on_interfaces() throws Exception {
        class WithSpy {
            @Spy List<String> list;
        }

        WithSpy withSpy = new WithSpy();
        try {
            MockitoAnnotations.initMocks(withSpy);
            fail();
        } catch (MockitoException e) {
            Assertions.assertThat(e.getMessage()).contains("is an interface and it cannot be spied on");
        }
    }

    @Test
    public void should_allow_spying_on_interfaces_when_instance_is_concrete() throws Exception {
        class WithSpy {
            @Spy List<String> list = new LinkedList<String>();
        }

        WithSpy withSpy = new WithSpy();
        //when
        MockitoAnnotations.initMocks(withSpy);

        //then
        verify(withSpy.list, never()).clear();
    }

    @Test
    public void should_report_when_no_arg_less_constructor() throws Exception {
        class FailingSpy {
            @Spy
            NoValidConstructor noValidConstructor;
        }

        try {
            MockitoAnnotations.initMocks(new FailingSpy());
            fail();
        } catch (MockitoException e) {
            Assertions.assertThat(e.getMessage()).contains("0-arg constructor");
        }
    }
    
    @Test
    public void should_report_when_constructor_is_explosive() throws Exception {
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
    public void should_spy_abstract_class() throws Exception {
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
    public void should_spy_inner_class() throws Exception {
         
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
    public void should_reset_spy() throws Exception {
        spiedList.get(10); // see shouldInitSpy
    }

    @Test
    public void should_report_when_encosing_instance_is_needed() throws Exception {
        class Outer {
            class Inner {}
        }
        class WithSpy {
            @Spy private Outer.Inner inner;
        }
        try {
            MockitoAnnotations.initMocks(new WithSpy());
            fail();
        } catch (MockitoException e) {
            assertThat(e).hasMessageContaining("@Spy annotation can only initialize inner classes");
        }
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
