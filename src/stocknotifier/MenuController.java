package stocknotifier;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.*;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by anthony on 4/4/17.
 */
public class MenuController implements Initializable {
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public JFXButton requestTabButton;
    @FXML
    public JFXButton localStockTabButton;
    @FXML
    public Pane contentPane;
    @FXML
    public VBox sideBar;

    private JFXButton requestButton;
    private JFXButton deleteButton;
    private ObservableList<Product> localProducts;
    private JFXTreeTableView<Product> localStockTreeView;

    private JFXButton acceptButton;
    private ObservableList<Product> requestedProducts;
    private JFXTreeTableView<Product> requestedStockTreeView;
    private TrayNotification trayNotification = new TrayNotification();

    private boolean onRequestTab;

    //TODO Implement Firebase
//    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
//    private DatabaseReference ref = database.getReference("stock");


    public void requestTabButtonPressed(ActionEvent actionEvent) {
        requestTabButton.setButtonType(JFXButton.ButtonType.RAISED);
        localStockTabButton.setButtonType(JFXButton.ButtonType.FLAT);

        if (!onRequestTab) {
            contentPane.getChildren().remove(localStockTreeView);
            contentPane.getChildren().remove(requestButton);
            contentPane.getChildren().remove(deleteButton);
            contentPane.getChildren().add(requestedStockTreeView);
            contentPane.getChildren().add(acceptButton);
            onRequestTab = true;
        }


    }

    public void localStockTabButtonPressed(ActionEvent actionEvent) {
        requestTabButton.setButtonType(JFXButton.ButtonType.FLAT);
        localStockTabButton.setButtonType(JFXButton.ButtonType.RAISED);

        if (onRequestTab) {
            contentPane.getChildren().add(localStockTreeView);
            contentPane.getChildren().add(requestButton);
            contentPane.getChildren().add(deleteButton);
            contentPane.getChildren().remove(requestedStockTreeView);
            contentPane.getChildren().remove(acceptButton);
            onRequestTab = false;
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sideBar.setSpacing(5);
        //TODO Get Data from Firebase
//        DatabaseReference newPostRef = ref.push();
//        newPostRef.setValue(new Product("HG-0012", "Baju Dinosaurus", "Baju", "Rp. 100.000", "Dago"));

        initLocalStockTreeView();
        initRequestedStockTreeView();
        initButtons();

        onRequestTab = true;
        contentPane.getChildren().add(requestedStockTreeView);
        contentPane.getChildren().add(acceptButton);
    }

    private void initButtons() {
        deleteButton = new JFXButton("Delete");
        deleteButton.setLayoutY(500.0);
        deleteButton.setLayoutX(100.0);
        deleteButton.setOnAction(this::deleteButtonPressed);
        deleteButton.setButtonType(JFXButton.ButtonType.RAISED);
        deleteButton.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Paint.valueOf("rgb(255,107,107)"), CornerRadii.EMPTY, Insets.EMPTY)));

        requestButton = new JFXButton("Request");
        requestButton.setLayoutY(500.0);
        requestButton.setOnAction(this::requestButtonPressed);
        requestButton.setButtonType(JFXButton.ButtonType.RAISED);
        requestButton.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Paint.valueOf("rgb(198,243,100)"), CornerRadii.EMPTY, Insets.EMPTY)));

        if (localProducts.isEmpty()) {
            deleteButton.setDisable(true);
            requestButton.setDisable(true);
        }
        acceptButton = new JFXButton("Accept Request");
        acceptButton.setLayoutY(500.0);
        acceptButton.setOnAction(this::acceptButtonPressed);
        acceptButton.setButtonType(JFXButton.ButtonType.RAISED);
        acceptButton.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Paint.valueOf("rgb(198,243,100)"), CornerRadii.EMPTY, Insets.EMPTY)));

        if (requestedProducts.isEmpty()) {
            acceptButton.setDisable(true);
        }
    }

    private void initLocalStockTreeView() {
        localStockTreeView = new JFXTreeTableView<>();
        localStockTreeView.setPrefHeight(490.0);

        JFXTreeTableColumn<Product, String> idCol = new JFXTreeTableColumn<>("ID Product");
        idCol.setPrefWidth(150);
        idCol.setCellValueFactory(param -> param.getValue().getValue().idProp);

        JFXTreeTableColumn<Product, String> nameCol = new JFXTreeTableColumn<>("Name");
        nameCol.setPrefWidth(150);
        nameCol.setCellValueFactory(param -> param.getValue().getValue().nameProp);

        JFXTreeTableColumn<Product, String> typeCol = new JFXTreeTableColumn<>("Type");
        typeCol.setPrefWidth(150);
        typeCol.setCellValueFactory(param -> param.getValue().getValue().typeProp);

        JFXTreeTableColumn<Product, String> priceCol = new JFXTreeTableColumn<>("Price");
        priceCol.setPrefWidth(150);
        priceCol.setCellValueFactory(param -> param.getValue().getValue().priceProp);

        localProducts = FXCollections.observableArrayList();
        localProducts.add(new Product("HG-0011", "Baju T-rex", "Baju", "Rp. 103.000"));

        final TreeItem<Product> root = new RecursiveTreeItem<>(localProducts, RecursiveTreeObject::getChildren);

        localStockTreeView.getColumns().setAll(idCol, nameCol, typeCol, priceCol);
        localStockTreeView.setRoot(root);
        localStockTreeView.setShowRoot(false);
    }

    private void initRequestedStockTreeView() {
        requestedStockTreeView = new JFXTreeTableView<>();
        requestedStockTreeView.setPrefHeight(490.0);

        JFXTreeTableColumn<Product, String> idCol = new JFXTreeTableColumn<>("ID Product");
        idCol.setPrefWidth(120);
        idCol.setCellValueFactory(param -> param.getValue().getValue().idProp);

        JFXTreeTableColumn<Product, String> nameCol = new JFXTreeTableColumn<>("Name");
        nameCol.setPrefWidth(120);
        nameCol.setCellValueFactory(param -> param.getValue().getValue().nameProp);

        JFXTreeTableColumn<Product, String> typeCol = new JFXTreeTableColumn<>("Type");
        typeCol.setPrefWidth(120);
        typeCol.setCellValueFactory(param -> param.getValue().getValue().typeProp);

        JFXTreeTableColumn<Product, String> priceCol = new JFXTreeTableColumn<>("Price");
        priceCol.setPrefWidth(120);
        priceCol.setCellValueFactory(param -> param.getValue().getValue().priceProp);

        JFXTreeTableColumn<Product, String> outletCol = new JFXTreeTableColumn<>("Outlet");
        outletCol.setPrefWidth(120);
        outletCol.setCellValueFactory(param -> param.getValue().getValue().outletProp);

        requestedProducts = FXCollections.observableArrayList();
        requestedProducts.add(new Product("HG-0012", "Baju Dinosaurus", "Baju", "Rp. 100.000", "Dago"));

        final TreeItem<Product> root = new RecursiveTreeItem<>(requestedProducts, RecursiveTreeObject::getChildren);

        requestedStockTreeView.getColumns().setAll(idCol, nameCol, typeCol, priceCol, outletCol);
        requestedStockTreeView.setRoot(root);
        requestedStockTreeView.setShowRoot(false);
    }


    private void deleteButtonPressed(ActionEvent actionEvent) {
        deleteLocalItem();
    }

    private void requestButtonPressed(ActionEvent event) {
        deleteLocalItem();
    }

    private void deleteLocalItem() {
        TreeItem<Product> selectedItem = localStockTreeView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            return;
        }
        Product selectedItemValue = selectedItem.getValue();
        localProducts.remove(selectedItemValue);
        if (localProducts.isEmpty()) {
            deleteButton.setDisable(true);
            requestButton.setDisable(true);

        }
    }


    private void acceptButtonPressed(ActionEvent actionEvent) {
        deleteRequestedItem();
    }

    private void deleteRequestedItem() {
        TreeItem<Product> selectedItem = requestedStockTreeView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            return;
        }
        Product selectedItemValue = selectedItem.getValue();
        requestedProducts.remove(selectedItemValue);
        if (requestedProducts.isEmpty()) {
            acceptButton.setDisable(true);
        }
    }

    public void notifyEmptyStock() {
        if (trayNotification.isTrayShowing()) return;
        Product testProduct = new Product("HG-0011", "Baju T-rex", "Baju", "Rp. 103.000");
        String title = "Empty Stock!";
        String message = testProduct.productName + " has gone empty!";
        NotificationType notification = NotificationType.WARNING;

        trayNotification.setTitle(title);
        trayNotification.setMessage(message);
        trayNotification.setNotificationType(notification);
        trayNotification.setAnimationType(AnimationType.POPUP);
        trayNotification.showAndDismiss(Duration.seconds(2));
        localProducts.add(testProduct);

    }



    class Product extends RecursiveTreeObject<Product> {

        StringProperty idProp;
        public String productID;
        StringProperty nameProp;
        public String productName;
        StringProperty typeProp;
        public String productType;
        StringProperty priceProp;
        public String productPrice;
        StringProperty outletProp;
        public String productOutlet;


        Product(String idProp, String nameProp, String typeProp, String priceProp) {
            this.idProp = new SimpleStringProperty(idProp);
            productID = idProp;
            this.nameProp = new SimpleStringProperty(nameProp);
            productName = nameProp;
            this.typeProp = new SimpleStringProperty(typeProp);
            productType = typeProp;
            this.priceProp = new SimpleStringProperty(priceProp);
            productPrice = priceProp;
        }

        Product(String idProp, String nameProp, String typeProp, String priceProp, String outletProp) {
            this(idProp, nameProp, typeProp, priceProp);
            this.outletProp = new SimpleStringProperty(outletProp);
            productOutlet = outletProp;
        }


    }
}
