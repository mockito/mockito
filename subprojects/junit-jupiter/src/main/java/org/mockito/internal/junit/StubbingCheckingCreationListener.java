package org.mockito.internal.junit;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.mockito.invocation.Invocation;
import org.mockito.listeners.MockCreationListener;
import org.mockito.mock.MockCreationSettings;
import org.mockito.quality.Strictness;

//TODO needs a better name
public class StubbingCheckingCreationListener implements MockCreationListener {
    private final Map<String, Map<Object, MockCreationSettings>> mockSettingsByTestByInstance = new HashMap<>();
    private final Map<String, Strictness> strictnessByTest = new HashMap<>();
    private final Map<String, DefaultStubbingLookupListener> stubbingListeners = new HashMap<>();

    private String testKey = null;

    @Override
    public void onMockCreated(Object mock, MockCreationSettings settings) {
        if (!settings.isLenient()) {
            mockSettingsByTestByInstance.computeIfAbsent(testKey, k -> new IdentityHashMap<>()).put(mock, settings);
        }
        stubbingListeners.computeIfAbsent(testKey, key -> new DefaultStubbingLookupListener(strictnessByTest.getOrDefault(key, Strictness.STRICT_STUBS)));
    }

    public void strictnessForTest(final String testKey, final Strictness currentStrictness) {
        this.testKey = testKey;
        strictnessByTest.put(testKey, currentStrictness);
    }

    public void checkStubbing(Class<?> testClass) {
        Set<Object> mocks = new HashSet<>();

        for (Map<Object, MockCreationSettings> testMocks : mockSettingsByTestByInstance.values()) {
            mocks.addAll(testMocks.keySet());
        }

        final UnusedStubbingsFinder unusedStubbingsFinder = new UnusedStubbingsFinder();
        Collection<Invocation> unusedStubbingsByLocation = unusedStubbingsFinder.getUnusedStubbingsByLocation(mocks);
        final Set<DefaultStubbingLookupListener> unreportedStubbingProblems = stubbingListeners.values().stream().filter(defaultStubbingLookupListener -> !defaultStubbingLookupListener.isMismatchesReported()).collect(Collectors.toSet());

        if (unusedStubbingsByLocation.isEmpty() && unreportedStubbingProblems.isEmpty()) {
            return;
        } else {
            new UnusedStubbings(Collections.emptyList()).reportUnused(unusedStubbingsByLocation, testClass);
        }
    }

    public void checkStubbing(Class<?> onClass, String forKey) {

        final UnusedStubbingsFinder unusedStubbingsFinder = new UnusedStubbingsFinder();
        final Map<Object, MockCreationSettings> creationSettingsMap = mockSettingsByTestByInstance.computeIfAbsent(forKey, k -> Collections.emptyMap());

        final Set<Object> mocks = creationSettingsMap.keySet();
        Collection<Invocation> unusedStubbingsByLocationPerTest;
        final Strictness strictness = strictnessByTest.get(forKey);
        switch (strictness) {
            case STRICT_STUBS:
                unusedStubbingsByLocationPerTest = unusedStubbingsFinder.getUnusedStubbingsByLocation(mocks);
                if (!unusedStubbingsByLocationPerTest.isEmpty() && !stubbingListeners.get(forKey).isMismatchesReported()) {
                    mockSettingsByTestByInstance.remove(forKey); //Remove so we don't re-report
                    new UnusedStubbings(Collections.emptyList()).reportUnused(unusedStubbingsByLocationPerTest, onClass);
                }
                break;
            case WARN:
                //TODO support warn need to work out logger etc
//                        unusedStubbingsByLocationPerTest = unusedStubbingsFinder.getUnusedStubbingsByLocation(stringMapEntry.getValue().keySet());
//                        unusedStubbingsFinder.format(stringMapEntry.getKey(), logger);
                break;
            case LENIENT:
                break;
            default:
                throw new IllegalStateException("Unknown strictness: " + strictness);

        }

    }
}

