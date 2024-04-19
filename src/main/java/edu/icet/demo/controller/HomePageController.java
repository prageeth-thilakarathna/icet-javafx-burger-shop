package edu.icet.demo.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class HomePageController {
    @FXML
    private AnchorPane anchorPane;

    public void placeOrderAction() throws IOException {
        Parent load = new FXMLLoader(getClass().getResource("/view/placeOrder.fxml")).load();
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(load);
    }

    public void searchOrderAction() throws IOException {
        Parent parent = new FXMLLoader(getClass().getResource("/view/searchOrder.fxml")).load();
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(parent);
    }

    public void exitAction(ActionEvent actionEvent) {
    }

    public void searchBestCustomerAction() throws IOException {
        Parent load = new FXMLLoader(getClass().getResource("/view/searchBestCustomer.fxml")).load();
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(load);
    }
}
