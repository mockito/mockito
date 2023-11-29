/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

class ToBeMockedInTestSuperClass {
    int identifier;

    public ToBeMockedInTestSuperClass(int identifier) {
        this.identifier = identifier;
    }
}

class ToBeMocked {
    int identifier;

    public ToBeMocked(int identifier) {
        this.identifier = identifier;
    }
}

class TestClassToBeInitiatedViaConstructorInSuperClass {
    ToBeMockedInTestSuperClass toBeMockedInTestSuperClass;

    public TestClassToBeInitiatedViaConstructorInSuperClass(
            ToBeMockedInTestSuperClass toBeMockedInTestSuperClass) {
        assertThat(toBeMockedInTestSuperClass).isNotNull();
        this.toBeMockedInTestSuperClass = new ToBeMockedInTestSuperClass(42);
    }
}

class TestClassToBeInitiatedViaConstructor {
    ToBeMockedInTestSuperClass toBeMockedInTestSuperClass;
    ToBeMocked toBeMocked;

    public TestClassToBeInitiatedViaConstructor(
            ToBeMocked toBeMocked, ToBeMockedInTestSuperClass toBeMockedInTestSuperClass) {
        assertThat(toBeMocked).isNotNull();
        assertThat(toBeMockedInTestSuperClass).isNotNull();
        this.toBeMocked = new ToBeMocked(42);
        this.toBeMockedInTestSuperClass = new ToBeMockedInTestSuperClass(42);
    }
}

class SuperTestClass {
    @Mock ToBeMockedInTestSuperClass toBeMockedInTestSuperClass;

    @InjectMocks
    TestClassToBeInitiatedViaConstructorInSuperClass
            testClassToBeInitiatedViaConstructorInSuperClass;
}

@ExtendWith(MockitoExtension.class)
class InjectMocksTest extends SuperTestClass {

    @Mock ToBeMocked toBeMocked;

    @InjectMocks TestClassToBeInitiatedViaConstructor testClassToBeInitiatedViaConstructor;

    /**
     * Checks that {@link #testClassToBeInitiatedViaConstructor} holds instances that have identifier 42.
     * It being 42 is proof that constructor injection was used over field injection.
     */
    @Test
    void
            given_instanceToBeInitializedByMockito_when_mocksRequestedByConstructorAreInTestAndSuperClass_should_useConstructorInjection() {
        assertThat(testClassToBeInitiatedViaConstructor)
                .extracting(
                        testInstance -> testInstance.toBeMocked.identifier,
                        testInstance -> testInstance.toBeMockedInTestSuperClass.identifier)
                .containsExactly(42, 42);
    }

    /**
     * Checks that {@link #testClassToBeInitiatedViaConstructorInSuperClass} holds instances that have identifier 42.
     * It being 42 is proof that constructor injection was used over field injection.
     */
    @Test
    public void
            given_instanceInSuperClassToBeInitializedByMockito_when_mocksRequestedAreInSuperClass_should_useConstructorInjection() {
        assertThat(testClassToBeInitiatedViaConstructorInSuperClass)
                .extracting(
                        yetAnotherClas1 -> yetAnotherClas1.toBeMockedInTestSuperClass.identifier)
                .isEqualTo(42);
    }
}
