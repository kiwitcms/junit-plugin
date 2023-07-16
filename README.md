JUnit 5 plugin for Kiwi TCMS
----------------------------

[![Build Status](https://travis-ci.org/kiwitcms/junit-plugin.svg?branch=master)](https://travis-ci.org/kiwitcms/junit-plugin)
[![Coverage Status](https://codecov.io/gh/kiwitcms/junit-plugin/branch/master/graph/badge.svg?token=2BCuQxHh2J)](https://codecov.io/gh/kiwitcms/junit-plugin)
[![TP for kiwitcms/junit-plugin (master)](https://img.shields.io/badge/kiwi%20tcms-results-9ab451.svg)](https://tcms.kiwitcms.org/plan/25/)
[![Maven Central](https://img.shields.io/maven-central/v/org.kiwitcms.java/kiwitcms-junit-plugin.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22org.kiwitcms.java%22%20AND%20a:%22kiwitcms-junit-plugin%22)
[![Tidelift](https://tidelift.com/badges/package/pypi/kiwitcms)](https://tidelift.com/subscription/pkg/pypi-kiwitcms?utm_source=pypi-kiwitcms&utm_medium=github&utm_campaign=junit-plugin)
[![Become-a-sponsor](https://opencollective.com/kiwitcms/tiers/sponsor/badge.svg?label=sponsors&color=brightgreen)](https://opencollective.com/kiwitcms#contributors)
[![Twitter](https://img.shields.io/twitter/follow/KiwiTCMS.svg)](https://twitter.com/KiwiTCMS)

## Installation & usage

Add this into your `pom.xml`:

    <dependency>
        <groupId>org.kiwitcms.java</groupId>
        <artifactId>kiwitcms-junit-plugin</artifactId>
        <version>x.y.z</version>
    </dependency>


This plugin extends `org.junit.jupiter.api.extension.Extension` and has
`junit.jupiter.extensions.autodetection.enabled=true` configured by default in
`pom.xml`. This means Jupiter will pick it up automatically.

You can point each executed test case to a specific case ID by annotating `@Test` method with `@TcmsTestCaseId(int)`,
where `int` is the test case ID. See the `KiwiTcmsExtension` example below.
If the test case is not found searching by ID, plugin will default to standard search method.
You can find the case ID in your TCMS instance URL (example: https://tcms.server/case/1)
or on the test case page in the test case name (TC-**1**: Test case 1).

You may alternatively decorate your test suite with the `KiwiTcmsExtension` class
but that should be redundant:

    import org.junit.jupiter.api.Test;
    import static org.hamcrest.MatcherAssert.assertThat;

    import org.junit.jupiter.api.extension.ExtendWith;
    import org.kiwitcms.java.junit.KiwiTcmsExtension;

    @ExtendWith(KiwiTcmsExtension.class)
    public class KiwiJsonRpcClientTest {
        @Test
        @TcmsTestCaseId(11)
        public void yourTest(){
            assertThat(...);
        }
    }


## Configuration and environment

Minimal config file `~/.tcms.conf`:

    [tcms]
    url = https://tcms.server/xml-rpc/
    username = your-username
    password = your-password

You can override the default file location by adding the Maven option ```tcmsConfigPath```
followed by the full path to a config file. For example:
```-DtcmsConfigPath=D:\Path\To\tcms.conf```

For more info see [tcms-api docs](https://tcms-api.readthedocs.io).

This plugin is only concerned with parsing the JUnit test results and reporting
them to the backend. Behavior is controlled via environment variables.

For example this is how our own environment looks like::

    #!/bin/bash

    if [ "$TRAVIS_EVENT_TYPE" == "push" ]; then
        # same as $TRAVIS_TAG when building tags
        export TCMS_PRODUCT_VERSION=$TRAVIS_BRANCH
    fi

    if [ "$TRAVIS_EVENT_TYPE" == "pull_request" ]; then
        export TCMS_PRODUCT_VERSION="PR-$TRAVIS_PULL_REQUEST"
    fi

    export TCMS_BUILD="$TRAVIS_BUILD_NUMBER-$(echo $TRAVIS_COMMIT | cut -c1-7)"

For a more detailed example see:
http://kiwitcms.org/blog/atodorov/2019/02/22/how-to-use-kiwi-tcms-plugins-pt-1/


## Changelog

Files signed with EC1CEB7CDFA79FB5, Kiwi TCMS &lt;info@kiwitcms.org&gt;

### 12.5 (14 Jul 2023)

- Add override parameter to config path (Marcin Lewandowski)
- Upgrade jackson-core from 2.13.3 to 2.15.2
- Upgrade jackson-databind from 2.13.4.2 to 2.15.2
- Upgrade junit-jupiter-api from 5.8.2 to 5.9.3
- Upgrade junit-jupiter-engine from 5.8.2 to 5.9.3
- Upgrade junit-platform-launcher from 1.8.2 to 1.9.3
- Upgrade maven-checkstyle-plugin from 3.1.2 to 3.3.0
- Upgrade maven-compiler-plugin from 3.10.1 to 3.11.0
- Upgrade maven-javadoc-plugin from 3.4.0 to 3.5.0
- Upgrade maven-surefire-plugin from 3.0.0-M7 to 3.1.2
- Upgrade mockito-core from 4.6.1 to 4.11.0

### 11.1 (14 Jul 2022)

- Don't hard-code test execution statuses, fetch them from API
- Update dependencies to latest versions
- Add pre-commit CI config

### 11.0 (13 Dec 2021)

- GPG key used to sign this package changed from `C0C5FF36` to `EC1CEB7CDFA79FB5`
- Forward compatibility with upcoming Kiwi TCMS v11.0
- Remove non-existing field `default_product_version` from API call
- Remove `TestRun.product_version` field
- Bump jackson-core from 2.12.1 to 2.13.0
- Bump jackson-databind from 2.12.0 to 2.13.0
- Bump jacoco-maven-plugin from 0.8.6 to 0.8.7
- Bump jsonrpc2-client from 1.16.4 to 1.16.5
- Bump junit-jupiter-api from 5.5.2 to 5.8.2
- Bump junit-jupiter-engine from 5.5.2 to 5.8.2
- Bump junit-platform-launcher from 1.5.2 to 1.8.2
- Bump maven-checkstyle-plugin from 3.1.1 to 3.1.2
- Bump maven-gpg-plugin from 1.6 to 3.0.1
- Bump maven-javadoc-plugin from 3.2.0 to 3.3.1
- Bump mockito-core from 3.7.0 to 4.1.0


### 10.0 (02 Mar 2021)

This version works only with Kiwi TCMS v10.0 or later!

- Upgrade jackson-databind from 2.10.0 to 2.12.0


### 9.0 (15 Jan 2021)

This version works only with Kiwi TCMS v9.0 or later!

- Bump maven-checkstyle-plugin from 3.1.0 to 3.1.1
- Bump commons-configuration2 from 2.6 to 2.7
- Bump maven-javadoc-plugin from 3.1.1 to 3.2.0
- Bump mockito-core from 3.2.4 to 3.3.3
- Bump jackson-databind from 2.9.10.1 to 2.10.0
- Bump maven-surefire-plugin from 3.0.0-M4 to 3.0.0-M5
- Bump jacoco-maven-plugin from 0.8.5 to 0.8.6
- Bump mockito-core from 3.2.4 to 3.7.0
- Bump jackson-core from 2.10.2 to 2.12.1
- Remove unused method


### 8.0 (09 Feb 2020)

This version works only with Kiwi TCMS v8.0 or later!

- Adjust field names for API changes coming in Kiwi TCMS v8.0
- Do not use deprecated `product` field when calling `TestCase.create()` API
- Bug-fix: take into account `TCMS_RUN_ID` environment variable
- Bump jackson-core from 2.10.0 to 2.10.2
- Bump maven-source-plugin from 3.2.0 to 3.2.1
- Bump maven-surefire-plugin from 3.0.0-M3 to 3.0.0-M4
- Bump mockito-core from 3.1.0 to 3.2.4


### 6.7.5 (10 Nov 2019)

Fixes moderate severity issue
[CVE-2019-16942](https://github.com/advisories/GHSA-mx7p-6679-8g3q),
critical severity issue
[CVE-2019-16335](https://github.com/advisories/GHSA-85cw-hj65-qqv9)
and critical severity issue
[CVE-2019-14540](https://github.com/advisories/GHSA-h822-r4r5-v8jg).

- Bump commons-beanutils from 1.9.3 to 1.9.4
- Bump jackson-core from 2.9.9 to 2.10.0
- Bump jackson-databind from 2.9.9 to 2.9.10.1
- Bump jacoco-maven-plugin from 0.8.4 to 0.8.5
- Bump junit-jupiter-api from 5.5.0 to 5.5.2
- Bump junit-jupiter-engine from 5.5.0 to 5.5.2
- Bump junit-platform-launcher from 1.5.0 to 1.5.2
- Bump hamcrest from 2.1 to 2.2
- Bump maven-javadoc-plugin from 3.1.0 to 3.1.1
- Bump maven-source-plugin from 3.1.0 to 3.2.0
- Bump mockito-core from 2.28.2 to 3.1.0


### 6.7.4 (06 July 2019)

- Bump junit-jupiter-api from 5.5.0-M1 to 5.5.0
- Bump junit-jupiter-engine from 5.5.0-M1 to 5.5.0
- Bump junit-platform-launcher from 1.4.2 to 1.5.0


### 6.7.3 (30 May 2019)

Fixes moderate severity issue
[CVE-2019-12086](https://nvd.nist.gov/vuln/detail/CVE-2019-12086)

- Test and build with openjdk8
- Bump jacoco-maven-plugin from 0.8.3 to 0.8.4
- Bump maven-compiler-plugin from 3.8.0 to 3.8.1
- Bump maven-source-plugin from 3.0.1 to 3.1.0
- Bump maven-checkstyle-plugin from 3.0.0 to 3.1.0
- Bump commons-configuration2 from 2.4 to 2.5
- Bump mockito-core from 2.27.0 to 2.28.2
- Bump jackson-core from 2.9.8 to 2.9.9
- Bump jackson-databind from 2.9.8 to 2.9.9


### 6.7.2 (01 May 2019)

- Don't create duplicate TestCases. Fixes
  [Issue #50](https://github.com/kiwitcms/junit-plugin/issues/50)
- Still Beta quality

### 6.7.1 (15 April 2019)

- Don't create 2 test runs (Aneta Petkova). Fixes
  [Issue #41](https://github.com/kiwitcms/junit-plugin/issues/41)
- Still Beta quality

### 6.7.0 (10 April 2019)

- Compatible with Kiwi TCMS v6.7 or newer. Fixes
  [Issue #42](https://github.com/kiwitcms/junit-plugin/issues/42)
- Version string updated to reflect minimum server version
- Still Beta quality

### 1.0.3 (21 March 2019)

- Initial release. Beta quality!


## Hacking

You need your `~/.m2/settings.xml` to look something like this

    <settings>
      <servers>
        <server>
          <id>ossrh</id>
          <username>kiwitcms-bot</username>
          <password>***</password>
        </server>
      </servers>

      <profiles>
        <profile>
          <id>ossrh</id>
          <activation>
            <activeByDefault>true</activeByDefault>
          </activation>
          <properties>
            <gpg.executable>gpg2</gpg.executable>
            <gpg.keyname>EC1CEB7CDFA79FB5</gpg.keyname>
          </properties>
        </profile>
      </profiles>
    </settings>

To push a new release/SNAPSHOT update the version string in `pom.xml` and do

    export GPG_TTY=$(tty)
    mvn clean deploy -P release

NOTE: try `ssh -X` if Maven fails to start GnuPG. It needs to ask for a password
which needs a terminal or X!

WARNING: consider setting `junit.jupiter.extensions.autodetection.enabled=false`
im `pom.xml` if you are having problems building locally!

You will need Maven >= 3.3.9 which on RHEL/CentOS system can be obtained via
SoftwareCollections:

    scl enable rh-maven36 /bin/bash
