package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * controller for add part screen
 */
public class    AddPart implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private Button addPartCancelToMainBtn;

    @FXML
    private TextField addPartIdTxt;

    @FXML
    private RadioButton addPartInHouseRBtn;

    @FXML
    private TextField addPartInvTxt;

    @FXML
    private TextField addPartMaxTxt;

    @FXML
    private TextField addPartMinTxt;

    @FXML
    private TextField addPartNameTxt;

    @FXML
    private RadioButton addPartOutsourcedRBtn;

    @FXML
    private TextField addPartPriceCostTxt;

    @FXML
    private Button addPartSaveBtn;

    @FXML
    private Label machineIdOrCompanyNameLbl;

    @FXML
    private TextField machineIdOrCompanyNameTxt;

    @FXML
    private ToggleGroup partType;

    /**
     * @param event the event of clicking cancel to return to the main screen
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

       /* Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();*/
    }

    /**
     * @param event the event of clicking save to create the new part
     * @throws IOException
     */
    @FXML
    void onActionSaveNewPart(ActionEvent event) throws IOException {

        //initialize id to 0, find highest id, then add 1 to that number
        int id = 0;
        for (Part part : Inventory.getAllParts()) {
            if (part.getId() > id) {
                id = part.getId();
            }
        }
        id = id + 1;
        try{
            String name = addPartNameTxt.getText();
            int stock = Integer.parseInt(addPartInvTxt.getText());
            double price = Double.parseDouble(addPartPriceCostTxt.getText());
            int max = Integer.parseInt(addPartMaxTxt.getText());
            int min = Integer.parseInt(addPartMinTxt.getText());
            int machineId;
            String companyName;

            //set bool for each check method
            boolean minMaxValuesCheck = checkMinMaxValues();
            boolean invValuesCheck = checkInvValues();

            //check if radio selected or nah
            if(minMaxValuesCheck == true && invValuesCheck == true) {
                if (addPartInHouseRBtn.isSelected()) {
                    machineId = 0;
                    //System.out.println("InHouse");
                    machineId = Integer.parseInt(machineIdOrCompanyNameTxt.getText());
                    Inventory.addPart(new InHouse(id, name, price, stock, min, max, machineId));

                } else if (addPartOutsourcedRBtn.isSelected()) {
                    //String companyName = "null";
                    companyName = machineIdOrCompanyNameTxt.getText();
                    Inventory.addPart(new Outsourced(id, name, price, stock, min, max, companyName));

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("No part type selected.");
                    alert.setContentText("Please select a part type.");

                    alert.showAndWait();
                    return;
                }
            }
            else {
                Alert alert = new Alert((Alert.AlertType.ERROR));
                alert.setTitle("Error");
                alert.setContentText("Please ensure all values are correct and not blank.");
                alert.showAndWait();
                return;
            }


            //want to close window when we hit save also
            //use the change scene stuff
            //add throw clause in FXML event handler declaration
            Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Main Menu");
            stage.setScene(scene);
            stage.show();

    } catch (NumberFormatException e) {
        e.printStackTrace();
        Alert alert = new Alert((Alert.AlertType.ERROR));
        alert.setTitle("Error");
        alert.setContentText("Please ensure all values are correct and not blank.");
        alert.showAndWait();
        return;

    }
    }

    /**
     * @param event the event of clicking on the radio button choice for inhouse
     */
    @FXML
    void onActionSelectInHouse(ActionEvent event) {
        machineIdOrCompanyNameLbl.setText("Machine ID");
    }

    /**
     * @param event the event of clicking on the radio button choice for outsourced
     */
    @FXML
    void onActionSelectOutsourced(ActionEvent event) {
        machineIdOrCompanyNameLbl.setText("Company Name");
    }

    /**
     * method to check for accuracy of min and max values
     */
    public boolean checkMinMaxValues() {
        int min = Integer.parseInt(addPartMinTxt.getText());
        int max = Integer.parseInt(addPartMaxTxt.getText());

        if(min > max) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setContentText("Min must be less than Max. Please try again.");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    /**
     * method to check stock values are greater than min value and less than max values
     */
    public boolean checkInvValues() {
        int min = Integer.parseInt(addPartMinTxt.getText());
        int max = Integer.parseInt(addPartMaxTxt.getText());
        int stock = Integer.parseInt(addPartInvTxt.getText());

        System.out.println(min + " " + max + " " +stock);
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