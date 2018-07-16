package org.jlib.cloud.aws.lambda.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.MDC;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static java.lang.System.lineSeparator;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class AwsLambdaAppenderTest {

    private ByteArrayOutputStream byteOut;
    private PrintStream originalStandardOut;

    @BeforeEach
    public void mockStandardOut() {
        byteOut = new ByteArrayOutputStream();

        originalStandardOut = System.out;

        System.setOut(new PrintStream(byteOut));
    }

    @AfterEach
    public void revertStandardOut() {
        System.setOut(originalStandardOut);
    }

    @Test
    public void xmlConfigNoRequestId() {

        // given
        // logback.xml present

        // when
        log.info("14m6d4 15 höt");

        // then
        assertThat(byteOut.toString())
                .matches("\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\] NO-REQUEST-ID INFO o.j.c.a.l.l.AwsLambdaAppenderTest - 14m6d4 15 höt" + lineSeparator() + "$");
    }

    @Test
    public void xmlConfigDebugShouldNotBeLogged() {

        // given
        // logback.xml present

        // when
        log.debug("14m6d4 15 höt");

        // then
        assertThat(byteOut.toString())
                .isEmpty();
    }

    @Test
    public void xmlConfigMdcRequestId() {

        String requestIdMdcKey = "AWSRequestId";

        // given
        // logback.xml present and
        MDC.put(requestIdMdcKey, "jlib-15-c001");

        // when
        log.info("14m6d4 15 höt");

        // then
        assertThat(byteOut.toString())
                .matches("\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\] jlib-15-c001 INFO o.j.c.a.l.l.AwsLambdaAppenderTest - 14m6d4 15 höt" + lineSeparator() + "$");

        // cleanup
        MDC.remove(requestIdMdcKey);
    }

    @Test
    public void programmaticConfig() {

        // given
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();

        encoder.setPattern("[%level] <%logger> %msg%n");
        encoder.setContext(lc);
        encoder.start();

        AwsLambdaAppender appender = new AwsLambdaAppender();
        appender.setEncoder(encoder);
        appender.setContext(lc);
        appender.start();

        Logger logger = (Logger) LoggerFactory.getLogger("special.Logger");
        logger.addAppender(appender);
        logger.setLevel(Level.DEBUG);
        logger.setAdditive(false); /* set to true if root should log too */

        // when
        logger.debug("14m6d4 15 höt");

        // then
        assertThat(byteOut)
                .hasToString("[DEBUG] <special.Logger> 14m6d4 15 höt" + lineSeparator());
    }

}
