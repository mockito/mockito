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

    public static class MultiLevelInjectionWithoutSuperClass {
        @Spy
        private DependencyOfDependency dep_of_dep;

        @Spy
        @InjectMocks
        private SecondLevelDependencyWithInjectMocks mid_level_dependency;

        @InjectMocks
        private TopLevelInjectee top_level_injectee;

        @Rule
        public MockitoRule rule = MockitoJUnit.rule();

        @Test
        public void should_inject_dependency() {
            assertThat(top_level_injectee).describedAs("'top_level_injectee' should be initialized").isNotNull();
            assertThat(mid_level_dependency).describedAs("'mid_level_dependency' should be initialized").isNotNull();
            assertThat(mid_level_dependency.independant_dep).describedAs("'mid_level_dependency.independant_dep' should be initialized").isNotNull();
            assertThat(top_level_injectee._2nd_level).describedAs("'_2nd_level' should be injected")
                                                     .isNotNull();
        }
    }

    static class EmptySuperClass {
    }

    public static class MultiLevelInjectionWithSuperClass extends EmptySuperClass {
        @Spy
        private DependencyOfDependency dep_of_dep;

        @Spy
        @InjectMocks
        private SecondLevelDependencyWithInjectMocks mid_level_dependency;

        @InjectMocks
        private TopLevelInjectee top_level_injectee;

        @Rule
        public MockitoRule rule = MockitoJUnit.rule();

        @Test
        public void should_inject_dependency() {
            assertThat(top_level_injectee).describedAs("'injectee' should be initialized").isNotNull();
            assertThat(mid_level_dependency).describedAs("'mid_level_dependency' should be initialized").isNotNull();
            assertThat(mid_level_dependency.independant_dep).describedAs("'mid_level_dependency.independant_dep' should be initialized").isNotNull();
            assertThat(top_level_injectee._2nd_level).describedAs("'_2nd_level' should be injected")
                                                     .isNotNull();
        }
    }

    public static class ThreeLevelInjection {
        @Spy
        private DependencyOfDependency dep_of_dep;

        @Spy
        @InjectMocks
        private ThirdLevelDependencyWithInjectMocks third_level;

        @Spy
        @InjectMocks
        private SecondLevelDependencyWithInjectMocks second_level;

        @InjectMocks
        private TopLevelInjectee top_level_injectee;

        @Rule
        public MockitoRule rule = MockitoJUnit.rule();

        @Test
        public void should_inject_dependency() {
            assertThat(top_level_injectee).describedAs("'top_level_injectee' should be initialized").isNotNull();
            assertThat(second_level).describedAs("'second_level' should be initialized").isNotNull();
            assertThat(second_level.independant_dep).describedAs("'second_level.independant_dep' should be initialized").isNotNull();
            assertThat(third_level).describedAs("'third_level' should be initialized").isNotNull();
            assertThat(third_level.independent_dep).describedAs("'third_level.independant_dep' should be initialized").isNotNull();
            assertThat(top_level_injectee._2nd_level).describedAs("'_2nd_level' should be injected")
                                                     .isNotNull();
        }
    }

    public static class FourLevelInjection {
        @Spy
        private DependencyOfDependency dep_of_dep;

        @Spy
        @InjectMocks
        private FourthLevelDependencyWithInjectMocks fourth_level;

        @Spy
        @InjectMocks
        private ThirdLevelDependencyWithInjectMocks third_level;

        @Spy
        @InjectMocks
        private SecondLevelDependencyWithInjectMocks second_level;

        @InjectMocks
        private TopLevelInjectee top_level_injectee;

        @Rule
        public MockitoRule rule = MockitoJUnit.rule();

        @Test
        public void should_inject_dependency() {
            assertThat(top_level_injectee).describedAs("'top_level_injectee' should be initialized").isNotNull();
            assertThat(second_level).describedAs("'second_level' should be initialized").isNotNull();
            assertThat(second_level.independant_dep).describedAs("'second_level.independant_dep' should be initialized").isNotNull();
            assertThat(third_level).describedAs("'third_level' should be initialized").isNotNull();
            assertThat(third_level.independent_dep).describedAs("'third_level.independant_dep' should be initialized").isNotNull();
            assertThat(fourth_level).describedAs("'fourth_level' should be initialized").isNotNull();
            assertThat(fourth_level.independent_dep).describedAs("'fourth_level.independant_dep' should be initialized").isNotNull();
            assertThat(top_level_injectee._2nd_level).describedAs("'_2nd_level' should be injected")
                                                     .isNotNull();
        }
    }


    static class DependencyOfDependency {
        
    }

    static class FourthLevelDependencyWithInjectMocks {
        DependencyOfDependency independent_dep;
    }

    static class ThirdLevelDependencyWithInjectMocks {
        DependencyOfDependency independent_dep;
    }

    static class SecondLevelDependencyWithInjectMocks {
        DependencyOfDependency independant_dep;
        ThirdLevelDependencyWithInjectMocks _3rd_level;
    }

    static class TopLevelInjectee {
        SecondLevelDependencyWithInjectMocks _2nd_level;
    }
}
