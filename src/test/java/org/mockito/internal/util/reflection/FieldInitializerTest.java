/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.exceptions.base.MockitoException;

@SuppressWarnings("unchecked")
public class FieldInitializerTest {

    private StaticClass alreadyInstantiated = new StaticClass();
    private StaticClass noConstructor;
    private StaticClassWithDefaultConstructor defaultConstructor;
    private StaticClassWithPrivateDefaultConstructor privateDefaultConstructor;
    private StaticClassWithoutDefaultConstructor noDefaultConstructor;
    private StaticClassThrowingExceptionDefaultConstructor throwingExDefaultConstructor;
    private AbstractStaticClass abstractType;
    private Interface interfaceType;
    private InnerClassType innerClassType;
    private EnumType enumType;
    private AbstractStaticClass instantiatedAbstractType = new ConcreteStaticClass();
    private Interface instantiatedInterfaceType = new ConcreteStaticClass();
    private InnerClassType instantiatedInnerClassType = new InnerClassType();
    private EnumType initializedEnumType = EnumType.INITIALIZED;

    @Test
    public void should_keep_same_instance_if_field_initialized() throws Exception {
        final StaticClass backupInstance = alreadyInstantiated;
        final ConstructorResolver resolver = mock(ConstructorResolver.class);

        FieldInitializer fieldInitializer =
                new FieldInitializer(this, field("alreadyInstantiated"), resolver);
        FieldInitializationReport report = fieldInitializer.initialize();

        assertSame(backupInstance, report.fieldInstance());
        assertFalse(report.fieldWasInitialized());
    }

    @Test
    public void should_instantiate_field_when_type_has_no_constructor() throws Exception {
        final ConstructorResolver resolver = createNoArgsResolverMock(StaticClass.class);
        FieldInitializer fieldInitializer =
                new FieldInitializer(this, field("noConstructor"), resolver);
        FieldInitializationReport report = fieldInitializer.initialize();

        assertNotNull(report.fieldInstance());
        assertTrue(report.fieldWasInitialized());
    }

    @Test
    public void should_instantiate_field_with_default_constructor() throws Exception {
        final ConstructorResolver resolver =
                createNoArgsResolverMock(StaticClassWithDefaultConstructor.class);
        FieldInitializer fieldInitializer =
                new FieldInitializer(this, field("defaultConstructor"), resolver);
        FieldInitializationReport report = fieldInitializer.initialize();

        assertNotNull(report.fieldInstance());
        assertTrue(report.fieldWasInitialized());
    }

    @Test
    public void should_instantiate_field_with_private_default_constructor() throws Exception {
        final ConstructorResolver resolver =
                createNoArgsResolverMock(StaticClassWithPrivateDefaultConstructor.class);
        FieldInitializer fieldInitializer =
                new FieldInitializer(this, field("privateDefaultConstructor"), resolver);
        FieldInitializationReport report = fieldInitializer.initialize();

        assertNotNull(report.fieldInstance());
        assertTrue(report.fieldWasInitialized());
    }

    @Test
    public void should_not_instantiate_field_if_field_is_not_resolvable() throws Exception {
        final ConstructorResolver resolver = mock(ConstructorResolver.class);
        given(resolver.isResolvable()).willReturn(false);

        FieldInitializer fieldInitializer =
                new FieldInitializer(this, field("noDefaultConstructor"), resolver);
        FieldInitializationReport report = fieldInitializer.initialize();

        assertThat(report).isNotNull();
        assertThat(report.fieldIsInitialized()).isFalse();
        assertThat(report.fieldInstance()).isNull();
        assertThat(report.fieldWasInitialized()).isFalse();
    }

    @Test
    public void should_fail_to_instantiate_field_if_default_constructor_throws_exception()
            throws Exception {
        final ConstructorResolver resolver =
                createNoArgsResolverMock(StaticClassThrowingExceptionDefaultConstructor.class);
        FieldInitializer fieldInitializer =
                new FieldInitializer(this, field("throwingExDefaultConstructor"), resolver);
        try {
            fieldInitializer.initialize();
            fail();
        } catch (MockitoException e) {
            InvocationTargetException ite = (InvocationTargetException) e.getCause();
            assertTrue(ite.getTargetException() instanceof NullPointerException);
            assertEquals("business logic failed", ite.getTargetException().getMessage());
        }
    }

    @Test
    public void should_fail_to_instantiate_field_if_resolver_throws_exception() throws Exception {
        final ConstructorResolver resolver = mock(ConstructorResolver.class);
        given(resolver.isResolvable()).willThrow(new MockitoException("resolver fails"));

        try {
            new FieldInitializer(this, field("noDefaultConstructor"), resolver).initialize();
            fail();
        } catch (MockitoException e) {
            assertThat(e.getMessage()).contains("noDefaultConstructor").contains("resolver fails");
        }
    }

    @Test
    public void should_fail_if_an_argument_type_does_not_match_wanted_type() throws Exception {
        final ConstructorResolver resolver = createParameterizedResolverMock();
        given(resolver.resolveArguments()).willReturn(new Object[] {new HashSet<String>()});

        try {
            new FieldInitializer(this, field("noDefaultConstructor"), resolver).initialize();
            fail();
        } catch (MockitoException e) {
            assertThat(e.getMessage()).contains("ConstructorResolver").contains("incorrect types");
        }
    }

    @Test(expected = MockitoException.class)
    public void should_fail_for_abstract_field() throws Exception {
        final ConstructorResolver resolver = mock(ConstructorResolver.class);
        new FieldInitializer(this, field("abstractType"), resolver);
    }

    @Test
    public void should_not_fail_if_abstract_field_is_instantiated() throws Exception {
        final ConstructorResolver resolver = mock(ConstructorResolver.class);
        new FieldInitializer(this, field("instantiatedAbstractType"), resolver);
    }

    @Test(expected = MockitoException.class)
    public void should_fail_for_interface_field() throws Exception {
        final ConstructorResolver resolver = mock(ConstructorResolver.class);
        new FieldInitializer(this, field("interfaceType"), resolver);
    }

    @Test
    public void should_not_fail_if_interface_field_is_instantiated() throws Exception {
        final ConstructorResolver resolver = mock(ConstructorResolver.class);
        new FieldInitializer(this, field("instantiatedInterfaceType"), resolver);
    }

    @Test(expected = MockitoException.class)
    public void should_fail_for_local_type_field() throws Exception {
        // when
        class LocalType {}

        class TheTestWithLocalType {
            @InjectMocks LocalType field;
        }

        TheTestWithLocalType testWithLocalType = new TheTestWithLocalType();

        // when
        final ConstructorResolver resolver = mock(ConstructorResolver.class);
        new FieldInitializer(
                testWithLocalType,
                testWithLocalType.getClass().getDeclaredField("field"),
                resolver);
    }

    @Test
    public void should_not_fail_if_local_type_field_is_instantiated() throws Exception {
        // when
        class LocalType {}

        class TheTestWithLocalType {
            @InjectMocks LocalType field = new LocalType();
        }

        TheTestWithLocalType testWithLocalType = new TheTestWithLocalType();

        // when
        final ConstructorResolver resolver = mock(ConstructorResolver.class);
        new FieldInitializer(
                testWithLocalType,
                testWithLocalType.getClass().getDeclaredField("field"),
                resolver);
    }

    @Test(expected = MockitoException.class)
    public void should_fail_for_inner_class_field() throws Exception {
        final ConstructorResolver resolver = mock(ConstructorResolver.class);
        new FieldInitializer(this, field("innerClassType"), resolver);
    }

    @Test
    public void should_not_fail_if_inner_class_field_is_instantiated() throws Exception {
        final ConstructorResolver resolver = mock(ConstructorResolver.class);
        new FieldInitializer(this, field("instantiatedInnerClassType"), resolver);
    }

    @Test(expected = MockitoException.class)
    public void should_fail_for_enum_field() throws Exception {
        final ConstructorResolver resolver = mock(ConstructorResolver.class);
        new FieldInitializer(this, field("enumType"), resolver);
    }

    @Test
    public void should_not_fail_if_enum_field_is_instantiated() throws Exception {
        final ConstructorResolver resolver = mock(ConstructorResolver.class);
        new FieldInitializer(this, field("initializedEnumType"), resolver);
    }

    @Test
    public void can_instantiate_class_with_parameterized_constructor() throws Exception {
        final ConstructorResolver resolver = createParameterizedResolverMock();
        new FieldInitializer(this, field("noDefaultConstructor"), resolver).initialize();

        assertNotNull(noDefaultConstructor);
    }

    private Field field(String fieldName) throws NoSuchFieldException {
        return this.getClass().getDeclaredField(fieldName);
    }

    private ConstructorResolver createNoArgsResolverMock(Class<?> type)
            throws NoSuchMethodException {
        final ConstructorResolver resolver = mock(ConstructorResolver.class);
        final Constructor constructor = type.getDeclaredConstructor();
        given(resolver.resolveConstructor()).willReturn(constructor);
        given(resolver.isResolvable()).willReturn(true);
        return resolver;
    }

    private ConstructorResolver createParameterizedResolverMock() throws NoSuchMethodException {
        final ConstructorResolver resolver = mock(ConstructorResolver.class);
        final Constructor constructor =
                StaticClassWithoutDefaultConstructor.class.getDeclaredConstructor(String.class);
        given(resolver.resolveConstructor()).willReturn(constructor);
        given(resolver.isResolvable()).willReturn(true);
        given(resolver.resolveArguments()).willReturn(new Object[] {null});
        return resolver;
    }

    static class StaticClass {}

    static class StaticClassWithDefaultConstructor {
        StaticClassWithDefaultConstructor() {}
    }

    static class StaticClassWithPrivateDefaultConstructor {
        private StaticClassWithPrivateDefaultConstructor() {}
    }

    static class StaticClassWithoutDefaultConstructor {
        private StaticClassWithoutDefaultConstructor(String param) {}
    }

    static class StaticClassThrowingExceptionDefaultConstructor {
        StaticClassThrowingExceptionDefaultConstructor() throws Exception {
            throw new NullPointerException("business logic failed");
        }
    }

    abstract static class AbstractStaticClass {
        public AbstractStaticClass() {}
    }

    interface Interface {}

    static class ConcreteStaticClass extends AbstractStaticClass implements Interface {}

    class InnerClassType {
        InnerClassType() {}
    }

    enum EnumType {
        INITIALIZED
    }
}
