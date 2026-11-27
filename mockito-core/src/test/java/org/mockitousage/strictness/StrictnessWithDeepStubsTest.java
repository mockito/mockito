/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.strictness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockitoSession;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;
import static org.mockito.quality.Strictness.LENIENT;
import static org.mockito.quality.Strictness.STRICT_STUBS;
import static org.mockito.quality.Strictness.WARN;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoSession;
import org.mockito.exceptions.misusing.UnnecessaryStubbingException;
import org.mockito.internal.util.SimpleMockitoLogger;
import org.mockito.quality.Strictness;

public class StrictnessWithDeepStubsTest {

    @Mock(answer = RETURNS_DEEP_STUBS, lenient = true)
    Root annotatedLenientRoot;

    @Mock(answer = RETURNS_DEEP_STUBS, strictness = Mock.Strictness.LENIENT)
    Root annotatedLenientStrictnessRoot;

    private MockitoSession session;
    private final SimpleMockitoLogger logger = new SimpleMockitoLogger();

    @Before
    public void start_session() {
        session =
                mockitoSession()
                        .initMocks(this)
                        .strictness(STRICT_STUBS)
                        .logger(logger::log)
                        .startMocking();
    }

    @After
    public void clean_up_session() {
        if (session != null) {
            finish_session();
        }
    }

    @Test
    public void annotated_lenient_deep_stubs_do_not_report_unused_terminal_stubbing() {
        when(annotatedLenientRoot.child().grandchild().value()).thenReturn("unused");

        finish_session();
    }

    @Test
    public void annotated_lenient_strictness_applies_to_deep_stubs() {
        when(annotatedLenientStrictnessRoot.child().grandchild().value()).thenReturn("unused");

        finish_session();
    }

    @Test
    public void explicitly_lenient_deep_stubs_do_not_report_unused_terminal_stubbing() {
        Root root =
                mock(
                        Root.class,
                        withSettings().defaultAnswer(RETURNS_DEEP_STUBS).strictness(LENIENT));
        when(root.child().grandchild().value()).thenReturn("unused");

        finish_session();
    }

    @Test
    public void leniency_applies_to_unused_stubbings_on_both_child_levels() {
        Root root =
                mock(
                        Root.class,
                        withSettings().defaultAnswer(RETURNS_DEEP_STUBS).strictness(LENIENT));
        when(root.child().value()).thenReturn("unused child");
        when(root.child().grandchild().value()).thenReturn("unused grandchild");

        finish_session();

        assert_child_strictness(root, LENIENT);
    }

    @Test
    public void unset_strictness_preserves_session_unused_stubbing_detection() {
        Root root = mock(Root.class, RETURNS_DEEP_STUBS);
        when(root.child().grandchild().value()).thenReturn("unused");

        assertThat(mockingDetails(root).getMockCreationSettings().getStrictness()).isNull();
        assert_child_strictness(root, null);
        assertThatThrownBy(this::finish_session)
                .isInstanceOf(UnnecessaryStubbingException.class)
                .hasMessageContaining("1. ->")
                .hasMessageNotContaining("2. ->");
    }

    @Test
    public void explicit_strictness_preserves_unused_stubbing_detection() {
        Root root =
                mock(
                        Root.class,
                        withSettings().defaultAnswer(RETURNS_DEEP_STUBS).strictness(STRICT_STUBS));
        when(root.child().grandchild().value()).thenReturn("unused");

        assertThatThrownBy(this::finish_session)
                .isInstanceOf(UnnecessaryStubbingException.class)
                .hasMessageContaining("1. ->")
                .hasMessageNotContaining("2. ->");
        assert_child_strictness(root, STRICT_STUBS);
    }

    @Test
    public void used_terminal_stubbing_returns_value_without_unused_stubbing_failure() {
        Root root =
                mock(
                        Root.class,
                        withSettings().defaultAnswer(RETURNS_DEEP_STUBS).strictness(STRICT_STUBS));
        when(root.child().grandchild().value()).thenReturn("used");

        assertThat(root.child().grandchild().value()).isEqualTo("used");

        finish_session();
        logger.assertEmpty();
    }

    @Test
    public void warn_deep_stubs_report_unused_stubbing_without_throwing() {
        session.setStrictness(WARN);
        Root root =
                mock(Root.class, withSettings().defaultAnswer(RETURNS_DEEP_STUBS).strictness(WARN));
        when(root.child().grandchild().value()).thenReturn("unused");
        logger.assertEmpty();

        finish_session();

        assertThat(logger.getLoggedInfo())
                .contains("[MockitoHint]", "1. Unused")
                .doesNotContain("2. Unused");
        assert_child_strictness(root, WARN);
    }

    @Test
    public void stubbing_leniency_overrides_explicit_strictness_on_deep_stubs() {
        Root root =
                mock(
                        Root.class,
                        withSettings().defaultAnswer(RETURNS_DEEP_STUBS).strictness(STRICT_STUBS));
        lenient().when(root.child().grandchild().value()).thenReturn("unused");

        finish_session();
        logger.assertEmpty();
    }

    @Test
    public void stubbing_leniency_suppresses_warnings_for_warn_deep_stubs() {
        session.setStrictness(WARN);
        Root root =
                mock(Root.class, withSettings().defaultAnswer(RETURNS_DEEP_STUBS).strictness(WARN));
        lenient().when(root.child().grandchild().value()).thenReturn("unused");

        finish_session();
        logger.assertEmpty();
    }

    private void assert_child_strictness(Root root, Strictness strictness) {
        assertThat(mockingDetails(root.child()).getMockCreationSettings().getStrictness())
                .isEqualTo(strictness);
        assertThat(
                        mockingDetails(root.child().grandchild())
                                .getMockCreationSettings()
                                .getStrictness())
                .isEqualTo(strictness);
    }

    private void finish_session() {
        MockitoSession currentSession = session;
        session = null;
        currentSession.finishMocking();
    }

    interface Root {
        Child child();
    }

    interface Child {
        Grandchild grandchild();

        String value();
    }

    interface Grandchild {
        String value();
    }
}
