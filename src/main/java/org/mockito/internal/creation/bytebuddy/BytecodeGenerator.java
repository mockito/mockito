/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.*;
import static net.bytebuddy.matcher.ElementMatchers.named;

public interface BytecodeGenerator {

    <T> Class<? extends T> mockClass(MockFeatures<T> features);

    void mockClassConstruction(Class<?> type);

    void mockClassStatic(Class<?> type);

    default void clearAllCaches() {}

    static ElementMatcher<MethodDescription> isGroovyMethod(boolean inline) {
        ElementMatcher.Junction<MethodDescription> matcher =
                isDeclaredBy(named("groovy.lang.GroovyObjectSupport"))
                        .or(isAnnotatedWith(named("groovy.transform.Internal")));
        if (inline) {
            return matcher.or(
                    named("$getStaticMetaClass").and(returns(named("groovy.lang.MetaClass"))));
        } else {
            return matcher;
        }
    }
}
