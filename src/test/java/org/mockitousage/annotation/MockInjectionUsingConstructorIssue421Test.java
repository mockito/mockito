/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.annotation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.MockUtil;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockitousage.examples.use.ArticleCalculator;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class MockInjectionUsingConstructorIssue421Test {

    @Mock private ArticleCalculator calculator;

    @InjectMocks private Issue421 issue421;

    @Test
    public void mockJustWorks() {
        issue421.checkIfMockIsInjected();
    }

    static class Issue421 {

        private ArticleCalculator calculator;

        public Issue421(int a) {
        }

        public Issue421(ArticleCalculator calculator) {
            this.calculator = calculator;
        }

        public void checkIfMockIsInjected(){
            assertThat(MockUtil.isMock(calculator)).isTrue();
        }
    }

}
