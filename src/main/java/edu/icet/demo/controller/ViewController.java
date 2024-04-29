package edu.icet.demo.controller;

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
    private void preparingOrdersAction() throws IOException {
        Parent load = new FXMLLoader(getClass().getResource("/view/viewPreparing.fxml")).load();
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(load);
    }

    @FXML
    private void cancelOrdersAction() throws IOException {
        Parent load = new FXMLLoader(getClass().getResource("/view/viewCancel.fxml")).load();
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(load);
    }
}
