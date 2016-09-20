package org.mockito.internal.creation.bytebuddy;

public class SubclassByteBuddyMockMakerTest extends AbstractByteBuddyMockMakerTest {

    public SubclassByteBuddyMockMakerTest() {
        super(new SubclassByteBuddyMockMaker());
    }

    @Override
    protected Class<?> mockTypeOf(Class<?> type) {
        return type.getSuperclass();
    }
}
