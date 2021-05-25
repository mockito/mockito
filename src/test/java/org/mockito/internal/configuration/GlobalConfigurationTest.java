/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import net.bytebuddy.ByteBuddy;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockitoutil.ClassLoaders;
import org.objenesis.Objenesis;

public class GlobalConfigurationTest {
    @Test
    public void returns_mockito_configuration_annotation_engine_if_non_default() throws Exception {
        ConfigurationAccess.getConfig().overrideAnnotationEngine(new CustomAnnotationEngine());
        assertThat(new GlobalConfiguration().getAnnotationEngine())
                .isInstanceOf(CustomAnnotationEngine.class);
        assertThat(new GlobalConfiguration().tryGetPluginAnnotationEngine())
                .isInstanceOf(CustomAnnotationEngine.class);
    }

    @Test
    public void returns_mockito_annotation_engine_of_Plugins_if_no_MockitoConfiguration()
            throws Throwable {
        ClassLoader anotherWorld =
                ClassLoaders.isolatedClassLoader()
                        .withCurrentCodeSourceUrls()
                        .withCodeSourceUrlOf(Mockito.class, ByteBuddy.class, Objenesis.class)
                        .withPrivateCopyOf("org.mockito", "net.bytebuddy", "org.objenesis")
                        .withCodeSourceUrlOf(Assertions.class)
                        .withPrivateCopyOf("org.assertj")
                        .without("org.mockito.configuration.MockitoConfiguration")
                        .build();

        ClassLoaders.using(anotherWorld)
                .execute(
                        new Runnable() {
                            @Override
                            public void run() {
                                assertThat(new GlobalConfiguration().getAnnotationEngine())
                                        .isInstanceOf(Plugins.getAnnotationEngine().getClass());
                                assertThat(new GlobalConfiguration().tryGetPluginAnnotationEngine())
                                        .isInstanceOf(Plugins.getAnnotationEngine().getClass());
                            }
                        });
    }

    @After
    public void reset_annotation_engine() {
        ConfigurationAccess.getConfig().overrideAnnotationEngine(null);
    }

    private static class CustomAnnotationEngine implements AnnotationEngine {
        @Override
        public AutoCloseable process(Class<?> clazz, Object testInstance) {
            return new NoAction();
        }
    }
}
