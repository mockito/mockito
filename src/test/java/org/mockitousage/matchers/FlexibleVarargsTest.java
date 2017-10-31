package org.mockitousage.matchers;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Documents work on the #1222
 */
public class FlexibleVarargsTest extends TestBase {

    @Mock IMethods mock;

    @Test
    public void verifies_varargs_flexibly() throws Exception {
        mock.mixedVarargs("1", "2", "3");

        //traditional verify with varargs, same number of args
        verify(mock).mixedVarargs(any(), eq("2"), eq("3"));

        //new matching, using an array
        verify(mock).mixedVarargs(any(), eq(new String[] {"2", "3"}));
        verify(mock).mixedVarargs(any(), (String[]) any());
        verify(mock).mixedVarargs(any(), (String) any());
    }

    @Test
    public void shouldCaptureVarArgs_nullArrayArg1() {
        mock.varargs((String[]) null);

        ArgumentCaptor captor = ArgumentCaptor.forClass(String[].class);
        verify(mock).varargs(captor.capture());
    }

    @Test
    public void shouldCaptureVarArgs_nullArrayArg2() {
        mock.varargs((String[]) null);

        ArgumentCaptor captor = ArgumentCaptor.forClass(String.class);
        verify(mock).varargs(captor.capture());
    }


    @Test
    public void shouldVerifyVarArgs_any_nullArrayArg() {
        mock.varargs((String[]) null);

        verify(mock).varargs(ArgumentMatchers.any());
    }

    @Test
    public void shouldVwerifyVarArgs_eq_NullArrayArg() {
        mock.varargs((String[]) null);

        verify(mock).varargs(ArgumentMatchers.eq(null));
    }



    @Test
    public void shouldVwerifyVarArgs_eq_nullArrayArg() {

        mock.varargs((String)null);

        verify(mock).varargs(ArgumentMatchers.eq(null));
    }

    @Test
    public void shouldVwerifyVarArgs_isNull_nullArrayArg() {

        mock.varargs((String)null);

        verify(mock).varargs(ArgumentMatchers.isNull());
    }

    @Test
    public void shouldVwerifyVarArgs_isNotNull_nullArrayArg() {

        mock.varargs();

        verify(mock).varargs(ArgumentMatchers.isNotNull());
    }

}
