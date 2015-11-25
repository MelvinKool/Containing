#include <iostream>
#include <vector>

#include "database.h"

using namespace std;

database::database(){
	rc = sqlite3_open("Files/containing.db", &db);
    if(rc){
        cout << "Can't open the database." << sqlite3_errmsg(db) << endl;
        open = false;
    }
    else{
        open = true;


        //check if table exists, else create it.
        // string create1 = "insert into test values(1);";
        // execute(create4);


    }
}

database::~database(){
    sqlite3_close(db);
}

bool database::isOpen(){
    return open;
}

bool database::execute(string sqlStatement){
    if(open){
        rc = sqlite3_exec(db, sqlStatement.c_str(), execute_callback, 0, &zErrMsg);
        if(rc != SQLITE_OK){
            cout << "Error in database::execute: " << sqlStatement << endl;
            return false;
        }
        else return true;
    }
    return false;
}

int database::execute_callback(void *NotUsed, int argc, char** argv, char** azColName){
	//Nothing to be done for a execute.
    // int i;
    // for(i=0; i<argc; i++){
    //     printf("%s = %s\n", azColName[i], argv[i] ? argv[i] : "NULL");
    // }
    // printf("\n");
    return 0;
}

bool database::createAllTables(){
    vector<string> createTables;
    createTables.push_back( "CREATE TABLE IF NOT EXISTS Owner("
                                "ownerID INT NOT NULL AUTOINCREMENT,"
                                "name VARCHAR(50),"
                                "PRIMARY KEY(ownerID)"
                            );
}

bool database::dropAllTables(){
    vector<string> dropTables;
	dropTables.push_back("DROP TABLE IF EXISTS Owner;");
	dropTables.push_back("DROP TABLE IF EXISTS Size;");
	dropTables.push_back("DROP TABLE IF EXISTS Content;");
	dropTables.push_back("DROP TABLE IF EXISTS ShippingType;");
	dropTables.push_back("DROP TABLE IF EXISTS ShippingCompany;");
	dropTables.push_back("DROP TABLE IF EXISTS Arrival;");
	dropTables.push_back("DROP TABLE IF EXISTS Departure;");
	dropTables.push_back("DROP TABLE IF EXISTS Container;");

    for(string drop : dropTables){
        if(execute(drop)) continue;
        else return false;
    }
    return true;
}
