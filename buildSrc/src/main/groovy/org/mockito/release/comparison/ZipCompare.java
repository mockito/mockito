package org.mockito.release.comparison;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static java.lang.String.format;

/**
 * Borrowed from the web so it does not look great.
 * I've removed System.out.println, fixed exception handling and added coverage.
 * For now it might be good enough.
 */
class ZipCompare {

    private final static Logger LOG = LoggerFactory.getLogger(ZipCompare.class);

    boolean compareZips(String filePath1, String filePath2) {
        ZipFile file1;
        try {
            file1 = new ZipFile(filePath1);
        } catch (IOException e) {
            throw new ZipCompareException("Could not open zip file " + filePath1, e);
        }

        ZipFile file2;
        try {
            file2 = new ZipFile(filePath2);
        } catch (IOException e) {
            throw new ZipCompareException("Could not open zip file " + filePath1, e);
        }

        LOG.info("Comparing " + filePath1 + " with " + filePath2);

        Set<String> set1 = new LinkedHashSet<String>();
        for (Enumeration e = file1.entries(); e.hasMoreElements(); )
            set1.add(((ZipEntry) e.nextElement()).getName());

        Set<String> set2 = new LinkedHashSet<String>();
        for (Enumeration e = file2.entries(); e.hasMoreElements(); )
            set2.add(((ZipEntry) e.nextElement()).getName());

        int errcount = 0;
        int filecount = 0;
        for (String name : set1) {
            if (!set2.contains(name)) {
                LOG.info(name + " not found in " + filePath2);
                errcount += 1;
                continue;
            }
            try {
                set2.remove(name);
                if (!streamsEqual(file1.getInputStream(file1.getEntry(name)), file2.getInputStream(file2
                        .getEntry(name)))) {
                    LOG.info(name + " does not match");
                    errcount += 1;
                    continue;
                }
            } catch (Exception e) {
                throw new ZipCompareException(format("Unable to compare zip entry '%s' found in '%s' with '%s'", name, filePath1, filePath2), e);
            }
            filecount += 1;
        }
        for (String name : set2) {
            LOG.info(name + " not found in " + filePath1);
            errcount += 1;
        }
        LOG.info(filecount + " entries matched");
        if (errcount > 0) {
            LOG.info(errcount + " entries did not match");
            return false;
        }
        return true;
    }

    static boolean streamsEqual(InputStream stream1, InputStream stream2) throws IOException {
        byte[] buf1 = new byte[4096];
        byte[] buf2 = new byte[4096];
        boolean done1 = false;
        boolean done2 = false;

        try {
            while (!done1) {
                int off1 = 0;
                int off2 = 0;

                while (off1 < buf1.length) {
                    int count = stream1.read(buf1, off1, buf1.length - off1);
                    if (count < 0) {
                        done1 = true;
                        break;
                    }
                    off1 += count;
                }
                while (off2 < buf2.length) {
                    int count = stream2.read(buf2, off2, buf2.length - off2);
                    if (count < 0) {
                        done2 = true;
                        break;
                    }
                    off2 += count;
                }
                if (off1 != off2 || done1 != done2)
                    return false;
                for (int i = 0; i < off1; i++) {
                    if (buf1[i] != buf2[i])
                        return false;
                }
            }
            return true;
        } finally {
            stream1.close();
            stream2.close();
        }
    }

    static class ZipCompareException extends RuntimeException {
        public ZipCompareException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}