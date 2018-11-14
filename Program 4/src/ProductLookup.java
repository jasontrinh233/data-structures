/*
 * Quang Trinh
 * cssc0759
 * Program 4
 * 05.05.18
 */
import data_structures.*;
import java.util.Iterator;

public class ProductLookup {
	DictionaryADT<String, StockItem> productTable;
	
    // Constructor.  There is no argument-less constructor, or default size
    public ProductLookup(int maxSize) {
    		productTable = new Hashtable<>(maxSize);
    }
       
    // Adds a new StockItem to the dictionary
    public void addItem(String SKU, StockItem item) {
    		productTable.add(SKU, item);
    }
           
    // Returns the StockItem associated with the given SKU, if it is
    // in the ProductLookup, null if it is not.
    public StockItem getItem(String SKU) {
    		return productTable.getValue(SKU);
    }
       
    // Returns the retail price associated with the given SKU value.
    // -.01 if the item is not in the dictionary
    public float getRetail(String SKU) {
    		if(productTable.contains(SKU))
    			return productTable.getValue(SKU).getRetail();
    		return -0.01f;
    }
    
    // Returns the cost price associated with the given SKU value.
    // -.01 if the item is not in the dictionary
    public float getCost(String SKU) {
		if(productTable.contains(SKU))
			return productTable.getValue(SKU).getCost();
		return -0.01f;
    }
    
    // Returns the description of the item, null if not in the dictionary.
    public String getDescription(String SKU) {
		if(productTable.contains(SKU))
			return productTable.getValue(SKU).getDescription();
		return null;
    }
       
    // Deletes the StockItem associated with the SKU if it is
    // in the ProductLookup.  Returns true if it was found and
    // deleted, otherwise false.  
    public boolean deleteItem(String SKU) {
    		return productTable.delete(SKU);
    }
       
    // Prints a directory of all StockItems with their associated
    // price, in sorted order (ordered by SKU).
    public void printAll() {
    	Iterator<StockItem> productValue = productTable.values();
        while(productValue.hasNext())
            System.out.println(productValue.next());
    }
    
    // Prints a directory of all StockItems from the given vendor, 
    // in sorted order (ordered by SKU).
    public void print(String vendor) {
    	Iterator<String> productKey = productTable.keys();
        while(productKey.hasNext()) {
            String item = productKey.next();
            if (vendor.compareTo(productTable.getValue(item).getVendor()) == 0)
                System.out.println(item);
        }
    }
    
    // An iterator of the SKU keys.
    public Iterator<String> keys() {
    		return productTable.keys();
    }
    
    // An iterator of the StockItem values.    
    public Iterator<StockItem> values() {
    		return productTable.values();
    }
}
