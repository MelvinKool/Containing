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
    loadVehicle(freightShips);
    rapidjson::Value& storage = document["Storage"];
    loadVehicle(storage);
    rapidjson::Value& seaShips = document["SeaShip"];
    loadVehicle(seaShips);
    rapidjson::Value& trains = document["Train"];
    loadVehicle(trains);
    rapidjson::Value& AGVs = document["AGV"];
    loadVehicle(AGVs);
    rapidjson::Value& truckCranes = document["TruckCrane"];
    loadVehicle(truckCranes);
    //JSONGenerator generator;
    string test = JSONGenerator::toString(freightShips);
    cout << test << endl;
    //cout << toString(freightShips) << endl;
}


void JSONReader::loadVehicle(rapidjson::Value& freightJSON){
    /*
    "FreightShip":{
      "count":8,
      "positions":[ ],
      "rotation":[ ],
      "speed":1.0,
      "grabber":{
         "holderSpeed":1.0,
         "position":[ ],
         "speed":1.0,
         "y_offset":-11.0,
         "has_holder":true
      }
   },
    */
    cout << "Began reading freightship json" << endl;
    int count = freightJSON["count"].GetInt();
    const Value& positions = freightJSON["positions"];
    for (rapidjson::SizeType i = 0; i < positions.Size(); i++)
    {
        const rapidjson::Value& position = positions[i];
        double x,y,z;
        x = position[0].GetDouble();
        y = position[1].GetDouble();
        z = position[2].GetDouble();
        cout << x << " " << y << " " << z << endl;
    }
    const Value& rotation = freightJSON["rotation"];
    double rotX, rotY, rotZ;
    rotX = rotation[0].GetDouble();
    rotY = rotation[1].GetDouble();
    rotZ = rotation[2].GetDouble();
    cout << rotX << " " << rotY << " " << rotZ << endl;
    double speed = freightJSON["speed"].GetDouble();
    Value::ConstMemberIterator itr = freightJSON.FindMember("grabber");
    if (itr != freightJSON.MemberEnd()){
        //vehicle has a grabber
        const Value& grabber = freightJSON["grabber"];
        double holderSpeed = grabber["holderSpeed"].GetDouble();
        const Value& grabberPosition = grabber["position"];
        double grabberPosX = grabberPosition[0].GetDouble();
        double grabberPosY = grabberPosition[1].GetDouble();
        double grabberPosZ = grabberPosition[2].GetDouble();
        double grabberSpeed = grabber["speed"].GetDouble();
        double grabber_y_offset = grabber["y_offset"].GetDouble();
        bool has_holder = grabber["has_holder"].GetBool();
    }
    cout << count << endl;
}
/*template <class T>
std::string JSONReader::toString(T &jsonValue){
	StringBuffer strbuf;
	Writer<StringBuffer> writer(strbuf);
	jsonValue.Accept(writer);
	return strbuf.GetString();
}*/
