package com.azenithsolutions.orderserviceapi.web.mappers;

import com.azenithsolutions.orderserviceapi.domain.model.Order;
import com.azenithsolutions.orderserviceapi.domain.command.OrderCommandDTO;
import com.azenithsolutions.orderserviceapi.infrastructure.dto.OrderRegisterRequestDTO;
import com.azenithsolutions.orderserviceapi.web.dto.OrderRequestDTO;
import com.azenithsolutions.orderserviceapi.web.rest.OrderRest;

public class OrderRestMapper {
    public static OrderRequestDTO toRest(Order domain) {
        if (domain == null) return null;

        OrderRequestDTO orderDTO = new OrderRequestDTO();
        orderDTO.setIdPedido(domain.getIdPedido());
        orderDTO.setCodigo(domain.getCodigo());
        orderDTO.setNomeComprador(domain.getNomeComprador());
        orderDTO.setEmailComprador(domain.getEmailComprador());
        orderDTO.setCnpj(domain.getCnpj());
        orderDTO.setValor(domain.getValor());
        orderDTO.setStatus(domain.getStatus());
        orderDTO.setTelCelular(domain.getTelCelular());
        orderDTO.setCreatedAt(domain.getCreatedAt());
        orderDTO.setUpdatedAt(domain.getUpdatedAt());

        return orderDTO;
    }

    public static Order toDomain(OrderRequestDTO rest) {
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

    public static OrderCommandDTO toCommand(OrderRequestDTO dto) {
        if (dto == null) return null;

        return new OrderCommandDTO(
                dto.getCodigo(),
                dto.getNomeComprador(),
                dto.getEmailComprador(),
                dto.getCnpj(),
                dto.getValor(),
                dto.getStatus(),
                dto.getTelCelular(),
                dto.getCreatedAt(),
                dto.getUpdatedAt());
    }
}
