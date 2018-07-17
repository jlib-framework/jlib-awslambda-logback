# jlib AWS Lambda Logback Appender

#### SLF4J/Logback for AWS Lambda
The _jlib AWS Lambda Logback appender_ library allows to log through [SLF4J](https://www.slf4j.org/)/[Logback](https://logback.qos.ch/) 
to [CloudWatch Logs](https://docs.aws.amazon.com/AmazonCloudWatch/latest/logs/WhatIsCloudWatchLogs.html) 
from [AWS Lambda](https://aws.amazon.com/de/lambda) code.

#### Features
##### Trouble-free multi-line logging
Instead of writing to the standard output, this library uses the `RuntimeLogger` provided by the AWS Lambda SDK.
This allows to avoid issues with exception stacktraces or other messages spanning across multiple lines.
Consequently, there is no need for complex handlings of newline characters, 
e.g. replacement by carriage return characters.

For example, when working with CloudWatch Logs, a multi-line-message will be registered as a single event,
not as multiple events, one per line written to the log.

##### AWS Request Id in every log message
The library also allows to include the `AWSRequestId` provided by the AWS Lambda runtime.
Simply by including an MDC reference to this id in the encoder pattern, will add it to every single log message. 
The encoder pattern is specified in the Logback configuration, e.g. `logback.xml`.
Please refer to the Logback documentation for details on how to use the [MDC](https://logback.qos.ch/manual/mdc.html). 

##### Save up to 700kB for faster deployment and cold starts
One goal when building Lambda applications should be to keep the application archive as small as possible.
This allows for a faster deployment of the application when uploading its archive to AWS.
It also soeeds up the initial loading of the application, also known as _cold start_.

_Up to 700kB_ can be saved when using this library.

When depending on `logback-classic` in Maven or Gradle, 
the dependency tree includes a transitive dependency to `com.sun.mail:javax.mail`.
When building an archive for the Lambda application,
this transitive dependency is included in the archive.
It raises the archive size by around 700kB.
This happens whether the application is packaged as an uber-jar or as a zip archive.
This library excludes this transitive dependency 
in order to minimize the archive size of the Lambda application.

Alternative approaches using other Logging implementations for SLF4J produce archives at least
about 100kB larger than the archive including this library.

#### Usage
##### Dependency
###### Gradle (build.gradle)
    dependencies {
        implementation 'org.slf4j:slf4j-api:1.8.0-beta2'
        runtimeOnly 'org.jlib:jlib-awslambda-logback:1.0.0'
    }
    
###### Maven (pom.xml)
    <dependencies>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.8.0-beta2</version>
        </dependency>
        <dependency>
            <groupId>org.jlib/groupId>
            <artifactId>jlib-awslambda-logback/artifactId>
            <version>1.0.0</version>
            <scope>runtime</scope>
        </dependency>
    </dependencies>

##### Configuration
###### XML (src/main/resources/logback.xml)
    <configuration>
    
        <appender name="lambda" class="org.jlib.cloud.aws.lambda.logback.AwsLambdaAppender">
            <encoder type="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
                <pattern>[%d{yyyy-MM-dd HH:mm:ss.SSS}] &lt;%-36X{AWSRequestId:-request-id-not-set-by-lambda-runtime}&gt; %-5level %logger{10} - %msg%n</pattern>
            </encoder>
        </appender>
    
        <root level="INFO">
            <appender-ref ref="lambda" />
        </root>
    
    </configuration>

#### License
Copyright 2018 Igor Akkerman

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
