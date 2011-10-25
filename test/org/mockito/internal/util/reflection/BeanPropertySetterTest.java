/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import org.fest.assertions.Assertions;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;

import static org.junit.Assert.*;


public class BeanPropertySetterTest {

    @Test
    public void shouldUseTheSetterOnTheTargetWithTheFieldType() throws Exception {
        SomeBean someBean = new SomeBean();
        Field theField = someBean.getClass().getDeclaredField("theField");

        File valueToInject = new File("path");

        boolean injected = new BeanPropertySetter(someBean, theField, true).set(valueToInject);

        assertTrue(injected);
        assertTrue(someBean.setTheFieldWasUsed);
        assertSame(valueToInject, someBean.getTheField());
    }

    @Test
    public void shouldNotFailIfBeanHasOnlyASetter() throws Exception {
        SomeBeanWithJustASetter someBean = new SomeBeanWithJustASetter();
        Field theField = someBean.getClass().getDeclaredField("theField");

        File valueToInject = new File("path");

        boolean injected = new BeanPropertySetter(someBean, theField, true).set(valueToInject);

        assertTrue(injected);
        assertTrue(someBean.setTheFieldWasUsed);
    }

    @Test
    public void shouldFailIfMatchingSetterCannotBeFoundAndIfReportFailureTrue() throws Exception {
        SomeBeanWithNoSetterMatchingFieldType bean = new SomeBeanWithNoSetterMatchingFieldType();
        Field theField = bean.getClass().getDeclaredField("theField");

        File valueToInject = new File("path");

        try {
            new BeanPropertySetter(bean, theField, true).set(valueToInject);
            fail();
        } catch (Exception e) {
            Assertions.assertThat(e.getMessage()).contains("setter not found");
        }
    }

    @Test
    public void shouldReturnFalseIfNoSetterFound() throws Exception {
        SomeBeanWithJustAGetter bean = new SomeBeanWithJustAGetter();
        Field theField = bean.getClass().getDeclaredField("theField");

        File valueToInject = new File("path");

        boolean injected = new BeanPropertySetter(bean, theField).set(valueToInject);

        assertFalse(injected);
    }

    @Test
    public void shouldReturnFalseIfNoSetterWasFoundAndIfReportFailureFalse() throws Exception {
        SomeBeanWithNoSetterMatchingFieldType bean = new SomeBeanWithNoSetterMatchingFieldType();
        Field theField = bean.getClass().getDeclaredField("theField");

        File valueToInject = new File("path");

        boolean injected = new BeanPropertySetter(bean, theField, false).set(valueToInject);

        assertFalse(injected);
    }

    static class SomeBean {
        private File theField;
        boolean setTheFieldWasUsed;

        public void setTheField(final File theField) {
            setTheFieldWasUsed = true;
            this.theField = theField;
        }

        public File getTheField() {
            return theField;
        }
    }

    static class SomeBeanWithJustASetter {
        private File theField;
        boolean setTheFieldWasUsed;

        public void setTheField(final File theField) {
            this.theField = theField;
            setTheFieldWasUsed = true;
        }
    }
    static class SomeBeanWithJustAGetter {
        private File theField;

        public File getTheField() {
            return theField;
        }
    }

    static class SomeBeanWithNoSetterMatchingFieldType {
        private File theField;
        boolean setTheFieldWasUsed;

        public void setTheField(final FileOutputStream somethingElse) {
            setTheFieldWasUsed = true;
        }
    }

}
