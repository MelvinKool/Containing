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
string JSONGenerator::craneTransferContainer(int craneId, int containerId, vector3f targetVect)
{
	//{'Command': 'craneMoveContainer', 'craneId': 8, 'containerId': 4, 'target': [51.25, 1.0, -73.5]}
	// document is the root of a json message
	rapidjson::Document document = createJSONDocument();
	// must pass an allocator when the object may need to allocate memory
	rapidjson::Document::AllocatorType& allocator = document.GetAllocator();
	document.AddMember("Command", "craneMoveContainer", allocator);
	document.AddMember("craneId", craneId, allocator);
	document.AddMember("containerId", containerId, allocator);
	rapidjson::Value target(rapidjson::kArrayType);
	target.PushBack(targetVect.getX(), allocator).PushBack(targetVect.getY(), allocator).PushBack(targetVect.getZ(),allocator);
	document.AddMember("target", target, allocator);
	return toString(document);
}

//Method for attaching a container to an AGV
string JSONGenerator::agvAttachContainer(int agvId, int containerId)
{
	//{'Command': 'agvAttachContainer', 'agvId': 90, 'containerId': 2}
	// document is the root of a json message
	rapidjson::Document document = createJSONDocument();
	// must pass an allocator when the object may need to allocate memory
	rapidjson::Document::AllocatorType& allocator = document.GetAllocator();
	document.AddMember("Command","agvAttachContainer", allocator);
	document.AddMember("agvId", agvId, allocator);
	document.AddMember("containerId", containerId, allocator);
	return toString(document);
}

//generates JSON for spawning an object
//templates???
//used for spawning trucks ships and tranes
/*string JSONGenerator::spawnObject(Transport& transport)//rotation???
{
	// {'cmdt': 2, 'cmd': {'Command': 'spawnTruck', 'position': [842.75, 0, 0], 'container': 2}},
	//{'cmdt': 15, 'cmd': {'Command': 'spawnTrain', 'containers': [i for i in range(5, 40)]}},
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
}*/

//JSON for crane
//PUT THOSE OBJECTS IN CRANE CLASS
//(int objectId, const char* vehicleType, vector3f coordinate, float maximumSpeed,
//float holderSpeed, float grabberSpeed, float grabber_y_offset, vector3f grabberPos, bool has_holder)
//vector3f rotation???
string JSONGenerator::spawnCrane(Crane& crane, int craneId,vector3f rotation)
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
	document.AddMember("id",craneId, allocator);
	Value s;
	s.SetString(crane.vehicleType.c_str(),strlen(crane.vehicleType.c_str()),allocator);    // can contain null character, length derived at compile time
	//s = vehicleType.c_str();
	document.AddMember("type", s ,allocator);
	document.AddMember("speed", crane.unloaded_Speed, allocator);
	rapidjson::Value spawnLocation(rapidjson::kArrayType);
	spawnLocation.PushBack(crane.currentLocation.getX(), allocator)
				.PushBack(crane.currentLocation.getY(), allocator)
				.PushBack(crane.currentLocation.getZ(),allocator);
	document.AddMember("position",spawnLocation,allocator);
	rapidjson::Value spawnRotation(rapidjson::kArrayType);
	spawnRotation.PushBack(rotation.getX(), allocator)
				.PushBack(rotation.getY(), allocator)
				.PushBack(rotation.getZ(),allocator);
	document.AddMember("rotation",spawnRotation,allocator);
	rapidjson::Value grabber(rapidjson::kObjectType);
	grabber.AddMember("holderSpeed",crane.currentGrabber.holderSpeed, allocator);
	//grabberPos
	rapidjson::Value grabberPositon(rapidjson::kArrayType);
	grabberPositon.PushBack(crane.currentGrabber.position.getX(), allocator)
				.PushBack(crane.currentGrabber.position.getY(), allocator)
				.PushBack(crane.currentGrabber.position.getZ(),allocator);
	grabber.AddMember("position", grabberPositon, allocator);
	grabber.AddMember("has_holder",crane.currentGrabber.has_holder, allocator);
	document.AddMember("grabber", grabber, allocator);
	return toString(document);
}


//JSON for crane
//PUT THOSE OBJECTS IN CRANE CLASS
//(int objectId, const char* vehicleType, vector3f coordinate, float maximumSpeed,
//float holderSpeed, float grabberSpeed, float grabber_y_offset, vector3f grabberPos, bool has_holder)
//vector3f rotation???
string JSONGenerator::spawnAGV(AGV& agv, int agvId, vector3f rotation)
{
	// document is the root of a json message
	rapidjson::Document document = createJSONDocument();
	// must pass an allocator when the object may need to allocate memory
	rapidjson::Document::AllocatorType& allocator = document.GetAllocator();
	document.AddMember("id",agvId, allocator);
	Value s;
	s.SetString("AGV",strlen("AGV"),allocator);    // can contain null character, length derived at compile time
	//s = vehicleType.c_str();
	document.AddMember("type", s ,allocator);
	document.AddMember("speed", agv.unloaded_Speed, allocator);
	rapidjson::Value spawnLocation(rapidjson::kObjectType);
	spawnLocation.AddMember("X",agv.currentLocation.getX(),allocator);
	spawnLocation.AddMember("Y",agv.currentLocation.getY(),allocator);
	spawnLocation.AddMember("Z",agv.currentLocation.getZ(),allocator);
	document.AddMember("position",spawnLocation,allocator);
	rapidjson::Value spawnRotation(rapidjson::kObjectType);
	spawnRotation.AddMember("X",rotation.getX(),allocator);
	spawnRotation.AddMember("Y",rotation.getY(),allocator);
	spawnRotation.AddMember("Z",rotation.getZ(),allocator);
	document.AddMember("rotation",spawnRotation,allocator);
	return toString(document);
}

//spawn multiple objects with one JSON string
std::string JSONGenerator::spawnObjects(std::vector<std::string>& spawnStrings)
{
	// document is the root of a json message
	rapidjson::Document document = createJSONDocument();
	// must pass an allocator when the object may need to allocate memory
	rapidjson::Document::AllocatorType& allocator = document.GetAllocator();
	document.AddMember("Command", "spawnObjects", allocator);
	rapidjson::Value spawnCommandList(rapidjson::kArrayType);
	for(string spawnString : spawnStrings)
	{
		Value s;
		const char* spawnString_c_str = spawnString.c_str();
		s.SetString(spawnString_c_str,strlen(spawnString_c_str),allocator);
		spawnCommandList.PushBack(s, allocator);
	}
	document.AddMember("objects", spawnCommandList, allocator);
	return toString(document);
}

//setCamera
//trainCam
//spawnTrain
//spawnTruck

//generates JSON for moving a vehicle
string JSONGenerator::moveTo(int vehicleId, vector<vector3f> coordinates, float totalDistance)
{
	// document is the root of a json message
	rapidjson::Document document = createJSONDocument();
	// must pass an allocator when the object may need to allocate memory
	rapidjson::Document::AllocatorType& allocator = document.GetAllocator();
	document.AddMember("Command", "moveAGV", allocator);
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
	document.AddMember("Command","containerCommands", allocator);
	document.AddMember("container", containerId, allocator);
	rapidjson::Value commandArray(rapidjson::kArrayType);
	for(string& s : commandList){
		Value commandStr;
		commandStr.SetString(s.c_str(),strlen(s.c_str()),allocator);
		commandArray.PushBack(commandStr, allocator);
	}
	document.AddMember("commands", commandArray, allocator);
	return toString(document);
}

/*std::string toCommandString(string commandString, int vehicleId){
	int bufSize = commandString.length() + 50;
	char buffer[bufSize];
	int strLen = snprintf(buffer,bufSize, "{'cmdt': %d, 'cmd': %s}");
	if(strLen > bufSize){
		//buffer overflow
		cout >> "buffer overflow" >> endl;
	}
	else{
		cout >> "success" >> endl;
	}
	string finalCommandString = str(buffer);
	return finalCommandString;
}*/
