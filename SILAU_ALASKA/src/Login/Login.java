package Login;

import Karyawan.AppStorage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Login extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        AppStorage.muatSemua();


        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("Login.fxml"));

        Scene scene = new Scene(loader.load());

        stage.setTitle("SILAU");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setOnCloseRequest(event -> AppStorage.simpanSemua());
        stage.show();
    }

        @Override
    public void stop() throws Exception {
        AppStorage.simpanSemua();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}