/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stubbing;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Test;
import org.mockito.Answers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

//Reproduces issue #1551
public class SmartNullsGenericBugTest {

    @Test
    public void smart_nulls_generic_bug() {
        final ConcreteDao concreteDao = mock(ConcreteDao.class, withSettings().defaultAnswer(Answers.RETURNS_SMART_NULLS));

        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            public void call() {
                concreteDao.findById();
            }
        }).isInstanceOf(ClassCastException.class);
        //TODO: can we avoid CCE here? Can we make the exception message better? See issue #1551
    }

    static class AbstractDao<T> {
        T findById() {
            return null;
        }
    }

    static class Entity { }
    static class ConcreteDao extends AbstractDao<Entity> { }
}
