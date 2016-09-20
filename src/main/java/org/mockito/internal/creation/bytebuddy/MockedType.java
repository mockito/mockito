package org.mockito.internal.creation.bytebuddy;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MockedType {

    Class<?> type();
}
