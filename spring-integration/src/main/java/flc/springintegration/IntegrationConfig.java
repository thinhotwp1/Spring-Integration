package flc.springintegration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

@Configuration
public class IntegrationConfig {

    private static final Logger logger = LoggerFactory.getLogger(IntegrationConfig.class);

    @Bean
    public MessageChannel flow1Channel() {
        return createLoggingChannel("Flow1Channel");
    }

    @Bean
    public MessageChannel flow2Channel() {
        return createLoggingChannel("Flow2Channel");
    }

    @Bean
    public IntegrationFlow flow1() {
        return f -> f
                .channel(flow1Channel())
                .handle((payload, headers) -> {
                    String result = payload + " + Flow1";
                    logger.info("Flow1 processed input: {}", payload);
                    logger.info("Flow1 processed output: {}", result);
                    return result;
                });
    }

    @Bean
    public IntegrationFlow flow2() {
        return f -> f
                .channel(flow2Channel())
                .handle((payload, headers) -> {
                    String result = payload + " + Flow2";
                    logger.info("Flow2 processed input: {}", payload);
                    logger.info("Flow2 processed output: {}", result);
                    return result;
                });
    }

    //
    private MessageChannel createLoggingChannel(String channelName) {
        DirectChannel channel = new DirectChannel();
        channel.addInterceptor(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                logger.info("{} pre-send: {}", channelName, message);
                return message;
            }

            @Override
            public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
                logger.info("{} post-send: {}", channelName, message);
                if (ex != null) {
                    logger.error("Exception during send on {}: ", channelName, ex);
                }
            }
        });
        return channel;
    }

    @MessagingGateway
    public interface ProcessGateway {

        @Gateway(requestChannel = "flow1Channel")
        String processInFlow1(String input);

        @Gateway(requestChannel = "flow2Channel")
        String processInFlow2(String input);
    }
}
