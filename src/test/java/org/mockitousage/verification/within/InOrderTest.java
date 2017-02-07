package org.mockitousage.verification.within;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.within;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.exceptions.verification.VerificationInOrderFailure;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockitousage.IMethods;

public class InOrderTest {


    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    
    @Mock
    private IMethods mock1,mock2;
    
    @Before
    public void init(){
        mock1.intArgumentMethod(1);
        mock1.intArgumentMethod(2);
        mock1.intArgumentMethod(2);
        mock1.intArgumentMethod(1);
        
        mock2.intArgumentMethod(100);
        mock2.intArgumentMethod(100);
        mock2.intArgumentMethod(200);
        mock2.intArgumentMethod(200);  
    }
    
    @Test
    public void test1()   {
        mock1.voidMethod();
        mock1.voidMethod();
        mock1.differentMethod();
        mock1.voidMethod();
        mock1.differentMethod();
        
        InOrder o = inOrder(mock1);

        o.verify(mock1,times(2)).voidMethod();
        o.verify(mock1,times(1)).differentMethod();
        o.verify(mock1,times(1)).differentMethod();
    }
    
    @Test
    public void test2()   {
        mock1.differentMethod();
        mock1.voidMethod();
        mock1.voidMethod();
        mock1.differentMethod();
        
        InOrder o = inOrder(mock1);

        o.verify(mock1,times(2)).voidMethod();
        o.verify(mock1,times(1)).differentMethod();
    }
    
    @Test
    public void test3()   {
        mock1.differentMethod();
        mock1.voidMethod();
        mock1.voidMethod();
        mock1.differentMethod();
        
        InOrder o = inOrder(mock1);
        
        o.verify(mock1,times(3)).voidMethod();
        
    }
    
    @Test
    public void test4()   {
        mock1.differentMethod();
        mock1.voidMethod();
        mock1.voidMethod();
        mock1.voidMethod();
        mock1.voidMethod();
        mock1.differentMethod();
        mock1.differentMethod();
        
        InOrder o = inOrder(mock1);
        
        o.verify(mock1,times(3)).voidMethod();
        
    }
    
    @Test
    public void test5()   {
        mock1.differentMethod();
        mock1.voidMethod();
        mock2.voidMethod();
        mock2.voidMethod();
        
        InOrder o = inOrder(mock1,mock2);
        
        o.verify(mock2,times(1)).voidMethod();
        
    }
    
    @Test
    public void within1() throws Exception {
        InOrder o = inOrder(mock1,mock2);

        o.verify(mock1,within(0, MILLISECONDS).times(1)).intArgumentMethod(1);
        
        o.verify(mock2,within(0, MILLISECONDS).times(2)).intArgumentMethod(100);
        o.verify(mock2,within(0, MILLISECONDS).times(2)).intArgumentMethod(200);
    }
    
    
    @Test
    public void within2() throws Exception {
        InOrder o = inOrder(mock1,mock2);
        
        o.verify(mock1,within(0, MILLISECONDS).times(2)).intArgumentMethod(2);
        o.verify(mock1,within(0, MILLISECONDS).times(1)).intArgumentMethod(1);
        
        o.verify(mock2,within(0, MILLISECONDS).times(2)).intArgumentMethod(100);
        o.verify(mock2,within(0, MILLISECONDS).times(2)).intArgumentMethod(200);
    }
    
    
    @Test
    public void within3() throws Exception {
        InOrder o = inOrder(mock1,mock2);
        
        o.verify(mock2,within(0, MILLISECONDS).times(2)).intArgumentMethod(100);
        o.verify(mock2,within(0, MILLISECONDS).times(2)).intArgumentMethod(200);
    }
    
    
    
    @Test
    public void within4() throws Exception {
        InOrder o = inOrder(mock1,mock2);
        

        o.verify(mock2,within(0, MILLISECONDS).times(2)).intArgumentMethod(200);
        
        exception.expect(VerificationInOrderFailure.class);
        exception.expectMessage("Wanted but not invoked");
        exception.expectMessage("mock1.intArgumentMethod(1)");
        exception.expectMessage("Wanted anywhere AFTER following interaction");
        exception.expectMessage("mock2.intArgumentMethod(200)");
        
        o.verify(mock1,within(0, MILLISECONDS).times(1)).intArgumentMethod(1);
    }
}
