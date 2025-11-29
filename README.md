# Electricity Billing System

## Overview

The Electricity Billing System is a comprehensive Java-based desktop application developed to streamline and automate electricity billing management. This user-friendly application, built with Java Swing for the graphical user interface, provides a robust platform for both customers and administrators to manage billing, payments, and user information efficiently.

The system connects to a MySQL database to handle all data storage and retrieval operations, ensuring data integrity and persistence. For secure and flexible database configuration, it utilizes a `.env` file to manage credentials, keeping sensitive information separate from the source code.

## Features

- **User Authentication:** Secure login and signup functionalities for both customers and administrators.
- **Customer Management:** Administrators can add, update, and view detailed customer information.
- **Bill Calculation & Management:** Automated calculation of electricity bills based on monthly consumption. Administrators can view, manage, and export bills.
- **Payment Processing:** Customers can conveniently pay their bills through the application and view their payment history.
- **Profile Management:** Users can view and update their profile information.
- **Database Integration:** All application data is stored and managed in a MySQL database. The application automatically creates the necessary tables on its first run if they don't exist.

## Project Structure

The project follows a simple, flat structure, making it easy to navigate and understand.

```
.
├── .env                  # Environment variables for database connection (DB_URL, DB_USER, DB_PASSWORD)
├── .gitignore            # Specifies intentionally untracked files to ignore
├── Bill.java             # Represents the Bill entity and its associated logic
├── BillDetails.java      # Swing-based UI for viewing detailed bill information
├── CalculateBill.java    # Handles the logic for calculating electricity bills
├── Customer.java         # Represents the Customer entity and their data
├── CustomerDetails.java  # UI for displaying customer details
├── Database.java         # Manages the connection to the MySQL database and table creation
├── ElectricityBoard.java # The main dashboard or admin panel for administrators
├── ExportBills.java      # Contains functionality for exporting bill data
├── Login.java            # UI and logic for user authentication
├── App.java              # The main entry point of the application
├── ManageBills.java      # UI for managing bills
├── PayBill.java          # UI and logic for processing bill payments
├── Profile.java          # User profile management UI
├── Signup.java           # UI and logic for new user registration
├── UpdateInformation.java# Logic to handle updates to user information
├── ViewInformation.java  # UI for viewing user information
├── icon/                 # Contains all the icons and images used in the application
├── lib/                  # Contains the external libraries (JAR files) required for the project, such as the MySQL JDBC driver and dotenv for Java
├── .vscode/              # Contains VS Code-specific settings, including launch configurations
├── README.md             # This file
└── SETUP_GUIDE.md        # Provides additional setup instructions
```

## Prerequisites

- Java JDK 8 or higher
- MySQL Server

## Setup and Installation

1.  **Clone the repository:**
    ```bash
    git clone <repository-url>
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

## Contributing

Contributions are welcome! If you'd like to contribute, please follow these steps:

1.  Fork the repository.
2.  Create a new branch for your feature or bugfix (`git checkout -b feature/your-feature-name`).
3.  Commit your changes with clear and descriptive messages.
4.  Push your changes to your fork.
5.  Submit a pull request to the main repository.

## License

This project is for educational purposes and is not licensed for commercial use.