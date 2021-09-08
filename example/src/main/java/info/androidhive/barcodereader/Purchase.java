package info.androidhive.barcodereader;

public class Purchase {
    String id, name;
    double amount;
    int quantity;
    String date;

    public Purchase(String id, String date, String name, int quantity, Double amount) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.amount = amount;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
