package edu.icet.demo.controller;

import edu.icet.demo.database.LoadDriver;
import edu.icet.demo.model.orderDetails;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class PlaceOrderController implements Initializable {
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
    @FXML
    private Button btnPlaceOrder;
    @FXML
    private Button btnCancel;

    public void placeOrderAction(ActionEvent actionEvent) {
        int custId = Integer.parseInt(customerID.getText());
        int quantity = Integer.parseInt(qty.getText());
        orderDetails newOrder = new orderDetails(orderID.getText(), custId, customerName.getText(), quantity, 0);
        placeOrder(newOrder);
    }

    public void cancelAction(ActionEvent actionEvent) {
        int custId = Integer.parseInt(customerID.getText());
        try {
            ResultSet resultSet = centerController.getInstance().getCustomerDetails(custId);
            if (resultSet.next()) {
                System.out.println("name : " + resultSet.getString("name"));
                System.out.println("no error");
            } else {
                System.out.println("else error");
            }

        } catch (SQLException e) {
            System.out.println("have error");
        }
    }

    public void customerIdKeyPressed(KeyEvent keyEvent) {
        String value = customerID.getText();
        String ch = keyEvent.getCode().getChar();
        int length = value.length();
        int count = 10;
        boolean conditionFirstEquals0 = true;
        boolean conditionChar = false;

        if (length == 0) {
            if (ch.equals("0")) {
                conditionFirstEquals0 = true;
            } else {
                conditionFirstEquals0 = false;
            }
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

        if ((length >= 0 && length < 10) & conditionChar & conditionFirstEquals0 || keyEvent.getCode().getCode() == 8) {
            customerID.setEditable(true);
            customerIdError.setText("");
        } else {
            customerID.setEditable(false);
            if (length == 10) {
                customerIdError.setText("* input 10 digits");
            } else if (conditionFirstEquals0 == false) {
                customerIdError.setText("* Not a first digit==0");
            } else {
                customerIdError.setText("* only digits(0-9)");
            }
        }
    }

    public void customerIdKeyTyped(KeyEvent keyEvent) {
        String value = customerID.getText();
        int length = value.length();

        if (length == 10) {
            customerID.setEditable(false);
            try {
                int custId = Integer.parseInt(value);
                ResultSet resultSet = centerController.getInstance().getCustomerDetails(custId);
                resultSet.next();

                customerName.setText(resultSet.getString("name"));
                customerName.setEditable(false);

            } catch (SQLException e) {

            }
        }
    }

    public int addCustomer(int customerId, String name) throws SQLException {
        String sql = "INSERT INTO customer VALUES(" + customerId + ", '" + name + "')";

        LoadDriver instance = LoadDriver.getInstance();
        Connection connection = instance.getConnection();
        Statement stm = connection.createStatement();

        int res = stm.executeUpdate(sql);
        return res;
    }

    public int addOrder(String orderId, int customerId) throws SQLException {
        String sql = "INSERT INTO orders VALUES('" + orderId + "', " + customerId + ")";

        LoadDriver instance = LoadDriver.getInstance();
        Connection connection = instance.getConnection();
        Statement stm = connection.createStatement();

        int res = stm.executeUpdate(sql);
        return res;
    }

    public int addOrderDetails(String orderId, int quantity, int status) throws SQLException {
        String sql = "INSERT INTO order_details VALUES('" + orderId + "', " + quantity + ", " + status + ")";

        LoadDriver instance = LoadDriver.getInstance();
        Connection connection = instance.getConnection();
        Statement stm = connection.createStatement();

        int res = stm.executeUpdate(sql);
        return res;
    }

    void placeOrder(orderDetails orderOb) {
        try {
            ResultSet resultSet = centerController.getInstance().getCustomerDetails(orderOb.getCustomerID());

            if (resultSet.next()) {
                int res = addOrder(orderOb.getOrderID(), orderOb.getCustomerID());
                if (res > 0) {
                    int resOD = addOrderDetails(orderOb.getOrderID(), orderOb.getQty(), orderOb.getStatus());
                }
            } else {
                addCustomer(orderOb.getCustomerID(), orderOb.getName());
                int res = addOrder(orderOb.getOrderID(), orderOb.getCustomerID());
                if (res > 0) {
                    int resOD = addOrderDetails(orderOb.getOrderID(), orderOb.getQty(), orderOb.getStatus());
                }
            }
        } catch (SQLException e) {

        }
    }

    // generate orderId
    public String generateId() {
        try {
            ResultSet resultOrderRowCount = getOrdersSize();
            resultOrderRowCount.next();

            int size = resultOrderRowCount.getInt("row_count");

            if (size > 0) {
                try {
                    ResultSet resultSet = getLastOrderId();
                    resultSet.next();

                    String lastId = resultSet.getString("id");

                    String[] part = lastId.split("[B]");
                    int num = Integer.parseInt(part[1]);
                    num++;

                    return String.format("B%03d", num);

                } catch (SQLException e) {

                }
            } else {
                return "B000";
            }
        } catch (SQLException e) {

        }

        return null;
    }

    public ResultSet getLastOrderId() throws SQLException {
        String sql = "SELECT * FROM orders ORDER BY id DESC LIMIT 1";

        LoadDriver instance = LoadDriver.getInstance();
        Connection connection = instance.getConnection();
        Statement stm = connection.createStatement();

        ResultSet resultSet = stm.executeQuery(sql);
        return resultSet;
    }

    public ResultSet getOrdersSize() throws SQLException {
        String sql = "SELECT COUNT(*) AS row_count FROM orders";

        LoadDriver instance = LoadDriver.getInstance();
        Connection connection = instance.getConnection();
        Statement stm = connection.createStatement();

        ResultSet resultSet = stm.executeQuery(sql);
        return resultSet;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        orderID.setText(generateId());
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

    public void qtyKeyPressed(KeyEvent keyEvent) {
        String value = qty.getText();
        int length = value.length();
        int chInt = keyEvent.getCode().getCode();

        boolean condition = true;
        int count = 0;
        if(length>0){
            count++;
        } else {
            count = 0;
        }

        if (count == 0 & chInt == 48) {
            condition = false;
        }

        if (length < 2 & condition & (chInt >= 48 && chInt <= 57) || chInt == 8) {
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


}
