package org.mockito.internal.util;

import static org.mockito.Mockito.*;

import java.util.LinkedList;

import org.junit.Test;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class ShallowCopyToolTest extends TestBase {

    private ShallowCopyTool tool = new ShallowCopyTool();

    // TODO: inherited fields
    // TODO: if one field fails - should carry on
    static class SomeObject {
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
        
        public int hashCode() {
            return 0;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final SomeObject other = (SomeObject) obj;
            if (defaultField == null) {
                if (other.defaultField != null) {
                    return false;
                }
            } else if (!defaultField.equals(other.defaultField)) {
                return false;
            }
            if (finalField != other.finalField) {
                return false;
            }
            if (instancePublicField == null) {
                if (other.instancePublicField != null) {
                    return false;
                }
            } else if (!instancePublicField.equals(other.instancePublicField)) {
                return false;
            }
            if (privateField != other.privateField) {
                return false;
            }
            if (privateTransientField != other.privateTransientField) {
                return false;
            }
            if (protectedField == null) {
                if (other.protectedField != null) {
                    return false;
                }
            } else if (!protectedField.equals(other.protectedField)) {
                return false;
            }
            return true;
        }
    }

    static class SomeOtherObject {
    }

    private SomeObject from = new SomeObject(100);
    private SomeObject to = mock(SomeObject.class);

    @Test
    public void shouldShallowCopyBasicFinalField() throws Exception {
        // given
        assertEquals(100, from.finalField);
        assertNotEquals(100, to.finalField);

        // when
        tool.copyToMock(from, to);

        // then
        assertEquals(100, to.finalField);
    }

    @Test
    public void shouldShallowCopyTransientPrivateFields() throws Exception {
        // given
        from.privateTransientField = 1000;
        assertNotEquals(1000, to.privateTransientField);

        // when
        tool.copyToMock(from, to);

        // then
        assertEquals(1000, to.privateTransientField);
    }

    @Test
    public void shouldShallowCopyLinkedListIntoMock() throws Exception {
        // given
        LinkedList fromList = new LinkedList();
        LinkedList toList = mock(LinkedList.class);

        // when
        tool.copyToMock(fromList, toList);

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
        
        assertNotEquals(from.defaultField, to.defaultField);
        assertNotEquals(from.instancePublicField, to.instancePublicField);
        assertNotEquals(from.privateField, to.privateField);
        assertNotEquals(from.privateTransientField, to.privateTransientField);
        assertNotEquals(from.protectedField, to.protectedField);

        // when
        tool.copyToMock(from, to);

        // then
        assertEquals(from.defaultField, to.defaultField);
        assertEquals(from.instancePublicField, to.instancePublicField);
        assertEquals(from.privateField, to.privateField);
        assertEquals(from.privateTransientField, to.privateTransientField);
        assertEquals(from.protectedField, to.protectedField);
    }
}