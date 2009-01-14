package org.mockitousage.configuration;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( { FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SmartMock {}