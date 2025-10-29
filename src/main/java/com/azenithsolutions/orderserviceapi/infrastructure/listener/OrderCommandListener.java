package com.azenithsolutions.orderserviceapi.infrastructure.listener;

import com.azenithsolutions.orderserviceapi.domain.command.OrderCommandDTO;
import com.azenithsolutions.orderserviceapi.infrastructure.dto.OrderRegisterRequestDTO;
import com.azenithsolutions.orderserviceapi.infrastructure.producer.OrderCreatedEventPublisher;
import com.azenithsolutions.orderserviceapi.web.mappers.OrderRestMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.azenithsolutions.orderserviceapi.application.usecase.CreateOrderUseCase;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderCommandListener {
    private final CreateOrderUseCase create;
    private final OrderCreatedEventPublisher publisher;

    @RabbitListener(
        queues = "${broker.order.command.queue}",
        containerFactory = "orderCommandListenerContainerFactory"
    )
    public void onMessage(OrderRegisterRequestDTO order) {
        try {
            System.out.println("Trying Create");
            OrderCommandDTO command = OrderRestMapper.toCommand(order);
            create.execute(command);
            publisher.publish("Order Created");
            System.out.println("Order Created");
        } catch (Exception exception) {
            publisher.publish("Order Creation Failed");
            System.out.println("Error Creating Order");
            throw exception;
        }
    }
}
