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
 * controller for modify product screen <br>
 * LOGICAL ERROR UNDER onActionSaveProduct<br>
 */
public class ModifyProduct implements Initializable {
    Product selectedProduct = new Product(0,null, 0.0, 0,0,0);
    /**
     *
     * @param url
     * @param resourceBundle
     * in initialize populate the allPart table on top and the associatedParts table on bottom according to the sent Product selected on the main screen
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        //selectedProduct = new Product(0, null, 0.0, 0,0,0);
        associatedParts = selectedProduct.getAllAssociatedParts();

        //tell tables where to get items from
        associatedPartsInProductTableView.setItems(associatedParts);
        addPartToProductTableView.setItems(Inventory.getAllParts());

        //sets each column to specific values
        //addPartToProductTableView.setItems(Inventory.getAllParts());
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        //addPartToProductTableView.setItems(Inventory.getAllParts());


        //associatedPartsInProductTableView.setItems(associatedParts);
        associatedPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id")); //import stuff ofc//calls getId method to populate table
        associatedPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        //associatedPartsInProductTableView.setItems(selectedProduct.getAllAssociatedParts());

    }

    @FXML
    private Button addAssociatedPartToProductBtn;

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
    private Button cancelBtn;

    @FXML
    private TextField modifyProductIdTxt;

    @FXML
    private TextField modifyProductInvTxt;

    @FXML
    private TextField modifyProductMaxTxt;

    @FXML
    private TextField modifyProductMinTxt;

    @FXML
    private TextField modifyProductNameTxt;

    @FXML
    private TextField modifyProductPriceTxt;

    @FXML
    private Button removeAssociatedPartBtn;

    @FXML
    private Button saveProductBtn;

    @FXML
    private TextField searchPartIdOrName;

    //= (0,null,0.0,0,0,0);

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     *
     * @param event the event of clicking the add button to add the selected part in top table to the associated parts list/table for the modified product
     */
    @FXML
    void onActionAddPartToAssociatedParts(ActionEvent event) {

        Part part = addPartToProductTableView.getSelectionModel().getSelectedItem();
        associatedParts.add(part);
        associatedPartsInProductTableView.setItems(associatedParts);
    }

    /**
     *
     * @param event the event of clicking the cancel button to return to the main screen
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
     * @param event the event of clicking the remove associated part button to remove a selected part from the bottom table associatedParts
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
            if(associatedPartsInProductTableView.getSelectionModel().getSelectedItem() == null){
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setContentText("No associated part selected.");
                alert2.showAndWait();

                return;
            }
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
     * @param event the event of clicking on the save button to save the modified product and return to the main screen
     * @throws IOException
     * * * LOGICAL ERROR //<br>
     *  *<p>My original code for onActionSaveProduct was generating new id for each modified product instead of keeping originally assigned id as intended.<br>
     *  *Before I was running a for loop that cycled through all the products in allProducts. An if statement would check each product id against the text
     *  *in the product id text field on the form with the ultimate goal of finding the highest id and then adding 1 to that id. This would generate a new id
     *  *for the product each time. Since this page is for modifying products, I wanted to keep the original id the same after modifying other information. <br>
     *  *To fix this, I used getText() to populate the text field upon opening the Modify Product screen. Then, I initialized an id variable to be set using that same
     *  *getText() method. When creating the tempProduct object, I just pass that local id variable into the id parameter. This allows me to keep the original
     *  *id on any modified product</p><br>
     *  * <br>
     *  */

    @FXML
    void onActionSaveProduct(ActionEvent event) throws IOException {

        //below just gets the sent id from selected product on main screen
        int id = Integer.parseInt(modifyProductIdTxt.getText());

        /*
        *check through all product ids, find highers, then add 1
        int id = 0;
        for(Product product : Inventory.getAllProducts()){
            if(product.getId() > id) {
                id = product.getId();
            }
        }
        id = id + 1;*/
        try {

            //set constructor values by getting text from text fields
            String name = modifyProductNameTxt.getText();
            int stock = Integer.parseInt(modifyProductInvTxt.getText());
            double price = Double.parseDouble(modifyProductPriceTxt.getText());
            int max = Integer.parseInt(modifyProductMaxTxt.getText());
            int min = Integer.parseInt(modifyProductMinTxt.getText());

            //checkMinMaxValues();
            //checkInvValues();

            //initialize temp product with values above
            Product tempProduct = new Product(id, name, price, stock, min, max);
            for (Part part : associatedParts) {
                //if (!(Product.getAllAssociatedParts().contains(part))) {
                tempProduct.addAssociatedPart(part);
                //}
            }


            //add the above product to the all products list
            //Inventory.deleteProduct(selectedProduct);
            boolean minMaxValuesCheck = checkMinMaxValues();
            boolean invValuesCheck = checkInvValues();

            //check if radio selected or nah
            if(minMaxValuesCheck == true && invValuesCheck == true) {
                Inventory.addProduct(tempProduct);
                Inventory.deleteProduct(selectedProduct);
            }
            else {
                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error");
                alert.setContentText("Please ensure all values are correct and not blank.");
                alert.showAndWait();
                return;
            }
            //


            //load back to main menu
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
     * @param product the method used for sending a product selected on the main screen to the modify product screen to be modified
     */
    public void sendProduct(Product product) {

        //set temp part to be passed for removal upon saving
        selectedProduct = product;

        //fill in all your text fields
        modifyProductIdTxt.setText(String.valueOf(selectedProduct.getId()));
        modifyProductNameTxt.setText(selectedProduct.getName());
        modifyProductInvTxt.setText(String.valueOf(selectedProduct.getStock()));
        modifyProductPriceTxt.setText(String.valueOf(selectedProduct.getPrice()));
        modifyProductMaxTxt.setText(String.valueOf(selectedProduct.getMax()));
        modifyProductMinTxt.setText(String.valueOf(selectedProduct.getMin()));

        associatedParts = selectedProduct.getAllAssociatedParts();
        associatedPartsInProductTableView.setItems(associatedParts);
        //associatedPartsInProductTableView.setItems(associatedParts);
    }

    /**
     *
     * @param event the event of hitting enter after inputting text into the search part text field
     */
    @FXML
    void onActionSearchPartIdOrName(ActionEvent event) {

        String searchPartText = searchPartIdOrName.getText();

        ObservableList<Part> searchPartResults = searchParts(searchPartText);

        addPartToProductTableView.setItems(searchPartResults);
        searchPartIdOrName.setText("");

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
            if (partSearch.getName().contains(searchPartText)) {
                searchedPartResults.add(partSearch);
            }
            else if(String.valueOf(partSearch.getId()).contains(searchPartText)){
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
            alert.close();

        }
        return searchedPartResults;
    }
    /**
     * method to check for accuracy of min and max values
     */
    public boolean checkMinMaxValues() {
        int min = Integer.parseInt(modifyProductMinTxt.getText());
        int max = Integer.parseInt(modifyProductMaxTxt.getText());

        if(min > max) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setContentText("Min must be less than Max. Please try again.");
            alert.showAndWait();
            alert.close();
            return false;
        }
        return true;
    }

    /**
     * method to check stock values are greater than min value and less than max values
     */
    public boolean checkInvValues() {
        int min = Integer.parseInt(modifyProductMinTxt.getText());
        int max = Integer.parseInt(modifyProductMaxTxt.getText());
        int stock = Integer.parseInt(modifyProductInvTxt.getText());

        if(stock > max || stock < min){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setContentText("Inventory must be greater than min and less than max. Please try again.");
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
