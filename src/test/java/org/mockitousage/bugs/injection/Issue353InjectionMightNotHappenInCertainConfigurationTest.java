package org.mockitousage.bugs.injection;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertSame;

@RunWith(MockitoJUnitRunner.class)
public class Issue353InjectionMightNotHappenInCertainConfigurationTest {
    @Mock Map<String, String> stringString_that_matches_field;
    @Mock Map<String, Integer> mockStringInteger_was_not_injected;
    @InjectMocks FooService fooService;

    @Test
    public void when_identical_types_and_the_correct_mock_name_is_greater_than_the_non_matching_name_then_injection_occurs_only_on_the_named_one() {
        assertThat("stringString_that_matches_field".compareTo("mockStringInteger_was_not_injected")).isGreaterThanOrEqualTo(1);

        assertSame(stringString_that_matches_field, fooService.stringString_that_matches_field);
        assertSame(mockStringInteger_was_not_injected, fooService.stringInteger_field);
    }

    public static class FooService {
        Map<String, Integer> stringInteger_field = new HashMap<String, Integer>();
        Map<String, String> stringString_that_matches_field = new HashMap<String, String>();
    }

}
