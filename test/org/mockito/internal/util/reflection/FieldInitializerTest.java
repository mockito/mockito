/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldInitializer.ConstructorArgumentResolver;



public class FieldInitializerTest {

    private final StaticClass alreadyInstantiated = new StaticClass();
    private StaticClass noConstructor;
    private StaticClassWithDefaultConstructor defaultConstructor;
    private StaticClassWithPrivateDefaultConstructor privateDefaultConstructor;
    private StaticClassWithoutDefaultConstructor noDefaultConstructor;
    private StaticClassThrowingExceptionDefaultConstructor throwingExDefaultConstructor;
    private AbstractStaticClass abstractType;
    private Interface interfaceType;
    private InnerClassType innerClassType;
    private final AbstractStaticClass instantiatedAbstractType = new ConcreteStaticClass();
    private final Interface instantiatedInterfaceType =  new ConcreteStaticClass();
    private final InnerClassType instantiatedInnerClassType = new InnerClassType();

    @Test
    public void should_keep_same_instance_if_field_initialized() throws Exception {
        final StaticClass backupInstance = alreadyInstantiated;
        final FieldInitializer fieldInitializer = new FieldInitializer(this, field("alreadyInstantiated"));
        final FieldInitializationReport report = fieldInitializer.initialize();

        assertSame(backupInstance, report.fieldInstance());
        assertFalse(report.fieldWasInitialized());
        assertFalse(report.fieldWasInitializedUsingContructorArgs());
    }

    @Test
    public void should_instantiate_field_when_type_has_no_constructor() throws Exception {
        final FieldInitializer fieldInitializer = new FieldInitializer(this, field("noConstructor"));
        final FieldInitializationReport report = fieldInitializer.initialize();

        assertNotNull(report.fieldInstance());
        assertTrue(report.fieldWasInitialized());
        assertFalse(report.fieldWasInitializedUsingContructorArgs());
    }

    @Test
    public void should_instantiate_field_with_default_constructor() throws Exception {
        final FieldInitializer fieldInitializer = new FieldInitializer(this, field("defaultConstructor"));
        final FieldInitializationReport report = fieldInitializer.initialize();

        assertNotNull(report.fieldInstance());
        assertTrue(report.fieldWasInitialized());
        assertFalse(report.fieldWasInitializedUsingContructorArgs());
    }

    @Test
    public void should_instantiate_field_with_private_default_constructor() throws Exception {
        final FieldInitializer fieldInitializer = new FieldInitializer(this, field("privateDefaultConstructor"));
        final FieldInitializationReport report = fieldInitializer.initialize();

        assertNotNull(report.fieldInstance());
        assertTrue(report.fieldWasInitialized());
        assertFalse(report.fieldWasInitializedUsingContructorArgs());
    }

    @Test(expected = MockitoException.class)
    public void should_fail_to_instantiate_field_if_no_default_constructor() throws Exception {
        final FieldInitializer fieldInitializer = new FieldInitializer(this, field("noDefaultConstructor"));
        fieldInitializer.initialize();
    }

    @Test
    public void should_fail_to_instantiate_field_if_default_constructor_throws_exception() throws Exception {
        final FieldInitializer fieldInitializer = new FieldInitializer(this, field("throwingExDefaultConstructor"));
        try {
            fieldInitializer.initialize();
            fail();
        } catch (final MockitoException e) {
            final InvocationTargetException ite = (InvocationTargetException) e.getCause();
            assertTrue(ite.getTargetException() instanceof NullPointerException);
            assertEquals("business logic failed", ite.getTargetException().getMessage());
        }
    }

    @Test(expected = MockitoException.class)
    public void should_fail_for_abstract_field() throws Exception {
        new FieldInitializer(this, field("abstractType"));
    }

    @Test
    public void should_not_fail_if_abstract_field_is_instantiated() throws Exception {
        new FieldInitializer(this, field("instantiatedAbstractType"));
    }

    @Test(expected = MockitoException.class)
    public void should_fail_for_interface_field() throws Exception {
        new FieldInitializer(this, field("interfaceType"));
    }

    @Test
    public void should_not_fail_if_interface_field_is_instantiated() throws Exception {
        new FieldInitializer(this, field("instantiatedInterfaceType"));
    }

    @Test(expected = MockitoException.class)
    public void should_fail_for_local_type_field() throws Exception {
        // when
        class LocalType { }

        class TheTestWithLocalType {
            @InjectMocks LocalType field;
        }

        final TheTestWithLocalType testWithLocalType = new TheTestWithLocalType();

        // when
        new FieldInitializer(testWithLocalType, testWithLocalType.getClass().getDeclaredField("field"));
    }

    @Test
    public void should_not_fail_if_local_type_field_is_instantiated() throws Exception {
        // when
        class LocalType { }

        class TheTestWithLocalType {
            @InjectMocks LocalType field = new LocalType();
        }

        final TheTestWithLocalType testWithLocalType = new TheTestWithLocalType();

        // when
        new FieldInitializer(testWithLocalType, testWithLocalType.getClass().getDeclaredField("field"));
    }

    @Test(expected = MockitoException.class)
    public void should_fail_for_inner_class_field() throws Exception {
        new FieldInitializer(this, field("innerClassType"));
    }

    @Test
    public void should_not_fail_if_inner_class_field_is_instantiated() throws Exception {
        new FieldInitializer(this, field("instantiatedInnerClassType"));
    }

    @Test
    public void can_instantiate_class_with_parameterized_constructor() throws Exception {
        final ConstructorArgumentResolver resolver = given(mock(ConstructorArgumentResolver.class).resolveTypeInstances(any(Class[].class)))
                        .willReturn(new Object[]{null}).getMock();

        new FieldInitializer(this, field("noDefaultConstructor"), resolver).initialize();

        assertNotNull(noDefaultConstructor);
    }

    private Field field(final String fieldName) throws NoSuchFieldException {
        return this.getClass().getDeclaredField(fieldName);
    }

    static class StaticClass {
    }

    static class StaticClassWithDefaultConstructor {
        StaticClassWithDefaultConstructor() { }
    }

    static class StaticClassWithPrivateDefaultConstructor {
        private StaticClassWithPrivateDefaultConstructor() { }
    }

    static class StaticClassWithoutDefaultConstructor {
        private StaticClassWithoutDefaultConstructor(final String param) { }
    }

    static class StaticClassThrowingExceptionDefaultConstructor {
        StaticClassThrowingExceptionDefaultConstructor() throws Exception {
            throw new NullPointerException("business logic failed");
        }
    }
    
    abstract static class AbstractStaticClass {
        public AbstractStaticClass() {}
    }

    interface Interface {

    }

    static class ConcreteStaticClass extends AbstractStaticClass implements Interface {
    }

    class InnerClassType {
        InnerClassType() { }
    }

}
