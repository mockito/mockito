public class MockSettingsImpl implements MockSettings {
    private Throwable constructorException;


    public MockSettings constructorException(Throwable exception) {
        this.constructorException = exception;
        return this;
    }

    public Throwable getConstructorException() {
        return constructorException;
    }


    @Override
    public MockSettings prepare(Object... extraInterfaces) {

        return this;
    }
}
