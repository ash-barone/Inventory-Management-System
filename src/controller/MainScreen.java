package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * controller for main screen<br>
 * LOGICAL ERROR UNDER onActionToModifyProductScreen<br>
 */
public class MainScreen implements Initializable {

    /**
     *
     * @param url
     * @param resourceBundle
     * populated parts and products tables with each respective list
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //populate part table
        partTableView.setItems(Inventory.getAllParts());

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id")); //import stuff ofc//calls getId method to populate table

        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        partInvLvlCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        //populate product table
        productTableView.setItems(Inventory.getAllProducts());

        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id")); //import stuff ofc//calls getId method to populate table

        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        productInvLvlCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    @FXML
    private Button addPartBtn;

    @FXML
    private Button addProductBtn;

    @FXML
    private Button deletePartBtn;

    @FXML
    private Button deleteProductBtn;

    @FXML
    private Button exitBtn;

    @FXML
    private Button modifyPartBtn;

    @FXML
    private Button modifyProductBtn;

    @FXML
    private TableColumn<Part, Integer> partIdCol;

    @FXML
    private TableColumn<Part, Integer> partInvLvlCol;

    @FXML
    private TableColumn<Part, String> partNameCol;

    @FXML
    private TableColumn<Part, Double> partPriceCol;

    @FXML
    private TableView<Part> partTableView;

    @FXML
    private TableColumn<Product, Integer> productIdCol;

    @FXML
    private TableColumn<Product, Integer> productInvLvlCol;

    @FXML
    private TableColumn<Product, String> productNameCol;

    @FXML
    private TableColumn<Product, Double> productPriceCol;

    @FXML
    private TableView<Product> productTableView;

    @FXML
    private TextField searchPartIdOrNameTxt;

    @FXML
    private TextField searchProductIdOrName;

    /**
     *
     * @param event the event for selecting a part then clicking the delete button
     */
    @FXML
    void onActionDeletePart(ActionEvent event) {
        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();

        if(selectedPart == null){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setContentText("No part selected.");
            alert.showAndWait();

            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("You are about to delete " + selectedPart.getName() +".");
        alert.setContentText("Are you sure?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Inventory.getAllParts().remove(selectedPart);
            partTableView.setItems(Inventory.getAllParts());
        } else {
            alert.close();
        }

        /*Part part = partTableView.getSelectionModel().getSelectedItem();
        Inventory.getAllParts().remove(part);
        partTableView.setItems(Inventory.getAllParts());*/
    }

    /**
     *
     * @param event the event for selecting a product then clicking the delete button
     */
    @FXML
    void onActionDeleteProduct(ActionEvent event) {
        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();

        if(productTableView.getSelectionModel().getSelectedItem() == null){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setContentText("No product selected.");
            alert.showAndWait();

            return;
        }

        ObservableList<Part> associatedPartsForProduct = selectedProduct.getAllAssociatedParts();
        /**
         * check if associatedPartsForProduct is empty or not
         * do not allow delete if the associatedParts list is not empty
         */
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("You are about to delete " + selectedProduct.getName() + ".");
        alert.setContentText("Are you sure?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (!(associatedPartsForProduct.isEmpty())) {
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setTitle("Error! Cannot delete product.");
                alert2.setContentText("Product contains associated parts. Please remove parts and try again.");
                alert2.showAndWait();
            } else {
                Inventory.getAllProducts().remove(selectedProduct);
                productTableView.setItems(Inventory.getAllProducts());
            }
        }
        else {
            alert.close();
        }
    }

    /**
     *
     * @param event the event of clicking the exit button to close application
     */
    @FXML
    void onActionExitApplication(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText("You are about to exit.");
        alert.setContentText("Are you sure?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            stage.close();
        } else {
            alert.close();
        }


        /*Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        stage.close();*/
    }

    /**
     *
     * @param event the event of hitting enter after text input in the search field for part
     */
    @FXML
    void onActionSearchPart(ActionEvent event) {
        String searchPartText = searchPartIdOrNameTxt.getText();

        ObservableList<Part> searchPartResults = searchParts(searchPartText);

        partTableView.setItems(searchPartResults);
        searchPartIdOrNameTxt.setText("");

    }

    /**
     *
     * @param searchPartText the string used to compare text in search field to names and id or parts in allParts list
     * @return searchedPartResults list with matching parts
     */
    private ObservableList<Part> searchParts(String searchPartText) {

        /**
         * make searchedPartResults list to store the results
         * make allPartsToSearch to store the allParts list to compare searchPartText to
         */
        ObservableList<Part> searchedPartResults = FXCollections.observableArrayList();
        ObservableList<Part> allPartsToSearch = Inventory.getAllParts();

        /**
         * for loop to check if there is a matching name or id in the searchText
         */
        for (Part partSearch : allPartsToSearch) {
            if (partSearch.getName().contains(searchPartText)) {
                searchedPartResults.add(partSearch);
            }
            else if (String.valueOf(partSearch.getId()).contains(searchPartText)) {
                searchedPartResults.add(partSearch);

            }
        }

        /**
         * alert message for no matching part found
         */
        if (searchedPartResults.isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error! Part not found.");
            alert.setContentText("No matching part found. Please try again.");
            alert.showAndWait();

        }
        return searchedPartResults;
    }

    /**
     *
     * @param event the event of hitting enter after text input in the search field for product
     */
    @FXML
    void onActionSearchProduct(ActionEvent event) {

        String searchProductText = searchProductIdOrName.getText();

        ObservableList<Product> searchProductResults = searchProducts(searchProductText);

        productTableView.setItems(searchProductResults);
        searchProductIdOrName.setText("");

    }

    /**
     *
     * @param searchProductText the string used to compare text in search field to names and id or product in allProduct list
     * @return searchedProductResults list with matching product
     */
    private ObservableList<Product> searchProducts(String searchProductText) {

        /**
         * make searchedProductResults list to store the results
         * make allProductsToSearch to store the allProduct list to compare searchProductText to
         */
        ObservableList<Product> searchedProductResults = FXCollections.observableArrayList();
        ObservableList<Product> allProductsToSearch = Inventory.getAllProducts();

        /**
         * for loop to check if there is a matching name or id in the searchText
         */
        for (Product productSearch : allProductsToSearch) {
            if (productSearch.getName().contains(searchProductText)) {
                searchedProductResults.add(productSearch);
            }
            else if(String.valueOf(productSearch.getId()).contains(searchProductText)){
                searchedProductResults.add(productSearch);
            }
        }

        /**
         * alert message for no matching part found
         */
        if (searchedProductResults.isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error! Product not found.");
            alert.setContentText("No matching product found. Please try again.");
            alert.showAndWait();

        }
        return searchedProductResults;
    }

    /**
     *
     * @param event the event of clicking the add part button to navigate to add part screen
     * @throws IOException
     */
    @FXML
    void onActionToAddPartScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     * @param event the event of clicking the add product button to navigate to add product screen
     * @throws IOException
     */
    @FXML
    void onActionToAddProductScreen(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Add Product");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     * @param event the event of clicking the modify part button to navigate to modify part screen
     * @throws IOException
     */
    @FXML
    void onActionToModifyPartScreen(ActionEvent event) throws IOException{

        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();

        if(selectedPart == null){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setContentText("There is no part selected. Please select a part.");
            alert.showAndWait();
            alert.showAndWait();

            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ModifyPart.fxml"));
        loader.load();

        ModifyPart MPController = loader.getController();
        MPController.sendPart(partTableView.getSelectionModel().getSelectedItem());

        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setTitle("Modify Part");
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     *
     * @param event the event of clicking the modify product button to navigate to modify product screen
     * @throws IOException
     * LOGICAL ERROR //<br>
     * <p>The associatedParts table on the Modify Product screen was not populating with the selected Product's associatedParts
     * until another part added from the allParts table. I kept getting a null pointer error in the Modify Product Controller's initialize method<br>
     * I was able to fix this by initializing selectedProduct here in the event handler for navigating to the Modify Product screen first
     * before using it again in the sendProduct method inside the Modify Product controller. It now works properly.</p>
     *
     */
    @FXML
    void onActionToModifyProductScreen(ActionEvent event) throws IOException{

        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();

        if(selectedProduct == null){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setContentText("There is no product selected. Please select a product.");
            alert.showAndWait();

            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ModifyProduct.fxml"));
        loader.load();

        ModifyProduct ModifyProductController = loader.getController();
        ModifyProductController.sendProduct(productTableView.getSelectionModel().getSelectedItem());

        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setTitle("Modify Product");
        stage.setScene(new Scene(scene));
        stage.show();
    }

}
