package org.mockito.junit5;


import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.quality.Strictness;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.create;
import static org.mockito.internal.junit5.AnnotationUtil.retrieveStrictness;
import static org.mockito.quality.Strictness.WARN;

public class MockitoExtension implements BeforeEachCallback, AfterEachCallback {

    private final static Namespace MOCKITO = create("org.mockito");

    private final static String SESSION = "session";


    /**
     * Callback that is invoked <em>before</em> each test is invoked.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void beforeEach(final ExtensionContext context) {
        Object testInstance = context.getRequiredTestInstance();

        Strictness strictness = retrieveStrictness(context);

        List<Object> testInstances = new LinkedList<>();

        testInstances.add(testInstance);

        addParentTestInstances(context, testInstances);


        MockitoSession session = Mockito.mockitoSession()
            .initMocks(testInstances)
            .strictness(strictness)
            .startMocking();

        store(context, session);
    }

    private void addParentTestInstances(ExtensionContext context, List<Object> testInstances) {

        Object testInstance = context.getRequiredTestInstance();

        collectParentsReflective( testInstances, testInstance);

        // collectParentsViaContextParent(context, testInstances);
        // ^^ don't work the test instance of every parent context is null
    }

    /**
     * FIXME this doesn't work the test instance of every parent context is null
     * https://stackoverflow.com/questions/47014642/junit5-how-to-get-the-instance-of-parent-test-form-a-nested-test-class
     */
    private void collectParentsViaContextParent(ExtensionContext context, List<Object> testInstances) {
        Optional<ExtensionContext> parent = context.getParent();
        while (parent.isPresent() && parent.get() != context.getRoot()){
            ExtensionContext parentContext = parent.get();

            System.out.println(parentContext.getElement()+" "+parentContext.getTestInstance());

            Optional<Object> parentTestInstance = parentContext.getTestInstance();
            parentTestInstance.ifPresent(testInstances::add);

            parent=parentContext.getParent();
        }
    }

    /**
     * This is errorprone since a compiler can decide how to name the outer instance field of a nested class
     */
    @Deprecated
    private void collectParentsReflective(List<Object> testInstances, Object testInstance) {
        Object instance=testInstance;
        while(true) {
            Field outerInstanceField;
            try {
                outerInstanceField = instance.getClass().getDeclaredField("this$0");
            } catch (NoSuchFieldException e) {
                return;
            }

            outerInstanceField.setAccessible(true);
            Object outerInstance;
            try {
                outerInstance = outerInstanceField.get(instance);
            } catch (IllegalAccessException e) {
                return;
            }

            testInstances.add(outerInstance);
            instance = outerInstance;

        }

    }


    private static void store(ExtensionContext context, MockitoSession session) {
        context.getStore(MOCKITO).put(SESSION, session);
    }

    private static MockitoSession removeMockitoSession(ExtensionContext context) {
        return context.getStore(MOCKITO).remove(SESSION,MockitoSession.class);
    }

    /**
     * Callback that is invoked <em>after</em> each test has been invoked.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void afterEach(ExtensionContext context) {
        MockitoSession session;
        session = removeMockitoSession(context);
        session.finishMocking();
    }
}

