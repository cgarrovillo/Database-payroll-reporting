package exceptions;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

/**
 * ******************************************** **
 * AlertWindow - exceptions.AlertWindow
 *
 * @author Christian Garrovillo
 * Information and Communications Technologies
 * Software Development
 * * ********************************************* **
 */
public class AlertWindow {

    public AlertWindow(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(s);
        alert.showAndWait();
    }

    public AlertWindow(String s, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(s);
        TextArea area = new TextArea();
        area.setEditable(false);
        area.setText(content);
        alert.getDialogPane().setContent(area);
        alert.showAndWait();
    }
}
