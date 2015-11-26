#ifndef DATABASE_H
#define DATABASE_H

#include <mysql.h>
#include <string>

class database
{
    private:
        MYSQL* connection;
        
        char* server = "mator.eu";
        char* username = "containing";
        char* password = "e0d2c603414413df6b6d15dcb4f7c1fb";
        char* dbName = "containing";
        bool open = false;
        
        bool createAllTables();
        bool dropAllTables();
    public:
        database();
        ~database();
        bool isOpen();
        bool execute(std::string sqlStatement);
        MYSQL_RES* select(std::string sqlStatement);
        bool resetDatabase();
};

#endif // DATABASE_H
