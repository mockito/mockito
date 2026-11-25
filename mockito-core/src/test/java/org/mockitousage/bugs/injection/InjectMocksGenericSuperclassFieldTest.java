/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs.injection;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.InputStream;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

// issue 3602: fields whose type is the type parameter of a generic superclass
// are not injected anymore since 5.2.0
@RunWith(MockitoJUnitRunner.class)
public class InjectMocksGenericSuperclassFieldTest {

    public static class GenericType<T> {
        T field;

        List<T> listField;
    }

    public static class TypeImpl extends GenericType<InputStream> {}

    public static class OtherTypeImpl extends GenericType<String> {}

    @Mock private InputStream inputStream;

    @Mock private List<InputStream> inputStreamList;

    @InjectMocks private TypeImpl typeImpl;

    @InjectMocks private OtherTypeImpl otherTypeImpl;

    @Test
    public void injects_field_typed_with_type_parameter_of_generic_superclass() {
        assertSame(inputStream, typeImpl.field);
    }

    @Test
    public void injects_parameterized_field_typed_with_type_parameter_of_generic_superclass() {
        assertSame(inputStreamList, typeImpl.listField);
    }

    @Test
    public void does_not_inject_mock_whose_type_is_incompatible_with_resolved_type_parameter() {
        assertNull(otherTypeImpl.field);
    }
}
