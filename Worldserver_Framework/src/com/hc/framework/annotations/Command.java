package com.hc.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Runtime so that the annotation can be accessed via reflection at runtime, else it will not be preserved at runtime
@Retention(RetentionPolicy.RUNTIME)

// Should be used on methods
@Target(ElementType.METHOD)

public @interface Command {
	public String name();
	public String description();
	public String delimiter() default " ";
}