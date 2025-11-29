import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class Database {
    
        private static String URL; 
        private static String USER;
        private static String PASSWORD; 
        
    public static Connection getConnection() throws java.sql.SQLException {
        Connection newConnection = null;
        try {
            // Load credentials exclusively from the .env file in the working directory
            loadCredentialsFromDotEnv();

            // Validate we have required values
            if (URL == null || URL.isEmpty() || USER == null || USER.isEmpty() || PASSWORD == null || PASSWORD.isEmpty()) {
                System.err.println("ERROR: Missing DB configuration. Please populate .env with DB_URL, DB_USER and DB_PASSWORD.");
                JOptionPane.showMessageDialog(null, "ERROR: Missing DB configuration. Please populate .env with DB_URL, DB_USER and DB_PASSWORD.", "Database Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            // System.out.println(URL + USER + PASSWORD);
            // Try to load MySQL JDBC driver
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.err.println("MySQL JDBC Driver not found. Please ensure the MySQL JDBC driver is on the runtime classpath.");
                System.err.println("Error: " + e.getMessage());
                JOptionPane.showMessageDialog(null, "MySQL JDBC Driver not found. Please ensure the MySQL JDBC driver is on the runtime classpath.", "Database Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            // Attempt connection
            newConnection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to MySQL Database Server!");
            createTables(newConnection);
        } catch (Exception e) {
            System.err.println("Unexpected error while initializing database connection: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Unexpected error while initializing database connection: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            if ("true".equalsIgnoreCase(System.getenv("DEBUG"))) e.printStackTrace();
            newConnection = null; // Ensure connection is null on error
            throw new java.sql.SQLException("Failed to establish database connection.", e);
        }
        return newConnection;
    }

    private static void createTables(Connection connection) {
        if (connection == null) return; 
        
        try (Statement statement = connection.createStatement()) {
            // Create admin table
            statement.execute("CREATE TABLE IF NOT EXISTS admin (" +
                    "EMP_Code VARCHAR(255) PRIMARY KEY," +
                    "number VARCHAR(255)," +
                    "name VARCHAR(255)," +
                    "username VARCHAR(255) UNIQUE," +
                    "password VARCHAR(255)," +
                    "admin_id VARCHAR(255)" +
                    ")");
                    
                    // Create user table (meter_id stored as INT to match application model)
                    statement.execute("CREATE TABLE IF NOT EXISTS user (" +
                    "meter_id INT PRIMARY KEY," +
                    "username VARCHAR(255) UNIQUE," +
                    "name VARCHAR(255)," +
                    "address VARCHAR(255)," +
                    "rmn VARCHAR(255)," +
                    "password VARCHAR(255)" +
                    ")");
                    
                    // Create bill table (meter_id as INT referencing user.meter_id)
                    statement.execute("CREATE TABLE IF NOT EXISTS bill (" +
                    "bill_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "meter_id INT," +
                    "month VARCHAR(255)," +
                    "year VARCHAR(255)," +
                    "units INT," +
                    "amount DOUBLE," +
                    "status VARCHAR(255) DEFAULT 'Pending'," +
                    "FOREIGN KEY (meter_id) REFERENCES user(meter_id) ON DELETE CASCADE" +
                    ")");
            // System.out.println("Admin table created/verified");
            // System.out.println("User table created/verified");
            // System.out.println("Bill table created/verified");
            System.out.println("All Tables created/verified");
            
        } catch (Exception e) {
            System.err.println("Error creating tables: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Load credentials from (in order): System env, System properties, .env file in working dir.
     * This allows setting env vars after editing .env or passing -Ddb.user=... when launching.
     */
    /**
     * Load credentials exclusively from a local .env file in the working directory.
     * Required keys: DB_URL, DB_USER, DB_PASSWORD
     */
    private static void loadCredentialsFromDotEnv() {
        File envFile = new File(".env");
        if (!envFile.exists()) {
            System.err.println("ERROR: .env file not found!! Please create a .env file with DB_URL, DB_USER and DB_PASSWORD.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(envFile))) {
            String line;
    
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split("=", 2);
                if (parts.length != 2) continue;
                String k = parts[0].trim();
                String v = parts[1].trim();
                if (k.equals("DB_URL")) URL = v;
                if (k.equals("DB_USER")) USER = v;
                if (k.equals("DB_PASSWORD")) PASSWORD = v;
            }
        } catch (Exception e) {
            System.err.println("ERROR: failed to read .env file: " + e.getMessage());
        }

        // Print masked debug info (do not print password value)
        // System.out.println("\t--- Initiatializing Server ---\nDB_URL :" + (URL == null ? "[NOT SET]" : URL) +
                // "\nDB_USER : " + (USER == null ? "[NOT SET]" : USER) + "\nDB_PASSWORD : " + (PASSWORD == null ? "[NOT SET]" : "*********"));
    }
}