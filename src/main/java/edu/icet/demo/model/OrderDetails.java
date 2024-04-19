package edu.icet.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderDetails {
    private String orderID;
    private int customerID;
    private String name;
    private int qty;
    private int status;
}
