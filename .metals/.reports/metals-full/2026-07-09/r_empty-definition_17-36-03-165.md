error id: file:///C:/Users/User/OneDrive/Dokumen/GitHub/project-expo/SILAU_ALASKA/src/Login/Login.java:_empty_/Stage#setResizable#
file:///C:/Users/User/OneDrive/Dokumen/GitHub/project-expo/SILAU_ALASKA/src/Login/Login.java
empty definition using pc, found symbol in pc: _empty_/Stage#setResizable#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 510
uri: file:///C:/Users/User/OneDrive/Dokumen/GitHub/project-expo/SILAU_ALASKA/src/Login/Login.java
text:
```scala
package Login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Login extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("Login.fxml"));

        Scene scene = new Scene(loader.load());

        stage.setTitle("SILAU");

        stage.setScene(scene);
        stage.setResizab@@le(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: _empty_/Stage#setResizable#