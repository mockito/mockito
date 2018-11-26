/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.listeners;

import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.invocation.Invocation;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Stubbing;

import java.util.Collection;
import java.util.List;

public class StubbingLookupNotifier {

    public static void notifyStubbedAnswerLookup(Invocation invocation, Stubbing stubbingFound,
                                                 Collection<Stubbing> allStubbings, CreationSettings creationSettings) {
        List<StubbingLookupListener> listeners = creationSettings.getStubbingLookupListeners();
        if (listeners.isEmpty()) {
            return;
        }
        StubbingLookupEvent event = new Event(invocation, stubbingFound, allStubbings, creationSettings);
        for (StubbingLookupListener listener : listeners) {
            listener.onStubbingLookup(event);
        }
    }

    static class Event implements StubbingLookupEvent {
        final private Invocation invocation;
        final private Stubbing stubbing;
        final private Collection<Stubbing> allStubbings;
        final private MockCreationSettings mockSettings;

        public Event(Invocation invocation,
                     Stubbing stubbing,
                     Collection<Stubbing> allStubbings,
                     MockCreationSettings mockSettings) {
            this.invocation = invocation;
            this.stubbing = stubbing;
            this.allStubbings = allStubbings;
            this.mockSettings = mockSettings;
        }

        @Override
        public Invocation getInvocation() {
            return invocation;
        }

        @Override
        public Stubbing getStubbingFound() {
            return stubbing;
        }

        @Override
        public Collection<Stubbing> getAllStubbings() {
            return allStubbings;
        }

        @Override
        public MockCreationSettings getMockSettings() {
            return mockSettings;
        }
    }
}
