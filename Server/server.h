#ifndef SERVER_H
#define SERVER_H

#include <string>
#include <mysql.h>

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
    bool stop;
    void checkContainers();
    void stopRunning();
};

#endif //SERVER_H
