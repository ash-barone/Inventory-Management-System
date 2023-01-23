package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();

    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     *
     * @param newPart the newPart to add to the allParts ObservableList
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     *
     * @param newProduct the newProduct to add tot he newProducts ObservableList
     */
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    /**
     *
     * @param partId the partId used to check through the allParts list for matching id
     * @return tempPart as part with matching id
     */
    public static Part lookupPart(int partId){

        Part tempPart = null;

        for(Part part : allParts){
            if(part.getId() == partId){
                tempPart = part;
            }
        }
        return tempPart;
    }

    /**
     *
     * @param productId the productId used to check through allProducts list for matching id
     * @return tempProduct as product with matching id
     */
    public static Product lookupProduct(int productId){

        Product tempProduct = null;

        for(Product product: allProducts){
            if(product.getId() == productId){
                tempProduct = product;
            }
        }
        return tempProduct;
    }

    /**
     *
     * @param partName the partName used to check through allParts list for matching name
     * @return tempPart as part with matching name
     */
    public static ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> tempPart = FXCollections.observableArrayList();

        for(Part part : allParts){
            if (part.getName().equals(partName)) {
                tempPart.add(part);
            }
        }
        return tempPart;
    }

    /**
     *
     * @param productName the productName used to check through allProducts list for matching name
     * @return tempProduct as part with matching name
     */
    public static ObservableList<Product> lookupProduct(String productName){

        ObservableList<Product> tempProduct = FXCollections.observableArrayList();

        for(Product product : allProducts){
            if(product.getName().equals(productName)){
                tempProduct.add(product);
            }
        }
        return tempProduct;
    }

    /**
     *
     * @param index
     * @param selectedPart the index and part to be updated in allParts list
     */
    public static void updatePart(int index, Part selectedPart){
        allParts.set(index, selectedPart);
    }

    /**
     *
     * @param index
     * @param newProduct the index and product to be updated in allProducts list
     */
    public static void updateProduct(int index, Product newProduct){
        allProducts.set(index, newProduct);
    }

    /**
     *
     * param selectedPart the selectedPart to be deleted from allParts list
     * @return
     */
    public static boolean deletePart(Part selectedPart){
        if(allParts.contains(selectedPart)){
            allParts.remove(selectedPart);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     *
     * @param selectedProduct the selectedProduct to be deleted from allProducts list
     * @return
     */
    public static boolean deleteProduct(Product selectedProduct){
        if(allProducts.contains(selectedProduct)){
            allProducts.remove(selectedProduct);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     *
     * @return allParts list
     */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }

    /**
     *
     * @return allProducts list
     */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }
}
