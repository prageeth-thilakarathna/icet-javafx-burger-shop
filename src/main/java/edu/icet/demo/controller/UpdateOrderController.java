package edu.icet.demo.controller;

import edu.icet.demo.database.LoadDriver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class UpdateOrderController implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button btnUpdateOrder;
    @FXML
    private ComboBox<String> selectOrderStatus;
    @FXML
    private TextField orderIdInput;
    @FXML
    private TextField customerIdDisplay;
    @FXML
    private TextField nameDisplay;
    @FXML
    private TextField quantityInput;
    @FXML
    private Label totalDisplay;
    @FXML
    private Label orderStatusErrorDisplay;
    @FXML
    private Button btnRefresh;
    @FXML
    private Label quantityErrorDisplay;
    private int searchQuantity;

    @FXML
    private void updateOrderAction() {
        int quantity = Integer.parseInt(quantityInput.getText());
        int status;

        if(selectOrderStatus.getValue().equals("Preparing")){
            status = 0;
        } else if(selectOrderStatus.getValue().equals("Delivered")){
            status = 1;
        } else {
            status = 2;
        }

        String sql = "UPDATE order_details SET quantity=?, status=? WHERE orderId=?";

        try{
            Connection connection = LoadDriver.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, quantity);
            preparedStatement.setInt(2, status);
            preparedStatement.setString(3, orderIdInput.getText());

            preparedStatement.executeUpdate();

            String tempId = orderIdInput.getText();
            orderIdInput.setEditable(true);
            orderIdInput.setText("");
            customerIdDisplay.setText("");
            nameDisplay.setText("");
            quantityInput.setText("");
            totalDisplay.setText("0.00");
            selectOrderStatus.setDisable(true);
            quantityInput.setDisable(true);
            btnUpdateOrder.setDisable(true);
            btnRefresh.setDisable(true);

            CenterController.alert.setAlertType(Alert.AlertType.CONFIRMATION);
            CenterController.alert.setContentText("Update "+tempId+" order successfully...");
            CenterController.alert.show();

        } catch (SQLException e) {
            CenterController.alert.setAlertType(Alert.AlertType.ERROR);
            CenterController.alert.setContentText(e.getMessage());
            CenterController.alert.show();
        }
    }

    @FXML
    private void cancelAction() throws IOException {
        orderIdInput.setEditable(true);
        orderIdInput.setText("");
        customerIdDisplay.setText("");
        nameDisplay.setText("");
        quantityInput.setText("");
        totalDisplay.setText("0.00");
        selectOrderStatus.setDisable(true);
        quantityInput.setDisable(true);
        btnUpdateOrder.setDisable(true);
        btnRefresh.setDisable(true);
        orderStatusErrorDisplay.setText("");

        Parent parent = new FXMLLoader(getClass().getResource("/view/homePage.fxml")).load();
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(parent);
    }

    @FXML
    private void orderIdKeyTyped() {
        String value = orderIdInput.getText();

        try {
            ResultSet resultSet = CenterController.getInstance().getOrder(value);

            if (resultSet.next()) {
                orderIdInput.setEditable(false);
                btnRefresh.setDisable(false);
                customerIdDisplay.setText("0" + resultSet.getString("customerId"));

                ResultSet resultSetCustomer = CenterController.getInstance().getCustomerDetails(Integer.parseInt("0" + resultSet.getString("customerId")));

                if (resultSetCustomer.next()) {
                    nameDisplay.setText(resultSetCustomer.getString("name"));

                    ResultSet resultSetOrderDetails = CenterController.getInstance().getOrderDetails(value);

                    if (resultSetOrderDetails.next()) {
                        quantityInput.setText(resultSetOrderDetails.getString("quantity"));
                        searchQuantity = resultSetOrderDetails.getInt("quantity");
                        double total = Integer.parseInt(resultSetOrderDetails.getString("quantity")) * CenterController.BURGERPRICE;
                        totalDisplay.setText(CenterController.decimalFormat.format(total));

                        if (resultSetOrderDetails.getInt("status") == 0) {
                            selectOrderStatus.setValue("Preparing");
                            selectOrderStatus.setDisable(false);
                            quantityInput.setDisable(false);
                        } else if (resultSetOrderDetails.getInt("status") == 1) {
                            selectOrderStatus.setValue("Delivered");
                            orderStatusErrorDisplay.setText("This Order is already delivered.\nSorry, you can not update this order.");
                        } else if (resultSetOrderDetails.getInt("status") == 2) {
                            selectOrderStatus.setValue("Canceled");
                            orderStatusErrorDisplay.setText("This Order is already cancelled.\nSorry, you can not update this order.");
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

    @FXML
    private void refreshAction() {
        orderIdInput.setEditable(true);
        orderIdInput.setText("");
        customerIdDisplay.setText("");
        nameDisplay.setText("");
        quantityInput.setText("");
        totalDisplay.setText("0.00");
        selectOrderStatus.setDisable(true);
        quantityInput.setDisable(true);
        btnUpdateOrder.setDisable(true);
        btnRefresh.setDisable(true);
        orderStatusErrorDisplay.setText("");
    }

    @FXML
    private void selectOrderStatusAction() {
        updateButtonVisibility();
    }

    @FXML
    private void quantityKeyPressed(KeyEvent keyEvent) {
        String value = quantityInput.getText();
        int length = value.length();
        int chInt = keyEvent.getCode().getCode();

        boolean condition = true;
        int count = 0;
        if (length > 0) {
            count++;
        }

        if (count == 0 && chInt == 48) {
            condition = false;
        }

        if (length < 2 && condition && (chInt >= 48 && chInt <= 57) || chInt == 8) {
            quantityInput.setEditable(true);
            quantityErrorDisplay.setText("");
        } else {
            quantityInput.setEditable(false);

            if (!condition) {
                quantityErrorDisplay.setText("  * Not a Q==0");
            } else if (length == 2) {
                quantityErrorDisplay.setText("  * only 2 digits");
            } else {
                quantityErrorDisplay.setText("  * only digits(0-9)");
            }
        }
    }

    @FXML
    private void quantityKeyTyped() {
        String value = quantityInput.getText();
        int length = value.length();
        double total;
        String totalValue = "";

        updateButtonVisibility();

        if (length > 0) {
            int qtyInt = Integer.parseInt(value);
            total = qtyInt * CenterController.BURGERPRICE;
            totalValue = String.valueOf(CenterController.decimalFormat.format(total));
            totalDisplay.setText(totalValue);
        } else {
            total = 0 * CenterController.BURGERPRICE;
            totalValue = String.valueOf(CenterController.decimalFormat.format(total));
            totalDisplay.setText(totalValue);
        }
    }

    private void updateButtonVisibility() {
        if (selectOrderStatus.getValue().equals("Preparing")) {
            if (!quantityInput.getText().isEmpty()) {
                if (searchQuantity != Integer.parseInt(quantityInput.getText())) {
                    btnUpdateOrder.setDisable(false);
                } else {
                    btnUpdateOrder.setDisable(true);
                }
            } else {
                btnUpdateOrder.setDisable(true);
            }
        } else {
            if (!quantityInput.getText().isEmpty()) {
                btnUpdateOrder.setDisable(false);
            } else {
                btnUpdateOrder.setDisable(true);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> comboBoxItems = FXCollections.observableArrayList();

        comboBoxItems.add("Delivered");
        comboBoxItems.add("Preparing");
        comboBoxItems.add("Cancel");

        selectOrderStatus.setItems(comboBoxItems);
        btnRefresh.setDisable(true);
        btnUpdateOrder.setDisable(true);
        selectOrderStatus.setDisable(true);
        quantityInput.setDisable(true);
        customerIdDisplay.setEditable(false);
        nameDisplay.setEditable(false);
    }
}
