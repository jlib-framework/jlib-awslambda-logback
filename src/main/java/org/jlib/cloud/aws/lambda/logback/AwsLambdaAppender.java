package org.jlib.cloud.aws.lambda.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.LambdaRuntime;
import com.amazonaws.services.lambda.runtime.LambdaRuntimeInternal;
import lombok.Setter;

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
