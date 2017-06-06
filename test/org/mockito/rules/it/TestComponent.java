package org.mockito.rules.it;

class TestComponent {

    @TestAutowired
    TestDependency returnDependency;

    @TestAutowired
    TestDependency countingDependency;

    public String foo(final int times) {
        for (int i = 0; i < times; ++i) {
            countingDependency.bar();
        }

        return returnDependency.bar();
    }
}
