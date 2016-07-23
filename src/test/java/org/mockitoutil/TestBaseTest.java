package org.mockitoutil;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by sfaber on 7/22/16.
 */
public class TestBaseTest extends TestBase {

    @Test public void filters_line_no_from_stack_trace() {
        assertEquals("", filterLineNo(""));
        assertEquals("asdf", filterLineNo("asdf"));
        assertEquals("asdf (FooBar.java:0) blah", filterLineNo("asdf (FooBar.java:23) blah"));
        assertEquals("asdf\n(FooBar.java:0)\nblah", filterLineNo("asdf\n(FooBar.java:123123)\nblah"));
        assertEquals("asdf\n(FooBar.java:0)\n(Xxx.java:0)blah", filterLineNo("asdf\n(FooBar.java:2)\n(Xxx.java:1)blah"));

        assertEquals("asdf\n(FooBar.java:0)\nXxx.java:20)blah", filterLineNo("asdf\n(FooBar.java:2)\nXxx.java:20)blah"));
    }
}
