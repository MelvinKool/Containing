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
    void checkContainers();
    void stopRunning();
private:
    Database db;
    XmlParser xmlParser;
    Connections connections;
    HttpServer httpserver;
    bool stop;
    void processLeavingContainer(MYSQL_ROW &row);
    void processArrivingContainer(MYSQL_ROW &row);
};

#endif //SERVER_H
