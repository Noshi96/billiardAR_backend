package pl.ncdc.billiard.configurations;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketAppender extends AppenderBase<ILoggingEvent> implements SmartLifecycle {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public WebSocketAppender(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    protected void append(ILoggingEvent event) {
        simpMessagingTemplate.convertAndSend("/log", event.getMessage());
    }

    @Override
    public boolean isRunning() {
        return isStarted();
    }
}
