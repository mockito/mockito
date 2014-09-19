package org.mockito.cglibsamples;
import org.mockito.cglib.proxy.*;
import java.util.*;
/**
 *
 * @author  baliuka
 */
public class Trace implements MethodInterceptor {
    
    int ident = 1;
    static Trace callback = new Trace();
    
    /** Creates a new instance of Trace */
    private Trace() {
    }
    
    public static  Object newInstance( Class clazz ){
      try{
            Enhancer e = new Enhancer();
            e.setSuperclass(clazz);
            e.setCallback(callback);
            return e.create();
      }catch( Throwable e ){
         e.printStackTrace(); 
         throw new Error(e.getMessage());
      }  
    
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        List list = (List)newInstance(Vector.class);
        Object value = "TEST";
        list.add(value);
        list.contains(value);
        try{
         list.set(2, "ArrayIndexOutOfBounds" );
        }catch( ArrayIndexOutOfBoundsException ignore ){
        
        }
       list.add(value + "1");
       list.add(value + "2");
       list.toString();
       list.equals(list); 
       list.set( 0, null ); 
       list.toString();
       list.add(list);
       list.get(1);
       list.toArray();
       list.remove(list);
       list.remove("");
       list.containsAll(list);
       list.lastIndexOf(value);
    }


    public Object intercept(Object obj, java.lang.reflect.Method method, Object[] args,
                            MethodProxy proxy) throws Throwable {
        printIdent(ident);
        System.out.println( method );
        for( int i = 0; i < args.length; i++ ){
          printIdent(ident);   
          System.out.print( "arg" + (i + 1) + ": ");
          if( obj == args[i])
              System.out.println("this");
          else
              System.out.println(args[i]);
        }
        ident++;

        Object retValFromSuper = null;
        try {
            retValFromSuper = proxy.invokeSuper(obj, args);
            ident--;
        } catch (Throwable t) {
            ident--;
            printIdent(ident);   
            System.out.println("throw " + t );  
            System.out.println();
            throw t.fillInStackTrace();
        }
        
        printIdent(ident); 
        System.out.print("return " );
        if( obj == retValFromSuper)
            System.out.println("this");
        else System.out.println(retValFromSuper);
        
        if(ident == 1)
             System.out.println();
        
        return retValFromSuper;
    }
    
   void printIdent( int ident ){
       
    
       while( --ident > 0 ){
         System.out.print(".......");
       }
      System.out.print("  ");
   }
    
}
