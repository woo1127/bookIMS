package com.bookIMS.Controllers;

import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import com.bookIMS.Database;

public class LoginController implements Initializable {

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private Button loginBtn;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    
    public void loginClick(ActionEvent e) throws IOException {
        connect = Database.connectDB();
        String sql = "SELECT * FROM users WHERE username = ? and password = ?";
        
        try {
            Alert alert;

            prepare = connect.prepareStatement(sql);
            prepare.setString(1, username.getText());
            prepare.setString(2, password.getText());
            result = prepare.executeQuery();

            if (username.getText().isEmpty() || password.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill in your username and password.");
                alert.showAndWait();
            }
            else if (result.next()) {
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Login successful!");
                alert.showAndWait();
                
                // hide login page to show the next page
                loginBtn.getScene().getWindow().hide();
                
                // define the next page to be redirect by check if user is admin or sales person
                String nextPageUrl = result.getInt("role_id") == 1 ? "../Views/DashboardPage.fxml" : "../Views/SalesPage.fxml";
                FXMLLoader loader = new FXMLLoader(getClass().getResource(nextPageUrl));
                Stage stage = new Stage();
                Scene scene = new Scene(loader.load());
        
                if (result.getInt("role_id") == 1) {
                    DashboardController controller = loader.getController();
                    controller.setUsernameLabel(username.getText());
                }
                else {
                    SalesController controller = loader.getController();
                    controller.setUsernameLabel(username.getText());
                }

                stage.setScene(scene);
                stage.show();
            }
            else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid username or password.");
                alert.showAndWait();
            }
        }
        catch (Exception err) {
            err.printStackTrace();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
