/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.util.reflection;

import java.lang.annotation.Retention;
import java.lang.reflect.Field;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.assertj.core.api.Assertions.assertThat;


@SuppressWarnings("unchecked")
public class AnnotationsTest {

    @WorksAlone
    @DoNotWorkWithWorksAlone
    Object with_WorksAlone_and_DoNotWorkWithWorksAlone_annotations;

    @WorksAlone
    Object with_WorksAlone_annotations;

    @Test
    public void should_report_unsupported_combination_of_annotation() {
        try {
            Annotations.assertNoIncompatibleAnnotations(WorksAlone.class,
                                                        field("with_WorksAlone_and_DoNotWorkWithWorksAlone_annotations"),
                                                        DoNotWorkWithWorksAlone.class);
            Assertions.fail("should have raised a misuse");
        } catch (Exception e) {
            assertThat(e).hasMessageContaining("This combination of annotations is not permitted on the field")
                         .hasMessageContaining("with_WorksAlone_and_DoNotWorkWithWorksAlone_annotations")
                         .hasMessageContaining(WorksAlone.class.getSimpleName())
                         .hasMessageContaining(DoNotWorkWithWorksAlone.class.getSimpleName());
        }
    }

    @Test
    public void should_not_report_unsupported_combination_of_annotation_when_annotations_not_present() {
        Annotations.assertNoIncompatibleAnnotations(WorksAlone.class,
                                                    field("with_WorksAlone_annotations"),
                                                    DoNotWorkWithWorksAlone.class);
        // noting raised
    }
    
    private Field field(String fieldName) {
        try {
            return this.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Retention(RUNTIME)
    private @interface WorksAlone {
    }

    @Retention(RUNTIME)
    private @interface DoNotWorkWithWorksAlone {
    }
}
