package edu.icet.demo.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ViewController {
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private void cancelAction() throws IOException {
        Parent load = new FXMLLoader(getClass().getResource("/view/homePage.fxml")).load();
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(load);
    }

    @FXML
    private void deliveredOrdersAction() throws IOException {
        Parent load = new FXMLLoader(getClass().getResource("/view/viewDelivered.fxml")).load();
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(load);
    }

    @FXML
    private void preparingOrdersAction(ActionEvent actionEvent) {
    }

    @FXML
    private void cancelOrdersAction(ActionEvent actionEvent) {
    }
}
