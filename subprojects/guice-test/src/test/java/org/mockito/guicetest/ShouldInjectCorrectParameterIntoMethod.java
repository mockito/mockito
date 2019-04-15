package org.mockito.guicetest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.common.collect.ImmutableList;
import com.google.inject.CreationException;
import com.google.inject.Guice;
import com.google.inject.Key;
import java.util.List;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ShouldInjectCorrectParameterIntoMethod {

    /** A base class providing some cool functionality for many subclasses. */
    static class Injectable<T> {
        private List<String> strings;

        /**
         * Inject-annotated method used to avoid having to pass some superclass implementation details
         * through all subclasses.
         */
        @Inject
        void setStrings(List<String> strings) {
            this.strings = strings;
        }
    }

    /**
     * A list of strings bound in the injectors to demonstrate that Guice calls
     * Injectable.setStrings().
     */
    private static final ImmutableList<String> PRIMARY_COLORS = ImmutableList
        .of("Red", "Yellow", "Green", "Blue");

    /** A Guice key for a fully-parameterized subtype of Injectable. */
    private static final Key<Injectable<Void>> VOID_INJECTABLE = new Key<>() {
    };

    @Mock
    private Injectable<Void> mockInjectable;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testWithRealInstance() {
        Injectable<Void> injectable =
            Guice.createInjector(
                binder -> {
                    binder.bind(new Key<List<String>>() {}).toInstance(PRIMARY_COLORS);
                    // This tells Guice to bind this parameterized type. It allows the injector to
                    // create an instance as long as it can find all the injected dependencies from
                    // constructor, field, and method injection.
                    binder.bind(VOID_INJECTABLE);
                })
                .getInstance(VOID_INJECTABLE);
        // In our case, Guice creates the instance by calling the default constructor (since there is
        // not a constructor tagged with @Inject) and then calling each method tagged with @Inject with
        // arguments pulled from the injector. Since we bound List<String>, it is able to inject that
        // method argument.
        assertThat(injectable.strings).contains("Green");
    }

    @Test
    public void testWithMockInstance() {
        // This should also create an object, but fails looking for a raw instance of List.
        Injectable<Void> injectable =
            Guice.createInjector(
                binder -> {
                    binder.bind(new Key<List<String>>() {}).toInstance(PRIMARY_COLORS);
                    // This tells Guice to use this instance.
                    binder.bind(VOID_INJECTABLE).toInstance(mockInjectable);
                })
                .getInstance(VOID_INJECTABLE);
        // Throws:
        // com.google.inject.CreationException: Unable to create injector, see the following errors:
        //
        // 1) No implementation for java.util.List was bound.
        //   Did you mean?
        //     java.util.List<java.lang.String> bound  at
        // bug.GuiceRepro.lambda$testWithMockInstance$1(GuiceRepro.java:78)
        //
        //   while locating java.util.List
        //     for the 1st parameter of
        // bug.GuiceRepro$Injectable$MockitoMock$1394068293.setStrings(Unknown Source)
        //   at bug.GuiceRepro$Injectable$MockitoMock$1394068293.setStrings(Unknown Source)
        //   at bug.GuiceRepro.lambda$testWithMockInstance$1(GuiceRepro.java:79)

        // If instead the object were created without issue we'd expect this to pass again:
        assertThat(injectable.strings).contains("Green");
    }
}
