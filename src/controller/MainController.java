package controller;

import exceptions.AlertWindow;
import exceptions.ExceptionWindow;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Optional;

/**
 * ******************************************** **
 * MainController - controller.MainController
 *
 * @author Christian Garrovillo
 * Information and Communications Technologies
 * Software Development
 * * ********************************************* **
 */
public class MainController {

    @FXML
    public Button checkBut;
    @FXML
    private Button processBut;
    @FXML
    private Button performBut;
    @FXML
    private Button exportBut;
    @FXML
    private Label message;

    @FXML
    private TextField userField;
    @FXML
    private TextField passField;

    static private Connection conn;
    static String username;
    static String password;
    File inputTextFile;
    String ctlFilePath;

    @FXML
    public void initialize() {
//        processBut.setDisable(true);
//        performBut.setDisable(true);
//        exportBut.setDisable(true);
    }

    @FXML
    private void check() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Login to Database");
        stage.setScene(scene);
        stage.showAndWait();
        validateConnection();
    }

    @FXML
    private void process() {
        try {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            fc.setTitle("Open delimited text file location");
            while (inputTextFile==null) {
                inputTextFile = fc.showOpenDialog(performBut.getScene().getWindow());
            }

            FileChooser save = new FileChooser();
            save.setTitle("Choose SQL*Loader control file save location");
            save.getExtensionFilters().add(new FileChooser.ExtensionFilter("CTL File", "*.ctl"));
            File outputControlFile = null;
            while (outputControlFile == null) {
                outputControlFile = save.showSaveDialog(performBut.getScene().getWindow());
            }
            ctlFilePath = outputControlFile.getAbsolutePath();
            new AlertWindow("File paths provided: ", "Processing: " + inputTextFile.getAbsolutePath()
                            + "\n\nControl File: " + ctlFilePath);

            FileWriter fw = new FileWriter(outputControlFile);
            fw.write("LOAD DATA" +
                    "\nINFILE '" + inputTextFile.getAbsolutePath() + "' " +
                    "\nINSERT INTO TABLE payroll_load" +
                    "\nFIELDS TERMINATED BY ';' OPTIONALLY ENCLOSED BY '\"'" +
                    "\nTRAILING NULLCOLS" +
                    "\n(payroll_date DATE \"Month dd, yyyy\"," +
                    "\n employee_id," +
                    "\n amount," +
                    "\n status)");
            fw.close();

            int fileNameLength = outputControlFile.getName().length();
            String logFilePath = ctlFilePath.substring(0, ctlFilePath.length()-fileNameLength) + "ctl.log";
            String sql = "sqlldr userid=" + username + "/" + password
                    + " control='" + ctlFilePath + "' log= '" + logFilePath + "'";
            new AlertWindow("SQLLoader command", "The following command will execute: \n\n" + sql);

            int exitValue;
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(sql);

            exitValue = proc.waitFor();
            if (exitValue == 0) {
                new AlertWindow("File loaded to database successfully.", "Exit value: " + exitValue);
                processBut.setDisable(true);
            }
            else {
                new ExceptionWindow("File load unsuccessful. Open .log file for details.");
            }

        } catch (IOException | InterruptedException e) {
            new ExceptionWindow(e);
        }
    }

    @FXML
    private void perform() {
        try {
            new AlertWindow("Month End Process will be performed.");
            CallableStatement call = conn.prepareCall("{call PRC_ZEROOUT}");
            call.execute();
            performBut.setDisable(true);
        } catch (SQLException e) {
            new ExceptionWindow(e);
        }
    }

    @FXML
    private void export() {
        FileChooser export = new FileChooser();
        export.setTitle("Choose Export file save location");
        File exportFile = null;
        while (exportFile == null) {
            exportFile = export.showSaveDialog(performBut.getScene().getWindow());
        }

        TextInputDialog aliasDialog = new TextInputDialog();
        aliasDialog.setTitle("Directory Alias");
        aliasDialog.setHeaderText("Choose a Directory Alias");
        aliasDialog.setContentText("Alias: ");
        Optional<String> result = aliasDialog.showAndWait();
        String alias = null;
        if (result.isPresent()) {
            alias = result.get();
        }
        alias = alias.toUpperCase();
        String path = exportFile.getAbsolutePath();
        int fileNameLength = exportFile.getName().length();
        String directoryPath = path.substring(0, (path.length()-fileNameLength) - 1);
        System.out.println(directoryPath);
        String sql = "CREATE OR REPLACE DIRECTORY " + alias + " AS '" + directoryPath + "'";
        try {
            Statement s = conn.createStatement();
            s.executeUpdate(sql);

            CallableStatement call = conn.prepareCall("{call PROC_POPULATE(?, ?)}");
            call.setString(1, alias);
            call.setString(2, exportFile.getName());

            int rowsAffected = call.executeUpdate();
            if (rowsAffected > 0) {
                new AlertWindow("Procedure ran successfully.", "Rows affected: " + rowsAffected);
                exportBut.setDisable(true);
            }
        } catch (SQLException e) {
            new ExceptionWindow(e);
        }
    }



    private boolean validateConnection() {
        try {
            if (conn.isValid(3)) {
                checkBut.setDisable(true);
                processBut.setDisable(false);
                return true;
            }
        } catch (SQLException e) {
            new ExceptionWindow(e);
        }
        new ExceptionWindow("Unable to validate connection to database");
        return false;
    }

    public static void closeConnection() {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setConnection(Connection connect){
        conn = connect;
    }

    public static Connection getConnection() {
        return conn;
    }

    public static void setUsername(String user) { username = user;}

    public static void setPassword(String pw) {password = pw;}

}
