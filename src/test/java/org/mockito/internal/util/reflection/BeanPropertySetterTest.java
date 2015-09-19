/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.UUID;

import static org.junit.Assert.*;


public class BeanPropertySetterTest {

    @Test
    public void use_the_correct_setter_on_the_target() throws Exception {
        // given
        SomeBean someBean = new SomeBean();
        Field theField = someBean.getClass().getDeclaredField("theField");
        File valueToInject = new File("path");

        // when
        boolean injected = new BeanPropertySetter(someBean, theField, true).set(valueToInject);

        // then
        assertTrue(injected);
        assertTrue(someBean.theFieldSetterWasUsed);
        assertSame(valueToInject, someBean.getTheField());
    }

    @Test
    public void use_the_setter_on_the_target_when_field_name_begins_by_at_least_2_caps() throws Exception {
        // given
        BeanWithWeirdFields someBean = new BeanWithWeirdFields();
        Field theField = someBean.getClass().getDeclaredField("UUID");
        UUID valueToInject = new UUID(0L, 0L);

        // when
        boolean injected = new BeanPropertySetter(someBean, theField, true).set(valueToInject);

        // then
        assertTrue(injected);
        assertTrue(someBean.theFieldSetterWasUSed);
        assertSame(valueToInject, someBean.UUID);
    }

    @Test
    public void should_not_fail_if_bean_class_declares_only_the_setter_for_the_property() throws Exception {
        // given
        SomeBeanWithJustASetter someBean = new SomeBeanWithJustASetter();
        Field theField = someBean.getClass().getDeclaredField("theField");
        File valueToInject = new File("path");

        // when
        boolean injected = new BeanPropertySetter(someBean, theField, true).set(valueToInject);

        // then
        assertTrue(injected);
        assertTrue(someBean.theFieldSetterWasUsed);
    }

    @Test
    public void should_fail_if_matching_setter_cannot_be_found_and_if_report_failure_is_true() throws Exception {
        // given
        SomeBeanWithNoSetterMatchingFieldType bean = new SomeBeanWithNoSetterMatchingFieldType();
        Field theField = bean.getClass().getDeclaredField("theField");
        File valueToInject = new File("path");

        try {
            // when
            new BeanPropertySetter(bean, theField, true).set(valueToInject);
            fail();
        } catch (Exception e) {
            // then
            Assertions.assertThat(e.getMessage()).contains("setter not found");
        }
    }

    @Test
    public void return_false_if_no_setter_was_found() throws Exception {
        // given
        SomeBeanWithJustAGetter bean = new SomeBeanWithJustAGetter();
        Field theField = bean.getClass().getDeclaredField("theField");
        File valueToInject = new File("path");

        // when
        boolean injected = new BeanPropertySetter(bean, theField).set(valueToInject);

        // then
        assertFalse(injected);
    }

    @Test
    public void return_false_if_no_setter_was_found_and_if_reportNoSetterFound_is_false() throws Exception {
        // given
        SomeBeanWithNoSetterMatchingFieldType bean = new SomeBeanWithNoSetterMatchingFieldType();
        Field theField = bean.getClass().getDeclaredField("theField");
        File valueToInject = new File("path");

        // when
        boolean injected = new BeanPropertySetter(bean, theField, false).set(valueToInject);

        // then
        assertFalse(injected);
    }

    static class SomeBean {
        private File theField;
        boolean theFieldSetterWasUsed;

        public void setTheField(final File theField) {
            theFieldSetterWasUsed = true;
            this.theField = theField;
        }

        public File getTheField() {
            return theField;
        }
    }

    static class SomeBeanWithJustASetter {
        private File theField;
        boolean theFieldSetterWasUsed;

        public void setTheField(final File theField) {
            theFieldSetterWasUsed = true;
            this.theField = theField;
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
        boolean theFieldSetterWasUsed;

        public void setTheField(final FileOutputStream somethingElse) {
            theFieldSetterWasUsed = true;
        }
    }

    static class BeanWithWeirdFields {
        private UUID UUID;
        boolean theFieldSetterWasUSed;

        public void setUUID(UUID UUID) {
            theFieldSetterWasUSed = true;
            this.UUID = UUID;
        }
    }

}
