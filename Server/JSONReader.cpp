#include "JSONReader.h"
#include "JSONGenerator.h"
#include <iostream>
#include <fstream>

using namespace std;
using namespace rapidjson;

JSONReader::JSONReader(char* transportFPath){
    this->transportFPath = transportFPath;
}

void JSONReader::loadTransport(map<int,Transport>& transportMap){
    string temp, transportJSON;
    ifstream jsonLoader(transportFPath);
    while(getline(jsonLoader,temp)){
        transportJSON += temp;
        transportJSON.push_back('\n');
    }
    Document document;
    document.Parse(transportJSON.c_str());
    rapidjson::Value& freightShips = document["FreightShip"];
    rapidjson::Value& storage = document["Storage"];
    rapidjson::Value& seaShips = document["SeaShip"];
    rapidjson::Value& trains = document["Train"];
    rapidjson::Value& AGVs = document["AGV"];
    rapidjson::Value& truckCranes = document["TruckCrane"];
    JSONGenerator generator;
    string test = toString(freightShips);
    cout << test << endl;
    //cout << toString(freightShips) << endl;
}

template <class T>
std::string JSONReader::toString(T &jsonValue){
	StringBuffer strbuf;
	Writer<StringBuffer> writer(strbuf);
	jsonValue.Accept(writer);
	return strbuf.GetString();
}
