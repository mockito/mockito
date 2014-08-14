package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.ByteBuddy;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockitoutil.ClassLoaders;
import org.objenesis.ObjenesisStd;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

public class ByteBuddyMockMakerTest {

    @Test
    public void report_issue_when_trying_to_load_objenesis() throws Exception {
        // given
        ClassLoader classpath_without_objenesis = ClassLoaders.excludingClassLoader()
                .withCodeSourceUrlOf(Mockito.class, ByteBuddy.class)
                .without("org.objenesis")
                .build();
        boolean initialize_class = true;

        Class<?> mock_maker_class_loaded_fine_until = Class.forName(
                "org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker",
                initialize_class,
                classpath_without_objenesis
        );


        // when
        try {
            mock_maker_class_loaded_fine_until.newInstance();
            fail();
        } catch (Throwable e) {
            // then
            assertThat(e).isInstanceOf(IllegalStateException.class);
            assertThat(e.getMessage()).containsIgnoringCase("objenesis").contains("missing");
        }
    }

    @Test
    public void instantiate_fine_when_objenesis_on_the_classpath() throws Exception {
        // given
        ClassLoader classpath_without_objenesis = ClassLoaders.excludingClassLoader()
                .withCodeSourceUrlOf(Mockito.class, ByteBuddy.class, ObjenesisStd.class)
                .build();
        boolean initialize_class = true;

        Class<?> mock_maker_class_loaded_fine_until = Class.forName(
                "org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker",
                initialize_class,
                classpath_without_objenesis
        );

        // when
        mock_maker_class_loaded_fine_until.newInstance();

        // then everything went fine
    }
}
