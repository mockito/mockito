package org.mockitousage.matchers;

import org.junit.Test;
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
    }
}
