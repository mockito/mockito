/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito;

import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockitoutil.TestBase;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.times;

@SuppressWarnings("unchecked")
public class MockitoTest extends TestBase {

    @Test
    public void shouldRemoveStubbableFromProgressAfterStubbing() {
        List mock = Mockito.mock(List.class);
        Mockito.when(mock.add("test")).thenReturn(true);
        //TODO Consider to move to separate test
        assertNull(new ThreadSafeMockingProgress().pullOngoingStubbing());
    }
    
    @Test(expected=NotAMockException.class)
    public void shouldValidateMockWhenVerifying() {
        Mockito.verify("notMock");
    }
    
    @Test(expected=NotAMockException.class)
    public void shouldValidateMockWhenVerifyingWithExpectedNumberOfInvocations() {
        Mockito.verify("notMock", times(19));
    }
    
    @Test(expected=NotAMockException.class)
    public void shouldValidateMockWhenVerifyingNoMoreInteractions() {
        Mockito.verifyNoMoreInteractions("notMock");
    }
    
    @Test(expected=NotAMockException.class)
    public void shouldValidateMockWhenVerifyingZeroInteractions() {
        Mockito.verifyZeroInteractions("notMock");
    }
    
    @SuppressWarnings("deprecation")
    @Test(expected=NotAMockException.class)
    public void shouldValidateMockWhenStubbingVoid() {
        Mockito.stubVoid("notMock");
    }
    
    @Test(expected=NotAMockException.class)
    public void shouldValidateMockWhenCreatingInOrderObject() {
        Mockito.inOrder("notMock");
    }
    
    @Test
    public void shouldStartingMockSettingsContainDefaultBehavior() {
        //when
        MockSettingsImpl settings = (MockSettingsImpl) Mockito.withSettings();
        
        //then
        assertEquals(Mockito.RETURNS_DEFAULTS, settings.getDefaultAnswer());
    }

    @Test
    public void mocksOnlyAbstractMethods() {
    	StaticAbstractClass mock = Mockito.spy(StaticAbstractClass.class);
    	Mockito.when(mock.play(1)).thenReturn("one");
    	Mockito.when(mock.play(2)).thenReturn("two");
    	assertEquals(Arrays.asList("one", "two", null), mock.playNumbers(1, 3));
    	Mockito.verify(mock).play(1);
    	Mockito.verify(mock).play(2);
    	Mockito.verify(mock).play(3);
    	Mockito.verify(mock).playNumbers(1, 3);
    }

    @Test(expected = WantedButNotInvoked.class)
    public void abstractMethodsSpied() {
    	StaticAbstractClass mock = Mockito.spy(StaticAbstractClass.class);
    	Mockito.verify(mock).play(1);
    }

    @Test(expected = WantedButNotInvoked.class)
    public void nonAbstractMethodsAlsoSpied() {
    	StaticAbstractClass mock = Mockito.spy(StaticAbstractClass.class);
    	Mockito.verify(mock).playNumbers(1, 3);
    }
  
    static abstract class StaticAbstractClass {
    	abstract String play(int i);
 
    	List<String> playNumbers(int from, int to) {
    		List<String> result = new ArrayList<String>();
    		for (int i = from ; i <= to; i++) {
    			result.add(play(i));
    		}
    		return result;
    	}
    }

    @Test
    public void spyWorksForFinalMethods() {
    	StaticAbstractClass mock = Mockito.spy(AbstractClassWithFinalMethods.class);
    	Mockito.when(mock.play(1)).thenReturn("one");
    	Mockito.when(mock.play(2)).thenReturn("two");
    	assertEquals(Arrays.asList("one", "two", null), mock.playNumbers(1, 3));
    	Mockito.verify(mock).play(1);
    	Mockito.verify(mock).play(2);
    	Mockito.verify(mock).play(3);
    	
    }
 
    static abstract class AbstractClassWithFinalMethods extends StaticAbstractClass {
    	@Override final List<String> playNumbers(int from, int to) {
    		return super.playNumbers(from, to);
    	}
    }

    @Test(expected=IllegalArgumentException.class)
    public void nonStaticInnerClassCannotBeDirectlySpied() {
    	Mockito.spy(NonStaticInnerClass.class);
    }

    abstract class NonStaticInnerClass extends StaticAbstractClass {
    	Object getEnclosingInstance() {
    		return MockitoTest.this;
    	}
    }

    @Test(expected=IllegalArgumentException.class)
    public void privateClassCannotBeDirectlySpied() {
    	Mockito.spy(PrivateAbstractClass.class);
    }

    private abstract class PrivateAbstractClass extends StaticAbstractClass {}
  
    @Test
    public void spyInvokesDefaultConstructor() {
    	AbstractClassWithState mock = Mockito.spy(AbstractClassWithState.class);
    	assertEquals(0, mock.state.size());
    }
  
    static abstract class AbstractClassWithState {
    	final List<String> state = new ArrayList<String>();
    }
  
    @Test
    public void spySkipsPrivateConstructor() {
    	AbstractClassWithPrivateConstructor mock = Mockito.spy(AbstractClassWithPrivateConstructor.class);
    	assertNull(mock.state);
    }
  
    static abstract class AbstractClassWithPrivateConstructor {
    	final List<String> state = new ArrayList<String>();
    	private AbstractClassWithPrivateConstructor() {
    		throw new AssertionError();
    	}
    }
  
    @Test
    public void spySkipsConstructorWithParameters() {
    	AbstractClassWithConstructorWithParameters mock = Mockito.spy(AbstractClassWithConstructorWithParameters.class);
    	assertNull(mock.state);
    }
  
    static abstract class AbstractClassWithConstructorWithParameters {
    	final List<String> state = new ArrayList<String>();
    	private AbstractClassWithConstructorWithParameters(int i) {
    		throw new AssertionError();
    	}
    }
  
    @Test(expected=NullPointerException.class)
    public void spyWithNullEnclosingInstance() {
    	Mockito.spy(null, NonStaticInnerClass.class);
    }

    @Test(expected=IllegalArgumentException.class)
    public void spyWithEnclosingInstanceAndStaticNestedClass() {
    	Mockito.spy(this, StaticAbstractClass.class);
    }

    @Test(expected=IllegalArgumentException.class)
    public void spyWithEnclosingInstanceAndTopLevelClass() {
    	Mockito.spy(this, AbstractList.class);
    }

    @Test(expected=IllegalArgumentException.class)
    public void spyWithWrongEnclosingInstance() {
    	Mockito.spy("not enclosing instance", NonStaticInnerClass.class);
    }

    @Test
    public void spyWithEnclosingInstance() {
    	NonStaticInnerClass mock = Mockito.spy(this, NonStaticInnerClass.class);
    	Mockito.when(mock.play(1)).thenReturn("one");
    	Mockito.when(mock.play(2)).thenReturn("two");
    	assertEquals(Arrays.asList("one", "two", null), mock.playNumbers(1, 3));
    	Mockito.verify(mock).play(1);
    	Mockito.verify(mock).play(2);
    	Mockito.verify(mock).play(3);
    	assertSame(this, mock.getEnclosingInstance());
    }
    
    //TODO: stack filter does not work very well when it comes to threads?
}