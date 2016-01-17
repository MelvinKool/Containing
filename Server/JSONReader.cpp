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

/*
void JSONReader::loadVehicle(const char* key,rapidjson::Document& document,Connections& simulator, vector<Crane>& transportVector){
    rapidjson::Value& transportJSON = document[key];
    int count = transportJSON["count"].GetInt();
    const Value& rotation = transportJSON["rotation"];
    float rotX, rotY, rotZ;
    rotX = (float) rotation[0].GetDouble();
    rotY = (float) rotation[1].GetDouble();
    rotZ = (float) rotation[2].GetDouble();
    vector3f rotationVect(rotX,rotY,rotZ);
    float speed = (float)transportJSON["speed"].GetDouble();
    Value::ConstMemberIterator itr = transportJSON.FindMember("grabber");
    float holderSpeed, grabberSpeed, grabber_y_offset;
    vector3f grabberPos;
    bool has_holder;
    if (itr != transportJSON.MemberEnd()){
        //vehicle has a grabber
        const Value& grabber = transportJSON["grabber"];
        holderSpeed = (float) grabber["holderSpeed"].GetDouble();
        const Value& grabberPosition = grabber["position"];
        float grabberPosX = (float) grabberPosition[0].GetDouble();
        float grabberPosY = (float) grabberPosition[1].GetDouble();
        float grabberPosZ = (float) grabberPosition[2].GetDouble();
        grabberPos = vector3f(grabberPosX,grabberPosY, grabberPosZ);
        grabberSpeed = (float) grabber["speed"].GetDouble();
        grabber_y_offset = (float) grabber["y_offset"].GetDouble();
        has_holder = grabber["has_holder"].GetBool();
    }
    JSONGenerator generator;
    vector3f tempVect;
    const Value& positions = transportJSON["positions"];
    vector<string> allObjectsJSON;
    for (rapidjson::SizeType i = 0; i < positions.Size(); i++)
    {
        const rapidjson::Value& position = positions[i];
        float x,y,z;
        x = position[0].GetDouble();
        y = position[1].GetDouble();
        z = position[2].GetDouble();
        cout << x << " " << y << " " << z << endl;
        cout << "spawning vehicle ..." << endl;
        tempVect = vector3f(x,y,z);
        //generate spawn json
        //int objectId, char* vehicleType, vector3f coordinate, vector3f rotation, float maximumSpeed,
		//float holderSpeed, float grabberSpeed, float grabber_y_offset, vector3f grabberPos, bool has_holder)
        allObjectsJSON.push_back(generator.spawnCraneObject(i, key.c_str(), tempVect, rotationVect, speed));
        //spawn objects in simulator
        //simulator.writeToSim(genJSON);
    }
    string final_JSON_string = vector_join(allObjectsJSON);
    simulator.writeToSim(final_JSON_string);
}*/

/*template <class T>
std::string JSONReader::toString(T &jsonValue){
	StringBuffer strbuf;
	Writer<StringBuffer> writer(strbuf);
	jsonValue.Accept(writer);
	return strbuf.GetString();
}*/
