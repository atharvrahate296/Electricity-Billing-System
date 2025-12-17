-- create database electricity_billing_database;
use electricity_billing_database;

show tables;
select * from admin;
select * from user;
select * from bill;
desc bill;

-- Creating the tables
CREATE TABLE admin (
    EMP_Code VARCHAR(255) NOT NULL,
    number VARCHAR(255) DEFAULT NULL,
    name VARCHAR(255) DEFAULT NULL,
    username VARCHAR(255) UNIQUE DEFAULT NULL,
    password VARCHAR(255) DEFAULT NULL,
    admin_id VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (EMP_Code)
);

CREATE TABLE user (
    meter_id INT NOT NULL,
    username VARCHAR(255) UNIQUE DEFAULT NULL,
    name VARCHAR(255) DEFAULT NULL,
    address VARCHAR(255) DEFAULT NULL,
    rmn VARCHAR(255) DEFAULT NULL,
    password VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (meter_id)
);

CREATE TABLE bill (
    bill_id INT NOT NULL AUTO_INCREMENT,
    meter_id INT DEFAULT NULL,
    month VARCHAR(255) DEFAULT NULL,
    year VARCHAR(255) DEFAULT NULL,
    units INT DEFAULT NULL,
    amount DOUBLE DEFAULT NULL,
    status VARCHAR(255) DEFAULT 'Pending',
    PRIMARY KEY (bill_id),
    CONSTRAINT fk_user_meter FOREIGN KEY (meter_id) REFERENCES user(meter_id)
);
