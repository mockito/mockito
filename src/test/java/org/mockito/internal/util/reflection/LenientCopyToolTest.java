/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import org.junit.Test;
import org.mockitoutil.TestBase;

import java.lang.reflect.Field;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class LenientCopyToolTest extends TestBase {

    //private LenientCopyTool tool = new LenientCopyTool();

    static class InheritMe {
        protected String protectedInherited = "protected";
        private String privateInherited = "private";
    }

    public static class SomeObject extends InheritMe {
        @SuppressWarnings("unused")
        // required because static fields needs to be excluded from copying
        private static int staticField = -100;
        private int privateField = -100;
        private transient int privateTransientField = -100;
        String defaultField = "-100";
        protected Object protectedField = new Object();
        public SomeOtherObject instancePublicField = new SomeOtherObject();
        final int finalField;

        public SomeObject(int finalField) {
            this.finalField = finalField;
        }
    }

    public static class SomeOtherObject {
    }

    private SomeObject from = new SomeObject(100);
    private SomeObject to = mock(SomeObject.class);

    @Test
    public void shouldShallowCopyBasicFinalField() throws Exception {
        // given
        assertEquals(100, from.finalField);
        assertThat(to.finalField).isNotEqualTo(100);

        // when
        LenientCopyTool.copyToMock(from, to);

        // then
        assertEquals(100, to.finalField);
    }

    @Test
    public void shouldShallowCopyTransientPrivateFields() throws Exception {
        // given
        from.privateTransientField = 1000;
        assertThat(to.privateTransientField).isNotEqualTo(1000);

        // when
        LenientCopyTool.copyToMock(from, to);

        // then
        assertEquals(1000, to.privateTransientField);
    }

    @Test
    public void shouldShallowCopyLinkedListIntoMock() throws Exception {
        // given
        LinkedList fromList = new LinkedList();
        LinkedList toList = mock(LinkedList.class);

        // when
        LenientCopyTool.copyToMock(fromList, toList);

        // then no exception is thrown
    }

    @Test
    public void shouldShallowCopyFieldValuesIntoMock() throws Exception {
        // given
        from.defaultField = "foo";
        from.instancePublicField = new SomeOtherObject();
        from.privateField = 1;
        from.privateTransientField = 2;
        from.protectedField = 3;

        assertThat(to.defaultField).isNotEqualTo(from.defaultField);
        assertThat(to.instancePublicField).isNotEqualTo(from.instancePublicField);
        assertThat(to.privateField).isNotEqualTo(from.privateField);
        assertThat(to.privateTransientField).isNotEqualTo(from.privateTransientField);
        assertThat(to.protectedField).isNotEqualTo(from.protectedField);

        // when
        LenientCopyTool.copyToMock(from, to);

        // then
        assertEquals(from.defaultField, to.defaultField);
        assertEquals(from.instancePublicField, to.instancePublicField);
        assertEquals(from.privateField, to.privateField);
        assertEquals(from.privateTransientField, to.privateTransientField);
        assertEquals(from.protectedField, to.protectedField);
    }

    @Test
    public void shouldCopyValuesOfInheritedFields() throws Exception {
        //given
        ((InheritMe) from).privateInherited = "foo";
        ((InheritMe) from).protectedInherited = "bar";

        assertThat(((InheritMe) to).privateInherited).isNotEqualTo(((InheritMe) from).privateInherited);

        //when
        LenientCopyTool.copyToMock(from, to);

        //then
        assertEquals(((InheritMe) from).privateInherited, ((InheritMe) to).privateInherited);
    }

    @Test
    public void shouldEnableAndThenDisableAccessibility() throws Exception {
        //given
        Field privateField = SomeObject.class.getDeclaredField("privateField");
        assertFalse(privateField.isAccessible());

        //when
        LenientCopyTool.copyToMock(from, to);

        //then
        privateField = SomeObject.class.getDeclaredField("privateField");
        assertFalse(privateField.isAccessible());
    }

    @Test
    public void shouldContinueEvenIfThereAreProblemsCopyingSingleFieldValue() throws Exception {
        //given
        FieldCopier hej = mock(FieldCopier.class);

        doNothing().
        doThrow(new IllegalAccessException()).
        doNothing().
        when(hej).
        copyValue(anyObject(), anyObject(), any(Field.class));

        //when
        LenientCopyTool.copyToMock(from, to);

        //then
        verify(hej, atLeast(3)).copyValue(any(), any(), any(Field.class));
    }


    @Test
    public void shouldBeAbleToCopyFromRealObjectToRealObject() throws Exception {

        // given
        from.defaultField = "defaultField";
        from.instancePublicField = new SomeOtherObject();
        from.privateField = 1;
        from.privateTransientField = 2;
        from.protectedField = "protectedField";
        from.protectedInherited = "protectedInherited";
        to = new SomeObject(0);

        // when
        LenientCopyTool.copyToRealObject(from, to);

        // then
        assertEquals(from.defaultField, to.defaultField);
        assertEquals(from.instancePublicField, to.instancePublicField);
        assertEquals(from.privateField, to.privateField);
        assertEquals(from.privateTransientField, to.privateTransientField);
        assertEquals(from.protectedField, to.protectedField);
        assertEquals(from.protectedInherited, to.protectedInherited);

    }
}
