/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.warnings;

import static org.mockito.Mockito.*;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.ExperimentalMockitoJUnitRunner;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
@RunWith(ExperimentalMockitoJUnitRunner.class)
public class WarningAboutStubCalledWithDifferentArgsTest extends TestBase {

    public class SomeController {

        private final ReadFromSomeFileSystem reader;

        public SomeController(ReadFromSomeFileSystem reader, Object object) {
            this.reader = reader;
        }

        public byte[] naughtyMethodUnderTestWhichDoesNotFailBecauseItReturnsAValue(String filename) {
            return this.reader.readFromFile("filename");
        }
    }

    public interface ReadFromSomeFileSystem {

        byte[] readFromFile(String filename);

    }

    @Mock
    IMethods mock;

    @Ignore
    @Test
    public void shouldFailButOnlyWhenIAssertReturnValueAndIWantToKnowWhy() throws Throwable {
        ReadFromSomeFileSystem reader = mock(ReadFromSomeFileSystem.class);

        SomeController controller = new SomeController(reader, null);

        final String filename = "/some/non/random/path";
        final String message = "this is my message to you";
        when(reader.readFromFile(filename)).thenReturn(message.getBytes("UTF8"));

        byte[] bytes = controller.naughtyMethodUnderTestWhichDoesNotFailBecauseItReturnsAValue(filename);

//        try {
        assertNotNull("Should have returned some bytes, i am HUNGRY!", bytes);
//        } catch (Error ex) {
//            verify(reader).readFromFile(filename);
//            // i want to replace this bit with something like "verifyAllTheThingsIStubbed(mock)"
//            throw ex;
//        }
    }
}