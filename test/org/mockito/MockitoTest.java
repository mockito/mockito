/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito;

import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
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
    	StaticAbstractClass mock = Mockito.partialMock(StaticAbstractClass.class);
    	Mockito.when(mock.play(1)).thenReturn("one");
    	Mockito.when(mock.play(2)).thenReturn("two");
    	assertEquals(Arrays.asList("one", "two", null), mock.playNumbers(1, 3));
    	Mockito.verify(mock).play(1);
    	Mockito.verify(mock).play(2);
    	Mockito.verify(mock).play(3);
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
    public void partialMockWorkForFinalMethods() {
    	StaticAbstractClass mock = Mockito.partialMock(AbstractClassWithFinalMethods.class);
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
    public void nonStaticInnerClassCannotBeDirectlyPartialMocked() {
    	Mockito.partialMock(NonStaticInnerClass.class);
    }

    abstract class NonStaticInnerClass extends StaticAbstractClass {
    	Object getEnclosingInstance() {
    		return MockitoTest.this;
    	}
    }

    @Test(expected=IllegalArgumentException.class)
    public void privateClassCannotBeDirectlyPartialMocked() {
    	Mockito.partialMock(PrivateAbstractClass.class);
    }

    private abstract class PrivateAbstractClass extends StaticAbstractClass {}
  
    @Test
    public void partialMockInvokesDefaultConstructor() {
    	AbstractClassWithState mock = Mockito.partialMock(AbstractClassWithState.class);
    	assertEquals(0, mock.state.size());
    }
  
    static abstract class AbstractClassWithState {
    	final List<String> state = new ArrayList<String>();
    }
  
    @Test
    public void partialMockSkipsPrivateConstructor() {
    	AbstractClassWithPrivateConstructor mock = Mockito.partialMock(AbstractClassWithPrivateConstructor.class);
    	assertNull(mock.state);
    }
  
    static abstract class AbstractClassWithPrivateConstructor {
    	final List<String> state = new ArrayList<String>();
    	private AbstractClassWithPrivateConstructor() {
    		throw new AssertionError();
    	}
    }
  
    @Test
    public void partialMockSkipsConstructorWithParameters() {
    	AbstractClassWithConstructorWithParameters mock = Mockito.partialMock(AbstractClassWithConstructorWithParameters.class);
    	assertNull(mock.state);
    }
  
    static abstract class AbstractClassWithConstructorWithParameters {
    	final List<String> state = new ArrayList<String>();
    	private AbstractClassWithConstructorWithParameters(int i) {
    		throw new AssertionError();
    	}
    }
  
    @Test(expected=NullPointerException.class)
    public void partialMockWithNullEnclosingInstance() {
    	Mockito.partialMock(null, NonStaticInnerClass.class);
    }

    @Test(expected=IllegalArgumentException.class)
    public void partialMockWithEnclosingInstanceAndStaticNestedClass() {
    	Mockito.partialMock(this, StaticAbstractClass.class);
    }

    @Test(expected=IllegalArgumentException.class)
    public void partialMockWithEnclosingInstanceAndTopLevelClass() {
    	Mockito.partialMock(this, AbstractList.class);
    }

    @Test(expected=IllegalArgumentException.class)
    public void partialMockWithWrongEnclosingInstance() {
    	Mockito.partialMock("not enclosing instance", NonStaticInnerClass.class);
    }

    @Test
    public void partialMockWithEnclosingInstance() {
    	NonStaticInnerClass mock = Mockito.partialMock(this, NonStaticInnerClass.class);
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