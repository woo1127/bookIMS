package com.bookIMS.Controllers;

import com.bookIMS.Database;
import com.bookIMS.Models.SalesRecord;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.Optional;
import java.util.ResourceBundle;


public class SalesRecordController implements Initializable {

    @FXML
    private TableView<SalesRecord> salesRecordTableView;

    @FXML
    private TableColumn<SalesRecord, Integer> saleIdCol;

    @FXML
    private TableColumn<SalesRecord, String> invoiceNoCol;

    @FXML
    private TableColumn<SalesRecord, Date> dateCol;

    @FXML
    private TableColumn<SalesRecord, String> usernameCol;

    @FXML
    private TableColumn<SalesRecord, Integer> bookIdCol;

    @FXML
    private TableColumn<SalesRecord, String> bookTitleCol;

    @FXML
    private TableColumn<SalesRecord, Integer> purchasedQtyCol;

    @FXML
    private TableColumn<SalesRecord, Double> unitPriceCol;

    @FXML
    private TableColumn<SalesRecord, Double> totalAmountCol;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button mainBtn;

    @FXML
    private Label usernameLabel;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;


    public void setUsernameLabel(String username) {
        usernameLabel.setText(username);
    }


    public ObservableList<SalesRecord> getSalesRecord() {
        ObservableList<SalesRecord> salesRecordList = FXCollections.observableArrayList();
        String sql = "SELECT s.sale_id, s.invoice_no, s.date, u.username, b.book_id, b.title, si.purchased_qty, b.unit_price, si.amount FROM sales_items si " +
                    "JOIN sales s ON si.sale_id = s.sale_id " +
                    "JOIN books b ON si.book_id = b.book_id " +
                    "JOIN users u ON s.user_id = u.user_id " +
                    "ORDER BY s.sale_id";
        connect = Database.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                salesRecordList.add(new SalesRecord(
                        result.getInt("sale_id"),
                        result.getString("invoice_no"),
                        result.getDate("date"),
                        result.getString("username"),
                        result.getInt("book_id"),
                        result.getString("title"),
                        result.getInt("purchased_qty"),
                        result.getDouble("unit_price"),
                        result.getDouble("amount")
                ));
            }
        }   
        catch (Exception e) {
            e.printStackTrace();
        }
        return salesRecordList;
    }


    private ObservableList<SalesRecord> salesRecordList;
    public void showSalesRecordList() {
        salesRecordList = getSalesRecord();

        saleIdCol.setCellValueFactory(new PropertyValueFactory<>("saleId"));
        invoiceNoCol.setCellValueFactory(new PropertyValueFactory<>("invoiceNo"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        bookIdCol.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        bookTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        purchasedQtyCol.setCellValueFactory(new PropertyValueFactory<>("purchasedQty"));
        unitPriceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        totalAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        salesRecordTableView.setItems(salesRecordList);
    }


    public void logoutClick() {
        try {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout ?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                logoutBtn.getScene().getWindow().hide();
                
                Parent root = FXMLLoader.load(getClass().getResource("../Views/LoginPage.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                stage.setScene(scene);
                stage.setTitle("Login");
                stage.show();
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void mainBtnClick() {
        try {
            String useridSql = "SELECT user_id FROM users WHERE username = ?";
            prepare = connect.prepareStatement(useridSql);
            prepare.setString(1, usernameLabel.getText());
            result = prepare.executeQuery();

            if (result.next()) {
                int userId = result.getInt("user_id");

                if (userId == 1) {
                    mainBtn.getScene().getWindow().hide();

                    FXMLLoader loader = FXMLLoader.load(getClass().getResource("../Views/DashboardPage.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(loader.load());

                    DashboardController controller = loader.getController();
                    controller.setUsernameLabel(usernameLabel.getText());

                    stage.setScene(scene);
                    stage.setTitle("Dashboard");
                    stage.show();
                }
                else if (userId == 2) {
                    mainBtn.getScene().getWindow().hide();

                    FXMLLoader loader = FXMLLoader.load(getClass().getResource("../Views/SalesPage.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(loader.load());

                    SalesController controller = loader.getController();
                    controller.setUsernameLabel(usernameLabel.getText());

                    stage.setScene(scene);
                    stage.setTitle("Sales");
                    stage.show();
                }
                else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("An error occurred, please rerun the program.");
                    alert.showAndWait();    
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showSalesRecordList();
    }
}
