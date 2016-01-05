
#ifndef JSONGENERATOR_H
#define JSONGENERATOR_H
#include "Files/rapidjson/document.h"
#include "Files/rapidjson/stringbuffer.h"
#include "Files/rapidjson/writer.h"
#include "vector3f.h"
class JSONGenerator
{
  public:
    std::string moveTo(int vehicleId, std::vector<vector3f> coordinates);
    std::string transferContainer(int containerId, int sourceId, int targetId);
    std::string spawnObject(int objectId, vector3f coordinates);
  private:
    rapidjson::Document createJSONDocument();
    std::string toString(rapidjson::Document *document);
};

#endif //JSONGENERATOR_H
