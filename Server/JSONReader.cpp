#include "JSONReader.h"
#include "JSONGenerator.h"
#include <iostream>
#include <fstream>
#include "vector3f.h"
#include "allObjects.h"

using namespace std;
using namespace rapidjson;

JSONReader::JSONReader(char* transportFPath, Server* server){
    this->transportFPath = transportFPath;
    this->server = server;
}

void JSONReader::loadTransport(AllObjects& allObjects)
{
    string temp, transportJSON;
    ifstream jsonLoader(transportFPath);
    while(getline(jsonLoader,temp)){
        transportJSON += temp;
        transportJSON.push_back('\n');
    }
    Document document;
    document.Parse(transportJSON.c_str());
    //`JSONReader::loadVehicle(char const*, rapidjson::GenericDocument<rapidjson::UTF8<char>,
    //rapidjson::MemoryPoolAllocator<rapidjssimulator.writeToSim(genJSON);on::CrtAllocator>, rapidjson::CrtAllocator>&,
    //Connections&, std::vector<Crane, std::allocator<Crane> >&)'
    //loadVehicle("FreightShip", document,*simulator,freightShipCranes);
    //loadVehicle("Storage", document,*simulator,storageCranes);
    std::vector<std::string> seaShipCraneObjects = loadVehicle("SeaShip", document,allObjects.seaShipCranes);
    //loadVehicle("Train", document,*simulator,trainCranes);
    std::vector<std::string> agvObjects = loadVehicle("AGV", document, allObjects.agvs);
    //loadVehicle("TruckCrane", document,*simulator,truckCranes);
    //concentrate vectors
    std::vector<std::string> allObjectsStrVector;
    //allObjectsStrVector.push_back(testJSON);
    allObjectsStrVector.insert( allObjectsStrVector.end(), agvObjects.begin(), agvObjects.end() );
    JSONGenerator generator;
    string allObjectsJSON = generator.spawnObjects(allObjectsStrVector);
    //server->writeToSim();
    server->writeToSim(allObjectsJSON);
    //send everything to the simulator


    //JSONGenerator generator;
    //cout << toString(freightShips) << endl;
}
