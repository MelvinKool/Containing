#ifndef SERVER_H
#define SERVER_H

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
    ShortestPathDijkstra pathFinderLoaded = ShortestPathDijkstra("./Files/RouteFiles/LoadedRoutes.csv");
    ShortestPathDijkstra pathFinderUnloaded = ShortestPathDijkstra("./Files/RouteFiles/UnloadedRoutes.csv");

private:
    struct storageLaneSpot_t {
        int x = -1;
        int y = 0;
        int z = 0;
        int nr = -1;
    } lastStorageLaneSpot;
    void processLeavingContainer(MYSQL_ROW &row);
    void processArrivingContainer(MYSQL_ROW &row);
    int getFreeAGV();
    int getTruckStop();
    int getTransportID();
    void spawnObject(std::string type,vector3f location, int contID);
    storageLaneSpot_t getStorageLaneSpot();
    void loadParkingLots();

    Database db;
    XmlParser xmlParser;
    Connections connections;
    HttpServer httpserver;
    Timer timer;
    thread t1;

    bool stop=true,trainSpawned=false,seaShipSpawned=false;
    std::vector<vector3f> truckStops;
    std::string vehicle="",currentDate="",currentTime="",previousDate="",previousTime="";
    int containerCount=0;
    int lastTrainContainer=-1,trainCraneId=0;
    int lastSeaShipContainer=-1,seaShipId=0,seaShipCraneId=0;
    int lastTruckStop = 0;
    int lastTransportID = 0;
};

#endif