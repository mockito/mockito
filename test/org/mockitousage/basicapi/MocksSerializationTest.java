/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.basicapi;

import org.junit.Ignore;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

@Ignore
public class MocksSerializationTest extends TestBase {

    @Test
    public void shouldAllowMockToBeSerializable() throws Exception {
        //given
        IMethods mock = mock(IMethods.class);
        ByteArrayOutputStream serialized = new ByteArrayOutputStream();

        //when
        new ObjectOutputStream(serialized).writeObject(mock);

        //then
    }
}