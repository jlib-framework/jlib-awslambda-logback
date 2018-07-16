# jlib AWS Lambda Logback Appender

Use [SLF4J](https://www.slf4j.org/)/[Logback](https://logback.qos.ch/) for logging to [CloudWatch Logs](https://docs.aws.amazon.com/AmazonCloudWatch/latest/logs/WhatIsCloudWatchLogs.html) from AWS Lambda code.

Log the request id provided by the AWS Lambda runtime 

#### Usage
##### Dependency
###### Gradle (build.gradle)
    dependencies {
        implementation 'org.jlib:jlib-awslambda-logback:1.0.0'
    }
    
###### Maven (pom.xml)
    <dependencies>
        <dependency>
            <groupId>org.jlib/groupId>
            <artifactId>jlib-awslambda-logback/artifactId>
            <version>1.0.0</version>
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
