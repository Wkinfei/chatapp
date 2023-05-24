package nus.iss.chatapp.com.server.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@SuppressWarnings("deprecation")
@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer{

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }

    @Override 
    protected void configureInbound(
    MessageSecurityMetadataSourceRegistry messages) { 
        messages
        // TODO: add this
        // .simpDestMatchers("/secured/**").authenticated()
        .anyMessage().permitAll(); 
    }

}