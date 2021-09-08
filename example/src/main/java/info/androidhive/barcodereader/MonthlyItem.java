package info.androidhive.barcodereader;

public class MonthlyItem {
    double amount;
    String monthYear;

    public MonthlyItem(double amount, String monthYear) {
        this.amount = amount;
        this.monthYear = monthYear;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }
}
