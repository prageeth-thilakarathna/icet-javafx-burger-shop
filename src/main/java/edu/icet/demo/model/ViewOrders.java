package edu.icet.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ViewOrders {
    private String orderId;
    private String customerId;
    private String name;
    private String quantity;
    private String orderValue;
}
