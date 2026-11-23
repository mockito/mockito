/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import net.bytebuddy.ByteBuddy;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockitoutil.ClassLoaders;

public class GlobalConfigurationTest {
    @Test
    public void returns_mockito_annotation_engine_of_Plugins_if_no_MockitoConfiguration()
            throws Exception {
        ClassLoader isolatedLoader =
                ClassLoaders.isolatedClassLoader()
                        .withCodeSourceUrlOf(Mockito.class, ByteBuddy.class)
                        .withPrivateCopyOf("org.mockito", "net.bytebuddy")
                        .without("org.mockito.configuration.MockitoConfiguration")
                        .build();

        Thread thread = Thread.currentThread();
        ClassLoader previousLoader = thread.getContextClassLoader();
        thread.setContextClassLoader(isolatedLoader);
        try {
            Class<?> configuration = isolatedLoader.loadClass(GlobalConfiguration.class.getName());
            Class<?> plugins = isolatedLoader.loadClass(Plugins.class.getName());

            Object engine =
                    configuration
                            .getMethod("tryGetPluginAnnotationEngine")
                            .invoke(configuration.getConstructor().newInstance());
            assertThat(engine)
                    .isNotNull()
                    .isSameAs(plugins.getMethod("getAnnotationEngine").invoke(null));
        } finally {
            thread.setContextClassLoader(previousLoader);
        }
    }

    @After
    public void reset_annotation_engine() {
        ConfigurationAccess.getConfig().overrideAnnotationEngine(null);
    }

    private static class CustomAnnotationEngine implements org.mockito.plugins.AnnotationEngine {
        @Override
        public AutoCloseable process(Class<?> clazz, Object testInstance) {
            return new NoAction();
        }
    }
}
