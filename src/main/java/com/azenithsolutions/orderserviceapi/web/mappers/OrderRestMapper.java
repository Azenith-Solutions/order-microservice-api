package com.azenithsolutions.orderserviceapi.web.mappers;

import com.azenithsolutions.orderserviceapi.domain.model.Order;
import com.azenithsolutions.orderserviceapi.domain.command.OrderCommandDTO;
import com.azenithsolutions.orderserviceapi.infrastructure.dto.OrderRegisterRequestDTO;
import com.azenithsolutions.orderserviceapi.web.rest.OrderRest;

public class OrderRestMapper {
    public static OrderRest toRest(Order domain) {
        if (domain == null) return null;

        OrderRest orderRest = new OrderRest();
        orderRest.setIdPedido(domain.getIdPedido());
        orderRest.setCodigo(domain.getCodigo());
        orderRest.setNomeComprador(domain.getNomeComprador());
        orderRest.setEmailComprador(domain.getEmailComprador());
        orderRest.setCnpj(domain.getCnpj());
        orderRest.setValor(domain.getValor());
        orderRest.setStatus(domain.getStatus());
        orderRest.setTelCelular(domain.getTelCelular());
        orderRest.setCreatedAt(domain.getCreatedAt());
        orderRest.setUpdatedAt(domain.getUpdatedAt());

        return orderRest;
    }

    public static Order toDomain(OrderRest rest) {
        if (rest == null) return null;

        Order domain = new Order();
        domain.setIdPedido(rest.getIdPedido());
        domain.setCodigo(rest.getCodigo());
        domain.setNomeComprador(rest.getNomeComprador());
        domain.setEmailComprador(rest.getEmailComprador());
        domain.setCnpj(rest.getCnpj());
        domain.setValor(rest.getValor());
        domain.setStatus(rest.getStatus());
        domain.setTelCelular(rest.getTelCelular());
        domain.setCreatedAt(rest.getCreatedAt());
        domain.setUpdatedAt(rest.getUpdatedAt());

        return domain;
    }

    public static OrderCommandDTO toCommand(OrderRegisterRequestDTO dto) {
        if (dto == null) return null;

        return new OrderCommandDTO(
                dto.codigo(),
                dto.nomeComprador(),
                dto.emailComprador(),
                dto.cnpj(),
                dto.valor(),
                dto.status(),
                dto.telCelular(),
                dto.createdAt(),
                dto.updatedAt());
    }
}
