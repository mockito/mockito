/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockitoutil.TestBase;

@SuppressWarnings("unused")
public class SpyAnnotationTest extends TestBase {

    @Spy final List<String> spiedList = new ArrayList<String>();

    @Spy InnerStaticClassWithNoArgConstructor staticTypeWithNoArgConstructor;

    @Spy InnerStaticClassWithoutDefinedConstructor staticTypeWithoutDefinedConstructor;

    @Spy MockTranslator translator;

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
    public void should_allow_spying_on_interfaces() throws Exception {
        class WithSpy {
            @Spy List<String> list;
        }

        WithSpy withSpy = new WithSpy();
        MockitoAnnotations.openMocks(withSpy);
        when(withSpy.list.size()).thenReturn(3);
        assertEquals(3, withSpy.list.size());
    }

    @Test
    public void should_allow_spying_on_interfaces_when_instance_is_concrete() throws Exception {
        class WithSpy {
            @Spy List<String> list = new LinkedList<String>();
        }
        WithSpy withSpy = new WithSpy();

        // when
        MockitoAnnotations.openMocks(withSpy);

        // then
        verify(withSpy.list, never()).clear();
    }

    @Test
    public void should_report_when_no_arg_less_constructor() throws Exception {
        class FailingSpy {
            @Spy NoValidConstructor noValidConstructor;
        }

        try {
            MockitoAnnotations.openMocks(new FailingSpy());
            fail();
        } catch (MockitoException e) {
            assertThat(e.getMessage())
                    .contains("Please ensure that the type")
                    .contains(NoValidConstructor.class.getSimpleName())
                    .contains("has a no-arg constructor");
        }
    }

    @Test
    public void should_report_when_constructor_is_explosive() throws Exception {
        class FailingSpy {
            @Spy ThrowingConstructor throwingConstructor;
        }

        try {
            MockitoAnnotations.openMocks(new FailingSpy());
            fail();
        } catch (MockitoException e) {
            assertThat(e.getMessage()).contains("Unable to create mock instance");
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
        MockitoAnnotations.openMocks(withSpy);
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
        MockitoAnnotations.openMocks(outer);
        when(outer.strength.strength()).thenReturn("strength");
        assertEquals("inner strength", outer.strength.fullStrength());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void should_reset_spy() throws Exception {
        spiedList.get(10); // see shouldInitSpy
    }

    @Test
    public void should_report_when_enclosing_instance_is_needed() throws Exception {
        class Outer {
            class Inner {}
        }
        class WithSpy {
            @Spy private Outer.Inner inner;
        }
        try {
            MockitoAnnotations.openMocks(new WithSpy());
            fail();
        } catch (MockitoException e) {
            assertThat(e).hasMessageContaining("@Spy annotation can only initialize inner classes");
        }
    }

    @Test
    public void should_report_private_inner_not_supported() throws Exception {
        try {
            MockitoAnnotations.openMocks(new WithInnerPrivate());
            fail();
        } catch (MockitoException e) {
            // Currently fails at instantiation time, because the mock subclass don't have the
            // 1-arg constructor expected for the outerclass.
            // org.mockito.internal.creation.instance.ConstructorInstantiator.withParams()
            assertThat(e)
                    .hasMessageContaining("Unable to initialize @Spy annotated field 'spy_field'")
                    .hasMessageContaining(WithInnerPrivate.InnerPrivate.class.getSimpleName());
        }
    }

    @Test
    public void should_report_private_abstract_inner_not_supported() throws Exception {
        try {
            MockitoAnnotations.openMocks(new WithInnerPrivateAbstract());
            fail();
        } catch (MockitoException e) {
            assertThat(e)
                    .hasMessageContaining(
                            "@Spy annotation can't initialize private abstract inner classes")
                    .hasMessageContaining(WithInnerPrivateAbstract.class.getSimpleName())
                    .hasMessageContaining(
                            WithInnerPrivateAbstract.InnerPrivateAbstract.class.getSimpleName())
                    .hasMessageContaining("You should augment the visibility of this inner class");
        }
    }

    @Test
    public void should_report_private_static_abstract_inner_not_supported() throws Exception {
        try {
            MockitoAnnotations.openMocks(new WithInnerPrivateStaticAbstract());
            fail();
        } catch (MockitoException e) {
            assertThat(e)
                    .hasMessageContaining(
                            "@Spy annotation can't initialize private abstract inner classes")
                    .hasMessageContaining(WithInnerPrivateStaticAbstract.class.getSimpleName())
                    .hasMessageContaining(
                            WithInnerPrivateStaticAbstract.InnerPrivateStaticAbstract.class
                                    .getSimpleName())
                    .hasMessageContaining("You should augment the visibility of this inner class");
        }
    }

    @Test
    public void should_be_able_to_stub_and_verify_via_varargs_for_list_params() throws Exception {
        // You can stub with varargs.
        when(translator.translate("hello", "mockito")).thenReturn(Arrays.asList("you", "too"));

        // Pretend the prod code will call translate(List<String>) with these elements.
        assertThat(translator.translate(Arrays.asList("hello", "mockito")))
                .containsExactly("you", "too");
        assertThat(translator.translate(Arrays.asList("not stubbed"))).isEmpty();

        // You can verify with varargs.
        verify(translator).translate("hello", "mockito");
    }

    @Test
    public void should_be_able_to_stub_and_verify_via_varargs_of_matchers_for_list_params()
            throws Exception {
        // You can stub with varargs of matchers.
        when(translator.translate(Mockito.anyString())).thenReturn(Arrays.asList("huh?"));
        when(translator.translate(eq("hello"))).thenReturn(Arrays.asList("hi"));

        // Pretend the prod code will call translate(List<String>) with these elements.
        assertThat(translator.translate(Arrays.asList("hello"))).containsExactly("hi");
        assertThat(translator.translate(Arrays.asList("not explicitly stubbed")))
                .containsExactly("huh?");

        // You can verify with varargs of matchers.
        verify(translator).translate(eq("hello"));
    }

    static class WithInnerPrivateStaticAbstract {
        @Spy private InnerPrivateStaticAbstract spy_field;

        private abstract static class InnerPrivateStaticAbstract {}
    }

    static class WithInnerPrivateAbstract {
        @Spy private InnerPrivateAbstract spy_field;

        public void some_method() {
            new InnerPrivateConcrete();
        }

        private abstract class InnerPrivateAbstract {}

        private class InnerPrivateConcrete extends InnerPrivateAbstract {}
    }

    static class WithInnerPrivate {
        @Spy private InnerPrivate spy_field;

        private class InnerPrivate {}

        private class InnerPrivateSub extends InnerPrivate {}
    }

    static class InnerStaticClassWithoutDefinedConstructor {}

    static class InnerStaticClassWithNoArgConstructor {
        InnerStaticClassWithNoArgConstructor() {}

        InnerStaticClassWithNoArgConstructor(String f) {}
    }

    static class NoValidConstructor {
        NoValidConstructor(String f) {}
    }

    static class ThrowingConstructor {
        ThrowingConstructor() {
            throw new RuntimeException("boo!");
        }
    }

    interface Translator {
        List<String> translate(List<String> messages);
    }

    abstract static class MockTranslator implements Translator {
        @Override
        public final List<String> translate(List<String> messages) {
            return translate(messages.toArray(new String[0]));
        }

        abstract List<String> translate(String... messages);
    }
}
