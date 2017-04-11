package stocknotifier;

import com.jfoenix.controls.*;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.asynchttpclient.*;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginController implements Initializable{

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXTextField email;

    @FXML
    private JFXPasswordField pass;

    @FXML
    private JFXButton login;

    @FXML
    private JFXSpinner spinner;

    @FXML
    void makeLogin(ActionEvent event) {
        email.setDisable(true);
        pass.setDisable(true);
        login.setDisable(true);
        spinner.setVisible(true);

        String emailStr = email.getText();
        String passwordStr = pass.getText();
        JFXSnackbar snackbar = new JFXSnackbar(anchorPane);
        executor.submit(() -> {
            try {
                attemptLogin(emailStr, passwordStr, snackbar, event);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    private void attemptLogin(String emailStr, String passwordStr, JFXSnackbar responseSnackbar, ActionEvent event) throws UnirestException {
        String requestBodyStr = "{\n    email: \"" + emailStr + "\",\n    password: \"" + passwordStr + "\",\n    returnSecureToken: true\n}";
        String urlAuth = "https://www.googleapis.com/identitytoolkit/v3/relyingparty/verifyPassword?key=AIzaSyBFVLxcn0Siqyd16V6kw3z4XL72142LrOM";

        AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();
        Request req = new RequestBuilder("POST")
                .setUrl(urlAuth)
                .addHeader("Content-Type", "application/json")
                .setBody(requestBodyStr)
                .build();
        asyncHttpClient.
                prepareRequest(req).
                execute(new AsyncCompletionHandler<Response>(){

                    @Override
                    public Response onCompleted(Response response) throws Exception{
                        JSONObject jsonObject = new JSONObject(response.getResponseBody());
                        if (jsonObject.has("error")) {
                            failLogin(jsonObject.getJSONObject("error"), responseSnackbar);
                            return response;
                        }
                        System.out.println(response.getResponseBody());
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                                Parent menu = null;
                                try {
                                    menu = FXMLLoader.load(getClass().getResource("menu.fxml"));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                primaryStage.setScene(new Scene(menu));
                                primaryStage.show();
                            }
                        });

                        return response;
                    }

                    @Override
                    public void onThrowable(Throwable t){
                        // Something wrong happened.
                    }
                });
    }

    private void failLogin(JSONObject errorObject, JFXSnackbar responseSnackbar) {
        String errorMessage;
        if (errorObject != null) {
            errorMessage = errorObject.get("message").toString();
        } else {
            errorMessage = "Unknown Error";
        }
        email.setDisable(false);
        pass.setDisable(false);
        login.setDisable(false);
        spinner.setVisible(false);
        Platform.runLater(() -> responseSnackbar.show(errorMessage, 2000));
    }
}
