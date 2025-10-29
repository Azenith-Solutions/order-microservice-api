package com.azenithsolutions.orderserviceapi.infrastructure.config;

import java.util.Map;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RabbitMQConfig {
    @Value("${broker.order.event.exchange}")
    private String orderCreatedEventExchangeName;
    @Value("${broker.order.event.queue}")
    private String orderCreatedEventQueueName;
    @Value("${broker.order.event.routing-key}")
    private String orderCreatedEventRoutingKey;
    @Value("${broker.order.event.dlx}")
    private String orderCreatedEventDlxName;
    @Value("${broker.order.event.dlq}")
    private String orderCreatedEventDlqName;

    @Value("${broker.order.command.exchange}")
    private String orderCommandExchangeName;
    @Value("${broker.order.command.queue}")
    private String orderCommandQueueName;
    @Value("${broker.order.command.routing-key}")
    private String orderCommandRoutingKey;
    @Value("${broker.order.command.dlx}")
    private String orderCommandDlxName;
    @Value("${broker.order.command.dlq}")
    private String orderCommandDlqName;

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public TopicExchange orderCreatedEventExchange() {
        return ExchangeBuilder
                .topicExchange(orderCreatedEventExchangeName)
                .durable(true)
                .build();
    }

    @Bean
    public TopicExchange orderCreatedEventDeadLetterExchange() {
        return ExchangeBuilder
                .topicExchange(orderCreatedEventDlxName)
                .durable(true)
                .build();
    }

    @Bean
    public Queue orderCreatedEventQueue() {
        return QueueBuilder.durable(orderCreatedEventQueueName)
                .withArguments(Map.of(
                        "x-dead-letter-exchange", orderCreatedEventDlxName,
                        "x-dead-letter-routing-key", orderCreatedEventRoutingKey
                ))
                .build();
    }

    @Bean
    public Queue orderCreatedEventDeadLetterQueue() {
        return QueueBuilder.durable(orderCreatedEventDlqName).build();
    }

    @Bean
    public Binding orderCreatedEventBinding(Queue orderCreatedEventQueue, TopicExchange orderCreatedEventExchange) {
        return BindingBuilder
                .bind(orderCreatedEventQueue)
                .to(orderCreatedEventExchange)
                .with(orderCreatedEventRoutingKey);
    }

    @Bean
    public Binding orderCreatedEventDlqBinding(Queue orderCreatedEventDeadLetterQueue, TopicExchange orderCreatedEventDeadLetterExchange) {
        return BindingBuilder
                .bind(orderCreatedEventDeadLetterQueue)
                .to(orderCreatedEventDeadLetterExchange)
                .with(orderCreatedEventRoutingKey);
    }


    @Bean
    public RetryOperationsInterceptor orderRetryInterceptor(RabbitTemplate rabbitTemplate) {
        RepublishMessageRecoverer recoverer = new RepublishMessageRecoverer(
                rabbitTemplate,
                orderCreatedEventDlxName,
                orderCreatedEventRoutingKey
        );

        return RetryInterceptorBuilder.stateless()
                .maxAttempts(3)
                .backOffOptions(1000, 2.0, 10000)
                .recoverer(recoverer)
                .build();
    }

    @Bean
    public RetryOperationsInterceptor orderCommandRetryInterceptor(RabbitTemplate rabbitTemplate) {
    RepublishMessageRecoverer recoverer = new RepublishMessageRecoverer(
        rabbitTemplate,
        orderCommandDlxName,
        orderCommandRoutingKey
    );

    return RetryInterceptorBuilder.stateless()
        .maxAttempts(3)
        .backOffOptions(1000, 2.0, 10000)
        .recoverer(recoverer)
        .build();
    }

    @Bean
    public RabbitListenerContainerFactory<?> orderListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jacksonMessageConverter,
            RetryOperationsInterceptor orderRetryInterceptor) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(2);
        factory.setMaxConcurrentConsumers(5);
        factory.setDefaultRequeueRejected(false);
        factory.setMessageConverter(jacksonMessageConverter);
        factory.setAdviceChain(orderRetryInterceptor);
        return factory;
    }

    @Bean
    public RabbitListenerContainerFactory<?> orderCommandListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jacksonMessageConverter,
            RetryOperationsInterceptor orderCommandRetryInterceptor) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(2);
        factory.setMaxConcurrentConsumers(5);
        factory.setDefaultRequeueRejected(false);
        factory.setMessageConverter(jacksonMessageConverter);
        factory.setAdviceChain(orderCommandRetryInterceptor);
        return factory;
    }

    @Bean
    public DirectExchange orderCommandExchange() {
        return ExchangeBuilder
                .directExchange(orderCommandExchangeName)
                .durable(true)
                .build();
    }

    @Bean
    public DirectExchange orderCommandDeadLetterExchange() {
        return ExchangeBuilder
                .directExchange(orderCommandDlxName)
                .durable(true)
                .build();
    }

    @Bean
    public Queue orderCommandQueue() {
        return QueueBuilder.durable(orderCommandQueueName)
                .withArguments(Map.of(
                        "x-dead-letter-exchange", orderCommandDlxName,
                        "x-dead-letter-routing-key", orderCommandRoutingKey
                ))
                .build();
    }

    @Bean
    public Queue orderCommandDeadLetterQueue() {
        return QueueBuilder.durable(orderCommandDlqName).build();
    }

    @Bean
    public Binding orderCommandBinding(Queue orderCommandQueue, DirectExchange orderCommandExchange) {
        return BindingBuilder
                .bind(orderCommandQueue)
                .to(orderCommandExchange)
                .with(orderCommandRoutingKey);
    }

    @Bean
    public Binding orderCommandDlqBinding(Queue orderCommandDeadLetterQueue, DirectExchange orderCommandDeadLetterExchange) {
        return BindingBuilder
                .bind(orderCommandDeadLetterQueue)
                .to(orderCommandDeadLetterExchange)
                .with(orderCommandRoutingKey);
    }

    @Bean
    public MessageConverter jacksonMessageConverter(ObjectMapper objectMapper) {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);
        converter.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.INFERRED);
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jacksonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jacksonMessageConverter);
        return template;
    }
}
