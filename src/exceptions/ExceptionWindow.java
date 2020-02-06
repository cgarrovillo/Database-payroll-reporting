package exceptions;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * ******************************************** **
 * Exception - exceptions.Exception
 *
 * @author Christian Garrovillo
 * Information and Communications Technologies
 * Software Development
 * * ********************************************* **
 */
public class ExceptionWindow {

    private StringWriter writer;
    private PrintWriter print;

    public ExceptionWindow(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(s);
        alert.showAndWait();
    }

    public ExceptionWindow(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception");
        alert.setHeaderText(e.getMessage());
        TextArea area = new TextArea();
        area.setEditable(false);
        area.setText(getStack(e));
        alert.getDialogPane().setContent(area);
        alert.showAndWait();
    }

    private String getStack(Exception e) {
        writer = new StringWriter();
        print = new PrintWriter(writer);
        e.printStackTrace(print);
        String stackTrace = writer.toString();
        return stackTrace;
    }

}
