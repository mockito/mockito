package org.mockito.stubbing;

public interface OngoingVoidStubbing {
    OngoingVoidStubbing thenAnswer(VoidAnswer answer);
    
    OngoingVoidStubbing then(VoidAnswer0 answer);
    
    <A> OngoingVoidStubbing then(VoidAnswer1<A> answer);
    
    <A,B> OngoingVoidStubbing then(VoidAnswer2<A,B> answer);
    
    <A,B,C> OngoingVoidStubbing then(VoidAnswer3<A,B,C> answer);
    
    <A,B,C,D> OngoingVoidStubbing then(VoidAnswer4<A,B,C,D> answer);
    
    <A,B,C, D,E> OngoingVoidStubbing then(VoidAnswer5<A,B,C,D,E> answer);

    OngoingVoidStubbing thenDoNothing();
    
    OngoingVoidStubbing thenThrow(Throwable... throwables);

    OngoingVoidStubbing thenThrow(Class<? extends Throwable> throwableType);

    @SuppressWarnings ("unchecked")
    OngoingVoidStubbing thenThrow(Class<? extends Throwable> toBeThrown, Class<? extends Throwable>... nextToBeThrown);

    OngoingVoidStubbing thenCallRealMethod();

    /**
     * Returns the mock that was used for this stub.
     * <p>
     * It allows to create a stub in one line of code.
     * This can be helpful to keep test code clean.
     * For example, some boring stub can be created & stubbed at field initialization in a test:
     * <pre class="code"><code class="java">
     * public class CarTest {
     *   Car boringStubbedCar = when(mock(Car.class).shiftGear()).thenThrow(EngineNotStarted.class).getMock();
     *
     *   &#064;Test public void should... {}
     * </code></pre>
     *
     * @param <M> The mock type given by the variable type.
     * @return Mock used in this ongoing stubbing.
     * @since 1.9.0
     */
    <M> M getMock();
}