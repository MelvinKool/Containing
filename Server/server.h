#ifndef SERVER_H
#define SERVER_H
#pragma once
#include <string>
#include <mysql.h>
#include "connections.h"
#include "xmlparser.h"
#include "database.h"
#include "httpserver.h"
#include "allObjects.h"
#include "agv.h"
#include "crane.h"
#include "JSONGenerator.h"

class Server
{
    public:
        Server();
        void writeToSim(std::string message);
        void checkContainers();
        void stopRunning();
        Connections* getConnections();

        AllObjects allObjects;
        JSONGenerator JGen;
        ShortestPathDijkstra pathFinderLoaded;
        ShortestPathDijkstra pathFinderUnloaded;
        Crane crane;
        AGV agvs[100];
        float x = 0,y = 0,z = 0;
        int dump = 2,train = 6,truck = 7,ship = 8;
    private:
        void processLeavingContainer(MYSQL_ROW &row);
        void processArrivingContainer(MYSQL_ROW &row);
        int getFreeAGV();
        vector3f getTruckStop();
        std::string spawnObject(std::string type,vector3f location);

        Database db;
        XmlParser xmlParser;
        Connections connections;
        HttpServer httpserver;

        bool stop = false;
        std::vector<vector3f> truckStops;
        std::string vehicle = "";
        int containerId = -1,agvID = 0;
        std::vector<std::string> commands;
};
#endif
