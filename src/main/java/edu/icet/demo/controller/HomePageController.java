package edu.icet.demo.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class HomePageController {
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private void placeOrderAction() throws IOException {
        Parent parent = new FXMLLoader(getClass().getResource("/view/placeOrder.fxml")).load();
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(parent);
    }

    @FXML
    private void searchOrderAction() throws IOException {
        Parent parent = new FXMLLoader(getClass().getResource("/view/searchOrder.fxml")).load();
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(parent);
    }

    @FXML
    private void exitAction() {
        System.exit(0);
    }

    @FXML
    private void searchBestCustomerAction() throws IOException {
        Parent parent = new FXMLLoader(getClass().getResource("/view/searchBestCustomer.fxml")).load();
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(parent);
    }

    @FXML
    private void searchCustomerAction() throws IOException {
        Parent parent = new FXMLLoader(getClass().getResource("/view/searchCustomer.fxml")).load();
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(parent);
    }

    @FXML
    private void viewOrdersAction() throws IOException {
        Parent parent = new FXMLLoader(getClass().getResource("/view/view.fxml")).load();
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(parent);
    }
}
