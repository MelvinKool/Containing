#include "JSONReader.h"
#include "JSONGenerator.h"
#include <iostream>
#include <fstream>
#include "vector3f.h"
#include "allObjects.h"

using namespace std;
using namespace rapidjson;

JSONReader::JSONReader(char* transportFPath, Server* server)
{
    this->transportFPath = transportFPath;
    this->server = server;
}

void JSONReader::loadTransport(AllObjects& allObjects)
{
    string temp, transportJSON;
    ifstream jsonLoader(transportFPath);
    while(getline(jsonLoader,temp))
    {
        transportJSON += temp;
        transportJSON.push_back('\n');
    }
    Document document;
    document.Parse(transportJSON.c_str());
    //vector with all objects to spawn
    vector<string> allObjectsStrVector;
    //`JSONReader::loadVehicle(char const*, GenericDocument<UTF8<char>,
    //MemoryPoolAllocator<rapidjssimulator.writeToSim(genJSON);on::CrtAllocator>, CrtAllocator>&,
    //Connections&, vector<Crane, allocator<Crane> >&)'
    //loadVehicle("FreightShip", document,*simulator,freightShipCranes);
    //loadVehicle("Storage", document,*simulator,storageCranes);
    int count = 0;

    vector<string> storageCraneObjects = loadCranes("Storage", document,allObjects.storageCranes,count);
    allObjectsStrVector.insert( allObjectsStrVector.end(), storageCraneObjects.begin(), storageCraneObjects.end() );
    count += allObjects.storageCranes.size();
    vector<string> truckCraneObjects = loadCranes("TruckCrane", document,allObjects.truckCranes,count);
    allObjectsStrVector.insert( allObjectsStrVector.end(), truckCraneObjects.begin(), truckCraneObjects.end() );
    count += allObjects.truckCranes.size();
    vector<string> trainCraneObjects = loadCranes("Train", document,allObjects.trainCranes,count);
    allObjectsStrVector.insert( allObjectsStrVector.end(), trainCraneObjects.begin(), trainCraneObjects.end() );
    count += allObjects.trainCranes.size();
    vector<string> freightShipCraneObjects = loadCranes("FreightShip", document,allObjects.freightShipCranes,count);
    allObjectsStrVector.insert( allObjectsStrVector.end(), freightShipCraneObjects.begin(), freightShipCraneObjects.end() );
    count += allObjects.freightShipCranes.size();
    vector<string> seaShipCraneObjects = loadCranes("SeaShip", document,allObjects.seaShipCranes,count);
    allObjectsStrVector.insert( allObjectsStrVector.end(), seaShipCraneObjects.begin(), seaShipCraneObjects.end() );
    //add the cranes to the crane spawnstrings vector
    //allCranesVector.insert(allCranesVector.end(), seaShipCraneObjects.begin(), seaShipCraneObjects.end());
    //loadVehicle("Train", document,*simulator,trainCranes);
    //insert all cranes

    //add all cranes to the spawnstrings vector
    //add all agvs to the spawnstrings vector
    vector<string> agvObjects = loadAGVs(document, allObjects.agvs);
    allObjectsStrVector.insert( allObjectsStrVector.end(), agvObjects.begin(), agvObjects.end() );
    //loadVehicle("TruckCrane", document,*simulator,truckCranes);
    //concentrate vectors
    //allObjectsStrVector.push_back(testJSON);
    JSONGenerator generator;
    string allObjectsJSON = generator.spawnObjects(allObjectsStrVector);
    //server->writeToSim();
    server->writeToSim(allObjectsJSON);
    //send everything to the simulator


    //JSONGenerator generator;
    //cout << toString(freightShips) << endl;
}

vector<string> JSONReader::loadCranes(const char* key,Document& document, vector<Crane>& craneVector, int indexStart)
{
    Value& transportJSON = document[key];
    //int count = transportJSON["count"].GetInt();
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
    //vector3f tempVect;
    const Value& positions = transportJSON["positions"];
    //vector<string> allObjectsJSON;
    vector<string> allSpawnObjects;
    for (SizeType i = 0; i < positions.Size(); i++)
    {
        const Value& position = positions[i];
        float x,y,z;
        x = position[0].GetDouble();
        y = position[1].GetDouble();
        z = position[2].GetDouble();
        //tempVect = vector3f(x,y,z);
        //generate spawn json
        int vehicleId = indexStart + craneVector.size();
        Crane crane(key, vehicleId,x,y,z,speed, server);
        /*
        struct grabber {
            //grabber() : () {}
            float holderSpeed = 0, speed = 0, y_offset = 0;
            bool has_holder = false;
            vector3f position = vector3f(0,0,0);
        } currentGrabber;
        */
        crane.currentGrabber.holderSpeed = holderSpeed;
        crane.currentGrabber.speed = grabberSpeed;
        crane.currentGrabber.y_offset = grabber_y_offset;
        crane.currentGrabber.has_holder = has_holder;
        crane.currentGrabber.position = grabberPos;
        string craneSpawn = generator.spawnCrane(crane, vehicleId, rotationVect);
        allSpawnObjects.push_back(craneSpawn);
        craneVector.push_back(crane);
        //simulator.writeToSim(final_JSON_string);
        //spawn objects in simulator
        //simulator.writeToSim(genJSON);
    }
    return allSpawnObjects;
}

vector<string> JSONReader::loadAGVs(Document& document, vector<AGV>& AGVVector){
    Value& transportJSON = document["AGV"];
    int count = transportJSON["count"].GetInt();
    const Value& rotation = transportJSON["rotation"];
    float rotX, rotY, rotZ;
    rotX = (float) rotation[0].GetDouble();
    rotY = (float) rotation[1].GetDouble();
    rotZ = (float) rotation[2].GetDouble();
    vector3f rotationVect(rotX,rotY,rotZ);
    float speed = (float)transportJSON["speed"].GetDouble();
    JSONGenerator generator;
    vector3f tempVect;
    const Value& positions = transportJSON["positions"];
    //vector<string> allObjectsJSON;
    vector<string> allSpawnObjects;
    for (SizeType i = 0; i < positions.Size(); i++)
    {
        const Value& position = positions[i];
        float x,y,z;
        x = position[0].GetDouble();
        y = position[1].GetDouble();
        z = position[2].GetDouble();
        tempVect = vector3f(x,y,z);
        //generate spawn json
        //JSONGenerator::spawnObject(int, const char*, vector3f&, vector3f&, float&, float&, float&, float&, vector3f&, bool&)
        //JSONGenerator::spawnObject(int, char*, vector3f, vector3f, float, float, float, float, vector3f, bool)
        int vehicleId = AGVVector.size();
        AGV agv(vehicleId,x,y,z,server);
        string agvSpawn = generator.spawnAGV(agv, vehicleId, rotationVect);
        allSpawnObjects.push_back(agvSpawn);
        AGVVector.push_back(agv);
        //simulator.writeToSim(final_JSON_string);
        //spawn objects in simulator
        //simulator.writeToSim(genJSON);
    }
    return allSpawnObjects;
}
