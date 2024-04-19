package edu.icet.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BestCustomer {
    private String customerId;
    private String name;
    private String total;
}
