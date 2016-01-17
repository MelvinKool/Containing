#ifndef SERVER_H
#define SERVER_H

#include <string>
#include "crane.h"
#include "agv.h"
#include "connections.h"
#include "xmlparser.h"
#include "database.h"
#include "httpserver.h"
#include "allObject.h"

class Server
{
public:
    Server();
    void writeToSim(std::string message);
    AllObject allObjects;
    Connections* getConnections();
private:
    Database db;
    XmlParser xmlParser;
    Connections connections;
    HttpServer httpserver;
};

#endif //SERVER_H
