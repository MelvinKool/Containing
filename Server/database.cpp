#include <iostream>
#include <vector>

#include "database.h"

using namespace std;

// Make connection to the db.
Database::Database()
{
    connection = mysql_init(NULL);
    if(!mysql_real_connect(connection, server.c_str(), username.c_str(), password.c_str(), dbName.c_str(), 0, NULL, 0))
    {
        cout << "Can't open the database." << endl;
        cout << "...." << mysql_error(connection) << endl;
        connected = false;
    }
    else
    {
        connected = true;
        createAllTables();
    }
}

Database::~Database()
{
    mysql_close(connection);
}

bool Database::isConnected()
{
    return connected;
}

// Use this for any statement that does not return data like: update, insert or drop.
bool Database::execute(string sqlStatement)
{
    if(connected)
    {
        if(mysql_query(connection, sqlStatement.c_str()))
        {
            cout << "Error in database::execute: " << sqlStatement << endl;
            cout << "...." << mysql_error(connection) << endl;
        }
        else
        {
            return true;
        }
    }
    return false;
}

// Use this for select statment.
MYSQL_RES* Database::select(string sqlStatement)
{
    if(execute(sqlStatement))
    {
        return mysql_store_result(connection);
    }
    return NULL;
}

// Creates all the tables in the database.
bool Database::createAllTables()
{
    vector<string> createTables;

    createTables.push_back( "CREATE TABLE IF NOT EXISTS Owner("
                            "ownerID INT NOT NULL AUTO_INCREMENT,"
                            "name VARCHAR(50),"
                            "PRIMARY KEY(ownerID)"
                            ");");
    createTables.push_back( "CREATE TABLE IF NOT EXISTS Size("
                            "sizeID INT NOT NULL AUTO_INCREMENT,"
                            "length VARCHAR(10),"
                            "width VARCHAR(10),"
                            "height VARCHAR(10),"
                            "PRIMARY KEY(SizeID)"
                            ");");
    createTables.push_back( "CREATE TABLE IF NOT EXISTS Content("
                            "contentID INT NOT NULL AUTO_INCREMENT,"
                            "name VARCHAR(50),"
                            "type VARCHAR(50),"
                            "danger VARCHAR(50),"
                            "PRIMARY KEY(contentID)"
                            ");");
    createTables.push_back( "CREATE TABLE IF NOT EXISTS ShippingType("
                            "shippingTypeID INT NOT NULL AUTO_INCREMENT,"
                            "sort VARCHAR(50) NOT NULL,"
                            "PRIMARY KEY(shippingTypeID)"
                            ");");
    createTables.push_back( "CREATE TABLE IF NOT EXISTS ShippingCompany("
                            "shippingCompanyID INT NOT NULL AUTO_INCREMENT,"
                            "name VARCHAR(50) NOT NULL,"
                            "PRIMARY KEY(shippingCompanyID)"
                            ");");
    createTables.push_back( "CREATE TABLE IF NOT EXISTS Arrival("
                            "shipmentID INT NOT NULL AUTO_INCREMENT,"
                            "date DATE,"
                            "timeFrom VARCHAR(5),"
                            "timeTill VARCHAR(5),"
                            "positionX INT,"
                            "positionY INT,"
                            "positionZ INT,"
                            "shippingType INT,"
                            "shippingCompany INT,"
                            "PRIMARY KEY(shipmentID),"
                            "FOREIGN KEY(shippingType) REFERENCES ShippingType(shippingTypeID) ON DELETE RESTRICT,"
                            "FOREIGN KEY(shippingCompany) REFERENCES ShippingCompany(shippingCompanyID) ON DELETE RESTRICT"
                            ");");
    createTables.push_back( "CREATE TABLE IF NOT EXISTS Departure("
                            "shipmentID INT NOT NULL AUTO_INCREMENT,"
                            "date DATE,"
                            "timeFrom VARCHAR(5),"
                            "timeTill VARCHAR(5),"
                            "shippingType INT,"
                            "shippingCompany INT,"
                            "PRIMARY KEY(shipmentID),"
                            "FOREIGN KEY(shippingType) REFERENCES ShippingType(shippingTypeID) ON DELETE RESTRICT,"
                            "FOREIGN KEY(shippingCompany) REFERENCES ShippingCompany(shippingCompanyID) ON DELETE RESTRICT"
                            ");");
    createTables.push_back( "CREATE TABLE IF NOT EXISTS Container("
                            "containerID INT NOT NULL AUTO_INCREMENT,"
                            "containerNr INT,"
                            "storagelane INT,"
                            "positionX INT,"
                            "positionY INT,"
                            "positionZ INT,"
                            "iso VARCHAR(50),"
                            "weightEmpty INT,"
                            "weightContents INT,"
                            "owner INT,"
                            "size INT,"
                            "contents INT,"
                            "arrivalInfo INT,"
                            "departureInfo INT,"
                            "PRIMARY KEY(containerID),"
                            "FOREIGN KEY(owner) REFERENCES Owner(ownerID) ON DELETE RESTRICT,"
                            "FOREIGN KEY(size) REFERENCES Size(sizeID) ON UPDATE CASCADE ON DELETE CASCADE,"
                            "FOREIGN KEY(contents) REFERENCES Content(contentID) ON UPDATE CASCADE ON DELETE CASCADE,"
                            "FOREIGN KEY(arrivalInfo) REFERENCES Arrival(shipmentID) ON UPDATE CASCADE ON DELETE CASCADE,"
                            "FOREIGN KEY(departureInfo) REFERENCES Departure(shipmentID) ON UPDATE CASCADE ON DELETE CASCADE"
                            ");");

    for(string createTable : createTables)
    {
        if(!execute(createTable)) return false;
    }
    return true;
}

bool Database::dropAllTables()
{
    vector<string> dropTables;

    dropTables.push_back("DROP TABLE Container;");
    dropTables.push_back("DROP TABLE Departure;");
    dropTables.push_back("DROP TABLE Arrival;");
    dropTables.push_back("DROP TABLE ShippingCompany;");
    dropTables.push_back("DROP TABLE ShippingType;");
    dropTables.push_back("DROP TABLE Content;");
    dropTables.push_back("DROP TABLE Size;");
    dropTables.push_back("DROP TABLE Owner;");

    for(string drop : dropTables)
    {
        if(!execute(drop)) return false;
    }
    return true;
}

// Drops all the tables in the database and then create's them again.
bool Database::resetDatabase()
{
    return (dropAllTables() && createAllTables());
}
