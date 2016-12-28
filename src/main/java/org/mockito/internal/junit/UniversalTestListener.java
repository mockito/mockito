package org.mockito.internal.junit;

import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.util.MockitoLogger;
import org.mockito.mock.MockCreationSettings;
import org.mockito.quality.Strictness;

import java.util.*;

/**
 * Universal test listener that behaves accordingly to current setting of strictness.
 * Will come handy when we offer tweaking strictness at the method level with annotation.
 * Should be relatively easy to improve and offer tweaking strictness per mock.
 */
class UniversalTestListener implements MockitoTestListener {

    private Strictness currentStrictness;
    private final MockitoLogger logger;

    private final Map<Object, MockCreationSettings> mocks = new IdentityHashMap<Object, MockCreationSettings>();
    private DefaultStubbingLookupListener stubbingLookupListener;

    UniversalTestListener(Strictness initialStrictness, MockitoLogger logger) {
        this.currentStrictness = initialStrictness;
        this.logger = logger;

        //creating single stubbing lookup listener per junit rule instance / test method
        this.stubbingLookupListener = new DefaultStubbingLookupListener(currentStrictness);
    }

    @Override
    public void testFinished(TestFinishedEvent event) {
        switch (currentStrictness) {
            case WARN: emitWarnings(logger, event, mocks.keySet()); break;
            case STRICT_STUBS: reportUnusedStubs(event, mocks.keySet()); break;
            case LENIENT: break;
            default: throw new IllegalStateException("Unknown strictness: " + currentStrictness);
        }
    }

    private static void reportUnusedStubs(TestFinishedEvent event, Collection<Object> mocks) {
        if (event.getFailure() == null) {
            //Detect unused stubbings:
            UnusedStubbings unused = new UnusedStubbingsFinder().getUnusedStubbings(mocks);
            unused.reportUnused();
        }
    }

    private static void emitWarnings(MockitoLogger logger, TestFinishedEvent event, Collection<Object> mocks) {
        String testName = event.getTestClassInstance().getClass().getSimpleName() + "." + event.getTestMethodName();
        if (event.getFailure() != null) {
            //print stubbing mismatches only when there is a test failure
            //to avoid false negatives. Give hint only when test fails.
            new ArgMismatchFinder().getStubbingArgMismatches(mocks).format(testName, logger);
        } else {
            //print unused stubbings only when test succeeds to avoid reporting multiple problems and confusing users
            new UnusedStubbingsFinder().getUnusedStubbings(mocks).format(testName, logger);
        }
    }

    @Override
    public void onMockCreated(Object mock, MockCreationSettings settings) {
        this.mocks.put(mock, settings);

        //It is not ideal that we modify the state of MockCreationSettings object
        //MockCreationSettings is intended to be an immutable view of the creation settings
        //In future, we should start passing MockSettings object to the creation listener
        //TODO #793 - when completed, we should be able to get rid of the CreationSettings casting below
        ((CreationSettings) settings).getStubbingLookupListeners().add(stubbingLookupListener);

        //TODO #840 - we should remove stubbing lookup listeners after we're done. Otherwise we risk leaks.
    }

    public void setStrictness(Strictness strictness) {
        this.currentStrictness = strictness;
        this.stubbingLookupListener.currentStrictness = strictness;
    }
}