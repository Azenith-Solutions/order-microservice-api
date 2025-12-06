package com.azenithsolutions.orderserviceapi.domain.mappers;

import com.azenithsolutions.orderserviceapi.domain.model.Order;
import com.azenithsolutions.orderserviceapi.domain.command.OrderCommandDTO;

public class OrderDomainMapper {
    public static Order toDomain(OrderCommandDTO command) {
        Order domain =  new Order();
        domain.setCodigo(command.codigo());
        domain.setNomeComprador(command.nomeComprador());
        domain.setEmailComprador(command.emailComprador());
        domain.setCnpj(command.cnpj());
        domain.setValor(command.valor());
        domain.setStatus(command.status());
        domain.setTelCelular(command.telCelular());
        domain.setCreatedAt(command.createdAt());
        domain.setUpdatedAt(command.updatedAt());
        return domain;
    };
}
