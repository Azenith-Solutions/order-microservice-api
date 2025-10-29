package com.azenithsolutions.orderserviceapi.application.usecase;

import com.azenithsolutions.orderserviceapi.domain.model.Order;
import com.azenithsolutions.orderserviceapi.domain.gateway.OrderGateway;
import com.azenithsolutions.orderserviceapi.domain.command.OrderCommandDTO;
import com.azenithsolutions.orderserviceapi.domain.mappers.OrderDomainMapper;

public class CreateOrderUseCase {
    private final OrderGateway repository;

    public CreateOrderUseCase(OrderGateway repository) { this.repository = repository; }

    public Order execute(OrderCommandDTO command) {
        System.out.println("Creating orderUSECAS");
        Order order = OrderDomainMapper.toDomain(command);
        return repository.save(order);
    }
}
