#ifndef SERVER_H
#define SERVER_H

#include <string>
#include "connections.h"
#include "xmlparser.h"
#include "database.h"

class Server
{
public:
    Server();
    ~Server();
    void writeToSim(string message);
private:
    Connections* connections;
    xmlparser xmlParser;
    database* db;
};

#endif //SERVER_H
