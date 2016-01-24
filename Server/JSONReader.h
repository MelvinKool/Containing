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
        template <class Vehicle>
        std::vector<std::string> loadVehicle(const char* key,rapidjson::Document& document, vector<Vehicle>& transportVector){
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
            //vector<string> allObjectsJSON;
            std::vector<std::string> allSpawnObjects;
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
                //JSONGenerator::spawnObject(int, const char*, vector3f&, vector3f&, float&, float&, float&, float&, vector3f&, bool&)
                //JSONGenerator::spawnObject(int, char*, vector3f, vector3f, float, float, float, float, vector3f, bool)
                if(key == "AGV")
                {
                    cout << "AGV" << endl;
                    AGV agv((int)i,x,y,z,server);
                    string agvSpawn = generator.spawnAGV(agv, (int) i, rotationVect);
                    allSpawnObjects.push_back(agvSpawn);
                }
                else{
                    //this is a crane
                    cout << "crane" << endl;
                    /*Crane crane((int)i, key.c_str(), tempVect, rotationVect,
                            speed,holderSpeed,grabberSpeed, grabber_y_offset,
                            grabberPos, has_holder);
                    string craneSpawn = generator.spawnCrane(crane, (int) i,rotationVect);//identity???
                    allSpawnObjects.push_back(agvSpawn);*/
                }
                //simulator.writeToSim(final_JSON_string);
                //spawn objects in simulator
                //simulator.writeToSim(genJSON);
            }
            return allSpawnObjects;
        }

};
#endif
