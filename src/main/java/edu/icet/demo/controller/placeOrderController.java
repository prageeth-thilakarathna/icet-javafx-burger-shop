package edu.icet.demo.controller;

import edu.icet.demo.database.loadDriver;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class placeOrderController {
    public Label orderID;
    public TextField customerID;
    public TextField customerName;
    public TextField qty;
    public Label customerIdError;
    public Label qtyError;
    public Button btnPlaceOrder;
    public Button btnCancel;

    public void placeOrderAction(ActionEvent actionEvent) {
        try{
            String sql = "SELECT * FROM customer WHERE id=''";
            loadDriver instance = loadDriver.getInstance();
            Connection connection = instance.getConnection();
            Statement stm = connection.createStatement();

            

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void cancelAction(ActionEvent actionEvent) {
    }
}
