#include <iostream>
#include <string>
#include <vector>
#include "vector3f.h"
#include "JSONGenerator.h"

using namespace std;
using namespace rapidjson;

//creates a default json document
rapidjson::Document JSONGenerator::createJSONDocument()
{
	// document is the root of a json message
	rapidjson::Document document;
	// define the document as an object rather than an array
	document.SetObject();
	return document;
}

//generates JSON for doing a container transfer between two vehicles
string JSONGenerator::transferContainer(int containerId, int sourceId, int targetId)
{
	// document is the root of a json message
	rapidjson::Document document = createJSONDocument();
	// must pass an allocator when the object may need to allocate memory
	rapidjson::Document::AllocatorType& allocator = document.GetAllocator();
	return toString(&document);
}

//generates JSON for spawning an
string JSONGenerator::spawnObject(int objectId, vector3f coordinates)
{
	// document is the root of a json message
	rapidjson::Document document = createJSONDocument();
	// must pass an allocator when the object may need to allocate memory
	rapidjson::Document::AllocatorType& allocator = document.GetAllocator();
	return toString(&document);
}

//generates JSON for moving a vehicle
string JSONGenerator::moveTo(int vehicleId, vector<vector3f> coordinates)
{
	// document is the root of a json message
	rapidjson::Document document = createJSONDocument();
	// must pass an allocator when the object may need to allocate memory
	rapidjson::Document::AllocatorType& allocator = document.GetAllocator();

	document.AddMember("Command", "moveTo", allocator);
	document.AddMember("vehicleId", vehicleId, allocator);
	// create a rapidjson array type with similar syntax to std::vector
	// chain methods as rapidjson provides a fluent interface when modifying its objects
	rapidjson::Value route(rapidjson::kArrayType);
	for(vector3f coord: coordinates)
	{
		rapidjson::Value coordinate(rapidjson::kArrayType);
		coordinate.PushBack(coord.getX(), allocator).PushBack(coord.getY(), allocator).PushBack(coord.getZ(),allocator);
		route.PushBack(coordinate, allocator);
	}
	//add the route to the document
	document.AddMember("Route", route, allocator);
	//	fromScratch["object"]["hello"] = "Yourname";
	return toString(&document);
}

//converts json document to string
string JSONGenerator::toString(rapidjson::Document *document)
{
	StringBuffer strbuf;
	Writer<StringBuffer> writer(strbuf);
	document->Accept(writer);
	return strbuf.GetString();
}
