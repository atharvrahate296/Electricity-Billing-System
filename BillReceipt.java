import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Generates and manages bill payment receipts.
 * Supports both TXT (plain text) and formatted file export.
 */
public class BillReceipt {
    
    private int billId;
    private int meterNumber;
    private String customerName;
    private String address;
    private String rmn;
    private String month;
    private String year;
    private int units;
    private double amount;
    private String paymentMethod;
    private Date paymentDate;
    private String transactionId;
    
    public BillReceipt(int billId, int meterNumber, String customerName, String address, 
                       String rmn, String month, String year, int units, double amount) {
        this.billId = billId;
        this.meterNumber = meterNumber;
        this.customerName = customerName;
        this.address = address;
        this.rmn = rmn;
        this.month = month;
        this.year = year;
        this.units = units;
        this.amount = amount;
        this.paymentDate = new Date();
        this.transactionId = generateTransactionId();
        this.paymentMethod = "Direct Payment";
    }
    
    /**
     * Generate a unique transaction ID
     */
    private String generateTransactionId() {
        return "ELEC-" + System.currentTimeMillis();
    }
    
    /**
     * Generate formatted receipt content as String
     */
    public String generateReceiptContent() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        SimpleDateFormat dateFormatOnly = new SimpleDateFormat("dd/MM/yyyy");
        
        StringBuilder receipt = new StringBuilder();
        
        // Header
        receipt.append("════════════════════════════════════════════\n");
        receipt.append("        ELECTRICITY BILLING SYSTEM\n");
        receipt.append("           PAYMENT RECEIPT\n");
        receipt.append("════════════════════════════════════════════\n\n");
        
        // Receipt Number and Date
        receipt.append("Receipt Number: ").append(transactionId).append("\n");
        receipt.append("Payment Date: ").append(dateFormat.format(paymentDate)).append("\n\n");
        
        // Customer Details
        receipt.append("────────────────────────────────────────────\n");
        receipt.append("CUSTOMER INFORMATION\n");
        receipt.append("────────────────────────────────────────────\n");
        receipt.append(String.format("%-20s: %s\n", "Customer Name", customerName));
        receipt.append(String.format("%-20s: %d\n", "Meter Number", meterNumber));
        receipt.append(String.format("%-20s: %s\n", "Address", address));
        receipt.append(String.format("%-20s: %s\n", "RMN", rmn));
        receipt.append("\n");
        
        // Bill Details
        receipt.append("────────────────────────────────────────────\n");
        receipt.append("BILL DETAILS\n");
        receipt.append("────────────────────────────────────────────\n");
        receipt.append(String.format("%-20s: %s\n", "Billing Month", month));
        receipt.append(String.format("%-20s: %s\n", "Billing Year", year));
        receipt.append(String.format("%-20s: %d units\n", "Consumption", units));
        receipt.append(String.format("%-20s: %.2f Rate (per unit)\n", "Rate", calculateRate()));
        receipt.append("\n");
        
        // Amount Details
        receipt.append("────────────────────────────────────────────\n");
        receipt.append("PAYMENT DETAILS\n");
        receipt.append("────────────────────────────────────────────\n");
        receipt.append(String.format("%-20s: Rs. %.2f\n", "Amount Due", amount));
        receipt.append(String.format("%-20s: Rs. 0.00\n", "Discount Applied"));
        receipt.append(String.format("%-20s: Rs. %.2f\n", "Total Paid", amount));
        receipt.append(String.format("%-20s: %s\n", "Payment Method", paymentMethod));
        receipt.append("\n");
        
        // Transaction Details
        receipt.append("────────────────────────────────────────────\n");
        receipt.append("TRANSACTION INFORMATION\n");
        receipt.append("────────────────────────────────────────────\n");
        receipt.append(String.format("%-20s: %s\n", "Transaction ID", transactionId));
        receipt.append(String.format("%-20s: %s\n", "Bill ID", billId));
        receipt.append(String.format("%-20s: %s\n", "Status", "PAID"));
        receipt.append("\n");
        
        // Footer
        receipt.append("════════════════════════════════════════════\n");
        receipt.append("Thank you for paying your electricity bill!\n");
        receipt.append("This is an auto-generated receipt.\n");
        receipt.append("Please keep this for your records.\n");
        receipt.append("════════════════════════════════════════════\n");
        
        return receipt.toString();
    }
    
    /**
     * Calculate rate per unit based on consumption
     */
    private double calculateRate() {
        if (units <= 0) return 0;
        return Math.round((amount / units) * 100.0) / 100.0;
    }
    
    /**
     * Export receipt to a text file
     */
    public boolean exportToFile(File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(generateReceiptContent());
            return true;
        } catch (IOException e) {
            System.err.println("Error writing receipt to file: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Export receipt with metadata to a detailed text file
     */
    public boolean exportDetailedReceipt(File file) {
        try (FileWriter writer = new FileWriter(file)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
            
            // Include metadata
            writer.write("FILE_TYPE=ELECTRICITY_BILL_RECEIPT\n");
            writer.write("GENERATED_DATE=" + dateFormat.format(new Date()) + "\n");
            writer.write("VERSION=1.0\n\n");
            
            // Write main receipt
            writer.write(generateReceiptContent());
            
            return true;
        } catch (IOException e) {
            System.err.println("Error writing detailed receipt: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Export receipt as CSV format
     */
    public boolean exportAsCSV(File file) {
        try (FileWriter writer = new FileWriter(file)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
            
            // CSV Header
            writer.write("Receipt Number,Payment Date,Customer Name,Meter Number,Address,RMN,");
            writer.write("Billing Month,Billing Year,Consumption (units),Rate Per Unit,Amount Paid,");
            writer.write("Payment Method,Bill ID,Status\n");
            
            // CSV Data
            writer.write(String.format("%s,%s,%s,%d,%s,%s,%s,%s,%d,%.2f,%.2f,%s,%d,PAID\n",
                transactionId,
                dateFormat.format(paymentDate),
                customerName,
                meterNumber,
                address,
                rmn,
                month,
                year,
                units,
                calculateRate(),
                amount,
                paymentMethod,
                billId
            ));
            
            return true;
        } catch (IOException e) {
            System.err.println("Error writing CSV receipt: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Getters
    public int getBillId() { return billId; }
    public int getMeterNumber() { return meterNumber; }
    public String getCustomerName() { return customerName; }
    public String getAddress() { return address; }
    public String getRmn() { return rmn; }
    public String getMonth() { return month; }
    public String getYear() { return year; }
    public int getUnits() { return units; }
    public double getAmount() { return amount; }
    public String getTransactionId() { return transactionId; }
    public Date getPaymentDate() { return paymentDate; }
    public String getPaymentMethod() { return paymentMethod; }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
