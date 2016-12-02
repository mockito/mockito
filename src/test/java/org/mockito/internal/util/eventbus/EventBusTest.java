package org.mockito.internal.util.eventbus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.internal.util.eventbus.EventBus;
import org.mockito.listeners.Subscribe;

public class EventBusTest {

    private EventBus eventBus;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void init() {
        eventBus = new EventBus();
    }

    @Test
    public void register_null_throwsIAE() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("The listener must not be null!");

        eventBus.register(null);
    }

    @Test
    public void register_noHandlerMethodsListener_noExceptionMustBeThrown() {
        eventBus.register(new Object());
    }

    @Test
    public void post_null_throwsIAE() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("The event must not be null!");

        eventBus.post(null);
    }

    @Test
    public void post_noSubscriberMethods_noExceptionMustBeThrown() {
        eventBus.register(new Object());

        eventBus.post("Hello");
    }

    @Test
    public void post_singleSubscriberMethod_mustBeInvoked() throws Exception {
        class Listener {
            Number lastPostedNumber;

            @Subscribe
            public void handle(Number n) {
                lastPostedNumber = n;
            }
        }
        
        Listener listener = new Listener();
        eventBus.register(listener);

        eventBus.post(123);

        assertThat(listener.lastPostedNumber).isEqualTo(123);
    }

    @Test
    public void post_noHandlerMethods_mustBeIgnored() throws Exception {
        ClassWithoutHandlerMethods listener = new ClassWithoutHandlerMethods();

        eventBus.register(listener);

        eventBus.post(123);

        assertThat(listener.timesCalled).isZero();
    }

    @Test
    public void post_multipleSubscribers_allMustMeNotified()   {
        MultipleSubscribers listener = new MultipleSubscribers();
        eventBus.register(listener);

        eventBus.post(123);

        assertThat(listener.lastPostedInteger).isEqualTo(123);
        assertThat(listener.lastPostedNumber1).isEqualTo(123);
        assertThat(listener.lastPostedNumber2).isEqualTo(123);
    }

    @Test
    public void post_multipleSubscribers_allMustMeNotified2()   {
        MultipleSubscribers listener = new MultipleSubscribers();
        eventBus.register(listener);

        eventBus.post(123L);

        assertThat(listener.lastPostedInteger).isNull();//must not be called cause 123L is not an Integer
        assertThat(listener.lastPostedNumber1).isEqualTo(123L);
        assertThat(listener.lastPostedNumber2).isEqualTo(123L);
    }
    
    @Test
    public void post_maliciousHandler_mustBeIgnored()   {
        MultipleSubscribers listener = new MultipleSubscribers(){
            @Override
            public void handle1(Number n) {
                throw new RuntimeException("I am evil!");
            }
        };
        eventBus.register(listener);

        eventBus.post(123);

        assertThat(listener.lastPostedInteger).isEqualTo(123);
        assertThat(listener.lastPostedNumber1).isNull();
        assertThat(listener.lastPostedNumber2).isEqualTo(123);
    }
    
    @Test
    public void register_multipleRegistrationOfTheSameListener() throws Exception {
        
        class Listener  {
            int timesCalled;
            @Subscribe
            public void consumeEvent(Number n){
                timesCalled++;
            }
        }
        Listener listener = new Listener();
        eventBus.register(listener);
        eventBus.register(listener);
        
        eventBus.post(123);

        assertThat(listener.timesCalled).isEqualTo(1);
    }

    @Test
    public void register_differentInstancesWithSameEqualsHashCode_bothMustBeInvoked() throws Exception {
        class Listener {
            Number lastPostedNumber;

            @Subscribe
            public void handle(Number n) {
                lastPostedNumber = n;
            }
            
            @Override
            public int hashCode() {
                return 1;
            }
            
            @Override
            public boolean equals(Object obj) {
                return obj instanceof Listener;
            }
        }
        
        Listener listener1 = new Listener();
        Listener listener2 = new Listener();
        eventBus.register(listener1);
        eventBus.register(listener2);

        eventBus.post(123);

        assertThat(listener1.lastPostedNumber).isEqualTo(123);
        assertThat(listener2.lastPostedNumber).isEqualTo(123);
    }
    
    @Test
    public void unregister_null_mustBeIgnored(){
        eventBus.unregister(null);
    }
    
    @Test
    public void unregister_notRegisteredInstance_mustBeIgnored(){
        eventBus.unregister(new MultipleSubscribers());
    }

    @Test
    public void unregister_registeredListener_willNotReceiveEventAnyMore() throws Exception {
        MultipleSubscribers listener=new MultipleSubscribers();
        eventBus.register(listener);
        
        eventBus.unregister(listener);
        
        eventBus.post(123);
        
        assertThat(listener.lastPostedInteger).isNull();
    }
    
    @Test
    public void unregister_registeredListener_willNotReceiveEventAnyMoreButOthers() throws Exception {
        MultipleSubscribers listener1=new MultipleSubscribers();
        MultipleSubscribers listener2=new MultipleSubscribers();
        eventBus.register(listener1);
        eventBus.register(listener2);

        
        eventBus.unregister(listener1);
        
        eventBus.post(123);
        
        assertThat(listener1.lastPostedInteger).isNull();
        assertThat(listener2.lastPostedInteger).isEqualTo(123);
    }

    @SuppressWarnings("unused")
    private static final class ClassWithoutHandlerMethods {
        public int timesCalled = 0;

        /** This is no valid handler method cause it lacks the {@link Subscribe} annotation */
        public void handle(Number n) {
            timesCalled++;
        }

        /** This is no valid handler method cause it has more than one parameter */
        @Subscribe
        public void handle(Number n, Number m) {
            timesCalled++;
        }

        /** This is no valid handler method cause it has no parameter */
        @Subscribe
        public void handle() {
            timesCalled++;
        }

        /** This is no valid handler method cause it has a not instance parameter */
        @Subscribe
        public void handle(boolean b) {
            timesCalled++;
        }

        /** This is no valid handler method cause it isn't public */
        @Subscribe
        void handleDefaultVisibility(Number n) {
            timesCalled++;
        }

        /** This is no valid handler method cause it isn't public */
        @Subscribe
        protected void handleProtectedVisibility(Number n) {
            timesCalled++;
        }

        /** This is no valid handler method cause it isn't public */
        @Subscribe
        private void handlePrivateVisibility(Number n) {
            timesCalled++;
        }
    }

    private static class MultipleSubscribers {

        public Number lastPostedNumber1, lastPostedNumber2;
        public Integer lastPostedInteger;

        @Subscribe
        public void handle1(Number n) {
            lastPostedNumber1 = n;
        }

        @Subscribe
        public void handle2(Number n) {
            lastPostedNumber2 = n;
        }

        @Subscribe
        public void handle(Integer n) {
            lastPostedInteger = n;
        }
    }
}