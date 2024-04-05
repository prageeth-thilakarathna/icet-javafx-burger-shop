package edu.icet.demo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class orderDetails {
    private String orderID;
    private String customerID;
    private String name;
    private int qty;
    private int status;
    public orderDetails next;

    public static final double BURGERPRICE = 500.00;

    public static final int PREPARING = 0;
    public static final int DELIVERED = 1;
    public static final int CANCEL = 2;
}
