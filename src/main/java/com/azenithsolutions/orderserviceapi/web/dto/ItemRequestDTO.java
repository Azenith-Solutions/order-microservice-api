package com.azenithsolutions.orderserviceapi.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Item", description = "Item representation for Order")
public class ItemRequestDTO {
    @Schema(description = "Component ID", example = "1")
    private Long fkComponente;
    
    @Schema(description = "Order ID", example = "1")
    private Long fkPedido;
    
    @Schema(description = "Quantity in cart", example = "3")
    private Integer quantidadeCarrinho;
}