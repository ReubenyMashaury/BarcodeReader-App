package info.androidhive.barcodereader;

public class MonthReportItem {
    String monthYear;
    double sales, purchases, profit;

    public MonthReportItem(String monthYear, double sales, double purchases, double profit) {
        this.monthYear = monthYear;
        this.sales = sales;
        this.purchases = purchases;
        this.profit = profit;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public double getSales() {
        return sales;
    }

    public void setSales(double sales) {
        this.sales = sales;
    }

    public double getPurchases() {
        return purchases;
    }

    public void setPurchases(double purchases) {
        this.purchases = purchases;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }
}
