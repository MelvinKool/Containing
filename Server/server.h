#ifndef SERVER_H
#define SERVER_H
#pragma once
#include <string>
#include <mysql.h>

#include "connections.h"
#include "xmlparser.h"
#include "JSONGenerator.h"
#include "database.h"
#include "httpserver.h"
#include "agv.h"
#include "crane.h"

class Server
{
    public:
        Server();
        void writeToSim(std::string message);
        void checkContainers();
        void stopRunning();
        AGV agvs[5];
        Crane crane;
        JSONGenerator JGen;
        ShortestPathDijkstra pathFinderLoaded;
        ShortestPathDijkstra pathFinderUnloaded;
        float x = 0,y = 0,z = 0;
        int dump = 2,train = 6,truck = 7,ship = 8;
    private:
        Database db;
        XmlParser xmlParser;
        Connections connections;
        HttpServer httpserver;
        bool stop = false;
        void processLeavingContainer(MYSQL_ROW &row);
        void processArrivingContainer(MYSQL_ROW &row);
        int getFreeAGV(vector3f destination);
};

#endif
