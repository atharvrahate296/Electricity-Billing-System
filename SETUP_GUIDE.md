# Project Setup and Details

This guide provides supplementary information and advanced details that are not covered in the main `README.md` file. For basic setup, compilation, and running the application, please refer to `README.md` first.

## MySQL Server Setup

This project requires a running MySQL server to function. If you do not have it installed, follow these steps.

1.  **Download MySQL Community Server:**
    *   Navigate to the official download page: [MySQL Community Downloads](https://dev.mysql.com/downloads/mysql/)
    *   Select your operating system and download the installer.

2.  **Install MySQL:**
    *   Run the installer and follow the on-screen instructions.
    *   During the setup type selection, you can choose **"Server only"** if you do not need GUI tools like MySQL Workbench.
    *   You will be prompted to set a password for the `root` user. **Remember this password**, as you will need it to configure the application's `.env` file.

3.  **Verify Installation:**
    *   After installation, ensure the MySQL server is running. You can typically check this from your system's services panel (e.g., `services.msc` on Windows).

## Managing Dependencies (JAR Files)

The required Java Archive (JAR) files, which contain necessary libraries, are already included in the `lib/` directory of this project. The application is pre-configured to use them, so you do not need to download them separately.

However, if the `lib/` directory is empty or if you need to download a specific version in the future, you can obtain the JARs from the Maven Central Repository:

-   **dotenv-java-2.3.2.jar:**
    *   **Purpose:** Loads database credentials from the `.env` file.
    *   **Download Link:** [https://repo1.maven.org/maven2/io/github/cdimascio/dotenv-java/2.3.2/dotenv-java-2.3.2.jar](https://repo1.maven.org/maven2/io/github/cdimascio/dotenv-java/2.3.2/dotenv-java-2.3.2.jar)

-   **mysql-connector-j-8.0.33.jar:**
    *   **Purpose:** The JDBC driver for MySQL connectivity.
    *   **Download Link:** [https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar](https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar)

After downloading, simply place the JAR files into the `lib/` directory.

## Detailed Database Setup Commands

While the `README.md` file provides instructions to create a database, here are the specific SQL commands you can use after connecting to your MySQL server via the command line (`mysql -u root -p`) or a GUI client like MySQL Workbench.

```sql
-- Create the database if it doesn't already exist.
CREATE DATABASE IF NOT EXISTS electricity_billing_database;

-- Switch to the newly created database to execute subsequent commands in its context.
USE electricity_billing_database;

-- You can verify that the database was created and you are using it with these commands:
SHOW DATABASES; -- Should list 'electricity_billing_database'
SELECT DATABASE(); -- Should return 'electricity_billing_database'
```

The application is designed to automatically create the required tables (`admin`, `user`, `bill`) on its first successful connection to the database.

## Dependencies

The `lib/` directory contains the following essential Java Archive (JAR) files that the project depends on. These are included in the classpath during compilation and runtime.

-   `dotenv-java-2.3.2.jar`: A library to load environment variables from a `.env` file, which is used for securely managing database credentials.
-   `mysql-connector-j-8.0.33.jar`: This is the official MySQL JDBC driver that allows the Java application to communicate with the MySQL database.

## Advanced Troubleshooting

The `README.md` covers common issues, but here are some more specific database-related errors and their solutions.

### "Unknown database 'electricity_billing_database'"

This error means the application connected to the MySQL server successfully, but the specific database it was instructed to use (from the `DB_URL` in your `.env` file) does not exist.

**Solution:**
-   Ensure you have created the database as shown in the "Detailed Database Setup Commands" section above.
-   Double-check for typos in the database name in your `DB_URL` inside the `.env` file.

### "Access denied for user 'root'@'localhost'"

This is a credential error. The username and password provided by the application do not not match a user in your MySQL server with the required permissions.

**Solution:**
-   Verify that the `DB_USER` and `DB_PASSWORD` in your `.env` file are correct for your MySQL instance.
-   Ensure that the user specified in `DB_USER` has sufficient permissions to create and manage tables in the `electricity_billing_database`.

## Application Testing Guide

After you have successfully compiled and run the application, you can follow these steps to test its core functionalities:

1.  **Sign Up:**
    *   Use the "Signup" button to create a new customer account. You will need to provide details like a meter ID, name, address, etc.
    -   You can also create an administrator account through the signup process.

2.  **Login:**
    *   Log in using the credentials (meter ID and password for customers, or username and password for admins) you created during signup.
    *   A successful login will take you to either the customer dashboard or the admin panel (`ElectricityBoard`).

3.  **Administrator Panel (Admin Login):**
    *   **Customer Details:** Verify that you can view the details of all registered customers.
    *   **Calculate Bill:** Choose a customer and generate a bill for a specific month and year.
    *   **Export Bills:** Test the functionality to export bill data.

4.  **Customer Dashboard (Customer Login):**
    *   **View Profile:** Check if your profile information is displayed correctly.
    *   **Pay Bill:** If a bill has been generated for your account by an admin, you should be able to view and pay it.
    *   **View Bill Details:** After paying, verify that the bill status updates accordingly.
