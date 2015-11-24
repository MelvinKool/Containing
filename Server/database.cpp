#include <iostream>

#include "database.h"

#include <stdlib.h>
#include <stdio.h>

using namespace std;

database::database()
{
	rc = sqlite3_open("Files/containing.db", &db);
    if(rc){
        cout << "Can't open the database." << sqlite3_errmsg(db) << endl;
        open = false;
    }
    else{
        open = true;
        //check if table exists, else create it.
        string create = "create table test4(col1 varchar(1) primary key)";
        execute(create);
    }
}

database::~database()
{
    sqlite3_close(db);
}

bool database::isOpen(){
    return open;
}

int database::callback(void *NotUsed, int argc, char** argv, char** azColName){
    int i;
    for(i=0; i<argc; i++){
        printf("%s = %s\n", azColName[i], argv[i] ? argv[i] : "NULL");
    }
    printf("\n");
    return 0;
}

bool database::execute(string sqlStatement){
    if(open){
        rc = sqlite3_exec(db, sqlStatement.c_str(), callback, 0, &zErrMsg);
        if(rc != SQLITE_OK){
            cout << "Error in database::execute: " << sqlStatement << endl;
            return false;
        }
        else return true;
    }
    return false;
}
