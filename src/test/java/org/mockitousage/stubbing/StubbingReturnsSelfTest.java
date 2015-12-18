package org.mockitousage.stubbing;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class StubbingReturnsSelfTest {

    @Test
    public void should_stub_builder_method() {
        Builder builder = mock(Builder.class, RETURNS_SELF);

        assertThat(builder.returnSelf()).isEqualTo(builder);
    }

    @Test
    public void should_return_default_return_when_not_a_builder() {
        Builder builder = mock(Builder.class, RETURNS_SELF);

        assertThat(builder.returnString()).isEqualTo(null);
    }

    @Test
    public void should_return_self_when_call_on_method_in_superclass() {
        BuilderSubClass builder = mock(BuilderSubClass.class, RETURNS_SELF);

        assertThat(builder.returnSelf()).isEqualTo(builder);
    }

    @Test
    public void should_return_self_when_call_on_method_in_subclass() {
        BuilderSubClass builder = mock(BuilderSubClass.class, RETURNS_SELF);

        assertThat(builder.returnsSubClass()).isEqualTo(builder);
    }

    @Test
    public void should_return_self_when_call_on_method_in_subclass_returns_superclass() {
        BuilderSubClass builder = mock(BuilderSubClass.class, RETURNS_SELF);

        assertThat(builder.returnSuperClass()).isEqualTo(builder);
    }

    @Test
    public void should_return_stubbed_answer_when_call_on_method_returns_self() {
        Builder builder = mock(Builder.class, RETURNS_SELF);
        Builder anotherBuilder = mock(Builder.class, RETURNS_SELF);

        when(builder.returnSelf()).thenReturn(anotherBuilder);

        assertThat(builder.returnSelf().returnSelf()).isEqualTo(anotherBuilder);
    }

    @Test
    public void should_not_fail_when_calling_void_returning_method() {
        Builder builder = mock(Builder.class, RETURNS_SELF);

        builder.returnNothing();
    }

    @Test
    public void should_not_fail_when_calling_primitive_returning_method() {
        Builder builder = mock(Builder.class, RETURNS_SELF);

        assertThat(builder.returnInt()).isEqualTo(0);
    }

    @Test
    public void use_full_builder_with_terminating_method() {
        HttpBuilder builder = mock(HttpBuilder.class, RETURNS_SELF);
        HttpRequesterWithHeaders requester = new HttpRequesterWithHeaders(builder);
        String response = "StatusCode: 200";

        when(builder.request()).thenReturn(response);

        assertThat(requester.request("URI")).isEqualTo(response);
    }

    private static class Builder {

        public Builder returnSelf() {
            return this;
        }

        public String returnString() {
            return "Self";
        }

        public void returnNothing() {}

        public int returnInt() {
            return 1;
        }
    }

    private static class BuilderSubClass extends Builder {

        public BuilderSubClass returnsSubClass() {
            return this;
        }

        public Builder returnSuperClass() {
            return this;
        }
    }

    private static class HttpRequesterWithHeaders {

        private HttpBuilder builder;

        public HttpRequesterWithHeaders(HttpBuilder builder) {
            this.builder = builder;
        }

        public String request(String uri) {
            return builder.withUrl(uri)
                    .withHeader("Content-type: application/json")
                    .withHeader("Authorization: Bearer")
                    .request();
        }
    }

    private static class HttpBuilder {

        private String uri;
        private List<String> headers;

        public HttpBuilder() {
            this.headers = new ArrayList<String>();
        }

        public HttpBuilder withUrl(String uri) {
            this.uri = uri;
            return this;
        }

        public HttpBuilder withHeader(String header) {
            this.headers.add(header);
            return this;
        }

        public String request() {
            return uri + headers.toString();
        }

    }
}
