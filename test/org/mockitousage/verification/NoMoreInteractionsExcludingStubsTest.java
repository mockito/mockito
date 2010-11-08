/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.internal.invocation.CapturesArgumensFromInvocation;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMarker;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.util.MockUtil;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.util.List;

import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class NoMoreInteractionsExcludingStubsTest extends TestBase {

    @Mock IMethods mock;

    @Test
    public void shouldAllowToExcludeStubsForVerification() throws Exception {
        //given
        when(mock.simpleMethod()).thenReturn("foo");

        //when
        String stubbed = mock.simpleMethod(); //irrelevant call because it is stubbing
        mock.objectArgMethod(stubbed);

        //then
        verify(mock).objectArgMethod("foo");

        //verifyNoMoreInteractions fails:
        try { verifyNoMoreInteractions(mock); fail(); } catch (NoInteractionsWanted e) {};
        
        //but it works when stubs are ignored:
        ignoreStubs(mock);
        verifyNoMoreInteractions(mock);
    }

//    @Test
//    public void shouldIgnoringStubsDetectNulls() throws Exception {
//        //given
//
//        //when
//        ignoreStubs(mock, null);
//
//        //then
//    }
//
//    @Test
//    public void shouldIgnoringStubsDetectNonMocks() throws Exception {
//        //given
//
//        //when
//        ignoreStubs(mock, new Object());
//
//        //then
//    }

    /**
     * Ignores stubbed methods of given mocks for the sake of verification.
     * <p>
     * Other words: all *stubbed* methods of given mocks are made *verfied* so that they don't get in a way during verifyNoMoreInteractions().
     * <p>
     * This method changes the input mocks! This method returns input mocks for convenience. 
     * <p>
     * Example:
     * <pre>
     *  //mocking lists for the sake of the example (if you mock List in real you will burn in hell)
     *  List mock1 = mock(List.class), mock2 = mock(List.class);
     * 
     *  //stubbing mocks:
     *  when(mock1.get(0)).thenReturn(10);
     *  when(mock2.get(0)).thenReturn(20);
     *
     *  //using mocks by calling stubbed get(0) methods:
     *  System.out.println(mock1.get(0)); //prints 10
     *  System.out.println(mock2.get(0)); //prints 20
     *
     *  //using mocks by calling clear() methods:
     *  mock1.clear();
     *  mock2.clear();
     *
     *  //verification:
     *  verify(mock1).clear();
     *  verify(mock2).clear();
     *
     *  //verifyNoMoreInteractions() fails because get() methods were not accounted for.
     *  try { verifyNoMoreInteractions(mock1, mock2); } catch (NoInteractionsWanted e);
     *
     *  //However, if ignore stubbed methods then we can verifyNoMoreInteractions() 
     *  verifyNoMoreInteractions(ignoreStubs(mock1, mock2));
     *
     *  //Remember that ignoreStubs() *changes* the input mocks and returns them for convenience.
     * <pre>
     *
     * @param mocks input mocks that will be changed
     * @return the same mocks that were passed in as parameters
     */
    public static Object[] ignoreStubs(Object... mocks) {
        for (Object m : mocks) {
            InvocationContainer invocationContainer = new MockUtil().getMockHandler(m).getInvocationContainer();
            List<Invocation> ins = invocationContainer.getInvocations();
            for (Invocation in : ins) {
                InvocationMarker invocationMarker = new InvocationMarker();
                if (in.stubInfo() != null) {
                    invocationMarker.markVerified(in, new CapturesArgumensFromInvocation() {
                        public void captureArgumentsFrom(Invocation i) {
                            //don't capture
                        }
                    });
                }
            }
        }
        return mocks;
    }

}