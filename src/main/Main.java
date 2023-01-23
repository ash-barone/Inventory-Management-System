package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Product;

/**
 * opens main screen when run<br>
 * FUTURE ENHANCEMENT<br>
 * <p>A future enhance that I would include for this application is the ability to connect to a database that could be synced between multiple devices.
 * This would allow for remote updates to parts and products lists, so that people who may not have access to the device on which the application is available
 * could easily add parts and products. A database would also allow for cleaner data storage by implementing constraints and structure for each object created
 * within the application. <br></p>
 * <p>Another enhancement could be the ability to search products by their associated parts via the main screen of the application. This would allow for quicker access
 * to product details without having to change screens making it faster to move between different product details. </p>
 */
public class Main extends Application {
    @Override
    public void start(Stage mainStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        mainStage.setTitle("Main Stage");
        mainStage.setScene(new Scene(root));
        mainStage.show();
    }


    /**
     *
     * @param args
     *
     * included test data to ensure application works properly
     */
    public static void main(String args[]){

        //test parts
        InHouse inhouse1 = new InHouse(1, "Cord", 5.99, 100, 20, 200, 0001);
        InHouse inhouse2 = new InHouse(2, "Mouse", 12.99, 50, 10, 60, 0002);
        InHouse inhouse3 = new InHouse(3, "Keyboard", 25.99, 50, 10, 100, 0003);
        Outsourced outsourced1 = new Outsourced(4, "Monitor", 215.99, 25, 5, 40, "Gary's Electronics");
        Outsourced outsourced2 = new Outsourced(5, "Speaker", 59.99, 30, 6, 50, "Gary's Electronics");

        //add test parts to allParts
        Inventory.addPart(inhouse1);
        Inventory.addPart(inhouse2);
        Inventory.addPart(inhouse3);
        Inventory.addPart(outsourced1);
        Inventory.addPart(outsourced2);

        //test products
        Product product1 = new Product(101, "Desktop", 1099.00, 10, 2, 20);
        Product product2 = new Product(102, "Laptop", 599.99, 15, 5, 30);

        //add test products to allProducts
        Inventory.addProduct(product1);
        Inventory.addProduct(product2);

        //add associated parts to each product
        product1.addAssociatedPart(inhouse1);
        product1.addAssociatedPart(inhouse2);
        product1.addAssociatedPart(inhouse3);
        product1.addAssociatedPart(outsourced1);

        product2.addAssociatedPart(inhouse1);

        launch(args);
    }
}
