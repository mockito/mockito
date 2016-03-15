/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import static junit.framework.TestCase.assertEquals;

import java.util.ArrayList;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockitoutil.TestBase;

public class CapturingMatcherTest extends TestBase {

    @Test
    public void should_capture_arguments() throws Exception {
        CapturingMatcher m = new CapturingMatcher(Any.ANY, new ArrayList<String>());
        
        Assertions.assertThat(m.toString()).containsSequence("<Capturing argument>");
    }
    
    @Test
    public void should_know_last_captured_value() throws Exception {
        ArrayList<String> arguments = new ArrayList<String>();
        CapturingMatcher<String> m = new CapturingMatcher<String>(Any.ANY, arguments);
        
        m.captureFrom("foo");
        m.captureFrom("bar");
        
        assertEquals("bar", arguments.get(1));
    }
}