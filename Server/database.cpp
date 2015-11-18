#include "database.h"

void database::saveData()
{

}

database::database()
{
	cout<<endl;
	cout<<"Creating DB connection..."<<endl

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

database::~database()
{
	//close connection
	delete con;
}