#ifndef DATABASE_H
#define DATABASE_H

#include <sqlite3.h>
#include <string>

class database
{
    private:
        bool open = false;
        sqlite3* db;
        char* zErrMsg = 0;
        int rc;

        static int callback(void *NotUsed, int argc, char** argv, char** azColName);
        bool execute(std::string sqlStatement);
    public:
		database();
		~database();
        bool isOpen();
};

#endif // DATABASE_H
