package org.mockitousage.stubbing;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

public class StubbingWithDelegateVarArgsTest {

    public interface Foo {
        int bar(String baz, Object... args);
    }

    private static final class FooImpl implements Foo {

        @Override
        public int bar(String baz, Object... args) {
            return args != null ? args.length : -1; // simple return argument count
        }

    }

    @Test
    public void should_not_fail_when_calling_varargs_method() {
        Foo foo = mock(Foo.class, withSettings()
                .defaultAnswer(delegatesTo(new FooImpl())));
        assertThat(foo.bar("baz", 12, "45", 67.8)).isEqualTo(3);
    }

    @Test
    public void should_not_fail_when_calling_varargs_method_without_arguments() {
        Foo foo = mock(Foo.class, withSettings()
                .defaultAnswer(delegatesTo(new FooImpl())));
        assertThat(foo.bar("baz")).isEqualTo(0);
        assertThat(foo.bar("baz", new Object[0])).isEqualTo(0);
    }

    @Test
    public void should_not_fail_when_calling_varargs_method_with_null_argument() {
        Foo foo = mock(Foo.class, withSettings()
                .defaultAnswer(delegatesTo(new FooImpl())));
        assertThat(foo.bar("baz", (Object[]) null)).isEqualTo(-1);
    }
}
