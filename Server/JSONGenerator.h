
#ifndef JSONGENERATOR_H
#define JSONGENERATOR_H
#include "Files/rapidjson/document.h"
#include "Files/rapidjson/stringbuffer.h"
#include "Files/rapidjson/writer.h"
#include "vector3f.h"
#include <vector>

class JSONGenerator
{
  private:
  public:
    std::string moveTo(int vehicleId, std::vector<vector3f> coordinates, float totalDistance);
    std::string transferContainer(int containerId, int sourceId, int targetId);
    std::string spawnObject(int objectId, vector3f coordinates);
    template <class T>
    std::string toString(T &jsonValue){
    	rapidjson::StringBuffer strbuf;
    	rapidjson::Writer<rapidjson::StringBuffer> writer(strbuf);
    	jsonValue.Accept(writer);
    	return strbuf.GetString();
    }
    //std::string toString(rapidjson::Value *jsonValue);
  private:
    rapidjson::Document createJSONDocument();
};

#endif //JSONGENERATOR_H
