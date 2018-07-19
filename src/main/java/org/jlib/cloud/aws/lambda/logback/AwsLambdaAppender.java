/*
 * jlib - Open Source Java Library
 *
 *     www.jlib.org
 *
 *
 *     Copyright 2005-2018 Igor Akkerman
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package org.jlib.cloud.aws.lambda.logback;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.LambdaRuntime;
import com.amazonaws.services.lambda.runtime.LambdaRuntimeInternal;

import org.slf4j.MDC;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.encoder.Encoder;

import lombok.Setter;

/**
 * <a href="https://logback.qos.ch/">Logback</a> {@link Appender}
 * to be used in <a href="https://aws.amazon.com/de/lambda">AWS Lambda</a> applications,
 * targeting <a href="https://docs.aws.amazon.com/AmazonCloudWatch/latest/logs/WhatIsCloudWatchLogs.html">CloudWatch Logs</a>.
 * <p>
 * Instead of writing to the standard output, this {@link Appender} passes the messages to {@link LambdaLogger}.
 * This allows to avoid issues with exception stacktraces or other messages spanning across multiple lines.
 * <p>
 * This {@link Appender} ensures that Logback's {@link MDC} contains the <code>AWSRequestId</code>, which can be added to the log pattern.
 * <p>
 * Example XML configuration:
 * <pre>{@code <configuration>
 *
 *     <appender name="awslambda" class="org.jlib.cloud.aws.lambda.logback.AwsLambdaAppender">
 *         <encoder type="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
 *             <pattern>[%d{yyyy-MM-dd HH:mm:ss.SSS}] &lt;%-36X{AWSRequestId:-request-id-not-set-by-lambda-runtime}&gt; %-5level %logger{10} - %msg%n</pattern>
 *         </encoder>
 *     </appender>
 *
 *     <root level="INFO">
 *         <appender-ref ref="awslambda" />
 *     </root>
 *
 * </configuration>
 * }</pre>
 */
public class AwsLambdaAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private final LambdaLogger logger = LambdaRuntime.getLogger();

    @Setter
    private Encoder<ILoggingEvent> encoder;

    public AwsLambdaAppender() {
        LambdaRuntimeInternal.setUseLog4jAppender(true);
    }

    @Override
    protected void append(ILoggingEvent event) {
        logger.log(encoder.encode(event));
    }
}
