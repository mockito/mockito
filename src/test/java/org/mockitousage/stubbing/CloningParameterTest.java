/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.stubbing;

import org.junit.Test;
import org.mockito.internal.stubbing.answers.ClonesArguments;
import org.mockitoutil.TestBase;

import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.*;

public class CloningParameterTest extends TestBase {

    @Test
    public void shouldVerifyEvenIfArgumentsWereMutated() throws Exception {

        // given
        EmailSender emailSender = mock(EmailSender.class, new ClonesArguments());

        // when
        businessLogic(emailSender);

        // then
        verify(emailSender).sendEmail(1, new Person("Wes"));
    }

    private void businessLogic(EmailSender emailSender) {
        Person person = new Person("Wes");
        emailSender.sendEmail(1, person);
        person.emailSent();
    }

    @Test
    public void shouldReturnDefaultValueWithCloningAnswer() throws Exception {

        // given
        EmailSender emailSender = mock(EmailSender.class, new ClonesArguments());
        when(emailSender.getAllEmails(new Person("Wes"))).thenAnswer(new ClonesArguments());

        // when
        List<?> emails = emailSender.getAllEmails(new Person("Wes"));

        // then
        assertNotNull(emails);
    }

    public class Person {

        private final String name;
        private boolean emailSent;

        public Person(String name) {
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
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Person other = (Person) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (emailSent != other.emailSent)
                return false;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            return true;
        }

        private CloningParameterTest getOuterType() {
            return CloningParameterTest.this;
        }

    }

    public interface EmailSender {

        void sendEmail(int i, Person person);

        List<?> getAllEmails(Person person);

    }
}