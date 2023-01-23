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
 * controller for modify part screen
 */
public class ModifyPart implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private Label machineIdOrCompanyNameLbl;

    @FXML
    private TextField machineIdOrCompanyNameTxt;

    @FXML
    private Button modifyPartCancelBtn;

    @FXML
    private TextField modifyPartIdTxt;

    @FXML
    private RadioButton modifyPartInHouseRBtn;

    @FXML
    private TextField modifyPartInvTxt;

    @FXML
    private TextField modifyPartMaxTxt;

    @FXML
    private TextField modifyPartMinTxt;

    @FXML
    private TextField modifyPartNameTxt;

    @FXML
    private RadioButton modifyPartOutsourcedRBtn;

    @FXML
    private TextField modifyPartPriceTxt;

    @FXML
    private Button modifyPartSaveBtn;

    @FXML
    private ToggleGroup partType;

    private Part tempPart;

    private int index;

    /**
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
        if (result.get() == ButtonType.OK) {
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
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();*/
    }

    /**
     * @param event the event of clicking the save button to same the modified part
     * @throws IOException
     */
    @FXML
    void onActionSaveModifiedPart(ActionEvent event) throws IOException {

        /**
         * was generating new id for each modified part instead of keeping originally assigned id
         * commented out old code and used getText() instead to maintain original id
         */
        //below just gets the sent id from selected part on main screen
        int id = Integer.parseInt(modifyPartIdTxt.getText());

        /*
        int id = 0;
        for(Part part : Inventory.getAllParts()){
            if(part.getId() > id) {
                id = part.getId();
            }
        }
        id = id + 1;*/
        try {
            String name = modifyPartNameTxt.getText();
            int stock = Integer.parseInt(modifyPartInvTxt.getText());
            double price = Double.parseDouble(modifyPartPriceTxt.getText());
            int max = Integer.parseInt(modifyPartMaxTxt.getText());
            int min = Integer.parseInt(modifyPartMinTxt.getText());
            int machineId;
            String companyName;

            //checkMinMaxValues();
            //checkInvValues();

            //set these bools
            boolean minMaxValuesCheck = checkMinMaxValues();
            boolean invValuesCheck = checkInvValues();

            //check if radio selected or nah
            if(minMaxValuesCheck == true && invValuesCheck == true) {
                if (modifyPartInHouseRBtn.isSelected()) {

                    machineId = Integer.parseInt(machineIdOrCompanyNameTxt.getText());
                    //remove old entry from list
                    Inventory.deletePart(tempPart);

                    /**
                     * original code was removing part then adding part as a new part which changed the index
                     * new code below create new part object then updates instead of adding new object
                     * thus maintaining original index
                     */
                    Part part = new InHouse(id, name, price, stock, min, max, machineId);
                    Inventory.updatePart(index, part);

                    //Inventory.addPart(new InHouse(id, name, price, stock, min, max, machineId));
                } else if (modifyPartOutsourcedRBtn.isSelected()) {
                    companyName = machineIdOrCompanyNameTxt.getText();
                    //remove old entry from list
                    Inventory.deletePart(tempPart);

                    Part part = new Outsourced(id, name, price, stock, min, max, companyName);
                    Inventory.updatePart(index, part);

                }
                else {
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
         *
         * @param event the event of selecting the inhouse radio button
         */
        @FXML
        void onActionSelectInHouse (ActionEvent event){
            machineIdOrCompanyNameLbl.setText("Machine ID");
        }

        /**
         *
         * @param event the event of selecting the outsourced radio button
         */
        @FXML
        void onActionSelectOutsourced (ActionEvent event){
            machineIdOrCompanyNameLbl.setText("Company Name");
        }

        /**
         *
         * @param part
         * sends the part selected from main screen to be modified
         */
        public void sendPart (Part part) {

            //set temp part to be passed for removal upon saving
            tempPart = part;

            /**
             * get index for part so that modified part can maintain its same index after modified
             */
            index = Inventory.getAllParts().indexOf(tempPart);
            System.out.println(index + " is the index");

            //fill in all your labels
            modifyPartIdTxt.setText(String.valueOf(part.getId()));
            modifyPartNameTxt.setText(part.getName());
            modifyPartInvTxt.setText(String.valueOf(part.getStock()));
            modifyPartPriceTxt.setText(String.valueOf(part.getPrice()));
            modifyPartMaxTxt.setText(String.valueOf(part.getMax()));
            modifyPartMinTxt.setText(String.valueOf(part.getMin()));


            //check if radio selected or nah

            if (part instanceof InHouse) {

                modifyPartInHouseRBtn.setSelected(true);
                machineIdOrCompanyNameLbl.setText("Machine ID");
                machineIdOrCompanyNameTxt.setText(String.valueOf(((InHouse) part).getMachineId()));

            } else if (part instanceof Outsourced) {

                modifyPartOutsourcedRBtn.setSelected(true);
                machineIdOrCompanyNameLbl.setText("Company Name");
                machineIdOrCompanyNameTxt.setText(String.valueOf(((Outsourced) part).getCompanyName()));

            }
        }
        /**
         * method to check for accuracy of min and max values
         */
        public boolean checkMinMaxValues() {
            int min = Integer.parseInt(modifyPartMinTxt.getText());
            int max = Integer.parseInt(modifyPartMaxTxt.getText());

            if(min > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setContentText("Min must be less than Max. Please try again.");
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
            int min = Integer.parseInt(modifyPartMinTxt.getText());
            int max = Integer.parseInt(modifyPartMaxTxt.getText());
            int stock = Integer.parseInt(modifyPartInvTxt.getText());

            System.out.println(min + " " + max + " " +stock);
            if(stock > max || stock < min){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setContentText("Inventory must be greater than min and less than max. Please try again.");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    alert.close();
                }
                return false;
            }
            return true;
        }
    }

