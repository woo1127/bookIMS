package com.bookIMS.Controllers;

import com.bookIMS.Database;
import com.bookIMS.Models.Book;
import com.bookIMS.Models.Cart;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class SalesController implements Initializable {

    @FXML
    private TableView<Book> bookTableView;

    @FXML
    private TableColumn<Book, Integer> bookIdCol;

    @FXML
    private TableColumn<Book, String> bookTitleCol;

    @FXML
    private TableColumn<Book, String> bookAuthorCol;

    @FXML
    private TableColumn<Book, String> bookGenreCol;

    @FXML
    private TableColumn<Book, Integer> bookQtyCol;

    @FXML
    private TableColumn<Book, Double> bookUnitPriceCol;

    @FXML
    private TableView<Cart> cartTableView;

    @FXML
    private TableColumn<Cart, Integer> cartBookIdCol;

    @FXML
    private TableColumn<Cart, String> cartBookTitleCol;

    @FXML
    private TableColumn<Cart, Integer> cartPurchasedQtyCol;

    @FXML
    private TableColumn<Cart, Double> cartUnitPriceCol;

    @FXML
    private TableColumn<Cart, Double> cartAmountCol;

    @FXML
    private TextField invoiceNoTextField;

    @FXML
    private DatePicker dateTextField;

    @FXML
    private TextField bookIdTextField;

    @FXML
    private TextField bookTitleTextField;

    @FXML
    private TextField purchasedQtyTextField;

    @FXML
    private TextField bookSearchTextField;

    @FXML
    private Button searchBtn;

    @FXML
    private Button addBtn;

    @FXML
    private Button updateBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button clearBtn;

    @FXML
    private Button payBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button salesRecordBtn;

    @FXML
    private Label totalPrice;

    @FXML
    private Label usernameLabel;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;


    public void setUsernameLabel(String username) {
        usernameLabel.setText(username);
    }


    public ObservableList<Book> getBookListData() {
        ObservableList<Book> bookListData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM books";
        connect = Database.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            Book bookData;

            while (result.next()) {
                bookData = new Book(
                    result.getInt("book_id"),
                    result.getString("title"),
                    result.getString("genre"),
                    result.getString("author"),
                    result.getInt("quantity"),
                    result.getDouble("unit_price")
                );

                bookListData.add(bookData);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return bookListData;
    }
    

    private ObservableList<Book> bookListData;
    public void showBookListData() {
        bookListData = getBookListData();

        bookIdCol.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        bookAuthorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        bookTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        bookGenreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        bookQtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        bookUnitPriceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        bookTableView.setItems(bookListData);
    }


    public void selectBook() {
        Book bookData = bookTableView.getSelectionModel().getSelectedItem();
        int num = bookTableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) return;

        bookIdTextField.setText(String.valueOf(bookData.getBookId()));
        bookTitleTextField.setText(bookData.getTitle());
    }


    public void searchBook() {
        String searchText = bookSearchTextField.getText().toLowerCase();

        List<Book> filteredData = bookListData.stream()
            .filter(predicateBookData -> Integer.toString(predicateBookData.getBookId()).contains(searchText) ||
                                         predicateBookData.getTitle().toLowerCase().contains(searchText) ||
                                         predicateBookData.getAuthor().toLowerCase().contains(searchText) ||
                                         predicateBookData.getGenre().toLowerCase().contains(searchText) ||
                                         Integer.toString(predicateBookData.getQuantity()).contains(searchText) ||
                                         predicateBookData.getUnitPrice().toString().contains(searchText))
            .collect(Collectors.toList());

        ObservableList<Book> filteredList = FXCollections.observableArrayList(filteredData);
        bookTableView.setItems(filteredList);
    }


    public void registerCartTableColumns() {
        cartBookIdCol.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        cartBookTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        cartPurchasedQtyCol.setCellValueFactory(new PropertyValueFactory<>("purchasedQty"));
        cartUnitPriceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        cartAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
    }


    public void selectCartItem() {
        Cart cartData = cartTableView.getSelectionModel().getSelectedItem();
        int num = cartTableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) return;

        bookIdTextField.setText(String.valueOf(cartData.getBookId()));
        bookTitleTextField.setText(cartData.getTitle());
        purchasedQtyTextField.setText(cartData.getPurchasedQty().toString());
    }


    private ObservableList<Cart> cartListData = FXCollections.observableArrayList();
    public void addBookToCart() {
        Integer bookId = Integer.parseInt(bookIdTextField.getText());
        String title = bookTitleTextField.getText();
        Double unitPrice = bookTableView.getSelectionModel().getSelectedItem().getUnitPrice();
        
        if (invoiceNoTextField.getText().isBlank() 
            || dateTextField.getValue() == null
            || bookIdTextField.getText().isBlank() 
            || bookTitleTextField.getText().isBlank() 
            || purchasedQtyTextField.getText().isBlank()) {
            
            Alert alert;
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all the fields.");
            alert.showAndWait();
            return;
        }
        
        Integer purchasedQty = Integer.parseInt(purchasedQtyTextField.getText());
        Double amount = purchasedQty * unitPrice;

        // show error message if purchased quantity is more than the available quantity
        if (purchasedQty > bookTableView.getSelectionModel().getSelectedItem().getQuantity()) {
            Alert alert;
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Purchased quantity could not be more than the available quantity.");
            alert.showAndWait();
            return;
        }

        // show error if the book is already in the cart
        for (Cart cartData : cartListData) {
            if (cartData.getBookId().equals(bookId)) {
                Alert alert;
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("The book is already in the cart.");
                alert.showAndWait();
                return;
            }
        }

        Cart cartData = new Cart(bookId, title, purchasedQty, unitPrice, amount);

        cartListData.add(cartData);
        cartTableView.setItems(cartListData);
        calTotalPrice();
    }


    public void updateCartItem() {
        Integer bookId = Integer.parseInt(bookIdTextField.getText());
        String title = bookTitleTextField.getText();
        Integer purchasedQty = Integer.parseInt(purchasedQtyTextField.getText());
        Double unitPrice = bookTableView.getSelectionModel().getSelectedItem().getUnitPrice();
        Double amount = purchasedQty * unitPrice;
        int selectedNum = cartTableView.getSelectionModel().getSelectedIndex();

        if ((selectedNum -1) < -1) {
            Alert alert;
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select an item in cart to update.");
            alert.showAndWait();
            return;
        }

        if (invoiceNoTextField.getText().isBlank() 
            || dateTextField.getValue() == null        
            || bookIdTextField.getText().isBlank() 
            || bookTitleTextField.getText().isBlank() 
            || purchasedQtyTextField.getText().isBlank()) {
            
            Alert alert;
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all the fields.");
            alert.showAndWait();
            return;
        }

        // show error message if purchased quantity is more than the available quantity
        if (purchasedQty > bookTableView.getSelectionModel().getSelectedItem().getQuantity()) {
            Alert alert;
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Purchased quantity could not be more than the available quantity.");
            alert.showAndWait();
            return;
        }

        Cart cartData = new Cart(bookId, title, purchasedQty, unitPrice, amount);

        cartListData.set(cartTableView.getSelectionModel().getSelectedIndex(), cartData);
        cartTableView.setItems(cartListData);
        calTotalPrice();
    }


    public void deleteCartItem() {
        // show error if no item is selected
        if (cartTableView.getSelectionModel().getSelectedIndex() < 0) {
            Alert alert;
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select an item to delete.");
            alert.showAndWait();
            return;
        }

        cartListData.remove(cartTableView.getSelectionModel().getSelectedIndex());
        cartTableView.setItems(cartListData);
        calTotalPrice();
    }


    public void clearTextFields() {
        invoiceNoTextField.setText("");
        dateTextField.setValue(null);
        bookIdTextField.setText("");
        bookTitleTextField.setText("");
        purchasedQtyTextField.setText("");
        bookSearchTextField.setText("");
    }


    public void calTotalPrice() {
        Double total = 0.0;

        for (Cart cartData : cartListData) {
            total += cartData.getAmount();
        }

        totalPrice.setText("RM " + total.toString());
    }


    public void payBtnClick() {
        connect = Database.connectDB();

        try {
            String insertSaleSql = "INSERT INTO sales (user_id, invoice_no, date, total_price) " +
                                "VALUES ((SELECT user_id FROM users WHERE username = ?), ?, ?, ?)";

            prepare = connect.prepareStatement(insertSaleSql, PreparedStatement.RETURN_GENERATED_KEYS);
            prepare.setString(1, usernameLabel.getText());
            prepare.setString(2, invoiceNoTextField.getText());
            prepare.setDate(3, java.sql.Date.valueOf(dateTextField.getValue()));
            prepare.setDouble(4, Double.parseDouble(totalPrice.getText().substring(3)));
            prepare.executeUpdate();

            ResultSet rs = prepare.getGeneratedKeys();
            long newSaleId = rs.next() ? rs.getLong(1) : 0;


            String insertDataItemSql = "INSERT INTO sales_items (sale_id, book_id, purchased_qty, amount) " +
                                    "VALUES (?, ?, ?, ?)";

            List<Cart> itemsToRemove = new ArrayList<>(); // Create a list to store items to be removed

            for (Cart cartData : cartListData) {
                prepare = connect.prepareStatement(insertDataItemSql);
                prepare.setLong(1, newSaleId);
                prepare.setInt(2, cartData.getBookId());
                prepare.setInt(3, cartData.getPurchasedQty());
                prepare.setDouble(4, cartData.getAmount());

                int rowsAffected_2 = prepare.executeUpdate();

                if (rowsAffected_2 > 0) {
                    String updateBookQtySql = "UPDATE books SET quantity = quantity - ? WHERE book_id = ?";

                    prepare = connect.prepareStatement(updateBookQtySql);
                    prepare.setInt(1, cartData.getPurchasedQty());
                    prepare.setInt(2, cartData.getBookId());

                    int rowsAffected_3 = prepare.executeUpdate();

                    if (rowsAffected_3 > 0) {
                        itemsToRemove.add(cartData); // Add the item to the removal list
                    }
                }
            }

            // Remove the items from the cartListData collection
            cartListData.removeAll(itemsToRemove);

            if (!itemsToRemove.isEmpty()) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Payment successful!");
                alert.showAndWait();

                clearTextFields();
                cartListData.clear();
                cartTableView.setItems(cartListData);
                calTotalPrice();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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


    public void salesRecordClick() {
        try {
            salesRecordBtn.getScene().getWindow().hide();
            
            FXMLLoader loader = FXMLLoader.load(getClass().getResource("../Views/SalesRecordPage.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());

            SalesRecordController controller = loader.getController();
            controller.setUsernameLabel(usernameLabel.getText());

            stage.setScene(scene);
            stage.setTitle("Sales Record");
            stage.show();
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showBookListData();
        registerCartTableColumns();
    }
}
