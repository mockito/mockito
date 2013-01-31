package org.mockito.rules.it;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.rules.annotations.ComponentUnderTest;

public class ComponentUnderTestRuleIntegrationTest extends TestBase {

    @ComponentUnderTest
    private TestComponent testComponent;

    @Test
    public void shouldCreateMocksAndStubMethod() {
        // given
        given(testComponent.returnDependency.bar()).willReturn("mock");
        final int invocations = 10;

        // when
        final String foo = testComponent.foo(invocations);

        // then
        assertThat(foo).isEqualTo("mock");
        verify(testComponent.countingDependency, times(invocations)).bar();
    }

}
