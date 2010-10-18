package org.mockito.internal.util.reflection;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.exceptions.base.MockitoException;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

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
    private AbstractStaticClass instantiatedAbstractType = new ConcreteStaticClass();
    private Interface instantiatedInterfaceType =  new ConcreteStaticClass();
    private InnerClassType instantiatedInnerClassType = new InnerClassType();

    @Test
    public void shouldKeepSameInstanceIfFieldInitialized() throws Exception {
        final StaticClass backupInstance = alreadyInstantiated;
        FieldInitializer fieldInitializer = new FieldInitializer(this, this.getClass().getDeclaredField("alreadyInstantiated"));
        assertSame(backupInstance, fieldInitializer.initialize());
    }

    @Test
    public void shouldInstantiateFieldWhenTypeHasNoConstructor() throws Exception {
        FieldInitializer fieldInitializer = new FieldInitializer(this, this.getClass().getDeclaredField("noConstructor"));
        assertNotNull(fieldInitializer.initialize());
    }

    @Test
    public void shouldInstantiateFieldWithDefaultConstructor() throws Exception {
        FieldInitializer fieldInitializer = new FieldInitializer(this, this.getClass().getDeclaredField("defaultConstructor"));
        assertNotNull(fieldInitializer.initialize());
    }

    @Test
    public void shouldInstantiateFieldWithPrivateDefaultConstructor() throws Exception {
        FieldInitializer fieldInitializer = new FieldInitializer(this, this.getClass().getDeclaredField("privateDefaultConstructor"));
        assertNotNull(fieldInitializer.initialize());
    }

    @Test(expected = MockitoException.class)
    public void shouldFailToInstantiateFieldIfNoDefaultConstructor() throws Exception {
        FieldInitializer fieldInitializer = new FieldInitializer(this, this.getClass().getDeclaredField("noDefaultConstructor"));
        fieldInitializer.initialize();
    }

    @Test
    public void shouldFailToInstantiateFieldIfDefaultConstructorThrowsException() throws Exception {
        FieldInitializer fieldInitializer = new FieldInitializer(this, this.getClass().getDeclaredField("throwingExDefaultConstructor"));
        try {
            fieldInitializer.initialize();
            fail();
        } catch (MockitoException e) {
            InvocationTargetException ite = (InvocationTargetException) e.getCause();
            assertTrue(ite.getTargetException() instanceof NullPointerException);
            assertEquals("business logic failed", ite.getTargetException().getMessage());
        }
    }

    @Test(expected = MockitoException.class)
    public void shouldFailForAbstractField() throws Exception {
        new FieldInitializer(this, this.getClass().getDeclaredField("abstractType"));
    }

    public void shouldNotFailIfAbstractFieldIsInstantiated() throws Exception {
        new FieldInitializer(this, this.getClass().getDeclaredField("instantiatedAbstractType"));
    }

    @Test(expected = MockitoException.class)
    public void shouldFailForInterfaceField() throws Exception {
        new FieldInitializer(this, this.getClass().getDeclaredField("interfaceType"));
    }

    public void shouldNotFailIfInterfaceFieldIsInstantiated() throws Exception {
        new FieldInitializer(this, this.getClass().getDeclaredField("instantiatedInterfaceType"));
    }

    @Test(expected = MockitoException.class)
    public void shouldFailForLocalTypeField() throws Exception {
        // when
        class LocalType { };

        class TheTestWithLocalType {
            @InjectMocks LocalType field;
        }

        TheTestWithLocalType testWithLocalType = new TheTestWithLocalType();

        // when
        new FieldInitializer(testWithLocalType, testWithLocalType.getClass().getDeclaredField("field"));
    }

    public void shouldNotFailIfLocalTypeFieldIsInstantiated() throws Exception {
        // when
        class LocalType { };

        class TheTestWithLocalType {
            @InjectMocks LocalType field = new LocalType();
        }

        TheTestWithLocalType testWithLocalType = new TheTestWithLocalType();

        // when
        new FieldInitializer(testWithLocalType, testWithLocalType.getClass().getDeclaredField("field"));
    }

    @Test(expected = MockitoException.class)
    public void shouldFailForInnerClassField() throws Exception {
        new FieldInitializer(this, this.getClass().getDeclaredField("innerClassType"));
    }

    public void shouldNotFailIfInnerClassFieldIsInstantiated() throws Exception {
        new FieldInitializer(this, this.getClass().getDeclaredField("instantiatedInnerClassType"));
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
        private StaticClassWithoutDefaultConstructor(String param) { }
    }

    static class StaticClassThrowingExceptionDefaultConstructor {
        StaticClassThrowingExceptionDefaultConstructor() throws Exception {
            throw new NullPointerException("business logic failed");
        }
    }
    
    static abstract class AbstractStaticClass {
        public AbstractStaticClass() {}
    }

    static interface Interface {

    }

    static class ConcreteStaticClass extends AbstractStaticClass implements Interface {
    }

    class InnerClassType {
        InnerClassType() { }
    }

}
