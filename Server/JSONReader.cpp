#include "JSONReader.h"
#include "JSONGenerator.h"
#include <iostream>
#include <fstream>
#include "vector3f.h"

using namespace std;
using namespace rapidjson;

JSONReader::JSONReader(char* transportFPath){
    this->transportFPath = transportFPath;
}

void JSONReader::loadTransport(Connections* simulator,vector<Crane>& freightShipCranes,
                                vector<Crane>& storageCranes, vector<Crane>& seaShipCranes, vector<Crane>& trainCranes,
                                vector<AGV>& agvs, vector<Crane>& truckCranes)
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
    //rapidjson::MemoryPoolAllocator<rapidjson::CrtAllocator>, rapidjson::CrtAllocator>&,
    //Connections&, std::vector<Crane, std::allocator<Crane> >&)'
    loadVehicle("FreightShip", document,*simulator,freightShipCranes);
    loadVehicle("Storage", document,*simulator,storageCranes);
    loadVehicle("SeaShip", document,*simulator,seaShipCranes);
    loadVehicle("Train", document,*simulator,trainCranes);
    loadVehicle("AGV", document,*simulator,agvs);
    loadVehicle("TruckCrane", document,*simulator,truckCranes);
    //JSONGenerator generator;
    //cout << toString(freightShips) << endl;
}
