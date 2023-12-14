package com.bookIMS.Controllers;

import com.bookIMS.Database;
import com.bookIMS.Models.Book;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.List;
import java.util.stream.Collectors;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class DashboardController implements Initializable {

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
    private TextField bookIdTextField;

    @FXML
    private TextField bookTitleTextField;

    @FXML
    private TextField bookAuthorTextField;

    @FXML
    private TextField bookGenreTextField;

    @FXML
    private TextField bookQtyTextField;

    @FXML
    private TextField bookUnitPriceTextField;

    @FXML
    private TextField bookSearchTextField;

    @FXML
    private Button addBtn;

    @FXML
    private Button updateBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button clearBtn;

    @FXML
    private Button searchBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button salesRecordBtn;

    @FXML
    private Label usernameLabel;

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
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

        if ((num - 1) < -1)  return;

        bookIdTextField.setText(String.valueOf(bookData.getBookId()));
        bookTitleTextField.setText(bookData.getTitle());
        bookAuthorTextField.setText(bookData.getAuthor());
        bookGenreTextField.setText(bookData.getGenre());
        bookQtyTextField.setText(String.valueOf(bookData.getQuantity()));
        bookUnitPriceTextField.setText(String.valueOf(bookData.getUnitPrice()));
    }


    public void addBook() {
        Alert alert;
        connect = Database.connectDB();

        try {
            if (bookIdTextField.getText().isEmpty() || bookTitleTextField.getText().isEmpty() || 
                bookAuthorTextField.getText().isEmpty() || bookGenreTextField.getText().isEmpty() ||
                bookQtyTextField.getText().isEmpty() || bookUnitPriceTextField.getText().isEmpty()) {
                
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill in all fields");
                alert.showAndWait();
            }
            else {
                String checkExistSql = "SELECT book_id FROM books WHERE book_id = '" + bookIdTextField.getText() + "'";
                statement = connect.createStatement();
                result = statement.executeQuery(checkExistSql);

                if (result.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Book ID: " + bookIdTextField.getText() + " was already exist");
                    alert.showAndWait();
                }
                else {
                    String sql = "INSERT INTO books (book_id, title, author, genre, quantity, unit_price) " +
                                 "VALUES (?, ?, ?, ?, ?, ?)"; 
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, bookIdTextField.getText());
                    prepare.setString(2, bookTitleTextField.getText());
                    prepare.setString(3, bookAuthorTextField.getText());
                    prepare.setString(4, bookGenreTextField.getText());
                    prepare.setString(5, bookQtyTextField.getText());
                    prepare.setString(6, bookUnitPriceTextField.getText());
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added");
                    alert.showAndWait();

                    showBookListData();
                    clearBook();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void deleteBook() {
        Alert alert;
        connect = Database.connectDB();

        try {
            if (bookIdTextField.getText().isEmpty() || bookTitleTextField.getText().isEmpty() || 
                bookAuthorTextField.getText().isEmpty() || bookGenreTextField.getText().isEmpty() ||
                bookQtyTextField.getText().isEmpty() || bookUnitPriceTextField.getText().isEmpty()) {
                
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select an item");
                alert.showAndWait();
            }
            else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete ?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    String sql = "DELETE FROM books WHERE book_id = '" + bookIdTextField.getText() + "'";

                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successful Delete");
                    alert.showAndWait();

                    showBookListData();
                    clearBook();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateBook() {
        Alert alert;
        connect = Database.connectDB();

        try {
            if (bookIdTextField.getText().isEmpty() || bookTitleTextField.getText().isEmpty() || 
                bookAuthorTextField.getText().isEmpty() || bookGenreTextField.getText().isEmpty() ||
                bookQtyTextField.getText().isEmpty() || bookUnitPriceTextField.getText().isEmpty()) {
                
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill in all fields");
                alert.showAndWait();
            }
            else {
                String sql = "UPDATE books SET" +
                             " title = '"       + bookTitleTextField.getText()     + "'" +
                             ", author = '"     + bookAuthorTextField.getText()    + "'" +
                             ", genre = '"      + bookGenreTextField.getText()     + "'" +
                             ", quantity = '"   + bookQtyTextField.getText()       + "'" +
                             ", unit_price = '" + bookUnitPriceTextField.getText() + "'" +
                             " WHERE book_id = '" + bookIdTextField.getText()      + "'";

                statement = connect.createStatement();
                statement.executeUpdate(sql);

                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Updated");
                alert.showAndWait();

                showBookListData();
                clearBook();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void clearBook() {
        bookIdTextField.setText("");
        bookTitleTextField.setText("");
        bookAuthorTextField.setText("");
        bookGenreTextField.setText("");
        bookQtyTextField.setText("");
        bookUnitPriceTextField.setText("");
    }


    // get text from search text field and filter the data based on the text
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
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/SalesRecordPage.fxml"));
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
    public void initialize(URL location, ResourceBundle resources) {
        showBookListData();
    }
}
