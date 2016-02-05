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

class JSONReader
{
private:
    char* transportFPath;
public:
    JSONReader(char* transportFPath, Server* server);
    void loadTransport(AllObjects& allObjects);
private:
    /*loads all vehicles with the specified key into a list of json strings*/
    Server* server;
    std::vector<std::string> loadCranes(const char* key,rapidjson::Document& document, std::vector<Crane>& craneVector, int indexStart);
    std::vector<std::string> loadAGVs(rapidjson::Document& document, std::vector<AGV>& AGVVector);
};
#endif