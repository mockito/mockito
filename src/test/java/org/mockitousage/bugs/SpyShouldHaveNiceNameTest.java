/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.bugs;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Spy;
import org.mockitoutil.TestBase;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.mockito.Mockito.verify;

//see issue 216
public class SpyShouldHaveNiceNameTest extends TestBase {

    @Spy List<Integer> veryCoolSpy = new LinkedList<Integer>();

    @Test
    public void shouldPrintNiceName() {
        //when
        veryCoolSpy.add(1);

        try {
            verify(veryCoolSpy).add(2);
            fail();
        } catch(AssertionError e) {
            Assertions.assertThat(e.getMessage()).contains("veryCoolSpy");
        }
    }
}