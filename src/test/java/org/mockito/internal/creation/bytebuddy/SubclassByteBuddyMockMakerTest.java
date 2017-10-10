/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import org.junit.Test;
import org.mockito.plugins.MockMaker;

import java.util.Observable;
import java.util.Observer;

import static org.assertj.core.api.Assertions.assertThat;

public class SubclassByteBuddyMockMakerTest extends AbstractByteBuddyMockMakerTest<SubclassByteBuddyMockMaker> {

    public SubclassByteBuddyMockMakerTest() {
        super(new SubclassByteBuddyMockMaker());
    }

    @Test
    public void is_type_mockable_excludes_primitive_wrapper_classes() {
        MockMaker.TypeMockability mockable = mockMaker.isTypeMockable(Integer.class);
        assertThat(mockable.mockable()).isFalse();
        assertThat(mockable.nonMockableReason()).contains("final");
    }

    @Test
    public void is_type_mockable_excludes_primitive_classes() {
        MockMaker.TypeMockability mockable = mockMaker.isTypeMockable(int.class);
        assertThat(mockable.mockable()).isFalse();
        assertThat(mockable.nonMockableReason()).contains("primitive");
    }

    @Test
    public void is_type_mockable_allow_anonymous() {
        Observer anonymous = new Observer() {
            @Override public void update(Observable o, Object arg) { }
        };
        MockMaker.TypeMockability mockable = mockMaker.isTypeMockable(anonymous.getClass());
        assertThat(mockable.mockable()).isTrue();
        assertThat(mockable.nonMockableReason()).contains("");
    }

    @Test
    public void is_type_mockable_give_empty_reason_if_type_is_mockable() {
        MockMaker.TypeMockability mockable = mockMaker.isTypeMockable(SomeClass.class);
        assertThat(mockable.mockable()).isTrue();
        assertThat(mockable.nonMockableReason()).isEqualTo("");
    }


    @Override
    protected Class<?> mockTypeOf(Class<?> type) {
        return type.getSuperclass();
    }
}
