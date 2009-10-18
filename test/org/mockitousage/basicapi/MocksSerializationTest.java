/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.basicapi;

import org.junit.Test;
import org.junit.Ignore;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MocksSerializationTest extends TestBase {

    @Test
    @Ignore
    public void shouldAllowMockToBeSerializable() throws Exception {
        //given
        IMethods mock = mock(IMethods.class, withSettings().extraInterfaces(Serializable.class));
        ByteArrayOutputStream serialized = new ByteArrayOutputStream();

        //when
        new ObjectOutputStream(serialized).writeObject(mock);

        //then
    }
}