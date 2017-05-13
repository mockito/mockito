package org.mockitousage.stubbing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.junit.MockitoJUnit.rule;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.MockitoRule;
import org.mockitousage.IMethods;

public class VoidStubbingTest {

    @Rule
    public MockitoRule mockito = rule();

    @Mock
    private IMethods mock;

    private List<Integer> result;
    
    @Before
    public void init(){
        result= new ArrayList<>();
    }
    
    @Test
    public void instanceReferenceStubbing() {
        when(mock::voidMethod).then(() -> {
            result.add(1);
        });
        
        mock.voidMethod();
        
        assertThat(result).contains(1);
    }

    @Test
    public void consecutiveStubbing() throws Exception {
        when(() -> mock.intArgumentMethod(5))
        .then((Integer a) -> result.add(a))
        .then((Integer a) -> result.add(100 * a));

        mock.intArgumentMethod(5);
        mock.intArgumentMethod(10);
        mock.intArgumentMethod(5);

        assertThat(result).containsExactly(5, 500);
    }

    
    @Test
    public void replaceStubbing() {
        when(() -> mock.voidMethod()).thenDoNothing();
        when(() -> mock.voidMethod()).thenThrow(new IllegalArgumentException());

        assertThatThrownBy(mock::voidMethod).isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void mixedStubbings() {
        when(mock::voidMethod).thenThrow(new IllegalArgumentException());
        doThrow(new UnsupportedOperationException()).when(mock).differentMethod();
        
        assertThatThrownBy(mock::voidMethod).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(mock::differentMethod).isInstanceOf(UnsupportedOperationException.class);
    }
    
    @Test
    public void captureArgument() {
        ArgumentCaptor<Integer> captor = forClass(int.class);
        
        when(()->mock.intArgumentMethod(captor.capture())).thenDoNothing();
        
        mock.intArgumentMethod(123);
        
        assertThat(captor.getValue()).isEqualTo(123);
    }
    
    
    
    @Test
    public void tooManyCallsOnMock() {
        assertThatThrownBy(() -> {

            when(() -> {
                mock.intArgumentMethod(5);
                mock.intArgumentMethod(2);
            });

        })
        .isInstanceOf(MockitoException.class)
        .hasMessageContaining("Too many mock methods were called").hasMessageContaining("Expected exactly one call to mock: " + mock);

    }
}