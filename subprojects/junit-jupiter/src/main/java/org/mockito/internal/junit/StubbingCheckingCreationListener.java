package org.mockito.internal.junit;

import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import org.mockito.invocation.Invocation;
import org.mockito.listeners.MockCreationListener;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Stubbing;

//TODO needs a better name
public class StubbingCheckingCreationListener implements MockCreationListener {
    private final Map<Object, MockCreationSettings> mockSettingsByInstance = new IdentityHashMap<>();
    private DefaultStubbingLookupListener stubbingLookupListener;

    @Override
    public void onMockCreated(Object mock, MockCreationSettings settings) {
        if(!settings.isLenient()) {
            mockSettingsByInstance.put(mock, settings);
        }
    }

    public void checkStubbing(Class<?> testClass) {
        Collection<Invocation> unusedStubbingsByLocation = new UnusedStubbingsFinder().getUnusedStubbingsByLocation(mockSettingsByInstance.keySet());
        if(!unusedStubbingsByLocation.isEmpty()) {
            new UnusedStubbings(Collections.emptyList()).reportUnused(unusedStubbingsByLocation, testClass);
        }

    }
}
