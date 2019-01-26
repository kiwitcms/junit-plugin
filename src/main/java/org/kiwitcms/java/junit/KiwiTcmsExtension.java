// Copyright (c) 2018-2019 Aneta Petkova <aneta.v.petkova@gmail.com>

// Licensed under the GPLv3: https://www.gnu.org/licenses/gpl.html

package org.kiwitcms.java.junit;

import org.kiwitcms.java.model.TestMethod;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.engine.discovery.predicates.IsTestMethod;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class KiwiTcmsExtension extends SummaryGeneratingListener  implements AfterAllCallback, AfterEachCallback, BeforeAllCallback {

    private List<TestMethod> tests;

    public void beforeAll(ExtensionContext context){
        tests = new ArrayList<>();
    }

    public void afterAll(ExtensionContext context){
        System.out.println(TestMethod.toJSONArrayString(tests));
        TestDataEmitter emitter = new TestDataEmitter();
        for (TestMethod test : tests){
            emitter.emitNewTestCase(test.name);
        }
        emitter.closeSession();
        //check for existing test plan
        //if no, create test plan
        //add results
    }

    public void afterEach(ExtensionContext context){
        if (context.getTestMethod().isPresent()) {
            Method method = context.getTestMethod().get();
            TestMethod test = new TestMethod();
            test.name = method.getName();
            if (context.getExecutionException().isPresent()){
                test.exception = context.getExecutionException().get();
                test.result = "FAIL";
            }
            else {
                test.result = "PASS";
            }
            test.containingClass = method.getDeclaringClass().getSimpleName();
            tests.add(test);
        }
    }

}
