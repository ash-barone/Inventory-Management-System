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
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * controller for add product screen
 */
public class AddProduct implements Initializable {

    /**
     *
     * @param url
     * @param resourceBundle
     * initialize populated the top table with Inventory.getAllParts() and the bottom table with associatedParts
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        addPartToProductTableView.setItems(Inventory.getAllParts());
        associatedPartsInProductTableView.setItems(associatedParts);


        addPartToProductTableView.setItems(Inventory.getAllParts());
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id")); //import stuff ofc//calls getId method to populate table
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedPartsInProductTableView.setItems(associatedParts);
        associatedPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id")); //import stuff ofc//calls getId method to populate table
        associatedPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    @FXML
    private TextField addPartIdTxtField;

    @FXML
    private Button addPartToProductBtn;

    @FXML
    private Button addProductCancelBtn;

    @FXML
    private Label addProductIdLbl;

    @FXML
    private Label addProductInvLbl;

    @FXML
    private TextField addProductInvTxt;

    @FXML
    private Label addProductLbl;

    @FXML
    private Label addProductMaxLbl;

    @FXML
    private TextField addProductMaxTxt;

    @FXML
    private Label addProductMinLbl;

    @FXML
    private TextField addProductMinTxt;

    @FXML
    private Label addProductNameLbl;

    @FXML
    private TextField addProductNameTxt;

    @FXML
    private Label addProductPriceLbl;

    @FXML
    private TextField addProductPriceTxt;

    @FXML
    private Button addProductSaveBtn;

    @FXML
    private TextField addSearchPartIdOrNameTxtField;

    @FXML
    private TableColumn<Part, Integer> associatedPartIdCol;

    @FXML
    private TableColumn<Part, Integer> associatedPartInvCol;

    @FXML
    private TableColumn<Part, String> associatedPartNameCol;

    @FXML
    private TableColumn<Part, Double> associatedPartPriceCol;

    @FXML
    private TableView<Part> associatedPartsInProductTableView;

    @FXML
    private TableView<Part> addPartToProductTableView;

    @FXML
    private TableColumn<Part, Integer> partIdCol;

    @FXML
    private TableColumn<Part, Integer> partInvCol;

    @FXML
    private TableColumn<Part, String> partNameCol;

    @FXML
    private TableColumn<Part, Double> partPriceCol;

    @FXML
    private Button removeAssociatedPartBtn;

    private static ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     *
     * @param event the end of adding a part from top table to the associated parts table for the new product
     */
    @FXML
    void onActionAddPartToProduct(ActionEvent event) {
        Part part = addPartToProductTableView.getSelectionModel().getSelectedItem();
        associatedParts.add(part);
        associatedPartsInProductTableView.setItems(associatedParts);

    }

    /**
     *
     * @param event the event of clicking on the cancel button to go back to main screen
     * @throws IOException
     */
    @FXML
    void onActionCancelToMainScreen(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("You are about to cancel.");
        alert.setContentText("Are you sure?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Main Menu");
            stage.setScene(scene);
            stage.show();
        } else {
            alert.close();
        }

        /*Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();*/
    }

    /**
     *
     * @param event the event of removing a part from the associatedPart table thus removing it from the associatedPart list for the new product
     */
    @FXML
    void onActionRemoveAssociatedPart(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Removal");
        alert.setHeaderText("You are about to remove an associated part.");
        alert.setContentText("Are you sure?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Part part = associatedPartsInProductTableView.getSelectionModel().getSelectedItem();
            associatedParts.remove(part);
            associatedPartsInProductTableView.setItems(associatedParts);
        } else {
            alert.close();
        }

        Part part = associatedPartsInProductTableView.getSelectionModel().getSelectedItem();
        associatedParts.remove(part);
        associatedPartsInProductTableView.setItems(associatedParts);
    }

    /**
     *
     * @param event the event of clicking the save button to save the product you just made
     * @throws IOException
     */
    @FXML
    void onActionSaveNewProduct(ActionEvent event) throws IOException {
        int id = 0;
        for (Product product : Inventory.getAllProducts()) {
            if (product.getId() > id) {
                id = product.getId();
            }
        }
        id = id + 1;
        try {

            String name = addProductNameTxt.getText();
            int stock = Integer.parseInt(addProductInvTxt.getText());
            double price = Double.parseDouble(addProductPriceTxt.getText());
            int max = Integer.parseInt(addProductMaxTxt.getText());
            int min = Integer.parseInt(addProductMinTxt.getText());

            //checkMinMaxValues();
            //checkInvValues();

            Product tempProduct = new Product(id, name, price, stock, max, min);

            boolean minMaxValuesCheck = checkMinMaxValues();
            boolean invValuesCheck = checkInvValues();

            //check if radio selected or nah
            if(minMaxValuesCheck == true && invValuesCheck == true) {
                Inventory.addProduct(tempProduct);
                for (Part part : associatedParts) {
                    tempProduct.addAssociatedPart(part);
                }
            }
            else {
                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error");
                alert.setContentText("Please ensure all values are correct and not blank.");
                alert.showAndWait();
                return;
            }

            Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Main Menu");
            stage.setScene(scene);
            stage.show();
        }

        catch (NumberFormatException e) {
            e.printStackTrace();
            Alert alert = new Alert((Alert.AlertType.ERROR));
            alert.setTitle("Error");
            alert.setContentText("Please ensure all values are correct and not blank.");
            alert.showAndWait();
            return;

        }
    }

    /**
     *
     * @param event the event of hitting enter after inputting text into the search part field
     */
    @FXML
    void onActionSearchPartIdOrName(ActionEvent event) {
        String searchPartText = addSearchPartIdOrNameTxtField.getText();

        ObservableList<Part> searchPartResults = searchParts(searchPartText);

        addPartToProductTableView.setItems(searchPartResults);
        addSearchPartIdOrNameTxtField.setText("");

    }

    /**
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
            if (partSearch.getName().contains(searchPartText) || String.valueOf(partSearch.getId()).contains(searchPartText)) {
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
            //return;

        }
        return searchedPartResults;
    }
    /**
     * method to check for accuracy of min and max values
     */
    public boolean checkMinMaxValues() {
        int min = Integer.parseInt(addProductMinTxt.getText());
        int max = Integer.parseInt(addProductMaxTxt.getText());

        if(min > max) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setContentText("Min must be less than Max. Please try again.");
            //alert.showAndWait();
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                alert.close();
            }
            return false;
        }
        return true;
    }

    /**
     * method to check stock values are greater than min value and less than max values
     */
    public boolean checkInvValues() {
        int min = Integer.parseInt(addProductMinTxt.getText());
        int max = Integer.parseInt(addProductMaxTxt.getText());
        int stock = Integer.parseInt(addProductInvTxt.getText());

        if(stock > max || stock < min){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setContentText("Inventory must be greater than min and less than max. Please try again.");
            //alert.showAndWait();
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                alert.close();
            }
            return false;
        }
        return true;
    }
}


