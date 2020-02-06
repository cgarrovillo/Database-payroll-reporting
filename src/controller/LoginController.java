package controller;

import exceptions.AlertWindow;
import exceptions.ExceptionWindow;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.*;

/**
 * ******************************************** **
 * LoginController - controller.LoginController
 *
 * @author Christian Garrovillo
 * Information and Communications Technologies
 * Software Development
 * * ********************************************* **
 */
public class LoginController {

    @FXML
    public TextField userField;
    @FXML
    public TextField passField;
    @FXML
    public Button loginBut;

    String username;
    String password;

    Connection conn;

    //handles Login click event
    @FXML
    private void login(ActionEvent event) throws InterruptedException {
        boolean connected = loginSupport();
        if (connected) {
            new AlertWindow("Connected to database successfully", "Verifying Permissions...");
            verifyPerms();
        }
    }
    //support method for login()
    private boolean loginSupport() {
        try {
            final String url = "jdbc:oracle:thin:@localhost:1521:XE";
            username = userField.getText();
            password = passField.getText();
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(url, username, password);
            MainController.setConnection(conn);
            if (conn.isValid(3)) {
                MainController.setUsername(username);
                MainController.setPassword(password);
                return true;
            }
        } catch (ClassNotFoundException | SQLException e) {
            new ExceptionWindow(e);
        }
        return false;
    }

    //support method
    private void verifyPerms() {
        String sql = username;
        CallableStatement call = null;
        try {
            call = conn.prepareCall("{?= call PERMSCHECK(?) }");
            call.setString(2, sql);
            call.registerOutParameter(1, Types.CHAR);
            call.execute();
            char privileged = call.getString(1).charAt(0);
            if (privileged == 'Y') {
                new AlertWindow("Permissions Granted");
                closeStage(loginBut);
            }
            else {
                new Exception("Logged in user does not have enough permissions");
            }
        } catch (SQLException e) {
            new ExceptionWindow(e);
        }
    }

    private void closeStage(Node node) {
        Scene scene = node.getScene();
        Stage stg = (Stage) scene.getWindow();
        stg.close();
    }

}
