package edu.icet.demo.controller;

import edu.icet.demo.database.LoadDriver;
import lombok.Getter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

public class CenterController {
    @Getter
    private static final CenterController instance = new CenterController();

    public static final double BURGERPRICE = 500.00;
    public static final int PREPARING = 0;
    public static final int DELIVERED = 1;
    public static final int CANCEL = 2;

    public static final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private CenterController(){}

    public ResultSet getCustomerDetails(int customerId) throws SQLException {
        String sql = "SELECT * FROM customer WHERE id="+customerId;

        LoadDriver instance = LoadDriver.getInstance();
        Connection connection = instance.getConnection();
        Statement stm = connection.createStatement();
        ResultSet resultSet = stm.executeQuery(sql);
        return resultSet;
    }






}
