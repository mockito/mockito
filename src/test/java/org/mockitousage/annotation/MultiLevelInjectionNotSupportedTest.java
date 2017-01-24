package org.mockitousage.annotation;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Enclosed.class)
public class MultiLevelInjectionNotSupportedTest {

    public static class MultiLevelInjectionFailsWithoutSuperClass {
        @Spy
        private DependencyOfDependency dep_of_dep;

        @Spy
        @InjectMocks
        private MidLevelDependencyWithInjectMocks mid_level_dependency;

        @InjectMocks
        private TopLevelInjectee top_level_injectee;

        @Rule
        public MockitoRule rule = MockitoJUnit.rule();

        @Test
        public void should_inject_dependency() {
            assertThat(top_level_injectee).describedAs("'top_level_injectee' should be initialized").isNotNull();
            assertThat(mid_level_dependency).describedAs("'mid_level_dependency' should be initialized").isNotNull();
            assertThat(mid_level_dependency.dep_of_dep).describedAs("'mid_level_dependency' should be initialized").isNotNull();
            assertThat(top_level_injectee.dependency).describedAs("'dependency' can't be injected, as Mockito can't decide whether to spy or injectmocks first for 'mid_level_dependency'")
                                                     .isNull();
        }
    }

    static class EmptySuperClass {
    }

    public static class MultiLevelInjectionDoesntFailWithSuperClass extends EmptySuperClass {
        @Spy
        private DependencyOfDependency dep_of_dep;

        @Spy
        @InjectMocks
        private MidLevelDependencyWithInjectMocks mid_level_dependency;

        @InjectMocks
        private TopLevelInjectee top_level_injectee;

        @Rule
        public MockitoRule rule = MockitoJUnit.rule();

        @Test
        public void should_inject_dependency() {
            assertThat(top_level_injectee).describedAs("'injectee' should be initialized").isNotNull();
            assertThat(mid_level_dependency).describedAs("'mid_level_dependency' should be initialized").isNotNull();
            assertThat(mid_level_dependency.dep_of_dep).describedAs("'mid_level_dependency' should be initialized").isNotNull();
            assertThat(top_level_injectee.dependency).describedAs("'dependency' can't be injected, as Mockito can't decide whether to spy or injectmocks first for 'mid_level_dependency'")
                                                     .isNull();
        }
    }

    static class DependencyOfDependency {
        
    }

    static class MidLevelDependencyWithInjectMocks {
        DependencyOfDependency dep_of_dep;
    }

    static class TopLevelInjectee {
        MidLevelDependencyWithInjectMocks dependency;
    }
}
