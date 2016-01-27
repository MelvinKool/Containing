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
#include "Timer.h"

class Server
{
    public:
        Server();
        void writeToSim(std::string message);
        void checkContainers();
        void startRunning();
        void stopRunning();

        AllObjects allObjects;
        JSONGenerator JGen;
        ShortestPathDijkstra pathFinderLoaded;
        ShortestPathDijkstra pathFinderUnloaded;

    private:
        void processLeavingContainer(MYSQL_ROW &row);
        void processArrivingContainer(MYSQL_ROW &row);
        int getFreeAGV();
        int getTruckStop();
        int getTransportID();
        void spawnObject(std::string type,vector3f location, int contID);

        Database db;
        XmlParser xmlParser;
        Connections connections;
        HttpServer httpserver;
        Timer timer;
        thread t1;

        bool stop=true,trainSpawned=false;
        std::vector<vector3f> truckStops;
        std::string vehicle="",currentDate="",currentTime="",previousDate="",previousTime="";
        int containerId = -1,agvID = 0;
};
#endif
