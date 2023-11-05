package org.kiwitcms.java.annotations;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TcmsTestAttributes
{
	int value() default 0;
	int testCaseId() default 0;
	int productId() default 0;
	int planId() default 0;
}
