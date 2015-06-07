/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockitoutil.TestBase;

@SuppressWarnings("rawtypes")
public class PlaygroundWithDemoOfUnclonedParametersProblemTest extends TestBase {

    ImportManager importManager;
    ImportLogDao importLogDao;
    IImportHandler importHandler;

    @Before
    public void setUp() throws Exception {
        importLogDao = Mockito.mock(ImportLogDao.class);
        importHandler = Mockito.mock(IImportHandler.class);
        importManager = new ImportManager(importLogDao);
    }

    @Test
    public void shouldIncludeInitialLog() {
        // given
        final int importType = 0;
        final Date currentDate = new GregorianCalendar(2009, 10, 12).getTime();

        final ImportLogBean initialLog = new ImportLogBean(currentDate, importType);
        initialLog.setStatus(1);

        given(importLogDao.anyImportRunningOrRunnedToday(importType, currentDate)).willReturn(false);
        willAnswer(byCheckingLogEquals(initialLog)).given(importLogDao).include(any(ImportLogBean.class));

        // when
        importManager.startImportProcess(importType, currentDate);

        // then
        verify(importLogDao).include(any(ImportLogBean.class));
    }

    @Test
    public void shouldAlterFinalLog() {
        // given
        final int importType = 0;
        final Date currentDate = new GregorianCalendar(2009, 10, 12).getTime();

        final ImportLogBean finalLog = new ImportLogBean(currentDate, importType);
        finalLog.setStatus(9);

        given(importLogDao.anyImportRunningOrRunnedToday(importType, currentDate)).willReturn(false);
        willAnswer(byCheckingLogEquals(finalLog)).given(importLogDao).alter(any(ImportLogBean.class));

        // when
        importManager.startImportProcess(importType, currentDate);

        // then
        verify(importLogDao).alter(any(ImportLogBean.class));
    }

    private Answer byCheckingLogEquals(final ImportLogBean status) {
        return new Answer() {
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                final ImportLogBean bean = (ImportLogBean) invocation.getArguments()[0];
                assertEquals(status, bean);
                return null;
            }
        };
    }

    public class ImportManager {

        public ImportManager(final ImportLogDao pImportLogDao) {
            super();
            importLogDao = pImportLogDao;
        }

        private ImportLogDao importLogDao = null;

        public void startImportProcess(final int importType, final Date date) {
            ImportLogBean importLogBean = null;

            try {
                importLogBean = createResume(importType, date);
                if (isOkToImport(importType, date)) {
                    // get the right handler
                    // importLogBean =
                    // ImportHandlerFactory.singleton().getImportHandler(importType).processImport(importLogBean);
                    // 2 = ok
                    importLogBean.setStatus(2);
                } else {
                    // 5 = failed - is there a running process
                    importLogBean.setStatus(9);
                }
            } catch (final Exception e) {
                // 9 = failed - exception
                if (importLogBean != null) {
                    importLogBean.setStatus(9);
                }
            } finally {
                if (importLogBean != null) {
                    finalizeResume(importLogBean);
                }
            }
        }

        private boolean isOkToImport(final int importType, final Date date) {
            return importLogDao.anyImportRunningOrRunnedToday(importType, date);
        }

        private ImportLogBean createResume(final int importType, final Date date) {
            final ImportLogBean importLogBean = new ImportLogBean(date,
                    importType);
            // 1 = running
            importLogBean.setStatus(1);
            importLogDao.include(importLogBean);
            return importLogBean;
        }

        private void finalizeResume(final ImportLogBean importLogBean) {
            importLogDao.alter(importLogBean);
        }
    }

    private interface ImportLogDao {
        boolean anyImportRunningOrRunnedToday(final int importType, final Date currentDate);

        void include(final ImportLogBean importLogBean);

        void alter(final ImportLogBean importLogBean);
    }

    private class IImportHandler {
    }

    private class ImportLogBean {
        private final Date currentDate;
        private final int importType;
        private int status;

        public ImportLogBean(final Date currentDate, final int importType) {
            this.currentDate = currentDate;
            this.importType = importType;
        }

        public void setStatus(final int status) {
            this.status = status;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ImportLogBean)) {
                return false;
            }

            final ImportLogBean that = (ImportLogBean) o;

            if (importType != that.importType) {
                return false;
            }
            if (status != that.status) {
                return false;
            }
            if (currentDate != null ? !currentDate.equals(that.currentDate) : that.currentDate != null) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = currentDate != null ? currentDate.hashCode() : 0;
            result = 31 * result + importType;
            result = 31 * result + status;
            return result;
        }
    }
}