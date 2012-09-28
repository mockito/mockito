package org.mockito.internal.matchers;

import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;
import org.mockito.exceptions.Reporter;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


@SuppressWarnings("unchecked")
public class VarargCapturingMatcher<T> extends ArgumentMatcher<T> implements CapturesArguments, VarargMatcher, Serializable {
    private static final long serialVersionUID = 4057053345838026645L;
    private LinkedList<List<T>> arguments = new LinkedList<List<T>>();

    public boolean matches(Object argument) {
        return true;
    }

    public void describeTo(Description description) {
        description.appendText("<Capturing variable argument>");
    }

    public List<T> getLastVarargs() {
        if (arguments.isEmpty()) {
            new Reporter().noArgumentValueWasCaptured();
            return null;
        } else {
            return arguments.getLast();
        }
    }

    public List<List<T>> getAllVarargs() {
        return arguments;
    }

    public void captureFrom(Object varArgArray) {
        List<T> vararg = unpackAsList(varArgArray);
        this.arguments.add(vararg);
    }

    private List<T> unpackAsList(Object varArgArray) {
        if (varArgArray instanceof Object[]) {
            return Arrays.asList((T[]) varArgArray);
        } else if (varArgArray.getClass().isArray()) {
            Object[] primitiveArray = new Object[Array.getLength(varArgArray)];
            for (int i = 0; i < primitiveArray.length; i++) {
                primitiveArray[i] = Array.get(varArgArray, i);
            }
            return Arrays.asList((T[]) primitiveArray);
        } else {
            return Collections.singletonList((T) varArgArray);
        }
    }
}
