package edu.icet.demo.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class homePageController {
    public Button btnPlaceOrder;
    public Button btnSearchOrder;
    public Button btnExit;
    public AnchorPane anchorPane;

    public void placeOrderAction(ActionEvent actionEvent) throws IOException {
        Parent load = new FXMLLoader(getClass().getResource("/view/placeOrder.fxml")).load();
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(load);
    }

    public void searchOrderAction(ActionEvent actionEvent) {
    }

    public void exitAction(ActionEvent actionEvent) {
    }
}
