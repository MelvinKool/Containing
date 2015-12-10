#ifndef SERVER_H
#define SERVER_H

#include <string>

#include "connections.h"
#include "xmlparser.h"
#include "JSONGenerator.h"
#include "database.h"
#include "httpserver.h"

class Server
{
public:
    Server();
    void writeToSim(std::string message);
private:
    Database db;
    XmlParser xmlParser;
    Connections connections;
    HttpServer httpserver;
};

#endif //SERVER_H