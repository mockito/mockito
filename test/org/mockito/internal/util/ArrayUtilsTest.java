package org.mockito.internal.util;

import org.fest.assertions.Assertions;
import org.junit.Test;
import org.mockitoutil.TestBase;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ArrayUtilsTest extends TestBase {

    ArrayUtils utils = new ArrayUtils();

    @Test
    public void shouldConcatenateItemToAnEmptyArray() throws Exception {
        //when
        Class<?>[] items = utils.concat(new Class[0], List.class);

        //then
        Assertions.assertThat(items).containsOnly(List.class);
    }

    @Test
    public void shouldConcatenateItemToFullArray() throws Exception {
        //when
        Class<?>[] items = utils.concat(new Class[] {Serializable.class, Map.class}, List.class);

        //then
        Assertions.assertThat(items).containsOnly(Serializable.class, Map.class, List.class);
    }
}