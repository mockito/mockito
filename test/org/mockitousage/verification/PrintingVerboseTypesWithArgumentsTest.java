/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class PrintingVerboseTypesWithArgumentsTest extends TestBase {

    class Boo {
        public void withLong(long x) {
        }
        
        public void withLongAndInt(long x, int y) {
        }
    }
    
    @Test
    public void shouldNotReportArgumentTypesWhenToStringIsTheSame() throws Exception {
        //given
        Boo boo = mock(Boo.class);
        boo.withLong(100);
        
        try {
            //when
            verify(boo).withLong(eq(100));
            fail();
        } catch (ArgumentsAreDifferent e) {
            //then
            assertContains("withLong((Integer) 100);", e.getMessage());
            assertContains("withLong((Long) 100);", e.getMessage());
        }
    }
    
    @Test
    public void shouldShowTheTypeOfOnlyTheArgumentThatDoesntMatch() throws Exception {
        //given
        Boo boo = mock(Boo.class);
        boo.withLongAndInt(100, 200);
        
        try {
            //when
            verify(boo).withLongAndInt(eq(100), eq(200));
            fail();
        } catch (ArgumentsAreDifferent e) {
            //then
            assertContains("withLongAndInt((Integer) 100, 200)", e.getMessage());
            assertContains("withLongAndInt((Long) 100, 200)", e.getMessage());
        }
    }
    
    @Test
    public void shouldShowTheTypeOfTheMismatchingArgumentWhenOutputDescriptionsForInvocationsAreDifferent() throws Exception {
        //given
        Boo boo = mock(Boo.class);
        boo.withLongAndInt(100, 200);
        
        try {
            //when
            verify(boo).withLongAndInt(eq(100), anyInt());
            fail();
        } catch (ArgumentsAreDifferent e) {
            //then
            assertContains("withLongAndInt((Long) 100, 200)", e.getMessage());
            assertContains("withLongAndInt((Integer) 100, <any>)", e.getMessage());
        }
    }
    
    @Test
    public void shouldNotShowTypesWhenArgumentValueIsDifferent() throws Exception {
        //given
        Boo boo = mock(Boo.class);
        boo.withLongAndInt(100, 200);
        
        try {
            //when
            verify(boo).withLongAndInt(eq(100L), eq(230));
            fail();
        } catch (ArgumentsAreDifferent e) {
            //then
            assertContains("withLongAndInt(100, 200)", e.getMessage());
            assertContains("withLongAndInt(100, 230)", e.getMessage());
        }
    }
    
    class Foo {
        
        private final int x;

        public Foo(int x) {
            this.x = x;
        }
        
        public boolean equals(Object obj) {
            return x == ((Foo) obj).x;
        }
        
        public int hashCode() {
            return 1;
        }
        
        public String toString() {
            return "foo";
        }
    }
    
    @Test
    public void shouldNotShowTypesWhenTypesAreTheSameEvenIfToStringGivesTheSameResult() throws Exception {
        //given
        IMethods mock = mock(IMethods.class);
        mock.simpleMethod(new Foo(10));
        
        try {
            //when
            verify(mock).simpleMethod(new Foo(20));
            fail();
        } catch (ArgumentsAreDifferent e) {
            //then
            assertContains("simpleMethod(foo)", e.getMessage());
        }
    }
}