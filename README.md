# Electricity Billing System

## Overview

The Electricity Billing System is a comprehensive Java-based desktop application developed to streamline and automate electricity billing management. This user-friendly application, built with Java Swing for the graphical user interface, provides a robust platform for both customers and administrators to manage billing, payments, and user information efficiently.

The system connects to a MySQL database to handle all data storage and retrieval operations, ensuring data integrity and persistence. For secure and flexible database configuration, it utilizes a `.env` file to manage credentials, keeping sensitive information separate from the source code.

## Features

- **User Authentication:** Secure login and signup functionalities for both customers and administrators.
- **Customer Management:** Administrators can add, update, and view detailed customer information.
- **Bill Calculation & Management:** Automated calculation of electricity bills based on monthly consumption. Administrators can view, manage, and export bills.
- **Payment Processing:** Customers can conveniently pay their bills through the application and view their payment history.
- **Receipt Download:** Customers and admins can download professional bill payment receipts in TXT or CSV format with unique transaction IDs.
- **Profile Management:** Users can view and update their profile information.
- **Database Integration:** All application data is stored and managed in a MySQL database. The application automatically creates the necessary tables on its first run if they don't exist.

## Project Structure

The project follows a simple, flat structure, making it easy to navigate and understand.

```
.
├── .env                  # Environment variables for database connection
├── .gitignore            # Git ignore file
├── App.java              # Main entry point of the application
├── Bill.java             # Bill entity and logic
├── BillDetails.java      # UI for viewing detailed bill information
├── BillReceipt.java      # Receipt generation and export (NEW)
├── Customer.java         # Customer entity and data
├── CustomerDetails.java  # UI for displaying customer details
├── Database.java         # Database connection and table management
├── ExportBills.java      # Bill data export functionality
├── ForgotPassword.java   # Password recovery functionality
├── GenerateBill.java     # Bill generation logic
├── Login.java            # User authentication UI
├── ManageBills.java      # UI for managing bills (enhanced with receipt download)
├── PasswordHasher.java   # Secure password hashing using bcrypt
├── PayBill.java          # Bill payment processing (enhanced with receipt download)
├── Profile.java          # User profile management UI
├── Signup.java           # New user registration UI
├── UpdateInformation.java# User information update logic
├── ViewInformation.java  # User information viewing UI
├── icon/                 # Icons and images used in the application
├── lib/                  # External libraries (MySQL JDBC driver, dotenv)
├── target/               # Compiled class files
├── README.md             # This file
├── SETUP_GUIDE.md        # Additional setup instructions
└── init_queries.sql      # Database initialization queries
```

## Bill Payment Receipt Download Feature

The system now includes a comprehensive receipt download feature for both customers and administrators:

### For Customers:
- **Download Receipt:** After paying a bill, customers can download a professional receipt
- **File Formats:** Choose between TXT (formatted) or CSV (spreadsheet) format
- **Automatic Button:** "Download Receipt" button enables after successful payment
- **Content Includes:** Customer details, bill information, amount paid, unique transaction ID, and payment timestamp

### For Administrators:
- **Download Any Receipt:** Admins can download receipts for any bill from the Manage Bills section
- **Audit Trail:** Useful for payment verification and customer support
- **Same Formats:** TXT and CSV export options available

### Receipt Contents:
```
- Unique Transaction ID (ELEC-{timestamp})
- Payment Date & Time
- Customer Name, Meter Number, Address, RMN
- Bill Month, Year, Units Consumed, Rate per Unit
- Amount Paid, Payment Status (PAID)
```

### Technical Details:
- No database schema changes required
- No external dependencies added
- Uses existing Java Swing components
- Efficient database queries with join operations
- Comprehensive error handling with user-friendly messages

## Prerequisites

- Java JDK 8 or higher
- MySQL Server

## Setup and Installation

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/atharvrahate296/Electricity-Billing-System
    cd Electricity_Billing_System
    ```

2.  **Set up the database:**
    - Make sure you have a MySQL server running.
    - Create a new database, for example, `electricity_billing_database`.
    - Create a `.env` file in the root of the project directory.
    - Add your database configuration to the `.env` file. For example:
      ```
      DB_URL=jdbc:mysql://localhost:3306/electricity_billing_database
      DB_USER=your_username
      DB_PASSWORD=your_password
      ```

## How to Compile

Open a terminal in the project's root directory and run the following commands:

```bash
# First, create the 'target/classes' directory to store the compiled files.
# The -p flag ensures that the command doesn't fail if the directory already exists.
mkdir -p target/classes

# Then, compile all Java source files.
# The -d flag specifies the output directory for the .class files.
# The -cp "lib/*" flag adds all JAR files from the 'lib' directory to the classpath.
javac -d target/classes *.java -cp "lib/*"
```

## How to Run

After compiling the project, run the following command from the root directory:

```bash
# The -cp flag sets the classpath. We need to include both the directory with our compiled classes ('target/classes')
# and the directory with our library JARs ('lib/*').
# The classpath separator for Windows is ';' and for Linux/macOS is ':'.
# For cross-platform compatibility, you can use the appropriate separator for your OS.
# For Windows:
java -cp "target/classes;lib/*" App

# For Linux/macOS:
java -cp "target/classes:lib/*" App
```

Alternatively, if you are using Visual Studio Code with the "Java Extension Pack", you can simply open the `App.java` file and press `F5` to run the application. The included `.vscode/launch.json` is pre-configured for this.

## Troubleshooting

- **Database Connection Errors:** If you encounter issues connecting to the database, double-check your credentials in the `.env` file and ensure your MySQL server is running.
- **`ClassNotFoundException`:** This error usually means that the classpath is not set correctly. Make sure you are including both the `target/classes` directory and the `lib` directory in your `-cp` argument when running the application.
- **GUI Issues:** Ensure your Java installation includes support for Swing/AWT. OpenJDK distributions should work fine.
- **Receipt Download Not Working:** Ensure BillReceipt.java is compiled. If the Download Receipt button is disabled, make sure a bill payment was successful.

## Testing Receipt Download Feature

To test the receipt download functionality:

1. **Compile & Run:** Use the commands above to compile and run the application
2. **As Customer:**
   - Login with customer credentials
   - Navigate to Actions → Pay Bill
   - Select a pending bill and click "Pay Now"
   - Enter your password to confirm payment
   - After success, click "Download Receipt"
   - Choose TXT or CSV format and save location
   - Verify receipt file contains all bill details and unique transaction ID

3. **As Admin:**
   - Login with admin credentials
   - Navigate to Bill → Manage Bills
   - Select any bill from the table
   - Click "Download Receipt"
   - Choose format and save location
   - Verify receipt generation works for both paid and pending bills

## Contributing

Contributions are welcome! If you'd like to contribute, please follow these steps:

1.  Fork the repository.
2.  Create a new branch for your feature or bugfix (`git checkout -b feature/your-feature-name`).
3.  Commit your changes with clear and descriptive messages.
4.  Push your changes to your fork.
5.  Submit a pull request to the main repository.

## License

This project is for educational purposes and is not licensed for commercial use.