/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitointegration;

import static org.mockito.Mockito.withSettings;
import static org.mockitoutil.ClassLoaders.coverageTool;

import java.lang.reflect.Method;

import org.assertj.core.api.Assertions;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.bytebuddy.InlineByteBuddyMockMaker;
import org.mockito.internal.creation.bytebuddy.SubclassByteBuddyMockMaker;
import org.mockito.internal.creation.proxy.ProxyMockMaker;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockMaker;
import org.mockitoutil.ClassLoaders;

public class DeferMockMakersClassLoadingTest {
    private static final Object MY_MOCK = new Object();

    @Test
    public void mockito_should_not_load_mock_makers_it_does_not_need() throws Exception {
        ClassLoader classLoader_without_mockMakers =
                ClassLoaders.excludingClassLoader()
                        .withCodeSourceUrlOf(
                                Mockito.class,
                                Matcher.class,
                                CustomMockMaker.class,
                                Assertions.class)
                        .withCodeSourceUrlOf(coverageTool())
                        .without(
                                ByteBuddyMockMaker.class.getName(),
                                SubclassByteBuddyMockMaker.class.getName(),
                                InlineByteBuddyMockMaker.class.getName(),
                                ProxyMockMaker.class.getName())
                        .build();

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader_without_mockMakers);
        try {
            Class<?> self = classLoader_without_mockMakers.loadClass(getClass().getName());
            Method createMock = self.getMethod("createMock");
            createMock.invoke(null);
        } finally {
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }

    // Called by reflection from the test method
    public static void createMock() {
        Assertions.assertThat(
                        Mockito.mock(
                                Object.class,
                                withSettings().mockMaker(CustomMockMaker.class.getName())))
                .isSameAs(MY_MOCK);
    }

    public static class CustomMockMaker implements MockMaker {
        @Override
        public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
            return settings.getTypeToMock().cast(MY_MOCK);
        }

        @Override
        public MockHandler getHandler(Object mock) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void resetMock(Object mock, MockHandler newHandler, MockCreationSettings settings) {
            throw new UnsupportedOperationException();
        }

        @Override
        public TypeMockability isTypeMockable(Class<?> type) {
            return new TypeMockability() {
                @Override
                public boolean mockable() {
                    return type.equals(Object.class);
                }

                @Override
                public String nonMockableReason() {
                    return mockable() ? "" : "type != Object.class";
                }
            };
        }
    }
}
