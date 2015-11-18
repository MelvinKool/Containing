#ifndef DATABASE_H
#define DATABASE_H

#include <stdlib.h>
#include <iostream>
#include <cppconn/driver.h>
#include <cppconn/exception.h>
#include <cppconn/resultset.h>
#include <cppconn/statement.h>

using namespace std;

class database
{
    private:
		sql::Connection *con;
    public:
        void saveData();
		database();
		~database();
};

#endif // DATABASE_H