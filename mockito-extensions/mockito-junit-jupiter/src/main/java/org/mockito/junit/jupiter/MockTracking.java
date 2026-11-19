/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.junit.jupiter;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

/**
 * Controls which mocks the {@link MockitoExtension} attaches to the {@link org.mockito.MockitoSession}
 * for strictness reporting (e.g. unused stubbings, strict stubs).
 *
 * @see MockitoSettings#mockTracking()
 * @since 5.21.0
 */
public enum MockTracking {

    /**
     * Default: only mocks created from {@link Mock @Mock}, {@link Spy @Spy} annotated fields and
     * parameters are tracked by the session.
     * <p>
     * Mocks instantiated manually (e.g. via {@link Mockito#mock(Class)}) are <strong>not</strong>
     * registered with the session. This matches Mockito's historical behavior.
     */
    ANNOTATED,

    /**
     * In addition to {@link #ANNOTATED}, the extension scans the declared fields of every test
     * instance (and its superclasses up to {@link Object}) after annotation processing. Any
     * non-annotated field whose value is a Mockito mock is registered with the session so that
     * strictness rules (such as detecting unused stubbings) apply to it as well.
     * <p>
     * Use this mode when your tests assign mocks to {@code final} fields via
     * {@link Mockito#mock(Class)} or {@link Mockito#mock()} and you still want the session to
     * detect over-stubbing on them.
     */
    ANNOTATED_AND_INSTANTIATED
}
