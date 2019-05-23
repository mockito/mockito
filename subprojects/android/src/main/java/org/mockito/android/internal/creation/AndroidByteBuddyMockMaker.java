/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.android.internal.creation;

import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.creation.bytebuddy.SubclassByteBuddyMockMaker;
import org.mockito.internal.util.Platform;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockMaker;

import static org.mockito.internal.util.StringUtil.join;

public class AndroidByteBuddyMockMaker implements MockMaker {

    private final MockMaker delegate;

    public AndroidByteBuddyMockMaker() {
        if (Platform.isAndroid() || Platform.isAndroidMockMakerRequired()) {
            delegate = new SubclassByteBuddyMockMaker(new AndroidLoadingStrategy());
        } else {
            Plugins.getMockitoLogger().log(join(
                    "IMPORTANT NOTE FROM MOCKITO:",
                    "",
                    "You included the 'mockito-android' dependency in a non-Android environment.",
                    "The Android mock maker was disabled. You should only include the latter in your 'androidTestCompile' configuration",
                    "If disabling was a mistake, you can set the 'org.mockito.mock.android' property to 'true' to override this detection.",
                    "",
                    "Visit https://javadoc.io/page/org.mockito/mockito-core/latest/org/mockito/Mockito.html#0.1 for more information"
            ));
            delegate = new SubclassByteBuddyMockMaker();
        }
    }

    @Override
    public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
        return delegate.createMock(settings, handler);
    }

    @Override
    public MockHandler getHandler(Object mock) {
        return delegate.getHandler(mock);
    }

    @Override
    public void resetMock(Object mock, MockHandler newHandler, MockCreationSettings settings) {
        delegate.resetMock(mock, newHandler, settings);
    }

    @Override
    public TypeMockability isTypeMockable(Class<?> type) {
        return delegate.isTypeMockable(type);
    }
}
