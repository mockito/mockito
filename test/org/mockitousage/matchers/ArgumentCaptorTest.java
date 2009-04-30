/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.matchers;

import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Test;
import org.mockito.Argument;
import org.mockito.exceptions.base.MockitoException;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class ArgumentCaptorTest extends TestBase {

    class Person {

        private final Integer age;

        public Person(Integer age) {
            this.age = age;
        }

        public int getAge() {
            return age;
        }
    }
    
    class Emailer {
     
        private EmailService service;
        
        public Emailer(EmailService service) {
            this.service = service;
        }

        public void email(Integer ... personId) {
            for (Integer i : personId) {
                Person person = new Person(i);
                service.sendEmailTo(person);
            }
        }
    }
    
    interface EmailService {
        void sendEmailTo(Person person);
    }

    EmailService emailService = mock(EmailService.class);
    Emailer emailer = new Emailer(emailService);

    @Test
    public void shouldAllowAssertionsOnCapturedArgument() {
        //when
        emailer.email(12);
        
        //then
        Argument<Person> argument = new Argument<Person>();
        verify(emailService).sendEmailTo(argument.capture());
        
        assertEquals(12, argument.value().getAge());
    }
    
    @Test
    public void shouldAllowAssertionsOnAllCapturedArguments() {
        //when
        emailer.email(11, 12);
        
        //then
        Argument<Person> argument = new Argument<Person>();
        verify(emailService, atLeastOnce()).sendEmailTo(argument.capture());
        List<Person> allValues = argument.allValues();
        
        assertEquals(11, allValues.get(0).getAge());
        assertEquals(12, allValues.get(1).getAge());
    }
    
    @Test
    public void shouldAllowAssertionsOnLastArgument() {
        //when
        emailer.email(11, 12, 13);
        
        //then
        Argument<Person> argument = new Argument<Person>();
        verify(emailService, atLeastOnce()).sendEmailTo(argument.capture());
        
        assertEquals(13, argument.value().getAge());
    }
    
    @Test
    public void shouldAllowAssertionsOnCapturedNull() {
        //when
        emailService.sendEmailTo(null);
        
        //then
        Argument<Person> argument = new Argument<Person>();
        verify(emailService).sendEmailTo(argument.capture());
        assertEquals(null, argument.value());
    }
    
    @Test
    public void shouldSaySomethingSmartWhenMisused() {
        Argument<Person> argument = new Argument<Person>();
        try {
            argument.value();
            fail();
        } catch (MockitoException e) {
            assertContains("Argument value not yet captured", e.getMessage());
        }
    }
}