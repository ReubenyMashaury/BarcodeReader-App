package info.androidhive.barcodereader;

public class Stock {
    int id, quantity, product_id;
    String barcode;

    public Stock(int id, int quantity, int product_id, String barcode) {
        this.id = id;
        this.quantity = quantity;
        this.product_id = product_id;
        this.barcode = barcode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
