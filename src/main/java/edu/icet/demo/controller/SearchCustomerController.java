package edu.icet.demo.controller;

import edu.icet.demo.model.Orders;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SearchCustomerController implements Initializable {
    @FXML
    private TableColumn<Orders, String> colOrderQuantity;
    @FXML
    private Label customerIdErrorDisplay;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField customerIdInput;
    @FXML
    private Label nameDisplay;
    @FXML
    private TableView<Orders> tableView;
    @FXML
    private TableColumn<Orders, String> colOrderId;
    @FXML
    private TableColumn<Orders, String> colTotalValue;

    private static final ObservableList<Orders> ordersList = FXCollections.observableArrayList();

    @FXML
    private void cancelAction() throws IOException {
        Parent parent = new FXMLLoader(getClass().getResource("/view/homePage.fxml")).load();
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(parent);
    }

    @FXML
    private void customerIdKeyPressed(KeyEvent keyEvent) {
        String value = customerIdInput.getText();
        String ch = keyEvent.getCode().getChar();
        int length = value.length();
        int count = 10;
        boolean conditionFirstEquals0 = true;
        boolean conditionChar = false;

        if (length == 0 && !ch.equals("0")) {
            conditionFirstEquals0 = false;
        }

        for (int i = 0; i < count; i++) {
            String charValue = String.valueOf(i);
            if (ch.equals(charValue)) {
                conditionChar = true;
                count = i + 1;
            } else {
                conditionChar = false;
            }
        }

        if (length < 10 && conditionChar && conditionFirstEquals0 || keyEvent.getCode().getCode() == 8) {
            customerIdInput.setEditable(true);
            customerIdErrorDisplay.setText("");
        } else {
            customerIdInput.setEditable(false);
            if (length == 10) {
                customerIdErrorDisplay.setText("* input 10 digits");
            } else if (!conditionFirstEquals0) {
                customerIdErrorDisplay.setText("* Not a first digit==0");
            } else {
                customerIdErrorDisplay.setText("* only digits(0-9)");
            }
        }
    }

    @FXML
    private void customerIdKeyTyped() {
        String value = customerIdInput.getText();
        int length = value.length();

        if (length == 10) {
            customerIdInput.setEditable(false);

            try {
                int customerID = Integer.parseInt(value);
                ResultSet resultSet = CenterController.getInstance().getCustomerDetails(customerID);
                if (resultSet.next()){
                    nameDisplay.setText(resultSet.getString("name"));
                    setTableDetails();
                }
            } catch (SQLException e) {
                CenterController.alert.setAlertType(Alert.AlertType.ERROR);
                CenterController.alert.setContentText(e.getMessage());
                CenterController.alert.show();
            }
        }
    }

    @FXML
    private void searchAnotherCustomerAction() {
        customerIdInput.setText("");
        customerIdInput.setEditable(true);
        nameDisplay.setText("");
        tableView.getItems().clear();
    }

    private void setTableDetails(){
        try{
            ResultSet resultSet = CenterController.getInstance().getOrdersByCustomerId(customerIdInput.getText());

            while(resultSet.next()){
                ResultSet rstDetails = CenterController.getInstance().getOrderDetails(resultSet.getString("id"));
                rstDetails.next();
                String total = CenterController.decimalFormat.format(rstDetails.getInt("quantity")*CenterController.BURGERPRICE);
                ordersList.add(new Orders(resultSet.getString("id"), rstDetails.getString("quantity"), total));
            }

        } catch (SQLException e) {
            CenterController.alert.setAlertType(Alert.AlertType.ERROR);
            CenterController.alert.setContentText(e.getMessage());
            CenterController.alert.show();
        }

        tableView.setItems(ordersList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colOrderQuantity.setCellValueFactory(new PropertyValueFactory<>("orderQuantity"));
        colTotalValue.setCellValueFactory(new PropertyValueFactory<>("totalValue"));
    }
}
