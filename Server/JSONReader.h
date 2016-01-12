#ifndef JSONREADER_H
#define JSONREADER_H
#include <map>
#include "connections.h"
#include "Files/rapidjson/document.h"
#include "Files/rapidjson/rapidjson.h"
#include "crane.h"
#include "agv.h"

class JSONReader{
    private:
        char* transportFPath;
    public:
        JSONReader(char* transportFPath);
        void loadTransport(Connections* simulator,vector<Crane>& freightShipCranes,
            vector<Crane>& storageCranes, vector<Crane>& seaShipCranes, vector<Crane>& trainCranes,
            vector<AGV>& agvs, vector<Crane>& truckCranes);
    private:
        void loadVehicle(std::string key,rapidjson::Document& document,Connections* simulator,vector<Crane>& freightShipCranes,
            vector<Crane>& storageCranes, vector<Crane>& seaShipCranes, vector<Crane>& trainCranes,
            vector<AGV>& agvs, vector<Crane>& truckCranes);
        /*template <class T>
        std::string toString(T &jsonValue);*/
        //std::string toString(rapidjson::Value &jsonValue);
};
#endif
