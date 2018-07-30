/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.listeners;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Stubbing;
import org.mockitoutil.TestBase;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.util.Lists.emptyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.listeners.StubbingLookupNotifier.notifyStubbedAnswerLookup;

public class StubbingLookupNotifierTest extends TestBase {

    Invocation invocation = mock(Invocation.class);
    Stubbing stubbingFound = mock(Stubbing.class);
    Collection<Stubbing> allStubbings = mock(Collection.class);
    CreationSettings creationSettings = mock(CreationSettings.class);

    @Test
    public void does_not_do_anything_when_list_is_empty() {
        // given
        doReturn(emptyList()).when(creationSettings).getStubbingLookupListeners();

        // when
        notifyStubbedAnswerLookup(invocation, stubbingFound, allStubbings, creationSettings);

        // then expect nothing to happen
    }

    @Test
    public void call_on_stubbing_lookup_method_of_listeners_with_correct_event() {
        // given
        StubbingLookupListener listener1 = mock(StubbingLookupListener.class);
        StubbingLookupListener listener2 = mock(StubbingLookupListener.class);
        List<StubbingLookupListener> listeners = Lists.newArrayList(listener1, listener2);
        doReturn(listeners).when(creationSettings).getStubbingLookupListeners();

        // when
        notifyStubbedAnswerLookup(invocation, stubbingFound, allStubbings, creationSettings);

        // then
        verify(listener1).onStubbingLookup(argThat(new EventArgumentMatcher()));
        verify(listener2).onStubbingLookup(argThat(new EventArgumentMatcher()));
    }

    class EventArgumentMatcher implements ArgumentMatcher<StubbingLookupNotifier.Event> {

        @Override
        public boolean matches(StubbingLookupNotifier.Event argument) {
            return invocation == argument.getInvocation() &&
                stubbingFound == argument.getStubbingFound() &&
                allStubbings == argument.getAllStubbings() &&
                creationSettings == argument.getMockSettings();
        }
    }
}
