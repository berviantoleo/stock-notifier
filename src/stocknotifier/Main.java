package stocknotifier;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //TODO Implement Firebase
//        FileInputStream serviceAccount =
//                new FileInputStream("conf/serviceAccountKey.json");
//
//        FirebaseOptions options = new FirebaseOptions.Builder()
//                .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
//                .setDatabaseUrl("https://stocknotifier-a7cdf.firebaseio.com")
//                .build();
//
//        FirebaseApp.initializeApp(options);

        Parent login = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("Stock Notifier");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(login));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
