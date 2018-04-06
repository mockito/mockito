/*
 * Copyright © 2017 Morgan Stanley.  All rights reserved.
 *
 * THIS SOFTWARE IS SUBJECT TO THE TERMS OF THE MIT LICENSE.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * IN ADDITION, THE FOLLOWING DISCLAIMER APPLIES IN CONNECTION WITH THIS SOFTWARE:
 * THIS SOFTWARE IS LICENSED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE AND ANY WARRANTY OF NON-INFRINGEMENT, ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. THIS SOFTWARE MAY BE REDISTRIBUTED TO OTHERS ONLY BY EFFECTIVELY USING THIS OR ANOTHER EQUIVALENT DISCLAIMER IN ADDITION TO ANY OTHER REQUIRED LICENSE TERMS.
 */

package org.mockito;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

import static java.lang.reflect.Modifier.isStatic;
import static java.text.MessageFormat.format;
import static org.mockito.Mockito.when;

/**
 * Helper class to pre-stub some behaviour in the mocks, it's done in Java as I've found some issues with the
 * Scala Reflection API and the CGlib class of the mock
 * <p>
 * This is a pseudo-hack, we could do better if the Mockito API supported a way to set a default answer to only some
 * of the methods in the class
 *
 * @author Bruno Bonanno
 */
class MockitoEnhancer {

    static <T> T stubMock(T entityMock, Class<?> entityClass) {
        Stream.of(entityClass.getMethods())
            .filter(m -> !isStatic(m.getModifiers()))
            .filter(m -> m.getName().contains("$default$"))
            .forEach(defaultParamMethod -> when(call(defaultParamMethod, entityMock)).thenCallRealMethod());

        return entityMock;
    }

    @SuppressWarnings("unchecked")
    private static <T> T call(Method m, Object mock, Object... arguments) {
        try {
            return (T) m.invoke(mock, arguments);
        } catch (Exception e) {
            String msg = format("Calling {0} on {1} with args {2}", m, mock, Arrays.toString(arguments));
            throw new RuntimeException(msg, e);
        }
    }
}
