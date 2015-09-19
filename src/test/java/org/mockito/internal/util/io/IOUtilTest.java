package org.mockito.internal.util.io;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class IOUtilTest {

    @Rule public TemporaryFolder tmp = new TemporaryFolder();

    @Test
    public void closes_streams() throws Exception {
        IOUtil.closeQuietly(null);
        IOUtil.closeQuietly(new ByteArrayOutputStream());

        IOUtil.close(null);
        IOUtil.close(new ByteArrayOutputStream());
    }

    @Test
    public void writes_reads_files() throws Exception {
        File file = tmp.newFile();
        IOUtil.writeText("foo\n\nbar", file);
        assertEquals(asList("foo", "", "bar"), IOUtil.readLines(new FileInputStream(file)));
    }
}