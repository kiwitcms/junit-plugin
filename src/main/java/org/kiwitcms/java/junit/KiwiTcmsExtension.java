// Copyright (c) 2018-2019 Aneta Petkova <aneta.v.petkova@gmail.com>

// Licensed under the GPLv3: https://www.gnu.org/licenses/gpl.html

package org.kiwitcms.java.junit;

import org.kiwitcms.java.config.Config;
import org.kiwitcms.java.model.TcmsTestCaseId;
import org.kiwitcms.java.model.TestMethod;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.engine.discovery.predicates.IsTestMethod;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

import javax.naming.ConfigurationException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class KiwiTcmsExtension extends SummaryGeneratingListener  implements AfterAllCallback, AfterEachCallback, BeforeAllCallback {

    private List<TestMethod> tests;

    public void beforeAll(ExtensionContext context) throws ConfigurationException {
        Config config = Config.getInstance();
        if (config.getUrl().isEmpty() || config.getUsername().isEmpty() || config.getPassword().isEmpty()){
            throw new ConfigurationException("API URL, username and password must be configured");
        }
        tests = new ArrayList<>();
    }

    public void afterAll(ExtensionContext context){
        TestDataEmitter emitter = new TestDataEmitter();
        int runId = emitter.getTestRunId();
        emitter.addTestResultsToRun(runId, tests);
        emitter.closeSession();
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
            if (method.isAnnotationPresent(TcmsTestCaseId.class)) {
                TcmsTestCaseId tcmsTestCaseId = method.getAnnotation(TcmsTestCaseId.class);
                test.id = tcmsTestCaseId.value();
            }
            tests.add(test);
        }
    }
}
