package org.mockito.cglibsamples;
import org.mockito.cglib.core.KeyFactory;
public class KeySample {
    private interface MyFactory {
        public Object newInstance(int a, char[] b, String d);
    }
    public static void main(String[] args) {
        MyFactory f = (MyFactory)KeyFactory.create(MyFactory.class);
        Object key1 = f.newInstance(20, new char[]{ 'a', 'b' }, "hello");
        Object key2 = f.newInstance(20, new char[]{ 'a', 'b' }, "hello");
        Object key3 = f.newInstance(20, new char[]{ 'a', '_' }, "hello");
        System.out.println(key1.equals(key2));
        System.out.println(key2.equals(key3));
    }
}
