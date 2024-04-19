package edu.icet.demo.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SearchOrderController implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button btnSearchAnotherOrder;
    @FXML
    private Button btnCancel;
    @FXML
    private TextField inputOrderId;
    @FXML
    private Label customerIdDisplay;
    @FXML
    private Label nameDisplay;
    @FXML
    private Label qtyDisplay;
    @FXML
    private Label totalDisplay;
    @FXML
    private Label statusDisplay;

    public void searchAnotherOrderAction() {
        inputOrderId.setText("");
        inputOrderId.setEditable(true);
        customerIdDisplay.setText("");
        nameDisplay.setText("");
        qtyDisplay.setText("");
        totalDisplay.setText("");
        statusDisplay.setText("");
        btnSearchAnotherOrder.setDisable(true);
    }

    public void cancelAction() throws IOException {
        inputOrderId.setText("");
        customerIdDisplay.setText("");
        nameDisplay.setText("");
        qtyDisplay.setText("");
        totalDisplay.setText("");
        statusDisplay.setText("");

        Parent parent = new FXMLLoader(getClass().getResource("/view/homePage.fxml")).load();
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(parent);
    }

    @FXML
    private void orderIdKeyTyped(KeyEvent keyEvent) {
        String value = inputOrderId.getText();

        try {
            ResultSet resultSet = CenterController.getInstance().getOrder(value);

            if (resultSet.next()) {
                customerIdDisplay.setText("0" + resultSet.getString("customerId"));

                ResultSet resultSetCust = CenterController.getInstance().getCustomerDetails(Integer.parseInt("0" + resultSet.getString("customerId")));

                if (resultSetCust.next()) {
                    nameDisplay.setText(resultSetCust.getString("name"));
                    btnSearchAnotherOrder.setDisable(false);
                    inputOrderId.setEditable(false);

                    ResultSet resultSetOrderDetails = CenterController.getInstance().getOrderDetails(value);

                    if (resultSetOrderDetails.next()) {
                        qtyDisplay.setText(resultSetOrderDetails.getString("quantity"));
                        double total = Integer.parseInt(resultSetOrderDetails.getString("quantity")) * CenterController.BURGERPRICE;
                        totalDisplay.setText(CenterController.decimalFormat.format(total));

                        if (resultSetOrderDetails.getInt("status") == 0) {
                            statusDisplay.setText("Preparing...");
                        } else if (resultSetOrderDetails.getInt("status") == 1) {
                            statusDisplay.setText("Delivered");
                        } else {
                            statusDisplay.setText("Canceled");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            CenterController.alert.setAlertType(Alert.AlertType.ERROR);
            CenterController.alert.setContentText(e.getMessage());
            CenterController.alert.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inputOrderId.setEditable(true);
        btnSearchAnotherOrder.setDisable(true);
    }
}
