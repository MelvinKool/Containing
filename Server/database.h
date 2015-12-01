//create database containing;
//create user 'containing'@'localhost' identified by 'e0d2c603414413df6b6d15dcb4f7c1fb';
//grant all privileges on containing.* to 'containing'@'localhost';

#ifndef DATABASE_H
#define DATABASE_H

#include <mysql.h>
#include <string>

class Database
{
    private:
        MYSQL* connection;
        std::string server = "localhost";
        std::string username = "containing";
        std::string password = "e0d2c603414413df6b6d15dcb4f7c1fb";
        std::string dbName = "containing";
        bool connected = false;
        
        bool createAllTables();
        bool dropAllTables();
    public:
        Database();
        ~Database();
        bool isConnected();
        bool execute(std::string sqlStatement);
        MYSQL_RES* select(std::string sqlStatement);
        bool resetDatabase();
};

#endif //DATABASE_H