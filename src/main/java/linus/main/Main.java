package linus.main;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import linus.Linus;
import linus.mainwindow.MainWindow;

/**
 * A GUI for Linus using FXML.
 */
public class Main extends Application {

    private Linus linus = new Linus("data/tasklist.txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setMinHeight(220);
            stage.setMinWidth(417);
            fxmlLoader.<MainWindow>getController().setLinus(linus);  // inject the Linus instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



