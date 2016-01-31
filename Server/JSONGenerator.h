#ifndef JSONGENERATOR_H
#define JSONGENERATOR_H
#include "Files/rapidjson/document.h"
#include "Files/rapidjson/stringbuffer.h"
#include "Files/rapidjson/writer.h"
#include "vector3f.h"
#include "crane.h"
#include "agv.h"
#include <vector>
#include <algorithm>
#include <string>

using namespace std;

class JSONGenerator
{
  public:
    std::string moveTo(int vehicleId, std::vector<vector3f> coordinates, float totalDistance, int containerId);
    std::string craneTransferContainer(int craneId, int containerId, int destAGV);
    std::string craneTransferContainer(int craneId, int containerId, int sortFieldID, vector3f targetVect);
    std::string agvAttachContainer(int agvId, int containerId);
    std::string despawnObject(int transportID, const char* vehicleType, int containerId);
    std::string spawnObject(string type,std::vector<int> contID);
    std::string spawnObject(string type,vector3f location,std::vector<int> contID,int transportID);
    std::string spawnCrane(Crane& crane, int craneId,vector3f rotation);
    std::string spawnTruck(vector3f location, int contID,int truckId);
    std::string spawnTrain(std::vector<int> contIDs,int trainID);
    std::string spawnSeaShip(std::vector<int> contIDs,int shipID);
    std::string spawnBargeShip(vector3f location,std::vector<int> contIDs,int shipID);
    std::string spawnAGV(AGV& agv, int agvId, vector3f rotation);
    std::string spawnObjects(std::vector<std::string>& spawnStrings);
    std::string generateCommandList(int containerId, std::vector<std::string>& commandList);
    template <class T>
    static std::string toString(T &jsonValue)
    {
    	rapidjson::StringBuffer strbuf;
    	rapidjson::Writer<rapidjson::StringBuffer> writer(strbuf);
    	jsonValue.Accept(writer);
    	string stupidRapidJSONString = strbuf.GetString();
        //stupidRapidJSONString.erase(std::remove(stupidRapidJSONString.begin(), stupidRapidJSONString.end(), '\\'), stupidRapidJSONString.end());
        return stupidRapidJSONString;
    }
    //std::string toString(rapidjson::Value *jsonValue);
  private:
    rapidjson::Document createJSONDocument();
};

#endif
