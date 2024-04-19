package edu.icet.demo.controller;

import edu.icet.demo.database.LoadDriver;
import edu.icet.demo.model.OrderDetails;
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class PlaceOrderController implements Initializable {
    @FXML
    private Button btnPlaceOrder;
    @FXML
    private Label totalQty;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label customerNameError;
    @FXML
    private Label orderID;
    @FXML
    private TextField customerID;
    @FXML
    private TextField customerName;
    @FXML
    private TextField qty;
    @FXML
    private Label customerIdError;
    @FXML
    private Label qtyError;

    public void placeOrderAction() {
        int custId = Integer.parseInt(customerID.getText());
        int quantity = Integer.parseInt(qty.getText());
        OrderDetails newOrder = new OrderDetails(orderID.getText(), custId, customerName.getText(), quantity, 0);
        placeOrder(newOrder);
    }

    public void cancelAction() throws IOException {
        Parent load = new FXMLLoader(getClass().getResource("/view/homePage.fxml")).load();
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(load);
    }

    public void customerIdKeyPressed(KeyEvent keyEvent) {
        String value = customerID.getText();
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
            customerID.setEditable(true);
            customerIdError.setText("");
        } else {
            customerID.setEditable(false);
            if (length == 10) {
                customerIdError.setText("* input 10 digits");
            } else if (!conditionFirstEquals0) {
                customerIdError.setText("* Not a first digit==0");
            } else {
                customerIdError.setText("* only digits(0-9)");
            }
        }
    }

    public void customerIdKeyTyped() {
        String value = customerID.getText();
        int length = value.length();

        if (length == 10) {
            customerID.setEditable(false);
            customerName.setDisable(false);
            qty.setDisable(false);

            try {
                int custId = Integer.parseInt(value);
                ResultSet resultSet = CenterController.getInstance().getCustomerDetails(custId);
                if (resultSet.next()){
                    customerName.setText(resultSet.getString("name"));
                    customerName.setDisable(true);
                }
            } catch (SQLException e) {
                CenterController.alert.setAlertType(Alert.AlertType.ERROR);
                CenterController.alert.setContentText(e.getMessage());
                CenterController.alert.show();
            }
        }
    }

    public void addCustomer(int customerId, String name) throws SQLException {
        String sql = "INSERT INTO customer VALUES(" + customerId + ", '" + name + "')";
        LoadDriver instance = LoadDriver.getInstance();
        Connection connection = instance.getConnection();
        Statement stm = connection.createStatement();
        stm.executeUpdate(sql);
    }

    public void addOrder(String orderId, int customerId) throws SQLException {
        String sql = "INSERT INTO orders VALUES('" + orderId + "', " + customerId + ")";
        LoadDriver instance = LoadDriver.getInstance();
        Connection connection = instance.getConnection();
        Statement stm = connection.createStatement();
        stm.executeUpdate(sql);
    }

    public void addOrderDetails(String orderId, int quantity, int status) throws SQLException {
        String sql = "INSERT INTO order_details VALUES('" + orderId + "', " + quantity + ", " + status + ")";
        LoadDriver instance = LoadDriver.getInstance();
        Connection connection = instance.getConnection();
        Statement stm = connection.createStatement();
        stm.executeUpdate(sql);
    }

    public void placeOrder(OrderDetails orderOb) {
        try {
            ResultSet resultSet = CenterController.getInstance().getCustomerDetails(orderOb.getCustomerID());

            if (resultSet.next()) {
                addOrder(orderOb.getOrderID(), orderOb.getCustomerID());
                addOrderDetails(orderOb.getOrderID(), orderOb.getQty(), orderOb.getStatus());

                String tempOrderId = orderID.getText();

                customerID.setText("");
                customerName.setText("");
                qty.setText("");
                totalQty.setText("0.00");
                btnPlaceOrder.setDisable(true);
                customerName.setDisable(true);
                qty.setDisable(true);
                orderID.setText(generateId());

                CenterController.alert.setAlertType(Alert.AlertType.CONFIRMATION);
                CenterController.alert.setContentText("Your " + tempOrderId + " order is enter to the system successfully...");
                CenterController.alert.show();

            } else {
                addCustomer(orderOb.getCustomerID(), orderOb.getName());
                addOrder(orderOb.getOrderID(), orderOb.getCustomerID());
                addOrderDetails(orderOb.getOrderID(), orderOb.getQty(), orderOb.getStatus());

                String tempOrderId = orderID.getText();

                customerID.setText("");
                customerName.setText("");
                qty.setText("");
                totalQty.setText("0.00");
                btnPlaceOrder.setDisable(true);
                customerName.setDisable(true);
                qty.setDisable(true);
                orderID.setText(generateId());

                CenterController.alert.setAlertType(Alert.AlertType.CONFIRMATION);
                CenterController.alert.setContentText("Your " + tempOrderId + " order is enter to the system successfully...");
                CenterController.alert.show();
            }
        } catch (SQLException e) {
            CenterController.alert.setAlertType(Alert.AlertType.ERROR);
            CenterController.alert.setContentText(e.getMessage());
            CenterController.alert.show();
        }
    }

    // generate orderId
    public String generateId() {
        try {
            ResultSet resultOrderRowCount = getOrdersSize();
            resultOrderRowCount.next();

            int size = resultOrderRowCount.getInt("row_count");

            if (size > 0) {
                ResultSet resultSet = getLastOrderId();
                resultSet.next();

                String lastId = resultSet.getString("id");

                String[] part = lastId.split("B");
                int num = Integer.parseInt(part[1]);
                num++;

                return String.format("B%03d", num);
            } else {
                return "B001";
            }
        } catch (SQLException e) {
            CenterController.alert.setAlertType(Alert.AlertType.ERROR);
            CenterController.alert.setContentText(e.getMessage());
            CenterController.alert.show();
        }
        return null;
    }

    public ResultSet getLastOrderId() throws SQLException {
        String sql = "SELECT * FROM orders ORDER BY id DESC LIMIT 1";
        LoadDriver instance = LoadDriver.getInstance();
        Connection connection = instance.getConnection();
        Statement stm = connection.createStatement();
        return stm.executeQuery(sql);
    }

    public ResultSet getOrdersSize() throws SQLException {
        String sql = "SELECT COUNT(*) AS row_count FROM orders";
        LoadDriver instance = LoadDriver.getInstance();
        Connection connection = instance.getConnection();
        Statement stm = connection.createStatement();
        return stm.executeQuery(sql);
    }

    public void nameKeyPressed(KeyEvent keyEvent) {
        int length = customerName.getText().length();
        int keyCode = keyEvent.getCode().getCode();

        if (length < 16 || keyCode == 8) {
            customerNameError.setText("");
            customerName.setEditable(true);
        } else {
            customerNameError.setText("* input 16 character");
            customerName.setEditable(false);
        }
    }

    public void nameKeyTyped() {
        int id = customerID.getText().length();
        int name = customerName.getText().length();
        int qtyLength = qty.getText().length();

        if (id == 10 && name > 0 && qtyLength > 0) {
            btnPlaceOrder.setDisable(false);
        } else {
            btnPlaceOrder.setDisable(true);
        }
    }

    public void qtyKeyPressed(KeyEvent keyEvent) {
        String value = qty.getText();
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
            qty.setEditable(true);
            qtyError.setText("");
        } else {
            qty.setEditable(false);

            if (!condition) {
                qtyError.setText("  * Not a Q==0");
            } else if (length == 2) {
                qtyError.setText("  * only 2 digits");
            } else {
                qtyError.setText("  * only digits(0-9)");
            }
        }
    }

    public void qtyKeyTyped() {
        String value = qty.getText();
        int length = value.length();
        double total;
        String totalValue = "";

        int id = customerID.getText().length();
        int name = customerName.getText().length();

        if (id == 10 && name > 0 && length > 0) {
            btnPlaceOrder.setDisable(false);
        } else {
            btnPlaceOrder.setDisable(true);
        }

        if (length > 0) {
            int qtyInt = Integer.parseInt(value);
            total = qtyInt * CenterController.BURGERPRICE;
            totalValue = String.valueOf(CenterController.decimalFormat.format(total));
            totalQty.setText(totalValue);
        } else {
            total = 0 * CenterController.BURGERPRICE;
            totalValue = String.valueOf(CenterController.decimalFormat.format(total));
            totalQty.setText(totalValue);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        orderID.setText(generateId());
        btnPlaceOrder.setDisable(true);
        customerName.setDisable(true);
        qty.setDisable(true);
    }
}
