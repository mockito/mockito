/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.annotation;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertSame;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class CaptorAnnotationBasicTest extends TestBase {

    public class Person {
        private final String name;
        private final String surname;

        public Person(String name, String surname) {
            this.name = name;
            this.surname = surname;
        }

        public String getName() {
            return name;
        }
        
        public String getSurname() {
            return surname;
        }
    }

    public interface PeopleRepository {
        void save(Person capture);
    }
    
    @Mock PeopleRepository peopleRepository;           
    
    private void createPerson(String name, String surname) {
        peopleRepository.save(new Person(name, surname));
    }      
    
    @Test
    public void shouldUseCaptorInOrdinaryWay() {
        //when
        createPerson("Wes", "Williams");
        
        //then
        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        verify(peopleRepository).save(captor.capture());
        assertEquals("Wes", captor.getValue().getName());
        assertEquals("Williams", captor.getValue().getSurname());
    }
    
    @Captor ArgumentCaptor<Person> captor;
    
    @Test
    public void shouldUseAnnotatedCaptor() {
        //when
        createPerson("Wes", "Williams");
        
        //then
        verify(peopleRepository).save(captor.capture());
        assertEquals("Wes", captor.getValue().getName());
        assertEquals("Williams", captor.getValue().getSurname());
    }
        
    @SuppressWarnings("rawtypes")
    @Captor ArgumentCaptor genericLessCaptor;
    
    @Test
    public void shouldUseGenericlessAnnotatedCaptor() {
        //when
        createPerson("Wes", "Williams");
        
        //then
        verify(peopleRepository).save((Person) genericLessCaptor.capture());
        assertEquals("Wes", ((Person) genericLessCaptor.getValue()).getName());
        assertEquals("Williams", ((Person) genericLessCaptor.getValue()).getSurname());
    }  
    
    @Captor ArgumentCaptor<List<String>> genericListCaptor;
    @Mock IMethods mock;
    
    @Test
    public void shouldCaptureGenericList() {
        //given
        List<String> list = new LinkedList<String>();
        mock.listArgMethod(list);
                
        //when
        verify(mock).listArgMethod(genericListCaptor.capture());
        
        //then
        assertSame(list, genericListCaptor.getValue());
    } 
}