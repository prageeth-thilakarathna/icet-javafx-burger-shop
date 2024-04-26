package edu.icet.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Orders {
    private String orderId;
    private String orderQuantity;
    private String totalValue;
}
