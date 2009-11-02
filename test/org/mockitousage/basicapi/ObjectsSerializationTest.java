/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.basicapi;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.*;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.internal.matchers.Any;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class ObjectsSerializationTest extends TestBase implements Serializable {

    //Ok, this test has nothing to do with mocks but it shows fundamental feature of java serialization that
    //plays important role in mocking:
    //Serialization/deserialization actually replaces all instances of serialized object in the object graph (if there are any)
    //thanks to that mechanizm, stubbing & verification can correctly match method invocations because
    //one of the parts of invocation matching is checking if mock object is the same

    class Bar implements Serializable {
        Foo foo;
    }

    class Foo implements Serializable {
        Bar bar;
        Foo() {
            bar = new Bar();
            bar.foo = this;
        }
    }

    @Test
    public void shouldSerializationWork() throws Exception {
        //given
        Foo foo = new Foo();
        //when
        foo = serializeAndBack(foo);
        //then
        assertSame(foo, foo.bar.foo);
    }
}