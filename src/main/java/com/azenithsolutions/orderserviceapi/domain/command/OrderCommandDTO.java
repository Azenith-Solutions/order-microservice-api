package com.azenithsolutions.orderserviceapi.domain.command;

import com.azenithsolutions.orderserviceapi.domain.enums.OrderStatus;

import java.time.LocalDateTime;

public record OrderCommandDTO(
        String codigo,
        String nomeComprador,
        String emailComprador,
        String cnpj,
        String valor,
        OrderStatus status,
        String telCelular,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
