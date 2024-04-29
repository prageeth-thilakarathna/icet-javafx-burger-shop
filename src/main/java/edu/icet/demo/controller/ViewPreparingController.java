package edu.icet.demo.controller;

import edu.icet.demo.model.ViewOrders;
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

public class ViewPreparingController implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TableView<ViewOrders> tableView;
    @FXML
    private TableColumn<ViewOrders, String> colOrderId;
    @FXML
    private TableColumn<ViewOrders, String> colCustomerId;
    @FXML
    private TableColumn<ViewOrders, String> colName;
    @FXML
    private TableColumn<ViewOrders, String> colQuantity;
    @FXML
    private TableColumn<ViewOrders, String> colOrderValue;

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

    private ObservableList<ViewOrders> getPreparingOrders() {
        ObservableList<ViewOrders> preparingOrders = FXCollections.observableArrayList();

        try {
            ResultSet resultSetAllOrders = CenterController.getInstance().getAllOrders();

            while (resultSetAllOrders.next()) {
                ResultSet resultSetOrderDetails = CenterController.getInstance().getOrderDetails(resultSetAllOrders.getString("id"));
                resultSetOrderDetails.next();

                if (resultSetOrderDetails.getInt("status") == 0) {
                    ResultSet resultSetCustomer = CenterController.getInstance().getCustomerDetails(resultSetAllOrders.getInt("customerId"));
                    resultSetCustomer.next();

                    String total = CenterController.decimalFormat.format(resultSetOrderDetails.getInt("quantity") * CenterController.BURGERPRICE);

                    ViewOrders order = new ViewOrders(resultSetAllOrders.getString("id"), "0"+resultSetAllOrders.getString("customerId"), resultSetCustomer.getString("name"), resultSetOrderDetails.getString("quantity"), total);
                    preparingOrders.add(order);
                }
            }
        } catch (SQLException e) {
            CenterController.alert.setAlertType(Alert.AlertType.ERROR);
            CenterController.alert.setContentText(e.getMessage());
            CenterController.alert.show();
        }

        return preparingOrders;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colOrderValue.setCellValueFactory(new PropertyValueFactory<>("orderValue"));

        tableView.setItems(getPreparingOrders());
    }
}
