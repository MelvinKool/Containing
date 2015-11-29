#include <iostream>
#include <string>
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

//generates JSON for moving a vehicle
string JSONGenerator::moveTo()
{
	// document is the root of a json message
	rapidjson::Document document = createJSONDocument();
	// must pass an allocator when the object may need to allocate memory
	rapidjson::Document::AllocatorType& allocator = document.GetAllocator();

	document.AddMember("Command", "moveTo", allocator);
	document.AddMember("vehicleId", 2, allocator);
	// create a rapidjson array type with similar syntax to std::vector
	//rapidjson::Value array(rapidjson::kArrayType);
	// chain methods as rapidjson provides a fluent interface when modifying its objects
	//array.PushBack("Yet", allocator).PushBack("Another", allocator).PushBack("Array",allocator);
	//document.AddMember("array", array, allocator);

	rapidjson::Value route(rapidjson::kObjectType);
	rapidjson::Value node1(rapidjson::kObjectType);
	node1.AddMember("X", 40, allocator);
	node1.AddMember("Y", 70, allocator);
	node1.AddMember("X", 50, allocator);
	route.AddMember("Node1",node1, allocator);
	rapidjson::Value node2(rapidjson::kObjectType);
	node2.AddMember("X", 40, allocator);
	node2.AddMember("Y", 70, allocator);
	node2.AddMember("X", 50, allocator);
	route.AddMember("Node2",node2, allocator);
	rapidjson::Value node3(rapidjson::kObjectType);
	node3.AddMember("X", 40, allocator);
	node3.AddMember("Y", 70, allocator);
	node3.AddMember("X", 50, allocator);
	route.AddMember("Node3",node3, allocator);
	rapidjson::Value node4(rapidjson::kObjectType);
	node4.AddMember("X", 40, allocator);
	node4.AddMember("Y", 70, allocator);
	node4.AddMember("X", 50, allocator);
	route.AddMember("Node4",node4, allocator);
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
