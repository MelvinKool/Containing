#include "database.h"
#include <stdlib.h>
#include <iostream.h>
#include <cppconn/driver.h>
#include <cppconn/exception.h>
#include <cppconn/resultset.h>
#include <cppconn/statement.h>

using namespace std;

sql::Connection *con;

void database::saveData()
{

}

void database::createConnection()
{
	cout<<endl;
	cout<<"Creating DB connection"<<endl

	try
	{
		sql::Driver *driver;
		driver = get_driver_instance();
		con = driver->connect("178.84.137.198:3306","containing","e0d2c603414413df6b6d15dcb4f7c1fb");
		con->setSchema("containing");
	}
	catch (sql::SQLException &e)
	{
		cout << "# ERR: SQLException in " << __FILE__;
  		cout << "(" << __FUNCTION__ << ") on line "<< __LINE__ << endl;
  		cout << "# ERR: " << e.what();
  		cout << " (MySQL error code: " << e.getErrorCode();
  		cout << ", SQLState: " << e.getSQLState() << " )" << endl;
	}

}
