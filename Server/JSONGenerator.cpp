#include <iostream>
#include <string>
#include "Files/rapidjson/document.h"
#include "Files/rapidjson/stringbuffer.h"
#include "Files/rapidjson/writer.h"
#include "JSONGenerator.h"

using namespace std;
using namespace rapidjson;

void JsonGenerator::generateJson()
{
    // document is the root of a json message
    rapidjson::Document document;
    // define the document as an object rather than an array
    document.SetObject();
    // must pass an allocator when the object may need to allocate memory
    rapidjson::Document::AllocatorType& allocator = document.GetAllocator();

    document.AddMember("Command", "GoToPosition", allocator);
    document.AddMember("transportId", 2, allocator);
    // create a rapidjson array type with similar syntax to std::vector
    rapidjson::Value array(rapidjson::kArrayType);
    // chain methods as rapidjson provides a fluent interface when modifying its objects
    array.PushBack("Yet", allocator).PushBack("Another", allocator).PushBack("Array",allocator);
    document.AddMember("array", array, allocator);

    // create a rapidjson object type
    rapidjson::Value object(rapidjson::kObjectType);
    object.AddMember("X", "50", allocator);
    object.AddMember("Y", "70", allocator);
    object.AddMember("X", "50", allocator);
    document.AddMember("Coords", object, allocator);
    //  fromScratch["object"]["hello"] = "Yourname";

    StringBuffer strbuf;
    Writer<StringBuffer> writer(strbuf);
    document.Accept(writer);
    string JsonData = strbuf.GetString();
    cout << JsonData << endl;
}