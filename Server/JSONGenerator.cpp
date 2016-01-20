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
	document.AddMember("Command", "transferContainer", allocator);
	document.AddMember("containerId", containerId, allocator);
	document.AddMember("sourceId", sourceId, allocator);
	document.AddMember("targetId", targetId, allocator);
	return toString(document);
}

//!!!!!!!!!
//generates JSON for spawning an object
string JSONGenerator::spawnObject(int objectId, const char* vehicleType, vector3f coordinate, vector3f rotation, float maximumSpeed)
{
	// document is the root of a json message
	rapidjson::Document document = createJSONDocument();
	// must pass an allocator when the object may need to allocate memory
	rapidjson::Document::AllocatorType& allocator = document.GetAllocator();
	document.AddMember("Command", "spawnObject", allocator);
	document.AddMember("objectId",objectId, allocator);
	Value s;
	s.SetString(vehicleType, strlen(vehicleType), allocator);    // can contain null character, length derived at compile time
	document.AddMember("vehicleType", s, allocator);
	document.AddMember("maximumSpeed", maximumSpeed, allocator);
	rapidjson::Value spawnLocation(rapidjson::kObjectType);
	spawnLocation.AddMember("X",coordinate.getX(),allocator);
	spawnLocation.AddMember("Y",coordinate.getY(),allocator);
	spawnLocation.AddMember("Z",coordinate.getZ(),allocator);
	document.AddMember("spawnLocation",spawnLocation,allocator);
	rapidjson::Value spawnRotation(rapidjson::kObjectType);
	spawnRotation.AddMember("X",coordinate.getX(),allocator);
	spawnRotation.AddMember("Y",coordinate.getY(),allocator);
	spawnRotation.AddMember("Z",coordinate.getZ(),allocator);
	document.AddMember("spawnRotation",spawnRotation,allocator);
	return toString(document);
}

//JSON for crane
string JSONGenerator::spawnObject(int objectId, const char* vehicleType, vector3f coordinate, vector3f rotation, float maximumSpeed,
										float holderSpeed, float grabberSpeed, float grabber_y_offset, vector3f grabberPos, bool has_holder)
{
	/*
	string objectStringTemplate =
	"{\
		\"grabber\": %s, \"id\":%d,\"position\":[%s],\"rotation\":[%s],\"speed\":%d,\"type\":%s\
	}";
	string templateGrabber = "{\
		\"has_holder\": %s,\
		\"holderSpeed\": %d,\
		\"position\": [%s],\
		\"speed\": %d,\
		\"y_offset\":%d\
	}";

	string object, grabber, final_string;
	sprintf(grabber ,templateGrabber,
			has_holder ? "true" : "false",
			holderSpeed,
			grabberPos.toString(),
			grabberSpeed,
			grabber_y_offset
		);
	sprintf(object,
			objectStringTemplate,
			grabber,
			objectId,
		 	coordinate.toString(),
			rotation.toString(),
			vehicleType
		);
	return object;
	*/
	// document is the root of a json message
	rapidjson::Document document = createJSONDocument();
	// must pass an allocator when the object may need to allocate memory
	rapidjson::Document::AllocatorType& allocator = document.GetAllocator();
	document.AddMember("Command", "spawnObject", allocator);
	document.AddMember("objectId",objectId, allocator);
	Value s;
	s.SetString(vehicleType,strlen(vehicleType),allocator);    // can contain null character, length derived at compile time
	//s = vehicleType.c_str();
	document.AddMember("vehicleType", s ,allocator);
	document.AddMember("maximumSpeed", maximumSpeed, allocator);
	rapidjson::Value spawnLocation(rapidjson::kObjectType);
	spawnLocation.AddMember("X",coordinate.getX(),allocator);
	spawnLocation.AddMember("Y",coordinate.getY(),allocator);
	spawnLocation.AddMember("Z",coordinate.getZ(),allocator);
	document.AddMember("spawnLocation",spawnLocation,allocator);
	rapidjson::Value spawnRotation(rapidjson::kObjectType);
	spawnRotation.AddMember("X",rotation.getX(),allocator);
	spawnRotation.AddMember("Y",rotation.getY(),allocator);
	spawnRotation.AddMember("Z",rotation.getZ(),allocator);
	document.AddMember("spawnRotation",spawnRotation,allocator);
	rapidjson::Value grabber(rapidjson::kObjectType);
	grabber.AddMember("holderSpeed",holderSpeed, allocator);
	grabber.AddMember("grabberSpeed",holderSpeed, allocator);
	grabber.AddMember("grabber_y_offset",holderSpeed, allocator);
	//grabberPos
	rapidjson::Value grabberPositon(rapidjson::kObjectType);
	grabberPositon.AddMember("X",grabberPos.getX(),allocator);
	grabberPositon.AddMember("Y",grabberPos.getY(),allocator);
	grabberPositon.AddMember("Z",grabberPos.getZ(),allocator);
	grabber.AddMember("grabberPos", grabberPositon, allocator);
	grabber.AddMember("has_holder",has_holder, allocator);
	document.AddMember("grabber", grabber, allocator);
	return toString(document);
}

//generates JSON for moving a vehicle
string JSONGenerator::moveTo(int vehicleId, vector<vector3f> coordinates, float totalDistance)
{
	// document is the root of a json message
	rapidjson::Document document = createJSONDocument();
	// must pass an allocator when the object may need to allocate memory
	rapidjson::Document::AllocatorType& allocator = document.GetAllocator();

	document.AddMember("Command", "moveTo", allocator);
	document.AddMember("vehicleId", vehicleId, allocator);
	// create a rapidjson array type with similar syntax to std::vector
	// chain methods as rapidjson provides a fluent interface when modifying its objects
	document.AddMember("totalDistance", totalDistance, allocator);
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
	return toString(document);
}

std::string JSONGenerator::generateCommandList(int containerId, vector<string>& commandList)
{
	// document is the root of a json message
	rapidjson::Document document = createJSONDocument();
	// must pass an allocator when the object may need to allocate memory
	rapidjson::Document::AllocatorType& allocator = document.GetAllocator();
	document.AddMember("Command","ContainerCommandList", allocator);
	document.AddMember("containerId", containerId, allocator);
	rapidjson::Value commandArray(rapidjson::kArrayType);
	for(string& s : commandList){
		Value commandStr;
		commandStr.SetString(s.c_str(),strlen(s.c_str()),allocator);
		commandArray.PushBack(commandStr, allocator);
	}
	document.AddMember("commandList", commandArray, allocator);
	return toString(document);
}
