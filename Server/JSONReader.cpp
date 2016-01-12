#include "JSONReader.h"
#include "JSONGenerator.h"
#include <iostream>
#include <fstream>

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
    loadVehicle("FreightShip", document,simulator,freightShipCranes);
    loadVehicle("Storage", document,simulator,storageCranes);
    loadVehicle("SeaShip", document,simulator,seaShipCranes);
    loadVehicle("Train", document,simulator,trainCranes);
    loadVehicle("AGV", document,simulator,agvs);
    loadVehicle("TruckCrane", document,simulator,truckCranes);
    //JSONGenerator generator;
    //cout << toString(freightShips) << endl;
}


void JSONReader::loadVehicle(std::string key,rapidjson::Document& document,Connections* simulator, vector<Transport>& transportVector){
    /*simulator,vector<Crane>& freightShipCranes,
        vector<Crane>& storageCranes, vector<Crane>& seaShipCranes, vector<Crane>& trainCranes,
        vector<AGV>& agvs, vector<Crane>& truckCranes*/
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
         "has_holder":truetrainCranes
    */
    rapidjson::Value& transportJSON = document[key];
    int count = transportJSON["count"].GetInt();
    const Value& positions = transportJSON["positions"];
    for (rapidjson::SizeType i = 0; i < positions.Size(); i++)
    {
        const rapidjson::Value& position = positions[i];
        double x,y,z;
        x = position[0].GetDouble();
        y = position[1].GetDouble();
        z = position[2].GetDouble();
        cout << x << " " << y << " " << z << endl;
    }
    const Value& rotation = transportJSON["rotation"];
    double rotX, rotY, rotZ;
    rotX = rotation[0].GetDouble();
    rotY = rotation[1].GetDouble();
    rotZ = rotation[2].GetDouble();
    double speed = transportJSON["speed"].GetDouble();
    Value::ConstMemberIterator itr = transportJSON.FindMember("grabber");
    if (itr != transportJSON.MemberEnd()){
        //vehicle has a grabber
        const Value& grabber = transportJSON["grabber"];
        double holderSpeed = grabber["holderSpeed"].GetDouble();
        const Value& grabberPosition = grabber["position"];
        double grabberPosX = grabberPosition[0].GetDouble();
        double grabberPosY = grabberPosition[1].GetDouble();
        double grabberPosZ = grabberPosition[2].GetDouble();
        double grabberSpeed = grabber["speed"].GetDouble();
        double grabber_y_offset = grabber["y_offset"].GetDouble();
        bool has_holder = grabber["has_holder"].GetBool();
    }
}
/*template <class T>
std::string JSONReader::toString(T &jsonValue){
	StringBuffer strbuf;
	Writer<StringBuffer> writer(strbuf);
	jsonValue.Accept(writer);
	return strbuf.GetString();
}*/
