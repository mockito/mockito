/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.regression;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * <a href="https://github.com/mockito/mockito/issues/3093">Issue #3093</a>
 */
@ExtendWith(MockitoExtension.class)
class Regression3093Test {

    @Test
    void testFindByNumber() {
        when(beraterTypeRepository.findByNumber(anyString()))
                .thenReturn(any(RealAbstractCustomTypeClass.class));
        assertNull(abstractBeraterTypeService.findByNumber("0"));
    }

    class RealAbstractCustomTypeService
            extends AbstractCustomTypeService<CustomTypeRepository<RealAbstractCustomTypeClass>> {
        public RealAbstractCustomTypeService(
                CustomTypeRepository<RealAbstractCustomTypeClass> repository) {
            super(repository);
        }
    }

    @Mock private CustomTypeRepository<RealAbstractCustomTypeClass> beraterTypeRepository;

    @InjectMocks
    private RealAbstractCustomTypeService abstractBeraterTypeService =
            new RealAbstractCustomTypeService(beraterTypeRepository);

    abstract class AbstractService<T extends MarkerTypeRepository> {
        protected T repository;

        protected AbstractService(T repository) {
            this.repository = repository;
        }
    }

    abstract class AbstractCustomTypeService<
                    T extends CustomTypeRepository<? extends AbstractCustomType>>
            extends AbstractService<T> {

        protected AbstractCustomTypeService(T repository) {
            super(repository);
        }

        public AbstractCustomType findByNumber(String number) {
            return repository.findByNumber(number);
        }
    }

    interface CustomTypeRepository<T extends AbstractCustomType> extends MarkerTypeRepository {
        T findByNumber(String number);
    }

    interface MarkerTypeRepository {}

    static class AbstractCustomType {

        private String number;

        public AbstractCustomType() {}

        public AbstractCustomType(String number) {
            this.number = number;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }

    static class RealAbstractCustomTypeClass extends AbstractCustomType {}
}
