#ifndef JSONGENERATOR_H
#define JSONGENERATOR_H
#include "Files/rapidjson/document.h"
#include "Files/rapidjson/stringbuffer.h"
#include "Files/rapidjson/writer.h"
#include "vector3f.h"
#include <vector>

using namespace std;

class JSONGenerator
{
  public:
    std::string moveTo(int vehicleId, std::vector<vector3f> coordinates, float totalDistance);
    std::string craneTransferContainer(int craneId, int containerId, vector3f targetVect);
    std::string agvAttachContainer(int agvId, int containerId);
    //std::string spawnObject(int objectId, char* vehicleType, vector3f coordinate, vector3f rotation, float maximumSpeed);
    std::string spawnObject(string type,vector3f location,std::vector<int> contID,int transportID);
    std::string spawnObject(int objectId, const char* vehicleType, vector3f coordinate, vector3f rotation, float maximumSpeed);
    std::string spawnObject(int objectId, const char* vehicleType, vector3f coordinate, vector3f rotation, float maximumSpeed,
    										float holderSpeed, float grabberSpeed, float grabber_y_offset, vector3f grabberPos, bool has_holder);
    std::string spawnObjects(std::vector<std::string> spawnStrings);
    std::string despawnObject(int transportID){};
    std::string generateCommandList(int containerId, std::vector<std::string>& commandList);
    template <class T>
    static std::string toString(T &jsonValue){
    	rapidjson::StringBuffer strbuf;
    	rapidjson::Writer<rapidjson::StringBuffer> writer(strbuf);
    	jsonValue.Accept(writer);
    	return strbuf.GetString();
    }
    //std::string toString(rapidjson::Value *jsonValue);
  private:
    rapidjson::Document createJSONDocument();
};

#endif
