#ifndef JSONREADER_H
#define JSONREADER_H
#include <map>
#include "connections.h"
#include "Files/rapidjson/document.h"
#include "Files/rapidjson/rapidjson.h"
#include "crane.h"
#include "agv.h"
#include "transport.h"
#include <sstream>
#include "JSONGenerator.h"
#include <iostream>
#include "vector3f.h"
#include "server.h"

using namespace rapidjson;

class JSONReader{
    private:
        char* transportFPath;
    public:
        JSONReader(char* transportFPath, Server* server);
        void loadTransport(AllObjects& allObjects);
        /*void loadTransport(Connections* simulator,vector<Crane>& freightShipCranes,
            vector<Crane>& storageCranes, vector<Crane>& seaShipCranes, vector<Crane>& trainCranes,
            vector<AGV>& agvs, vector<Crane>& truckCranes);*/
    private:
        //void loadVehicle(const char* key,rapidjson::Document& document,Connections& simulator, vector<Crane>& transportVector);
        //void loadVehicle(const char* key,rapidjson::Document& document,Connections& simulator, vector<AGV>& transportVector);
        /*template <class T>
        string vector_join( const vector<T>& v, const string& token ){
          ostringstream result;
          for (typename vector<T>::const_iterator i = v.begin(); i != v.end(); i++){
            if (i != v.begin()) result << token;
            result << *i;
          }
          return result.str();
        }*/

        /*loads all vehicles with the specified key into a list of json strings*/
        Server* server;
        std::vector<std::string> loadCranes(const char* key,rapidjson::Document& document, std::vector<Crane>& craneVector);
        std::vector<std::string> loadAGVs(rapidjson::Document& document, std::vector<AGV>& AGVVector);
};
#endif
