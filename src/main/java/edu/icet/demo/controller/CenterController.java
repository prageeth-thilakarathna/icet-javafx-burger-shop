package edu.icet.demo.controller;

import edu.icet.demo.database.LoadDriver;
import javafx.scene.control.Alert;
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

    public static final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    public static final Alert alert = new Alert(Alert.AlertType.NONE);

    private CenterController() {
    }

    public ResultSet getCustomerDetails(int customerId) throws SQLException {
        String sql = "SELECT * FROM customer WHERE id=" + customerId;
        Connection connection = LoadDriver.getInstance().getConnection();
        Statement statement = connection.createStatement();
        return statement.executeQuery(sql);
    }

    public ResultSet getOrderDetails(String orderId) throws SQLException {
        String sql = "SELECT * FROM order_details WHERE orderId='" + orderId + "'";
        Connection connection = LoadDriver.getInstance().getConnection();
        Statement statement = connection.createStatement();
        return statement.executeQuery(sql);
    }

    public ResultSet getCustomers() throws SQLException {
        String sql = "SELECT * FROM customer";
        Connection connection = LoadDriver.getInstance().getConnection();
        Statement statement = connection.createStatement();
        return statement.executeQuery(sql);
    }

    public ResultSet getOrdersByCustomerId(String customerId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE customerId=" + customerId;
        Connection connection = LoadDriver.getInstance().getConnection();
        Statement statement = connection.createStatement();
        return statement.executeQuery(sql);
    }

    public ResultSet getOrder(String orderId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE id='" + orderId + "'";
        Connection connection = LoadDriver.getInstance().getConnection();
        Statement statement = connection.createStatement();
        return statement.executeQuery(sql);
    }

    public ResultSet getAllOrders() throws SQLException {
        String sql = "SELECT * FROM orders";
        Connection connection = LoadDriver.getInstance().getConnection();
        Statement statement = connection.createStatement();
        return statement.executeQuery(sql);
    }

}
