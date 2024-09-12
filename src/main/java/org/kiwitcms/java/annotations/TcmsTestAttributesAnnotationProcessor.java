package org.kiwitcms.java.annotations;

import java.lang.reflect.Method;

public class TcmsTestAttributesAnnotationProcessor {

	private int value = 0;
	private int testCaseId = 0;
	private int productId = 0;
	private int planId = 0;

	public TcmsTestAttributesAnnotationProcessor(Class<?> annotatedClass) {
		if (annotatedClass.isAnnotationPresent(TcmsTestAttributes.class)) {
			TcmsTestAttributes classAttributes = annotatedClass.getAnnotation(TcmsTestAttributes.class);
			this.value = classAttributes.value();
			this.testCaseId = classAttributes.testCaseId();
			this.productId = classAttributes.productId();
			this.planId = classAttributes.planId();
		}
	}

	public TcmsTestAttributesAnnotationProcessor(Method annotatedMethod) {
		if (annotatedMethod.isAnnotationPresent(TcmsTestAttributes.class)) {
			TcmsTestAttributes methodAttributes = annotatedMethod.getAnnotation(TcmsTestAttributes.class);
			this.value = methodAttributes.value();
			this.testCaseId = methodAttributes.testCaseId();
			this.productId = methodAttributes.productId();
			this.planId = methodAttributes.planId();
		}
	}

	public int getTestCaseId() {
		if (this.testCaseId > 0) {return this.testCaseId;}
		return Math.max(this.value, 0);
	}

	public int getProductId() {
		return Math.max(this.productId, 0);
	}

	public int getPlanId() {
		return Math.max(this.planId, 0);
	}
}
