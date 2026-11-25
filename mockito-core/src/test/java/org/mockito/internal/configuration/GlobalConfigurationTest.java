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
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockitoutil.ClassLoaders;

public class GlobalConfigurationTest {
    @Test
    public void returns_mockito_annotation_engine_of_Plugins_if_no_MockitoConfiguration()
            throws Exception {
        ClassLoader isolatedLoader =
                ClassLoaders.isolatedClassLoader()
                        .withCurrentCodeSourceUrls()
                        .withCodeSourceUrlOf(Mockito.class, ByteBuddy.class, Assertions.class)
                        .withPrivateCopyOf(
                                "org.mockito",
                                "net.bytebuddy",
                                "org.mockitoutil",
                                "org.assertj.core")
                        .without("org.mockito.configuration.MockitoConfiguration")
                        .build();

        ClassLoaders.using(isolatedLoader)
                .execute(
                        () -> {
                            assertThat(new GlobalConfiguration().tryGetPluginAnnotationEngine())
                                    .isNotNull()
                                    .isSameAs(Plugins.getAnnotationEngine());
                            return null;
                        });
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
