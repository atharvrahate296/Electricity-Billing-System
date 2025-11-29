import java.io.Serializable;

public class Customer implements Serializable {
    private int meterNumber;
    private String name;
    private String address;
    private String rmn;
    private String password;
    private String username;


    public Customer(int meterNumber, String name, String address, String rmn, String password, String username) {
        this.meterNumber = meterNumber;
        this.name = name;
        this.address = address;
        this.rmn = rmn;
        this.password = password;
        this.username = username;
    }

    public int getMeterNumber() { return meterNumber; }
    public void setMeterNumber(int meterNumber) { this.meterNumber = meterNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getRmn() { return rmn; }
    public void setRmn(String rmn) { this.rmn = rmn; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }


    @Override
    public String toString() {
        return meterNumber + " - " + name;
    }
}