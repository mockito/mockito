/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.stubbing;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.mockito.internal.stubbing.answers.ClonesArguments;
import org.mockitoutil.TestBase;

public class CloningParameterTest extends TestBase {

    @Test
    public void shouldVerifyEvenIfArgumentsWereMutated() throws Exception {

        // given
        final EmailSender emailSender = mock(EmailSender.class, new ClonesArguments());

        // when
        businessLogic(emailSender);

        // then
        verify(emailSender).sendEmail(1, new Person("Wes"));
    }

    private void businessLogic(final EmailSender emailSender) {
        final Person person = new Person("Wes");
        emailSender.sendEmail(1, person);
        person.emailSent();
    }

    @Test
    public void shouldReturnDefaultValueWithCloningAnswer() throws Exception {

        // given
        final EmailSender emailSender = mock(EmailSender.class, new ClonesArguments());
        when(emailSender.getAllEmails(new Person("Wes"))).thenAnswer(new ClonesArguments());

        // when
        final List<?> emails = emailSender.getAllEmails(new Person("Wes"));

        // then
        assertNotNull(emails);
    }

    public class Person {

        private final String name;
        private boolean emailSent;

        public Person(final String name) {
            this.name = name;
        }

        public void emailSent() {
            emailSent = true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + (emailSent ? 1231 : 1237);
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Person other = (Person) obj;
            if (!getOuterType().equals(other.getOuterType())) {
                return false;
            }
            if (emailSent != other.emailSent) {
                return false;
            }
            if (name == null) {
                if (other.name != null) {
                    return false;
                }
            } else if (!name.equals(other.name)) {
                return false;
            }
            return true;
        }

        private CloningParameterTest getOuterType() {
            return CloningParameterTest.this;
        }

    }

    public interface EmailSender {

        void sendEmail(final int i, final Person person);

        List<?> getAllEmails(final Person person);

    }
}