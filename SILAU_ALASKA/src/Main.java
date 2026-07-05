import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setTitle("SILAU - Sistem Informasi Laundry");

        changeScene("/laundry/view/Login.fxml");

        primaryStage.show();
    }

    public static void changeScene(String fxmlPath) throws Exception {
        Parent root = FXMLLoader.load(Main.class.getResource(fxmlPath));
        primaryStage.setScene(new Scene(root));
    }

    public static void main(String[] args) {
        launch(args);
    }
}