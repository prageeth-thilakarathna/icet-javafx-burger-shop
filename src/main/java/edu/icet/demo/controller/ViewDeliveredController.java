package edu.icet.demo.controller;

import edu.icet.demo.model.Delivered;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ViewDeliveredController implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TableView<Delivered> tableView;
    @FXML
    private TableColumn<Delivered, String> colOrderId;
    @FXML
    private TableColumn<Delivered, String> colCustomerId;
    @FXML
    private TableColumn<Delivered, String> colName;
    @FXML
    private TableColumn<Delivered, String> colQuantity;
    @FXML
    private TableColumn<Delivered, String> colOrderValue;

    @FXML
    private void btnBackHome() throws IOException {
        Parent load = new FXMLLoader(getClass().getResource("/view/homePage.fxml")).load();
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(load);
    }

    @FXML
    private void btnCancel() throws IOException {
        Parent load = new FXMLLoader(getClass().getResource("/view/view.fxml")).load();
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(load);
    }

    private ObservableList<Delivered> getDeliveredOrders() {
        ObservableList<Delivered> deliveredOrders = FXCollections.observableArrayList();

        try {
            ResultSet resultSetAllOrders = CenterController.getInstance().getAllOrders();

            while (resultSetAllOrders.next()) {
                ResultSet resultSetOrderDetails = CenterController.getInstance().getOrderDetails(resultSetAllOrders.getString("id"));
                resultSetOrderDetails.next();

                if (resultSetOrderDetails.getInt("status") == 1) {
                    ResultSet resultSetCustomer = CenterController.getInstance().getCustomerDetails(resultSetAllOrders.getInt("customerId"));

                    String total = CenterController.decimalFormat.format(resultSetOrderDetails.getInt("quantity") * CenterController.BURGERPRICE);

                    Delivered order = new Delivered(resultSetAllOrders.getString("id"), resultSetAllOrders.getString("customerId"), resultSetCustomer.getString("name"), resultSetOrderDetails.getString("quantity"), total);
                    deliveredOrders.add(order);
                }
            }
        } catch (SQLException e) {
            CenterController.alert.setAlertType(Alert.AlertType.ERROR);
            CenterController.alert.setContentText(e.getMessage());
            CenterController.alert.show();
        }

        return deliveredOrders;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colOrderValue.setCellValueFactory(new PropertyValueFactory<>("orderValue"));

        tableView.setItems(getDeliveredOrders());
    }
}
