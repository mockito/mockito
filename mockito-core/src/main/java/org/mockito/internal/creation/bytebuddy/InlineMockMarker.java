/*
 * Copyright (c) 2025 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.description.annotation.AnnotationDescription;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface InlineMockMarker {

    AnnotationDescription DESCRIPTION =
            AnnotationDescription.Builder.ofType(InlineMockMarker.class).build();
}
