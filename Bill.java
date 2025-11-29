
public class Bill {
    private int billId;
    private int meterNumber;
    private int units;
    private double amount;
    private String month;
    private String year;
    private String status;

    public Bill(int meterNumber, int units, String month, String year) {
        this.meterNumber = meterNumber;
        this.units = units;
        this.month = month;
        this.year = year;
    }

    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }
    public int getMeterNumber() { return meterNumber; }
    public int getUnits() { return units; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }
    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
