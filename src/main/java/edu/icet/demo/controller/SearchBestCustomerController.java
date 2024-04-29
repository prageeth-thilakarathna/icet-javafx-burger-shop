package edu.icet.demo.controller;

import edu.icet.demo.model.BestCustomer;
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

public class SearchBestCustomerController implements Initializable {
    @FXML
    private TableColumn<BestCustomer, String> colCustomerId;
    @FXML
    private TableColumn<BestCustomer, String> colName;
    @FXML
    private TableColumn<BestCustomer, String> colTotal;
    @FXML
    private TableView<BestCustomer> tableView;
    @FXML
    private AnchorPane anchorPane;

    public void cancelAction() throws IOException {
        Parent load = new FXMLLoader(getClass().getResource("/view/homePage.fxml")).load();
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(load);
    }

    private ObservableList<BestCustomer> getBestCustomers(){
        ObservableList<BestCustomer> bestCustomers = FXCollections.observableArrayList();

        try{
            ResultSet resultSet = CenterController.getInstance().getCustomers();

            while(resultSet.next()){
                ResultSet resultSetOrders = CenterController.getInstance().getOrdersByCustomerId(resultSet.getString("id"));

                int customerId = 0;

                while (resultSetOrders.next()){
                    if(resultSetOrders.getRow()>1){
                        customerId = resultSet.getInt("id");
                    }

                    ResultSet resultSetOrderDetails = CenterController.getInstance().getOrderDetails(resultSetOrders.getString("id"));
                    resultSetOrderDetails.next();

                    if(resultSetOrderDetails.getInt("status")!=2 && customerId != resultSet.getInt("id")){
                        String total = CenterController.decimalFormat.format(resultSetOrderDetails.getInt("quantity")*CenterController.BURGERPRICE);
                        bestCustomers.add(new BestCustomer("0"+resultSet.getInt("id"), resultSet.getString("name"), total));
                    } else {
                        double prevTotal = Double.parseDouble(bestCustomers.get(bestCustomers.size()-1).getTotal());
                        double tempTotal = resultSetOrderDetails.getInt("quantity")*CenterController.BURGERPRICE;
                        String total = CenterController.decimalFormat.format(prevTotal+tempTotal);
                        bestCustomers.get(bestCustomers.size()-1).setTotal(total);
                    }
                }
            }
        } catch (SQLException e) {
            CenterController.alert.setAlertType(Alert.AlertType.ERROR);
            CenterController.alert.setContentText(e.getMessage());
            CenterController.alert.show();
        }
        return sortBestCustomer(bestCustomers);
    }

    private ObservableList<BestCustomer> sortBestCustomer(ObservableList<BestCustomer> bestCustomers){
        for(int i=1; i<bestCustomers.size(); i++){
            for(int j=0; j<i; j++){
                double second = Double.parseDouble(bestCustomers.get(i).getTotal());
                double first = Double.parseDouble(bestCustomers.get(j).getTotal());
                if(second>first){
                    BestCustomer x = bestCustomers.get(j);
                    bestCustomers.set(j, bestCustomers.get(i));
                    bestCustomers.set(i, x);
                }
            }
        }
        return bestCustomers;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        tableView.setItems(getBestCustomers());
    }
}
