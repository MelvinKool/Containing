
#ifndef JSONGENERATOR_H
#define JSONGENERATOR_H
#include "Files/rapidjson/document.h"
#include "Files/rapidjson/stringbuffer.h"
#include "Files/rapidjson/writer.h"
class JSONGenerator
{
  private:
  public:
    std::string moveTo();
  private:
    rapidjson::Document createJSONDocument();
    std::string toString(rapidjson::Document *document);
};

#endif //JSONGENERATOR_H
