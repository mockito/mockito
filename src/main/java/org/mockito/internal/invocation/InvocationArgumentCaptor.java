package org.mockito.internal.invocation;

import static java.lang.reflect.Array.get;
import static java.lang.reflect.Array.getLength;

import java.lang.reflect.Method;
import java.util.List;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.invocation.Invocation;


/**
 * 
 * @author Christian Schwarz
 *
 */
public class InvocationArgumentCaptor {

    private InvocationArgumentCaptor() {
    }
    
    public static void captureArguments(Invocation invocation,List<ArgumentMatcher<?>> matchers) {
        captureRegularArguments(invocation,matchers);
        if (invocation.getMethod().isVarArgs()) {
            captureVarargsPart(invocation,matchers);
        }
    }

    private static void captureRegularArguments(Invocation invocation,List<ArgumentMatcher<?>> matchers) {
        for (int position = 0; position < regularArgumentsSize(invocation); position++) {
            ArgumentMatcher<?> m = matchers.get(position);
            if (m instanceof CapturesArguments) {
                ((CapturesArguments) m).captureFrom(invocation.getArgument(position));
            }
        }
    }

    private static void captureVarargsPart(Invocation invocation,List<ArgumentMatcher<?>> matchers) {
        int indexOfVararg = invocation.getRawArguments().length - 1;
        
        ArgumentMatcher<?>[] varargMatchers =varargMatchers(matchers,indexOfVararg);
        Object varargsArray = invocation.getRawArguments()[indexOfVararg];
        
        if (varargMatchers.length>1){
            captureEachWithItsDedicatedCaptor(varargsArray,varargMatchers);
        }else if (varargMatchers.length==1){
            captureAllWithASingleCaptor(varargsArray, varargMatchers[0]);
            
        }
    }

    private static void captureAllWithASingleCaptor(Object varargsArray, ArgumentMatcher<?> varargMatcher) {
        if (!(varargMatcher instanceof CapturesArguments))
            return;
        CapturesArguments capturingMatcher = (CapturesArguments) varargMatcher;

        for (int i = 0; i < getLength(varargsArray); i++) {
            capturingMatcher.captureFrom(get(varargsArray, i));
        }
    }

    private static void captureEachWithItsDedicatedCaptor(Object varargsArray , ArgumentMatcher<?>[] matchers) {
        for (int i = 0; i < matchers.length; i++) {
            ArgumentMatcher<?> m = matchers[i];
            if (m instanceof CapturesArguments) {
                ((CapturesArguments) m).captureFrom(get(varargsArray, i));
            }
        }
    }

    private static ArgumentMatcher<?>[] varargMatchers(List<ArgumentMatcher<?>> matchers,int indexOfVararg) {
        int varargsSize = matchers.size()-indexOfVararg;
        ArgumentMatcher<?>[] varargMatchers = new ArgumentMatcher[varargsSize];
        for (int i = 0; i < varargsSize; i++) {
            varargMatchers[i]=matchers.get(i+indexOfVararg);
        }
        return varargMatchers;
    }
    
    private static int regularArgumentsSize(Invocation invocation) {
        Method method = invocation.getMethod();
        int parameterCount = method.getParameterTypes().length;
        if (method.isVarArgs()){
            return parameterCount - 1; // ignores vararg array
        }
        
        return parameterCount;
    }
}
