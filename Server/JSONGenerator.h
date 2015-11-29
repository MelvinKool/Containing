#ifndef JSONGENERATOR_H_
#define JSONGENERATOR_H_
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

#endif //JSONGENERATOR_H_
