package edu.icet.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class orderDetails {
    private String orderID;
    private int customerID;
    private String name;
    private int qty;
    private int status;
}
