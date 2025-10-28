package com.azenithsolutions.orderserviceapi.infrastructure.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderCreatedEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    @Value("${broker.order.event.exchange}")
    private String exchange;
    @Value("${broker.order.event.queue}")
    private String queue;
    @Value("${broker.order.event.routing-key}")
    private String routingKey;
    @Value("${broker.order.event.dlx}")
    private String dlx;
    @Value("${broker.order.event.dlq}")
    private String dlq;

    public void publish(String event) {
        try {
            System.out.println("Trying Publish");
            rabbitTemplate.convertAndSend(exchange, routingKey, event);
            System.out.println("Order Published");
        } catch (Exception exception) {
            throw new RuntimeException("Failed to send order created event", exception);
        }
    }
}
