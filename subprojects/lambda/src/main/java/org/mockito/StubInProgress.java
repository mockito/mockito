package org.mockito;

import org.mockito.exceptions.base.MockitoException;

import java.util.concurrent.Callable;

abstract class StubInProgress<R> {

    public void thenReturn(R returnValue) {
        MockitoLambdaHandlerImpl.answerValue = (_unused) -> returnValue;

        this.invokeMethod();
    }

    public void thenThrow(Throwable e) {
        MockitoLambdaHandlerImpl.answerValue = (_unused) -> {throw e;};

        this.invokeMethod();
    }

    abstract void invokeMethod();

}
